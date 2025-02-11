package top.DrakGod.KaMCUP.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;

import top.DrakGod.KaMCUP.*;
import top.DrakGod.KaMCUP.Handlers.Listeners;

@SuppressWarnings("deprecation")
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

    public void On_Player_Enter_Vehicle(VehicleEnterEvent Event) {
        if (Event.getVehicle().getType() == EntityType.MINECART) {
            Minecart Minecart = (Minecart) Event.getVehicle();
            Player Player = (Player) Event.getEntered();
            if (Player == null || !Player.getScoreboardTags().contains("Cared")) {
                return;
            }

            Minecart.setMaxSpeed(65536);
            Player.removeScoreboardTag("Cared");
            Player.sendTitle("§a欢迎乘坐§bKaMC§e快速列车", "§4如果被清除,请重新执行/car");
        }
    }

    public void On_Minecart_Move(VehicleMoveEvent Event) {
        if (!(Event.getVehicle() instanceof Minecart)) {
            return;
        }

        Minecart Minecart = (Minecart) Event.getVehicle();
        Player Player = (Player) Minecart.getPassenger();
        if (Player == null) {
            return;
        }

        Location From = Event.getFrom();
        Location To = Event.getTo();

        double Delta_X = To.getX() - From.getX();
        double Delta_Y = To.getY() - From.getY();
        double Delta_Z = To.getZ() - From.getZ();

        double Distance = Math.sqrt(Delta_X * Delta_X + Delta_Y * Delta_Y + Delta_Z * Delta_Z);
        if (Distance <= 0) {
            return;
        }

        Integer Int_Speed = (int) (Distance * 72.0);
        StringBuilder Out = new StringBuilder(Int_Speed.toString());
        while (Out.length() < 5) {
            Out.insert(0, " ");
        }

        Player.sendActionBar("当前速度:" + Out + " km/h");
    }

    @Override
    public KaMCCommand Init() {
        Car_Wait_Time = new HashMap<>();
        Listeners Class_Listeners = Get_Main().Class_Listeners;
        Class_Listeners.Register_Second_Update(this::Update_Wait_Time);

        EventExecutor On_Player_Enter_Vehicle = Class_Listeners
                .Get_Event_Executor((Consumer<VehicleEnterEvent>) this::On_Player_Enter_Vehicle);
        Class_Listeners.Register_Event(VehicleEnterEvent.class, On_Player_Enter_Vehicle);

        EventExecutor On_Minecart_Move = Class_Listeners
                .Get_Event_Executor((Consumer<VehicleMoveEvent>) this::On_Minecart_Move);
        Class_Listeners.Register_Event(VehicleMoveEvent.class, On_Minecart_Move);
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
