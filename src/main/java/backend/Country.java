package backend;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Holds the name, id, and all query fields for a Country from the WorldBank API.
 */
public class Country {
    private String name;
    private String id;
    private LinkedHashMap<String ,String> gdp = new LinkedHashMap<String, String>(36);
    private LinkedHashMap<String ,String> gni = new LinkedHashMap<String, String>(36);
    private LinkedHashMap<String ,String> inflationGDPDeflator = new LinkedHashMap<String, String>(36);
    private LinkedHashMap<String ,String> bop = new LinkedHashMap<String, String>(36);
    private LinkedHashMap<String ,String> fdi = new LinkedHashMap<String, String>(36);
    private LinkedHashMap<String ,String> exports = new LinkedHashMap<String, String>(36);
    private LinkedHashMap<String ,String> imports = new LinkedHashMap<String, String>(36);
    private LinkedHashMap<String ,String> unemployment = new LinkedHashMap<String, String>(36);

    /**
     * Constructor
     * @param jObj a Country object from WorldBank API.
     */
    Country(JSONObject jObj){
        name = jObj.get("name").toString();
        id = jObj.get("iso2Code").toString();
        setGDP(id,getDate());
        setGNI(id,getDate());
        setInflationGDPDeflator(id,getDate());
        setBOP(id,getDate());
        setFDI(id,getDate());
        setExport(id,getDate());
        setImport(id,getDate());
        setUnemployment(id,getDate());
    }

    private String getDate(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String formattedDate = sdf.format(date);
        int year = Integer.valueOf(formattedDate);
        int yearToReturn = year - 1;
        return Integer.toString(yearToReturn);
    }


    /**
     * 'inflation GDP deflator' Builder
     * @param id the "iso2Code" of the Country WorldBank API.
     * @param finalYear the end year for querying data from API.
     */
    private void setInflationGDPDeflator(String id,String finalYear) {
        JSONArray jArray = new URLInputStream("http://api.worldbank.org/countries/"+id+"/indicators/NY.GDP.DEFL.KD.ZG?date="+finalYear+":1980&format=json").getWorldBankInputStream();
        inflationGDPDeflator = populateHashMap(jArray);
        System.out.println(id);
    }

    /**
     * 'gdp' Builder
     * @param id the "iso2Code" of the Country WorldBank API.
     * @param finalYear the end year for querying data from API.
     */
    private void setGDP(String id, String finalYear) {
        JSONArray jArray = new URLInputStream("http://api.worldbank.org/countries/"+id+"/indicators/NY.GDP.MKTP.CD?date="+finalYear+":1980&format=json").getWorldBankInputStream();
        gdp = populateHashMap(jArray);
    }

    /**
     * 'gni' Builder
     * @param id the "iso2Code" of the Country WorldBank API.
     * @param finalYear the end year for querying data from API.
     */
    private void setGNI(String id,String finalYear){
        JSONArray jArray = new URLInputStream("http://api.worldbank.org/countries/"+id+"/indicators/NY.GNP.PCAP.PP.CD?date="+finalYear+":1980&format=json").getWorldBankInputStream();
        gni = populateHashMap(jArray);
    }

    /**
     * 'Balance of payments' Builder
     * @param id the "iso2Code" of the Country WorldBank API.
     * @param finalYear the end year for querying data from API.
     */
    private void setBOP(String id,String finalYear){
        JSONArray jArray = new URLInputStream("http://api.worldbank.org/countries/"+id+"/indicators/BN.CAB.XOKA.CD?date="+finalYear+":1980&format=json").getWorldBankInputStream();
        bop = populateHashMap(jArray);
    }

    /**
     * 'Foreign direct investment' Builder
     * @param id the "iso2Code" of the Country WorldBank API.
     * @param finalYear the end year for querying data from API.
     */
    private void setFDI(String id,String finalYear){
        JSONArray jArray = new URLInputStream("http://api.worldbank.org/countries/"+id+"/indicators/BX.KLT.DINV.CD.WD?date="+finalYear+":1980&format=json").getWorldBankInputStream();
        fdi = populateHashMap(jArray);
    }

    /**
     * 'Exports' Builder
     * @param id the "iso2Code" of the Country WorldBank API.
     * @param finalYear the end year for querying data from API.
     */
    private void setExport(String id,String finalYear){
        JSONArray jArray = new URLInputStream("http://api.worldbank.org/countries/"+id+"/indicators/NE.EXP.GNFS.ZS?date="+finalYear+":1980&format=json").getWorldBankInputStream();
        exports = populateHashMap(jArray);
    }

    /**
     * 'Imports' Builder
     * @param id the "iso2Code" of the Country WorldBank API.
     * @param finalYear the end year for querying data from API.
     */
    private void setImport(String id,String finalYear){
        JSONArray jArray = new URLInputStream("http://api.worldbank.org/countries/"+id+"/indicators/NE.IMP.GNFS.ZS?date="+finalYear+":1980&format=json").getWorldBankInputStream();
        imports = populateHashMap(jArray);
    }

    /**
     * 'Unemployment' Builder
     * @param id the "iso2Code" of the Country WorldBank API.
     * @param finalYear the end year for querying data from API.
     */
    private void setUnemployment(String id,String finalYear){
        JSONArray jArray = new URLInputStream("http://api.worldbank.org/countries/"+id+"/indicators/SL.UEM.TOTL.ZS?date="+finalYear+":1980&format=json").getWorldBankInputStream();
        unemployment = populateHashMap(jArray);
    }


    /**
     * populates a <String> LinkedHashMap
     * @param jArray World Bank JSONArray
     * @return LinkedHashMap
     */
    private LinkedHashMap<String, String> populateHashMap(JSONArray jArray){
        String date,value;
        LinkedHashMap<String,String> temp = new LinkedHashMap<String, String>(36);
        for(Object o: jArray){
            JSONObject jObj =(JSONObject) o;
            if(jObj.get("date") != null){
                date = jObj.get("date").toString();
            }else {date = "";}

            if(jObj.get("value")!= null){
                value = jObj.get("value").toString();
            }else {value = "0";}

            temp.put(date,value);
        }

        return temp;
    }

    /**
     * Getter for name of Country.
     * @return name of Country.
     */
    String getName(){
        return name;
    }

    /**
     * Getter for country ID.
     * @return Country iso2Code.
     */
    String getId(){
        return id;
    }

    /**
     * Getter for indicator data.
     * @param indicator
     * @return HashMap of indicator values and corresponding year.
     *
     */
    public HashMap<String,String> getIndiData(String indicator) {
        HashMap<String,String> toReturn;
        if(indicator.equals("Gross Domestic Product")){
            toReturn = gdp;
        }else if(indicator.equals("Gross National Index")){
            toReturn = gni;
        }else if(indicator.equals("Inflation")){
            toReturn = inflationGDPDeflator;
        }else if(indicator.equals("Balance Of Payments")){
            toReturn = bop;
        }else if(indicator.equals("Foreign Direct Investment")){
            toReturn = fdi;
        }else if(indicator.equals("Exports")){
            toReturn = exports;
        }else if(indicator.equals("Imports")){
            toReturn = imports;
        }else{
            toReturn = unemployment;
        }

        return toReturn;
    }
}