package top.DrakGod.KaMCUP.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import top.DrakGod.KaMCUP.Main;
import top.DrakGod.KaMCUP.Handlers.Commands;

public class kamcup implements KaMCCommand {
    public Commands Class_Commands;
    public HashMap<String, Command> Commands;

    @Override
    public KaMCCommand Init() {
        Main Main = Get_Main();
        Class_Commands = Main.Class_Commands;
        Commands = Class_Commands.Commands;
        return this;
    }

    @Override
    public String Get_Command_Name() {
        return "kamcup";
    }

    @Override
    public List<String> On_TabComplete(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (Args.length == 1) {
            Set<String> Key_Set = new HashSet<String>(Commands.keySet());
            Key_Set.remove("kamcup");

            return new ArrayList<>(Key_Set);
        } else if (Args.length > 1) {
            String[] New_Args = Arrays.copyOfRange(Args, 1, Args.length);
            String SubCommand_Name = Args[0];
            Command SubCommand = Commands.getOrDefault(SubCommand_Name, Commands.get("kamcup"));

            if (SubCommand == Commands.get("kamcup")) {
                return new ArrayList<>();
            }

            KaMCCommand SubCommand_Class = Class_Commands.Command_Classes.get(SubCommand_Name);
            return SubCommand_Class.On_TabComplete(Main, Sender, Label, New_Args);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean On_Command(Main Main, CommandSender Sender, String Label, String[] Args) {
        String[] New_Args = Args;
        Command SubCommand = Commands.get("help");
        if (Args.length != 0) {
            String SubCommand_Name = Args[0];
            New_Args = Arrays.copyOfRange(Args, 1, Args.length);
            SubCommand = Commands.getOrDefault(SubCommand_Name, Commands.get("kamcup"));
        }

        if (SubCommand == Commands.get("kamcup")) {
            Sender.sendMessage("§c子命令错误,显示帮助");
            SubCommand = Commands.get("help");
            New_Args = new String[0];
        }
        return Class_Commands.onCommand(Sender, SubCommand, SubCommand.getLabel(), New_Args);
    }
}
