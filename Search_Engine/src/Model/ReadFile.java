package Model;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import java.io.*;
import java.util.*;
/**
 * This class represent a read of one file, that can includes many files inside.
 */


public class ReadFile {
    private File[] files;
    private Model.Parse parse;
    private Indexer indexer;
    private CitiesIndexer citiesIndexer;
    private HashSet sw;
    private HashMap<String, Document> docs;
    public boolean stem;
    private HashSet<String> languages;
    private HashSet<String> cities;
    private String posting_path;
    private String corpus_path;
    private int sizeOfDocs;

    public int getSizeOfDocs() {
        return sizeOfDocs;
    }

    public HashSet<String> getCities() {
        return cities;
    }

    public ReadFile(boolean stem, String posting_path, String corpus_path) throws IOException {
        this.corpus_path = corpus_path;
        this.posting_path = posting_path + "/posting/";
        File directory = new File(this.posting_path);
        directory.mkdir();
        if(stem)
            this.posting_path = this.posting_path + "Stemmer_";
        parse = new Parse();
        indexer = new Indexer();
        citiesIndexer = new CitiesIndexer();
        sw = new HashSet();
        docs = new HashMap<>();
        languages = new HashSet<>();
        this.stem = stem;
        cities=new HashSet<>();
    }

    public HashSet getSw() {
        return sw;
    }

    public Indexer getIndexer() {
        return indexer;
    }

    public HashMap<String, Document> getDocs() {
        return docs;
    }

    public void setStem(Boolean stem) {
        this.stem = stem;
    }

