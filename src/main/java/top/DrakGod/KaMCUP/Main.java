package top.DrakGod.KaMCUP;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Global {
    public String Version;

    @Override
    public void onEnable() {
        Version = getDescription().getVersion();
        Plugin_Log("INFO", "§1Ka§4MC§aUtility§bPlugin§6 §1v" + Version + " §a正在加载...");

        Check_Data_Folder();
        if (!isEnabled()) {
            return;
        }

        Plugin_Log("INFO", "§1Ka§4MC§b实用插件§a已启用!");
    }

    @Override
    public void onDisable() {
        Plugin_Log("INFO", "§1Ka§4MC§b实用插件§4已禁用!");
    }

    public File Get_File() {
        return getFile();
    }

    public void Check_Data_Folder() {
        File Data_Folder = Get_Data_Folder();
        if (!Data_Folder.exists()) {
            Plugin_Log("WARN", "未检测到数据文件夹,正在初始化数据文件夹");
            Data_Folder.mkdirs();

            try {
                Copy_Data();
            } catch (IOException e) {
                Plugin_Log("ERROR", "数据文件夹初始化失败:" + e.toString());
                setEnabled(false);
                return;
            }
            Plugin_Log("INFO", "数据文件夹初始化成功");
        }
    }

    public void Copy_Data() throws IOException {
        try (JarFile Jar_File = new JarFile(Get_Main().Get_File())) {
            Enumeration<JarEntry> Entries = Jar_File.entries();
            File Data_Folder = Get_Data_Folder();

            while (Entries.hasMoreElements()) {
                JarEntry Entry = Entries.nextElement();
                String Entry_Name = Entry.getName();

                if (Entry_Name.startsWith("Data/") && !Entry.isDirectory()) {
                    InputStream Input_Stream = getClass().getClassLoader().getResourceAsStream(Entry_Name);
                    if (Input_Stream != null) {
                        File Target_File = new File(Data_Folder, Entry_Name.substring(5));

                        if (!Target_File.getParentFile().exists()) {
                            Target_File.getParentFile().mkdirs();
                        }
                        Files.copy(Input_Stream, Target_File.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            Jar_File.close();
        }
    }
}