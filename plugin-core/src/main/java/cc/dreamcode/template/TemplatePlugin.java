package cc.dreamcode.template;

import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.menu.adventure.BukkitMenuProvider;
import cc.dreamcode.menu.adventure.serializer.MenuBuilderSerializer;
import cc.dreamcode.notice.bukkit.BukkitNoticeProvider;
import cc.dreamcode.notice.serializer.BukkitNoticeSerializer;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.ConfigurationResolver;
import cc.dreamcode.platform.bukkit.serializer.ItemMetaSerializer;
import cc.dreamcode.platform.bukkit.serializer.ItemStackSerializer;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.other.component.DreamCommandExtension;
import cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.template.command.NavigateCommand;
import cc.dreamcode.template.command.handler.InvalidInputHandlerImpl;
import cc.dreamcode.template.command.handler.InvalidPermissionHandlerImpl;
import cc.dreamcode.template.command.handler.InvalidSenderHandlerImpl;
import cc.dreamcode.template.command.handler.InvalidUsageHandlerImpl;
import cc.dreamcode.template.command.result.BukkitNoticeResolver;
import cc.dreamcode.template.config.MessageConfig;
import cc.dreamcode.template.config.PluginConfig;
import cc.dreamcode.template.inventory.Holder;
import cc.dreamcode.template.nms.api.VersionProvider;
import cc.dreamcode.template.utils.RedisHandler;
import cc.dreamcode.template.utils.Server;
import cc.dreamcode.template.utils.ServerSerializer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class TemplatePlugin extends DreamBukkitPlatform implements DreamBukkitConfig, DreamPersistence {

    @Getter
    private static TemplatePlugin instance;
    RedisHandler redisHandler;
    private String configUri;

    @Override
    public void load(@NonNull ComponentService componentService) {
        instance = this;
    }

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
            this.registerInjectable(pluginConfig.redisConfig);
            componentService.setDebug(pluginConfig.debug);
        });
        componentService.registerComponent(RedisHandler.class, redisHandler -> {
            this.redisHandler = redisHandler;
            this.redisHandler.init1();
        });
        componentService.registerComponent(Holder.class);
        componentService.registerComponent(NavigateCommand.class);
    }

    @Override
    public void disable() {
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
