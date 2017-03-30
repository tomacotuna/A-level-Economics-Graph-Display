package backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Class representing the database. Keeps data and allows querying for visual representation.
 */
public class DBAccess {
    private ArrayList<String> countryTracker = new ArrayList<String>();
    private boolean isLoaded ;
    private String[] indicators;

    /**
     * Constructor loading database.
     * @param indicatorArray
     */
    public DBAccess(String[] indicatorArray){
        indicators = indicatorArray;
        isLoaded = false;
        try {
            loadDB();
        } catch (Exception e) {
        }
    }

    /**
     * Return size of countryTracker.
     * @return countryTracker size
     */
    public int countryNum(){
        return countryTracker.size();
    }

    /**
     * Method to load or create the database.
     * @return Connection handle
     * @throws Exception
     */
    public Connection loadDB() throws SQLException {
        Connection handle = null;
        try {
            handle = DriverManager.getConnection("jdbc:derby:database;");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                handle = DriverManager.getConnection("jdbc:derby:database;create=true");
                initDB(handle);
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
        if(!isLoaded) {
            Statement runner = handle.createStatement();
            ResultSet result = runner.executeQuery("select Name from Country");

            while (result.next()) {
                countryTracker.add(result.getString("Name"));
            }
            result.close();
            runner.close();
            isLoaded = true;
        }

        return handle;
    }

    private void initDB(Connection handle) throws Exception {
        Statement runner = handle.createStatement();
        runner.execute("CREATE TABLE Country(Name VARCHAR(30), ID VARCHAR(15) NOT NULL PRIMARY KEY)");

        for(String indicator : indicators){
            String squashed = indicator.replaceAll(" ","");
            runner.execute("CREATE TABLE "+squashed+"(ID VARCHAR(15) NOT NULL, "+squashed+" DOUBLE," +
                    " y INT NOT NULL, FOREIGN KEY (ID) REFERENCES Country(ID), PRIMARY KEY(ID,y))");
        }

    }


    /**
     * Method to add indicator data to their indicator table.
     * @param ID
     * @param values
     * @param indicator
     * @throws Exception
     */
    public void addData(String ID, HashMap<String, String> values, String indicator) throws Exception{
        Connection handle = loadDB();
        Statement runner = handle.createStatement();
        String statement = "insert into "+indicator.replaceAll(" ","")+" values ";
        for(String year : values.keySet()){
            statement += "('"+ID+"',"+values.get(year)+", "+year+"), ";
        }

        statement = statement.substring(0, statement.lastIndexOf(','));

        runner.executeUpdate(statement);

        runner.close();
        handle.close();
    }



    /**
     * Method to add the countries to a table of all the countries
     * @param countries
     * @throws Exception
     */
    public void addCountries(ArrayList<Country> countries)throws Exception{
        Connection handle = loadDB();
        String insertCountries = "INSERT INTO Country VALUES (?, ?)";
        PreparedStatement pstmt = handle.prepareStatement(insertCountries);
        for (Country c : countries) {
            if(!countryTracker.contains(c.getName().replace("'", "''"))) {
                System.out.println(c.getName());
                pstmt.setString(1, c.getName().replace("'", "''"));
                pstmt.setString(2, c.getId());
                pstmt.addBatch();
                countryTracker.add(c.getName());
            }
        }
        pstmt.executeBatch();
        pstmt.close();
        handle.close();
    }


    /**
     * Method to give data to view using select statement.
     * @param countryName
     * @param indicator
     * @param startYear
     * @param endYear
     * @throws Exception
     */
    public LinkedHashMap<Double,Integer> getData(String countryName, String indicator, int startYear, int endYear) throws Exception {
        Connection handle = loadDB();
        Statement runner = handle.createStatement();
        String squashed = indicator.replaceAll(" ","");
        LinkedHashMap<Double,Integer> toReturn = new LinkedHashMap<Double,Integer>();
        String sql = "SELECT "+ squashed +", y FROM "+ squashed +" NATURAL JOIN Country WHERE Name = '"+countryName+"' and y >= "+startYear+" and y <= "+endYear+"";
        ResultSet result = runner.executeQuery(sql);
        while(result.next()){
            double indic = result.getDouble(squashed);
            int y = result.getInt("y");
            toReturn.put(indic,y);
        }
        result.close();
        runner.close();
        handle.close();
        return toReturn;
    }

    /**
     * Getter for countries.
     * @return ArrayList of Strings, the countries.
     * @throws Exception
     */
    public ArrayList<String> getCountries() throws Exception {
        Connection handle = loadDB();
        Statement runner = handle.createStatement();
        ResultSet result = runner.executeQuery("select Name from Country");
        ArrayList<String> countries = new ArrayList<String>();
        while (result.next()) {
            countries.add(result.getString("Name"));
        }
        result.close();
        runner.close();
        handle.close();
        return countries;
    }

    /**
     * Getter to check if database is loaded.
     * @return boolean
     */
    public boolean isLoaded(){
        return isLoaded;
    }

}