package backend;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Class representing the access to the model.
 */
public final class ModelInitializer extends Service<String> {
    private static ModelInitializer instance = null;
    private backend.DBAccess database;
    private backend.CountryCreator countryCreator;
    private ArrayList<String> countryNames;
    private String[] indicators;
    private ArrayList<Country> countries;

    private boolean isLoaded = false;


    /**
     * Singleton Constructor
     */
    private ModelInitializer(){}

    /**
     * The ModelInitializer maintains a static reference
     * to the lone singleton instance.
     * @return reference from the static getInstance() method.
     */
    public static ModelInitializer getInstance(){
        if(instance == null){
            instance = new ModelInitializer();
        }
        return instance;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                indicators = new String[]{"Gross Domestic Product", "Gross National Index", "Inflation", "Balance Of Payments","Foreign Direct Investment", "Exports", "Imports","Unemployment"};
                database = new DBAccess(indicators);
                initialize();
                return null;
            }
        };
    }

    private void initialize(){
        countryCreator = new CountryCreator();
        countryNames =  countryCreator.getCountries();
        ArrayList<String> countryNamesInDB = new ArrayList<>();
        try {
            countryNamesInDB = database.getCountries();
            if(countryNames == null||countryNames.isEmpty()) countryNames = countryNamesInDB;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        final ArrayList<String> countriesInDB = countryNamesInDB;
        new Thread(()->{
            countries = new ArrayList<Country>();
            for(String name : countryNames){
                if(!countriesInDB.contains(name)){
                        addToCountries(countryCreator.getCountry(name));
                    }
                }
                try {
                    database.addCountries(countries);
                    addData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }).start();
        isLoaded = true;
    }

    private synchronized void addToCountries(Country c){
        countries.add(c);
    }

    private synchronized void addData(){
        for(backend.Country c : countries){
            System.out.println("Adding for " + c.getName());
            for(String indic : indicators) {
                String ID = c.getId();

                try {
                    database.addData(ID, c.getIndiData(indic), indic);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }

    }

    private void addData(Country c){
        for(String indic : indicators) {
            String ID = c.getId();

            try {
                database.addData(ID, c.getIndiData(indic), indic);
            } catch (Exception e) {

            }
        }
    }

    /**
     * Method to retrieve data.
     * @param countryName
     * @param indicator
     * @param startYear
     * @param endYear
     * @return HashMap of data depending on query.
     */
    public HashMap<Double,Integer> getData(String countryName, String indicator, int startYear, int endYear){
        HashMap<Double,Integer> toReturn = null;
        try {
            toReturn = database.getData(countryName,indicator,startYear,endYear);
        } catch (Exception e) {}

        try {
            if(toReturn == null || toReturn.size() < (endYear-startYear)+1){
                ArrayList<Country> country = new ArrayList<>();
                country.add(countryCreator.getCountry(countryName));
                database.addCountries(country);
                addData(country.get(0));
                toReturn = database.getData(countryName,indicator,startYear,endYear);
            }
        } catch (Exception e) {}

        return toReturn;
    }

    /**
     * Method to retrieve all countries.
     * @return ArrayList<String>
     */
    public ArrayList<String> getCountries() {
        ArrayList<String> countryList = new ArrayList<String>();
        if(countryNames != null) {
            countryList = new ArrayList<>(countryNames);
        }
        Collections.sort(countryList);
        return countryList;
    }

    /**
     * Getter to check if database is loaded.
     * @return boolean
     */
    public boolean isLoaded(){
      return isLoaded;
    }

    /**
     * Method to retrieve all indicators
     * @return String[]
     */
    public String[] getIndicators() { return indicators;}

    /**
     * Setter for indicator array.
     * @param indicators
     */
	public void setIndicators(String[] indicators) { this.indicators = indicators;}

    /**
     * Getter for the hook.
     * @return EventHandler<WindowEvent>.
     */
    public EventHandler<WindowEvent> getHook() {

        return (event -> {
            new Thread(()->{
                try {
                    if(countries != null){
                        database.addCountries(countries);
                        addData();
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }).start();

        });
    }
}
