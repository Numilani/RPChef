package me.numilani.rpchef;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class Ingredient {
  public String RecipeName = "";
  public Material ItemType;
  public List<String> LoreLines = new ArrayList<String>();

  public List<String> Adjectives = new ArrayList<String>();
  public List<String> DescriptiveSentences = new ArrayList<String>();

  public Ingredient(String name, String type){
    RecipeName = name;
    ItemType = Material.getMaterial(type);
  }

  public Ingredient(String name, String type, List<String> lore){
    RecipeName = name;
    ItemType = Material.getMaterial(type);
    LoreLines = lore;
  }
 
  public Ingredient(String name, String type, List<String> lore, List<String> adjs, List<String> descripts){
    RecipeName = name;
    ItemType = Material.getMaterial(type);
    LoreLines = lore;
    Adjectives = adjs;
    DescriptiveSentences = descripts;
  }

}
