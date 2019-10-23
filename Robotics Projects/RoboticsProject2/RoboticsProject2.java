//package RoboticsProject2;

import java.io.Console;
import java.io.Serializable;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Point2D;
import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;


public class RoboticsProject2 extends Application {
    int bSize = 20;
    boolean HOLD_PAINT;
	Pane root;
	
    @Override
    public void start(Stage primaryStage) {
        //Set display area and control panel backgrounds
        Label title = new Label("Rock 'Em Sock 'Em Robots");
        title.setStyle("-fx-font: 64 Cambria; -fx-padding:8px;");
        title.setLayoutY(10);
        title.setLayoutX(40);
        title.setTextFill(Color.web("#FFFFFF"));
        title.setTextAlignment(TextAlignment.CENTER);
        
        Rectangle display = new Rectangle(10, 110, 580, 580);
        display.setFill(Color.WHITE);
        display.setStroke(Color.BLACK);
        
        Rectangle jointControlBack = new Rectangle(600, 130, 180, 220);
        jointControlBack.setFill(Color.GREY);
        jointControlBack.setStroke(Color.BLACK);
        
        Rectangle worldControlBack = new Rectangle(600, 360, 180, 180);
        worldControlBack.setFill(Color.GREY);
        worldControlBack.setStroke(Color.BLACK);
        
        Rectangle parametersBack = new Rectangle(600, 550, 180, 100);
        parametersBack.setFill(Color.GREY);
        parametersBack.setStroke(Color.BLACK);
        
        //Joint mode Control Panel
        Label controlP = new Label("Joint Mode \nControl Panel");
        controlP.setStyle("-fx-font: 18 Cambria;");
        controlP.setLayoutY(133);
        controlP.setLayoutX(640);
        controlP.setTextFill(Color.web("#000000"));
        controlP.setTextAlignment(TextAlignment.CENTER);
        
        Button btn1 = new Button();
        btn1.setText("Joint 1 left");
        btn1.setLayoutY(180);
        btn1.setLayoutX(610);
        btn1.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn2 = new Button();
        btn2.setText("Joint 1 right");
        btn2.setLayoutY(180);
        btn2.setLayoutX(690);
        btn2.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn3 = new Button();
        btn3.setText("Joint 2 left");
        btn3.setLayoutY(220);
        btn3.setLayoutX(610);
        btn3.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn4 = new Button();
        btn4.setText("Joint 2 right");
        btn4.setLayoutY(220);
        btn4.setLayoutX(690);
        btn4.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn5 = new Button();
        btn5.setText("Joint 3 left");
        btn5.setLayoutY(260);
        btn5.setLayoutX(610);
        btn5.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn6 = new Button();
        btn6.setText("Joint 3 right");
        btn6.setLayoutY(260);
        btn6.setLayoutX(690);
        btn6.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button paint = new Button();
        paint.setText("Paint");
        paint.setLayoutY(300);
        paint.setLayoutX(660);
        paint.setStyle("-fx-font: 16 Cambria; -fx-base: #000000;");
        
        CheckBox hold1 = new CheckBox();
        hold1.setText("Hold");
        hold1.setSelected(false);
        hold1.setLayoutY(310);
        hold1.setLayoutX(725);
        hold1.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        //World mode Control Panel
        Label controlPW = new Label("World Mode \nControl Panel");
        controlPW.setStyle("-fx-font: 18 Cambria;");
        controlPW.setLayoutY(363);
        controlPW.setLayoutX(640);
        controlPW.setTextFill(Color.web("#000000"));
        controlPW.setTextAlignment(TextAlignment.CENTER);
        
        Button btn1W = new Button();
        btn1W.setText("X+");
        btn1W.setLayoutY(440);
        btn1W.setLayoutX(710);
        btn1W.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn2W = new Button();
        btn2W.setText("X-");
        btn2W.setLayoutY(440);
        btn2W.setLayoutX(640);
        btn2W.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn3W = new Button();
        btn3W.setText("Y-");
        btn3W.setLayoutY(470);
        btn3W.setLayoutX(675);
        btn3W.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button btn4W = new Button();
        btn4W.setText("Y+");
        btn4W.setLayoutY(410);
        btn4W.setLayoutX(675);
        btn4W.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        Button paintW = new Button();
        paintW.setText("Paint");
        paintW.setLayoutY(500);
        paintW.setLayoutX(660);
        paintW.setStyle("-fx-font: 16 Cambria; -fx-base: #000000;");
        
        //Parameters
        Label parametersLabel = new Label("Parameters");
        parametersLabel.setStyle("-fx-font: 18 Cambria;");
        parametersLabel.setLayoutY(553);
        parametersLabel.setLayoutX(645);
        parametersLabel.setTextFill(Color.web("#000000"));
        parametersLabel.setTextAlignment(TextAlignment.CENTER);
        
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setLayoutY(580);
        colorPicker.setLayoutX(625);
        String colorPickerStyle = colorPicker.getStyle();
        colorPickerStyle = colorPickerStyle + " -fx-background-color: #000000;";
        colorPicker.setStyle(colorPickerStyle);
        colorPicker.setValue(Color.BLACK);
        
        TextField brushSize = new TextField();
        brushSize.setPromptText("Brush Size");
        brushSize.setLayoutY(613);
        brushSize.setLayoutX(610);
        brushSize.setPrefWidth(100);
        
        Button submitSize = new Button();
        submitSize.setText("Submit");
        submitSize.setLayoutY(613);
        submitSize.setLayoutX(720);
        submitSize.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        
        //Base of robot arm
        Arc arc = new Arc();
        arc.setLayoutX(290.0);
        arc.setLayoutY(690.0);
        arc.setRadiusX(50.0);
        arc.setRadiusY(50.0);
        arc.setStartAngle(0.0);
        arc.setLength(180.0);
        arc.setType(ArcType.ROUND);
        arc.setFill(Color.GREY);
        
        //variables to hold coordinates of Axis nodes and brush size
        int axis1X = 290;
        int axis1Y = 660;
        int axis2X = 290;
        int axis2Y = 510;
        int axis3X = 290;
        int axis3Y = 410;
        int brushX = 290;
        int brushY = 335;
        
        
        //arms, dependent on coords
        Line arm1 = new Line(axis1X, axis1Y, axis2X, axis2Y);
        arm1.setStyle("-fx-stroke: grey; -fx-stroke-width: 20;");
        arm1.setStrokeLineCap(StrokeLineCap.ROUND);
        
        Line arm2 = new Line(arm1.getEndX(), arm1.getEndY(), axis3X, axis3Y);
        arm2.setStyle("-fx-stroke: grey; -fx-stroke-width: 20;");
        arm2.setStrokeLineCap(StrokeLineCap.ROUND);

        
        Line arm3 = new Line(arm2.getEndX(), arm2.getEndY(), brushX, brushY);
        arm3.setStyle("-fx-stroke: grey; -fx-stroke-width: 20;");
        arm3.setStrokeLineCap(StrokeLineCap.ROUND);
        
        //circles for axis's
        Circle axis1 = new Circle(axis1X, axis1Y, 5);
        Circle axis2 = new Circle(axis2X, axis2Y, 5);
        Circle axis3 = new Circle(axis3X, axis3Y, 5);
        Circle brush = new Circle(brushX, brushY, bSize);
        
        //copyright label
        Label license = new Label("© Zachariah Stratton\n"
                + "Pablo Vielma Jr.\n"
                + "Shaun Fattig\n"
                + "Russell Pier\n"
				+ "Tom King\n");
        license.setStyle("-fx-font: 14 Cambria;");
        license.setLayoutY(730);
        license.setLayoutX(668);
        license.setTextFill(Color.web("#FFFFFF"));
        license.setTextAlignment(TextAlignment.RIGHT);


        //button actions
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
                System.out.println(getAngle(arm3.getStartX(),arm3.getStartY(),arm3.getEndX(),arm3.getEndY()));
            }
        });
        

