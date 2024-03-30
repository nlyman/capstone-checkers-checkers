package checkers.capstone.models;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public BaseModel() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
