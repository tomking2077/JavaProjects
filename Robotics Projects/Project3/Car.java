import java.awt.geom.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Car {
    private int width;
    private int height;
    private Point2D centerPoint;
    private int angle;
    /*
        |
        |___  is 90 degrees
    */

    double IntensityS1;
    double IntensityS2;

    //TODO--Might need to make this an int for easy pixel movement (pixels/frame)
    private double wheel1Speed;
    private double wheel2Speed;

    private Point2D sensor1Point;
    private Point2D sensor2Point;

    private Point2D wheel1Point;
    private Point2D wheel2Point;

    private double[] KArray;
    /*
    k11 k12  -------- k11, k12, k21, k22
    k21 k22
    */
    double[] getKArray(){

        return KArray;
    }
    public void setKArray(double[] KArray) {

        this.KArray = KArray;
    }
	
	public Point2D getCenterPoint() {
		return centerPoint;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

    public Point2D getSensor1Point() {
        return sensor1Point;
    }

    public Point2D getSensor2Point() {
        return sensor2Point;
    }


    double[] getWheelSpeeds(){
        double[] speeds = {wheel1Speed, wheel2Speed};
        return speeds;
    }

    void setWheelSpeeds(double[] speeds){
        wheel1Speed = speeds[0];
        wheel2Speed = speeds[1];
    }

    double[] getIntensities(){
        double[] Intensities = {IntensityS1, IntensityS2};
        return Intensities;
    }

    void setIntensity(double[] Intensities){
        IntensityS1 = Intensities[0];
        IntensityS2 = Intensities[1];
    }

    Car(Point2D newCenterPoint, int newAngle, int w, int h){
        centerPoint = new Point2D.Double();
        sensor1Point = new Point2D.Double();
        sensor2Point = new Point2D.Double();
        wheel1Point = new Point2D.Double();
        wheel2Point = new Point2D.Double();

        angle = newAngle;
        width = w;
        height = h;
        wheel1Speed = 0;
        wheel2Speed = 0;

        centerPoint.setLocation(newCenterPoint);
        reCalculatePoints();

    }

    void moveCar(){
        System.out.println("wheel1Speed: " + wheel1Speed + "wheel2Speed: " + wheel2Speed);
        boolean greaterThan = isGreaterThan(wheel1Speed, wheel2Speed);
        move1Rotate2(greaterThan);
    }

    double [] moveCarPablo(Rectangle rect){
        boolean greaterThan = isGreaterThan(wheel1Speed, wheel2Speed);
        double moveSpeed;
        double rotateSpeed;
        Point2D pivotPoint = new Point2D.Double();
        //wheel1speed > wheel2speed
        boolean one_two;
        double cosAngle = Math.cos(Math.toRadians(angle));
        double sinAngle = Math.sin(Math.toRadians(angle));
        System.out.println("Angle: " + angle + " cosAngle: " + cosAngle + " sinAngle: " + sinAngle);

        if(greaterThan){
            moveSpeed = wheel2Speed;
            rotateSpeed = wheel1Speed;
            one_two = true;
        }   else{
            moveSpeed = wheel1Speed;
            rotateSpeed = wheel2Speed;
            one_two = false;
        }

        System.out.println("Initialx:" + rect.getX() + "Initialy: " + rect.getY());
        rect.getTransforms().add(new Translate(moveSpeed*cosAngle,moveSpeed*sinAngle));

        double newX = centerPoint.getX() + moveSpeed*cosAngle;
        double newY = centerPoint.getY() - moveSpeed*sinAngle;
		System.out.println(newX);
		System.out.println(newY);
        Point2D newPoint = new Point2D.Double(newX, newY);
        centerPoint.setLocation(newPoint);
        reCalculatePoints();
        if(one_two){	
            pivotPoint.setLocation(wheel2Point);
        } else{
            pivotPoint.setLocation(wheel1Point);
        }

        System.out.println("aTranslates:" + rect.localToParent(rect.getX(), rect.getY()));
        rotateSpeed -= moveSpeed;

        //rx = arclength
        double angleRadian = rotateSpeed/this.wheel1Point.distance(wheel2Point);
		System.out.println("RotateSpeed: " + rotateSpeed);
        //rect = rotate(rect, pivotPoint, angleRadian);
        //System.out.println("Distance: " + this.wheel1Point.distance(wheel2Point));

        //TODO----- THIS POINT ON

        System.out.println("Radians: " + angleRadian + " Degrees: " + Math.toDegrees(angleRadian));
        Rotate rotate = new Rotate();
        rotate.setAngle(Math.toRadians(angleRadian));
        rotate.setPivotX(pivotPoint.getX());
        rotate.setPivotY(pivotPoint.getY());
        rect.getTransforms().add(rotate);

        //TODO----- TO THIS DONE IN EVENT HANDLER

        this.angle+= Math.toDegrees(angleRadian);
        System.out.println("MoveSpeed: " + moveSpeed + " RotateSpeed: " + rotateSpeed);
        System.out.println("Pivot x: " + rect.getX() + "Pivot y: " + pivotPoint.getY());
        System.out.println("Rect x: " + rect.getX() + "Rect y: " + rect.getY());

        javafx.geometry.Point2D coord = rect.localToParent(rect.getX(),rect.getY());
        System.out.println("Final x: " + coord.getX() + "Final y: " + coord.getY());

        return coord;
    }

    //Untested
    void move1Rotate2(boolean greaterThan){
        double moveSpeed;
        double rotateSpeed;
        Point2D pivotPoint = new Point2D.Double();
        double cosAngle = Math.cos(Math.toRadians(angle));
        double sinAngle = Math.sin(Math.toRadians(angle));

        if(greaterThan){
            moveSpeed = wheel2Speed;
            rotateSpeed = wheel1Speed;
            pivotPoint.setLocation(wheel2Point);
        }   else{
            moveSpeed = wheel1Speed;
            rotateSpeed = wheel2Speed;
            pivotPoint.setLocation(wheel1Point);
        }

        double newX = centerPoint.getX() + moveSpeed*cosAngle;
        double newY = centerPoint.getY() - moveSpeed*sinAngle;
        Point2D newPoint = new Point2D.Double(newX, newY);

        centerPoint.setLocation(newPoint);
        reCalculatePoints();

        rotateSpeed -= moveSpeed;
        //rotate(rect, pivotPoint, rotateSpeed);
    }

    //TODO---- ADD ANGLE SUPPORT
    void reCalculatePoints(){
        //Based on angle but ignored for now
        double sensor1X = centerPoint.getX() + height/2;
        double sensor1Y = centerPoint.getY() - width/2;

        double sensor2X = centerPoint.getX() + height/2;
        double sensor2Y = centerPoint.getY() + width/2;

        sensor1Point.setLocation(sensor1X, sensor1Y);
        sensor2Point.setLocation(sensor2X, sensor2Y);

        double wheel1X = sensor1X - width;
        double wheel1Y = sensor1Y;

        double wheel2X = sensor2X - width;
        double wheel2Y = sensor2Y;

        wheel1Point.setLocation(wheel1X, wheel1Y);
        wheel2Point.setLocation(wheel2X, wheel2Y);

        //TODO--- ADD ROTATE HERE (PIVOTPOINT = CENTERPOINT)
    }

    Boolean isGreaterThan( double A, double B){
        if(A > B){
            return true;
        }
        else return false;
    }

    //TODO--- FINISH
    //2pir*d(degrees)/360 = arc length
    //r*d(radians) = arc length
    Rectangle rotate(Rectangle rect,  Point2D pivotPoint, double angleRadians){
        Rotate rotate = new Rotate();
        rotate.setAngle(Math.toRadians(angleRadians));
        rotate.setPivotX(pivotPoint.getX());
        rotate.setPivotY(pivotPoint.getY());
        rect.getTransforms().add(rotate);
        return rect;
    }
}