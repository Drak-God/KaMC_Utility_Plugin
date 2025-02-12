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
import top.DrakGod.KaMCUP.Commands.kamcupCommands.reload;
import top.DrakGod.KaMCUP.Commands.kamcupCommands.version;
import top.DrakGod.KaMCUP.Handlers.Commands;

public class kamcup implements KaMCCommand {
    public Commands Class_Commands;
    public HashMap<String, Command> Commands;
    public HashMap<String, KaMCCommand> Command_Classes;
    public HashMap<String, String> Command_Permissions;

    @Override
    public KaMCCommand Init() {
        Main Main = Get_Main();
        Class_Commands = Main.Class_Commands;
        Commands = Class_Commands.Commands;
        Command_Classes = Class_Commands.Command_Classes;
        Command_Permissions = Class_Commands.Command_Permissions;
        return this;
    }

    @Override
    public String Get_Command_Name() {
        return "kamcup";
    }

    @Override
    public List<String> On_TabComplete(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (Args.length == 1) {
            Set<String> Key_Set = new HashSet<String>(Command_Classes.keySet());
            Key_Set.remove("kamcup");

            return new ArrayList<>(Key_Set);
        } else if (Args.length > 1) {
            String[] New_Args = Arrays.copyOfRange(Args, 1, Args.length);
            String Sub_Command_Name = Args[0];
            Command Sub_Command = Commands.get(Sub_Command_Name);

            if (Sub_Command == null) {
                return new ArrayList<>();
            }

            KaMCCommand Sub_Command_Class = Command_Classes.get(Sub_Command_Name);
            return Sub_Command_Class.On_TabComplete(Main, Sender, Label, New_Args);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean On_Command(Main Main, CommandSender Sender, String Label, String[] Args) {
        String[] New_Args = Args;
        KaMCCommand Sub_Command = null;
        String Sub_Command_Name = "help";
        if (Args.length != 0) {
            Sub_Command_Name = Args[0];
            New_Args = Arrays.copyOfRange(Args, 1, Args.length);
            Sub_Command = Command_Classes.get(Sub_Command_Name);
        }

        if (Sub_Command == null) {
            Sender.sendMessage("§c子命令错误,显示帮助");
            Sub_Command = Command_Classes.get("help");
            New_Args = new String[0];
        }

        return Sub_Command.Call(Main, Sender, Sub_Command_Name, New_Args);
    }

    public void Register_Command() {
        KaMCCommand Command = Init();
        String Command_Name = Get_Command_Name();
        Get_Main().Class_Commands.Command_Classes.put(Command_Name, Command);
        Register_Sub_Command();
    }

    public void Register_Sub_Command() {
        new reload().Register_Command();
        new version().Register_Command();
    }
}
