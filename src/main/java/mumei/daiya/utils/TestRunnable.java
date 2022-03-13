package mumei.daiya.utils;


import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;

class TestRunnable extends BukkitRunnable {
    Random r = new Random();
    /**
     * 発射位置
     */
    private Location lc;

    /**
     * コンストラクタ
     *
     * @param player
     *            矢の発射の中心地点となるプレイヤー
     */
    public TestRunnable(Location _loc) {

        lc=_loc;


    }

    /**
     * タスク処理内容
     */
    Color[] colors = {Color.BLUE,Color.RED,Color.AQUA,Color.YELLOW,Color.GREEN};
    public static Boolean check = false;
    public void run() {
        int x = (int) (lc.getX()+r.nextInt(15));
        int z = (int) (lc.getZ()+r.nextInt(15));
        Location loc = lc.clone();
        loc.setX(x);
        loc.setZ(z);
        Firework firework = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta meta=firework.getFireworkMeta();
        FireworkEffect.Builder effect=FireworkEffect.builder();
        effect.withColor(colors[r.nextInt(4)]);
        meta.addEffect(effect.build());
        meta.setPower(1);
        firework.setFireworkMeta(meta);

        if (check){
            check = false;
            cancel();
        }

    }
}
