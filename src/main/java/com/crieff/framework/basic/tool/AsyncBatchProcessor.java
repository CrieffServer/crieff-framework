/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.crieff.framework.basic.tool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 异步批处理器
 *
 * @author aKuang
 * @description
 * @date 2025/6/4 17:40
 */
@Slf4j
public class AsyncBatchProcessor<T> {

    // 线程安全队列
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();
    // 定时任务执行器
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    // 批量处理大小
    private final int batchSize;
    // 任务执行间隔（毫秒）
    private final long interval;

    private final BatchProcessor<T> processor;

    // 批量处理接口
    @FunctionalInterface
    public interface BatchProcessor<T> {
        void process(List<T> batch);
    }

    public AsyncBatchProcessor(int batchSize, long interval, TimeUnit unit, BatchProcessor<T> processor) {
        this.batchSize = batchSize;
        this.interval = unit.toMillis(interval);
        this.processor = processor;
        startScheduler();
    }

    // 启动定时任务
    private void startScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                List<T> dataList = new ArrayList<>(batchSize);
                // 批量拉取数据（阻塞直到获取到数据或超时）
                queue.drainTo(dataList, batchSize);

                if (!dataList.isEmpty()) {
                    log.info("AsyncQueueHandler scheduler pull data size: {}", batchSize);
                    processor.process(dataList);
                }
            } catch (Exception e) {
                // 异常处理
                log.error("AsyncQueueHandler scheduler error", e);
            }
        }, 0, interval, TimeUnit.MILLISECONDS);
    }

    // 添加数据到队列（阻塞）
    public void add(T item) throws InterruptedException {
        queue.put(item);
    }

    // 添加数据到队列（超时非阻塞）
    public boolean addWithTimeout(T item, long timeout, TimeUnit unit) throws InterruptedException {
        return queue.offer(item, timeout, unit);
    }

    // 关闭处理器（停止定时任务）
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
