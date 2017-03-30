package Controller;

import backend.ModelInitializer;
import front.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Hyperlink;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import java.util.*;

/**
 * 
 * @author MaxHein and team, Is the main application launch point. combines all aspects of the project thus allowing the passing 
 * 							 of data from the backed to the views
 *
 */
public class Controller{

	//  - instances -
	//an instance of the model, allows us to access the database
	private ModelInitializer data;
	//the country search view
	//the DataProjection view
	private DataProjection dProjection;
	//the mainView
	private MainView mainView;

	// - Variables -
	//Holds the current max number of countries that can be displayed on a graph
	private int maxNumberOfCountries;
	//Holds the current number of countries data currently bring displayed
	private int currentNumberOfCountries;
	//Holds the index of the next country to be replaced
	private int nextCountryToChangeIndex;
	//Holds the the countries being displayed
	private String[] country;
	//Holds the current indicator being displayed and queried for information
	private String indicator;
	//Holds the flaw value of the year currently being queries
	private int startYear;
	//Holds the ceiling value of the year currently being queries
	private int endYear;
	//Corresponds to the type of chart we are using to display the information
	private int ID;

	/**
	 * Constructor that loads all the views and database controller
	 */
	public Controller() { // connects the views and model via event handlers

		//Initializes the model on a new thread
		data = ModelInitializer.getInstance();

		//Initializes views
		dProjection = new DataProjection(data.getCountries(), data.getIndicators());


        mainView = new MainView();
		mainView.show();
		//Initializes variables
		ID = 0;
		maxNumberOfCountries = 1;
		currentNumberOfCountries = 1;
		nextCountryToChangeIndex = 0;
		country = new String[maxNumberOfCountries];
		country[nextCountryToChangeIndex] = "Afghanistan";
		indicator = "GDP";
		startYear = 1980;
		endYear = getDate();

		//adds the event handlers
		addMainViewEventHandlers();
		addDataProjectionEventsTopBar();
		addDataProjectionCountriesEvent();
        setDataBaseHook();

		dProjection.setBackButtonListener((event -> {
		    mainView.show();
		    dProjection.hide();
        }));

		dProjection.setExitButtonListener((event -> {
			dProjection.close();
		}));
	}

    private void setDataBaseHook() {
        EventHandler<WindowEvent> hook = data.getHook();
	    dProjection.setOnCloseRequest(hook);
	    mainView.setOnCloseRequest(hook);
    }

    private int getDate(){
		int date = Calendar.getInstance().get(Calendar.YEAR) - 1;
		return date;
	}
	
