package mumei.daiya.Listeners;

import mumei.daiya.Daiya;
import mumei.daiya.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * 看板クリックにクールタイムを付与するリスナー
 * @author HIYU1576
 * @description CoolTimeSeconds 変数を編集することで、クールタイム時間を変更できます
 * @attention 正常動作を行うためには、看板の2行目に必ず「納付」という文字を含めてください。
 */
public class SignClick implements Listener {

    /**
     * インスタンス生成
     */
    private Plugin Plugin;

    public SignClick(Plugin plugin) {
        Plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, Plugin);
    }

    /**
     * クールタイム時間の設定
     */
    Integer CoolTimeSeconds = 60; // クールタイム時間(秒)

    /**
     * その他変数
     */
    private Long CoolTimeMilliSeconds =  CoolTimeSeconds * new Long(1000).longValue(); // クールタイム時間(ミリ秒)
    private HashMap<Player, Long> CoolTime = new HashMap(); // クールタイム時間保持用
    private HashSet<Player> PreventDoubleClick = new HashSet<Player>();

    /**
     * 看板イベントクリック
     */
    @EventHandler
    public void onSignClick(PlayerInteractEvent Event) {
        Player player = Event.getPlayer();
        Block clickedBlock = Event.getClickedBlock();
        Action action = Event.getAction();

        if (!PreventDoubleClick.contains(player)) {
            PreventDoubleClick.add(player);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Plugin, new Runnable() {
                @Override public void run() {
                    PreventDoubleClick.remove(player);
                }
            }, 20L);
        } else { return; }

        // 右クリックのみ
        if (action != Action.RIGHT_CLICK_BLOCK) { return; }

        // 看板のみ
        if (clickedBlock == null || !clickedBlock.getType().name().contains("SIGN")) return;

        // 納付看板のみ
        Sign signBlock = (Sign) clickedBlock.getState();
        if (!signBlock.getLine(1).contains("納付")) { return; }


        // 差分取得
        long nowTime = new Date().getTime();
        long prevTime = CoolTime.getOrDefault(player, nowTime);
        long diffTime = nowTime - prevTime;

        // クールタイムの状況
        boolean isCoolDown = diffTime != 0 && diffTime <= CoolTimeMilliSeconds;

        // クールタイムがある場合
        if (isCoolDown) { Event.setCancelled(true);

            // ミリ秒を秒へ変換
            double secondsCoolDown = (CoolTimeMilliSeconds - diffTime) / 1000.0;
            int secondsCoolDownCeil = (int) Math.ceil(secondsCoolDown);

            // 通知
            player.sendTitle("§c§l§nクールダウン中", "§f§lあと" + "§6§l" + secondsCoolDownCeil + "秒間§f§l待ってください", 0, 20, 20);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 0.0F);

        } else { // ない場合

            Bukkit.dispatchCommand(player, "nouhu");

            // 期間算出
            long periodTime = nowTime + CoolTimeSeconds;

            // クールタイム付与
            CoolTime.remove(player);
            CoolTime.put(player, periodTime);



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
            }else{
                player.sendMessage(ChatColor.AQUA + "インベントリにダイヤがありません。");
            }

        }




    }

}
