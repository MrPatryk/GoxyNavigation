package cc.dreamcode.template;

import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.menu.adventure.BukkitMenuProvider;
import cc.dreamcode.menu.adventure.serializer.MenuBuilderSerializer;
import cc.dreamcode.notice.bukkit.BukkitNoticeProvider;
import cc.dreamcode.notice.serializer.BukkitNoticeSerializer;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.ConfigurationResolver;
import cc.dreamcode.platform.bukkit.serializer.ItemMetaSerializer;
import cc.dreamcode.platform.bukkit.serializer.ItemStackSerializer;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.other.component.DreamCommandExtension;
import cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.platform.persistence.component.DocumentPersistenceResolver;
import cc.dreamcode.platform.persistence.component.DocumentRepositoryResolver;
import cc.dreamcode.template.command.ExampleCommand;
import cc.dreamcode.template.command.NavigateCommand;
import cc.dreamcode.template.command.handler.InvalidInputHandlerImpl;
import cc.dreamcode.template.command.handler.InvalidPermissionHandlerImpl;
import cc.dreamcode.template.command.handler.InvalidSenderHandlerImpl;
import cc.dreamcode.template.command.handler.InvalidUsageHandlerImpl;
import cc.dreamcode.template.command.result.BukkitNoticeResolver;
import cc.dreamcode.template.config.MessageConfig;
import cc.dreamcode.template.config.PluginConfig;
import cc.dreamcode.template.config.RedisConfig;
import cc.dreamcode.template.inventory.Holder;
import cc.dreamcode.template.nms.api.VersionProvider;
import cc.dreamcode.template.utils.RedisHandler;
import cc.dreamcode.template.utils.Server;
import cc.dreamcode.template.utils.ServerSerializer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import io.lettuce.core.RedisURI;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.goxy.minecraft.api.GoxyApi;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class TemplatePlugin extends DreamBukkitPlatform implements DreamBukkitConfig, DreamPersistence {

    @Getter private static TemplatePlugin instance;
    RedisHandler redisHandler;
    //private RedisDocumentPersistenceResolver redisDocumentPersistenceResolver;
    @Override
    public void load(@NonNull ComponentService componentService) {
        instance = this;
    }
    private String configUri;
    @Override
    public void enable(@NonNull ComponentService componentService) {
        componentService.setDebug(false);

        this.registerInjectable(BukkitTasker.newPool(this));
        this.registerInjectable(BukkitMenuProvider.create(this));
        this.registerInjectable(BukkitNoticeProvider.create(this));

        this.registerInjectable(BukkitCommandProvider.create(this));
        componentService.registerExtension(DreamCommandExtension.class);

        this.registerInjectable(VersionProvider.getVersionAccessor());

        componentService.registerResolver(ConfigurationResolver.class);
        componentService.registerComponent(MessageConfig.class);

        componentService.registerComponent(BukkitNoticeResolver.class);
        componentService.registerComponent(InvalidInputHandlerImpl.class);
        componentService.registerComponent(InvalidPermissionHandlerImpl.class);
        componentService.registerComponent(InvalidSenderHandlerImpl.class);
        componentService.registerComponent(InvalidUsageHandlerImpl.class);



            componentService.registerComponent(PluginConfig.class, pluginConfig -> {

                getLogger().info("====dream123");
                getLogger().info(pluginConfig.redisConfig.toString());
                // register persistence + repositories
                this.registerInjectable(pluginConfig.redisConfig);

                getLogger().info("====dream");
                getLogger().info(pluginConfig.redisConfig.toString());



              //  componentService.registerResolver(DocumentPersistenceResolver.class);
            //    componentService.registerComponent(DocumentPersistence.class);
            //    componentService.registerResolver(DocumentRepositoryResolver.class);

                //componentService.registerResolver(RedisDocumentPersistenceResolver.class);
                //componentService.registerComponent(DocumentPersistence.class);
                //componentService.registerResolver(DocumentRepositoryResolver.class);
                //this.redisDocumentPersistenceResolver = new RedisDocumentPersistenceResolver(DreamBukkitPlatform.getPlugin(TemplatePlugin.class),pluginConfig.redisConfig);
                // enable additional logs and debug messages

                componentService.setDebug(pluginConfig.debug);
            });
        componentService.registerComponent(ExampleCommand.class);
        componentService.registerComponent(RedisHandler.class, redisHandler -> {
            this.redisHandler=redisHandler;
            this.redisHandler.init1();
        });
       // NavigateCommand navigateCommand = new NavigateCommand(this.redisDocumentPersistenceResolver.getRedisConn());
       // componentService.registerComponent(StatefulRedisConnection.class);
        //this.registerInjectable("redisConn", redisDocumentPersistenceResolver.getRedisConn());
        //componentService.registerComponent(Holder.class);

        componentService.registerComponent(Holder.class);
        componentService.registerComponent(NavigateCommand.class);
    }
    public void setConfigUri(String configUri) {
        this.configUri = configUri;
    }

    public String getConfigUri() {
        return configUri;
    }
    @Override
    public void disable() {
        // features need to be call when server is stopping
            RedisHandler.close();
    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("Dream-Template", "1.0-InDEV", "author");
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {
            registry.register(new BukkitNoticeSerializer());
            registry.register(new MenuBuilderSerializer());
            registry.register(new ServerSerializer());
        };
    }

    @Override
    public @NonNull OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {
            registry.register(new SerdesBukkit());

            registry.registerExclusive(ItemStack.class, new ItemStackSerializer());
            registry.registerExclusive(ItemMeta.class, new ItemMetaSerializer());
            registry.registerExclusive(Server.class, new ServerSerializer());
        };
    }

}