//****************************************************TODO*********************************************
        addPressAndHoldHandler(btn1W, new EventHandler<MouseEvent>() {
            @Override  
            public void handle(MouseEvent e) {
                double newX = brush.getCenterX() + 1;
                double newY = brush.getCenterY();
                if(ikTriple(arm1, 150, arm2, 100, arm3, 75, newX, newY)){
                    axis2.setCenterX(arm1.getEndX());
                    axis2.setCenterY(arm1.getEndY());
                    axis3.setCenterX(arm2.getEndX());
                    axis3.setCenterY(arm2.getEndY());
                    brush.setCenterX(newX);
                }
                else if(ikPair(arm2, 100, arm3, 75, newX, newY)){
                    axis3.setCenterX(arm2.getEndX());
                    axis3.setCenterY(arm2.getEndY());
                    brush.setCenterX(newX);
                }
				if (HOLD_PAINT) {
					paint(brush, root, colorPicker);
				}
            }
        });
        
        addPressAndHoldHandler(btn2W, new EventHandler<MouseEvent>() {
            @Override  
            public void handle(MouseEvent e) {
                double newX = brush.getCenterX() - 1;
                double newY = brush.getCenterY();

                
                if(ikTriple(arm1, 150, arm2, 100, arm3, 75, newX, newY)){
                    axis2.setCenterX(arm1.getEndX());
                    axis2.setCenterY(arm1.getEndY());
                    axis3.setCenterX(arm2.getEndX());
                    axis3.setCenterY(arm2.getEndY());
                    brush.setCenterX(newX);
                }
                else if(ikPair(arm2, 100, arm3, 75, newX, newY)){
                    axis3.setCenterX(arm2.getEndX());
                    axis3.setCenterY(arm2.getEndY());
                    brush.setCenterX(newX);
                }
				if (HOLD_PAINT) {
					paint(brush, root, colorPicker);
				}
            }
        });
        
        addPressAndHoldHandler(btn3W, new EventHandler<MouseEvent>() {
            @Override  
            public void handle(MouseEvent e) {
                double newX = brush.getCenterX();
                double newY = brush.getCenterY() + 1;

                
                if(ikTriple(arm1, 150, arm2, 100, arm3, 75, newX, newY)){
                    axis2.setCenterX(arm1.getEndX());
                    axis2.setCenterY(arm1.getEndY());
                    axis3.setCenterX(arm2.getEndX());
                    axis3.setCenterY(arm2.getEndY());
                    brush.setCenterY(newY);
                }
                else if(ikPair(arm2, 100, arm3, 75, newX, newY)){
                    axis3.setCenterX(arm2.getEndX());
                    axis3.setCenterY(arm2.getEndY());
                    brush.setCenterY(newY);
                }
				if (HOLD_PAINT) {
					paint(brush, root, colorPicker);
				}
            }
        });
        
        addPressAndHoldHandler(btn4W, new EventHandler<MouseEvent>() {
            @Override  
            public void handle(MouseEvent e) {
                double newX = brush.getCenterX();
                double newY = brush.getCenterY() - 1;

                if(ikTriple(arm1, 150, arm2, 100, arm3, 75, newX, newY)){
                    axis2.setCenterX(arm1.getEndX());
                    axis2.setCenterY(arm1.getEndY());
                    axis3.setCenterX(arm2.getEndX());
                    axis3.setCenterY(arm2.getEndY());
                    brush.setCenterY(newY);
                }
                else if(ikPair(arm2, 100, arm3, 75, newX, newY)){
                    axis3.setCenterX(arm2.getEndX());
                    axis3.setCenterY(arm2.getEndY());
                    brush.setCenterY(newY);
                }
				if (HOLD_PAINT) {
					paint(brush, root, colorPicker);
				}
            }
        });

        root = new Pane();
        
        root.setStyle("-fx-background-color: #500000");
        
        root.getChildren().add(title);
        root.getChildren().add(display);
        
        root.getChildren().add(jointControlBack);
        root.getChildren().add(controlP);
        root.getChildren().add(btn1);
        root.getChildren().add(btn2);
        root.getChildren().add(btn3);
        root.getChildren().add(btn4);
        root.getChildren().add(btn5);
        root.getChildren().add(btn6);
        root.getChildren().add(paint);
        root.getChildren().add(hold1);
        
        root.getChildren().add(worldControlBack);
        root.getChildren().add(controlPW);
        root.getChildren().add(btn1W);
        root.getChildren().add(btn2W);
        root.getChildren().add(btn3W);
        root.getChildren().add(btn4W);
        root.getChildren().add(paintW);
        
        root.getChildren().add(parametersBack);
        root.getChildren().add(parametersLabel);
        root.getChildren().add(colorPicker);
        root.getChildren().add(brushSize);
        root.getChildren().add(submitSize);
        
        root.getChildren().add(arc);
        root.getChildren().add(arm1);
        root.getChildren().add(arm2);
        root.getChildren().add(arm3);
        root.getChildren().add(axis1);
        root.getChildren().add(axis2);
        root.getChildren().add(axis3);
        
        
        root.getChildren().add(brush);
        
        root.getChildren().add(license);
        
        Scene scene = new Scene(root, 800, 800);
        
        primaryStage.setTitle("PaintBot");
        primaryStage.setScene(scene);
        primaryStage.show();	
		   
        submitSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    if (brushSize.getText() != null && !brushSize.getText().isEmpty()){
                        brush.setRadius(Integer.parseInt(brushSize.getText()));
                    }
                    System.out.println(bSize + "FUNCTION 1");
                }
        });
		
		
        
		paintW.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
            public void handle(ActionEvent e) {
				if (HOLD_PAINT) {
					paint(brush, root, colorPicker);
				}
				HOLD_PAINT = !HOLD_PAINT;
			}
		});
		
        paint.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                if (hold1.isSelected() != true){
                    Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                    circle.setFill(colorPicker.getValue());
                    root.getChildren().add(circle);
                    brush.toFront();
                }
                else if (hold1.isSelected() == true){
                    addPressAndHoldHandler(btn1, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                    
                    addPressAndHoldHandler(btn2, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                    
                    addPressAndHoldHandler(btn3, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                    
                    addPressAndHoldHandler(btn4, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                    
                    addPressAndHoldHandler(btn5, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                    
                    addPressAndHoldHandler(btn6, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                }
            }
        });
    
            
        paintW.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                if (hold1.isSelected() != true){
                    Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                    circle.setFill(colorPicker.getValue());
                    root.getChildren().add(circle);
                    brush.toFront();
                }
                else if (hold1.isSelected() == true){
                    addPressAndHoldHandler(btn1W, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                    
                    addPressAndHoldHandler(btn2W, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                    
                    addPressAndHoldHandler(btn3W, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                    
                    addPressAndHoldHandler(btn4W, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
                            circle.setFill(colorPicker.getValue());
                            root.getChildren().add(circle);
                            brush.toFront();
                        }
                    });
                }
            }
        });
    
    }

	private void paint(Circle brush, Pane root, ColorPicker colorPicker) {
        Circle circle = new Circle(brush.getCenterX(), brush.getCenterY(), brush.getRadius());
        circle.setFill(colorPicker.getValue());
        root.getChildren().add(circle);
        brush.toFront();
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

    //*************FUNCTIONS TO HELP WITH IK****************
    public double getAngle(double x1, double y1, double x2, double y2){
        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        return angle;
    }
    public void drawL1(double angle, double len, Line line){
        double dx = len*Math.cos(angle);
        double dy = len*Math.sin(angle);
        line.setEndX(line.getStartX()+dx);
        line.setEndY(line.getStartY()+dy);
    }
    public boolean ikPair(Line line1, double len1, Line line2, double len2, double endX, double endY){
        double x1 = line1.getStartX();
        double y1 = line1.getStartY();
        double x2 = endX;
        double y2 = endY;
        double dx = x2 - x1;
        double dy = y2 - y1;
        //distance from first line to new endpoint to complete triangle
        double dist = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
        //law of cosines to find angle within triangle
        double angleB = Math.acos((Math.pow(len2,2) - Math.pow(len1,2) - Math.pow(dist,2))/(2*len1*dist));
        
		if(Double.isNaN(angleB)){
            return false;
        }
		else if ((endY - line1.getStartY()) > 20){
			return false;
		}
        line2.setEndX(endX);
        line2.setEndY(endY);
        double angleA = getAngle(x1,y1,x2,y2);
        //offset from angle of distance to find angle from x axis to line1
        double angleLine1 = angleB + Math.PI + angleA;
        drawL1(angleLine1, len1, line1);
        //redraw line2
        line2.setStartX(line1.getEndX());
        line2.setStartY(line1.getEndY());
        return true;

    }
    public boolean ikTriple(Line line1, double len1, Line line2, double len2, Line line3, double len3, double endX, double endY){
        //Set arm3 to point along alpha
		double start3X;
		double start3Y;
		if ((line1.getStartY()-endY) > 20 || (endX-line1.getStartX()) > 20) {
			double alpha;
			alpha = Math.atan2(line1.getStartY()-endY, endX-line1.getStartX());
			//System.out.println("Alpha: " + alpha);
			start3X = endX - len3 * Math.cos(alpha);
			start3Y = endY + len3 * Math.sin(alpha);
		}
		else {
			start3X = line3.getStartX() + (endX - line3.getEndX());
			start3Y = line3.getStartY() - (line3.getEndY() - endY);
		}
        
        double x1 = line1.getStartX();
        double y1 = line1.getStartY();
        double x2 = start3X;
        double y2 = start3Y;
        double dx = x2 - x1;
        double dy = y2 - y1;
        //distance from line1 to new endpoint to complete triangle
        double dist = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
        System.out.println(dist);
        //law of cosines to find angle within triangle
        double angleB = Math.acos((Math.pow(len2,2) - Math.pow(len1,2) - Math.pow(dist,2))/(2*len1*dist));
        if(Double.isNaN(angleB)){
            return false;
        }
        line3.setStartX(start3X);
        line3.setStartY(start3Y);
        line2.setEndX(line3.getStartX());
        line2.setEndY(line3.getStartY());
        double angleA = getAngle(x1,y1,x2,y2);
        line3.setEndX(endX);
        line3.setEndY(endY);
        //offset from angle of distance to find angle from x axis to line1
        double angleLine1 = angleB + Math.PI + angleA;
        //redraw line1
        drawL1(angleLine1, len1, line1);
        //redraw line2
        line2.setStartX(line1.getEndX());
        line2.setStartY(line1.getEndY());

        return true;

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}