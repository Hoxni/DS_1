public class Point{
    private double x, y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point getLocation(){
        return new Point(x, y);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
