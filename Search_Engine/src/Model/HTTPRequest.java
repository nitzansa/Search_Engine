package Model;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
/**
 * This class represent an http request for using some site in the internet.
 */

public class HTTPRequest {
    private  JSONObject jsonObject;

    public HTTPRequest(String url) throws IOException {
        try {
            URL address = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) address.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            String json = "{\"result\":";
            Scanner scan = new Scanner(address.openStream());
            while (scan.hasNext())
                json += scan.nextLine();
            scan.close();
            json += "}";
            jsonObject = new JSONObject(json);
        }
        catch (Exception e){

        }
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
