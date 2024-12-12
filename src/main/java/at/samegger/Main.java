package at.samegger;

import at.samegger.dataaccess.MySqlCourseRepository;
import at.samegger.dataaccess.MySqlStudentRepository;
import at.samegger.ui.Cli;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try {
            Cli mycli = new Cli(new MySqlCourseRepository(), new MySqlStudentRepository());
            mycli.start();
        } catch (SQLException e) {
            throw new RuntimeException("Datenbankfehler " + e.getMessage() + " SQL-State: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Datenbankfehler " + e.getMessage());
        }
    }

}