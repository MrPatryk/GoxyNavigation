package cc.dreamcode.template.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.annotation.*;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.template.TemplatePlugin;
import cc.dreamcode.template.config.MessageConfig;
import cc.dreamcode.template.config.PluginConfig;
import cc.dreamcode.template.inventory.Holder;
import cc.dreamcode.template.utils.ServerRegistry;
import cc.dreamcode.utilities.TimeUtil;
import com.google.gson.JsonObject;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cc.dreamcode.template.utils.RedisHandler.sendCommand;

@Command(name = "navigation", aliases = {"nav"})
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class NavigateCommand implements CommandBase {

    private final Holder totemMenuHolder;

    private final MessageConfig messageConfig;
    private final PluginConfig pluginConfig;
    private String[] list = ServerRegistry.STATIC_SERVER_LIST;
//    public NavigateCommand(StatefulRedisConnection redis){
//        this.redisPubSubConnection=redis;
//    }

    @Async
    @Permission("navigator.gui")
    @Executor(path = "gui", description = "Otwiera gui z serwerami.")
    @Sender(DreamSender.Type.CLIENT)
    BukkitNotice gui(Player sender) {
        totemMenuHolder.open(sender);
        return this.messageConfig.guiOpened;
    }

    @Async
    @Permission("navigator.send")
    @Executor(path = "send", description = "Wysyla komende do wskazanego serwera")
    BukkitNotice send(@Arg String serverId, @Args String[] args) {
        List<String> serverList = new ArrayList<>(Arrays.asList(ServerRegistry.STATIC_SERVER_LIST));
        serverList.add("all");
        if (!(serverList.contains(serverId))) {
            return this.messageConfig.commandServerInvalid.with("list", String.join(", ", ServerRegistry.STATIC_SERVER_LIST));
        }

        String command = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        JsonObject message = new JsonObject();
        message.addProperty("command", command);

        TemplatePlugin.getPlugin(TemplatePlugin.class).getLogger().info("Wysyłanie komendy do serwera " + serverId + ": " + command);
        try {

            sendCommand(message.toString(), serverId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.messageConfig.commandSent.with("sent", serverId + ": " + command);
    }

    @Async
    @Permission("navigator.reload")
    @Executor(path = "reload", description = "Przeladowuje konfiguracje.")
    BukkitNotice reload() {
        final long time = System.currentTimeMillis();

        try {
            this.messageConfig.load();
            this.pluginConfig.load();
            this.totemMenuHolder.update();
            return this.messageConfig.reloaded
                    .with("time", TimeUtil.format(System.currentTimeMillis() - time));
        } catch (NullPointerException | OkaeriException e) {
            e.printStackTrace();

            return this.messageConfig.reloadError
                    .with("error", e.getMessage());
        }
    }
}
