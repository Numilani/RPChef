package me.numilani.rpchef.data;

import me.numilani.rpchef.RpChef;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteDataSourceConnector implements IDataSourceConnector {

    private RpChef plugin;
    private String dbFilename = "rpchef.db";
    public Connection conn;

    public SqliteDataSourceConnector(RpChef plugin) throws SQLException {
        this.plugin = plugin;
        conn = DriverManager.getConnection("jdbc:sqlite:" + new File(plugin.getDataFolder(), dbFilename).getPath());
    }

    public void ensureConnClosed() throws SQLException {
        if (!conn.isClosed()) {
            conn.close();
        }
    }

    public void initDatabase() throws SQLException {
        var statement = conn.createStatement();
        statement.execute("CREATE TABLE Settings (settingsName TEXT PRIMARY KEY, settingsValue TEXT)");
    }

}
