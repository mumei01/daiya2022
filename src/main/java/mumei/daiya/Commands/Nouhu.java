package mumei.daiya.Commands;

import mumei.daiya.Daiya;
import mumei.daiya.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;

public class Nouhu implements CommandExecutor {
    private JavaPlugin plugin = Daiya.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        //ダイヤモンド系の処理
        Inventory inv = player.getInventory();

        CustomConfig config = Daiya.getCustomConfig();




        if (inv.contains((Material.DIAMOND)) && (int)config.getConfiguration().get("sum_point")<= 2022 ){
            String a = inv.all(Material.DIAMOND).toString();
            String[] b = a.split(",");

            int sum = 0;


            for (int i = 0;i < b.length;i++){
                String c;
                if (i == 0){
                    c = b[i].split(" ")[2];
                }else {
                    c = b[i].split(" ")[3];
                }

                int d = Integer.parseInt(c.split("}")[0]);
                sum +=d;
            }

            Daiya.sidebar.addpoint(player, sum);
            return true;
        }else{
            player.sendMessage(ChatColor.AQUA + "インベントリにダイヤがありません。");
            return true;
        }
    }

}
