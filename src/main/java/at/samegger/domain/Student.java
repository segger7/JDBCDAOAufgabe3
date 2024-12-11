package at.samegger.domain;

import java.sql.Date;

public class Student extends BaseEntity{

    private String firstname;
    private String lastname;
    private Date dateOfBirth;

    public Student(Long id, String firstname, String lastname, Date dateOfBirth) {
        super(id);
        setFirstname(firstname);
        setLastname(lastname);
        setDateOfBirth(dateOfBirth);
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        if(dateOfBirth != null) {
            this.dateOfBirth = dateOfBirth;
        } else {
            throw new InvalidValueException("Geburtsdatum darf nicht null sein");
        }
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        if(lastname != null) {
            this.lastname = lastname;
        } else {
            throw new InvalidValueException("Nachname darf nicht null sein");
        }
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        if(firstname != null) {
            this.firstname = firstname;
        } else {
            throw new InvalidValueException("Vorname darf nicht null sein");
        }
    }
}
