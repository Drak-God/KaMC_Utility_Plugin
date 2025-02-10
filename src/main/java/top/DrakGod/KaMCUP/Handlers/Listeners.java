package top.DrakGod.KaMCUP.Handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import top.DrakGod.KaMCUP.Global;

public class Listeners implements Listener, Global {
    public List<Supplier<Boolean>> Second_Update_Suppliers = new ArrayList<>();
    public BukkitRunnable Second_Update = new BukkitRunnable() {
        @Override
        public void run() {
            Iterator<Supplier<Boolean>> Iterator = Second_Update_Suppliers.iterator();
            while (Iterator.hasNext()) {
                Supplier<Boolean> Supplier = Iterator.next();
                boolean Out;
                String Error = "";

                try {
                    Out = Supplier.get();
                } catch (Exception e) {
                    Error = e.toString();
                    Out = false;
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

    public void Register_Event(Supplier<> Listener) {
        Second_Update_Suppliers.clear();
    }
}
