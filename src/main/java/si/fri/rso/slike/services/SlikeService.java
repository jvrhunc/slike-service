package si.fri.rso.slike.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import si.fri.rso.slike.models.Slika;
import si.fri.rso.slike.repositories.SlikeRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SlikeService {

    @Autowired
    private SlikeRepository slikeRepository;

    private AmazonClient amazonClient;

    @Autowired
    SlikeService(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    public List<Slika> getSlike() {
        return slikeRepository.findAll();
    }

    public Slika getSlikaById (Integer slikaId) {
        return slikeRepository.findById(slikaId).orElse(null);
    }

    public Slika addSlika(MultipartFile file) throws IOException {
        Slika added = new Slika();
        added.setCreated(LocalDate.now());
        added.setUrl(this.amazonClient.uploadFile(file));
        added.setReceptId(1);
        return slikeRepository.save(added);
    }

    public Slika updateSlika(Integer slikaId, MultipartFile file) throws IOException {

        Optional<Slika> slikaToUpdate = slikeRepository.findById(slikaId);

        if (slikaToUpdate.isEmpty()) {
            return null;
        }

        String imageUrl = this.amazonClient.uploadFile(file);
        Slika updated = slikaToUpdate.get();
        updated.setUrl(imageUrl);
        updated.setCreated(LocalDate.now());
        return slikeRepository.save(updated);
    }

    public String deleteSlika(String fileUrl) {
        String deleted = this.amazonClient.deleteFileFromS3Bucket(fileUrl);

        Slika slika = slikeRepository.getByUrl(fileUrl);
        slikeRepository.delete(slika);
        return deleted;
    }

    public String uploadFile (MultipartFile file) throws IOException {
        return this.amazonClient.uploadFile(file);
    }

    public String deleteFile (String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }

}
