package cc.dreamcode.template.utils;

import cc.dreamcode.template.TemplatePlugin;
import cc.dreamcode.template.config.PluginConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.okaeri.injector.annotation.Inject;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import pl.goxy.minecraft.api.GoxyApi;

import java.util.UUID;
import java.util.logging.Level;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RedisHandler {
    public static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;
    private static StatefulRedisPubSubConnection<String, String> pubSubConnection;
    private final PluginConfig pluginConfig;

    public static void setUri(RedisURI redisURI) {
        redisClient = RedisClient.create(redisURI);
        connection = redisClient.connect();
        subscribeToRedisChannel(GoxyApi.getNetworkManager().getServer().getName());
    }

    public static void init(PluginConfig pluginConfig) {
        String clientName = GoxyApi.getConfiguration().getServerId() + "--" + UUID.randomUUID().toString();
        RedisURI redisUri = RedisURI.create("redis://" + pluginConfig.redisConfig.login + ":" + pluginConfig.redisConfig.password + "@" + pluginConfig.redisConfig.uri);
        redisUri.setClientName(clientName);
        setUri(redisUri);
    }

    public static void sendCommand(String command, String target) {
        try {
            if (target.equals("all")) {
                for (String serverTarget : ServerRegistry.STATIC_SERVER_LIST) {
                    pubSubConnection.async().publish(serverTarget, command);
                }
                return;
            }
            pubSubConnection.async().publish(target, command);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close() {
        if (pubSubConnection != null) pubSubConnection.close();
        if (connection != null) connection.close();
        if (redisClient != null) redisClient.close();
    }

    private static void subscribeToRedisChannel(String channelName) {
        if (pubSubConnection == null) {
            pubSubConnection = redisClient.connectPubSub();
        }
        pubSubConnection.addListener(new RedisPubSubListener<String, String>() {
            @Override
            public void message(String channel, String message) {
                Bukkit.getLogger().log(Level.INFO, "[Redis] Received: " + message);
                handleMessage(message);
            }

            @Override
            public void message(String pattern, String channel, String message) {
            }

            @Override
            public void subscribed(String channel, long count) {
                Bukkit.getLogger().log(Level.INFO, "[Redis] Subscribed to: " + channel);
            }

            @Override
            public void unsubscribed(String channel, long count) {
                Bukkit.getLogger().log(Level.INFO, "[Redis] Unsubscribed from: " + channel);
            }

            @Override
            public void psubscribed(String pattern, long count) {
            }

            @Override
            public void punsubscribed(String pattern, long count) {
            }
        });

        pubSubConnection.sync().subscribe(channelName);
    }

    private static void handleMessage(String message) {
        try {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            String command = json.get("command").getAsString();
            Bukkit.getScheduler().runTask(TemplatePlugin.getPlugin(TemplatePlugin.class), () -> {
                TemplatePlugin.getPlugin(TemplatePlugin.class).getLogger().info("Wykonywanie komendy z Redis: " + command);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            });
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Błąd podczas obsługi wiadomości Redis: " + e.getMessage());
        }
    }

    public void init1() {
        init(this.pluginConfig);
    }

}
