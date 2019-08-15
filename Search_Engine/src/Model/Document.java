package Model;

import java.io.Serializable;
import java.security.KeyStore;
import java.util.*;
import java.util.Map.Entry;
import java.util.Map;
/**
 * This class represent one document.
 */

public class Document implements Serializable{
    public static boolean ASC = true;
    private String docName;
    private String city;
    private String date;
    private int max_tf;
    private int numOfDifferentTerms;
    private int documentLength;
    private HashMap<String,Integer> termsPerDoc;
    private String [] fiveOrLessWords;
    private int [] fiveOrLessValue;
    private HashSet<String> title;


    public Document(String docName, String city, String date, int max_tf, int numOfDifferentTerms,HashSet<String> titel) {
        this.docName = docName;
        this.city = city;
        this.max_tf = max_tf;
        this.numOfDifferentTerms = numOfDifferentTerms;
        this.date = date;
        title=titel;
        termsPerDoc=new HashMap<>();
        fiveOrLessWords=new String[5];
        fiveOrLessValue=new int[5];
    }
    public HashMap<String, Integer> getTermsPerDoc() {
        return termsPerDoc;
    }

    public void setTermsPerDoc(HashMap<String, Integer> termsPerDoc) {
        for (HashMap.Entry<String, Integer> entry : termsPerDoc.entrySet()) {
            this.termsPerDoc.put(entry.getKey(),entry.getValue());
        }
    }

    public HashSet<String> getTitel() {
        return title;
    }

    public void setTitel(HashMap<String, Term> titel) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDocumentLength(int documentLength) {
        this.documentLength = documentLength;
    }

    public int getDocumentLength() {
        return documentLength;
    }

    public String[] getFiveOrLessWords() {
        return fiveOrLessWords;
    }

    public int[] getFiveOrLessValue() {
        return fiveOrLessValue;
    }


    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getMax_tf() {
        return max_tf;
    }

    public void setMax_tf(int max_tf) {
        this.max_tf = max_tf;
    }

    public int getNumOfDifferentTerms() {
        return numOfDifferentTerms;
    }

    public void setNumOfDifferentTerms(int numOfDifferentTerms) {
        this.numOfDifferentTerms = numOfDifferentTerms;
    }

    public String getDate() {
        return date;
    }
    //Sort the field termsPerDoc according to the frequencies, from high to low,
    // and update the results in the fields fiveOrLessWords,fiveOrLessValue
    public void sort5things(){
        ArrayList<Entry<String,Integer>> list = new ArrayList<Entry<String,Integer>>(getTermsPerDoc().entrySet());
        boolean order= ASC;
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String,Integer> o1, Entry<String, Integer> o2) {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });
        if(list.size()>0) {
            int place = 0;
            for (int i = list.size() - 1; i >= 0; i--) {
                fiveOrLessWords[place] = list.get(i).getKey();
                fiveOrLessValue[place] = list.get(i).getValue();
                place++;
                if (place == 5)
                    break;
            }
        }

        termsPerDoc=new HashMap<>();
    }

}