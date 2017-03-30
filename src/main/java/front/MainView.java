package front;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

/**
 * 
 * @author MaxHein and team, This is the first/Title view where the user is
 * able to choose how to search for data
 *
 */
public class MainView extends Stage {
	
	private JFXButton indicator;
	
	/**
	 * Initializes the main view
	 * 
	 *  Stage takes the primary stage as the argument
	 */
	public MainView() {

		//Sets the title of the current view
		setTitle("Search");

        indicator = new JFXButton("Search Data");
		indicator.setStyle("-fx-background-color: #D3D3D3");
		indicator.setDisable(false);
        
        //grid is the main layout manager of the current view
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        //scenetitle is a statement describing the application and is displayed in the middle top of the grid
        Text scenetitle = new Text("A-level Economics Data");
        scenetitle.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        scenetitle.setStyle("-fx-text-fill: ghostwhite");
		final Effect glow = new DropShadow(5, Color.WHITE);
		scenetitle.setEffect(glow);
        
        //textFlow is like an HBox, hold's the search options
        TextFlow flow = new TextFlow(indicator);
        flow.setEffect(glow);
        flow.setPadding(new Insets(10, 0, 0, 10));
		flow.setTextAlignment(TextAlignment.CENTER);
		flow.setMinWidth(350);
		flow.setMaxWidth(350);
        //adds both scenetitle and textFlow to the view
        grid.add(scenetitle, 0, 0, 2, 1);
        grid.add(flow, 1, 1);
		StackPane stack = new StackPane();
		stack.getChildren().addAll(grid);
		stack.setId("MenuPane");
        //Initializes the views size and content
        Scene scene = new Scene(stack, 600, 400);
        scene.getStylesheets().add("style.css");
        setResizable(false);
        setScene(scene);
    }
	
	/**
	 * Getter for the indicator JFXButton
	 * 
	 * @return indicator JFXButton
	 */
	public JFXButton getIndicator(){
		return indicator;
	} //returns the country interactive button
}
