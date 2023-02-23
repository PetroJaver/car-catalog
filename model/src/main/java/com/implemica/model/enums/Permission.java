package com.implemica.model.enums;

import lombok.AllArgsConstructor;

/**
 * Enum primary permissions for a User.
 */
@AllArgsConstructor
public enum  Permission {
    READ("read"),
    DELETE("delete"),
    UPDATE("update"),
    CREATE("create");

    /**
     * String representation of permission.
     */
    public final String permission;
}
