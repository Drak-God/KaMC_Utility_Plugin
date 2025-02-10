package top.DrakGod.KaMCUP.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import top.DrakGod.KaMCUP.*;

public class car implements KaMCCommand {
    public HashMap<UUID, Integer> Car_Wait_Time;

    public boolean Update_Wait_Time() {
        for (UUID UUID : Car_Wait_Time.keySet()) {
            int Wait_Time = Car_Wait_Time.get(UUID);
            if (Wait_Time > 0) {
                Car_Wait_Time.put(UUID, Wait_Time - 1);
            }
        }
        return true;
    }

    @Override
    public KaMCCommand Init() {
        Car_Wait_Time = new HashMap<>();
        Get_Main().Class_Listeners.Register_Second_Update(this::Update_Wait_Time);
        return this;
    }

    @Override
    public String Get_Command_Name() {
        return "car";
    }

    @Override
    public List<String> On_TabComplete(Main Main, CommandSender Sender, String Label, String[] Args) {
        return new ArrayList<>();
    }

    @Override
    public boolean On_Command(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (Sender instanceof Player) {
            Player Player = (Player) Sender;
            int Wait_Time = Car_Wait_Time.getOrDefault(Player.getUniqueId(), 0);

            if (Wait_Time <= 0) {
                Player.addScoreboardTag("Cared");
                Player.getInventory().addItem(new ItemStack(Material.MINECART));

                Sender.sendMessage("§a已获得§d1§a个§d矿车");
                Car_Wait_Time.put(Player.getUniqueId(), 10);
            } else {
                Sender.sendMessage("§c冷却中，剩余§d" + Integer.toString(Wait_Time) + "秒");
            }
        } else {
            Sender.sendMessage("§4此命令仅可由玩家执行");
        }
        return true;
    }
}
