package com.spectro.main.commands;

import com.spectro.main.Main;
import com.spectro.main.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;




public class TPCommands
        implements CommandExecutor
{
    private Main main;

    public TPCommands(Main main) { this.main = main; }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final TeleportUtils teleport = new TeleportUtils(this.main);
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (this.main.cooldown.containsKey(player.getUniqueId()) && (this.main.cooldown.get(player.getUniqueId())) > System.currentTimeMillis()) {
                long longRemaning = (this.main.cooldown.get(player.getUniqueId())) - System.currentTimeMillis();
                int intRemaning = (int)(longRemaning / 7200);

                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + main.getConfig().getString("tag") + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Aguarde " + ChatColor.GOLD + intRemaning + ChatColor.GOLD + " segundos" + ChatColor.GRAY + " para usar esse comando novamente!");
                Bukkit.getScheduler().runTaskLater(main, () -> {
                    player.playSound(player.getLocation(), Sound.LEVEL_UP,0.5F,0.5F);
                    player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + main.getConfig().getString("tag") + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você já pode usar o comando " + ChatColor.GOLD + "/jogar" + ChatColor.GRAY + " novamente!");
                },144000);
            } else {
                this.main.cooldown.put(player.getUniqueId(), (System.currentTimeMillis() + (7200 * 7200)));


                if (args.length == 0) {
                    (new BukkitRunnable()
                    {
                        public void run() {
                            player.teleport(teleport.generatorLocation());
                        }
                    }).runTaskLater(this.main, 60L);


                    player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + main.getConfig().getString("tag") + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Teleportando para algum lugar aleatório da Ilha em " + ChatColor.GOLD + "3 segundos..." + ChatColor.GRAY + " Não se mova!");
                } else if (args.length == 1 &&
                        player.hasPermission("tpv.players")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    target.teleport(teleport.generatorLocation());

                    int x = target.getLocation().getBlockX();
                    int y = target.getLocation().getBlockY();
                    int z = target.getLocation().getBlockZ();

                    target.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + main.getConfig().getString("tag") + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você foi teleportado por " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " para um lugar aleatorio!");
                    player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + main.getConfig().getString("tag") + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você teleportou " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " para: " + ChatColor.GRAY + "X:" + ChatColor.GOLD + x + " " + ChatColor.GRAY + "Y:" + ChatColor.GOLD + y + " " + ChatColor.GRAY + "Z:" + ChatColor.GOLD + z);
                }
            }
        }

        return false;
    }
}


