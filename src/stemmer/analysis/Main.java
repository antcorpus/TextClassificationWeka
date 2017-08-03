/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stemmer.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import file.utils.FileUtil;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author amina
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
	String fin="" ;	// input file 
	String fout = "";
        Properties options = new Properties();
        options.load(new FileInputStream("param.txt"));
        fin = options.getProperty("fileToStem", fin).trim();
        fout = options.getProperty("fileStemmed", fout).trim();
        
	file.utils.FileUtil fr = new FileUtil();

	// load stopwords
	String stopwords = fr.readEntilerFile("stopwords.txt");
	List<String> stopwordsList = Arrays.asList(stopwords.split("\n"));
			
	String s = fr.readEntilerFile(fin);	// read input file
	
	String[] lines = s.split("\n");
	StringBuilder sbuf = new StringBuilder();
	for (int i = 0; i < lines.length; i++) {
	    String[] tokens = lines[i].split("\\s");
	    for (int j = 0; j < tokens.length; j++) {
	        String t = tokens[j];
		if (! stopwordsList.contains(t)) {	// ignore stopwords 
			String resut = lightStem(t);	// Light stemming algorithm 
			sbuf.append(resut).append(" ");
		}

	    }
	    sbuf.append("\n");
	}
	
	fr.writeFileUTF(sbuf.toString(),fout);	// write results to the output file
                
    }
    
    private static String lightStem(String string) {
        ArabicNormalizer arabicNorm = new ArabicNormalizer();
        char[] c = string.toCharArray();
        int len = c.length;
        len = arabicNorm.normalize(c, len);
        char[] normalizedWord = new char[len];
        System.arraycopy(c, 0, normalizedWord, 0, len);



        ArabicStemmer araLightStemmer = new ArabicStemmer();
        len = araLightStemmer.stem(normalizedWord, len);
        char[] lightWord = new char[len];
        System.arraycopy(normalizedWord, 0, lightWord, 0, len);
       

        StringBuilder sbuf = new StringBuilder();
        sbuf.append(lightWord);


        String result = sbuf.toString();
        return result;
    }
    
}
