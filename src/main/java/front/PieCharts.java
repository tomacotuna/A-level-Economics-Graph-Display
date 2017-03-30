package front;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Map;

/**
 * Creates an custom JavaFX Pie Chart, segments have tooltips
 */
public class PieCharts extends PieChart {

    /**
     *
     * @param graphData , holds the data to be passed to the graph
     * @param countryNames , holds the names of countries that the graph displays data
     * @param name , the graph header
     */
    @SuppressWarnings({ })
	public PieCharts(ArrayList<Map<Double,Integer>> graphData, String[] countryNames, final String name) {
    	super();
        //sets the title of the graph
        this.setTitle(name);

        //creates a list of data that the piechart displays
        ObservableList<Data> pieChartData = FXCollections.observableArrayList();

        //holds a list of percentages
        ArrayList<Double> results = new ArrayList<Double>();
        //calculates the percentage a country hold in the pie chart
        double sum = 0;
        for(int i = 0; i < graphData.size(); i++) {
            double indVal = (Double)(graphData.get(i).keySet().toArray()[0]);
            results.add(indVal);
            sum += indVal;
        }

        //adds the percentage and names to the pie chart list of data
        for(int x = 0; x < results.size(); x++){
            double val = ((results.get(x)/sum)*100);
            pieChartData.add(new Data(countryNames[x],val));
        }

        //adds the list of data to the pie chart to be displayed
        this.getData().addAll(pieChartData);

        //loops through all the pie chart information and adds a tooltip
        //gets each data segment in the piechart
        for(final Data theData : pieChartData){

            //creates a new tooltip
            final Tooltip tooltip = new Tooltip();
            tooltip.setText(name.split(" ")[0] + " :" + theData.getPieValue() + "%");

            //adds an event handler to the data segment to show the tool tip on mouse hover
            theData.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    //System.out.println("Entered the node");
                    tooltip.show(theData.getNode(), event.getSceneX(), event.getSceneY()+25);
                }
            });

            //adds an event handler to the data segment to close the tool tip on mouse hover exit
            theData.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    //System.out.println("Entered the node");
                    tooltip.hide();
                }
            });
        }
    }
}


