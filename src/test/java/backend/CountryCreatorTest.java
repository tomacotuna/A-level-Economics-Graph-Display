package backend;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for class CountryCreatorTest.
 */
public class CountryCreatorTest {

    private CountryCreator countryCreator;
    @Before
    public void initialize(){
        countryCreator = new CountryCreator();
    }


    @Test
    public void getCountries() throws Exception {
        ArrayList<String> list = countryCreator.getCountries();
        assertNotNull(list);
        int numberOfCountries = list.size();
        assertEquals(211,numberOfCountries);
        Collections.sort(list);
         String result = list.get(0);
        assertEquals("Afghanistan",result);
        result = list.get(list.size()-1);
        assertEquals("Zimbabwe",result);
    }

    @Test
    public void getCountry() throws Exception{
        Country country = countryCreator.getCountry("Brazil");
        String result = country.getId();
        assertEquals("BR",result);
    }

    @Test
    public void getQueryURL() throws Exception {
        String results = countryCreator.getQueryURL();
        assertEquals("http://api.worldbank.org/countries?per_page=304&format=json",results);
    }

}