package top.DrakGod.KaMCUP.Functions;

import com.google.gson.JsonParser;

import top.DrakGod.KaMCUP.Global;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public class CheckUpdate implements Global {
    public boolean Can_Check;
    public BukkitRunnable Check_Update = new BukkitRunnable() {
        @Override
        public void run() {
            String Now_Version = Get_Now_Version();
            String New_Version = Get_New_Version();

            if (New_Version == null) {
                return;
            }
            if (!Now_Version.equalsIgnoreCase(New_Version)) {
                Plugin_Log("WARN", "有新版本可用:" + New_Version + " 当前版本:" + Now_Version);
            }
        }
    };

    public CheckUpdate() {
        YamlConfiguration Config = Get_Config();
        Can_Check = Config.getBoolean("check_update");
        if (Can_Check) {
            Check_Update.runTaskTimer(Get_Main(), 0, 20 * 60 * 60 * 12);
        }
    }

    public void Unregister() {
        if (Can_Check) {
            Check_Update.cancel();
        }
    }

    public String Get_Now_Version() {
        return "v" + Get_Main().getDescription().getVersion().toString();
    }

    public String Get_New_Version() {
        String Response = HttpConnection
                .Get("https://api.github.com/repos/Drak-God/KaMC_Utility_Plugin/releases/latest");
        if (Response == null) {
            Plugin_Log("WARN", "无法获取最新版本");
            return null;
        }
        return JsonParser.parseString(Response).getAsJsonObject().get("name").getAsString();
    }
}