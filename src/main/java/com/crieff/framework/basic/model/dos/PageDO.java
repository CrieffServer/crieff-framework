/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.crieff.framework.basic.model.dos;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageDO<T> extends Page<T> {

    public PageDO(long current, long size) {
        super(current, size);
    }

    public static <T> PageDO<T> of(Integer page, Integer pageSize) {
        return new PageDO<>(page, pageSize);
    }

    public static <T> PageDO<T> of(Long total, List<T> list) {
        PageDO<T> pageDO = new PageDO<>();
        pageDO.setTotal(total);
        pageDO.setRecords(list);
        return pageDO;
    }

    public List<T> getList() {
        return getRecords();
    }
}

