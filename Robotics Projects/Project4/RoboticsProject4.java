//package RoboticsProject4;

import javafx.application.Application;

//import java.awt.Window;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.text.Text;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class RoboticsProject4 extends Application {
    
    Pane root = new Pane();
    Window mainWin = new Window();
	
	ArrayList<Circle> visitedCoordinates = new ArrayList<Circle>();
	ArrayList<Line> traceLines = new ArrayList<Line>();

        //placeholder parameters for car.
        float d = 16; //distance from midpoint of back wheels, to each of the wheels.
        float g = 50; //length of car from back wheels to front wheels.
        
        float wheelW = d/2;
        float wheelH = g/4;
        double aR = Math.toRadians(0.01);
        double vF = 0;
        
        double turn = 0;
        double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
        int displayWidth = 900;
        int displayHeight = 800;
        int windowWidth = 1200;
        int windowHeight = 800;
        
        //title
        Label title = new Label("Braitenberg Vehicles");
        title.setStyle("-fx-font: 58 Cambria;");
        title.setLayoutY(20);
        title.setLayoutX(250);
        title.setTextFill(Color.BLACK);
        Background titleBackground = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
        title.setBackground(titleBackground);
        title.setTextAlignment(TextAlignment.CENTER);
        
        //Base display backgrounds
        Rectangle display = new Rectangle(displayWidth, 0, windowWidth-displayWidth, displayHeight);
        display.setFill(Color.MAROON);
        display.setStroke(Color.BLACK);
        
        Rectangle keyLabelBack = new Rectangle(940, 325, 240, 150);
        keyLabelBack.setFill(Color.WHITE);
        keyLabelBack.setStroke(Color.BLACK);
        keyLabelBack.setStyle("-fx-fill: #A9A9A9;");

        Rectangle dashBack = new Rectangle(910, 500, 285, 290);
        dashBack.setFill(Color.WHITE);
        dashBack.setStroke(Color.BLACK);
        dashBack.setStyle("-fx-fill: #A9A9A9;");
        
        Rectangle speedBack = new Rectangle(930, 520, 220, 50);
        speedBack.setFill(Color.WHITE);
        speedBack.setStroke(Color.BLACK);
        speedBack.setStyle("-fx-fill: #000000;");
        
        //Submit vehicle to arraylist
        Button submitVehicle = new Button();
        submitVehicle.setText("Create Vehicle");
        submitVehicle.setLayoutY(150);
        submitVehicle.setLayoutX(960);
        submitVehicle.setStyle("-fx-font: 24 Cambria; -fx-base: #000000;");

        /*
        Button right = new Button();
        right.setText("right");
        right.setLayoutY(550);
        right.setLayoutX(1000);
        right.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        right.setOnAction(ae -> {
            aR = Math.toRadians(30);
        });
        Button left = new Button();
        left.setText("left");
        left.setLayoutY(550);
        left.setLayoutX(900);
        left.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        left.setOnAction(ae -> {
            aR = Math.toRadians(-30);
        });
        */
        
        //steering wheel image
        Image wheel = new Image("wheel.png");
        ImageView ivWheel = new ImageView(wheel);
        ivWheel.setTranslateY(580);
        ivWheel.setTranslateX(920);
        ivWheel.setFitWidth(200);
        ivWheel.setFitHeight(200);
        
        //Key labels image
        Image keysLabel = new Image("keyslabel.png");
        ImageView ivKeys = new ImageView(keysLabel);
        ivKeys.setTranslateY(300);
        ivKeys.setTranslateX(960);
        ivKeys.setFitWidth(200);
        ivKeys.setFitHeight(200);
        
        //Speedometer
        Text showSpeed = new Text();
        showSpeed.setText("Speed: " + Double.toString(vF));
        showSpeed.setLayoutY(550);
        showSpeed.setLayoutX(940);
        showSpeed.setStyle("-fx-font: 36 Cambria; -fx-fill: #7CFC00;");
        
        //left and right steering with arrow keys
        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case LEFT:  
                        if (turn > -30){
                            turn = turn - 1;
                            aR = Math.toRadians(turn); 
                            ivWheel.getTransforms().add(new Rotate(-4,100,100));
                        }
                        break;
                    case RIGHT:  
                        if (turn < 30){
                            turn = turn + 1;
                            aR = Math.toRadians(turn);
                            ivWheel.getTransforms().add(new Rotate(4,100,100));
                        }
                        break;
                    case UP:
                        if (vF < 200){
                            vF += 5;
                            showSpeed.setText("Speed: " + Double.toString(vF));
                        }
                        break;
                    case DOWN:
                        if (vF > -5){
                            vF -= 5;
                            showSpeed.setText("Speed: " + Double.toString(vF));
                        }
                        break;
                }
            }
        });
        
        
        //steering with mouse input
        root.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);
            if (me.isPrimaryButtonDown() && mousePosX < mouseOldX) {
                if (turn > -30){
                    turn = turn - .1;
                    aR = Math.toRadians(turn);
                    ivWheel.getTransforms().add(new Rotate(-.4,100,100));
                }
            } else if (me.isPrimaryButtonDown() && mousePosX > mouseOldX) {
                if (turn < 30){
                    turn = turn + .1;
                    aR = Math.toRadians(turn);
                    ivWheel.getTransforms().add(new Rotate(.4,100,100));
                }
            }
        });
        
        
        Button speedUp = new Button();
        speedUp.setText("+");
        speedUp.setLayoutY(600);
        speedUp.setLayoutX(1130);
        speedUp.setStyle("-fx-font: 31 Cambria; -fx-base: #000000;");
        speedUp.setOnAction(ae -> {
            if (vF < 200){
                vF += 5;
                showSpeed.setText("Speed: " + Double.toString(vF));
            }
        });
        Button slowDown = new Button();
        slowDown.setText("-");
        slowDown.setLayoutY(700);
        slowDown.setLayoutX(1130);
        slowDown.setStyle("-fx-font: 36 Cambria; -fx-base: #000000;");
        slowDown.setOnAction(ae -> {
            if (vF > -5){
                vF -= 5;
                showSpeed.setText("Speed: " + Double.toString(vF));
            }
        });

        Image image = new Image("track.PNG");
        ImageView map = new ImageView(image);
        double mapScale = 0.25;
        map.setFitHeight(image.getHeight()*mapScale);
        map.setFitWidth(image.getWidth()*mapScale);
        map.setTranslateX(20);
        map.setTranslateY(100);

        ImageView localImage = new ImageView("track.PNG");
        double localScale = 8;
        localImage.setFitHeight(image.getHeight()*localScale);
        localImage.setFitWidth(image.getWidth()*localScale);
        localImage.setTranslateX(0);
        localImage.setTranslateY(0);

        root.setStyle("-fx-background-color: #500000");


        //Action to add vehicle to pane
        submitVehicle.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {


                //TODO--- DRAW AN ACTUAL CAR HERE INSTEAD
                    Rectangle rect = new Rectangle (displayWidth/2 - d, displayHeight/2 - g/2,d*2,g); //rectangle(topleft point X, topleft point Y, width, height);
                    rect.setFill(Color.BLUE);
                    Rectangle wFL = new Rectangle (rect.getX() - wheelW/2,rect.getY()-wheelH/2,wheelW,wheelH);
                    wFL.setFill(Color.BLACK);
                    Rectangle wFR = new Rectangle (rect.getX() - wheelW/2 + d*2,rect.getY()-wheelH/2,wheelW,wheelH);
                    wFR.setFill(Color.BLACK);
                    Rectangle wBL = new Rectangle (rect.getX() - wheelW/2 , rect.getY()-wheelH/2 + g,wheelW,wheelH);
                    wBL.setFill(Color.BLACK);
                    Rectangle wBR = new Rectangle (rect.getX() - wheelW/2 + d*2, rect.getY()-wheelH/2 + g,wheelW,wheelH);
                    wBR.setFill(Color.BLACK);
					// Find initial rear center location and store/display
					Point2D initialButtCoord = getCarButt(rect);
					Circle initialButt = new Circle(initialButtCoord.getX(), initialButtCoord.getY(), 1);
					initialButt.setFill(Color.RED);
					visitedCoordinates.add(initialButt);
                    
                    root.getChildren().add(rect);
                    root.getChildren().add(wFL);
                    root.getChildren().add(wFR);
                    root.getChildren().add(wBL);
                    root.getChildren().add(wBR);
                    root.getChildren().add(initialButt);
                    

                    ArrayList<Double> prevAngleR = new ArrayList<Double>();
                    ArrayList<Double> prevAngleL = new ArrayList<Double>();
                    prevAngleR.add(0.0);
                    prevAngleL.add(0.0);
                    
                    //This is where the animation will be for each vehicle, though we may need to do this outside the action event to access the light positions. This is a way to individually control the animation of each vehicle.
                     Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(50),
                        ae -> {
                        //rect.getTransforms().add(new Rotate(1,rect.getX()+(dimensions.getX()/2),rect.getY()+(dimensions.getY()/2))); //rotates at 1 degree per 50 milliseconds. rectangle dimensions subtracted to rotate about the origin.
                        //add animation stuff here!!
                        Rotate rotate = new Rotate();
                        //Rotate rotateL = new Rotate();
                        //Rotate rotateR = new Rotate();
                        //rotateR.setPivotX(rect.getX()+ d*2);
                        //rotateR.setPivotY(rect.getY());
                        double aL = getAplhaL(g,d,aR);
                        double r = getR(g,d,aR);
                        double newAngle = aR - prevAngleR.get(0);
                        prevAngleR.set(0,aR);
                        rotate.setPivotX(getPoints(r,rect).getX());
                        rotate.setPivotY(getPoints(r,rect).getY());
                        rotate.setAngle(getAngular(r,aR,vF));

						// Add new coordinate (each new one will be placed back at the origin)
                        /*
						visitedCoordinates.add(new Circle(initialButt.getCenterX(), initialButt.getCenterY(), 1));
						traceLines.add(new Line(initialButt.getCenterX(), initialButt.getCenterY(), initialButt.getCenterX(), initialButt.getCenterY()+10));
						root.getChildren().add(visitedCoordinates.get(visitedCoordinates.size()-1));
						root.getChildren().add(traceLines.get(traceLines.size()-1));
                        */
						
                        //System.out.println(newAngle);
                        //rotateR.setAngle(Math.toDegrees(newAngle));
                        rect.getTransforms().addAll(rotate);
                        wFL.getTransforms().addAll(rotate);
                        wFR.getTransforms().addAll(rotate);
                        wBL.getTransforms().addAll(rotate);
                        wBR.getTransforms().addAll(rotate);
						// Add every subsequent rotation to all visited coordinates
						for (int i = 0; i < visitedCoordinates.size(); i++) {
							visitedCoordinates.get(i).getTransforms().addAll(rotate);
						}
						for (int i = 0; i < traceLines.size(); i++) {
							traceLines.get(i).getTransforms().addAll(rotate);
						}
                        }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
        });
        root.getChildren().add(display);
        root.getChildren().add(keyLabelBack);
        root.getChildren().add(dashBack);
        root.getChildren().add(speedBack);
        root.getChildren().add(title);
        root.getChildren().add(submitVehicle);
        //root.getChildren().add(right);
        //root.getChildren().add(left);
        root.getChildren().add(speedUp);
        root.getChildren().add(slowDown);
        root.getChildren().add(showSpeed);
        root.getChildren().add(ivWheel);
        root.getChildren().add(ivKeys);

        root.getChildren().add(map);
        root.getChildren().add(localImage);
        localImage.toBack();
        Translate initialTranslate = new Translate();
        initialTranslate.setX(-6300);
        initialTranslate.setY(100);

        Rotate initialRotate = new Rotate(-90, 0, 0);
        //System.out.println("Initial Rotate, X: " + localImage.getFitWidth()/2 + " Y: " + localImage.getFitHeight()/2 );
        localImage.getTransforms().add(initialRotate);
        localImage.getTransforms().add(initialTranslate);

        Point2D testPoint = localImage.localToParent(localImage.getX(), localImage.getY());
        System.out.println("X: " + testPoint.getX() + " Y: " + testPoint.getY());
        //localImage.getTransforms().add(Transform.rotate(-30,windowWidth/2, windowHeight) );
        
        primaryStage.setTitle("Robotics Project 3");
        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));
        primaryStage.show();
    }

    double getAplhaL(float g, float d, double aR){
        return Math.atan(1.0*((-2*d)/g + 1/(Math.tan(aR))));

    }
    double getR(float g, float d, double aR){
        return (g/Math.tan(aR))-d;
    }
    Point2D getPoints(double r, Rectangle rect){
        double pointX = rect.getX() + rect.getWidth()/2 + r;
        double pointY = rect.getY() + rect.getHeight();
        Point2D pivot = new Point2D(pointX,pointY);
        return pivot;
    }
    double getAngular(double r, double aR, double vF){
        return (vF * Math.cos(aR))/r;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
	
	// Average x and y coordinates of rear wheels to find back of car
	Point2D getCarButt(Rectangle rect) {
		double height = rect.getHeight();
		double width = rect.getWidth();
		double x = rect.getX() + (width / 2.0);
		double y = rect.getY() + height;		
		return new Point2D(x,y);
	}

	void addImage(GraphicsContext gc, Image image){
    }
}
