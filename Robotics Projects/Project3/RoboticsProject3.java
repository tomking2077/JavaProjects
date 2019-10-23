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
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.awt.geom.Point2D;
import javafx.scene.layout.Pane;

public class RoboticsProject3 extends Application {
    
	ArrayList<Car> cars = new ArrayList<Car>();
    ArrayList<Point2D> lights = new ArrayList<Point2D>();
    Group vehicles = new Group();
    Pane root = new Pane();
    Window mainWin = new Window();
	
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        //title
        Label title = new Label("Braitenberg Vehicles");
        title.setStyle("-fx-font: 58 Cambria;");
        title.setLayoutY(20);
        title.setLayoutX(250);
        title.setTextFill(Color.web("#FFFFFF"));
        title.setTextAlignment(TextAlignment.CENTER);
        
        //Base display backgrounds
        Rectangle display = new Rectangle(50, 100, 800, 600);
        display.setFill(Color.WHITE);
        display.setStroke(Color.BLACK);

        
        Rectangle vehicleCreationBack = new Rectangle(875, 100, 210, 400);
        vehicleCreationBack.setFill(Color.GREY);
        vehicleCreationBack.setStroke(Color.BLACK);
        
        Rectangle lightCreationBack = new Rectangle(875, 550, 210, 200);
        lightCreationBack.setFill(Color.GREY);
        lightCreationBack.setStroke(Color.BLACK);
        
        //Fields to create vehicles/light sources
        
        //First Vehicle height/width
        Label vehicleSizeLabel = new Label("Vehicle Size");
        vehicleSizeLabel.setStyle("-fx-font: 18 Cambria;");
        vehicleSizeLabel.setLayoutY(120);
        vehicleSizeLabel.setLayoutX(930);
        vehicleSizeLabel.setTextFill(Color.web("#000000"));
        vehicleSizeLabel.setTextAlignment(TextAlignment.CENTER);
        
        TextField vehicleSizeWidth = new TextField();
        vehicleSizeWidth.setPromptText("Width");
        vehicleSizeWidth.setText("20");
        vehicleSizeWidth.setLayoutY(150);
        vehicleSizeWidth.setLayoutX(915);
        vehicleSizeWidth.setPrefWidth(60);
        
        TextField vehicleSizeHeight = new TextField();
        vehicleSizeHeight.setPromptText("Height");
        vehicleSizeHeight.setText("20");
        vehicleSizeHeight.setLayoutY(150);
        vehicleSizeHeight.setLayoutX(985);
        vehicleSizeHeight.setPrefWidth(60);
        
        
        //Then K-Matrix for vehicle
        Label kMatrixSizeLabel = new Label("K-Matrix");
        kMatrixSizeLabel.setStyle("-fx-font: 18 Cambria;");
        kMatrixSizeLabel.setLayoutY(220);
        kMatrixSizeLabel.setLayoutX(950);
        kMatrixSizeLabel.setTextFill(Color.web("#000000"));
        kMatrixSizeLabel.setTextAlignment(TextAlignment.CENTER);
        
        TextField matrixK11 = new TextField();
        matrixK11.setPromptText("k11");
        matrixK11.setText("1");
        matrixK11.setLayoutY(250);
        matrixK11.setLayoutX(920);
        matrixK11.setPrefWidth(50);
        
        TextField matrixK12 = new TextField();
        matrixK12.setPromptText("k12");
        matrixK12.setText("0");
        matrixK12.setLayoutY(250);
        matrixK12.setLayoutX(990);
        matrixK12.setPrefWidth(50);
        
        TextField matrixK21 = new TextField();
        matrixK21.setPromptText("k21");
        matrixK21.setText("0");
        matrixK21.setLayoutY(300);
        matrixK21.setLayoutX(920);
        matrixK21.setPrefWidth(50);
        
