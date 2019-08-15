package Model;

import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.util.*;
/**
 * This class represent a search of a single quary.
 */

public class Searcher {
    private Parse p;
    private Ranker ranker;
    private HashMap<String, Pair<String, String>> indexer;
    private String[] query;
    private String[] title;
    private HashMap<String, Term> qTerms;
    private HashSet sw;
    private HashMap<String, Document> docs;
    private HashSet<String> relevantDocs;
    private HashSet<String> userChooseCities;
    private List<String> docsThatReturned;
    private String postingPath;
    HashMap<String,Integer>strongWord;
    public static boolean DESC = true;

    public Searcher(HashMap<String, Pair<String, String>> indexer , HashSet sw, HashMap<String, Document> docs,
                    HashSet<String>userChooseCities, String path){
        p = new Parse();
        this.qTerms = new HashMap<>();
        this.sw = sw;
        this.docs = docs;
        this.userChooseCities =userChooseCities;
        relevantDocs = new HashSet<>();
        docsThatReturned=new ArrayList<>();
        this.indexer = indexer;
        this.strongWord=new HashMap<>();
        this.postingPath = path;
    }
    //Prepares what it takes to search the query in the repository documents and starts searching
    public void searcher(String query , boolean stem, boolean semantic) throws IOException {
        Document doc = new Document("query", "", "", 0, 0, new HashSet<>());
        p.parsing(doc, query, sw, stem);
        if(semantic) {
            query=query+useSemantics(p.getTerms());
            p.parsing(doc, query, sw, stem);
        }
        String[]array=query.split(" ");
        int numberOfMakaf = 0;
        for(int i=0;i<array.length;i++){
            String [] forMakaf = array[i].split("-");
            if(forMakaf.length>1)
                numberOfMakaf=numberOfMakaf+forMakaf.length;
        }
        int max=Math.max(p.getTerms().size(),array.length+numberOfMakaf);
        this.query = new String[max];
        this.title = new String[max];
        for (HashMap.Entry<String, Term> entry : p.getTerms().entrySet()) {
            try {
                String first = entry.getValue().getDocs().get("query").getValue();
                if (first.contains("&")) {
                    String[] moreThanOnce = first.split("&");
                    title[Integer.valueOf(moreThanOnce[0]) - 1] = entry.getKey();
                    this.query[Integer.valueOf(moreThanOnce[0]) - 1] = entry.getKey();
                    if (strongWord.containsKey(entry.getKey())) {
                        strongWord.put(entry.getKey(), moreThanOnce.length + 1);
                    } else
                        strongWord.put(entry.getKey(), moreThanOnce.length);
                } else {
                    int index = Integer.valueOf(first);
                    title[index - 1] = entry.getKey();
                    this.query[index - 1] = entry.getKey();
                }
            }catch (Exception e){
                continue;
            }
        }
        this.title =fixQArr(title);
        getInfoFromPosting(postingPath ,this.query);
        for (int i = 0; i < this.query.length; i++) {
            if(!qTerms.containsKey(this.query[i]))
                this.query[i] = "";
        }
        this.query= fixQArr(this.query);
        ranker = new Ranker(docs, this.qTerms, query,strongWord);
        for (HashMap.Entry<String, Term> entry : qTerms.entrySet()) {
            relevantDocs.addAll(entry.getValue().getDocs().keySet());
        }
    }

