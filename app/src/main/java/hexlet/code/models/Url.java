package hexlet.code.models;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Entity
@Table
public final class Url extends Model {
    @Id
    private long id;
    private String name;

    @OneToMany(mappedBy = "url", cascade = CascadeType.ALL)
    private List<UrlCheck> urlChecks;

    @WhenCreated
    private Instant createdAt;

    public Url(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<UrlCheck> getUrlChecks() {
        return urlChecks;
    }

    public UrlCheck getLatestCheck() {
        return this.getUrlChecks().stream()
                .max(Comparator.comparing(UrlCheck::getCreatedAt))
                .orElse(null);
    }
}
