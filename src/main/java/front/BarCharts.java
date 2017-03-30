package front;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Map;

/**
 * Creates an custom JavaFX Bar Chart, segments have tooltips
 */
@SuppressWarnings("rawtypes")
public class BarCharts extends BarChart{

	/**
	 *  constructs a custom Line Chart
	 *
	 * @param xAxis , an instance of a xAxis
	 * @param yAxis , an instance of a yAxis
	 * @param graphData , the data the graph needs to display given as an array of maps
	 * @param countryNames , a list of the country names that data is being displayed
	 * @param heading , the title of the graph
	 */
	@SuppressWarnings("unchecked")
	public BarCharts(Axis xAxis, Axis yAxis, ArrayList<Map<Double,Integer>> graphData, String[] countryNames, final String heading) {
		super(xAxis, yAxis);

		//sets the title of the graph
		setTitle(heading);
		//sets the title of the x axis
		xAxis.setLabel("Country");
		yAxis.setLabel(getYAxisTitle(heading));

		//a list of series(list of data) to be added to the graph
		ObservableList<Series<String, Double>> toAdd = FXCollections.observableArrayList();

		//adds series to the list above
		//loos through the maps in graphData
		for(int i = 0; i < graphData.size(); i++) {
			//creates a new series
			XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
			series.setName(countryNames[i]);

			double indVal = (Double)graphData.get(i).keySet().toArray()[0];
			//creates new data
			final XYChart.Data<String, Double> data = new XYChart.Data<String, Double>(countryNames[i], indVal);

			//adds the data to the series
			series.getData().add(data);

			//adds the series to the list
			toAdd.add(series);
		}

		//adds the list of series to the graph to be displayed
		this.getData().addAll(toAdd);

		//adds the tooltip to the graph data
		//loops through the series
		for(final Series<String,Double> s : toAdd){
			//loops through the series data
			for(final XYChart.Data<String,Double> theData : s.getData()) {

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
				tooltip.setText(heading.split(" ")[0] + " :" + value);

				//adds event to display the tooltip on hover
				theData.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						//System.out.println("Entered the node");
						tooltip.show(theData.getNode(), event.getSceneX(), event.getSceneY() + 25);
					}
				});

				//adds an event to remove the tooltip when not hovering over the node
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
