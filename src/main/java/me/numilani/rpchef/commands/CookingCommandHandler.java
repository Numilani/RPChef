package me.numilani.rpchef.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.annotations.Command;

import de.themoep.inventorygui.InventoryGui;
import me.numilani.rpchef.RpChef;

public class CookingCommandHandler {
    private RpChef plugin;

    public CookingCommandHandler(RpChef plugin) {
      this.plugin = plugin;
    }

  @Command("rpchef showgui")
   public void OpenCookingGui(CommandSender sender) {
    Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);
    String[] guiLayout = {
      "  a a a  ",
      "         ",
      " b b b b ",
      "b b b b b",
      "         ",
      "    c    "
    };
    InventoryGui gui = new InventoryGui(plugin, null, "CookingUi", guiLayout);
    gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS, 1));
    gui.show((HumanEntity) sender);
  } 

  }
