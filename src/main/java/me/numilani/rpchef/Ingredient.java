package me.numilani.rpchef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

public class Ingredient {
  public String name = "";
  public Material itemType;
  public String foodTypeTag;
  public List<String> loreLines = new ArrayList<String>();

  public List<String> adjectives = new ArrayList<String>();
  public List<String> descriptiveSentences = new ArrayList<String>();

  public Ingredient(String name, String type){
    this.name = name;
    this.itemType = Material.getMaterial(type);
  }

  public Ingredient(String name, String type, List<String> lore){
    this.name = name;
    this.itemType = Material.getMaterial(type);
    this.loreLines = lore;
  }
 
  public Ingredient(String name, String type, List<String> lore, List<String> adjs, List<String> descripts){
    this.name = name;
    this.itemType = Material.getMaterial(type);
    this.loreLines = lore;
    this.adjectives = adjs;
    this.descriptiveSentences = descripts;
 }

  public Ingredient(String name, String type, String foodTypeTag, List<String> lore, List<String> adjs, List<String> descripts){
    this.name = name;
    this.itemType = Material.getMaterial(type);
    this.foodTypeTag = foodTypeTag;
    this.loreLines = lore;
    this.adjectives = adjs;
    this.descriptiveSentences = descripts;
 }

  public Map<String, Object> toMap(){
    Map<String, Object> data = new HashMap<>();
    data.put("name", this.name);
    data.put("itemType", this.itemType.name());
    data.put("foodTypeTag", this.foodTypeTag);
    data.put("loreLines", this.loreLines);
    data.put("adjectives", this.adjectives);
    data.put("descriptiveSentences", this.descriptiveSentences);
    return data;
  }

  public static Ingredient fromMap(Map<String, Object> map){
    String name = (String) map.get("name");
    String itemType = (String) map.get("itemType");
    String foodTypeTag = (String) map.get("foodTypeTag");
    List<String> loreLines = new ArrayList<>((List<String>) map.get("loreLines"));
    List<String> adjectives = new ArrayList<>((List<String>) map.get("adjectives"));
    List<String> descriptiveSentences = new ArrayList<>((List<String>) map.get("descriptiveSentences"));
    return new Ingredient(name, itemType, foodTypeTag, loreLines, adjectives, descriptiveSentences);
  }

} 
