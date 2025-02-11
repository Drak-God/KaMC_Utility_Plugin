package top.DrakGod.KaMCUP.Commands.kamcupCommands;

import org.bukkit.command.CommandSender;

import top.DrakGod.KaMCUP.*;
import top.DrakGod.KaMCUP.Commands.KaMCCommand;
import top.DrakGod.KaMCUP.Commands.kamcup;
import top.DrakGod.KaMCUP.Handlers.Commands;

public interface kamcupCommand extends KaMCCommand {
    public kamcupCommand Init();

    public String Get_Description();

    public String Get_Usage();

    public default boolean Call(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (!Sender.hasPermission("kamcup.commands.kamcup." + Get_Command_Name())) {
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
        kamcup.kamcup_Command_Classes.put(Command_Name, Command);
    };
}
