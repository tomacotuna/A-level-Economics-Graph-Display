package front;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Map;

/**
 * Creates an custom JavaFX Line Chart, nodes have tooltips
 */
@SuppressWarnings("rawtypes")
public class LineCharts extends LineChart {

	/**
	 *  constructs a custom Line Chart
	 *
	 * @param xAxis , an instance of a xAxis
	 * @param yAxis , an instance of a yAxis
	 * @param graphData , the data the graph needs to display given as an array of maps
	 * @param countryNames , a list of the country names that data is being displayed
	 * @param name , the title of the graph
	 */
    @SuppressWarnings({ "unchecked" })
	public LineCharts(Axis xAxis, Axis yAxis, ArrayList<Map<Double,Integer>> graphData, String[] countryNames, final String name) {
		super(xAxis, yAxis);

		//sets the charts title
		this.setTitle(name);
		//titles the x axis
		xAxis.setLabel("Years");
		yAxis.setLabel(getYAxisTitle(name));

		//holds the list of series (singular line data) to pass to the graph
		ObservableList<XYChart.Series<Integer, Double>> toAdd = FXCollections.observableArrayList();

		//adds data to the list that holds the data to add to the graph (Above)
		//loops through the maps given in the parameters
		for(int i = 0; i < graphData.size(); i++) {
			//creates a series (One line)
			XYChart.Series<Integer, Double> series = new XYChart.Series<Integer, Double>();
			series.setName(countryNames[i]);
			//loops through the map data and passes it to the series
			for (Map.Entry<Double, Integer> entry : graphData.get(i).entrySet()) {
				Double key = entry.getKey();
				Integer value = entry.getValue();

				final XYChart.Data<Integer, Double> data = new XYChart.Data<Integer, Double>(value, key);

				//adds a coordinate data to the series
				series.getData().add(data);
			}
			//adds the series (one line) to the list of series
			toAdd.add(series);
		}

		//adds all the series data to the graph
		this.getData().addAll(toAdd);

		//loops through the series to add a tooltip (pop up information)
		for(XYChart.Series<Integer, Double> s : toAdd){
			//loops through the nodes in the series
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
				final Tooltip tooltip = new Tooltip();
				tooltip.setText("Year :" + theData.getXValue().toString() + "\n" +
						name.split(" ")[0] + " :" + value);

				//adds event handler to the node thus opening the tooltip
				theData.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						//System.out.println("Entered the node");
						tooltip.show(theData.getNode(), event.getSceneX(), event.getSceneY()+25);
					}
				});

				//adds event handler to the node thus closing the tooltip
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
