package com.dear_tree.dear_tree.domain;

import com.dear_tree.dear_tree.exception.InvalidEnumValueException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Icon {
    SOCKS,
    WREATH,
    SNOWMAN,
    GIFT;

    @JsonCreator
    public static Icon fromValue(String value) {
        for (Icon icon : Icon.values()) {
            if (icon.name().equalsIgnoreCase(value)) {
                return icon;
            }
        }
        throw new InvalidEnumValueException("아이콘 Enum값 잘못됨(SOCKS, WREATH, SNOWMAN, GIFT 중 하나 입력해야 함): " + value);
    }
}
