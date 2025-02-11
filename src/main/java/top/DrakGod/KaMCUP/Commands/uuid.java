package top.DrakGod.KaMCUP.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import top.DrakGod.KaMCUP.*;

public class uuid implements KaMCCommand {
    @Override
    public KaMCCommand Init() {
        return this;
    }

    @Override
    public String Get_Command_Name() {
        return "uuid";
    }

    @Override
    public List<String> On_TabComplete(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (Args.length == 1) {
            List<String> TabComplete = new ArrayList<>();
            for (OfflinePlayer player : Server.getOfflinePlayers()) {
                TabComplete.add(player.getName());
            }
            return TabComplete;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean On_Command(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (Args.length == 1) {
            OfflinePlayer Player = (OfflinePlayer) Server.getOfflinePlayer(Args[0]);
            if (Player == null) {
                Sender.sendMessage("§4玩家 " + Args[0] + " 不存在");
                return true;
            }

            Sender.sendMessage("玩家 " + Player.getName() + " 的UUID是" + Player.getUniqueId().toString());
        } else {
            Sender.sendMessage("§4命令用法:/uuid <Player>");
        }
        return true;
    }
}
