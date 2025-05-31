package me.numilani.rpchef;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.numilani.rpchef.data.IDataSourceConnector;
import me.numilani.rpchef.data.SqliteDataSourceConnector;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.meta.SimpleCommandMeta;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import org.incendo.cloud.paper.PaperCommandManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class RpChef extends JavaPlugin {

  // public CloudSimpleHandler cmdHandler = new CloudSimpleHandler();
  public LegacyPaperCommandManager<CommandSender> cmdHandler;
  public AnnotationParser<CommandSender> cmdParser;
  public FileConfiguration cfg;
  public IDataSourceConnector dataSource;


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

    // do a check for datasourcetype once that's added to config
    // for now, just set datasource to sqlite always
    try {
      dataSource = new SqliteDataSourceConnector(this);
      if (isFirstRun)
        dataSource.initDatabase();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    // if
    // (cmdHandler.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION))
    // {
    // cmdHandler.registerAsynchronousCompletions();
    // }
    //
    // if (cmdHandler.queryCapability(CloudBukkitCapabilities.BRIGADIER)) {
    // cmdHandler.registerBrigadier();
    // }

    // cmdParser.parse(new ChatCommandHandler(this));

    // Register events
    // getServer().getPluginManager().registerEvents(new FastRpChatListeners(this), this);

    // Register commands
    // cmdHandler.enable(this);
    // cmdHandler.getParser().parse(new ChatCommandHandler(this));
 }

  private void doPluginInit() {
    var cfgFile = new FileConfiguration(this, "rpchef.yml");
    cfgFile.addHeader("This config file is unused. It will be populated in the future.");
    cfgFile.set("unusedValue", "");

    cfgFile.saveSync();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
