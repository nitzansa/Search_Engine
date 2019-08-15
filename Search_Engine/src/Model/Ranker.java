package Model;

import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This class represent a ranker for single quary or queris file.
 */

public class Ranker {
    private HashMap<String, Document> docs;
    private HashMap<String,Double>[] distanceForEachPair;
    HashMap<String, Term> qTerms;
    private double avdl;
    private HashMap<String, Integer> dates;
    HashMap<String,Integer>strongWord;

    private void fillDates() {
        this.dates.put("January", 32); this.dates.put("February", 64); this.dates.put("March", 96); this.dates.put("April", 128);
        this.dates.put("May", 160); this.dates.put("June", 192); this.dates.put("July", 224); this.dates.put("August", 256);
        this.dates.put("September", 288); this.dates.put("October", 320); this.dates.put("November", 352);
        this.dates.put("December", 384);
        this.dates.put("JANUARY", 32); this.dates.put("FEBRUARY", 64); this.dates.put("MARCH", 96); this.dates.put("APRIL", 128);
        this.dates.put("MAY", 160); this.dates.put("JUNE", 192); this.dates.put("JULY", 224); this.dates.put("AUGUST", 256);
        this.dates.put("SEPTEMBER", 288); this.dates.put("OCTOBER", 320); this.dates.put("NOVEMBER", 352);
        this.dates.put("DECEMBER", 384);
        this.dates.put("Jan", 32); this.dates.put("Feb", 64); this.dates.put("Mar", 96); this.dates.put("Apr", 128);
        this.dates.put("Jun", 192); this.dates.put("Jul", 224); this.dates.put("Aug", 256); this.dates.put("Sep", 288);
        this.dates.put("Oct", 320); this.dates.put("Nov", 352); this.dates.put("Dec", 388);
        this.dates.put("JAN", 32);this.dates.put("FEB", 64); this.dates.put("MAR", 96); this.dates.put("APR", 128);
        this.dates.put("JUN", 192);this.dates.put("JUL", 224);this.dates.put("AUG", 256);this.dates.put("SEP", 288);
        this.dates.put("OCT", 320);this.dates.put("NOV", 352);this.dates.put("DEC", 388);
        this.dates.put("january", 32);this.dates.put("february", 64);this.dates.put("march", 96);this.dates.put("april", 128);
        this.dates.put("may", 160);this.dates.put("june", 192);this.dates.put("july", 224);this.dates.put("august", 256);
        this.dates.put("september", 288);this.dates.put("october", 320);this.dates.put("november", 352);
        this.dates.put("december", 388);
        this.dates.put("jan", 32);this.dates.put("feb", 64);this.dates.put("mar", 96);this.dates.put("apr", 128);
        this.dates.put("jun", 192);this.dates.put("jul", 224);this.dates.put("aug", 256);this.dates.put("sep", 288);
        this.dates.put("oct", 320);this.dates.put("nov", 352);this.dates.put("dec", 388);
    }

