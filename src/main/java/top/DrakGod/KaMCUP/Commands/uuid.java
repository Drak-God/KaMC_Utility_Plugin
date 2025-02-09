package top.DrakGod.KaMCUP.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import top.DrakGod.KaMCUP.*;

public class uuid implements KaMCCommand {
    public KaMCCommand Init() {
        return this;
    }

    public String Get_Command_Name() {
        return "uuid";
    }

    public boolean On_Command(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (Args.length == 1) {
            Player player = Server.getPlayer(Args[0]);
            if (player == null) {
                Sender.sendMessage("§4玩家 " + Args[0] + " 不存在");
                return true;
            }

            Sender.sendMessage("玩家 " + player.getName() + " 的UUID是" + player.getUniqueId().toString());
        } else {
            Sender.sendMessage("§4命令用法:/uuid <Player>");
        }
        return true;
    }
}
