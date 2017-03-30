package front;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.Axis;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Map;

/**
 *  Creates an custom JavaFX Scatter Chart, Nodes have tooltips
 */
@SuppressWarnings("rawtypes")
public class ScatterCharts extends ScatterChart {

    /**
     *
     * @param xAxis , an instance of a xAxis
     * @param yAxis , an instance of a yAxis
     * @param graphData , the data the graph needs to display given as an array of maps
     * @param countryNames , a list of the country names that data is being displayed
     * @param name , the title of the graph
     */
    @SuppressWarnings({ "unchecked" })
    public ScatterCharts(Axis xAxis, Axis yAxis, ArrayList<Map<Double,Integer>> graphData, String[] countryNames, final String name) {
        super(xAxis, yAxis);

        //sets the title of the graph
        this.setTitle(name);
        //sets the title of the x axis
        xAxis.setLabel("Years");
        yAxis.setLabel(getYAxisTitle(name));

        //a list of series (list of Nodes that create a line)
        ObservableList<Series<Integer, Double>> toAdd = FXCollections.observableArrayList();

        //adds series to the list above
        //loops through the graphData
        for(int i = 0; i < graphData.size(); i++) {
            //creates a new series
            XYChart.Series series = new XYChart.Series();
            series.setName(countryNames[i]);
            //loops through the map data
            for (Map.Entry<Double, Integer> entry : graphData.get(i).entrySet()) {
                Double key = entry.getKey();
                Integer value = entry.getValue();

                //adds data to the series
                series.getData().add(new XYChart.Data(value, key));
            }
            //adds the series to the list of data to be passed to the chart
            toAdd.add(series);
        }

        //adds the list of series to the scatter chart to be displayed
        this.getData().addAll(toAdd);

        //adds tooltips to the nodes of each series
        // loops through the series the graph is displaying
        for(XYChart.Series<Integer, Double> s : toAdd){
            //gets each node in the series
            for(final XYChart.Data<Integer, Double> theData : s.getData()){

                String value = "";
                if(theData.getYValue().longValue() > 100) {
                    //creates a new tooltip
                    char[] addCommas = (theData.getYValue().longValue() + "").toCharArray();
                    value = value + addCommas[0];
                    for (int i = 1; i < addCommas.length; i++) {
                        if ((addCommas.length - i) % 3 == 0)
                            value = value + ",";
                        value = value + addCommas[i];
                    }
                } else { try { value = (theData.getYValue().doubleValue() + "").substring(0,5); } catch (Exception e) { value = theData.getYValue().doubleValue() + "";}}

                //creates a new tooltip
                final Tooltip tooltip = new Tooltip();
                tooltip.setText("Year :" + theData.getXValue().toString() + "\n" +
                        name.split(" ")[0] + " :" + value);

                //displays the tooltip when the mouse hovers over the node
                theData.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        //System.out.println("Entered the node");
                        tooltip.show(theData.getNode(), event.getSceneX(), event.getSceneY()+25);
                    }
                });

                //removes the tooltip when the mouse is not hovering over the node
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

    //gets the y axsis title
    private String getYAxisTitle(String name){
        String name1 = name.split(" ")[0];
        String name2 = "";
        try { name2 = name.split(" ")[1]; } catch (Exception e){ System.out.println(""); }
        if(name1.equals("Gross") && name2.equals("Domestic")) return "GDP In US $";
        if(name1.equals("Gross") && name2.equals("National")) return "GNI Per Capita";
        if(name1.equals("Inflation")) return "Inflation CPI + RPI % ";
        if(name1.equals("Balance")) return "Current account balance";
        if(name1.equals("Foreign")) return "Foreign Direct Investment";
        if(name1.equals("Exports")) return "Exports As % Of GDP";
        if(name1.equals("Imports")) return "Imports As % Of GDP";
        if(name1.equals("Unemployment")) return "Unemployment As % Of Labour Force";
        return "";
    }

}
