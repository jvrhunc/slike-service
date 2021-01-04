package si.fri.rso.slike.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import si.fri.rso.slike.models.Slika;

public interface SlikeRepository extends JpaRepository<Slika, Integer> {
    Slika getByUrl(String fileUrl);
}
