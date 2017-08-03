package weka;

/**
 *
 * @author amina
 */
import weka.core.*;
import weka.filters.*;
import weka.filters.unsupervised.attribute.*;
import java.io.*;
import java.util.Properties;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;

public class Weka {

    public static void main(String[] args) throws Exception {

        String file = "";
        String classfier = "";
        String flds = "";
        int folds;
        Properties options = new Properties();
        options.load(new FileInputStream("param.txt"));
        file = options.getProperty("fileToClassify", file).trim();
        classfier = options.getProperty("classifier", classfier).trim();
        flds = options.getProperty("folds", flds).trim();
        folds = Integer.parseInt(flds);
        BufferedReader reader = new BufferedReader(
                new FileReader(file));
        Instances dataRaw = new Instances(reader);
        reader.close();
       // System.out.println("\n\nImported data:\n\n" + dataRaw);

        dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
        //filter to convert texts to vectors
        StringToWordVector filter = new StringToWordVector();
        filter.setIDFTransform(true);
        filter.setTFTransform(true);
        filter.setInputFormat(dataRaw);

        Instances dataFiltered = Filter.useFilter(dataRaw, filter);
        //System.out.println("\n\nFiltered data:\n\n" + dataFiltered);

        // randomize data
        Instances randData = new Instances(dataFiltered);
        if (randData.classAttribute().isNominal()) {
            randData.stratify(folds);
        }

        // perform cross-validation
        Evaluation eval = new Evaluation(randData);
        for (int n = 0; n < folds; n++) {
            Instances train = randData.trainCV(folds, n);
            Instances test = randData.testCV(folds, n);

            switch (classfier) {
                case "NaiveBayes": {

                    NaiveBayes classifier = new NaiveBayes();
                    classifier.buildClassifier(train);
                    eval.evaluateModel(classifier, test);
                    break;

                }
                case "SMO": {
                    SMO classifier = new SMO();
                    classifier.buildClassifier(train);
                    eval.evaluateModel(classifier, test);
                    break;
                }
            }

            
        }
        System.out.println(randData.classAttribute().numValues());
            for (int i = 0; i < randData.classAttribute().numValues(); i++) {
                System.out.println("recall of the category num " + i + "is: " + eval.recall(i));
                System.out.println("precision of the category num " + i + "is: " + eval.precision(i));
                System.out.println("fMeasure of the category num " + i + "is: " + eval.fMeasure(i));
            }
            System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

    }

}
