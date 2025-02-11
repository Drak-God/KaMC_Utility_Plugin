package top.DrakGod.KaMCUP.Commands.kamcupCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import top.DrakGod.KaMCUP.Main;

public class reload implements kamcupCommand {
    @Override
    public kamcupCommand Init() {
        return this;
    }

    @Override
    public String Get_Command_Name() {
        return "reload";
    }

    @Override
    public String Get_Description() {
        return "重载插件";
    }

    @Override
    public String Get_Usage() {
        return "/kamcup reload";
    }

    @Override
    public List<String> On_TabComplete(Main Main, CommandSender Sender, String Label, String[] Args) {
        return new ArrayList<>();
    }

    @Override
    public boolean On_Command(Main Main, CommandSender Sender, String Label, String[] Args) {
        try {
            Main.onDisable();
            Main.onEnable();
        } catch (Exception e) {
            Sender.sendMessage(Plugin_Name + "§c重载失败,请查看控制台");
            e.printStackTrace();
            return true;
        }
        Sender.sendMessage(Plugin_Name + "§a已重载");
        return true;
    }
}
