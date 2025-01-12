//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package cc.dreamcode.template.config;

import cc.dreamcode.platform.persistence.StorageType;
import cc.dreamcode.template.utils.StorageTypeAdjusted;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Comments;
import eu.okaeri.configs.annotation.CustomKey;
import lombok.Generated;
import lombok.NonNull;

public class RedisConfig extends OkaeriConfig {
    @Comment({"W jakiej formie maja byc zapisywane dane o graczu?", "Dostepne zapisy: (FLAT, REDIS)"})
    @CustomKey("storage-type")
    public StorageTypeAdjusted storageType;
    @Comment({"Jaki prefix ustawic dla danych?", "Dla FLAT prefix nie jest uzywany."})
    @CustomKey("prefix")
    public String prefix;
    @Comments({@Comment("Konfiguracja REDIS'a"),@Comment("Host:Port Redisa")})
    public String uri="";
    @Comment("Login do Redisa")
    @CustomKey("login")
    public String login="default";
    @Comment("Hasło do Redisa")
    @CustomKey("password")
    public String password="";

//    public RedisConfig() {
//        this.storageType = StorageTypeAdjusted.REDIS;
//        this.prefix = "dreamtemplate";
//        this.uri = "";
//    }
//
//    public RedisConfig(@NonNull String prefix) {
//        this.storageType = StorageTypeAdjusted.REDIS;
//        this.prefix = "dreamtemplate";
//        this.uri = "";
//        if (prefix == null) {
//            throw new NullPointerException("prefix is marked non-null but is null");
//        } else {
//            this.prefix = prefix;
//        }
//    }
//
//    public RedisConfig(String prefix, StorageTypeAdjusted storageType,String uri,String login, String password) {
//        this.uri = uri;
//        this.prefix = prefix;
//        this.storageType = storageType;
//        this.password = password;
//        this.login = login;
//    }
//
//    public RedisConfig(String prefix, StorageTypeAdjusted storageType) {
//        this.prefix = prefix;
//        this.storageType = storageType;
//    }

    public RedisConfig(@NonNull String prefix) {
        this.storageType = StorageTypeAdjusted.REDIS;
        this.prefix = "dreamtemplate";
        this.uri = "";
        if (prefix == null) {
            throw new NullPointerException("prefix is marked non-null but is null");
        } else {
            this.prefix = prefix;
        }
    }

    @Generated
    public RedisConfig() {
        this.storageType = StorageTypeAdjusted.REDIS;
        this.prefix = "dreamtemplate";
        this.uri = "";
    }

    @Generated
    public RedisConfig(final StorageTypeAdjusted storageType, final String prefix, final String uri) {
        this.storageType = StorageTypeAdjusted.REDIS;
        this.prefix = "dreamtemplate";
        this.uri = "";
        this.storageType = storageType;
        this.prefix = prefix;
        this.uri = uri;
    }



}
