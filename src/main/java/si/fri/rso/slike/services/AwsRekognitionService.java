package si.fri.rso.slike.services;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;

@Service
public class AwsRekognitionService {

    private AmazonRekognition client;

    public AwsRekognitionService(AmazonRekognition client) {
        this.client = client;
    }

    /**
     * Metoda preveri, ce je na sliki hrana in vrne true ce je, false drugace.
     * @param imageToCheck
     * @return
     * @throws IOException
     */
    public Boolean detectFood(MultipartFile imageToCheck) throws IOException {
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image().withBytes(ByteBuffer.wrap(imageToCheck.getBytes())));

        DetectLabelsResult labelsResult = client.detectLabels(request);

        for (Label l :labelsResult.getLabels()) {
            if ("Food".equals(l.getName()) || "food".equals(l.getName())) {
                if (l.getConfidence() > 60) {
                    return true;
                }
            }
        }

        return false;
    }
}