        TextField matrixK22 = new TextField();
        matrixK22.setPromptText("k22");
        matrixK22.setText("1");
        matrixK22.setLayoutY(300);
        matrixK22.setLayoutX(990);
        matrixK22.setPrefWidth(50);
        
        
        //Then Initial Position of vehicle
        Label initialPositionLabel = new Label("Initial Position");
        initialPositionLabel.setStyle("-fx-font: 18 Cambria;");
        initialPositionLabel.setLayoutY(360);
        initialPositionLabel.setLayoutX(930);
        initialPositionLabel.setTextFill(Color.web("#000000"));
        initialPositionLabel.setTextAlignment(TextAlignment.CENTER);
        
        TextField initialPositionX = new TextField();
        initialPositionX.setPromptText("X Coord");
        initialPositionX.setText("200");
        initialPositionX.setLayoutY(400);
        initialPositionX.setLayoutX(920);
        initialPositionX.setPrefWidth(50);
        
        TextField initialPositionY = new TextField();
        initialPositionY.setPromptText("Y Coord");
        initialPositionY.setText("200");
        initialPositionY.setLayoutY(400);
        initialPositionY.setLayoutX(990);
        initialPositionY.setPrefWidth(50);
        
        //Submit vehicle to arraylist
        Button submitVehicle = new Button();
        submitVehicle.setText("Create Vehicle");
        submitVehicle.setLayoutY(450);
        submitVehicle.setLayoutX(940);
        submitVehicle.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
        
        //Light source adding
        
        
        
        Label lightSourcePosLabel = new Label("Position of Light Source");
        lightSourcePosLabel.setStyle("-fx-font: 18 Cambria;");
        lightSourcePosLabel.setLayoutY(570);
        lightSourcePosLabel.setLayoutX(890);
        lightSourcePosLabel.setTextFill(Color.web("#000000"));
        lightSourcePosLabel.setTextAlignment(TextAlignment.CENTER);
        
        TextField lightPositionX = new TextField();
        lightPositionX.setPromptText("X Coord");
        lightPositionX.setText("250");
        lightPositionX.setLayoutY(600);
        lightPositionX.setLayoutX(920);
        lightPositionX.setPrefWidth(50);
        
        TextField lightPositionY = new TextField();
        lightPositionY.setPromptText("Y Coord");
        lightPositionY.setText("250");
        lightPositionY.setLayoutY(600);
        lightPositionY.setLayoutX(990);
        lightPositionY.setPrefWidth(50);
        
        Button submitLightSource = new Button();
        submitLightSource.setText("Create Light Source");
        submitLightSource.setLayoutY(630);
        submitLightSource.setLayoutX(925);
        submitLightSource.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");

		
		//Load parameters from file
        Button loadParams = new Button();
        loadParams.setText("Load from file");
        loadParams.setLayoutY(750);
        loadParams.setLayoutX(400);
        loadParams.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");
		
		//Save parameters to file
        Button writeParams = new Button();
        writeParams.setText("Save to file");
        writeParams.setLayoutY(750);
        writeParams.setLayoutX(500);
        writeParams.setStyle("-fx-font: 12 Cambria; -fx-base: #000000;");


        root.setStyle("-fx-background-color: #500000");
        
        root.getChildren().add(display);

