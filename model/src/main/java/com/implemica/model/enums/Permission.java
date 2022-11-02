package com.implemica.model.enums;

public enum Permission {
    CARS_READ("cars:read"),
    CARS_DELETE("cars:delete"),
    CARS_UPDATE("cars:update"),
    CARS_CREATE("cars:create");

    private final String permission;

    Permission(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return this.permission;
    }
}
