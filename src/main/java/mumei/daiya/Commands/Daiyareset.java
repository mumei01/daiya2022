package mumei.daiya.Commands;

import mumei.daiya.Daiya;
import mumei.daiya.utils.Effects;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Daiyareset implements CommandExecutor {

    Effects effect = new Effects(); // これ追加しました by hiyu かも。

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         Player player = sender.getServer().getPlayer(sender.getName());


        if (args.length != 0){
            if (sender.hasPermission("op")){
                switch (args[0]){
                    case "setting":
                        Daiya.sidebar.init();
                        break;
                    case "uooo":
                        effect.uoo(player.getLocation());
                        break;
                    case "ustop":
                        effect.ustop();
                        break;
                }
            }else{
                sender.sendMessage("権限がありません。");
            }

        }else{
            sender.sendMessage("コマンドミスってます。");
        }
        return true;
    }
}
