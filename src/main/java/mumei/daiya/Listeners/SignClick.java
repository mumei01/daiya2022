package mumei.daiya.Listeners;

import mumei.daiya.Daiya;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 看板クリックにクールタイムを付与するリスナー
 * * @author HIYU1576
 */
public class SignClick implements Listener {

    /**
     * プラグインインスタンス
     */
    private Plugin Plugin;

    public SignClick(Plugin plugin) {
        Plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, Plugin);
    }

    /**
     * クールタイム時間の設定
     */
    private Integer CoolTimeSeconds = 60; // クールタイム時間(秒)
    private Long CoolTimeMilliSeconds =  CoolTimeSeconds * new Long(1000).longValue(); // クールタイム時間(ミリ秒)

    /**
     * その他変数
     */
    private ArrayList<Player> PreventDoubleClick = new ArrayList<>(); // ダブルクリック防止用
    private HashMap<Player, Date> CoolTime = new HashMap(); // クールタイム時間保持用

    /**
     * 看板イベントクリック
     */
    @EventHandler
    public void onSignClick(PlayerInteractEvent Event) {
        Player player = Event.getPlayer();
        Block clickedBlock = Event.getClickedBlock();
        if (clickedBlock == null || !clickedBlock.getType().name().contains("SIGN")) return;

        // 納付看板のみ
        Sign signBlock = (Sign) clickedBlock.getState();
        if (!signBlock.getLine(1).contains("納付")) { return; }

        // ダブルクリック防止
        if (!PreventDoubleClick.contains(player)) {

            PreventDoubleClick.add(player);
            new BukkitRunnable() {

                @Override public void run() {

                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    PreventDoubleClick.remove(player);
                }
            }.runTaskAsynchronously(Plugin);

        } else { return; }


        // Dateインスタンス
        Date nowDate = new Date();
        Date timeDate = CoolTime.get(player);

        // 残り時間
        Long leftTime = nowDate.getTime() - timeDate.getTime();
        Boolean isOutDate = leftTime <= CoolTimeMilliSeconds;

        Bukkit.broadcastMessage("今DATE " + String.valueOf(nowDate.getTime()));
        Bukkit.broadcastMessage("昔DATE " + String.valueOf(timeDate.getTime()));

        Bukkit.broadcastMessage("[DEBUG] LONG TIME " + String.valueOf(leftTime));
        Bukkit.broadcastMessage("isOutDate : "+isOutDate.toString());
        // クールタイムがある場合
        if (!isOutDate) { Event.setCancelled(true);

            Bukkit.broadcastMessage("クールタイムある");

            // ミリ秒を秒へ変換
            double secondsCoolDown = leftTime.doubleValue() / 1000.0;

            // 通知
            player.sendTitle("§c§l§nクールタイム中", "§f§lあと" + "§6§l" + Math.floor(secondsCoolDown) + "秒§f§l間待ってください", 0, 20, 20);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 0.0F);

        } else { // ない場合
            Bukkit.broadcastMessage("クールタイムない");
            // クールタイム付与
            Date periodDate = new Date(timeDate.getTime() + CoolTimeSeconds);
            CoolTime.replace(player, periodDate);
        }


    }

}
