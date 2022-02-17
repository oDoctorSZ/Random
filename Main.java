package com.spectro.main;

import com.spectro.main.commands.TPCommands;
import com.spectro.main.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {


    @Override
    public void onEnable() {
        Bukkit.getWorld(this.getConfig().getString("world"));
        getCommand("jogar").setExecutor(new TPCommands(this));
        TeleportUtils utils = new TeleportUtils(this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }
    public HashMap<UUID, Long> cooldown = new HashMap<>();

}
