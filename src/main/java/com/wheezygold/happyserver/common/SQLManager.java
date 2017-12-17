package com.wheezygold.happyserver.common;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class SQLManager extends SmallPlugin {

    private String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `accounts` ( `id` INT(11) NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL , `name` VARCHAR(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL , `tokens` INT(11) NOT NULL DEFAULT '0' , `rank` VARCHAR(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT 'NONE' , UNIQUE (`id`)) ENGINE = InnoDB;";
    private String CREATE_USER = "INSERT INTO accounts (uuid, name) VALUES (?, ?);";

    private Connection connection;
    private String password;

    public SQLManager(JavaPlugin plugin) {
        super("SQL Manager", plugin);
        password = getPlugin().getConfig().getString("sqlPass");
        try {
            connect();
            connection.createStatement().executeUpdate(CREATE_TABLE);
        } catch (SQLException | ClassNotFoundException e) {
            log("Error while connecting to MySQL!");
        }
    }

    private void connect() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/coins", "root", password);
        }
    }

    public boolean createUser(String uuid, String name) {
        try {
            PreparedStatement statement = connection.prepareStatement(CREATE_USER);
            statement.setString(1, uuid);
            statement.setString(2, name);
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isActive(String uuid) {
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM accounts WHERE uuid = '" + uuid + "';");
            return result.next();
        } catch (SQLException | NullPointerException e) {
            log("Error while loading user: " + e.getMessage());
            return false;
        }
    }

    public String getRank(String uuid) {
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT rank FROM accounts WHERE uuid = '" + uuid + "';");
            if (result.next())
                return result.getString(1);
            return null;
        } catch (SQLException e) {
            log("Error while loading user: " + e.getMessage());
            return null;
        }
    }

}
