package top.DrakGod.KaMCUP.Commands.kamcupCommands;

import org.bukkit.command.CommandSender;

import top.DrakGod.KaMCUP.*;
import top.DrakGod.KaMCUP.Commands.KaMCCommand;
import top.DrakGod.KaMCUP.Commands.kamcup;
import top.DrakGod.KaMCUP.Handlers.Commands;

public interface kamcupCommand extends KaMCCommand {
    public kamcupCommand Init();

    public String Get_Description();

    public String Get_Permission();

    public String Get_Usage();

    public default String Get_Full_Name() {
        return "kamcup " + Get_Command_Name();
    }

    public default boolean Call(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (!Sender.hasPermission(Get_Permission())) {
            Sender.sendMessage("§c你没有权限使用此命令");
            return true;
        }

        return On_Command(Main, Sender, Label, Args);
    }

    public default void Register_Command() {
        kamcupCommand Command = Init();
        String Command_Name = Get_Command_Name();

        Commands Class_Commands = Get_Main().Class_Commands;
        kamcup kamcup = (kamcup) Class_Commands.Command_Classes.get("kamcup");
        kamcup.Command_Classes.put(Command_Name, Command);
        kamcup.Command_Permissions.put(Command_Name, Get_Permission());
    };
}