    public HashSet<String> getLanguages() {
        return languages;
    }
    //The general and central function in the first part of the project. In which all the files are read in the selected folder,
    // performing the parsing and indexing, while updating the postings files and then merging them into one final posting file.
    // While updating the main dictionary and document dictionary.
    public void readFile() throws IOException {
        readStopWords(corpus_path + "/stop_words.txt");
        String text = "";
        File resourceDirectory = new File(corpus_path);
        File[] allDirectory = resourceDirectory.listFiles();
        org.jsoup.nodes.Document document;
        org.jsoup.select.Elements elements;
        int i = 0;
        int numOfPosting = 0;
        for (File dir : allDirectory) {
            if(!dir.isDirectory())
                continue;
            files = dir.listFiles();
            for (File f : files) {
                document = Jsoup.parse(f, "UTF-8");
                elements = document.getElementsByTag("DOC");
                for (org.jsoup.nodes.Element e : elements) { //each "e" is one doc in file
                    String docName = e.getElementsByTag("DOCNO").text();
                    String title =  e.getElementsByTag("TI").text();
                    Parse p = new Parse();
                    Document temporary= new Document("temp", "", "", 0, 0,new HashSet<>());
                    p.parsing(temporary,title,sw,stem);
                    String[] temp = e.outerHtml().split("<f p=\"104\">");
                    String city = getCities(temp);
                    if (!city.equals(""))
                        cities.add(city.toUpperCase());
                    temp = e.outerHtml().split("<f p=\"105\">"); //language
                    fillLanguages(temp);
                    HashSet<String> toAdd=new HashSet<>();
                    toAdd.addAll(p.getTerms().keySet());
                    Document doc = new Document(docName, city.toUpperCase(), e.getElementsByTag("DATE1").text(), 0,0,toAdd);
                    text = e.select("TEXT").text();
                    parse.parsing(doc, text, sw, stem);
                    parse.setPlace(0);
                    docs.put(docName, doc); //*****
                    citiesIndexer.indexing(parse.getTerms(), city, docName);
                    doc.setTermsPerDoc(parse.getTermsPerDoc());
                    parse.getTermsPerDoc().clear();
                    doc.sort5things();
                }
            }
            i++;
            if (i >= 40){
                numOfPosting++;
                indexer.indexing(parse.getTerms(), posting_path + numOfPosting, stem, citiesIndexer.getCitiesIndexer());
                parse.startAgain();
                i = 0;
            }
        }
        if(i!=0){
            numOfPosting++;
            indexer.indexing(parse.getTerms(), posting_path + numOfPosting, stem, citiesIndexer.getCitiesIndexer());
            parse.startAgain();
        }

        sizeOfDocs =docs.size();
        FileOutputStream fout = new FileOutputStream(posting_path+"docs.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(docs);
        fout.flush();
        fout.close();
        oos.flush();
        oos.close();
        docs=new HashMap<String, Document>();
        FileOutputStream fout3 = new FileOutputStream(posting_path+"cities.txt");
        ObjectOutputStream oos3 = new ObjectOutputStream(fout3);
        oos3.writeObject(cities);
        fout3.flush();
        fout3.close();
        oos3.flush();
        oos3.close();
        cities.clear();
        citiesIndexer.writPosting(posting_path);

        FileOutputStream fout4 = new FileOutputStream(posting_path+"languages.txt");
        ObjectOutputStream oos4 = new ObjectOutputStream(fout4);
        oos4.writeObject(languages);
        fout4.flush();
        fout4.close();
        oos4.flush();
        oos4.close();
        languages.clear();

        indexer.finalPosting(posting_path,"a",numOfPosting, stem);
        indexer.finalPosting(posting_path,"b",numOfPosting, stem);
        indexer.finalPosting(posting_path,"c",numOfPosting, stem);
        indexer.finalPosting(posting_path,"d",numOfPosting, stem);
        indexer.finalPosting(posting_path,"e_f",numOfPosting, stem);
        indexer.finalPosting(posting_path,"g_h",numOfPosting, stem);
        indexer.finalPosting(posting_path,"i_k",numOfPosting, stem);
        indexer.finalPosting(posting_path,"l_m",numOfPosting, stem);
        indexer.finalPosting(posting_path,"n_o",numOfPosting, stem);
        indexer.finalPosting(posting_path,"p",numOfPosting, stem);
        indexer.finalPosting(posting_path,"q_r",numOfPosting, stem);
        indexer.finalPosting(posting_path,"s",numOfPosting, stem);
        indexer.finalPosting(posting_path,"t",numOfPosting, stem);
        indexer.finalPosting(posting_path,"u_z",numOfPosting, stem);
        indexer.finalPosting(posting_path,"prices_percents",numOfPosting, stem);
        indexer.finalPosting(posting_path,"numbers",numOfPosting, stem);
        sorting(indexer.getIndexer());


        FileOutputStream fout2 = new FileOutputStream(posting_path+"sw.txt");
        ObjectOutputStream oos2 = new ObjectOutputStream(fout2);
        oos2.writeObject(sw);
        fout2.flush();
        fout2.close();
        oos2.flush();
        oos2.close();

    }
    //Fill in the stop_words dictionary
    public void readStopWords(String path) throws IOException {
        File f = new File(path);
        StringBuilder allText = new StringBuilder();
        FileReader fileReader = new FileReader(f);
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null)
                allText = allText.append(line + "\n");
        }
        String[] stopWords = allText.toString().split("\n");
        for (int i = 0; i < stopWords.length; i++) {
            sw.add(stopWords[i]);
        }
    }

    /**
     * Sort the final dictionary
     * @param unsortedMap
     * @throws IOException
     */
    public void sorting(HashMap<String,Pair<String,String>> unsortedMap) throws IOException {
        StringBuilder insertToFile=new StringBuilder();
        TreeMap<String,Pair<String,String>> sortedMap = new TreeMap<>();
        sortedMap.putAll(unsortedMap);
        StringBuilder text=new StringBuilder();
        for (HashMap.Entry<String, Pair<String,String>> entry : sortedMap.entrySet()) {
            insertToFile.append(entry.getKey()+"="+entry.getValue().getValue().split("#")[1]+"\r"+"\n");
        }

        FileOutputStream fout = new FileOutputStream(posting_path+"dictionary.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(unsortedMap);
        fout.flush();
        fout.close();
        oos.flush();
        oos.close();

        BufferedWriter mini = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(posting_path+"sorted_Dictionary.txt")));
        mini.write(insertToFile.toString());
        mini.flush();
        mini.close();
        insertToFile=new StringBuilder("");

    }

    /**
     * Delete the postings file
     * @param postingsPath
     */
    public void deletePosting (String postingsPath){
        File resourceDirectory = new File(postingsPath);
        File[] allDirectory = resourceDirectory.listFiles();
        for (File f: allDirectory) {
            try {
                f.delete();
            } catch (Exception e) {
                continue;
            }
        }
    }

    private String getCities (String[] fromTag){
        String city = "";
        String[] temp2;
        if(fromTag.length > 1) {
            temp2 = fromTag[1].split("</f>");
            city = parse.cutOffDelimiters(temp2[0]);
            String[] firstWord = city.split(" ");
            city = firstWord[0];
        }
        return city;
    }
    //Fill in the languages dictionary
    private void fillLanguages (String[] fromTag){
        String[] temp2;
        String language = "";
        if(fromTag.length > 1) {
            temp2 = fromTag[1].split("</f>");
            language = parse.cutOffDelimiters(temp2[0]);
            boolean ans = true;
            for (int j = 0; j < language.length() && ans; j++) {
                if(Character.isDigit(language.charAt(j)))
                    ans = false;
            }
            if (ans)
                languages.add(language);
        }
    }

}
