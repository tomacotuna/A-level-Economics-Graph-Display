package backend;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Takes a URL(String) from the constructor.
 * Returns a JSONArray with getWorldBankInputStream().
 * URL must have JSON format request.
 *
 */
class URLInputStream {
    private String queryURL;

    /**
     * Constructor
     * @param queryURL the correct URL for a WorldBank query.
     */
    URLInputStream(String queryURL){
        this.queryURL = queryURL;
    }

    /**
     * Getter
     * @return a JSONArray of what was queried via 'queryURL' instance variable.
     */
    JSONArray getWorldBankInputStream(){
        JSONArray dataArray = new JSONArray();
        JSONParser parser = new JSONParser();
        try {
            JSONArray jsonArray = new JSONArray();
            URL urlQuery = new URL(queryURL);
            URLConnection connection = urlQuery.openConnection();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = inputStream.readLine()) != null) {
                jsonArray = (JSONArray) parser.parse(inputLine);
            }
            inputStream.close();
            dataArray = (JSONArray) jsonArray.get(1);

        } catch (Exception e) {
        }
        return dataArray;
    }
}