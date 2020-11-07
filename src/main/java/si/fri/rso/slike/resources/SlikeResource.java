package si.fri.rso.slike.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import si.fri.rso.slike.models.Slika;
import si.fri.rso.slike.repositories.SlikeRepository;
import si.fri.rso.slike.services.AmazonClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/slike")
public class SlikeResource {

    @Autowired
    private SlikeRepository slikeRepository;

    private AmazonClient amazonClient;

    @Autowired
    SlikeResource(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @GetMapping
    public List<Slika> getSlike() {
        return slikeRepository.findAll();
    }

    @GetMapping("/{slikaId}")
    public Optional<Slika> getSlikaById (@PathVariable("slikaId") Integer slikaId) {
        return slikeRepository.findById(slikaId);
    }

    @PostMapping("/add")
    public Slika addSlika(@RequestPart(value = "slika") Slika slika,
                          @RequestPart(value = "file") MultipartFile file) throws IOException {
        slika.setUrl(this.amazonClient.uploadFile(file));
        slika.setCreated(LocalDate.now());
        return slikeRepository.save(slika);
    }

    @PutMapping("/update/{slikaId}")
    public Slika updateSlika(@RequestBody Slika slika, @PathVariable("slikaId") Integer slikaId) {

        Optional<Slika> slikaToUpdate = slikeRepository.findById(slikaId);

        return slikaToUpdate.map(s -> {
            s.setCreated(slika.getCreated());
            s.setUrl(slika.getUrl());
            s.setReceptId(slika.getReceptId());
            return slikeRepository.save(s);
        }).orElseGet(() -> {
            slika.setSlikaId(slikaId);
            return slikeRepository.save(slika);
        });
    }

    @DeleteMapping("/delete")
    public String deleteSlika(@RequestPart(value = "url") String fileUrl) {
        String deleted = this.amazonClient.deleteFileFromS3Bucket(fileUrl);

        Slika slika = slikeRepository.getByUrl(fileUrl);
        slikeRepository.delete(slika);
        return deleted;
    }

    @PostMapping("/s3/uploadFile")
    public String uploadFile (@RequestPart(value = "file") MultipartFile file) throws IOException {
        return this.amazonClient.uploadFile(file);
    }

    @DeleteMapping("/s3/deleteFile")
    public String deleteFile (@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
}
