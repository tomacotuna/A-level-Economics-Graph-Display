package front;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 
 * @author MaxHein and team, view that displays the graphical data
 *
 */
public class DataProjection extends Stage{
	
	//buttons to select the charts
	private Hyperlink line,pie,bar,scatter;
	//button to change indicator
	private ComboBox changeIndicator;
	//the pane that we are working with
	private BorderPane border;
	//a list of the countries the user can select (on the right of the application)
	private ArrayList<Hyperlink> countries;
    private VBox countrybox;
	private JFXButton clearCountries;
	private ComboBox numCountries;
	private ComboBox minDate;
	private ComboBox maxDate;
	private int maxYear;
	private Button homeButton;
	private double x,y;
	private Button exitButton;

	/**
	 * Initializes the dataProjection view.
	 * @param c, ArrayList of countries.
	 * @param i , String array of indicators.
	 */
	public DataProjection(ArrayList<String> c, String[] i) {

		maxYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
		//set's the title of the view
		this.setTitle("Information");
		border = new BorderPane();
		countries = new ArrayList<Hyperlink>();
		
		//adds the link to show the Line Graph
		line = createHyperlink("Line");
		
		//adds the link to show the Pie Graph
		pie = createHyperlink("Pie");
		
		//adds the link to show the bar Graph
		bar = createHyperlink("Box");

		//adds the link to show the bar Graph
		scatter = createHyperlink("Scatter");
		
		//the top bar
		BorderPane topBar = new BorderPane();
		//creates the change indicator button
		clearCountries = new JFXButton("Clear Countries");
		changeIndicator = genComboBox(i, "Indicator");
		String[] numOfCountriesToChooseFrom = {"Select 1", "Select 2", "Select 3", "Select 4", "Select 5"};
		numCountries = genComboBox(numOfCountriesToChooseFrom, "Number Of Countries");
        homeButton = new Button("");
        homeButton.setId("HomeButton");
        exitButton = new Button("");
        exitButton.setId("ExitButton");

		TextFlow flowLeft = new TextFlow(homeButton,clearCountries,changeIndicator,numCountries);
		flowLeft.setPadding(new Insets(5,0,0,0));

		//adds the chart selection buttons to a horizontal layout pane
		minDate = genComboBox(generateListOfIntegers(1980, maxYear),  "From");
		maxDate = genComboBox(generateListOfIntegers(1980,maxYear),  "Until");
		TextFlow flowRight = new TextFlow(line,scatter,pie,bar,minDate,maxDate,exitButton);
		flowRight.setPadding(new Insets(5,0,0,0));


		//adds all widgets to the top Panel and styles them
		topBar.setPadding(new Insets(10,50,10,10));
		topBar.setStyle("-fx-border-color: white white black white; -fx-background-color: white;");
		topBar.setLeft(flowLeft);
		topBar.setRight(flowRight);
		border.setTop(topBar);
		
		//left list of countries and styles
		VBox listOfCountries = new VBox();
		listOfCountries.setStyle("-fx-background-color: white;");
		
		//adds a heading to the left list of countries
		Label headingCountries = new Label("Change Countries");
		headingCountries.setFont(new Font("Arial", 14));
		headingCountries.setPadding(new Insets(5,10,5,10));
		listOfCountries.getChildren().add(headingCountries);
		listOfCountries.setMinWidth(250);
		//allows the scrolling of the left panel of countries
		ScrollPane sp = new ScrollPane();
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		sp.setVmax(440);
		sp.setMinWidth(250);
        sp.setPrefSize(250, 150);
        sp.setContent(listOfCountries);
        sp.setStyle("-fx-background-color: white;");

        countrybox = new VBox();
		countrybox.setStyle("-fx-background-color: white;");
		//Loads the list in to the left pane
		for(String str : c){
		    Hyperlink var = createHyperlink(str);
            countries.add(var);
			countrybox.getChildren().add(var);
		}
		listOfCountries.getChildren().add(countrybox);
		
		border.setLeft(sp);

		border.setId("Background");
		Scene scene  = new Scene(border,1065,600);
		setResizable(false);

		this.initStyle(StageStyle.TRANSPARENT);
		scene.setOnMousePressed((event -> {
			x = scene.getWindow().getX() - event.getScreenX();
			y = scene.getWindow().getY() - event.getScreenY();
		}));
		Stage handle = this;
		scene.setOnMouseDragged((event -> {
				handle.setX(event.getScreenX() + x);
				handle.setY(event.getScreenY() + y);
		}));

		scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("style.css");
		this.setScene(scene);
		
	}

