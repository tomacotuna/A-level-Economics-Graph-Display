package backend;

import org.json.simple.JSONArray;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of class URLInputStream.
 */
public class URLInputStreamTest {
   @Test
    public void getWorldBankInputStream() throws Exception {
        URLInputStream urlInputStream = new URLInputStream("http://api.worldbank.org/countries?per_page=304&format=json");
        JSONArray result = urlInputStream.getWorldBankInputStream();
        assertNotNull(result);
    }

}