		//Action to write parameters to file parameters.txt
		writeParams.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
				saveParameters();
			}
		});
		
		//Action to load parameters from file parameters.txt
		loadParams.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
				loadParameters();
			}
		});
		
		
        //Action to add vehicle to pane
        submitVehicle.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                Point2D initialPoint = new Point2D.Double(Double.parseDouble(initialPositionX.getText()),Double.parseDouble(initialPositionY.getText()));
                Point2D dimensions = new Point2D.Double(Double.parseDouble(vehicleSizeWidth.getText()),Double.parseDouble(vehicleSizeHeight.getText()));
                double [] KArray = {Double.parseDouble(matrixK11.getText()),Double.parseDouble(matrixK12.getText()),Double.parseDouble(matrixK21.getText()),Double.parseDouble(matrixK22.getText())};

                //TODO--- DRAW AN ACTUAL CAR HERE INSTEAD
                //Rectangle(xpos,ypos,width,height); width and height subtracted to set the position based on origin
                Rectangle rect = new Rectangle (initialPoint.getX()-(dimensions.getX()/2), initialPoint.getY()-(dimensions.getY()/2), dimensions.getX(), dimensions.getY());
                rect.setFill(Color.VIOLET);
                vehicles.getChildren().add(rect);

                //Radians
                int ang = 0;
                Car tempCar = new Car(initialPoint, ang, Integer.parseInt(vehicleSizeWidth.getText()), Integer.parseInt(vehicleSizeHeight.getText()));

                cars.add(tempCar);

                tempCar.setKArray(KArray);

                
                //This is where the animation will be for each vehicle, though we may need to do this outside the action event to access the light positions. This is a way to individually control the animation of each vehicle.
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(500),
                        ae -> {
                        //rect.getTransforms().add(new Rotate(1,rect.getX()+(dimensions.getX()/2),rect.getY()+(dimensions.getY()/2))); //rotates at 1 degree per 50 milliseconds. rectangle dimensions subtracted to rotate about the origin.
                        //add animation stuff here!!
                        
                        setIntensity(tempCar, mainWin);
                        calculateWheelSpeeds(tempCar);
                        javafx.geometry.Point2D tempRect = tempCar.moveCarPablo(rect);
                        System.out.println("tempRect X: " + tempRect.getX());
                        rect.setX(tempRect.getX());
                        rect.setY(tempRect.getY());
                        }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
        });
        
        //Action to add light sources to arraylist
        submitLightSource.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Point2D lightPoint = new Point2D.Double(Double.parseDouble(lightPositionX.getText()),Double.parseDouble(lightPositionY.getText()));
                lights.add(lightPoint);
                mainWin.setLightPoints(lights);

                Circle bulb = new Circle(lightPoint.getX(), lightPoint.getY(), 20);
                bulb.setFill(Color.YELLOW);
                root.getChildren().add(bulb);
            }
        });

        

        //Parent root = FXMLLoader.load(getClass().getResource("/roboticsproject3/sample.fxml"));
        
        root.getChildren().add(title);
        root.getChildren().add(vehicles);
        root.getChildren().add(vehicleCreationBack);
        root.getChildren().add(lightCreationBack);
        root.getChildren().add(vehicleSizeLabel);
        root.getChildren().add(vehicleSizeWidth);
        root.getChildren().add(vehicleSizeHeight);
        root.getChildren().add(kMatrixSizeLabel);
        root.getChildren().add(matrixK11);
        root.getChildren().add(matrixK12);
        root.getChildren().add(matrixK21);
        root.getChildren().add(matrixK22);
        root.getChildren().add(initialPositionLabel);
        root.getChildren().add(initialPositionX);
        root.getChildren().add(initialPositionY);
        root.getChildren().add(submitVehicle);
        
        root.getChildren().add(lightSourcePosLabel);
        root.getChildren().add(lightPositionX);
        root.getChildren().add(lightPositionY);
        root.getChildren().add(submitLightSource);
		
		root.getChildren().add(loadParams);
		root.getChildren().add(writeParams);
        
        
        primaryStage.setTitle("Robotics Project 3");
        primaryStage.setScene(new Scene(root, 1100, 800));
        primaryStage.show();

        //Testing Car
        Point2D testPoint = new Point2D.Double(50, 50);
        Car myCar = new Car(testPoint, 0, 10, 10);
        System.out.println(myCar.getSensor1Point().getX());

        //Testing Window
        Window myWindow = new Window();
        double[] myKArray = {1.0,0.3,0.4,1.0};
        myCar.setKArray(myKArray);
        ArrayList<Point2D> lightPoints = new ArrayList<Point2D>();
        lightPoints.add(new Point2D.Double());
        lightPoints.add(new Point2D.Double(20.3,20));
        myWindow.setLightPoints(lightPoints);





        //Testing setIntensity
        printArray(myCar.getIntensities());
        setIntensity(myCar, myWindow);
        printArray(myCar.getIntensities());

        //Testing calculatespeeds
        printArray(myCar.getWheelSpeeds());
        calculateWheelSpeeds(myCar);
        printArray(myCar.getWheelSpeeds());





