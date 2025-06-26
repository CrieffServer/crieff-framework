package com.crieff.framework.auth.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: aKuang
 * @time: 2023/11/21 16:14
 */
@Getter
@AllArgsConstructor
public enum Sex {
    FEMALE(0, "女"),
    MALE(1, "男"),
    ;

    private final int val;

    private final String desc;
}
