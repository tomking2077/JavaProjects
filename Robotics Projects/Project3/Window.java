import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Window {
    int maxIntensity;
    int windowHeight;
    int windowWidth;

    private ArrayList<Point2D>  lightPoints;


    public ArrayList<Point2D> getLightPoints() {
        return lightPoints;
    }


    public void setLightPoints(ArrayList<Point2D> lightPoints) {
        this.lightPoints = lightPoints;
    }

    Window(){
        maxIntensity = 100;
        windowHeight = 600;
        windowWidth = 800;
    }
}
