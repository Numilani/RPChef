package me.numilani.rpchef.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.annotations.Command;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;

import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.GuiStorageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.numilani.rpchef.RpChef;

public class CookingCommandHandler {
  private RpChef plugin;

  String redrawState = "default";
  public CookingCommandHandler(RpChef plugin) {
    this.plugin = plugin;
  }

  @Command("rpchef showgui")
  public void OpenCookingGui(CommandSender sender) {
    Player p = (Player) sender;
    Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);
    String[] guiLayout = {
        "rrr ooooo",
        "rar obbbo",
        "rar obbbo",
        "rar ooooo",
        "rrr      ",
        "    c ldl"
    };

    InventoryGui gui = new InventoryGui(plugin, null, "CookingUi", guiLayout);
    gui.setFiller(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1));

    Inventory definingItemsInventory = Bukkit.createInventory(null, InventoryType.CHEST);
    gui.addElement(new GuiStorageElement('a', definingItemsInventory));

    Inventory recipeItemsInventory = Bukkit.createInventory(null, InventoryType.CHEST);
    gui.addElement(new GuiStorageElement('b', recipeItemsInventory));

    Inventory resultInventory = Bukkit.createInventory(null, InventoryType.CHEST);
    gui.addElement(new GuiStorageElement('d', resultInventory));


    var stateBtn = new GuiStateElement('c', "default",
        new GuiStateElement.State(change -> {
          var baseItems = plugin.cookingService.getIngredientsFromItems(
              Arrays.asList(definingItemsInventory.getContents()));
          var additionalItems = plugin.cookingService.getIngredientsFromItems(
              Arrays.asList(recipeItemsInventory.getContents()));

          var recipe = plugin.cookingService.findRecipeFromIngredients(baseItems, additionalItems);
          if (!recipe.isEmpty()) {
            resultInventory.addItem(plugin.cookingService.createResult(recipe.get()));
            redrawState = "recipe_success";
          }
        else redrawState = "recipe_failure";

        }, "default", new ItemStack(Material.WHITE_WOOL, 1), "Cook Recipe"),
        new GuiStateElement.State(change -> {
          redrawState = "default";
        }, "recipe_success", new ItemStack(Material.GREEN_WOOL, 1), ""),
        new GuiStateElement.State(change -> {
        redrawState = "default";
        }, "recipe_failure", new ItemStack(Material.RED_WOOL, 1), ""));

    gui.addElement(stateBtn);

    gui.addElement(new StaticGuiElement('r', new ItemStack(Material.RED_STAINED_GLASS_PANE, 1)));
    gui.addElement(new StaticGuiElement('o', new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1)));
    gui.addElement(new StaticGuiElement('l', new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1)));

    // adds items back to player inventory on close
    gui.setCloseAction(close -> {
      redrawState = "default";
      gui.getElements().forEach(x -> {
        if (x instanceof GuiStorageElement) {
          ((GuiStorageElement) x).getStorage().forEach(y -> {
            if (y != null) {
              p.getInventory().addItem(y);
            }
          });
        }
      });
      return false;
    });

    var baseItems = plugin.cookingService.getIngredientsFromItems(
        Arrays.asList(definingItemsInventory.getContents()));
    var additionalItems = plugin.cookingService.getIngredientsFromItems(
        Arrays.asList(recipeItemsInventory.getContents()));

    var recipe = plugin.cookingService.findRecipeFromIngredients(baseItems, additionalItems);
    if (!recipe.isEmpty()) {
      resultInventory.addItem(plugin.cookingService.createResult(recipe.get()));
    }

    stateBtn.setState(redrawState);
    gui.show((HumanEntity) sender);
  }

  @Command("rpchef saveingredient")
  public void SaveIngredient(CommandSender sender) {
    Player p = (Player) sender;
    var item = p.getInventory().getItemInMainHand();
    String name;
    if (item.getItemMeta().hasDisplayName()) {
      name = item.getItemMeta().getDisplayName();
    } else {
      name = item.getType().name();
    }

    String check = plugin.cfg.get("data.ingredients." + name, "");
    if (!check.isEmpty()) {
      sender.sendMessage("That ingredient already exists in the config!");
      return;
    }

    plugin.cfg.set("data.ingredients." + name + ".name", name);
    plugin.cfg.set("data.ingredients." + name + ".type", item.getType().name());

    if (item.getItemMeta().hasLore()) {
      plugin.cfg.set("data.ingredients." + name + ".lore", item.getItemMeta().getLore());
    }

    plugin.cfg.save();

    sender.sendMessage("Ingredient added to config.");
  }

}
