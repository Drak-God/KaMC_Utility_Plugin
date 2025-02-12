package top.DrakGod.KaMCUP.Commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;

import top.DrakGod.KaMCUP.*;

public interface KaMCCommand extends Global {
    public KaMCCommand Init();

    public String Get_Command_Name();

    public List<String> On_TabComplete(Main Main, CommandSender Sender, String Label, String[] Args);

    public boolean On_Command(Main Main, CommandSender Sender, String Label, String[] Args);

    public default boolean Call(Main Main, CommandSender Sender, String Label, String[] Args) {
        HashMap<String, String> Permissions = Main.Class_Commands.Command_Permissions;
        if (!Sender.hasPermission(Permissions.get(Get_Command_Name()))) {
            Sender.sendMessage("§c你没有权限使用此命令");
            return true;
        }

        return On_Command(Main, Sender, Label, Args);
    }

    public default void Register_Command() {
        KaMCCommand Command = this;
        String Command_Name = Get_Command_Name();
        Get_Main().Class_Commands.Command_Classes.put(Command_Name, Command);
    };
}
