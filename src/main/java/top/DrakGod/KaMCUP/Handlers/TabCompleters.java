package top.DrakGod.KaMCUP.Handlers;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import top.DrakGod.KaMCUP.*;
import top.DrakGod.KaMCUP.Commands.KaMCCommand;

public class TabCompleters implements TabCompleter, Global {
    @Override
    public List<String> onTabComplete(CommandSender Sender, Command Command, String Label, String[] Args) {
        String Command_Name = Command.getName();
        Main Main = Get_Main();

        KaMCCommand Command_Class = Main.Class_Commands.Command_Classes.get(Command_Name);
        return Command_Class.On_TabComplete(Main, Sender, Label, Args);
    }
}