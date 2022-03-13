package mumei.daiya;

import mumei.daiya.Commands.Daiyareset;
import mumei.daiya.Commands.Nouhu;
import mumei.daiya.Listeners.PlayerJoin;
import mumei.daiya.Listeners.SignClick;
import mumei.daiya.utils.CustomConfig;
import org.bukkit.plugin.java.JavaPlugin;


public final class Daiya extends JavaPlugin {
    private static JavaPlugin instance;
    public static SideBar sidebar;
    public static CustomConfig config;

    @Override
    public void onEnable() {
        config = new CustomConfig(this);
        config.saveDefault();
        getLogger().info("ダイヤ2022起動したお");
        instance = this;
        getCommand("nouhu").setExecutor(new Nouhu());
        getCommand("da").setExecutor(new Daiyareset());
        sidebar = new SideBar(Daiya.getInstance());

        // 看板クリッククールタイム
        new SignClick(this);

        registerListener(this);
        super.onEnable();

    }

    @Override
    public void onDisable() { getLogger().info("ダイヤ2022停止したお"); super.onDisable();}

    public static CustomConfig getCustomConfig()
    {
        return config;
    }

    public static JavaPlugin getInstance(){
        return instance;
    }

    private void registerListener(JavaPlugin plugin){
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoin(), plugin);
    }
}
