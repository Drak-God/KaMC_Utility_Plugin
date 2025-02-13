package top.DrakGod.KaMCUP.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.kyori.adventure.text.Component;
import top.DrakGod.KaMCUP.*;
import top.DrakGod.KaMCUP.Functions.PlayerItems;
import top.DrakGod.KaMCUP.Functions.PlayerMoney;

public class bank implements KaMCCommand {
    public List<Double> Denominations;
    public NamespacedKey Coin_Denomination_Key;

    @Override
    public KaMCCommand Init() {
        Denominations = new ArrayList<>();
        for (int i : new int[] { 1, 2, 5 }) {
            for (int j = -3; j < 8; j++) {
                Denominations.add(i * Math.pow(10, j));
            }
        }
        Denominations.add(Math.pow(10, 8));
        Collections.sort(Denominations, Comparator.reverseOrder());

        Coin_Denomination_Key = new NamespacedKey(Get_Main(), "Coin_Denomination");
        return this;
    }

    @Override
    public String Get_Command_Name() {
        return "bank";
    }

    @Override
    public List<String> On_TabComplete(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (Args.length == 1) {
            return Arrays.asList("extract", "save");
        } else if (Args.length == 2) {
            if (Args[0].equalsIgnoreCase("extract")) {
                return Arrays.asList("all", "0.001");
            } else if (Args[0].equalsIgnoreCase("save")) {
                return Arrays.asList("all", "hand");
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean On_Command(Main Main, CommandSender Sender, String Label, String[] Args) {
        if (Sender instanceof Player) {
            Player Player = (Player) Sender;

            String Mode;
            String Option;
            if (Args.length == 1) {
                Mode = Args[0];
                Option = "all";
            } else if (Args.length == 2) {
                Mode = Args[0];
                Option = Args[1];
            } else {
                Sender.sendMessage("§c用法:/bank <extract|save> [all|hand|money]");
                return true;
            }

            if (Mode.equalsIgnoreCase("extract")) {
                double Money;
                if (Option.equalsIgnoreCase("all")) {
                    Money = PlayerMoney.Get_Money(Player);
                } else {
                    try {
                        Money = Double.parseDouble(Option);
                        if (Money <= 0) {
                            Sender.sendMessage("§c金额必须大于0");
                            return true;
                        }
                        if (Money > PlayerMoney.Get_Money(Player)) {
                            Sender.sendMessage("§c余额不足");
                            return true;
                        }
                    } catch (Exception e) {
                        Sender.sendMessage("§c\"" + Option + "\"不是一个有效的金额");
                        return true;
                    }
                }

                ItemStack[] Coins = Get_Coins(Money);
                boolean Result_Item = PlayerItems.Add_Items(Player, Coins);
                boolean Result_Money = PlayerMoney.Remove_Money(Player, Money);
                if (Result_Item && Result_Money) {
                    Sender.sendMessage("§a已取出" + String.valueOf(Money) + "K币");
                } else if (!Result_Item) {
                    Sender.sendMessage("§c背包剩余空间不足");
                    PlayerMoney.Add_Money(Player, Money);
                } else if (!Result_Money) {
                    Sender.sendMessage("§c金钱错误");
                    PlayerItems.Remove_Items(Player, Coins);
                }
                return true;
            } else if (Mode.equalsIgnoreCase("save")) {
                List<ItemStack> Items = new ArrayList<>();
                if (Option.equalsIgnoreCase("all")) {
                    ItemStack[] SunFlowers = PlayerItems.Get_Items(Player, Material.SUNFLOWER);
                    for (ItemStack SunFlower : SunFlowers) {
                        PersistentDataContainer SunFlower_Container = SunFlower.getItemMeta()
                                .getPersistentDataContainer();
                        if (SunFlower_Container.has(Coin_Denomination_Key, PersistentDataType.DOUBLE)) {
                            Items.add(SunFlower);
                        }
                    }
                } else if (Option.equalsIgnoreCase("hand")) {
                    ItemStack Hand = Player.getInventory().getItemInMainHand();
                    if (Hand.getType() == Material.SUNFLOWER) {
                        PersistentDataContainer Hand_Container = Hand.getItemMeta()
                                .getPersistentDataContainer();
                        if (Hand_Container.has(Coin_Denomination_Key, PersistentDataType.DOUBLE)) {
                            Items.add(Hand);
                        }
                    }
                }

                double Money = 0;
                for (ItemStack Item : Items) {
                    PersistentDataContainer Item_Container = Item.getItemMeta()
                            .getPersistentDataContainer();
                    double Denomination = Item_Container.get(Coin_Denomination_Key, PersistentDataType.DOUBLE);
                    Money += Denomination * Item.getAmount();
                }

                if (Money == 0) {
                    Sender.sendMessage("§c没有可以存入的K币");
                    return true;
                } else {
                    ItemStack[] Items_Array = Items.toArray(new ItemStack[0]);

                    boolean Result_Item = PlayerItems.Remove_Items(Player, Items_Array);
                    boolean Result_Money = PlayerMoney.Add_Money(Player, Money);
                    if (Result_Item && Result_Money) {
                        Sender.sendMessage("§a已存入" + String.valueOf(Money) + "K币");
                    } else if (!Result_Item) {
                        Sender.sendMessage("§c背包剩余物品不足");
                        PlayerMoney.Remove_Money(Player, Money);
                    } else if (!Result_Money) {
                        Sender.sendMessage("§c金钱错误");
                        PlayerItems.Add_Items(Player, Items_Array);
                    }
                    return true;
                }
            }
        } else {
            Sender.sendMessage("§c此命令仅可由玩家执行");
        }
        Sender.sendMessage("§c用法:/bank <extract|save> [all|hand|money]");
        return true;
    }

    public ItemStack[] Get_Coins(double Money) {
        List<ItemStack> Coins = new ArrayList<>();
        for (double Denomination : Denominations) {
            int Count = (int) (Money / Denomination);
            for (int i = 0; i < Count & 64 < Count; i += 64) {
                Coins.add(Create_Coin(Denomination, 64));
            }
            Coins.add(Create_Coin(Denomination, Count % 64));
            Money -= Denomination * Count;
        }
        return Coins.toArray(new ItemStack[0]);
    }

    public ItemStack Create_Coin(double Denomination, int Count) {
        ItemStack Coin = new ItemStack(Material.SUNFLOWER, Count);

        ItemMeta Coin_Meta = Coin.getItemMeta();
        Coin_Meta.displayName(Component.text("§e" + Denomination + "K币硬币"));
        Coin_Meta.lore(
                Arrays.asList(
                        Component.text("§7-§6价值" + Denomination + "K币§7的硬币"),
                        Component.text("§7-§b可在任何地方使用")));

        PersistentDataContainer Coin_Container = Coin_Meta.getPersistentDataContainer();
        Coin_Container.set(Coin_Denomination_Key, PersistentDataType.DOUBLE, Denomination);

        Coin.setItemMeta(Coin_Meta);
        return Coin;
    }
}
