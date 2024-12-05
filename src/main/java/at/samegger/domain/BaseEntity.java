package at.samegger.domain;

public abstract class BaseEntity {
    private Long id;

    public BaseEntity(Long id) {
        setId(id);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        if(id==null || id >= 0) {
            this.id = id;
        } else {
            throw new InvalidValueException("Kurs-ID muss größer glech 0 sein");
        }
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
