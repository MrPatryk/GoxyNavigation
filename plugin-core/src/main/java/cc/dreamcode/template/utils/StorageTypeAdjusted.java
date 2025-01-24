package cc.dreamcode.template.utils;

import lombok.Generated;

public enum StorageTypeAdjusted {
    REDIS("Redis"),
    FLAT("Flat");

    private final String name;

    @Generated
    private StorageTypeAdjusted(final String name) {
        this.name = name;
    }

    @Generated
    public String getName() {
        return this.name;
    }
}