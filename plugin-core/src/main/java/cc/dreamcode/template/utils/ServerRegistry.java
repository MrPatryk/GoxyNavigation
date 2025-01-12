package cc.dreamcode.template.utils;

import pl.goxy.minecraft.api.GoxyApi;
import pl.goxy.minecraft.api.network.GoxyServer;

import java.util.*;
import java.util.stream.Stream;


public class ServerRegistry {
    public static final String[] STATIC_SERVER_LIST = loadServerList();
    private static String[] loadServerList() {
        return Arrays.stream(Objects.requireNonNull(GoxyApi.getNetworkManager().getContainer())
                                .getServers().stream()
                                .map(GoxyServer::getName)
                                .toArray(String[]::new)).toArray(String[]::new);
    }
}