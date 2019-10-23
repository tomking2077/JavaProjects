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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.animation.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class RoboticsProject3 extends Application {
    
    Pane root = new Pane();
    Window mainWin = new Window();
	
    @Override
    public void start(Stage primaryStage) throws Exception{
        int displayWidth = 900;
        int displayHeight = 800;
        
        //title
        Label title = new Label("Braitenberg Vehicles");
        title.setStyle("-fx-font: 58 Cambria;");
        title.setLayoutY(20);
        title.setLayoutX(250);
        title.setTextFill(Color.BLACK);
        title.setTextAlignment(TextAlignment.CENTER);
        
        //Base display backgrounds
        Rectangle display = new Rectangle(0, 0, displayWidth, displayHeight);
        display.setFill(Color.WHITE);
        display.setStroke(Color.BLACK);

        
        Rectangle vehicleCreationBack = new Rectangle(875, 100, 210, 400);
        vehicleCreationBack.setFill(Color.GREY);
        vehicleCreationBack.setStroke(Color.BLACK);
        
        Rectangle lightCreationBack = new Rectangle(875, 550, 210, 200);
        lightCreationBack.setFill(Color.GREY);
        lightCreationBack.setStroke(Color.BLACK);
        
        //Submit vehicle to arraylist
        Button submitVehicle = new Button();
        submitVehicle.setText("Create Vehicle");
        submitVehicle.setLayoutY(450);
        submitVehicle.setLayoutX(940);
        submitVehicle.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        //Light source adding



        root.setStyle("-fx-background-color: #500000");
        
        root.getChildren().add(display);

        //placeholder parameters for car.
        float d = 50; //distance from midpoint of back wheels, to each of the wheels.
        float g = 200; //length of car from back wheels to front wheels.
        double aR = Math.toRadians(.5);
        double vF = 200;
		
		
		
        //Action to add vehicle to pane
        submitVehicle.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {


                //TODO--- DRAW AN ACTUAL CAR HERE INSTEAD
                    Rectangle rect = new Rectangle (displayWidth/2 - d, displayHeight/2 - g/2,d*2,g); //rectangle(topleft point X, topleft point Y, width, height);
                    rect.setFill(Color.BLUE);
                    Rectangle wFL = new Rectangle (rect.getX() - 10,rect.getY()-25,20,50);
                    wFL.setFill(Color.BLACK);
                    Rectangle wFR = new Rectangle (rect.getX() - 10 + d*2,rect.getY()-25,20,50);
                    wFR.setFill(Color.BLACK);
                    Rectangle wBL = new Rectangle (rect.getX() - 10 , rect.getY()-25 + g,20,50);
                    wBL.setFill(Color.BLACK);
                    Rectangle wBR = new Rectangle (rect.getX() - 10 + d*2, rect.getY()-25 + g,20,50);
                    wBL.setFill(Color.BLACK);
                    
                    
                    root.getChildren().add(rect);
                    root.getChildren().add(wFL);
                    root.getChildren().add(wFR);
                    root.getChildren().add(wBL);
                    root.getChildren().add(wBR);
                    

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
                        Rotate rotateL = new Rotate();
                        Rotate rotateR = new Rotate();
                        rotateR.setPivotX(rect.getX()+ d*2);
                        rotateR.setPivotY(rect.getY());
                        double aL = getAplhaL(g,d,aR);
                        double r = getR(g,d,aR);
                        double newAngle = aR - prevAngleR.get(0);
                        prevAngleR.set(0,aR);
                        rotate.setPivotX(getPoints(r,rect).getX());
                        rotate.setPivotY(getPoints(r,rect).getY());
                        rotate.setAngle(getAngular(r,aR,vF));

                        System.out.println(newAngle);
                        rotateR.setAngle(Math.toDegrees(newAngle));
                        rect.getTransforms().addAll(rotate);
                        wFL.getTransforms().addAll(rotate);
                        wFR.getTransforms().addAll(rotateR,rotate);
                        wBL.getTransforms().addAll(rotate);
                        wBR.getTransforms().addAll(rotate);
                        }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
        });
        
        root.getChildren().add(title);
        root.getChildren().add(submitVehicle);
        
        
        primaryStage.setTitle("Robotics Project 3");
        primaryStage.setScene(new Scene(root, 1100, 800));
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




}
