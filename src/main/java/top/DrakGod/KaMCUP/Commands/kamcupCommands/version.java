package top.DrakGod.KaMCUP.Commands.kamcupCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import top.DrakGod.KaMCUP.*;

public class version implements kamcupCommand {
    @Override
    public kamcupCommand Init() {
        return this;
    }

    @Override
    public String Get_Command_Name() {
        return "version";
    }

    @Override
    public String Get_Description() {
        return "查看插件版本";
    }

    @Override
    public String Get_Usage() {
        return "/kamcup version";
    }

    @Override
    public List<String> On_TabComplete(Main Main, CommandSender Sender, String Label, String[] Args) {
        return new ArrayList<>();
    }

    @Override
    public boolean On_Command(Main Main, CommandSender Sender, String Label, String[] Args) {
        String Now_Version = Main.Class_CheckUpdate.Get_Now_Version();
        String New_Version = Main.Class_CheckUpdate.Get_New_Version();

        if (New_Version == null) {
            New_Version = "§c获取失败";
        }
        Sender.sendMessage("§6当前版本: " + Now_Version + " §b最新版本: " + New_Version);
        return true;
    }
}
