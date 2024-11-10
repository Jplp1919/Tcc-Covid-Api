package covid.weka;

import java.io.File;
import java.io.IOException;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class ArffMaker {
    public void csvToArff(String source, String output) {
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(source));
            Instances data = loader.getDataSet();
            data.deleteAttributeAt(0);
            int attributeIndex = data.attribute("ValorQualitativo").index();
            NumericToNominal numericToNominal = new NumericToNominal();  
            numericToNominal.setAttributeIndices("" + (attributeIndex + 1)); 
            numericToNominal.setInputFormat(data);
            Instances newData = Filter.useFilter(data, numericToNominal);
            ArffSaver saver = new ArffSaver();
            saver.setInstances(newData);
            File f = new File(output);
            if (f.exists()) {
                f.delete();
            }
            saver.setFile(new File(output));
            saver.writeBatch();

        } catch (IOException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println("Erro inesperado");
            System.out.println(e.getMessage());
        }
    }   
}