    public List<String> getDocsThatReturned() {
        return docsThatReturned;
    }
    //Calculates the ranking of the documents for the query
    public void evaluate() throws IOException {
        titelToRelevant();
        HashMap<String,Double> distances = new HashMap<>();
        String[] termsInOrder = this.query;
        //add according distances
        for (int i = 0; i < termsInOrder.length - 1 ; i++){
            if(termsInOrder.length > 1) {
                ranker.findDocs(termsInOrder[i], termsInOrder[i + 1], i);
            }
        }
        if(ranker.getDistanceForEachPair().length > 1)
            distances = ranker.mergeDistances();
        //add according BM25
        for (String entry : relevantDocs) {
            double ibm25 = ranker.BM25(entry);
            if(distances.containsKey(entry)){
                distances.put(entry, distances.get(entry) + ibm25);
            }
            else
                distances.put(entry, ibm25);
        }
        //add all dates rank
        for (HashMap.Entry<String, Double> entry : distances.entrySet()) {
            try {
                distances.put(entry.getKey(), entry.getValue() + (ranker.dateValue(docs.get(entry.getKey()).getDate()))*0.5);
            }
            catch (Exception e){
               continue;
            }
        }
        //filter by cities
        if(userChooseCities.size()>0) {
            ArrayList<String> temp = new ArrayList<>();
            for (HashMap.Entry<String, Double> entry : distances.entrySet()) {
                if (!(userChooseCities.contains(docs.get(entry.getKey()).getCity())))
                    temp.add(entry.getKey());
            }
            for (int i = 0; i < temp.size(); i++)
                distances.remove(temp.get(i));
            temp=new ArrayList<>();
        }

        List bigList = ranker.sortingResults(distances, DESC);
        returnOnlyFifty(bigList);
    }
    //Adds documents where the terms appear in the document title to the documents that are relevant to the retrieval
    private HashMap<String,Double> titelToRelevant() {
        HashMap<String, Double> forTitles = new HashMap<String, Double>();
        for (int i = 0; i < title.length; i++) {
            for (HashMap.Entry<String, Document> allDocs :docs.entrySet()) {
                try {
                    if(allDocs.getValue().getTitel().contains(title[i].toUpperCase())||allDocs.getValue().getTitel().contains(title[i].toLowerCase())){
                        if(!qTerms.containsKey(title[i])){
                            Document d = new Document(allDocs.getKey(),"","",1,1,new HashSet<>());
                            Term t = new Term(title[i], allDocs.getKey(), 1, "1");
                            t.getDocs().put(allDocs.getKey(),new Pair<>(1,""));
                            qTerms.put(t.term,t);
                        }
                        relevantDocs.add(allDocs.getKey());
                    }
                }
                catch (Exception e){
                }
            }
        }
        return forTitles;
    }
    //Reduces the list to a maximum of 50 documents
    private void returnOnlyFifty(List bigList) {
        List<Object> list = new ArrayList();
        while (bigList.size()>50){
            bigList.remove(50);
        }
        for(int i=0;i<bigList.size() && i<50 ;i++){
            list.add(bigList.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                String[] array = list.get(i).toString().split("=");
                docsThatReturned.add(array[0]);
            }catch (Exception e){
                continue;
            }
        }
    }
    //Downloading empty cells passes terms that do not appear at all in the main dictionary
    private String[] fixQArr(String[] query) {
        int test = 0;
        for (int i=0; i<query.length; i++){
            if(query[i]==null || query[i].equals(""))
                test++;
        }
        String[] test2 = new String[query.length - test];
        int j = 0;
        for (int i = 0; i < query.length; i++) {
            if (query[i]!=null && !query[i].equals("")) {
                test2[j] = query[i];
                j++;
            }
        }
        return test2;
    }
    //Write the results to the file selected by the user
    public void writeResults (String path) throws IOException {
        StringBuilder addToFile = new StringBuilder();
        for (int i = 0; i < docsThatReturned.size(); i++) {
            addToFile.append("240 " + "0 " + docsThatReturned.get(i) + " 1 " + "42.38 mt"+"\n");/////////\n
        }
        BufferedWriter test = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "\\Results.txt")));
        test.write(addToFile.toString());
        addToFile = new StringBuilder();
        test.flush();
        test.close();

    }
    //Allows the use of a semantic connection for the query
    public String useSemantics(HashMap<String, Term> queryAfterParse) throws IOException {
        String allSynonyms = "";
        try {

            for (HashMap.Entry<String, Term> entry : queryAfterParse.entrySet()) {
                URL glove = new URL("https://api.datamuse.com/words?rel_syn=" + entry.getKey());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(glove.openStream()));
                String line = bufferedReader.readLine();
                bufferedReader.close();
                if (line.equals("[]"))
                    continue;
                String[] theSameContext = line.split("\":\"");
                for (int i = 1; i < theSameContext.length; i++) {
                    String[] onlyWord = theSameContext[i].split(",");
                    allSynonyms += onlyWord[0].substring(0, onlyWord[0].length() - 1) + " ";
                    if (i == 1)
                        break;
                }
            }
        }
        catch (Exception e){

        }
        return allSynonyms;
    }
    //Saving the information from the posting file in memory using the path and pointer found in the general dictionary
    public void getInfoFromPosting(String posting_path, String[] termsOfQuery) throws IOException {
        qTerms = new HashMap<>();
        for (int i = 0; i < termsOfQuery.length; i++) {
            try {
                if (termsOfQuery[i] == null || termsOfQuery[i].equals(""))
                    continue;
                String path = "";
                long pointer = 0;
                if (indexer.containsKey(termsOfQuery[i].toUpperCase())) {
                    path = indexer.get(termsOfQuery[i].toUpperCase()).getKey().split("#")[0];
                    pointer = Long.valueOf(indexer.get(termsOfQuery[i].toUpperCase()).getKey().split("#")[1]);
                } else if (indexer.containsKey(termsOfQuery[i].toLowerCase())) {
                    path = indexer.get(termsOfQuery[i].toLowerCase()).getKey().split("#")[0];
                    pointer = Long.valueOf(indexer.get(termsOfQuery[i].toLowerCase()).getKey().split("#")[1]);
                } else {
                    if (indexer.containsKey(termsOfQuery[i])) {
                        path = indexer.get(termsOfQuery[i]).getKey().split("#")[0];
                        pointer = Long.valueOf(indexer.get(termsOfQuery[i]).getKey().split("#")[1]);
                    } else
                        continue;
                }
                File f = new File(posting_path + "/" + path);
                RandomAccessFile ra = new RandomAccessFile(f, "r");
                ra.seek(pointer);
                String termInfo = ra.readLine();
                String[] temp = termInfo.split(",");
                for (int j = 0; j < temp.length; j++) {
                    String[] temp2 = temp[j].split(" ");
                    if (!qTerms.containsKey(termsOfQuery[i])) {
                        if (!temp2[2].contains("&"))
                            temp2[2] = String.valueOf(Integer.valueOf(temp2[2]) - 1);
                        Term t = new Term(termsOfQuery[i], temp2[0], Integer.valueOf(temp2[1]), temp2[2]);
                        qTerms.put(termsOfQuery[i], t);
                    } else {
                        Pair<Integer, String> pair = new Pair<>(Integer.valueOf(temp2[1]), temp2[2]);
                        Term t = qTerms.get(termsOfQuery[i]);
                        HashMap<String, Pair<Integer, String>> docs = qTerms.get(termsOfQuery[i]).getDocs();
                        docs.put(temp2[0], pair);
                        t.setDocs(docs);
                    }
                }
            }
            catch (Exception e){
                continue;
            }
        }
    }


}