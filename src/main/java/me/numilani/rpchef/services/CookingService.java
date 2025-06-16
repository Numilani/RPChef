package me.numilani.rpchef.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.numilani.rpchef.BaseRecipe;
import me.numilani.rpchef.Ingredient;
import me.numilani.rpchef.RpChef;

public class CookingService {

  private RpChef plugin;

  public CookingService(RpChef plugin) {
    this.plugin = plugin;
  }

  public Map<Ingredient, Integer> getIngredientsFromItems(List<ItemStack> items) {

    Map<Ingredient, Integer> rv = Map.of();

    for (var i : items) {
      if (i == null) continue;
      String name;
      if (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
        name = i.getItemMeta().getDisplayName();
      } else {
        name = i.getType().name();
      }

      var filteredIngredients = plugin.ingredients.stream()
          .filter(x -> x.name.equals(name)
              && x.itemType.equals(i.getType()))
          .collect(Collectors.toList());

      if (filteredIngredients.size() > 1) {
        Bukkit.getLogger()
            .warning(String.format(
                "[RpChef] Found multiple matches for ingredient %s, check ingredient list for potential duplicates!",
                name));
      }

      if (filteredIngredients.size() >= 1) {
        rv.put(filteredIngredients.getFirst(), i.getAmount());
      }

    }
    return rv;
  }

  public Optional<BaseRecipe> findRecipeFromIngredients(Map<Ingredient, Integer> baseIngredients,
      Map<Ingredient, Integer> additionalIngredients) {
    var filteredRecipes = plugin.recipes.stream().filter(x -> x.baseIngredients.equals(baseIngredients))
        .collect(Collectors.toList());
    if (additionalIngredients.size() > 0) {
      filteredRecipes = filteredRecipes.stream().filter(x -> x.additionalIngredients.equals(additionalIngredients))
          .collect(Collectors.toList());
    }

    if (filteredRecipes.size() > 1) {
      Bukkit.getLogger().warning(
          "[RpChef] Found multiple matches when searching a recipe - this may be harmless, or your recipes may have ingredient lists that are too similar!");
    }

    if (filteredRecipes.size() > 0)
      return Optional.of(filteredRecipes.getFirst());
    else
      return Optional.empty();
  }

  public ItemStack createResult(BaseRecipe recipe) {
    List<String> adjectives = List.of();
    List<String> descriptiveSentences = List.of();

    for (var i : recipe.baseIngredients.entrySet()) {
      for (int x = i.getValue(); x > 0; x--) {
        var index = ThreadLocalRandom.current().nextInt(i.getKey().adjectives.size());
        adjectives.add(i.getKey().adjectives.get(index));
        var index2 = ThreadLocalRandom.current().nextInt(i.getKey().descriptiveSentences.size());
        descriptiveSentences.add(i.getKey().descriptiveSentences.get(index2));
      }
    }

    for (var i : recipe.additionalIngredients.entrySet()) {
      for (int x = i.getValue(); x > 0; x--) {
        var index = ThreadLocalRandom.current().nextInt(i.getKey().adjectives.size());
        adjectives.add(i.getKey().adjectives.get(index));
        var index2 = ThreadLocalRandom.current().nextInt(i.getKey().descriptiveSentences.size());
        descriptiveSentences.add(i.getKey().descriptiveSentences.get(index2));
      }
    }

    ItemStack rv = new ItemStack(recipe.resultType);
    rv.setAmount(1);

    ItemMeta meta = rv.getItemMeta();

    meta.setDisplayName(String.format("%s %s %s",
        adjectives.get(ThreadLocalRandom.current().nextInt(adjectives.size())),
        adjectives.get(ThreadLocalRandom.current().nextInt(adjectives.size())),
        recipe.name));

    meta.setLore(
        List.of(recipe.descriptiveStarters.get(ThreadLocalRandom.current().nextInt(recipe.descriptiveStarters.size())),
            descriptiveSentences.get(ThreadLocalRandom.current().nextInt(descriptiveSentences.size())),
            descriptiveSentences.get(ThreadLocalRandom.current().nextInt(descriptiveSentences.size()))));

    rv.setItemMeta(meta);

    return rv;
  }

}
