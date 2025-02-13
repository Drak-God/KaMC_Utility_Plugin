package top.DrakGod.KaMCUP.Functions;

import java.math.BigDecimal;

import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import top.DrakGod.KaMCUP.Global;

public class PlayerMoney {
    public static Essentials Get_Essentials() {
        return Global.Get_Main_Static().Essentials;
    }

    public static User Get_User(Player Player) {
        return Get_Essentials().getUser(Player);
    }

    public static Double Get_Money(Player Player) {
        User User = Get_User(Player);
        return User.getMoney().doubleValue();
    }

    public static boolean Set_Money(Player Player, Double Money) {
        User User = Get_User(Player);
        try {
            User.setMoney(BigDecimal.valueOf(Money));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean Add_Money(Player Player, Double Money) {
        User User = Get_User(Player);
        try {
            User.setMoney(BigDecimal.valueOf(Get_Money(Player) + Money));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean Remove_Money(Player Player, Double Money) {
        User User = Get_User(Player);
        try {
            User.setMoney(BigDecimal.valueOf(Get_Money(Player) - Money));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}