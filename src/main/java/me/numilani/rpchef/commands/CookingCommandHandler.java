package me.numilani.rpchef.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.suggestion.Suggestions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;

import de.themoep.inventorygui.DynamicGuiElement;
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
        "   x  ldl"
    };

    InventoryGui gui = new InventoryGui(plugin, null, "CookingUi", guiLayout);
    gui.setFiller(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1));

    Inventory definingItemsInventory = Bukkit.createInventory(null, InventoryType.CHEST);
    Inventory recipeItemsInventory = Bukkit.createInventory(null, InventoryType.CHEST);
    Inventory resultInventory = Bukkit.createInventory(null, InventoryType.CHEST);

    var resultGui = new GuiStorageElement('d', resultInventory);
    // resultGui.setPlaceValidator(x -> false);
    resultGui.setTakeValidator(x -> {
      for (var i : definingItemsInventory.getContents()) {
        if (i == null)
          continue;
        i.setAmount(i.getAmount() - 1);
      }
      for (var i : recipeItemsInventory.getContents()) {
        if (i == null)
          continue;
        i.setAmount(i.getAmount() - 1);
      }

      var baseItems = plugin.cookingService
          .getIngredientsFromItems(Arrays.asList(definingItemsInventory.getContents()));
      var additionalItems = plugin.cookingService
          .getIngredientsFromItems(Arrays.asList(recipeItemsInventory.getContents()));
      var recipe = plugin.cookingService.findRecipeFromIngredients(baseItems, additionalItems);

      // if (recipe.isEmpty()){
      // resultInventory.clear(0);
      // }

      gui.draw();
      return true;
    });

    gui.addElement(resultGui);

    var definingItemsGui = new GuiStorageElement('a', definingItemsInventory);
    // definingItemsGui.setPlaceValidator(x -> {
    // var thisInv = Bukkit.createInventory(null, InventoryType.CHEST);
    // thisInv.setContents(definingItemsInventory.getContents());
    // thisInv.addItem(x.getItem());
    //
    // var baseItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(thisInv.getContents()));
    // var additionalItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(recipeItemsInventory.getContents()));
    //
    // var recipe = plugin.cookingService.findRecipeFromIngredients(baseItems,
    // additionalItems);
    //
    // if (!recipe.isEmpty()) {
    // resultInventory.setItem(0, plugin.cookingService.createResult(recipe.get()));
    // } else {
    // resultInventory.clear();
    // }
    // gui.draw();
    // return true;
    // });
    // definingItemsGui.setTakeValidator(x -> {
    // var thisInv = Bukkit.createInventory(null, InventoryType.CHEST);
    // thisInv.setContents(definingItemsInventory.getContents());
    // var fail = thisInv.removeItem(x.getItem());
    //
    // var baseItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(thisInv.getContents()));
    // var additionalItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(recipeItemsInventory.getContents()));
    //
    // var recipe = plugin.cookingService.findRecipeFromIngredients(baseItems,
    // additionalItems);
    //
    // if (!recipe.isEmpty()) {
    // resultInventory.setItem(0, plugin.cookingService.createResult(recipe.get()));
    // } else {
    // resultInventory.clear();
    // }
    // gui.draw();
    // return true;
    // });
    gui.addElement(definingItemsGui);

    var recipeItemsGui = new GuiStorageElement('b', recipeItemsInventory);
    // recipeItemsGui.setPlaceValidator(x -> {
    // var baseItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(definingItemsInventory.getContents()));
    //
    // var thisInv = Bukkit.createInventory(null, InventoryType.CHEST);
    // thisInv.setContents(recipeItemsInventory.getContents());
    // thisInv.addItem(x.getItem());
    // var additionalItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(thisInv.getContents()));
    //
    // var recipe = plugin.cookingService.findRecipeFromIngredients(baseItems,
    // additionalItems);
    // if (!recipe.isEmpty()) {
    // resultInventory.setItem(0, plugin.cookingService.createResult(recipe.get()));
    // } else {
    // resultInventory.clear();
    // }
    // gui.draw();
    // return true;
    // });
    // recipeItemsGui.setTakeValidator(x -> {
    // var baseItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(definingItemsInventory.getContents()));
    //
    // var thisInv = Bukkit.createInventory(null, InventoryType.CHEST);
    // thisInv.setContents(recipeItemsInventory.getContents());
    // var fail = thisInv.removeItem(x.getItem());
    // var additionalItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(thisInv.getContents()));
    //
    // var recipe = plugin.cookingService.findRecipeFromIngredients(baseItems,
    // additionalItems);
    // if (!recipe.isEmpty()) {
    // resultInventory.setItem(0, plugin.cookingService.createResult(recipe.get()));
    // } else {
    // resultInventory.clear();
    // }
    // gui.draw();
    // return true;
    // });
    gui.addElement(recipeItemsGui);

    gui.addElement(new DynamicGuiElement('x', (viewer) -> {
      return new StaticGuiElement('x', new ItemStack(Material.WHITE_WOOL), click -> {
        var baseItems = plugin.cookingService.getIngredientsFromItems(
            Arrays.asList(definingItemsInventory.getContents()));
        var additionalItems = plugin.cookingService.getIngredientsFromItems(
            Arrays.asList(recipeItemsInventory.getContents()));

        var recipe = plugin.cookingService.findRecipeFromIngredients(baseItems, additionalItems);
        if (!recipe.isEmpty()) {
          for (var i : definingItemsInventory.getContents()) {
            if (i == null)
              continue;
            i.setAmount(i.getAmount() - 1);
          }
          for (var i : recipeItemsInventory.getContents()) {
            if (i == null)
              continue;
            i.setAmount(i.getAmount() - 1);
          }

          resultInventory.setItem(0, plugin.cookingService.createResult(recipe.get()));

        } else {
          resultInventory.clear();
        }

        click.getGui().draw();
        return true;
      }, "Cook Recipe");
    }));

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

    // var baseItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(definingItemsInventory.getContents()));
    // var additionalItems = plugin.cookingService.getIngredientsFromItems(
    // Arrays.asList(recipeItemsInventory.getContents()));
    //
    // var recipe = plugin.cookingService.findRecipeFromIngredients(baseItems,
    // additionalItems);
    // if (!recipe.isEmpty()) {
    // resultInventory.addItem(plugin.cookingService.createResult(recipe.get()));
    // }
    //
    // stateBtn.setState(redrawState);
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

  @Command("rpchef give ingredient <name>")
  public void GiveIngredient(CommandSender sender,
      @Argument(value = "name", suggestions = "suggestIngredient") @Greedy String name) {
    Player p = (Player) sender;

    var ingredient = plugin.ingredients.stream().filter(x -> x.name.equals(name)).findFirst();
    if (ingredient.isPresent()) {
      ItemStack i = new ItemStack(ingredient.get().itemType);
      var meta = i.getItemMeta();
      meta.setDisplayName(ingredient.get().name);
      meta.setLore(ingredient.get().loreLines);
      i.setItemMeta(meta);

      p.getInventory().addItem(i);
    } else {
      p.sendMessage(String.format("No ingredient %s could be found.", name));
    }
  }

  @Suggestions("suggestIngredient")
  public List<String> SuggestIngredient() {
    return plugin.ingredients.stream().map(x -> x.name).collect(Collectors.toList());
  }
}
