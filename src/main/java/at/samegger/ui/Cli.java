package at.samegger.ui;

import at.samegger.dataaccess.DatabaseException;
import at.samegger.dataaccess.MyCourseRepository;
import at.samegger.dataaccess.MyStudentRepository;
import at.samegger.domain.Course;
import at.samegger.domain.CourseType;
import at.samegger.domain.InvalidValueException;
import at.samegger.domain.Student;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Cli {

    Scanner scan;
    MyCourseRepository repoCourse;
    MyStudentRepository repoStudent;

    public Cli(MyCourseRepository repoCourse, MyStudentRepository repoStudent) {
        this.scan = new Scanner(System.in);
        this.repoCourse = repoCourse;
        this.repoStudent = repoStudent;
    }

    public void start() {
        String input = "-";
        while(!input.equals("x")) {
            showMenu();
            input = scan.nextLine();
            switch(input) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    showAllCourses();
                    break;
                case "3":
                    showCourseDetails();
                    break;
                case "4":
                    updateCourseDetails();
                    break;
                case "5":
                    deleteCourse();
                    break;
                case "6":
                    courseSearch();
                    break;
                case "7":
                    runningCourses();
                    break;
                case "8":
                    addStudent();
                    break;
                case "9":
                    showAllStudents();
                    break;
                case "10":
                    studentSearch();
                    break;
                case "11":
                    updateStudentDetails();
                    break;
                case "12":
                    deleteStudent();
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

    private void runningCourses() {
        System.out.println("Aktuell laufende Kurse: ");
        List<Course> list;
        try {
            list = repoCourse.findAllRunningCourses();
            for(Course course : list) {
                System.out.println(course);
            }

        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Kurs-Anzeige für laufende Kurse: " + databaseException.getMessage());
        } catch(Exception exception) {
            System.out.println("Unbekannter Fehler bei Kurs-Anzeige für laufende Kurse: " + exception);
        }
    }

    private void courseSearch() {
        System.out.println("Geben Sie einen Suchbegriff an:");
        String searchString  = scan.nextLine();
        List<Course> courseList;
        try {
            courseList = repoCourse.findAllCoursesByNameOrDescription(searchString);
            for(Course course : courseList) {
                System.out.println(course);
            }

        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
        } catch(Exception exception) {
            System.out.println("Unbekannter Fehler bei der Kurssuche: " + exception.getMessage());
        }
    }

    private void deleteCourse() {
        System.out.println("Welchen Kurs möchten Sie löschen? Bitte ID eingeben: ");
        Long courseIdToDelete = Long.parseLong(scan.nextLine());

        try{
            repoCourse.deleteByid(courseIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }
    }

    private void updateCourseDetails() {
        System.out.println("Für welche Kurs-ID möchten Sie die Kursdetails ändern?");
        Long courseId = Long.parseLong(scan.nextLine());

        try{
            Optional<Course> courseOptional = repoCourse.getById(courseId);
            if(courseOptional.isEmpty()) {
                System.out.println("Kurs mit der gegebenen ID nicht in der Datenbank!");
            } else {
                Course course = courseOptional.get();

                System.out.println("Änderungen für folgenden Kurs: ");
                System.out.println(course);

                String name, description, hours, dateFrom, dateTo, courseType;

                System.out.println("Bitte neue Kursdaten angeben (Enter, falls keine Änderung gewünscht ist): ");
                System.out.println("Name: ");
                name = scan.nextLine();
                System.out.println("Beschreibung: ");
                description = scan.nextLine();
                System.out.println("Stunden: ");
                hours = scan.nextLine();
                System.out.println("Begindatum (YYYY-MM-DD): ");
                dateFrom = scan.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD): ");
                dateTo = scan.nextLine();
                System.out.println("Kurstyp (ZA/BF/FF/OE): ");
                courseType = scan.nextLine();

                Optional<Course> optionalCourseUpdated = repoCourse.update(
                        new Course(
                                course.getId(),
                                name.equals("") ? course.getName() : name,
                                description.equals("") ? course.getDescription() : description,
                                hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                                dateFrom.equals("") ? course.getBeginDate() : Date.valueOf(dateFrom),
                                dateTo.equals("") ? course.getEndDate() : Date.valueOf(dateTo),
                                courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                        )
                ) ;

                optionalCourseUpdated.ifPresentOrElse(
                        (c)-> System.out.println("Kurs aktualisiert: " + c),
                        ()-> System.out.println("Kurs konnte nicht aktualisiert werden!")
                );
            }

        } catch(IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch(InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch(Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen:" + exception.getMessage());
        }
    }

    private void addCourse() {
        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseType courseType;

        try {
            System.out.println("Bitte alle Kursdaten angeben: ");
            System.out.println("Name: ");
            name = scan.nextLine();
            if(name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Beschreibung: ");
            description = scan.nextLine();
            if(description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Stundenanzahl: ");
            hours = Integer.parseInt(scan.nextLine());
            System.out.println("Startdatum (YYYY-MM-DD)");
            dateFrom = Date.valueOf(scan.nextLine());
            System.out.println("Startdatum (YYYY-MM-DD)");
            dateTo = Date.valueOf(scan.nextLine());
            System.out.println("Kurstyp: (ZA/BF/FF/OE)");
            courseType = CourseType.valueOf(scan.nextLine());

            Optional<Course> optionalCourse = repoCourse.insert(new Course(name, description, hours, dateFrom, dateTo, courseType));

            if(optionalCourse.isPresent()) {
                System.out.println("Kurs angelegt: " + optionalCourse.get());
            } else {
                System.out.println("Kurs konnte nicht angelegt werden!");
            }

        } catch(IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch(InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch(Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen:" + exception.getMessage());
        }
    }

    private void showCourseDetails() {
        System.out.println("Für welchen Kurs möchten Sie die Kursdetals anzeigen?");
        Long courseId = Long.parseLong(scan.nextLine());
        try{
            Optional<Course> courseOptional = repoCourse.getById(courseId);
            if(courseOptional.isPresent()) {
                System.out.println(courseOptional.get());
            } else {
                System.out.println("Kein Kurs zu ID gefunden!");
            }
        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler be Kurs-Detailanzeige:" + databaseException.getMessage());
        } catch(Exception exception) {
            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeige: " + exception.getMessage());
        }
    }

    private void showAllCourses() {
        List<Course> list = null;

        try {
            list = repoCourse.getAll();
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
        System.out.println("(1) Kurse eingeben \t (2) Alle Kurse anzeigen \t (3) Kurs-Details anzeigen \t (4) Kurs-Details ändern \t (5) Kurs löschen \t (6) Kurs-Suche \t (7) Laufende Kurse");
        System.out.println("(8) Student eingeben \t (9) Alle Studenten anzeigen \t (10) Studenten-Details anzeigen \t (11) Studenten-Details ändern \t (12) Student löschen");
        System.out.println("(x) ENDE");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben1");
    }

    private void addStudent() {
        String firstname, lastname;
        Date dateOfBirth;

        try {
            System.out.println("Bitte alle Studentendaten angeben: ");
            System.out.println("Vorname: ");
            firstname = scan.nextLine();
            if(firstname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Nachname: ");
            lastname = scan.nextLine();
            if(lastname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Geburtsdatum (YYYY-MM-DD)");
            dateOfBirth = Date.valueOf(scan.nextLine());

            Optional<Student> optionalStudent = repoStudent.insert(new Student(firstname, lastname, dateOfBirth));

            if(optionalStudent.isPresent()) {
                System.out.println("Student gespeichert: " + optionalStudent.get());
            } else {
                System.out.println("Student konnte nicht gespeichert werden!");
            }

        } catch(IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch(InvalidValueException invalidValueException) {
            System.out.println("Studentendaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch(Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen:" + exception.getMessage());
        }
    }

    private void showAllStudents() {
        List<Student> list = null;

        try {
            list = repoStudent.getAll();
            if (!list.isEmpty()) {
                for (Student student : list) {
                    System.out.println(student);
                }
            } else {
                System.out.println("Studentenliste leer!");
            }
        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Anzeige aller Studenten: " + databaseException.getMessage());
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private void studentSearch() {
        System.out.println("Geben Sie einen Suchbegriff an:");
        String searchString  = scan.nextLine();
        List<Student> studentList;
        try {
            studentList = repoStudent.findAllStudentsByNameLike(searchString);
            for(Student student : studentList) {
                System.out.println(student);
            }

        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Studentensuche: " + databaseException.getMessage());
        } catch(Exception exception) {
            System.out.println("Unbekannter Fehler bei der Studentensuche: " + exception.getMessage());
        }
    }

    private void updateStudentDetails() {
        System.out.println("Für welche Studenten-ID möchten Sie die Studentendetails ändern?");
        Long studentId = Long.parseLong(scan.nextLine());

        try{
            Optional<Student> studentOptional = repoStudent.getById(studentId);
            if(studentOptional.isEmpty()) {
                System.out.println("Student mit der gegebenen ID nicht in der Datenbank!");
            } else {
                Student student = studentOptional.get();

                System.out.println("Änderungen für folgenden Kurs: ");
                System.out.println(student);

                String firstname, lastname, dateOfBirth;

                System.out.println("Bitte neue Kursdaten angeben (Enter, falls keine Änderung gewünscht ist): ");
                System.out.println("Vorname: ");
                firstname = scan.nextLine();
                System.out.println("Nachname: ");
                lastname = scan.nextLine();
                System.out.println("Geburtsdatum (YYYY-MM-DD): ");
                dateOfBirth = scan.nextLine();

                Optional<Student> optionalStudentUpdated = repoStudent.update(
                        new Student(
                                student.getId(),
                                firstname.equals("") ? student.getFirstname() : firstname,
                                lastname.equals("") ? student.getLastname() : lastname,
                                dateOfBirth.equals("") ? student.getDateOfBirth() : Date.valueOf(dateOfBirth)
                        )
                ) ;

                optionalStudentUpdated.ifPresentOrElse(
                        (c)-> System.out.println("Student aktualisiert: " + c),
                        ()-> System.out.println("Student konnte nicht aktualisiert werden!")
                );
            }

        } catch(IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch(InvalidValueException invalidValueException) {
            System.out.println("Studentendaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch(DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch(Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen:" + exception.getMessage());
        }
    }

    private void deleteStudent() {
        System.out.println("Welchen Student möchten Sie löschen? Bitte ID eingeben: ");
        Long studentIdToDelete = Long.parseLong(scan.nextLine());

        try{
            repoStudent.deleteByid(studentIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }
    }
}
