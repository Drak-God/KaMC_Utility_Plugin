package top.DrakGod.KaMCUP.Handlers;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import top.DrakGod.KaMCUP.*;
import top.DrakGod.KaMCUP.Commands.*;

@SuppressWarnings({ "unchecked", "deprecation" })
public class Commands implements CommandExecutor, Global {
    public HashMap<String, Command> Commands;
    public HashMap<String, String> Command_Permissions;
    public HashMap<String, KaMCCommand> Command_Classes;

    public Commands() {
        Commands = new HashMap<>();
        Command_Permissions = new HashMap<>();
        Main Main_Class = Get_Main();
        Main_Class.getDescription().getCommands().forEach((Name, Value) -> {
            PluginCommand Command = Main_Class.getCommand(Name);
            Command.setUsage((String) Value.get("usage"));
            Command.setAliases((List<String>) Value.get("aliases"));
            Command.setDescription((String) Value.get("description"));
            Command.setTabCompleter(Main_Class.Class_TabCompleters);
            Command.setPermission("");
            Command.setExecutor(this);

            Commands.put(Name, Command);
            Command_Permissions.put(Name, (String) Value.get("permission"));
        });
    }

    public void Register_Commands() {
        Command_Classes = new HashMap<>();
        new kamcup().Register_Command();
        new car().Register_Command();
        new help().Register_Command();
        new uuid().Register_Command();
    }

    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {
        String Command_Name = Command.getName();
        return Command_Classes.get(Command_Name).Call(Get_Main(), Sender, Label, Args);
    }
}
