package me.numilani.rpchef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class BaseRecipe {
  public String name = "";
  public Material resultType;
  public List<String> descriptiveStarters = new ArrayList<String>();

  public Map<Ingredient, Integer> baseIngredients = new HashMap<Ingredient, Integer>();
  public Map<String, Integer> baseIngredientTags = new HashMap<String, Integer>();
  // public List<Ingredient> baseIngredients = new ArrayList<Ingredient>();
  public Map<Ingredient, Integer> additionalIngredients = new HashMap<Ingredient, Integer>();

  public BaseRecipe() {
  }

  public BaseRecipe(String name, String resultType, Map<Ingredient, Integer> baseIngredients) {
    this.name = name;
    this.resultType = Material.getMaterial(resultType);
    this.baseIngredients = baseIngredients;
  }

  public BaseRecipe(String name, String resultType, Map<Ingredient, Integer> baseIngredients,
      Map<Ingredient, Integer> addIngredients) {
    this.name = name;
    this.resultType = Material.getMaterial(resultType);
    this.baseIngredients = baseIngredients;
    this.additionalIngredients = addIngredients;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> data = new HashMap<>();
    
    List<String> baseIngredientsListified = new ArrayList<String>();
    for (var x : baseIngredients.entrySet()){
      baseIngredientsListified.add(String.format("%s:%d", x.getKey().name, x.getValue()));
    }

    List<String> baseIngredientTagsListified = new ArrayList<String>();
    for (var x : baseIngredientTags.entrySet()){
      baseIngredientTagsListified.add(String.format("%s:%d", x.getKey(), x.getValue()));
    }

    List<String> additionalIngredientsListified = new ArrayList<String>();
    for (var x : additionalIngredients.entrySet()){
      additionalIngredientsListified.add(String.format("%s:%d", x.getKey().name, x.getValue()));
    }

    data.put("name", this.name);
    data.put("resultType", this.resultType.name());
    data.put("descriptiveStarters", this.descriptiveStarters);
    data.put("baseIngredients", baseIngredientsListified);
    data.put("baseIngredientTags", baseIngredientTagsListified);
    data.put("additionalIngredients", additionalIngredientsListified);
    return data;
  }

  public static BaseRecipe fromMap(Map<String, Object> map, List<Ingredient> ingredientList) {
    BaseRecipe recipe = new BaseRecipe();
    recipe.name = (String) map.get("name");
    recipe.resultType = Material.getMaterial((String) map.get("resultType"));
    recipe.descriptiveStarters = new ArrayList<>((List<String>) map.get("descriptiveStarters"));

    var baseIngredientsList = new ArrayList<>((List<String>) map.get("baseIngredients"));
    var baseIngredientTagsList = new ArrayList<>((List<String>) map.get("baseIngredientTags"));
    var additionalIngredientsList = new ArrayList<>((List<String>) map.get("additionalIngredients"));

    for (var x : baseIngredientsList){
      String[] parts = x.split(":");
      // Bukkit.getLogger().info(parts[0]);
      // Bukkit.getLogger().info(parts[1]);
      // for (var y : ingredientList){
      //   Bukkit.getLogger().info(y.name);
      // }
      recipe.baseIngredients.put(ingredientList.stream().filter(y -> y.name.equals(parts[0])).findFirst().get(), Integer.parseInt(parts[1]));
    }
 
    for (var x : baseIngredientTagsList){
      String[] parts = x.split(":");
      recipe.baseIngredientTags.put(parts[0], Integer.parseInt(parts[1]));
    }
    
    for (var x : additionalIngredientsList){
      String[] parts = x.split(":");
      recipe.additionalIngredients.put(ingredientList.stream().filter(y -> y.name.equals(parts[0])).findFirst().get(), Integer.parseInt(parts[1]));
    }
    
    return recipe;
  }

}
