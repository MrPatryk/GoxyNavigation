package cc.dreamcode.template.config;

import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.platform.persistence.StorageConfig;
import cc.dreamcode.template.utils.Server;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Comments;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration(child = "config.yml")
@Header("## Dream-Template (Main-Config) ##")
public class PluginConfig extends OkaeriConfig {

    @Comment
    @Comment("Debug pokazuje dodatkowe informacje do konsoli. Lepiej wylaczyc. :P")
    @CustomKey("debug")
    public boolean debug = true;

    @Comment
    @Comment("Ponizej znajduja sie dane do logowania bazy danych:")
    @CustomKey("redis-config")
    public RedisConfig redisConfig = new RedisConfig("dreamtemplate");

    public StorageConfig storageConfig = new StorageConfig("dreamtemplate");

    @Comments({@Comment("Ustawienia menu z wyborem serwera"), @Comment("Nazwa GUI")})
    @CustomKey("gui-config.name")
    public String navMenuName = "&c&lWybór serwera";

    @Comment("Rozmiar GUI")
    @CustomKey("gui-config.size")
    public int navMenuSize = 3;

    @Comment("Nazwy serwerow (id) goxy do ignorowania")
    @CustomKey("gui-config.ignore-servers")
    public List<String> navMenuIgnoreServers = Collections.singletonList("hub");

    @Comment("Customowe nazwy serwerow i ich opis")
    @CustomKey("gui-config.custom-servers")

    public List<Server> customServers = Arrays.asList(
            new Server("server1", "&bCustom nazwa", "&7Defaultowy opis serwera #1")
    );
}
