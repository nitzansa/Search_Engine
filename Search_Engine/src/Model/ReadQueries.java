package Model;

import javafx.util.Pair;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.*;
/**
 * This class represent a search of a queries file.
 */

public class ReadQueries {
    HashMap<String, Pair<String, String>> indexer;
    HashSet<String> sw;
    HashMap<String, Document> docs;
    Searcher searcher;
    HashSet<String> userChooseCities;
    StringBuilder toFile;
    private List<String> docsThatReturned;


    public ReadQueries(HashMap<String, Pair<String, String>> indexer ,HashSet sw, HashMap<String, Document> docs,
                       HashSet<String> cities) throws IOException {
        this.indexer = indexer;
        this.sw = sw;
        this.docs = docs;
        this.userChooseCities = cities;
        toFile=new StringBuilder();
        docsThatReturned=new ArrayList<>();
    }

    public StringBuilder getToFile() {
        return toFile;
    }
    //Read the query file, send each query to the searcher, and save the results in the class field
    public void readQueries(String path, boolean stem, boolean semantic, String postingPath) throws IOException {
        StringBuilder text = new StringBuilder();
        File f = new File(path + "/queries.txt");
        org.jsoup.nodes.Document document;
        org.jsoup.select.Elements elements;
        document = Jsoup.parse(f, "UTF-8");
        elements = document.getElementsByTag("top");
        for (org.jsoup.nodes.Element e : elements) {
            String[] lines = e.outerHtml().split("\n");
            try {

                String qID = lines[2].split(":")[1].substring(1, lines[2].split(":")[1].length() - 1);
                text.append(e.getElementsByTag("title").text());
                String[] add = e.getElementsByTag("desc").text().replace("Description:", "").split("Narrative:");
                text.append(add[0]);
                searcher = new Searcher(indexer, sw, docs, userChooseCities, postingPath);
                searcher.searcher(text.toString(), stem, semantic);
                searcher.evaluate();
                List list = searcher.getDocsThatReturned();
                for (int i = 0; i < list.size(); i++) {
                    String[] temp = list.get(i).toString().split("=");
                    String docName = temp[0];
                    toFile.append(qID + " 0 " + docName + " 1 " + "42.38 " + "mt" + "\r\n");
                    docsThatReturned.add(docName);
                }
                text = new StringBuilder();
            }catch (Exception exc){
                continue;
            }
        }

    }

    public List<String> getDocsThatReturned() {
        return docsThatReturned;
    }
    //Write the results to a file on disk
    public void writeResults (String path) throws IOException {
        BufferedWriter test = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "\\Results_for_queries_file.txt")));
        test.write(toFile.toString());
        toFile = new StringBuilder();
        test.flush();
        test.close();
    }

}
