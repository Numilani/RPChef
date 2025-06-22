package me.numilani.rpchef;

import com.bergerkiller.bukkit.common.config.FileConfiguration;

import me.numilani.rpchef.commands.CookingCommandHandler;
import me.numilani.rpchef.services.CookingService;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.meta.SimpleCommandMeta;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class RpChef extends JavaPlugin {

  // public CloudSimpleHandler cmdHandler = new CloudSimpleHandler();
  public LegacyPaperCommandManager<CommandSender> cmdHandler;
  public AnnotationParser<CommandSender> cmdParser;
  public FileConfiguration cfg;
  
  public List<Ingredient> ingredients = new ArrayList<Ingredient>();
  public List<BaseRecipe> recipes = new ArrayList<BaseRecipe>();

  public CookingService cookingService = new CookingService(this);

  private Logger log;

  @Override
  public void onEnable() {
    log = Bukkit.getLogger();
    log.info("Starting RpChef...");

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
      log.info("Generating fresh config file...");
      doPluginInit();
    }

    cfg = loadConfig();

    cmdParser.parse(new CookingCommandHandler(this));

    // Register events
    // getServer().getPluginManager().registerEvents(new FastRpChatListeners(this),
    // this);

    log.info("RpChef loading complete!");
  }

  private void doPluginInit() {
    var cfgFile = new FileConfiguration(this, "rpchef.yml");
    // set config flags
    cfgFile.set("allow-ingredient-registration-command", true);

    // add sample ingredients
    var broth = new Ingredient("Gross Broth", "WATER_BUCKET", List.of("A gross broth!", "It's mostly muddy water."), List.of("Murky", "Hazy"), List.of("The broth is full of mud and sand."));
    var monsterMeat = new Ingredient("Monster Meat", "ROTTEN_FLESH", List.of(), List.of("Stringy", "Foul"), List.of("The meat is rancid."));
    var gnats = new Ingredient("Gnats", "GUNPOWDER", List.of(), List.of("Gritty", "Chunky"), List.of("There are odd flecks of...something in here."));

    cfgFile.set("data.ingredients.Gross Broth", broth.toMap());
    cfgFile.set("data.ingredients.Monster Meat", monsterMeat.toMap());
    cfgFile.set("data.ingredients.Gnats", gnats.toMap());

    // add sample recipes
    var exampleRecipe = new BaseRecipe("Monster Stew", "SUSPICIOUS_STEW", Map.of(broth, 1, monsterMeat,1, gnats,1),
        Map.of());
    exampleRecipe.descriptiveStarters = List.of("It's a revolting stew.");

    cfgFile.set("data.recipes.examplerecipe", exampleRecipe.toMap());

    // baseRecipes.add(exampleRecipe);

    // save everything
    cfgFile.saveSync();
  }

  private FileConfiguration loadConfig() {
    var cfg = new FileConfiguration(this, "rpchef.yml");
    cfg.load();

    for (var node : cfg.getNodeList("data.ingredients")) {
      ingredients.add(Ingredient.fromMap(node.getValues()));
    }
    log.info(ingredients.size() + " ingredients loaded");

    for (var node : cfg.getNodeList("data.recipes")) {
      recipes.add(BaseRecipe.fromMap(node.getValues(), ingredients));
    }
    log.info(recipes.size() + " recipes loaded");

    return cfg;
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
