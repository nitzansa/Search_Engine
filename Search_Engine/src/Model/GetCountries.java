package Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class get a JSon Object with information about cities.
 * The information we put on a map with key-name of capital city and value-Country object
 */
public class GetCountries {
    private HashMap<String, Country> allCountries;

    /**
     * pass all over information and split each country and its information
     * @param allInformation
     * @throws IOException
     */
    public GetCountries(JSONObject allInformation) throws IOException {
        try {
            allCountries = new HashMap<>();
            JSONArray result = allInformation.getJSONArray("result");
            for (Object obj : result) {
                JSONObject data = (JSONObject) obj;
                Country country = new Country(data);
                allCountries.put(country.getCapitalCity(), country);
            }
        }
        catch (Exception e){
            System.out.println("error in GetCountries class");
        }

    }

    public Country getCountryByCapital(String capitalCityName) {
        return allCountries.get(capitalCityName);
    }

    public HashMap<String, Country> getAllCountries() {
        return allCountries;
    }

}
