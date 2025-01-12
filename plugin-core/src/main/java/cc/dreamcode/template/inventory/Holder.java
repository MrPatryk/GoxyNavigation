package cc.dreamcode.template.inventory;

import cc.dreamcode.menu.adventure.base.BukkitMenu;
import cc.dreamcode.template.TemplatePlugin;
import cc.dreamcode.template.config.MessageConfig;
import cc.dreamcode.template.utils.Server;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import pl.goxy.minecraft.api.GoxyApi;

import java.util.List;
import java.util.Objects;

public class Holder {
    @Inject
    private TemplatePlugin templatePlugin;
    private BukkitMenu navMenu;
    List<Server> servers;
    public void update() {
        Utils navMenuSetup = this.templatePlugin.createInstance(Utils.class);
        navMenuSetup.reload();
        this.navMenu = navMenuSetup.getMenu();
        this.navMenu.setInventoryClickEvent(clickEvent -> {
            Player player= (Player) clickEvent.getWhoClicked();
            if(servers==null)servers= navMenuSetup.getPublicServers();
            String name=clickEvent.getCurrentItem().getItemMeta().getDisplayName();
            Server target=servers.stream().filter(server -> ChatColor.stripColor(server.getName().replace("\u0026", "§")).equalsIgnoreCase(ChatColor.stripColor(name))).findFirst().orElse(null);
            if(target==null){
                return;
            }
            Objects.requireNonNull(GoxyApi.getNetworkManager().getServer(target.getId())).connect(GoxyApi.getPlayerStorage().getPlayer(player.getUniqueId()));
        });
    }

    public void open(@NonNull HumanEntity humanEntity) {
        if (this.navMenu == null) this.update();
        Bukkit.getScheduler().runTask(templatePlugin, () -> this.navMenu.open(humanEntity));
    }
}
