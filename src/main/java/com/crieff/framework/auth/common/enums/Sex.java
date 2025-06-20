package com.crieff.framework.auth.common.enums;

/**
 * @description:
 * @author: aKuang
 * @time: 2023/11/21 16:14
 */

public enum Sex {
    FEMALE(0, "女"),
    MALE(1, "男"),
    ;

    private final int val;

    private final String desc;

    Sex(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }
}