	private Hyperlink createHyperlink(String name){
		Hyperlink var = new Hyperlink(name);
		var.setPadding(new Insets(0,15,0,15));
		var.setBorder(Border.EMPTY);
		var.setId("Hyperlink");
		return var;
	}

	private ComboBox genComboBox(String[] l, String name){
		ObservableList<String> choices =
				FXCollections.observableArrayList(l);
		ComboBox cb = new ComboBox(choices);
		cb.setStyle("-fx-underline: false; -fx-border-width: 0px; -fx-focus-color:TRANSPARENT;-fx-unfocus-color:TRANSPARENT;-fx-background-color:TRANSPARENT;");
		cb.setValue(name);

		return cb;
	}

	private String[] generateListOfIntegers(int start, int end){
		String[] toReturn = new String[(end-start)+1];

		for(int i = 0; i <= end-start; i++){
			toReturn[i] = ""+(start+i);
		}

		return toReturn;
	}
	
	/**
	 * Getter for the line hyperlink.
	 * @return Hyperlink.
	 */
	public Hyperlink getLine(){
		return line;
	}
	
	/**
	 * Getter for the Pie button
	 * @return Hyperlink.
	 */
	public Hyperlink getPie(){
		return pie;
	}
	
	/**
	 * Getter for the Bar hyperlink.
	 * @return Hyperlink.
	 */
	public Hyperlink getBar(){
		return bar;
	}

	/**
	 * Getter for the Scatter hyperlink
	 * @return Hyperlink
	 */
	public Hyperlink getScatter(){
		return scatter;
	}
	
	/**
	 * Gets a list of countries as hyperlinks.
	 * @return ArrayList of Hyperlinks.
	 */
	public ArrayList<Hyperlink> getCountries(){
		return countries;
	}

	/**
	 * Clears the current view.
	 * @return Button
	 */
	public Button getClearCountries() { return clearCountries; }
	
	/**
	 * Gets the changeIndicator button.
	 * @return ComboBox
	 */
	public ComboBox getChangeIndicator(){
		return changeIndicator;
	}

	/**
	 *Gets the clearCountries ComboBox.
	 * @return ComboBox
	 */
	public ComboBox getNumCountries() { return numCountries; }
	
	/**
	 * Gets the current panel we are working in.
	 * @return BorderPane
	 */
	public BorderPane getBorder(){
		return border;
	}

	/**
	 * Getter for the minDate
	 * @return ComboBox
	 */
	public ComboBox getMinDate() { return minDate; }

	/**
	 * Getter for the maxDate
	 * @return ComboBox
	 */
	public ComboBox getMaxDate() { return maxDate; }

	/**
	 * Sets the minDate
	 * @param maxYear
	 */
	public void setMinDateMaxValue(int maxYear){
		ObservableList<String> list1 = FXCollections.observableArrayList(generateListOfIntegers(1980, maxYear));
		minDate.setItems(list1);
	}

	/**
	 * Sets the maxDate
	 * @param minYear
	 */
	public void setMaxDateMinValue(int minYear){
		ObservableList<String> list2 = FXCollections.observableArrayList(generateListOfIntegers(minYear,maxYear));
		maxDate.setItems(list2);
	}


	/**
	 * Shows message on screen.
	 * @param message
	 */
	public void setMessage(String message){
		border.setCenter(new Label(message));
	}

	/**
	 * Button Listener for home button.
	 * @param listener
	 */
	public void setBackButtonListener(EventHandler<ActionEvent> listener){
	    homeButton.setOnAction(listener);
    }

	/**
	 * Creates hyperlinks for new countries.
	 * @param countryList
	 */
	public void setCountries(ArrayList<String> countryList) {
        countries.clear();
	    for(String country : countryList){
            countries.add(createHyperlink(country));
        }
    }

    /**
     * Button listener for exit button.
     * @param listener
     */
    public void setExitButtonListener(EventHandler<ActionEvent> listener){ exitButton.setOnAction(listener);}
}
