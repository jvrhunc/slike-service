package si.fri.rso.slike.resources;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import si.fri.rso.slike.models.Slika;
import si.fri.rso.slike.services.AwsRekognitionService;
import si.fri.rso.slike.services.SlikeService;

import java.io.IOException;

@RestController
@RequestMapping("/v1/slike")
@RefreshScope
public class SlikeResource {

    @Autowired
    private SlikeService slikeService;

    @Autowired
    private AwsRekognitionService awsRekognitionService;

    @Timed(
            value = "Slike.getAll",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "v1"}
    )
    @GetMapping
    public ResponseEntity<Object> getSlike() {
        return ResponseEntity.status(HttpStatus.OK).body(slikeService.getSlike());
    }

    @Timed(
            value = "Slike.getById",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "v1"}
    )
    @GetMapping("/{slikaId}")
    public ResponseEntity<Object> getSlikaById (@PathVariable("slikaId") Integer slikaId) {

        if (slikaId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Slika Id is required!");
        }

        Slika slika = slikeService.getSlikaById(slikaId);

        if(slika == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(slika);
    }

    @GetMapping("/recept/{receptId}")
    public ResponseEntity<Object> getSlikaByReceptId (@PathVariable("receptId") Integer receptId) {

        if (receptId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recept Id is required!");
        }

        Slika slika = slikeService.getSlikaByReceptId(receptId);

        if(slika == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(slika);
    }

    @Timed(
            value = "Slike.save",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "v1"}
    )
    @PostMapping("/add/{receptId}")
    public ResponseEntity<Object> addSlika(@RequestPart(value = "file") MultipartFile file,
                                           @PathVariable("receptId") Integer receptId) throws IOException {

        if (file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Slika file is required!");
        }

        Slika addedSlika = null;
        if (awsRekognitionService.detectFood(file)) {
            addedSlika = slikeService.addSlika(file, receptId);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Slika is not a food!");
        }

        if (addedSlika == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while adding Slika!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(addedSlika);
    }

    @Timed(
            value = "Slike.update",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "v1"}
    )
    @PutMapping("/update/{slikaId}")
    public ResponseEntity<Object> updateSlika(@PathVariable("slikaId") Integer slikaId,
                                              @RequestPart(value = "file") MultipartFile file) throws IOException {

        if (slikaId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Slika Id is required!");
        }

        Slika updatedSlika = null;
        if (awsRekognitionService.detectFood(file)) {
            updatedSlika = slikeService.updateSlika(slikaId, file);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Slika is not a food!");
        }

        if (updatedSlika == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while updating Slika!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedSlika);
    }

    @Timed(
            value = "Slike.delete",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "v1"}
    )
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteSlika(@RequestPart(value = "url") String fileUrl) {
        String deleted = slikeService.deleteSlika(fileUrl);

        if(deleted == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while deleting Slika with url: " + fileUrl + "!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

    @Timed(
            value = "Slike.uploadFile.S3",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "v1"}
    )
    @PostMapping("/s3/uploadFile")
    public ResponseEntity<Object> uploadFile (@RequestPart(value = "file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(slikeService.uploadFile(file));
    }

    @Timed(
            value = "Slike.deleteFile.S3",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "v1"}
    )
    @DeleteMapping("/s3/deleteFile")
    public ResponseEntity<Object> deleteFile (@RequestPart(value = "url") String fileUrl) {
        return ResponseEntity.status(HttpStatus.OK).body(slikeService.deleteFile(fileUrl));
    }

    @Timed(
            value = "Slike.detectFood",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "v1"}
    )
    @PostMapping("/rekognition")
    public Object detectFood(@RequestParam MultipartFile image) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(awsRekognitionService.detectFood(image));
    }
}
