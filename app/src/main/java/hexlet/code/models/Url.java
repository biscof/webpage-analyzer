package hexlet.code.models;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "urls")
public class Url extends Model {
    @Id
    private int id;
    private String name;

    @WhenCreated
    private Instant createdAt;

    public Url(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}