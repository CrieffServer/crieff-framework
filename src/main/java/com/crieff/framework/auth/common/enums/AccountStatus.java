package com.crieff.framework.auth.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: aKuang
 * @time: 2023/11/23 20:21
 */
@Getter
@AllArgsConstructor
public enum AccountStatus {
    UNKNOWN(-1, "未知"),
    ACTIVE(0, "正常"),
    BAN(1, "封禁"),
    ABANDON(2, "销号"),
    STOP(3, "停用"),
    ;

    private final int val;

    private final String desc;

    public static AccountStatus of(int val) {
        for (AccountStatus accountStatus : AccountStatus.values()) {
            if (accountStatus.getVal() == val) {
                return accountStatus;
            }
        }
        return UNKNOWN;
    }
}
