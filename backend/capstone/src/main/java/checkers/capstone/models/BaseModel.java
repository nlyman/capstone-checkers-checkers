package checkers.capstone.models;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public BaseModel() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
