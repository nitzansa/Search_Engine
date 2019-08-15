package Controller;
import Model.Document;
import Model.ReadFile;
import Model.ReadQueries;
import Model.Searcher;
import View.View;
import javafx.util.Pair;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Controller {
    public ReadFile readFile;
    public Searcher searcher;
    public ReadQueries rq;
    public HashSet<String> sw;
    public HashMap<String, Document> docs;
    public View view;
    public HashSet<String> languages;
    public HashSet<String> cities;
    public HashMap<String, Pair<String,String>> dictionary;
    public String time;
    public HashSet<String> cityAfterFilter;

    public Controller() throws IOException {
        languages = new HashSet<>();
        dictionary = new HashMap<>();
        cities=new HashSet<>();
        cityAfterFilter = new HashSet<>();
        languages = new HashSet<>();

    }
    //Running the parser and indexer on the corpus
    public void start(String corpusPath, String postingsPath, boolean stem) throws IOException {
        readFile = new ReadFile(stem, postingsPath, corpusPath);
        readFile.setStem(stem);
        long s = System.nanoTime();
        readFile.readFile();
        long d = System.nanoTime();
        time = (d-s)/1000000000 + "";
        languages = readFile.getLanguages();
        cities = readFile.getCities();
    }
    //Displays a message after first part of the project is finished to run
    public String getAlert (){
        String info = "The number of documents that were indexed: " + readFile.getSizeOfDocs() + "\n"
                + "Unique terms: " + readFile.getIndexer().getIndexer().size() + "\n" + "Total time: " + time + "second";
        return info;
    }
    //Resetting the objects to restart the program
    public void reset(String postingsPath) throws IOException {
        readFile.deletePosting(postingsPath);
        readFile = new ReadFile(false, "", "");
    }
    //Loads the objects into memory
    public void loadDic(String postingsPath, boolean stem) throws IOException, ClassNotFoundException {
        String dicPath = "";
        String docsPath= "";
        if(stem) {
            dicPath = "/posting/Stemmer_dictionary.txt";
            docsPath = "/posting/Stemmer_docs.txt";
        }
        else {
            dicPath = "/posting/dictionary.txt";
            docsPath = "/posting/docs.txt";
        }
        FileInputStream fin = new FileInputStream(postingsPath + dicPath);
        ObjectInputStream ois = new ObjectInputStream(fin);
        dictionary = (HashMap<String, Pair<String,String>>) ois.readObject();
        fin.close();
        ois.close();
        FileInputStream fin1 = new FileInputStream(postingsPath + docsPath);
        ObjectInputStream ois1 = new ObjectInputStream(fin1);
        docs = (HashMap<String, Document>) ois1.readObject();
        fin1.close();
        ois1.close();
        FileInputStream fin2 = new FileInputStream(postingsPath + "/posting/sw.txt");
        ObjectInputStream ois2 = new ObjectInputStream(fin2);
        sw = (HashSet<String>) ois2.readObject();
        fin2.close();
        ois2.close();
        FileInputStream fin3 = new FileInputStream(postingsPath + "/posting/cities.txt");
        ObjectInputStream ois3 = new ObjectInputStream(fin3);
        cities = (HashSet<String>) ois3.readObject();
        fin3.close();
        ois3.close();
        FileInputStream fin4 = new FileInputStream(postingsPath + "/posting/languages.txt");
        ObjectInputStream ois4 = new ObjectInputStream(fin4);
        languages = (HashSet<String>) ois4.readObject();
        fin4.close();
        ois4.close();
    }
    //Single query search function
    public StringBuilder search(String text, HashSet<String> cities, boolean stem, boolean semantic, String path) throws IOException {
        searcher = new Searcher(dictionary, sw, docs, cities, path);
        searcher.searcher(text, stem, semantic);
        searcher.evaluate();
        List<String> doc=searcher.getDocsThatReturned();
        StringBuilder docsTotal=new StringBuilder();
        docsTotal.append("retrieval docs:\n\n");
        int wordInRow=0;
        int add=1;
        for(int i=0;i<doc.size();i++) {
            if (wordInRow == 3) {
                docsTotal.append("\n" + add + ". " + doc.get(i) + "  ");
                wordInRow = 1;
                add++;
            } else {
                docsTotal.append(add + ". " + doc.get(i) + "  ");
                wordInRow++;
                add++;
            }
        }
        return docsTotal;
    }

    public HashSet<String> getLanguages(){
        return languages;
    }
    //Concatenates and ranks the five most dominant entities in the document to show the user the entities of a selected document
    public String sotr5things(String docName){
        String docAndValue="entity for "+ docName+ ":\n\n";
        String[]arrayOfEntity=docs.get(docName).getFiveOrLessWords();
        int[]arrayOfValues=docs.get(docName).getFiveOrLessValue();
        for(int i=0;i<arrayOfEntity.length;i++){
            if(arrayOfValues[i]==0)
                return docAndValue;
            else {
                docAndValue=docAndValue + " entity: " + arrayOfEntity[i] + ", rank: " +arrayOfValues[i]+"\n" ;
            }
        }
        return docAndValue;
    }
    //Save search results to file on disk, if check==true its the results of single query, else its results of queries file
    public void save(String path,boolean check) throws IOException {
        if(check) {
            searcher.writeResults(path);
        }
        else
            rq.writeResults(path);

    }
    //queries file search function
    public boolean searchFromfile(String path, HashSet<String> cities, boolean stem, boolean semantic, String postingPath) throws IOException {
        Path p = Paths.get(path + "/queries.txt");
        if(Files.exists(p)) {
            rq = new ReadQueries(dictionary, sw, docs, cities);
            rq.readQueries(path, stem, semantic, postingPath);
            StringBuilder addToFiles = rq.getToFile();
            BufferedWriter test = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "\\showResults.txt")));
            test.write(addToFiles.toString());
            addToFiles = new StringBuilder();
            test.flush();
            test.close();
            File f = new File(path + "\\showResults.txt");
            Desktop.getDesktop().open(f);
            return true;
        }
        else
            return false;
    }

    public List<String> getReturnDocs(){
        return searcher.getDocsThatReturned();
    }

}

