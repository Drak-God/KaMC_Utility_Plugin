package top.DrakGod.KaMCUP.Handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitRunnable;

import top.DrakGod.KaMCUP.Global;

@SuppressWarnings("unchecked")
public class Listeners implements Listener, Global {
    public List<Supplier<Boolean>> Second_Update_Suppliers = new ArrayList<>();
    public BukkitRunnable Second_Update = new BukkitRunnable() {
        @Override
        public void run() {
            Iterator<Supplier<Boolean>> Iterator = Second_Update_Suppliers.iterator();
            while (Iterator.hasNext()) {
                Supplier<Boolean> Supplier = Iterator.next();
                boolean Out = false;
                String Error = "";
                try {
                    Out = Supplier.get();
                } catch (Exception e) {
                    Error = e.toString();
                }

                if (!Out) {
                    Module_Log("WARN", "Second_Update", "运行" + Supplier.toString() + "时发生错误" + Error + ",已卸载");
                    Unregister_Second_Update(Supplier);
                }
            }
        }
    };

    public Listeners() {
        Second_Update.runTaskTimer(Get_Main(), 0, 20);
    }

    public void Unregister() {
        Second_Update_Suppliers.clear();
        Second_Update.cancel();
    }

    public void Register_Second_Update(Supplier<Boolean> Supplier) {
        Second_Update_Suppliers.add(Supplier);
    }

    public void Unregister_Second_Update(Supplier<Boolean> Supplier) {
        Second_Update_Suppliers.remove(Supplier);
    }

    public EventExecutor Get_Event_Executor(Consumer<? extends Event> Consumer) {
        return new EventExecutor() {
            @Override
            public void execute(Listener Listener, Event Event) {
                try {
                    ((Consumer<Event>) Consumer).accept(Event);
                } catch (Exception e) {
                    Module_Log("WARN", "EventExecutor.execute", "运行" + Consumer.toString() + "事件时发生错误" + e.toString());
                }
            }
        };
    }

    public void Register_Event(Class<? extends Event> Event_Type, EventExecutor Event_Executor) {
        Plugin_Manager.registerEvent(Event_Type, this, EventPriority.NORMAL, Event_Executor, Get_Main());
    }
}
