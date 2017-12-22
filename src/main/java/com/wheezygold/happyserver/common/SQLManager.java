package com.wheezygold.happyserver.common;

import com.wheezygold.happyserver.account.HappyPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

@SuppressWarnings("FieldCanBeLocal")
public class SQLManager extends SmallPlugin {

    private String CREATE_A_TABLE = "CREATE TABLE IF NOT EXISTS `accounts` ( `id` INT(11) NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL , `name` VARCHAR(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL , `tokens` INT(11) NOT NULL DEFAULT '0' , `rank` VARCHAR(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT 'NONE' , UNIQUE (`id`)) ENGINE = InnoDB;";
    private String CREATE_USER = "INSERT INTO accounts (uuid, name) VALUES (?, ?);";
    private String UPDATE_NAME = "UPDATE accounts SET name = ? WHERE uuid = ?;";
    private String UPDATE_TOKENS = "UPDATE accounts SET tokens = ? WHERE uuid = ?;";
    private String UPDATE_RANK = "UPDATE accounts SET rank = ? WHERE uuid = ?;";

    private String CREATE_LINK_TABLE = "";

    public Connection connection;
    private String password;

    public SQLManager(JavaPlugin plugin) {
        super("SQL Manager", plugin);
        password = getPlugin().getConfig().getString("sqlPass");
        try {
            connect();
            connection.createStatement().executeUpdate(CREATE_A_TABLE);
        } catch (SQLException | ClassNotFoundException e) {
            log("Error while connecting to MySQL!");
        }
    }

    private void connect() throws SQLException, ClassNotFoundException {
        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                connection = null;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/coins", "root", password);
        }
    }

    private void connectH() {
        try {
            synchronized (this) {
                if (connection.isClosed()) {
                    connection = null;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/coins", "root", password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            log("While attempting to save our connection");
            e.printStackTrace();
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
            connectH();
            return false;
        }
    }

    public boolean isActive(String uuid) {
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM accounts WHERE uuid = '" + uuid + "';");
            return result.next();
        } catch (SQLException | NullPointerException e) {
            log("Error while loading user: " + e.getMessage());
            connectH();
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
            connectH();
            return null;
        }
    }

    public int getTokens(String uuid) {
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT tokens FROM accounts WHERE uuid = '" + uuid + "';");
            if (result.next())
                return result.getInt(1);
            return 0;
        } catch (SQLException e) {
            log("Error while loading user: " + e.getMessage());
            connectH();
            return 0;
        }
    }

    public void updateUser(HappyPlayer user) {
        try {
            PreparedStatement nS = connection.prepareStatement(UPDATE_NAME);
            nS.setString(1, user.getPlayerName());
            nS.setString(2, user.getUuid().toString());
            PreparedStatement tS = connection.prepareStatement(UPDATE_TOKENS);
            tS.setInt(1, user.getTokens());
            tS.setString(2, user.getUuid().toString());
            PreparedStatement rS = connection.prepareStatement(UPDATE_RANK);
            rS.setString(1, user.getRank().name());
            rS.setString(2, user.getUuid().toString());
            nS.execute();
            tS.execute();
            rS.execute();
        } catch (SQLException e) {
            connectH();
            e.printStackTrace();
        }
    }

}
