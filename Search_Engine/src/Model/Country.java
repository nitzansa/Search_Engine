package Model;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class represent an object "Country".
 * In this object there are information about each Country - name, capital city, population, currency
 * and MAP with all docs that this country show in them with all its places in each doc.
 */
public class Country {
    private String CountryName;
    private String CapitalCity;
    private String Population;
    private String Currency;
    private HashMap<String, String> docs; // docs and places
    private Parse parsePopulation = new Parse();

    /**
     * Get information from json object that we get from API
     * @param information
     */
    public Country(JSONObject information) {
        CountryName = information.get("name").toString();
        CapitalCity = information.get("capital").toString();
        Population = parsePopulation.convertNumbers(information.get("population").toString());
        Currency = information.getJSONArray("currencies").getJSONObject(0).get("name").toString();
    }

    public Country(String countryName, String population, String currency, HashMap<String, String> docs) {
        CountryName = countryName;
        Population = population;
        Currency = currency;
        this.docs = docs;
        CapitalCity = "";
    }

    public String getCountryName() {
        return CountryName;
    }

    public String getCapitalCity() {
        return CapitalCity;
    }

    public String getPopulation() {
        return Population;
    }

    public String getCurrency() {
        return Currency;
    }

    public HashMap<String, String> getDocs() {
        return docs;
    }

    public void setDocs(String nameDoc, String place) {
        if(docs.containsKey(nameDoc)) {
            docs.put(nameDoc, place);
        }
        else {
            docs.put(nameDoc, place);
        }
    }

}
