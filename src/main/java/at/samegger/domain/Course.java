package at.samegger.domain;

import java.sql.Date;

public class Course extends BaseEntity {

    private String name;
    private String description;
    private int hours;
    private Date beginDate;
    private Date endDate;
    private CourseType courseType;

    public Course(String name, String description, int hours, Date beginDate, Date endDate, CourseType courseType) throws InvalidValueException {
        super(null);
        setName(name);
        setDescription(description);
        setHours(hours);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setCourseType(courseType);
    }

    public Course(Long id, String name, String description, int hours, Date beginDate, Date endDate, CourseType courseType) throws InvalidValueException {
        super(id);
        setName(name);
        setDescription(description);
        setHours(hours);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setCourseType(courseType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidValueException {
        if(name!=null && name.length()>1) {
            this.name = name;
        } else {
            throw new InvalidValueException("Kursname muss mindestens 2 Zeichen lang sein");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws InvalidValueException {
        if(description!= null && description.length()>10) {
            this.description = description;
        } else {
            throw new InvalidValueException("Beschreibung muss mindestens 10 Zeichen lang sein");
        }
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) throws InvalidValueException {
        if(courseType != null) {
            this.courseType = courseType;
        } else {
            throw new InvalidValueException("Kurstyp darf nicht null sein");
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) throws InvalidValueException {
        if(endDate!=null) {
            if(this.beginDate != null) {
                if(endDate.after(this.beginDate)) {
                    this.endDate = endDate;
                } else {
                    throw new InvalidValueException("Kursbeginn muss VOR Kursende sein!");
                }
            } else {
                this.endDate = endDate;
            }
        } else {
            throw new InvalidValueException("Enddatum darf nicht null / leer sein!");
        }
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) throws InvalidValueException {
        if(beginDate!=null) {
            if(this.endDate != null) {
                if(beginDate.before(this.endDate)) {
                    this.beginDate = beginDate;
                } else {
                    throw new InvalidValueException("Kursbeginn muss VOR Kursende sein!");
                }
            } else {
                this.beginDate = beginDate;
            }
        } else {
            throw new InvalidValueException("Startdatum darf nicht null / leer sein!");
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) throws InvalidValueException {
        if(hours >0 && hours < 10) {
            this.hours = hours;
        } else {
            throw new InvalidValueException("Anzahl der Kursstunden pro Kurs darf nur zwischen 1 und 10 liegen!");
        }
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + this.getId() + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", hours=" + hours +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", courseType=" + courseType +
                ", id=" + getId() +
                '}';
    }
}
