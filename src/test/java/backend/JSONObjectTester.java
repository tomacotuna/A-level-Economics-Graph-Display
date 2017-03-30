package backend;

import org.json.simple.JSONObject;

/**
 *A JSONObject builder to be used to in country test.
 */
class JSONObjectTester extends JSONObject {
    private final static String NAME = "name";
    private final static String ID = "iso2Code";

    JSONObjectTester(String name, String id){
        super();
        this.put(NAME,name );
        this.put(ID, id);
    }
}
