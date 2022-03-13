package mumei.daiya;

import mumei.daiya.utils.CustomConfig;
import mumei.daiya.utils.Effects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class SideBar {
    private static CustomConfig config = Daiya.getCustomConfig();
    //フィールド
    private JavaPlugin plugin;


    //コンストラクタ
    public SideBar(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    //スコアボードの初期化
    public void init() {
        config.getConfiguration().set("sum_point",0);
        config.save();
        //スコアボードの作成と設定
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "scoreboard objectives add score dummy \"§9納付ランキング\"");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "scoreboard objectives setdisplay sidebar score");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "scoreboard players set @a score 0");

        //ボスバーの作成と設定
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "bossbar add scores \"§f現在数 §e0/2020\"");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "bossbar set minecraft:scores name \"§f現在数 §e0/2020\"");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "bossbar set minecraft:scores max 2022");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "bossbar set minecraft:scores visible true");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "bossbar set minecraft:scores players @a");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "bossbar set minecraft:scores value 0");

    }

    public void addpoint(Player player, int amount){

        /*
        * 達成前のサイドバー処理
        *
        * */
        int at = amount;
        // 合計値の処理
        int now_sum = (int) config.getConfiguration().get("sum_point");
        config.getConfiguration().set("sum_point",now_sum+at);
        //納付処理
        player.sendTitle("§e§l" + at + "§e§l個" + "§f§lのダイヤを納付しました", null, 0, 40, 20); // by hiyu;
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F); // by hiyu
        Bukkit.broadcastMessage(ChatColor.BLUE+ player.getDisplayName()+"さんが"+ChatColor.WHITE+"ダイヤを"+ChatColor.YELLOW+at+"個"+ChatColor.WHITE+"納付しました");
        player.getInventory().removeItem(new ItemStack(Material.DIAMOND,at));
        //コンフィグとサイドバーの加算処理
        if (config.getConfiguration().contains(player.getDisplayName())){
            int player_now = (int)config.getConfiguration().get(player.getDisplayName());
            config.getConfiguration().set(player.getDisplayName(),player_now+at);
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    "scoreboard players add "+player.getDisplayName()+" score "+at);
        }else{
            config.getConfiguration().set(player.getDisplayName(),1);
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    "scoreboard players set "+player.getDisplayName()+" score "+at);
        }

        config.save();

        /*
        *   達成したらここで処理
        *
        * */
        if ((int)config.getConfiguration().get("sum_point") >= 2022){
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    "bossbar set minecraft:scores name \"§f現在数 §e2022/2022\"");
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    "bossbar set minecraft:scores value 2022");

            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    "advancement revoke @a only test:advancement");
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    "advancement grant @a only test:advancement");

            return;
        }
        /*
        *       達成前のボスバー処理
        * */
        //ボスバーの処理
        int rest = (int)config.getConfiguration().get("sum_point");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "bossbar set minecraft:scores name \"§f現在数 §e"+rest+"/2022\"");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                "bossbar set minecraft:scores value "+(int)config.getConfiguration().get("sum_point"));


        Effects.effect(player.getLocation());

    }



}
