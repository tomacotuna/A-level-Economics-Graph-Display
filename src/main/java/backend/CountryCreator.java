package backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Creates an ArrayList of Country objects.
 * Provides getters for instance variables.
 */
class CountryCreator {
    private HashMap<String, JSONObject> countries = new HashMap<>();
    private final String queryURL = "http://api.worldbank.org/countries?per_page=304&format=json";

    /**
     * Constructor
     * Populates 'countries' instance variable.
     */
    CountryCreator() {
        ArrayList<JSONObject> jsonObjList = new ArrayList<JSONObject>();
        URLInputStream worldBankInputStream = new URLInputStream(queryURL);
        JSONArray dataArray = worldBankInputStream.getWorldBankInputStream();

        for (Object o: dataArray) {
            JSONObject jsonObj = (JSONObject) o;
            if(!jsonObj.get("latitude").toString().isEmpty()) jsonObjList.add(jsonObj);
        }

        for(JSONObject j: jsonObjList){
            countries.put(j.get("name").toString(), j);
        }
    }

    /**
     * Getter
     * @return countries
     */
     ArrayList<String> getCountries(){
        return new ArrayList<>(countries.keySet());
    }


    Country getCountry(String name){
        return new Country(countries.get(name));
    }

    /**
     * Getter
     * @return queryURL
     */
    String getQueryURL(){
        return queryURL;
    }

}