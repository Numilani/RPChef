package me.numilani.rpchef;

import com.bergerkiller.bukkit.common.config.FileConfiguration;

import me.numilani.rpchef.commands.CookingCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.meta.SimpleCommandMeta;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import java.util.ArrayList;
import java.util.List;

public final class RpChef extends JavaPlugin {

  // public CloudSimpleHandler cmdHandler = new CloudSimpleHandler();
  public LegacyPaperCommandManager<CommandSender> cmdHandler;
  public AnnotationParser<CommandSender> cmdParser;
  public FileConfiguration cfg;
  public List<Ingredient> ingredients = new ArrayList<Ingredient>();
  public List<BaseRecipe> baseRecipes = new ArrayList<BaseRecipe>();


  public static void Main(String[] args){
    System.out.println("test");
  }

  @Override
  public void onEnable() {

    try {
      cmdHandler = LegacyPaperCommandManager.createNative(this, ExecutionCoordinator.simpleCoordinator());
      cmdParser = new AnnotationParser<>(cmdHandler, CommandSender.class,
          parserParameters -> SimpleCommandMeta.empty());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // First run setup
    var isFirstRun = false;
    if (!(new FileConfiguration(this, "rpchef.yml").exists())) {
      isFirstRun = true;
      doPluginInit();
    }

    cfg = new FileConfiguration(this, "rpchef.yml");
    cfg.load();

    cmdParser.parse(new CookingCommandHandler(this));

    // Register events
    // getServer().getPluginManager().registerEvents(new FastRpChatListeners(this), this);

 }

  private void doPluginInit() {
    var cfgFile = new FileConfiguration(this, "rpchef.yml");
    cfgFile.set("allow-ingredient-registration-command", true);
    cfgFile.set("data.recipetypes.exampletype.suffix", "Monsterstew");
    List<String> reqIngredientList = new ArrayList<String>();
    reqIngredientList.add("Monster Meat");
    reqIngredientList.add("Gnats");
    reqIngredientList.add("Gross Broth");
    cfgFile.set("data.recipetypes.exampletype.requredIngredients", reqIngredientList);
    cfgFile.set("data.recipetypes.exampletype.result.type", "Carrot");
    cfgFile.set("data.recipetypes.exampletype.result.initiallore", "This disgusting abomination is a 'monsterstew'.");

    cfgFile.set("data.ingredients.Monster Meat.name", "Monster Meat");
    cfgFile.set("data.ingredients.Monster Meat.type", "ROTTEN_FLESH");
    
    cfgFile.set("data.ingredients.Gnats.name", "Gnats");
    cfgFile.set("data.ingredients.Gnats.type", "GUNPOWDER");

    List<String> brothLore = new ArrayList<String>();
    brothLore.add("A gross broth!");
    brothLore.add("It's mostly muddy water.");
    // cfgFile.set("data.ingredients.Gross Broth.name", "Gross Broth");
    // cfgFile.set("data.ingredients.Gross Broth.type", "WATER_BUCKET");
    // cfgFile.set("data.ingredients.Gross Broth.lore", brothLore);
    
    var broth = new Ingredient("Gross Broth", "WATER_BUCKET", brothLore);
    cfgFile.set("data.ingredients.Gross Broth", broth.toMap());

    cfgFile.saveSync();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
