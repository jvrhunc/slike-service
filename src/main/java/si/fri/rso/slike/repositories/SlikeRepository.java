package si.fri.rso.slike.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import si.fri.rso.slike.models.Slika;

import java.util.List;

public interface SlikeRepository extends JpaRepository<Slika, Integer> {
    Slika getByUrl(String fileUrl);
    List<Slika> getByReceptId(Integer receptId);
}
