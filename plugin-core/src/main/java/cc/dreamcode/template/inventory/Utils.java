package cc.dreamcode.template.inventory;

import cc.dreamcode.menu.adventure.BukkitMenuBuilder;
import cc.dreamcode.menu.adventure.base.BukkitMenu;
import cc.dreamcode.template.config.PluginConfig;
import cc.dreamcode.template.utils.Server;
import cc.dreamcode.template.utils.ServerRegistry;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.injector.annotation.Inject;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    @Inject
    private PluginConfig pluginConfig;
    public BukkitMenuBuilder navMenu;
    private List<String> ignoreServers;
    @Getter
    private List<Server> publicServers;
    public void reload(){
        ignoreServers=this.pluginConfig.navMenuIgnoreServers;
        if (this.pluginConfig.navMenuSize <= 0) {
            throw new IllegalArgumentException("Menu nie moze miec rozmiaru mniejszego niz 1");
        }
        Map<Integer, ItemStack> items = new HashMap<>();
        ItemStack glassItem = new ItemBuilder(Objects.requireNonNull(XMaterial.GLASS_PANE.parseItem()).getType())
                .setName(" ").toItemStack();
        String[] filteredServerList=Arrays.stream(ServerRegistry.STATIC_SERVER_LIST).filter(name -> !ignoreServers.contains(name)).toArray(String[]::new);
        publicServers = Arrays.stream(filteredServerList)
                .map(name -> this.pluginConfig.customServers.stream()
                        .filter(aa -> aa.getId().equals(name))
                        .findAny()
                        .orElse(new Server(name,name, ""))
                )
                .collect(Collectors.toList());

        for (int i = 0; i < 9*this.pluginConfig.navMenuSize; i++) {
            items.put(i, glassItem);
        }
        if(!publicServers.isEmpty()) {
            int upDownCenter = (int) Math.floor((double) this.pluginConfig.navMenuSize / 2) * 9;
            int totalServers = publicServers.size();
            int spacing = (totalServers >= 4) ? 1 : 2;
            int totalWidth = (totalServers - 1) * spacing + totalServers;
            int startPosition = (9 - totalWidth) / 2;

            for (int i = 0; i < totalServers; i++) {
                int slot = upDownCenter + startPosition + (i * (1 + spacing));
                items.replace(slot, new ItemBuilder(Objects.requireNonNull(XMaterial.DIAMOND.parseItem()).getType())
                        .setName(publicServers.get(i).getName())
                        .setLore(publicServers.get(i).getLore())
                        .toItemStack());
            }
        }

        navMenu = new BukkitMenuBuilder(
                this.pluginConfig.navMenuName,
                this.pluginConfig.navMenuSize,
                items);
    }
    public BukkitMenu getMenu(){
        return navMenu.buildWithItems();
    }
}