/*
        int _timeSlice = 67;  // update every 66.7 milliseconds (1/15th of a second)
        Timer _timer = new Timer(_timeSlice, new ActionListener() {
            public void actionPerformed (ActionEvent e) {

                System.out.println(_timeSlice);
            }
        });
        */
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    void setIntensity(Car car, Window window){
        Point2D sensor1Point = car.getSensor1Point();
        Point2D sensor2Point = car.getSensor2Point();

        double Intensity1 = 0;
        double Intensity2 = 0;

        double distance;
        double intensity;
        ArrayList<Point2D>lightPoints = window.getLightPoints();
        for(int i = 0; i < lightPoints.size(); i++){
            distance = sensor1Point.distance(lightPoints.get(i));
            intensity = scaleIntensity(distance);
            Intensity1 += intensity;

            distance = sensor2Point.distance(lightPoints.get(i));
            intensity = scaleIntensity(distance);
            Intensity2 += intensity;
        }

        double[] Intensities = {Intensity1, Intensity2};
        car.setIntensity(Intensities);
    }

    double scaleIntensity(double distance){
        double maxDistance  = 100;
        if(distance > maxDistance){
            return 0;
        }
        else
            return 1 - (distance/maxDistance);
    }

    void calculateWheelSpeeds(Car car){
        double[] KArray = car.getKArray();
        double[] Intensities = car.getIntensities();

        double wheel1Speed = KArray[0]*Intensities[0] + KArray[1]*Intensities[1];
        double wheel2Speed = KArray[2]*Intensities[0] + KArray[3]*Intensities[1];

        double[] wheelSpeeds = {10*wheel1Speed, 10*wheel2Speed};
        car.setWheelSpeeds(wheelSpeeds);
    }
    //Debug function
    void printArray(double[] array){
        for(int i = 0; i < array.length; i++){
            System.out.println(array[i]);
        }
    }
	
	
	void saveParameters() {
		File parameters = new File("parameters.txt");
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(parameters));
			
			int carsSize = cars.size();
			int lightsSize = lights.size();
			
			for (int i = 0; i < carsSize; i++) {
				writer.write("Vehicle" + Integer.toString(i) + ":");
				writer.newLine();
				writer.write("K:[" + Double.toString(cars.get(i).getKArray()[0]) + "," + Double.toString(cars.get(i).getKArray()[1]) + "," + Double.toString(cars.get(i).getKArray()[2]) + "," + Double.toString(cars.get(i).getKArray()[3]) + "]");
				writer.newLine();
				writer.write("X:" + Double.toString(cars.get(i).getCenterPoint().getX()));
				writer.newLine();
				writer.write("Y:" + Double.toString(cars.get(i).getCenterPoint().getY()));
				writer.newLine();
				writer.write("L:" + Integer.toString(cars.get(i).getWidth()));
				writer.newLine();
				writer.write("W:" + Integer.toString(cars.get(i).getHeight()));
				writer.newLine();
				writer.newLine();
			}
				
			for (int i = 0; i < lightsSize; i++) {
				writer.write("Light" + Integer.toString(i) + ":\n");
				writer.newLine();
				writer.write("X:" + Double.toString(lights.get(i).getX()));
				writer.newLine();
				writer.write("Y:" + Double.toString(lights.get(i).getY()));
				writer.newLine();
				writer.newLine();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void loadParameters() {
		File parameters = new File("parameters.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(parameters));
			String text = null;
			//FIXME: add default values in case not specified
			while ((text = reader.readLine()) != null) {
				double x_coord = 0;
				double y_coord = 0;
				double length = 4;
				double width = 4;
				double KArray[];
				int widthInt = 4;
				int lengthInt = 4;
				KArray = new double[4];
				
				if (text.indexOf("Vehicle") == 0) {
					//Vehicle
					for (int i = 0; i < 3; i++) {
						text = reader.readLine();
						if (text.indexOf("K:") == 0) {
							String arrayText = text.substring(2);
							arrayText = arrayText.replace("[", "");
							arrayText = arrayText.replace("]", "");
							String stringKArray[] = arrayText.split(",");
							
							for (int k = 0; k < 4; k++) {
								KArray[k] = Double.parseDouble(stringKArray[k]);
								System.out.println(KArray[k]);
							}
						}
						else if (text.indexOf("X:") == 0) {
							x_coord = Double.parseDouble(text.substring(2));
							System.out.println(x_coord);
						}
						else if (text.indexOf("Y:") == 0) {
							y_coord = Double.parseDouble(text.substring(2));
							System.out.println(y_coord);
						}
						else if (text.indexOf("L:") == 0) {
							//Vehicle length
							length = Double.parseDouble(text.substring(2));
							lengthInt = Integer.parseInt(text.substring(2));
							System.out.println(length);
						}
						else if (text.indexOf("W:") == 0) {
							//Vehicle width
							width = Double.parseDouble(text.substring(2));
							widthInt = Integer.parseInt(text.substring(2));
							System.out.println(width);
						}
					}
					Point2D initialPoint = new Point2D.Double(x_coord, y_coord);
					Point2D dimensions = new Point2D.Double(width, length);
					
					//TODO--- DRAW AN ACTUAL CAR HERE INSTEAD
					//Rectangle(xpos,ypos,width,height); width and height subtracted to set the position based on origin
					Rectangle rect = new Rectangle (initialPoint.getX()-(dimensions.getX()/2), initialPoint.getY()-(dimensions.getY()/2), dimensions.getX(), dimensions.getY());
					rect.setFill(Color.VIOLET);
					vehicles.getChildren().add(rect);
					System.out.println("Adding circle at point: (" + initialPoint.getX() + ", " + initialPoint.getY() + ") to vehicles ");
					
					
					root.getChildren().add(rect);
					
					int ang = 1;
					Car tempCar = new Car(initialPoint, ang, widthInt, lengthInt);
					
					cars.add(tempCar);
					
					tempCar.setKArray(KArray);
					
					//This is where the animation will be for each vehicle, though we may need to do this outside the action event to access the light positions. This is a way to individually control the animation of each vehicle.
					 Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(50),
                        ae -> {
                        //rect.getTransforms().add(new Rotate(1,rect.getX()+(dimensions.getX()/2),rect.getY()+(dimensions.getY()/2))); //rotates at 1 degree per 50 milliseconds. rectangle dimensions subtracted to rotate about the origin.
                        //add animation stuff here!!
                        setIntensity(tempCar, mainWin);
                        calculateWheelSpeeds(tempCar);
                        double [] tempRect = tempCar.moveCarPablo(rect);
                        rect.setX(tempRect[0]);
                        rect.setY(tempRect[1]);
                        rect.getTransforms().add(new Rotate(tempRect[2],rect.getX(),rect.getY()));
                        }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
				else if (text.indexOf("Light") == 0) {
					//Light
					for (int i = 0; i < 2; i++) {
						text = reader.readLine();
						if (text.indexOf("X:") == 0) {
							x_coord = Double.parseDouble(text.substring(2));
							System.out.println(x_coord);
						}
						else if (text.indexOf("Y:") == 0) {
							y_coord = Double.parseDouble(text.substring(2));
							System.out.println(y_coord);
						}
					}
					Point2D lightPoint = new Point2D.Double(x_coord,y_coord);
					lights.add(lightPoint);
					//Point2D[] temp = lights.toArray(new Point2D[lights.size()]);
					mainWin.setLightPoints(lights);


					Circle bulb = new Circle(lightPoint.getX(), lightPoint.getY(), 20);
					bulb.setFill(Color.YELLOW);
					root.getChildren().add(bulb);
				}			
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