	//event handler for mainView
	private void addMainViewEventHandlers(){

		//Indicator button opens the DataProjectionView
		mainView.getIndicator().setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	dProjection.show();
				mainView.hide();
            }
        });
	}
	
	//adds events for the top bar in the DataProjection view
	private void addDataProjectionEventsTopBar() {

		//initialises the colour of the bar and pie hyperlinks to gray to symbolise they are not clickable
		dProjection.getBar().setStyle("-fx-text-fill: gray;");
		dProjection.getPie().setStyle("-fx-text-fill: gray;");
		
		//line button at the top changes the graph displays the a line graph
		dProjection.getLine().setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
				((Hyperlink) event.getTarget()).setVisited(false);
				addLineGraph();
            }
        });
		
		//pie button at the top changes the graph display to pie chart / unable to to when only one country is being viewed
		dProjection.getPie().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				((Hyperlink) event.getTarget()).setVisited(false);
                dProjection.setMessage("Please choose at least two countries to compare.");
			}
		});
		
		//bar button at the top changes the graph display to bar chart / unable to to when only one country is being viewed
		dProjection.getBar().setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				((Hyperlink) event.getTarget()).setVisited(false);
                dProjection.setMessage("Please choose at least two countries to compare.");
				//addBarChart();
            }
        });

		//scatter button at the top changes the graph display to scatter chart
		dProjection.getScatter().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				((Hyperlink) event.getTarget()).setVisited(false);
				addScatterChart();
			}
		});

		//clears the countries currently being displayed
		dProjection.getClearCountries().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				country = new String[maxNumberOfCountries];
				currentNumberOfCountries = 0;
				nextCountryToChangeIndex = 0;
				update();
			}
		});

		//changes the indicator
		dProjection.getChangeIndicator().valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				indicator = t1;
				update();
			}
		});

		//changes the number of countries that can be displayed
		dProjection.getNumCountries().valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				try {
					currentNumberOfCountries = 0;
					nextCountryToChangeIndex = 0;
					maxNumberOfCountries = Integer.parseInt(t1.substring(t1.length() - 1));
					country = new String[maxNumberOfCountries];

					//changes if the user can select a pie chart or bare chart based of number of countries comparing
					if(maxNumberOfCountries == 1){
						ID = 0;

						//changes the colour of the country to simulate that it can't be clicked
						dProjection.getBar().setStyle("-fx-text-fill: gray;");
						dProjection.getPie().setStyle("-fx-text-fill: gray;");

						//disables the action of the hyperlink, thus the button does nothing useful
						dProjection.getBar().setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								((Hyperlink) event.getTarget()).setVisited(false);
                                dProjection.setMessage("Please choose at least two countries to compare.");
							}
						});

						//disables the action of the hyperlink, thus the button dose nothing useful
						dProjection.getPie().setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								((Hyperlink) event.getTarget()).setVisited(false);
                                dProjection.setMessage("Please choose at least two countries to compare.");
							}
						});
					} else {
						//re adds the original colour thus simulating it is clickable
						dProjection.getBar().setStyle("-fx-text-fill: rgb(58,149,201);");
						dProjection.getPie().setStyle("-fx-text-fill: rgb(58,149,201);");

						//pie button at the top changes the graph display to pie chart
						dProjection.getPie().setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								((Hyperlink) event.getTarget()).setVisited(false);
								addPieChart();
							}
						});

						//bar button at the top changes the graph display to bar chart
						dProjection.getBar().setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								((Hyperlink) event.getTarget()).setVisited(false);
								addBarChart();
							}
						});
					}

					update();
				} catch (Exception e) {
					System.out.println("number conversion error");
				}
			}
		});


		//changes the query minimum date and appropriately alters the maximum date choice
		dProjection.getMinDate().valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue ov, String t, String t1) {
                //System.out.println("old: " + t + "\n new:" + t1);
                int newMinDate = 0;
				try {
                    String max = t1;
                    if(t1 == null) max = t;

                    newMinDate = Integer.parseInt(max.replaceAll(" ", ""));
					if (newMinDate > endYear) {
						System.out.println("This Date exceeds the current max year");
					} else {
						startYear = newMinDate;
						dProjection.setMaxDateMinValue(startYear);
						update();
					}
				} catch(Exception e){

                }
			}
		});


		//changes the query maximum date and appropriately alters the minimum date choice
		dProjection.getMaxDate().valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue ov, String t, String t1) {
                //System.out.println("old: " + t + "\n new:" + t1);
				try {
				    String max = t1;
				    if(t1 == null) max = t;
					int newMaxDate = Integer.parseInt(max.replaceAll(" ", ""));
                    endYear = newMaxDate;
                    dProjection.setMinDateMaxValue(endYear);
                    update();

				} catch(Exception e){

				}
			}
		});
}
	
	//sets the events for the left bar in dataProjection view
	private void addDataProjectionCountriesEvent() {
		
		//loops through all the countries that can be selected
		for(Hyperlink l : dProjection.getCountries()){
			
			//changes the country field in this object thus allowing the changing of a query
			l.setOnAction(new EventHandler<ActionEvent>() {

	            @Override
	            public void handle(ActionEvent event) {
					Hyperlink nameOfCountry = ((Hyperlink) event.getTarget());
					nameOfCountry.setVisited(false);
					country[nextCountryToChangeIndex] = nameOfCountry.getText();
					if(currentNumberOfCountries != maxNumberOfCountries) currentNumberOfCountries++;
					nextCountryToChangeIndex = (nextCountryToChangeIndex+1)%maxNumberOfCountries;
					update();
	            }
	        });
			
		}
	}



	//creates and adds a line graph to the center of the dProjection view
	private void addLineGraph(){

		// Update ID Value for the update function
		ID = 0;

		//generates x and y axis/there values
		NumberAxis xAxis = new NumberAxis("Years",startYear,endYear,5);
		NumberAxis yAxis = new NumberAxis();

		xAxis.setTickLabelFormatter(new StringConverter<Number>() {
			@Override
			public String toString(Number object) {
				return object.intValue() + "";
			}

			@Override
			public Number fromString(String string) {
				return 0;
			}
		});


		ArrayList<Map<Double, Integer>> data = getData(startYear, endYear);

		data.removeIf((doubleIntegerMap ->{return doubleIntegerMap == null;}));

		if(data.isEmpty()){
			dProjection.setMessage("No data for these options has been found. \n" +
                    "It either doesn't exist or network was unavailable.");
		}else{

			//creates the custom linechart
			LineCharts c = new LineCharts(xAxis,yAxis,data,country,indicator +" - " + startYear + " to " + endYear);

			//adds the custom pie chart to the center of the dProjection view
			c.setPadding(new Insets(20,20,20,20));
			dProjection.getBorder().setCenter(c);
			c.setId("Chart");
		}


	}

	//creates and adds a custom pie chart to the center of the dProjection view
	private void addPieChart(){

		// Update ID value for the update function
		ID = 1;

		ArrayList<Map<Double, Integer>> data = getData(startYear, endYear);

		data.removeIf((doubleIntegerMap ->{return doubleIntegerMap == null;}));

		if(data.isEmpty()){
			dProjection.setMessage("No data for these options has been found. \n" +
                    "It either doesn't exist or network was unavailable.");
		}else{

			//creates a custom pie chart
			PieCharts c = new PieCharts(data,country,indicator + " - " + endYear);

			//adds the custom pie chart to the center of the dProjection view
			c.setPadding(new Insets(20,20,20,20));
			dProjection.getBorder().setCenter(c);
			c.setId("Chart");
		}

	}

	//creates a custom scattar chart and adds it to the center of teh dProjection view
	private void addScatterChart(){

		// Update ID Value for the update function
		ID = 2;

		//generates x and y axis/there values
		NumberAxis xAxis = new NumberAxis("Years",startYear,endYear,5);
		NumberAxis yAxis = new NumberAxis();

		xAxis.setTickLabelFormatter(new StringConverter<Number>() {
			@Override
			public String toString(Number object) {
				return object.intValue() + "";
			}

			@Override
			public Number fromString(String string) {
				return 0;
			}
		});
		ArrayList<Map<Double, Integer>> data = getData(startYear, endYear);

		data.removeIf((doubleIntegerMap ->{return doubleIntegerMap == null;}));

		if(data.isEmpty()){
			dProjection.setMessage("No data for these options has been found. \n" +
                    "It either doesn't exist or network was unavailable.");
		}else{

			//creates a custom scatter chart
			ScatterCharts c = new ScatterCharts(xAxis,yAxis,data,country,indicator +" - " + startYear + " to " + endYear);

			//adds the custom pie chart to the center of the dProjection view
			c.setPadding(new Insets(20,20,20,20));
			dProjection.getBorder().setCenter(c);
			c.setId("Chart");
		}

	}

	//creates a custom bar chart and adds it to the center of the dProjection view
	private void addBarChart(){

		// Update ID value for the update function
		ID = 3;

		//generates x and y axis/there values
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();

		ArrayList<Map<Double, Integer>> data = getData(startYear, endYear);

		data.removeIf((doubleIntegerMap ->{return doubleIntegerMap == null;}));

		if(data.isEmpty()){
			dProjection.setMessage("No data for these options has been found. \n" +
                    "It either doesn't exist or network was unavailable.");
		}else{

			//creates a custom bar chart
			BarCharts c = new BarCharts(xAxis,yAxis,data,country,indicator + " - " + endYear);

			//adds the custom pie chart to the center of the dProjection view
			c.setPadding(new Insets(20,20,20,20));
			dProjection.getBorder().setCenter(c);
			c.setId("Chart");
		}

}

	//creates a list of integers that represent the dates that the user can select from
	private ArrayList<Map<Double,Integer>> getData(int startYear, int endYear){
		ArrayList<Map<Double,Integer>> toReturn = new ArrayList<Map<Double,Integer>>();

		for(int i = 0; i < currentNumberOfCountries; i++) {
			Map<Double, Integer> d = new HashMap<Double, Integer>();
			toReturn.add(data.getData(country[i], indicator, startYear, endYear));
		}
		return toReturn;
	}

	//reloads the last viewed graph with the updated indicators
	private void update(){
		switch (ID) {
			case 0:
				addLineGraph();
				break;
			case 1:
				addPieChart();
				break;
			case 2:
				addScatterChart();
				break;
			case 3:
				addBarChart();
				break;
		}
	}




}