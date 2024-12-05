package at.samegger.ui;

import java.util.Scanner;

public class Cli {

    Scanner scan;

    public Cli() {
        this.scan = new Scanner(System.in);
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
                    System.out.println("Alle Kurse");
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

    private void showMenu() {
        System.out.println("-------------KURSMANAGEMENT-------------");
        System.out.println("(1) Kurse eingeben \t (2) Alle Kurse anzeigen");
        System.out.println("(x) ENDE");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben1");
    }
}
