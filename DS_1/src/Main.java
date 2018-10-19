import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{
    protected Point location = new Point(25, 25);
    static final int LINES_NUMBER = 18;
    static final int CELL_SIZE = 50;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Line algorithms");
        VBox root = new VBox();
        Scene scene = new Scene(root, 900, 700);
        Canvas canvas = new Canvas(900, 700);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0 ,0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(3);
        for(int i = 0; i < LINES_NUMBER; i++){
            gc.strokeLine(i * CELL_SIZE, 0, i * CELL_SIZE, canvas.getHeight());
            gc.strokeLine(0, i * CELL_SIZE, canvas.getWidth(), i * CELL_SIZE);
        }

        root.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new MouseAction(location, gc));
    }
}
