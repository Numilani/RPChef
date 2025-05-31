package me.numilani.rpchef.data;

import me.numilani.rpchef.RpChef;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDataSourceConnector {

    RpChef plugin = null;
    Connection conn = null;

    public void ensureConnClosed() throws SQLException;
    public void initDatabase() throws SQLException;
}
