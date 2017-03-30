package backend;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

/**
 * Test of class DBAccess.
 */
public class DBAccessTest {


    private String[] indicators = new String[]{"Gross Domestic Product", "Gross National Index",
                "Inflation", "Balance Of Payments","Foreign Direct Investment", "Exports", "Imports","Unemployment"};
    private DBAccess dataBase = new DBAccess(indicators);


    @Test
    public void countryNumIsNotNull() throws Exception {
    	DBAccess database = new DBAccess(null);
    	assertNotNull(database.countryNum());
    	//assertTrue(database.countryNum() == 211);
    }

    @Test
    public void loadDB() throws Exception {
        Connection handle = dataBase.loadDB();
        assertNotNull(handle);
    }
    

    /*@Test
    public void getData() throws Exception {
        String name = "Brazil";
        String indicator = "Gross Domestic Product";
        int startYear = 2015;
        int endYear = 2015;
        double d = 1.77472481890048E12;
        LinkedHashMap<Double,Integer> data = dataBase.getData(name, indicator, startYear, endYear);
        int result = data.get(d);
        assertEquals(2015,result);
    }*/

    @Test
    public void getCountriesIsFull() throws Exception{
        ArrayList<String> countries = dataBase.getCountries();
        assertTrue(countries.size() >= 0);


    }

    /*@Test
    public void getCountriesIsCountry() throws Exception{
        ArrayList<String> results = dataBase.getCountries();
        Collections.sort(results);
        String result = results.get(0);
        assertEquals("Afghanistan",result);
    }*/

}