package mumei.daiya.utils;

import mumei.daiya.Daiya;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Effects {

    /**
     * 指定ロケーションに花火を出現させます
     * @param loc
     */
    public static void effect(Location loc){

        Firework firework = loc.getWorld().spawn(loc, Firework.class);

        FireworkMeta meta = firework.getFireworkMeta();

        FireworkEffect.Builder effect = FireworkEffect.builder();

        effect.with(FireworkEffect.Type.BALL_LARGE);
        effect.withColor(Color.YELLOW);
        effect.withFade(Color.RED);
        effect.flicker(true);
        effect.trail(true);

        meta.addEffect(effect.build());

        meta.setPower(1);

        firework.setFireworkMeta(meta);

    }

    public static void uoo(Location loc){new TestRunnable(loc).runTaskTimer(Daiya.getInstance(),10,5);}

    public static void ustop(){ TestRunnable.check = true; }
}
