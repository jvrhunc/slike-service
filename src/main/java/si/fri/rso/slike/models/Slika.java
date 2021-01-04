package si.fri.rso.slike.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "slika")
public class Slika {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slikaId;

    @JsonFormat(pattern="dd-MM-yyyy", timezone="Europe/Ljubljana")
    private LocalDate created;

    private String url;

    private Integer receptId;

    public Slika() {
    }

    public Integer getSlikaId() {
        return slikaId;
    }

    public void setSlikaId(Integer slikaId) {
        this.slikaId = slikaId;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getReceptId() {
        return receptId;
    }

    public void setReceptId(Integer receptId) {
        this.receptId = receptId;
    }
}
