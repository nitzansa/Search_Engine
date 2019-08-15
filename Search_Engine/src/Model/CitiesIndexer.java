
package Model;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * This class put all cities from the correct tag to MAP.
 * The key is the name of city and value is an object whit all information about this city.
 */
public class CitiesIndexer {
    private HashMap<String, Country> citiesIndexer;
    private GetCountries countriesInfo;
    private HTTPRequest http;

    public CitiesIndexer() throws IOException {
        citiesIndexer = new HashMap<>();
        http = new HTTPRequest("https://restcountries.eu/rest/v2/all");
        countriesInfo = new GetCountries(http.getJsonObject());
    }

    public HashMap<String, Country> getCitiesIndexer() {
        return citiesIndexer;
    }

    public void indexing(HashMap<String, Term> terms, String city, String docName) throws IOException {
        if(city.equals(""))
            return;
        if(terms.containsKey(city.toLowerCase()) || terms.containsKey(city.toUpperCase())){
            if(citiesIndexer.containsKey(city.toUpperCase())){
                Country c = citiesIndexer.get(city.toUpperCase());
                String places = "";
                if(terms.containsKey(city.toLowerCase())) {
                    if(terms.get(city.toLowerCase()).getDocs().get(docName) != null) {
                        c.getDocs().put(docName, terms.get(city.toLowerCase()).getDocs().get(docName).getValue());
                    }
                    else {
                        c.getDocs().put(docName, "0");
                    }
                }
                else if(terms.containsKey(city.toUpperCase())) {
                    if(terms.get(city.toUpperCase()).getDocs().get(docName) != null) {
                        c.getDocs().put(docName, terms.get(city.toUpperCase()).getDocs().get(docName).getValue());
                    }
                    else {
                        c.getDocs().put(docName, "0");
                    }
                }
                citiesIndexer.put(city.toUpperCase(), c);
            }
            else { //not yet in indexer
                String places = "";
                HashMap<String, String> docs = new HashMap<>();
                if(terms.containsKey(city.toLowerCase())) {
                    if(terms.get(city.toLowerCase()).getDocs().get(docName) != null)
                        places = terms.get(city.toLowerCase()).getDocs().get(docName).getValue();
                    else
                        places = "0";
                }
                else if(terms.containsKey(city.toUpperCase())) {
                    if(terms.get(city.toUpperCase()).getDocs().get(docName) != null)
                        places = terms.get(city.toUpperCase()).getDocs().get(docName).getValue();
                    else
                        places = "0";
                }
                docs.put(docName, places);
                Country c;
                if(countriesInfo.getCountryByCapital(city) == null){
                    c = new Country(city, "", "", docs);
                }
                else
                    c = new Country(countriesInfo.getCountryByCapital(city).getCountryName(),
                            countriesInfo.getCountryByCapital(city).getCurrency(), countriesInfo.getCountryByCapital(city).getPopulation(), docs);
                citiesIndexer.put(city.toUpperCase(), c);
            }
        }
        else {
            HashMap<String, String> docs;
            if(citiesIndexer.containsKey(city.toUpperCase())){
                docs = citiesIndexer.get(city.toUpperCase()).getDocs();
            }
            else {
                docs = new HashMap<String, String>();
            }
            docs.put(docName, "0");
            Country c;
            if(countriesInfo.getCountryByCapital(city) == null)
                c = new Country(city, "", "", docs);
            else
                c = new Country(countriesInfo.getCountryByCapital(city).getCountryName(),
                    countriesInfo.getCountryByCapital(city).getPopulation(), countriesInfo.getCountryByCapital(city).getCurrency(),
                    docs);
            citiesIndexer.put(city.toUpperCase(), c);
        }

    }

    /**
     * This function write to file the index of cities.
     * @param path - postings path
     * @throws IOException
     */
    public void writPosting (String path) throws IOException {
        StringBuilder toWrite = new StringBuilder();
        for (HashMap.Entry<String, Country> entry : citiesIndexer.entrySet()) {
            toWrite.append(entry.getKey() + "= " + entry.getValue().getCountryName() + " " + entry.getValue().getCurrency()
                    + " " + entry.getValue().getPopulation() + " " + createFullString(entry.getKey()) + "\n");
        }
        BufferedWriter test = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "citiesIndexer.txt")));
        test.write(toWrite.toString());
        toWrite = new StringBuilder();
        test.close();
    }

    private StringBuilder createFullString(String name) {
        StringBuilder temp = new StringBuilder("");
        for (HashMap.Entry<String, String> entry : citiesIndexer.get(name).getDocs().entrySet()) {
            temp.append(entry.getKey() + " " + entry.getValue() + ",");
        }
        return temp;
    }
}
