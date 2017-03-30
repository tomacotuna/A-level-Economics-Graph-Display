package loading;

import Controller.Controller;
import backend.ModelInitializer;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Created by tomacotuna on 03/12/2016.
 */
public class MainScreen  extends Application {

    private ModelInitializer data;
    private JFXProgressBar pbPrimary;
    private Label loadingText;

    /**
     * calls the start function of the application this starting the application
     * @param args
     */
    public static void main(String [] args) {
        Application.launch(args);
    }


    /**
     * starts the application
     *
     * @param primaryStage the stage that is seen on the screen
     * @throws Exception
     */
    public void start(Stage primaryStage) {
        data = ModelInitializer.getInstance();

        primaryStage.setTitle("Loading Screen");

        //Service and task are both abstract classes and implement the Worker interface
        final Service thread = new Service<Integer>() {
            @Override
            public Task createTask() {
                return new Task<Integer>() {
                    @Override
                    public Integer call() throws InterruptedException {
                        int i = 0;
                        data.restart();
                        while(!data.isLoaded()) {
                            i++;
                            updateProgress(i, 4000);
                        }
//                        for( i= 0; i< 2700; ++i) {
//                            updateProgress(i, 2700);
//                            Thread.sleep(20);
//                        }
                        Platform.runLater(()->{
                            primaryStage.close();
                            new Controller();
                        });
                        return i;
                    }
                };
            }
        };



        /**
         * The text should be changed and maybe add an Icon as well as the application animation
         */
        Label text = new Label("A-level economic Data");
        text.setStyle("-fx-font-family: 'Raleway'; -fx-font-size: 20px; -fx-text-fill: #F8F8FF ");


        //Create a progress bar and progress indicator
        pbPrimary = new JFXProgressBar();
        pbPrimary.setPrefSize(505, 10);
        pbPrimary.setVisible(false);

        /**
         * Label that lets the use know that the database is downloading
         */
        loadingText = new Label("The database is downloading...");
        loadingText.setVisible(false);
        loadingText.setStyle("-fx-text-fill: #A9A9A9");

        /**
         * Set value of progress bar and progress indicator through thread
         */
        pbPrimary.progressProperty().bind(thread.progressProperty());

        /**
         * Container for Progress Bar and Text
         */
        VBox loadingBox = new VBox();
        loadingBox.setAlignment(Pos.BOTTOM_CENTER);
        loadingBox.getChildren().addAll(loadingText,pbPrimary);



        /**
         * Container for the button box and the application title
         */
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(30);
        centerBox.getChildren().add(text);


        /**
         * Main Container for the Loading Screen (Border Pane)
         */
        BorderPane box = new BorderPane();
        //image was obtained from the url https://static1.squarespace.com/static/555a7c3ce4b06b408509e0f3/557f4266e4b0f030b34df389/557f4359e4b08d6ebc996613/1434403676627/particles.jpg?format=2500w
        // we take no credit for the production of the image
        box.setStyle("-fx-background-image: url(images/particles.jpg); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto");
        box.setCenter(centerBox);
        box.setBottom(loadingBox);




        //Create a new Horizontal box for add the progress bar and progress indicator
        box.setPadding(new Insets(1,20,10,-3));

        Scene scene = new Scene(box, 500, 250);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Raleway");


        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        loadingText.setVisible(true);
        pbPrimary.setVisible(true);
        thread.restart();

    }

}
