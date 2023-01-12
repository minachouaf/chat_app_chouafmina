package Finale;

import java.sql.Connection;
import java.sql.SQLException;

public class Connexion_db {
    private Connexion_db() {

    }
    private static Connexion_db connexion_db;
    private Connection connection;

    public static Connexion_db getInstance() {
        if (connexion_db == null) {
            connexion_db = new Connexion_db();
        }
        return connexion_db;
    }

    public void connectToDatabase() throws SQLException {
        String server = "localhost";
        String port = "3306";
        String database = "chat_application";
        String userName = "root";
        String password = "";
        connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database, userName, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


}
