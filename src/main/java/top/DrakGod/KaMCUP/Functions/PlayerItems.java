package top.DrakGod.KaMCUP.Functions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerItems {
    public static Integer Get_Item_Count(Player Player, ItemStack ItemStack) {
        return Get_Item_Count(Player.getInventory().getContents(), ItemStack);
    }

    public static Integer Get_Item_Count(Player Player, Material Material) {
        return Get_Item_Count(Player.getInventory().getContents(), Material);
    }

    public static Integer Get_Item_Count(ItemStack[] Items, Material Material) {
        Integer Count = 0;
        for (ItemStack Item : Items) {
            if (Item != null && Item.getType() == Material) {
                Count += Item.getAmount();
            }
        }
        return Count;
    }

    public static Integer Get_Item_Count(ItemStack[] Items, ItemStack ItemStack) {
        Integer Count = 0;
        for (ItemStack Item : Items) {
            if (Item != null && Item.isSimilar(ItemStack)) {
                Count += Item.getAmount();
            }
        }
        return Count;
    }

    public static ItemStack[] Get_Items(Player Player, Material Material) {
        return Get_Items(Player.getInventory().getContents(), Material);
    }

    public static ItemStack[] Get_Items(Player Player, ItemStack ItemStack) {
        return Get_Items(Player.getInventory().getContents(), ItemStack);
    }

    public static ItemStack[] Get_Items(ItemStack[] ItemStacks, Material Material) {
        List<ItemStack> Items = new ArrayList<>();
        for (ItemStack Item : ItemStacks) {
            if (Item != null && Item.getType() == Material) {
                Items.add(Item);
            }
        }
        return Items.toArray(new ItemStack[0]);
    }

    public static ItemStack[] Get_Items(ItemStack[] ItemStacks, ItemStack ItemStack) {
        List<ItemStack> Items = new ArrayList<>();
        for (ItemStack Item : ItemStacks) {
            if (Item != null && Item.isSimilar(ItemStack)) {
                Items.add(Item);
            }
        }
        return Items.toArray(new ItemStack[0]);
    }

    public static Integer Get_Empty_Slot(Player Player) {
        return Get_Empty_Slot(Player.getInventory().getContents());
    }

    public static Integer Get_Empty_Slot(ItemStack[] Items) {
        Integer Count = 0;
        for (ItemStack Item : Items) {
            if (Item == null || Item.getType() == Material.AIR) {
                Count++;
            }
        }
        return Count;
    }

    public static ItemStack[] Create_Items(Player Player, ItemStack ItemStack, Integer Count) {
        List<ItemStack> Items = new ArrayList<>();
        Integer Max_Stack_Size = ItemStack.getMaxStackSize();
        while (Count > 0) {
            Integer Stack_Count = Math.min(Count, Max_Stack_Size);
            ItemStack.setAmount(Stack_Count);
            Items.add(ItemStack);
            Count -= Stack_Count;
        }
        return Items.toArray(new ItemStack[0]);
    }

    public static boolean Add_Item(Player Player, ItemStack ItemStack, Integer Count) {
        Integer Empty_Slot = Get_Empty_Slot(Player);
        ItemStack[] Items = Create_Items(Player, ItemStack, Count);
        if (Empty_Slot >= Items.length) {
            Player.getInventory().addItem(Items);
            return true;
        }
        return false;
    }

    public static boolean Add_Items(Player Player, ItemStack[] Items) {
        for (ItemStack Item : Items) {
            boolean Success = Add_Item(Player, Item, Item.getAmount());
            if (!Success) {
                return false;
            }
        }
        return true;
    }

    public static boolean Remove_Item(Player Player, ItemStack ItemStack, Integer Count) {
        Integer Item_Count = Get_Item_Count(Player, ItemStack);
        if (Item_Count >= Count) {
            ItemStack.setAmount(Count);
            Player.getInventory().removeItem(ItemStack);
            return true;
        }
        return false;
    }

    public static boolean Remove_Items(Player Player, ItemStack[] Items) {
        for (ItemStack Item : Items) {
            boolean Success = Remove_Item(Player, Item, Item.getAmount());
            if (!Success) {
                return false;
            }
        }
        return true;
    }
}
