package si.fri.rso.slike.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import si.fri.rso.slike.models.Slika;
import si.fri.rso.slike.repositories.SlikeRepository;

public class Query implements GraphQLQueryResolver {

    private SlikeRepository slikeRepository;

    public Query(SlikeRepository slikeRepository) {
        this.slikeRepository = slikeRepository;
    }

    public Iterable<Slika> findAllSlike() {
        return slikeRepository.findAll();
    }

    public Slika findSlikaById(Integer id) {
        return slikeRepository.findById(id).orElse(null);
    }

    public Iterable<Slika> findSlikaByReceptId(Integer id) {
        return slikeRepository.getByReceptId(id);
    }

    public Slika findSlikaByUrl(String url) {
        return slikeRepository.getByUrl(url);
    }


}
