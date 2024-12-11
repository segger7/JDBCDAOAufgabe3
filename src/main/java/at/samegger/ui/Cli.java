package at.samegger.ui;

import at.samegger.dataaccess.DatabaseException;
import at.samegger.dataaccess.MyCourseRepository;
import at.samegger.dataaccess.MySqlCourseRepository;
import at.samegger.domain.Course;

import java.util.List;
import java.util.Scanner;

public class Cli {

    Scanner scan;
    MyCourseRepository repo;

    public Cli(MyCourseRepository repo) {
        this.scan = new Scanner(System.in);
        this.repo = repo;
    }

    public void start() {
        String input = "-";
        while(!input.equals("x")) {
            showMenu();
            input = scan.nextLine();
            switch(input) {
                case "1":
                    System.out.println("Kurseingabe");
                    break;
                case "2":
                    System.out.println("Alle Kurse anzeigen");
                    showAllCourses();
                    break;
                case "x":
                    System.out.println("Auf Wiedersehen!");
                    break;
                default:
                    inputError();
                    break;
            }
        }
        scan.close();
    }

    private void showAllCourses() {
        List<Course> list = null;

        try {
            list = repo.getAll();
            if (!list.isEmpty()) {
                for (Course course : list) {
                    System.out.println(course);
                }
            } else {
                System.out.println("Kursliste leer!");
            }
        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: " + databaseException.getMessage());
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private void showMenu() {
        System.out.println("-------------KURSMANAGEMENT-------------");
        System.out.println("(1) Kurse eingeben \t (2) Alle Kurse anzeigen");
        System.out.println("(x) ENDE");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben1");
    }
}
