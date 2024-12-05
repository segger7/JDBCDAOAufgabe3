package at.samegger;

import at.samegger.ui.Cli;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        Cli mycli = new Cli();
        mycli.start();
        try {
            Connection myConnection = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/imstkurssystem","root","");
            System.out.println("Verbindung aufgebaut!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}