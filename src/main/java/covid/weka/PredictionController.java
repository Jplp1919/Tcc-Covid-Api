package covid.weka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;

@RestController
@RequestMapping("/api")
public class PredictionController {

    private final PredictService predictService;
    private final SavePredictionService savePredictionService;
    private final ConnectionFactory connectionFactory;

    @Autowired
    public PredictionController(PredictService predictService, SavePredictionService savePredictionService, ConnectionFactory connectionFactory) {
        this.predictService = predictService;
        this.savePredictionService = savePredictionService;
        this.connectionFactory = connectionFactory;
    }
    
    @PostMapping("/predict")
    public PredictionResponse predict(@RequestBody PredictionRequest predictionRequest) throws Exception {
        Connection con = connectionFactory.establishConnection();
        int id = predictionRequest.getId();
        String[] results = predictService.predict(con, id);
        if (results != null) {
            savePredictionService.save(results[0], results[1], id, con);
            return new PredictionResponse(results[0], results[1], id);
        } else {
            throw new RuntimeException("Predição falhou");
        }
    }
}

class PredictionRequest {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class PredictionResponse {
    private String prediction;
    private String percentage;
    private int id;

    public PredictionResponse(String prediction, String percentage, int id) {
        this.prediction = prediction;
        this.percentage = percentage;
        this.id = id;
    }

    public String getPrediction() {
        return prediction;
    }

    public String getPercentage() {
        return percentage;
    }

    public int getId() {
        return id;
    }
}