    public Ranker(HashMap<String, Document> docs, HashMap<String, Term> qTerms, String query,HashMap strongWord) {
        this.docs = docs;
        dates = new HashMap<>();
        fillDates();
        this.qTerms = qTerms;
        this.strongWord = strongWord;
        double allUniqueTerms = 0;
        if(qTerms.size()!=0) {
            distanceForEachPair = new HashMap[this.qTerms.size() - 1];
        }
        else {
            distanceForEachPair=new HashMap[0];
        }
        for (HashMap.Entry<String, Document> entry : docs.entrySet()) {
            allUniqueTerms =allUniqueTerms+ entry.getValue().getDocumentLength();
        }

        avdl = allUniqueTerms / docs.size();
    }
    //Rated using the bm25 formula
    public double BM25(String nameDoc){
        double k = 1.2;
        double B = 0.5;
        double ans = 0;
        double df = 0;
        for (HashMap.Entry<String, Term> entry : qTerms.entrySet()) {
            df = Double.valueOf(entry.getValue().getDocs().size());
            double m=docs.size();
            double idf = Math.log10((m+1) / df);
            double tf=0 ;
            double cwq=1.0;
            if(strongWord.containsKey(entry.getKey()))
                cwq=(double)strongWord.get(entry.getKey());
            if(entry.getValue().getDocs().containsKey(nameDoc)) {
                tf = entry.getValue().getDocs().get(nameDoc).getKey();
            }
            else if (docs.get(nameDoc).getTitel().contains(entry.getKey().toLowerCase()) || docs.get(nameDoc).getTitel().contains(entry.getKey().toUpperCase()) ){
                tf =1;
            }
            double first=(k+1)*cwq*tf;
            double second = 0;
            second = (B * docs.get(nameDoc).getDocumentLength()) / avdl;
            double third=k*(1-B+second);
            double four=third+tf;
            ans =ans+(first*idf/four);
        }
        return ans;
    }
    //Find common documents for calculating distances between different terms in a document
    public void findDocs(String first, String second, int whichPair) {
        HashMap<String, Double> forAllDocs = new HashMap<>();
        try {
            int size1 = qTerms.get(first).getDocs().size();
            int size2 = qTerms.get(second).getDocs().size();
            Term t;
            Term other;
            if (size1 >= size2) {
                t = qTerms.get(second);
                other = qTerms.get(first);
            } else {
                t = qTerms.get(first);
                other = qTerms.get(second);
            }
            for (HashMap.Entry<String, Pair<Integer, String>> entry : t.getDocs().entrySet()) {
                if (other.getDocs().containsKey(entry.getKey())) {
                    double distance = distance(other.getDocs().get(entry.getKey()).getValue(), t.getDocs().get(entry.getKey()).getValue());
                    forAllDocs.put(entry.getKey(), distance);
                } else
                    continue;
            }
            if (forAllDocs != null)
                distanceForEachPair[whichPair] = forAllDocs;
        }catch (Exception e){
            return;
        }
    }
    //Connect document ratings for identical documents that share different terms in a query
    public HashMap<String,Double> mergeDistances(){
        HashMap<String,Double> toReturn = new HashMap<>();
        try {
            for (HashMap.Entry<String, Double> entry : distanceForEachPair[0].entrySet()) {
                toReturn.put(entry.getKey(), entry.getValue());
            }
            for (int i = 1; i < distanceForEachPair.length; i++) {
                for (HashMap.Entry<String, Double> entry : distanceForEachPair[i].entrySet()) {
                    if (toReturn.containsKey(entry.getKey())) {
                        toReturn.put(entry.getKey(), toReturn.get(entry.getKey()) + (entry.getValue()));
                    } else
                        toReturn.put(entry.getKey(), entry.getValue());
                }
            }
            for (HashMap.Entry<String, Double> entry : toReturn.entrySet()) {
                toReturn.put(entry.getKey(), (((double) 1) / entry.getValue()) * 0.1);
            }
            return toReturn;
        }
        catch (Exception e){
            return toReturn;
        }
    }
    //Sort the results of the document ratings from the highest to the lowest
    public List sortingResults(HashMap<String,Double> toSort, final boolean order){
        List<Map.Entry<String, Double>> list = new ArrayList<>(toSort.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (order) {

                    return o2.getValue().compareTo(o1.getValue());
                } else {
                    return o1.getValue().compareTo(o2.getValue());

                }
            }
        });

        return list;
    }
    //Rated using the distance formula
    public double distance(String firstWord, String secondWord) {
        String[] first = firstWord.split("&");//first in sheilta
        String[] second = secondWord.split("&");//second in sheilta
        double distance = Double.POSITIVE_INFINITY;
        int f = 0;
        int s = 0;
        int i = 0;
        int j = 0;
        while (j < first.length && i < second.length) {
            f = Integer.valueOf(first[j]);
            s = Integer.valueOf(second[i]);
            if (f < s) {
                if (distance > s - f)
                    distance = s - f;
                i++;
                j++;
            } else if (f == s) {
                i++;
            } else {
                i++;
            }
            if (distance == 1)
                return 1;
        }
        return distance;
    }
    //Provides a rating for the date of publication of the document
    public double dateValue(String date){
        if(date.equals("")) {
            double first=(2019*390)+100+1;
            double second=(1994 * 390) + 101;
            return 1.5/(first-second);
        }
        String [] threeParts=date.split(" ");
        double dateOfToday=(2019*390)+100+1;
        double current=0;
        try {
            if(threeParts.length==2){
                current= Integer.valueOf(threeParts[0]);
                current = current + (double) dates.get(threeParts[1]);
                current = current +(1994*390);
            }
            else if(dates.containsKey(threeParts[0])){
                current= Integer.valueOf(threeParts[1])+ (Integer.valueOf(threeParts[2])*390);
                current = current + (double) dates.get(threeParts[0]);
            }
            else if(dates.containsKey(threeParts[1])) {
                current = Integer.valueOf(threeParts[0]) + (Integer.valueOf(threeParts[2]) * 390);
                current = current + (double) dates.get(threeParts[1]);
            }
            else {
                current = Integer.valueOf(threeParts[0]) + (Integer.valueOf(threeParts[2]) * 390);
                current=current+32;
            }
        }

        catch (Exception e){
            double first=(2019*390)+100+1;
            double second=(1994 * 390) + 101;
            return 1.5/(first-second);
        }

        return 1.5/(dateOfToday-current);
    }

    public HashMap<String, Double>[] getDistanceForEachPair() {
        return distanceForEachPair;
    }

}
