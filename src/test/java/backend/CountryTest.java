package backend;

import org.junit.Test;

import java.util.LinkedHashMap;

import static junit.framework.TestCase.assertEquals;

/**
 * Test for class Country.
 */
public class CountryTest {

    private final static JSONObjectTester value = new JSONObjectTester("Brazil", "BR");
    private final static Country country = new Country(value);
    private final static String GDP = "Gross Domestic Product";
    private final static String GNI = "Gross National Index";
    private final static String INFLATION = "Inflation";
    private final static String BOP = "Balance Of Payments";
    private final static String FDI = "Foreign Direct Investment";
    private final static String EXPORTS = "Exports";
    private final static String IMPORTS = "Imports";


    @Test
    public void getName() throws Exception {
        String result = country.getName();
        assertEquals("Brazil",result);
    }

    @Test
    public void getId() throws Exception {
        String result = country.getId();
        assertEquals("BR",result);
    }

/*    @Test
    public void getData(){

    }*/

    @Test
    public void getIndiDataGDP() throws Exception{
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) country.getIndiData(GDP);
        String result = map.get("1980");
        assertEquals("235024598983.261", result);
    }

    @Test
    public void getIndiDataGNI() throws Exception{
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) country.getIndiData(GNI);
        String result = map.get("2007");
        assertEquals("11870",result);
    }
    @Test
    public void getIndiDataInflation() throws Exception{
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) country.getIndiData(INFLATION);
        String result = map.get("1989");
        assertEquals("1209.12132836991",result);
    }

    @Test
    public void getIndiDataBOP() throws Exception{
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) country.getIndiData(BOP);
        String result = map.get("1992");
        assertEquals("6089000000",result);
    }

    @Test
    public void getIndiDataFDI() throws Exception{
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) country.getIndiData(FDI);
        String result = map.get("1983");
        assertEquals("1609000000",result);
    }

    @Test
    public void getIndiDataExports() throws Exception{
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) country.getIndiData(EXPORTS);
        String result = map.get("1999");
        assertEquals("9.56485684156913",result);
    }

    @Test
    public void getIndiDataImports() throws Exception{
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) country.getIndiData(IMPORTS);
        String result = map.get("1991");
        assertEquals("7.91394353581263",result);
    }

    @Test
    public void getIndiDataUnemployment() throws Exception{
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) country.getIndiData("anything");
        String result = map.get("2004");
        assertEquals("8.89999961853027",result);
    }

}