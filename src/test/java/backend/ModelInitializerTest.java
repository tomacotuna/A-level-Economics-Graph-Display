package backend;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;


/**
 * Test for Class ModelInitializer.
 */
public class ModelInitializerTest {
	
    private ModelInitializer model;

    @Before
    public void initialize() {
        model = ModelInitializer.getInstance();
    }

    @Test
    public void testGetInstanceIsNotNull() throws Exception {
    	assertNotNull(ModelInitializer.getInstance());
    }
    
    @Test
    public void testGetIndicators() throws Exception {
        String[] indicators = new String[]{"Gross Domestic Product", "Gross National Index",
                "Inflation", "Balance Of Payments","Foreign Direct Investment", "Exports", "Imports","Unemployment"};
    	model.setIndicators(indicators);
    	String[] results = model.getIndicators();
        assertEquals(indicators,results);
    }
    
    @Test
    public void testGetIndicatorsIsValid() throws Exception {
    	String[] indicators = {"GDP"};
    	model.setIndicators(indicators);
    	String[] results = model.getIndicators();
    	String result = results[0];
        assertEquals("GDP",result);
    }

    /*@Test
    public void getData() throws Exception {

        String name = "Brazil";
        String indicator = "GDP";
        int startYear = 2015;
        int endYear = 2015;
        double d = 1.77472481890048E12;
        HashMap<Double, Integer> data = model.getData(name, indicator, startYear, endYear);
        int result = data.get(d);
        assertEquals(2015,result);
    }*/

   /* @Test
    public void getCountries() throws Exception {
        ArrayList<String> countries = model.getCountries();
        assertTrue(countries.size() == 211);
        Collections.sort(countries);
        String result = countries.get(0);
        assertEquals("Afghanistan", result);
    }
    */



}