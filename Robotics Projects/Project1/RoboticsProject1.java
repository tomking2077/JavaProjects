/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package roboticsproject1;

import java.io.Console;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Point2D;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 *
 * @author Zak
 */
public class RoboticsProject1 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Rectangle display = new Rectangle(10, 10, 580, 480);
        display.setFill(Color.WHITE);
        display.setStroke(Color.BLACK);
        
        Rectangle control = new Rectangle(600, 30, 180, 250);
        control.setFill(Color.GREY);
        control.setStroke(Color.BLACK);
        
        Label controlP = new Label("Control Panel");
        controlP.setStyle("-fx-font: 26 Cambria;");
        controlP.setLayoutY(40);
        controlP.setLayoutX(610);
        controlP.setTextFill(Color.web("#000000"));
        
        Label license = new Label("© Zacharaih Stratton\n"
                + "Pablo Vielma Jr.\n"
                + "Shaun Fattig\n"
                + "Russell Pier\n");
        license.setStyle("-fx-font: 14 Cambria;");
        license.setLayoutY(430);
        license.setLayoutX(668);
        license.setTextFill(Color.web("#FFFFFF"));
        license.setTextAlignment(TextAlignment.RIGHT);
        
        Button btn1 = new Button();
        btn1.setText("Joint 1 left");
        btn1.setLayoutY(80);
        btn1.setLayoutX(610);
        btn1.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn2 = new Button();
        btn2.setText("Joint 1 right");
        btn2.setLayoutY(80);
        btn2.setLayoutX(690);
        btn2.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn3 = new Button();
        btn3.setText("Joint 2 left");
        btn3.setLayoutY(120);
        btn3.setLayoutX(610);
        btn3.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn4 = new Button();
        btn4.setText("Joint 2 right");
        btn4.setLayoutY(120);
        btn4.setLayoutX(690);
        btn4.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn5 = new Button();
        btn5.setText("Joint 3 left");
        btn5.setLayoutY(160);
        btn5.setLayoutX(610);
        btn5.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn6 = new Button();
        btn6.setText("Joint 3 right");
        btn6.setLayoutY(160);
        btn6.setLayoutX(690);
        btn6.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button paint = new Button();
        paint.setText("Paint");
        paint.setLayoutY(200);
        paint.setLayoutX(655);
        paint.setStyle("-fx-font: 16 Cambria; -fx-base: #000000;");
        
        Arc arc = new Arc();
        arc.setLayoutX(290.0);
        arc.setLayoutY(490.0);
        arc.setRadiusX(50.0);
        arc.setRadiusY(50.0);
        arc.setStartAngle(0.0);
        arc.setLength(180.0);
        arc.setType(ArcType.ROUND);
        arc.setFill(Color.GREY);
        
        //variables to hold coordinates of Axis nodes
        int axis1X = 290;
        int axis1Y = 460;
        int axis2X = 290;
        int axis2Y = 310;
        int axis3X = 400;
        int axis3Y = 250;
        int brushX = 450;
        int brushY = 200;
        
        //arms, dependent on coords
        Line arm1 = new Line(axis1X, axis1Y, axis2X, axis2Y);
        arm1.setStyle("-fx-stroke: grey; -fx-stroke-width: 20;");
        arm1.setStrokeLineCap(StrokeLineCap.ROUND);
        
        Line arm2 = new Line(arm1.getEndX(), arm1.getEndY(), axis3X, axis3Y);
        arm2.setStyle("-fx-stroke: grey; -fx-stroke-width: 20;");
        arm2.setStrokeLineCap(StrokeLineCap.ROUND);

        
        Line arm3 = new Line(arm2.getEndX(), arm2.getEndY(), 450, 200);
        arm3.setStyle("-fx-stroke: grey; -fx-stroke-width: 20;");
        arm3.setStrokeLineCap(StrokeLineCap.ROUND);
        
        //circles for axis's
        Circle axis1 = new Circle(axis1X, axis1Y, 5);
        Circle axis2 = new Circle(axis2X, axis2Y, 5);
        Circle axis3 = new Circle(axis3X, axis3Y, 5);
        Circle brush = new Circle(brushX, brushY, 15);

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setLayoutY(240);
        colorPicker.setLayoutX(620);
        String colorPickerStyle = colorPicker.getStyle();
        colorPickerStyle = colorPickerStyle + " -fx-background-color: #000000;";
        //System.out.print(colorPickerStyle);
        colorPicker.setStyle(colorPickerStyle);
        colorPicker.setValue(Color.BLACK);

        addPressAndHoldHandler(btn1, new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent e) {
            
                Line newArm1 = new Line(arm1.getStartX(),arm1.getStartY(),arm1.getEndX(),arm1.getEndY());
                newArm1.getTransforms().add(new Rotate(-1,newArm1.getStartX(),newArm1.getStartY()));
                Point2D startPoint1 = newArm1.localToParent(newArm1.getStartX(),newArm1.getStartY());
                Point2D endPoint1 = newArm1.localToParent(newArm1.getEndX(),newArm1.getEndY());
                arm1.setStartX(startPoint1.getX());
                arm1.setStartY(startPoint1.getY());
                arm1.setEndX(endPoint1.getX());
                arm1.setEndY(endPoint1.getY());
                axis2.setCenterX(endPoint1.getX());
                axis2.setCenterY(endPoint1.getY());
                Line newArm2 = new Line(arm2.getStartX(),arm2.getStartY(),arm2.getEndX(),arm2.getEndY());
                newArm2.getTransforms().add(new Rotate(-1,newArm1.getStartX(),newArm1.getStartY()));
                Point2D startPoint = newArm2.localToParent(newArm2.getStartX(),newArm2.getStartY());
                Point2D endPoint = newArm2.localToParent(newArm2.getEndX(),newArm2.getEndY());
                arm2.setStartX(startPoint.getX());
                arm2.setStartY(startPoint.getY());
                arm2.setEndX(endPoint.getX());
                arm2.setEndY(endPoint.getY());
                axis3.setCenterX(endPoint.getX());
                axis3.setCenterY(endPoint.getY());
                Line newArm3 = new Line(arm3.getStartX(),arm3.getStartY(),arm3.getEndX(),arm3.getEndY());
                newArm3.getTransforms().add(new Rotate(-1,newArm1.getStartX(),newArm1.getStartY()));
                startPoint = newArm3.localToParent(newArm3.getStartX(),newArm3.getStartY());
                endPoint = newArm3.localToParent(newArm3.getEndX(),newArm3.getEndY());
                arm3.setStartX(startPoint.getX());
                arm3.setStartY(startPoint.getY());
                arm3.setEndX(endPoint.getX());
                arm3.setEndY(endPoint.getY());
                brush.setCenterX(endPoint.getX());
                brush.setCenterY(endPoint.getY());
            }
        });

        addPressAndHoldHandler(btn2, new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent e) {
                Line newArm1 = new Line(arm1.getStartX(),arm1.getStartY(),arm1.getEndX(),arm1.getEndY());
                newArm1.getTransforms().add(new Rotate(1,newArm1.getStartX(),newArm1.getStartY()));
                Point2D startPoint1 = newArm1.localToParent(newArm1.getStartX(),newArm1.getStartY());
                Point2D endPoint1 = newArm1.localToParent(newArm1.getEndX(),newArm1.getEndY());
                arm1.setStartX(startPoint1.getX());
                arm1.setStartY(startPoint1.getY());
                arm1.setEndX(endPoint1.getX());
                arm1.setEndY(endPoint1.getY());
                axis2.setCenterX(endPoint1.getX());
                axis2.setCenterY(endPoint1.getY());
                Line newArm2 = new Line(arm2.getStartX(),arm2.getStartY(),arm2.getEndX(),arm2.getEndY());
                newArm2.getTransforms().add(new Rotate(1,newArm1.getStartX(),newArm1.getStartY()));
                Point2D startPoint = newArm2.localToParent(newArm2.getStartX(),newArm2.getStartY());
                Point2D endPoint = newArm2.localToParent(newArm2.getEndX(),newArm2.getEndY());
                arm2.setStartX(startPoint.getX());
                arm2.setStartY(startPoint.getY());
                arm2.setEndX(endPoint.getX());
                arm2.setEndY(endPoint.getY());
                axis3.setCenterX(endPoint.getX());
                axis3.setCenterY(endPoint.getY());
                Line newArm3 = new Line(arm3.getStartX(),arm3.getStartY(),arm3.getEndX(),arm3.getEndY());
                newArm3.getTransforms().add(new Rotate(1,newArm1.getStartX(),newArm1.getStartY()));
                startPoint = newArm3.localToParent(newArm3.getStartX(),newArm3.getStartY());
                endPoint = newArm3.localToParent(newArm3.getEndX(),newArm3.getEndY());
                arm3.setStartX(startPoint.getX());
                arm3.setStartY(startPoint.getY());
                arm3.setEndX(endPoint.getX());
                arm3.setEndY(endPoint.getY());
                brush.setCenterX(endPoint.getX());
                brush.setCenterY(endPoint.getY());
            }
        });

        addPressAndHoldHandler(btn3, new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent e) {
                Line newArm2 = new Line(arm2.getStartX(),arm2.getStartY(),arm2.getEndX(),arm2.getEndY());
                newArm2.getTransforms().add(new Rotate(-1,newArm2.getStartX(),newArm2.getStartY()));
                Point2D startPoint = newArm2.localToParent(newArm2.getStartX(),newArm2.getStartY());
                Point2D endPoint = newArm2.localToParent(newArm2.getEndX(),newArm2.getEndY());
                arm2.setStartX(startPoint.getX());
                arm2.setStartY(startPoint.getY());
                arm2.setEndX(endPoint.getX());
                arm2.setEndY(endPoint.getY());
                axis3.setCenterX(endPoint.getX());
                axis3.setCenterY(endPoint.getY());
                Line newArm3 = new Line(arm3.getStartX(),arm3.getStartY(),arm3.getEndX(),arm3.getEndY());
                newArm3.getTransforms().add(new Rotate(-1,newArm2.getStartX(),newArm2.getStartY()));
                startPoint = newArm3.localToParent(newArm3.getStartX(),newArm3.getStartY());
                endPoint = newArm3.localToParent(newArm3.getEndX(),newArm3.getEndY());
                arm3.setStartX(startPoint.getX());
                arm3.setStartY(startPoint.getY());
                arm3.setEndX(endPoint.getX());
                arm3.setEndY(endPoint.getY());
                brush.setCenterX(endPoint.getX());
                brush.setCenterY(endPoint.getY());
            }
        });

        addPressAndHoldHandler(btn4, new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent e) {
                Line newArm2 = new Line(arm2.getStartX(),arm2.getStartY(),arm2.getEndX(),arm2.getEndY());
                newArm2.getTransforms().add(new Rotate(1,newArm2.getStartX(),newArm2.getStartY()));
                Point2D startPoint = newArm2.localToParent(newArm2.getStartX(),newArm2.getStartY());
                Point2D endPoint = newArm2.localToParent(newArm2.getEndX(),newArm2.getEndY());
                arm2.setStartX(startPoint.getX());
                arm2.setStartY(startPoint.getY());
                arm2.setEndX(endPoint.getX());
                arm2.setEndY(endPoint.getY());
                axis3.setCenterX(endPoint.getX());
                axis3.setCenterY(endPoint.getY());
                Line newArm3 = new Line(arm3.getStartX(),arm3.getStartY(),arm3.getEndX(),arm3.getEndY());
                newArm3.getTransforms().add(new Rotate(1,newArm2.getStartX(),newArm2.getStartY()));
                startPoint = newArm3.localToParent(newArm3.getStartX(),newArm3.getStartY());
                endPoint = newArm3.localToParent(newArm3.getEndX(),newArm3.getEndY());
                arm3.setStartX(startPoint.getX());
                arm3.setStartY(startPoint.getY());
                arm3.setEndX(endPoint.getX());
                arm3.setEndY(endPoint.getY());
                brush.setCenterX(endPoint.getX());
                brush.setCenterY(endPoint.getY());
            }   
        });

        addPressAndHoldHandler(btn5, new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent e) {
                Line newArm3 = new Line(arm3.getStartX(),arm3.getStartY(),arm3.getEndX(),arm3.getEndY());
                newArm3.getTransforms().add(new Rotate(-1,newArm3.getStartX(),newArm3.getStartY()));
                Point2D startPoint = newArm3.localToParent(newArm3.getStartX(),newArm3.getStartY());
                Point2D endPoint = newArm3.localToParent(newArm3.getEndX(),newArm3.getEndY());
                arm3.setStartX(startPoint.getX());
                arm3.setStartY(startPoint.getY());
                arm3.setEndX(endPoint.getX());
                arm3.setEndY(endPoint.getY());
                brush.setCenterX(endPoint.getX());
                brush.setCenterY(endPoint.getY());

            }
        });    

        addPressAndHoldHandler(btn6, new EventHandler<MouseEvent>() {
            @Override  
            public void handle(MouseEvent e) {
                Line newArm3 = new Line(arm3.getStartX(),arm3.getStartY(),arm3.getEndX(),arm3.getEndY());
                newArm3.getTransforms().add(new Rotate(1,newArm3.getStartX(),newArm3.getStartY()));
                Point2D startPoint = newArm3.localToParent(newArm3.getStartX(),newArm3.getStartY());
                Point2D endPoint = newArm3.localToParent(newArm3.getEndX(),newArm3.getEndY());
                arm3.setStartX(startPoint.getX());
                arm3.setStartY(startPoint.getY());
                arm3.setEndX(endPoint.getX());
                arm3.setEndY(endPoint.getY());
                brush.setCenterX(endPoint.getX());
                brush.setCenterY(endPoint.getY());
            }
        });

        Pane root = new Pane();
        
        root.setStyle("-fx-background-color: #500000");
        
        root.getChildren().add(display);
        root.getChildren().add(control);
        root.getChildren().add(controlP);
        root.getChildren().add(license);
        root.getChildren().add(btn1);
        root.getChildren().add(btn2);
        root.getChildren().add(btn3);
        root.getChildren().add(btn4);
        root.getChildren().add(btn5);
        root.getChildren().add(btn6);
        root.getChildren().add(paint);
        root.getChildren().add(arc);
        root.getChildren().add(arm1);
        root.getChildren().add(arm2);
        root.getChildren().add(arm3);
        root.getChildren().add(axis1);
        root.getChildren().add(axis2);
        root.getChildren().add(axis3);
        root.getChildren().add(brush);
        root.getChildren().add(colorPicker);
        Scene scene = new Scene(root, 800, 500);
        
        primaryStage.setTitle("PaintBot");
        primaryStage.setScene(scene);
        primaryStage.show();
		
		   
        paint.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {

                Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), 15);
                circle.setFill(colorPicker.getValue());
                root.getChildren().add(circle);
                brush.toFront();
            }
        });
	
    }

    private void addPressAndHoldHandler(Node node, EventHandler<MouseEvent> handler) {

        class Wrapper<T> { T content ; }
        Wrapper<MouseEvent> eventWrapper = new Wrapper<>();

        PauseTransition holdTimer = new PauseTransition(Duration.millis(500));
        holdTimer.setOnFinished(event -> {
            handler.handle(eventWrapper.content);
            holdTimer.setDuration(Duration.millis(15));
            holdTimer.playFromStart();
        });


        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            eventWrapper.content = event ;
            handler.handle(eventWrapper.content);
            holdTimer.playFromStart();
        });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            holdTimer.stop();
            holdTimer.setDuration(Duration.millis(500));
        });
        node.addEventHandler(MouseEvent.DRAG_DETECTED, event -> {
            holdTimer.stop();
            holdTimer.setDuration(Duration.millis(500));
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
