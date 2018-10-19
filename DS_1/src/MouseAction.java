import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MouseAction implements EventHandler<MouseEvent>{
    protected Point location;
    protected Point target;
    protected GraphicsContext gc;
    static final int CELL_SIZE = 50;

    public MouseAction(Point location, GraphicsContext gc){
        this.location = location;
        this.gc = gc;
    }

    @Override
    public void handle(MouseEvent event){
        int x = (int) event.getSceneX() / CELL_SIZE * CELL_SIZE + CELL_SIZE/2;
        int y = (int) event.getSceneY() / CELL_SIZE * CELL_SIZE + CELL_SIZE/2;
        target = new Point(x, y);
        gc.strokeOval(target.getX() - 10, target.getY() - 10, 20, 20);
        gc.strokeLine(location.getX(), location.getY(), target.getX(), target.getY());

        if(event.isPrimaryButtonDown()){
            gc.setFill(Color.YELLOW);
            bresenhamAlgorithm();
        } else if(event.isSecondaryButtonDown()){
            gc.setFill(Color.PAPAYAWHIP);
            lineDDA();
        }
    }

    public void bresenhamAlgorithm(){
        final int[] x = {(int) location.getX() / CELL_SIZE};
        final int[] y = {(int) location.getY() / CELL_SIZE};
        int x1 = (int) target.getX() / CELL_SIZE;
        int y1 = (int) target.getY() / CELL_SIZE;
        int a = x1 - x[0];
        int b = y1 - y[0];
        int dx = Math.abs(a);
        int dy = Math.abs(b);
        int s1 = (int) Math.signum(a);
        int s2 = (int) Math.signum(b);
        if(dx == 0 && dy == 0) {
            gc.fillRect(x[0] * CELL_SIZE + 1, y[0] * CELL_SIZE + 1, 48, 48);
            return;
        }
        boolean flag;
        if(dy > dx){
            int temp = dx;
            dx = dy;
            dy = temp;
            flag = true;
        } else {
            flag = false;
        }
        final int[] t = {2 * dy - dx};

        int finalDx = dx;
        int finalDy = dy;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            gc.fillRect(x[0] * CELL_SIZE + 1, y[0] * CELL_SIZE + 1, 48, 48);
            while(t[0] >= 0){
                if(flag){
                    x[0] += s1;    // увеличение либо уменьшение на 1
                } else {
                    y[0] += s2;
                }    // увеличение либо уменьшение на 1
                t[0] -= 2 * finalDx;        // коррекция ошибки
            }
            if(flag)
                y[0] += s2;
            else
                x[0] += s1;
            t[0] = t[0] + 2 * finalDy;    // вычисление ошибки для следующего шага
        }));
        timeline.setCycleCount(dx + 1);
        timeline.play();
        timeline.setOnFinished(event -> {
            gc.strokeOval(target.getX() - 10, target.getY() - 10, 20, 20);
            gc.strokeLine(location.getX(), location.getY(), target.getX(), target.getY());
            location.setLocation(target.getX(), target.getY());
        });
    }

    public void lineDDA(){
        // Целочисленные значения координат начала и конца отрезка,
        // округленные до ближайшего целого
        int iX1 = Math.toIntExact(Math.round(location.getX())) / CELL_SIZE;
        int iY1 = Math.toIntExact(Math.round(location.getY())) / CELL_SIZE;
        int iX2 = Math.toIntExact(Math.round(target.getX())) / CELL_SIZE;
        int iY2 = Math.toIntExact(Math.round(target.getY())) / CELL_SIZE;

        // Длина и высота линии
        int deltaX = Math.abs(iX1 - iX2);
        int deltaY = Math.abs(iY1 - iY2);

        // Считаем минимальное количество итераций, необходимое
        // для отрисовки отрезка. Выбирая максимум из длины и высоты
        // линии, обеспечиваем связность линии
        int length = Math.max(deltaX, deltaY);

        // особый случай, на экране закрашивается ровно один пиксел
        if(length == 0){
            gc.fillRect(iX1 * CELL_SIZE + 1, iY1 * CELL_SIZE + 1, 48, 48);
            return;
        }

        // Вычисляем приращения на каждом шаге по осям абсцисс и ординат
        double dX = (target.getX() / CELL_SIZE - location.getX() / CELL_SIZE) / length;
        double dY = (target.getY() / CELL_SIZE - location.getY() / CELL_SIZE) / length;

        // Начальные значения
        final double[] x = {location.getX() / CELL_SIZE};
        final double[] y = {location.getY() / CELL_SIZE};

        // Основной цикл
        gc.fillRect(iX1 * CELL_SIZE + 1, iY1 * CELL_SIZE + 1, 48, 48);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            x[0] += dX;
            y[0] += dY;
            gc.fillRect((int) x[0] * CELL_SIZE + 1, (int) y[0] * CELL_SIZE + 1, 48, 48);
        }));
        timeline.setCycleCount(length);
        timeline.play();
        timeline.setOnFinished(event -> {
            gc.strokeOval(target.getX() - 10, target.getY() - 10, 20, 20);
            gc.strokeLine(location.getX(), location.getY(), target.getX(), target.getY());
            location.setLocation(target.getX(), target.getY());
        });
    }

}