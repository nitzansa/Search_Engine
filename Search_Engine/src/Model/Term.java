package Model;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represent each Term.
 * The field "docs" represent MAP whit Key - name of doc and Value- pair with Integer who representing number of this term shows in this doc, and String with term's places in this doc.
 */
public class Term implements Serializable {

    private HashMap<String, Pair<Integer, String>> docs;
    String term;

    public Term(String nameTerm , String nameDoc, int value, String place) {
        this.docs = new HashMap<>();
        int firsPlace = 0;
        if(!place.contains("&")) {
            firsPlace = Integer.valueOf(place) + 1;
            docs.put(nameDoc, new Pair<>(value, firsPlace + ""));
        }
        else
            docs.put(nameDoc, new Pair<>(value, place));
        term = nameTerm;

    }

    public HashMap<String, Pair<Integer, String>> getDocs() {
        return docs;
    }

    public void setDocs(HashMap<String, Pair<Integer, String>> docs) {
        this.docs = docs;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm (String nameDoc, String place) {
        int fixPlace = Integer.valueOf(place) + 1;
        if(docs.containsKey(nameDoc)) {
            docs.put(nameDoc, new Pair<>(docs.get(nameDoc).getKey() + 1, docs.get(nameDoc).getValue() + "&" + fixPlace));
        }
        else {
            docs.put(nameDoc, new Pair<>( 1, fixPlace + ""));
        }
    }

    public Integer getValue(String docName) {
        return docs.get(docName).getKey();
    }

}
