package me.numilani.rpchef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

public class BaseRecipe {
  public String Name = "";
  public Material ResultType;
  public List<String> DescriptiveStarters = new ArrayList<String>();

  public List<Ingredient> BaseIngredients = new ArrayList<Ingredient>();
  public HashMap<Ingredient, Integer> AdditionalIngredients = new HashMap<Ingredient, Integer>(); 


}
