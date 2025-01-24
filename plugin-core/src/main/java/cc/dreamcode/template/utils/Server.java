package cc.dreamcode.template.utils;

public class Server {
    public String id;
    public String name;
    public String lore;

    public Server() {
    }

    public Server(String id, String name, String lore) {
        this.id = id;
        this.name = name;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public String getId() {
        return id;
    }
}
