package Model;

import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * This class is responsible for inserting all the trams after parsing into the dictionary and the posting files
 */
public class Indexer {

    private HashMap<String, Pair<String, String>> indexer;

    public Indexer() {
        indexer = new HashMap<>();
    }

    /**
     * This function get map with terms after parsing and write them to temporary files.
     * Each term written in a specific file according to his first letter.
     * @param terms
     * @param tempPath
     * @param cities for know if term is a city and available in cities indexer
     * @throws IOException
     */
    public void indexing(HashMap<String, Term> terms, String tempPath, boolean stem, HashMap<String, Country> cities) throws IOException {
        BufferedWriter writer_a = new BufferedWriter(new FileWriter(tempPath + "a.txt", true));
        BufferedWriter writer_b = new BufferedWriter(new FileWriter(tempPath + "b.txt", true));
        BufferedWriter writer_c = new BufferedWriter(new FileWriter(tempPath + "c.txt", true));
        BufferedWriter writer_d = new BufferedWriter(new FileWriter(tempPath + "d.txt", true));
        BufferedWriter writer_e_f = new BufferedWriter(new FileWriter(tempPath + "e_f.txt", true));
        BufferedWriter writer_g_h = new BufferedWriter(new FileWriter(tempPath + "g_h.txt", true));
        BufferedWriter writer_i_k = new BufferedWriter(new FileWriter(tempPath + "i_k.txt", true));
        BufferedWriter writer_l_m = new BufferedWriter(new FileWriter(tempPath + "l_m.txt", true));
        BufferedWriter writer_n_o = new BufferedWriter(new FileWriter(tempPath + "n_o.txt", true));
        BufferedWriter writer_p = new BufferedWriter(new FileWriter(tempPath + "p.txt", true));
        BufferedWriter writer_q_r = new BufferedWriter(new FileWriter(tempPath + "q_r.txt", true));
        BufferedWriter writer_s = new BufferedWriter(new FileWriter(tempPath + "s.txt", true));
        BufferedWriter writer_t = new BufferedWriter(new FileWriter(tempPath + "t.txt", true));
        BufferedWriter writer_u_z = new BufferedWriter(new FileWriter(tempPath + "u_z.txt", true));;
        BufferedWriter writer_prices_percents = new BufferedWriter(new FileWriter(tempPath + "prices_percents.txt", true));
        BufferedWriter writer_numbers = new BufferedWriter(new FileWriter(tempPath + "numbers.txt", true));
        String posPath = "/posting/";
        if(stem)
            posPath = "/posting/Stemmer_";
        int i = 0;
        for (HashMap.Entry<String, Term> entry : terms.entrySet()) {
            i++;
            if (cities.containsKey(entry.getKey().toUpperCase())) // save cities only in indexer of cities
                continue;
            StringBuilder fullString = createFullString(entry.getKey(), entry.getValue());
            if (entry.getKey().charAt(0) == 's' || entry.getKey().charAt(0) == 'S') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "s.txt");
                writer_s.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'c' || entry.getKey().charAt(0) == 'C') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "c.txt");
                writer_c.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'm' || entry.getKey().charAt(0) == 'M' || entry.getKey().charAt(0) == 'l' || entry.getKey().charAt(0) == 'L') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "l_m.txt");
                writer_l_m.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'e' || entry.getKey().charAt(0) == 'E' || entry.getKey().charAt(0) == 'f'
                    || entry.getKey().charAt(0) == 'F') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "e_f.txt");
                writer_e_f.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'p' || entry.getKey().charAt(0) == 'P') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "p.txt");
                writer_p.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'q' || entry.getKey().charAt(0) == 'Q' || entry.getKey().charAt(0) == 'r'
                    || entry.getKey().charAt(0) == 'R') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "q_r.txt");
                writer_q_r.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'a' || entry.getKey().charAt(0) == 'A') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "a.txt");
                writer_a.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'u' || entry.getKey().charAt(0) == 'U' || entry.getKey().charAt(0) == 'v'
                    || entry.getKey().charAt(0) == 'V' || entry.getKey().charAt(0) == 'w' || entry.getKey().charAt(0) == 'W'
                    || entry.getKey().charAt(0) == 'x' || entry.getKey().charAt(0) == 'X' || entry.getKey().charAt(0) == 'y'
                    || entry.getKey().charAt(0) == 'Y' || entry.getKey().charAt(0) == 'z' || entry.getKey().charAt(0) == 'Z') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "u_z.txt");
                writer_u_z.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'i' || entry.getKey().charAt(0) == 'I' || entry.getKey().charAt(0) == 'j'
                    || entry.getKey().charAt(0) == 'J' || entry.getKey().charAt(0) == 'k' || entry.getKey().charAt(0) == 'K') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "i_k.txt");
                writer_i_k.append(fullString.toString() + "\n");
                continue;
            }

            if (entry.getKey().charAt(0) == 'g' || entry.getKey().charAt(0) == 'G' || entry.getKey().charAt(0) == 'h'
                    || entry.getKey().charAt(0) == 'H') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "g_h.txt");
                writer_g_h.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'd' || entry.getKey().charAt(0) == 'D') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "d.txt");
                writer_d.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'b' || entry.getKey().charAt(0) == 'B') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "b.txt");
                writer_b.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 't' || entry.getKey().charAt(0) == 'T') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "t.txt");
                writer_t.append(fullString.toString() + "\n");
                continue;
            }
            if (entry.getKey().charAt(0) == 'n' || entry.getKey().charAt(0) == 'N' || entry.getKey().charAt(0) == 'o'
                    || entry.getKey().charAt(0) == 'O') {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "n_o.txt");
                writer_n_o.append(fullString.toString() + "\n");
                continue;
            }
            if(entry.getKey().contains("Dollars") || entry.getKey().contains("%")) {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "prices_percents.txt");
                writer_prices_percents.append(fullString.toString() + "\n");
                continue;
            }
            if(Character.isDigit(entry.getKey().charAt(0))) {
                insertToDictionary(entry.getKey(), entry.getValue(), posPath + "numbers.txt");
                writer_numbers.append(fullString.toString() + "\n");
                continue;
            }
        }
        writer_a.flush();
        writer_b.flush();
        writer_c.flush();
        writer_d.flush();
        writer_e_f.flush();
        writer_g_h.flush();
        writer_i_k.flush();
        writer_l_m.flush();
        writer_n_o.flush();
        writer_p.flush();
        writer_q_r.flush();
        writer_s.flush();
        writer_t.flush();
        writer_u_z.flush();
        writer_prices_percents.flush();
        writer_numbers.flush();
        writer_a.close();
        writer_b.close();
        writer_c.close();
        writer_d.close();
        writer_e_f.close();
        writer_g_h.close();
        writer_i_k.close();
        writer_l_m.close();
        writer_n_o.close();
        writer_p.close();
        writer_q_r.close();
        writer_s.close();
        writer_t.close();
        writer_u_z.close();
        writer_prices_percents.close();
        writer_numbers.close();
    }
    /**
     * This function get one term and first check if its exist already and put the term in a dictionary with correct value.
     * @param nameTerm
     * @param term
     * @param path - pointer for term to its posting file
     */
    private void insertToDictionary(String nameTerm, Term term, String path) {
        int numberOfTermInCorpus = 0;
        String toAdd = "";
        int numberOfDocs = term.getDocs().size();
        for (HashMap.Entry<String, Pair<Integer, String>> entry : term.getDocs().entrySet()) {
            numberOfTermInCorpus = numberOfTermInCorpus + entry.getValue().getKey();
        }
        if (indexer.containsKey(nameTerm.toLowerCase()) && nameTerm.matches("(([a-z])([a-z]+))")) {
            String[] temp = indexer.get(nameTerm.toLowerCase()).getValue().split("#");
            int first = Integer.valueOf(temp[0]) + numberOfDocs;
            int second = Integer.valueOf(temp[1]) + numberOfTermInCorpus;
            toAdd = first + "#" + second;
            Pair<String, String> pair = new Pair<>(path, toAdd);
            indexer.put(nameTerm, pair);
            return;
        } else if (indexer.containsKey(nameTerm.toLowerCase()) && nameTerm.matches("(([A-Z])([A-Z]+))")) {
            String[] temp = indexer.get(nameTerm.toLowerCase()).getValue().split("#");
            int first = Integer.valueOf(temp[0]) + numberOfDocs;
            int second = Integer.valueOf(temp[1]) + numberOfTermInCorpus;
            toAdd = first + "#" + second;
            Pair<String, String> pair = new Pair<>(path, toAdd);
            indexer.put(nameTerm.toLowerCase(), pair);
            return;
        } else if (indexer.containsKey(nameTerm.toUpperCase()) && nameTerm.matches("(([a-z])([a-z]+))")) {
            String[] temp = indexer.get(nameTerm.toUpperCase()).getValue().split("#");
            int first = Integer.valueOf(temp[0]) + numberOfDocs;
            int second = Integer.valueOf(temp[1]) + numberOfTermInCorpus;
            toAdd = first + "#" + second;
            Pair<String, String> pair = new Pair<>(path, toAdd);
            indexer.put(nameTerm, pair);
            indexer.remove((nameTerm.toUpperCase()));
            return;
        } else if (indexer.containsKey(nameTerm.toUpperCase()) && nameTerm.matches("(([A-Z])([A-Z]+))")) {
            String[] temp = indexer.get(nameTerm.toUpperCase()).getValue().split("#");
            int first = Integer.valueOf(temp[0]) + numberOfDocs;
            int second = Integer.valueOf(temp[1]) + numberOfTermInCorpus;
            toAdd = first + "#" + second;
            Pair<String, String> pair = new Pair<>(path, toAdd);
            indexer.put(nameTerm, pair);
            return;
        } else if(indexer.containsKey(nameTerm)) {
            String[] temp = indexer.get(nameTerm).getValue().split("#");
            int first = Integer.valueOf(temp[0]) + numberOfDocs;
            int second = Integer.valueOf(temp[1]) + numberOfTermInCorpus;
            toAdd = first + "#" + second;
            Pair<String, String> pair = new Pair<>(path, toAdd);
            indexer.put(nameTerm, pair);
            return;
        }
        else {
            toAdd = numberOfDocs + "#" + numberOfTermInCorpus;
            Pair<String, String> pair = new Pair<>(path, toAdd);
            indexer.put(nameTerm, pair);
        }

    }
    /**
     * This function passing on term and put in one string all term's information
     * @param name
     * @param term
     * @return
     */
    private StringBuilder createFullString(String name, Term term) {
        StringBuilder temp = new StringBuilder("");
        temp.append(name + "=");
        for (HashMap.Entry<String, Pair<Integer, String>> entry : term.getDocs().entrySet()) {
            temp.append(entry.getKey() + " " + entry.getValue().getKey() + " " + entry.getValue().getValue() + ",");
        }
        return temp;
    }
    //Read and return a single file
    private StringBuilder readOneFile(String path) throws FileNotFoundException {
        File f = new File(path);
        FileReader fileReader = new FileReader(f);
        StringBuilder newText = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                newText.append(line + "\n");
            }
            fileReader.close();
        } catch (IOException e) {
            return newText.append("");
        }
        return newText;
    }
    //Combines two text files
    private StringBuilder merge(String pathA, String pathB) throws IOException {
        StringBuilder textA = readOneFile(pathA);
        StringBuilder textB = readOneFile(pathB);
        StringBuilder newText = new StringBuilder();
        newText.append(textA.toString() + textB.toString());
        return duplicates(newText);
    }
    /**
     * This function read 2 files each time and return the united string without duplicates.
     * @param text
     * @return
     * @throws IOException
     */
    private StringBuilder duplicates(StringBuilder text) throws IOException {
        HashMap<String, String> terms = new HashMap<>();
        StringBuilder newText = new StringBuilder();
        if(text.length() < 1)
            return newText.append("");
        String[] lines = text.toString().split("\n");
        String temp = "";
        try {
            for (int i = 0; i < lines.length; i++) {
                String[] array = lines[i].split("=");

                if (terms.containsKey(array[0].toLowerCase()) && array[0].matches("(([a-z])([a-z]+))")) {
                    terms.put(array[0], terms.get(array[0].toLowerCase()) + array[1]);
                    continue;
                } else if (terms.containsKey(array[0].toLowerCase()) && array[0].matches("(([A-Z])([A-Z]+))")) {
                    terms.put(array[0].toLowerCase(), terms.get(array[0].toLowerCase()) + array[1]);
                    continue;
                } else if (terms.containsKey(array[0].toUpperCase()) && array[0].matches("(([a-z])([a-z]+))")) {
                    terms.put(array[0].toLowerCase(), terms.get(array[0].toUpperCase()) + array[1]);
                    terms.remove(array[0].toUpperCase());
                    continue;
                } else if (terms.containsKey(array[0].toUpperCase()) && array[0].matches("(([A-Z])([A-Z]+))")) {
                    terms.put(array[0], terms.get(array[0].toUpperCase()) + array[1]);
                    continue;
                } else if (terms.containsKey(array[0])){
                        terms.put(array[0], terms.get(array[0]) + array[1]);
                    continue;
                }
                else {
                    terms.put(array[0],array[1]);
                    continue;
                }
            }
            for (HashMap.Entry<String, String> entry : terms.entrySet()) {
                newText.append(entry.getKey() + "=" + entry.getValue() + "\n");
            }
            terms = new HashMap<>();
            return newText;
        } catch (Exception e) {
            return newText.append("");
        }
    }
    //Combines all temporary postings files into one final posting file, taking into account duplicates.
    // When you have finished deleting all temporary postings files
    public void finalPosting(String path, String letter, int iterationNumber, boolean stemmer) throws IOException {
            int place = 0;
            int numberOfPosting = iterationNumber;
            int numberOfDocsUpdate = iterationNumber / 2;
            StringBuilder text = new StringBuilder();
            if (iterationNumber % 2 != 0) {
                numberOfDocsUpdate++;
            }
            StringBuilder[] array = new StringBuilder[numberOfDocsUpdate];
            for (int i = 1; i < iterationNumber; i = i + 2) { //read to string all posting files of one letter
                text = merge(path + i + letter + ".txt",
                        path + (i + 1) + letter + ".txt");
                array[place] = text;
                text = new StringBuilder();
                place++;
            }
            if (iterationNumber % 2 != 0) { //read to string the final file of this letter
                array[place] = readOneFile(path + iterationNumber + letter + ".txt");
            }
            place = 0;
            iterationNumber = numberOfDocsUpdate;
            while (numberOfDocsUpdate > 1) {
                for (int i = 0; i < numberOfDocsUpdate - 1; i = i + 2) {
                    text.append(array[i]);
                    text.append(array[i + 1]);
                    array[place] = duplicates(text);
                    text = new StringBuilder();
                    place++;
                }
                if (numberOfDocsUpdate == 2) {
                    array[1].setLength(0);
                    break;
                }
                numberOfDocsUpdate = numberOfDocsUpdate / 2;
                if (iterationNumber % 2 != 0) {
                    array[place] = new StringBuilder(array[iterationNumber - 1]);
                    numberOfDocsUpdate++;
                    place++;
                }
                for (int i = place; i < array.length && i != 0; i++) {
                    if (array[i].length() != 0)
                        array[i].setLength(0);
                    else
                        break;
                }
                place = 0;
                iterationNumber = numberOfDocsUpdate;
            }

            String term = "";
            String[] withoutName = array[0].toString().split("\n");
            array[0] = new StringBuilder();
            StringBuilder toReturn = new StringBuilder("");
            long pointer = 0;
            for (int i = 0; i < withoutName.length; i++) {
                int index = withoutName[i].indexOf('=');
                if(index > 0)
                    term = withoutName[i].substring(0, index);
                else
                    continue;
                withoutName[i] = withoutName[i].substring(index + 1);
                try {
                    String firstValue = indexer.get(term).getKey() + "#" + pointer;
                    Pair pair = new Pair(firstValue, indexer.get(term).getValue());
                    indexer.put(term, pair);
                }
                catch (Exception e){
                    continue;
                }
                toReturn.append(withoutName[i] + "\n");
                pointer = pointer + withoutName[i].getBytes().length + 1;
                withoutName[i] = "";
            }

            FileOutputStream file = new FileOutputStream(path + letter + ".txt");
            OutputStreamWriter outputStream = new OutputStreamWriter(file);
            BufferedWriter test = new BufferedWriter(outputStream);
            test.write(toReturn.toString());
            test.flush();
            test.close();
            file.flush();
            file.close();
            outputStream.close();
            text.setLength(0);
            array[0].setLength(0);
            //toReturn.setLength(0);

            for (int i = 1; i <= numberOfPosting; i++) {
                Path p = Paths.get(path + i + letter + ".txt");
                try {
                    Files.delete(p);
                } catch (Exception e) {
                    continue;
                }
            }
    }

    public HashMap<String, Pair<String, String>> getIndexer() {
        return indexer;
    }
}