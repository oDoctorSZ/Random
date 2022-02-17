package com.spectro.main.utils;

import com.spectro.main.Main;
import java.util.HashSet;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;


public class TeleportUtils
{
        static Main plugin;

        public TeleportUtils(Main plugin) {
            TeleportUtils.plugin = plugin;
        }


    public static HashSet<Material> bblocks = new HashSet();

    static  {
        bblocks.add(Material.WATER_LILY);
        bblocks.add(Material.STATIONARY_WATER);
        bblocks.add(Material.LAVA);
        bblocks.add(Material.WATER);
        bblocks.add(Material.FIRE);
        bblocks.add(Material.STATIONARY_LAVA);
        bblocks.add(Material.LEAVES);
    }


    public Location generatorLocation() {
        Random random = new Random();

        int x = 0;
        int y = 0;
        int z = 0;

        if (plugin.getConfig().getBoolean("world-border")) {
            x = random.nextInt(plugin.getConfig().getInt("border"));
            y = 300;
            z = random.nextInt(plugin.getConfig().getInt("border"));
        } else if (!plugin.getConfig().getBoolean("world-border")) {
            x = random.nextInt(25000);
            y = 300;
            z = random.nextInt(25000);
        }

        Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("world")), x, y, z); double x1;

        double finalX = x;
        double finalY = y;
        double finalZ = z;

        new BukkitRunnable() {
            public void runTaskAsyncTimer(Main plugin, int i, int i1) {
            }

            final Location  loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("world")), finalX, finalY, finalZ);
             double x = loc.getX() - 96;
             double z = loc.getZ() - 96;

            @Override
            public void run(){
                double finalX = x;
                double finalZ = z;

                if(finalX > loc.getX()+96) this.cancel();
                double z1; for (z1 = (finalZ - 96); z1 <= (finalZ + 96); z1 += 16.0D) {
                    Location location = new Location(loc.getWorld(), finalX, 0.0D, finalZ);
                    try {
                        if (!location.getChunk().isLoaded())
                            location.getChunk().isLoaded();
                    } catch (Exception exception) {}
                }
            }
        }.runTaskAsyncTimer(plugin, 0, 20);

        y = loc.getWorld().getHighestBlockYAt(loc);
        loc.setY(y);

        while (!isLocationSafe(loc)) {
            loc = generatorLocation();
        }
        return loc;
    }



    public boolean isLocationSafe(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        return (!bblocks.contains(below.getType()) && !block.getType().isSolid() && !above.getType().isSolid());
    }
}