package covid.weka;


import java.io.File;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

@Service
public class PredictService {
    public String[] predict(Connection con, int id) throws Exception {
        CSVMaker csvMaker = new CSVMaker();
        String filename = System.getProperty("user.dir") + "/person";
        csvMaker.sqlToCSV(filename, con, id);
        ArffMaker arffMaker = new ArffMaker();
        String input = System.getProperty("user.dir") + "/person.csv";
        String output = System.getProperty("user.dir") + "/person.arff";
        arffMaker.csvToArff(input, output);

        String modelPath = System.getProperty("user.dir") + "/RNA.model";

        try {
            ArffLoader loader = new ArffLoader();
            loader.setFile(new File(output));
            Instances dataset = loader.getDataSet();
            dataset.setClassIndex(dataset.numAttributes() - 1);
            MultilayerPerceptron rnaModel = 
                 (MultilayerPerceptron) weka.core.SerializationHelper.read(modelPath);
            Instance lastInstance = dataset.lastInstance();
            double[] predictionDistribution = 
                    rnaModel.distributionForInstance(lastInstance);
            double prediction = rnaModel.classifyInstance(lastInstance);
            String result = Double.toString(prediction);
            String percent = 
                    Double.toString(predictionDistribution[(int) prediction] * 100) + "%";
            return new String[]{result, percent};
        } catch (Exception ex) {
            Logger.getLogger(PredictService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
