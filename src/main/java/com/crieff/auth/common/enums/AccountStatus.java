package com.crieff.auth.common.enums;

/**
 * @description:
 * @author: aKuang
 * @time: 2023/11/23 20:21
 */
public enum AccountStatus {
    NORMAL(0, "正常"),
    BAN(1, "封禁"),
    ABANDON(2, "销号"),
    ;

    private final int val;

    private final String desc;

    AccountStatus(int val, String desc) {
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
