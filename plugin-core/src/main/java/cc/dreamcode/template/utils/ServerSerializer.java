package cc.dreamcode.template.utils;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class ServerSerializer implements ObjectSerializer<Server> {

    @Override
    public boolean supports(@NonNull Class<? super Server> type) {
        return Server.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull Server object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("id", object.getId());
        data.add("name", object.getName());
        data.add("lore", object.getLore());
    }

    @Override
    public Server deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        String id = data.get("id", String.class);
        String name = data.get("name", String.class);
        String lore = data.get("lore", String.class);

        return new Server(id, name, lore);
    }
}