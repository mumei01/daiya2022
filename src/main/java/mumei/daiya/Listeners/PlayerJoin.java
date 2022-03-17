package mumei.daiya.Listeners;

import mumei.daiya.Daiya;
import mumei.daiya.SideBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoin implements Listener {
    private Plugin plugin = Daiya.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent p){
        Player player = p.getPlayer();
        p.setJoinMessage(ChatColor.GREEN+player.getDisplayName()+ChatColor.WHITE+"さんが参加しました。");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "bossbar set minecraft:scores players @a");
    }
}
