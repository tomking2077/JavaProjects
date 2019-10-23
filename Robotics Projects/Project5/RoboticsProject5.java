import javafx.application.Application;

//import java.awt.Window;
import java.awt.Window;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class RoboticsProject5 extends Application {
    
    Pane root = new Pane();
    //Window mainWin = new Window();
	
        double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;
        int step = 1;
        ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();
        ArrayList<Rectangle> segmentList = new ArrayList<>();
        ArrayList<Point2D> pointList = new ArrayList<Point2D>();
        double startPointX;
        double startPointY;
        double endPointX;
        double endPointY;
        double recursionStart = 0;
        double recursionEnd = 0;
        ArrayList<Text> recursionCountText = new ArrayList<Text>();
        int displayWidth = 500;
        int displayHeight = 500;
        int windowWidth = 1200;
        int windowHeight = 800;
        Rectangle display;

        List<Line> linesList;
        List<Point2D> midpoints;
        List<Node> nodes;
        Graph graph;
		Circle startPoint;
		Circle endPoint;
		int startID;
		int endID;
		Node startNode;
		Node endNode;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        linesList = new ArrayList<Line>();
        midpoints = new ArrayList<Point2D>();
        graph = new Graph();

        //title
        Label title = new Label("Click to create box 1");
        title.setStyle("-fx-font: 30 Arial; -fx-text-fill: white; text-shadow: -2px 0 black, 0 2px black, 2px 0 black, 0 -2px black;");
        title.setLayoutY(20);
        title.setLayoutX(50);
        
        //Base display backgrounds
        display = new Rectangle(20, 80, 500, 500);
        display.setFill(Color.WHITE);
        display.setStroke(Color.BLACK);
        
        //Load from file
        Button load = new Button();
        load.setText("Load from file");
        load.setLayoutY(600);
        load.setLayoutX(250);
        load.setStyle("-fx-font: 24 Cambria; -fx-base: #000000;");
        
        //Begin Pathing
        Button start = new Button();
        start.setText("Start!");
        start.setLayoutY(600);
        start.setLayoutX(150);
        start.setStyle("-fx-font: 24 Cambria; -fx-base: #000000;");
        
        
        root.setStyle("-fx-background-color: #500000");

        root.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
            Boolean triggered = false;
            Boolean ready = false;
            Boolean completed = false;
            Boolean calculating = false;


            if(step == 1){
                if (mousePosX < (520 - 200) && mousePosY < (580 - 200)){
                    Rectangle rect1 = new Rectangle (520-350, 580-200,200,200); //rectangle(topleft point X, topleft point Y, width, height);
                    rect1.setFill(Color.BLUE);
                    rect1.setStroke(Color.BLACK);
                    Text num = new Text(((520-250) + 10),(380 + 35),"1");
                    num.setStyle("-fx-font: 40 Cambria; -fx-base: #FFFFFF;");
                    root.getChildren().add(rect1);
                    root.getChildren().add(num);
                    title.setText("Click to create box 2");
                    title.setTextFill(Color.WHITE);
                    rectList.add(rect1);
                    triggered = true;
                }
                else {
                    title.setText("Error! Obstance placed outside area");
                    title.setTextFill(Color.RED);
                }

            }
            if(step == 2){
                if (mousePosX < (520 - 150) && mousePosY < (580 - 150)){
                    Rectangle rect2 = new Rectangle (520 - 150, 580-200-150,150,150); //rectangle(topleft point X, topleft point Y, width, height);
                    rect2.setFill(Color.BLUE);
                    rect2.setStroke(Color.BLACK);
                    Text num = new Text((370 + 10),(230 + 35),"2");
                    num.setStyle("-fx-font: 40 Cambria; -fx-base: #FFFFFF;");
                    root.getChildren().add(rect2);
                    root.getChildren().add(num);
                    title.setText("Click to create box 3");
                    title.setTextFill(Color.WHITE);
                    rectList.add(rect2);
                    triggered = true;
                }
                else {
                    title.setText("Error! Obstance placed outside area");
                    title.setTextFill(Color.RED);
                }
            }
            if(step == 3){
                if (mousePosX < (520 - 100) && mousePosY < (580 - 100)){
                    Rectangle rect = new Rectangle (mousePosX, mousePosY,100,100); //rectangle(topleft point X, topleft point Y, width, height);
                    rect.setFill(Color.BLUE);
                    rect.setStroke(Color.BLACK);
                    Text num = new Text((mousePosX + 10),(mousePosY + 35),"3");
                    num.setStyle("-fx-font: 40 Cambria; -fx-base: #FFFFFF;");
                    root.getChildren().add(rect);
                    root.getChildren().add(num);
                    title.setText("Click to select starting point");
                    title.setTextFill(Color.WHITE);
                    rectList.add(rect);
                    triggered = true;
                }
                else {
                    title.setText("Error! Obstance placed outside area");
                    title.setTextFill(Color.RED);
                }
            }
            if(step == 4){
                startPoint = new Circle (mousePosX,mousePosY,10); //rectangle(topleft point X, topleft point Y, width, height);
                startPoint.setFill(Color.BLACK);
                root.getChildren().add(startPoint);
                title.setText("Click to select goal point");
                title.setTextFill(Color.WHITE);
                startPointX = mousePosX;
                startPointY = mousePosY;
                triggered = true;
            }
            if(step == 5){
                endPoint = new Circle (mousePosX, mousePosY,10); //rectangle(topleft point X, topleft point Y, width, height);
                endPoint.setFill(Color.BLACK);
                root.getChildren().add(endPoint);
                title.setText("Click Start!");
                endPointX = mousePosX;
                endPointY = mousePosY;
                triggered = true;
                ready = true;
            }
            
            if (triggered.equals(true)){
                step++;
                triggered = false;
            }

            if(ready == true && completed == false){
                /*for(int i = 0; i < segmentList.size(); i++){
                    segmentList.get(i).toFront();
                    System.out.println(i);
                }
                root.getChildren().addAll(segmentList);

                for(int i = 0; i < recursionCountText.size(); i++){
                    recursionCountText.get(i).toFront();
                }
                root.getChildren().addAll(recursionCountText);
                */

                System.out.println("RecursionStart: " + recursionStart + " RecursionEnd: " + recursionEnd);
                completed = true;
                calculating = true;
            }

            // if(calculating){
            //     Graph testGraph = new Graph();
            //     List<Node> testNodes = new ArrayList<>();
            //     int nodeCount = 5;
            //     int minEdgeCount = 1;
            //     Random rand = new Random();
            //     String CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

            //     for(int i = 0; i < nodeCount; i++){
            //         //char name = CAPS.charAt( rand.nextInt(CAPS.length()) );
            //         Node testNode = new Node(i);
            //         testNodes.add(testNode);
            //     }
            //     testGraph.addNodes(testNodes);

            //     for(int i = 0; i <nodeCount; i++){
            //         int randomCount = rand.nextInt(nodeCount -  minEdgeCount + 1) + minEdgeCount;
            //         for(int j = 0; j < randomCount; j++){
            //             int randomIndex = rand.nextInt(nodeCount);
            //             int randomWeight = rand.nextInt(100);
            //             testNodes.get(i).addNeighbor(testNodes.get(randomIndex), randomWeight);
            //         }
            //     }

            //     testGraph.printGraph();


            //     Graph realTestGraph = new Graph();
            //     Node nodeA = new Node(0);
            //     Node nodeB = new Node(1);
            //     Node nodeC = new Node(2);
            //     Node nodeD = new Node(3);
            //     Node nodeE = new Node(4);

            //     Edge edgeAB = new Edge(nodeA, nodeB, 10);
            //     Edge edgeAC = new Edge(nodeA, nodeC, 20);
            //     Edge[] helperA = new Edge[]{edgeAB, edgeAC};

            //     Edge edgeBD = new Edge(nodeB, nodeD, 50);
            //     Edge edgeBE = new Edge(nodeB, nodeE, 5);
            //     Edge[] helperB = new Edge[]{edgeBD, edgeBE};

            //     Edge edgeCD = new Edge(nodeC, nodeD, 10);
            //     Edge edgeCE = new Edge(nodeC, nodeE, 5);
            //     Edge[] helperC = new Edge[]{edgeCD, edgeCE};

            //     Edge edgeDB = new Edge(nodeD, nodeB, 20);
            //     Edge edgeEC = new Edge(nodeE, nodeC, 20);

            //     ArrayList<Edge> listA = new ArrayList<Edge>(Arrays.asList(helperA));
            //     ArrayList<Edge> listB = new ArrayList<Edge>(Arrays.asList(helperB));
            //     ArrayList<Edge> listC = new ArrayList<Edge>(Arrays.asList(helperC));

            //     nodeA.addEdges(listA);
            //     nodeB.addEdges(listB);
            //     nodeC.addEdges(listC);
            //     nodeD.addEdge(edgeDB);
            //     nodeE.addEdge(edgeEC);

            //     Node nodeArray[] = new Node[]{nodeA, nodeB, nodeC, nodeD, nodeE};
            //     realTestGraph.addNodes(Arrays.asList(nodeArray) );

            //     //realTestGraph.printGraph();
            //     realTestGraph.Djikstra(nodeA);
            //     System.out.println("Done");
            // }

            
        });



        //Action to add vehicle to pane
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                title.setText("");
                drawlines();
                drawMidpoints();
                collectNodes();
                pruneNodes();
				addNodesToGraph();
                drawEdges();
				//graph.Djikstra(startNode);
				//drawPath();
				
                
               // getSegments(rectList, display);
                     Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(100),
                        ae -> {



                        }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
        });
        
        //Action to coordinates from file
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int onePosX, onePosY, twoPosX, twoPosY, threePosX, threePosY, startX, startY, endX, endY;
                System.out.println(new File("config.txt").getAbsolutePath());
                
                try {
                    Scanner sc = new Scanner(new File("config.txt"));
                    
                    while (sc.hasNextLine()){
                        onePosX = sc.nextInt();
                        onePosY = sc.nextInt();
                        twoPosX = sc.nextInt();
                        twoPosY = sc.nextInt();
                        threePosX = sc.nextInt();
                        threePosY = sc.nextInt();
                        startX = sc.nextInt();
                        startY = sc.nextInt();
                        endX = sc.nextInt();
                        endY = sc.nextInt();
                        
                        Rectangle rect = new Rectangle (onePosX, onePosY,200,200); //rectangle(topleft point X, topleft point Y, width, height);
                        rect.setFill(Color.BLUE);
                        rect.setStroke(Color.BLACK);
                        Text num = new Text((onePosX + 10),(onePosY + 35),"1");
                        num.setStyle("-fx-font: 40 Cambria; -fx-base: #FFFFFF;");
                        root.getChildren().add(rect);
                        root.getChildren().add(num);
                        rectList.add(rect);
                
                        Rectangle rect2 = new Rectangle (twoPosX, twoPosY,150,150); //rectangle(topleft point X, topleft point Y, width, height);
                        rect2.setFill(Color.BLUE);
                        rect2.setStroke(Color.BLACK);
                        Text num2 = new Text((twoPosX + 10),(twoPosY + 35),"2");
                        num2.setStyle("-fx-font: 40 Cambria; -fx-base: #FFFFFF;");
                        root.getChildren().add(rect2);
                        root.getChildren().add(num2);
                        rectList.add(rect);
                
                        Rectangle rect3 = new Rectangle (threePosX, threePosY,100,100); //rectangle(topleft point X, topleft point Y, width, height);
                        rect3.setFill(Color.BLUE);
                        rect3.setStroke(Color.BLACK);
                        Text num3 = new Text((threePosX + 10),(threePosY + 35),"3");
                        num3.setStyle("-fx-font: 40 Cambria; -fx-base: #FFFFFF;");
                        root.getChildren().add(rect3);
                        root.getChildren().add(num3);
                        rectList.add(rect);
                
                        Circle startPoint = new Circle (startX,startY,10); //rectangle(topleft point X, topleft point Y, width, height);
                        startPoint.setFill(Color.BLACK);
                        root.getChildren().add(startPoint);
                
                        Circle endPoint = new Circle (endX, endY,10); //rectangle(topleft point X, topleft point Y, width, height);
                        endPoint.setFill(Color.BLACK);
                        root.getChildren().add(endPoint);
                    }
                    sc.close();
                }
                catch (FileNotFoundException i){
                    i.printStackTrace();
                }
                
                
                
            }
        });
        
        
        root.getChildren().add(display);
        root.getChildren().add(title);
        root.getChildren().add(load);
        root.getChildren().add(start);
        
        primaryStage.setTitle("Robotics Project 5");
        primaryStage.setScene(new Scene(root, 540, 700));
        primaryStage.show();
    }

    void getSegments( ArrayList<Rectangle> blocks, Rectangle display){
        System.out.println("starting");
        System.out.println(recursionStart++);

        int intersectionIndex = getFirstIntersection(blocks, display);
        if(intersectionIndex != -1) {
            Rectangle intersectionRect = blocks.get(intersectionIndex);
            System.out.println("Intersected with: " + blocks.get(intersectionIndex));
            double difference = intersectionRect.getX() - display.getX();

            Rectangle newCell = new Rectangle(display.getX(), display.getY(),difference, display.getHeight());
            //segments.add(setUpRect(newSegment));

            root.getChildren().add(setUpRect(newCell, 0));

            Text recCount = new Text(newCell.getX()+ newCell.getWidth()/2, newCell.getY() + newCell.getHeight()/2, Double.toString(recursionStart) );
            //recursionCountText.add(recCount);
            recCount.toFront();
            root.getChildren().add(recCount);
            System.out.println("Added mid cell: " + newCell);

            ArrayList<Rectangle> newDisplayRects =  getCutOffRect(display, intersectionRect);
            for(int i = 0; i < newDisplayRects.size(); i++){
                getSegments(blocks, newDisplayRects.get(i));
            }
        }
        else{
            Rectangle edgeCell = new Rectangle(display.getX(), display.getY(), display.getWidth(), display.getHeight());
            root.getChildren().add(setUpRect(edgeCell,0));
            System.out.println("Added edge cell: " + edgeCell );
        }

        System.out.println("returning");
        recursionEnd++;
        return;
        //return segments;
    }

    int getFirstIntersection(ArrayList<Rectangle> blocks, Rectangle display){
        double min = 99999;
        double currentMin;
        int minIndex = -1;
        for(int i = 0; i < blocks.size(); i++){
            //System.out.println("In loop");
            //if(blocks.get(i).getX() > display.getX() && blocks.get(i).getY() < display.getY()+display.getHeight() || blocks.get(i).getY() >)
            if(isInside(blocks.get(i),display) )
            {
                currentMin = blocks.get(i).getX() - display.getX();
                if(currentMin < min){
                    min = currentMin;
                    minIndex = i;
                }
            }
        }
        return minIndex;
    }

    ArrayList<Rectangle> getCutOffRect(Rectangle rectangle, Rectangle intersectRect){
        ArrayList<Rectangle> newRects = new ArrayList<>();

        int inset = 1;

        double startingX12 = intersectRect.getX();
        double widthDifference12 = intersectRect.getX() - rectangle.getX();
        double width12 = rectangle.getWidth() - widthDifference12;

        double startingY1 = rectangle.getY();
        double height1 = intersectRect.getY() - rectangle.getY();
        if(height1 > 0) {
            Rectangle rect1 = new Rectangle(startingX12, startingY1, width12, height1);
            if(isEmpty(rect1))
            {
                System.out.println("EMPTY RECT1");
            }else
                newRects.add(setUpRect(rect1, inset));

        }

        double startingY2 = startingY1 + height1 + intersectRect.getHeight();
        if(startingY2 >= (intersectRect.getY() + intersectRect.getHeight())) {
            double height2 = startingY1 + rectangle.getHeight() - startingY2 ;
            Rectangle rect2 = new Rectangle(startingX12, startingY2, width12, height2);
            if(isEmpty(rect2))
            {
                System.out.println("EMPTY RECT2");
            }else
                newRects.add(setUpRect(rect2, inset));
        }

        double startingX3 = intersectRect.getX() + intersectRect.getWidth();
        double startingY3 = startingY1;
        double widthDifference3 = widthDifference12;
        double width3 = rectangle.getWidth() - widthDifference3 - intersectRect.getWidth();
        if(width3>0) {
            double height3 = rectangle.getHeight();
            Rectangle rect3 = new Rectangle(startingX3, startingY3, width3, height3);
            if (isEmpty(rect3)) {
                System.out.println("EMPTY RECT3");
            }
            else
                newRects.add(setUpRect(rect3, inset));
        }

        return newRects;
    }

    boolean isInside(Rectangle block, Rectangle display){

        Point2D points[] = new Point2D[2];
        points[0] = new Point2D(block.getX(), block.getY());
        //points[1] = new Point2D(rect.getX()+rect.getWidth(), rect.getY());
        //points[2] = new Point2D(rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight());
        points[1] = new Point2D(block.getX(), block.getY()+block.getHeight());

        Line line = new Line(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY());

        for(double i = line.getStartY(); i < line.getEndY(); i++ ){
            Point2D point = new Point2D(line.getStartX(), i);
            if(isPointColliding(point,display) )
                return true;
        }

        /*
        if(block.intersects(display.getBoundsInLocal()) ) {
            System.out.println("Block in Display");
            return true;
        }*/

        return false;
    }

    boolean isEmpty(Rectangle rect){
        if(rect.getHeight() <= 0) {
            System.out.println("Height <=0");
            return true;
        }
        else if(rect.getWidth() <= 0) {
            System.out.println("Width <=0");
            return true;
        }
        return false;
    }

    boolean isPointColliding(Point2D point, Rectangle rect) {
        if (point.getX() > rect.getX() && point.getX() <= rect.getX() + rect.getWidth()){
            if (point.getY() > rect.getY() && point.getY() <= rect.getY() + rect.getHeight()) {
                System.out.println("Point: (" + point.getX() + ", " + point.getY() + ") intersected with " + rect);
                return true;
            }
        }
        return false;
    }


    Rectangle setUpRect(Rectangle rect, int inset){
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.RED);
        rect.setStrokeType(StrokeType.INSIDE);
        rect.setX(rect.getX() + inset);
        rect.setY(rect.getY() + inset);
        rect.setWidth(rect.getWidth() - 2 * inset);
        rect.setHeight(rect.getHeight() - 2 * inset);
        rect.toFront();
        return rect;
    }
	
	void drawPath() {
		int id = graph.previous.get(endID).id;
		Node currentNode = endNode;
		Node prevNode = null;
		while (id != startID) {
			id = graph.previous.get(id).id;
			prevNode = graph.nodes.get(id);
			root.getChildren().add(new Line(currentNode.position.getX(), currentNode.position.getY(), prevNode.position.getX(), prevNode.position.getY()));
			currentNode = prevNode;			
		}
	}
	
    void drawlines(){
        //check top
        for (Rectangle rect : rectList) {
            System.out.println(rect.getX() + " " + rect.getY());
            Line l1 = new Line();
            l1.setStartX(rect.getX());
            l1.setStartY(rect.getY());
            l1.setEndX(rect.getX());
            l1.setEndY(display.getY());

            Line l2 = new Line();
            l2.setStartX(rect.getX());
            l2.setStartY(rect.getY() + rect.getHeight());
            l2.setEndX(rect.getX());
            l2.setEndY(display.getY() + display.getHeight());

            Line l3 = new Line();
            l3.setStartX(rect.getX() + rect.getWidth());
            l3.setStartY(rect.getY());
            l3.setEndX(rect.getX() + rect.getWidth());
            l3.setEndY(display.getY());
            
            Line l4 = new Line();
            l4.setStartX(rect.getX() + rect.getWidth());
            l4.setStartY(rect.getY()+ rect.getHeight());
            l4.setEndX(rect.getX() + rect.getWidth());
            l4.setEndY(display.getY()+ display.getHeight());

            for(Rectangle rect2 : rectList){
                if(rect2.getX() < l1.getStartX() && (rect2.getX() + rect2.getWidth()) > l1.getStartX()){
                    if(rect2.getY() + rect2.getHeight() < l1.getStartY()){
                        l1.setEndY(rect2.getY() + rect2.getHeight());
                    }
                }

                if(rect2.getX() < l2.getStartX() && (rect2.getX() + rect2.getWidth()) > l2.getStartX()){
                    if(rect2.getY() > l2.getStartY()){
                        l2.setEndY(rect2.getY());
                    }
                }

                
                if(rect2.getX() < l3.getStartX() && (rect2.getX() + rect2.getWidth()) > l3.getStartX()){
                    if(rect2.getY() + rect2.getHeight() < l3.getStartY()){
                        l3.setEndY(rect2.getY() + rect2.getHeight());
                    }
                }

                if(rect2.getX() < l4.getStartX() && (rect2.getX() + rect2.getWidth()) > l4.getStartX()){
                    if(rect2.getY() > l4.getStartY()){
                        l4.setEndY(rect2.getY());
                    }
                }

            }
            if(!checkIfNull(midpoint(l1) ) ) {
                linesList.add(l1);
                midpoints.add(midpoint(l1));
            }
            if(!checkIfNull(midpoint(l2) ) ) {
                linesList.add(l2);
                midpoints.add(midpoint(l2));
            }
            if(!checkIfNull(midpoint(l4) ) ) {
                linesList.add(l3);
                midpoints.add(midpoint(l3));
            }
            if(!checkIfNull(midpoint(l4) ) ) {
                linesList.add(l4);
                midpoints.add(midpoint(l4));
            }

            /*
            linesList.add(l2);
            linesList.add(l3);
            linesList.add(l4);
            midpoints.add(midpoint(l2));
            midpoints.add(midpoint(l3));
            midpoints.add(midpoint(l4));
            */
            root.getChildren().add(l1);
            root.getChildren().add(l2);
            root.getChildren().add(l3);
            root.getChildren().add(l4);
            // Line l2 = new Line();
            
            // l2.setStartX(rect.getX()+ rect.getWidth());
            // l2.setStartY(rect.getY());
            // l2.setEndX(rect.getX() + rect.getWidth());
            // l2.setEndY(display.getY());

        }
    }

    void drawMidpoints() {
        for (Point2D point: midpoints) {
            root.getChildren().add(new Circle(point.getX(), point.getY(), 5, Color.RED));
        }
    }

    void collectNodes() {
        nodes = new ArrayList<>();
        int id = 0;
        for (Point2D point : midpoints) {
            nodes.add(new Node(id, point));
            id++;
        }
		startID = id;
		startNode = new Node(id, new Point2D(startPoint.getCenterX(), startPoint.getCenterY()));
		nodes.add(startNode);
		id++;
		endID = id;
		endNode = new Node(id, new Point2D(endPoint.getCenterX(), endPoint.getCenterY()));
		nodes.add(endNode);
		
    }

	void addNodesToGraph() {
		graph.addNodes(nodes);
	}
	
    void pruneNodes() {
        for (Node node : nodes) {
            List<Node> neighbors = new ArrayList<>();
            double min = 580;
            for (Node prospectiveNode : nodes) {
                boolean collision = false;
                for (Rectangle rect : rectList) {
					// if node is below obstacle and prospNode is above obstacle
					// OR if node is above obstacle and prospNode is below obstacle
                    /*
                    if ((node.getPosition().getY() > rect.getY()+rect.getHeight() && prospectiveNode.getPosition().getY() < rect.getY())
                            || (node.getPosition().getY() < rect.getY() && prospectiveNode.getPosition().getY() > rect.getY()+rect.getHeight())) {
                        collision = true;
                    }

                    if ((node.getPosition().getX() >= rect.getX()+rect.getWidth() && prospectiveNode.getPosition().getX() < rect.getX())
                            || (node.getPosition().getX() < rect.getX() && prospectiveNode.getPosition().getX() > rect.getX()+rect.getWidth())) {
                        collision = true;
                    }
                    */

                    Line2D line = new Line2D.Float((float)node.getPosition().getX(), (float)node.getPosition().getY(), (float)prospectiveNode.getPosition().getX(), (float)prospectiveNode.getPosition().getY());
                    Rectangle2D rect2D = new java.awt.geom.Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                    if(line.intersects(rect2D))
                    {
                        collision = true;
                    }

                }

                if(!collision){
                    neighbors.add(prospectiveNode);
                }

                /*
                if (!collision && (prospectiveNode.position.getX() == min))
                    neighbors.add(prospectiveNode);
                else if (!collision && (prospectiveNode.position.getX() > node.position.getX()) && prospectiveNode.position.getX() < min) {
                    neighbors.clear();
                    neighbors.add(prospectiveNode);
                    min = prospectiveNode.position.getX();
                }
                */
            }
            for (Node neighborNode : neighbors) {
                node.addNeighbor(neighborNode, getDistance(node.position, neighborNode.position)); //TODO find disrance
            }
        }
    }

	int getDistance(Point2D one, Point2D two) {
		double result = Math.sqrt(Math.pow(one.getX()-two.getX(), 2) + Math.pow(one.getY()-two.getY(), 2));
		return (int)result;
	}
	
    void drawEdges() {
        for (Node node : nodes) {
            for (Node neighbor : node.neighbors) {
                /*
                if ((Double.compare(neighbor.position.getX(), 520) == 0) || (Double.compare(neighbor.position.getX(), 20) == 0) || (Double.compare(neighbor.position.getY(), 580) == 0) || (Double.compare(neighbor.position.getY(), 80) == 0)){
                    root.getChildren().clear();
        
                    //No path declaration
                    Label nopath = new Label("NO PATH\n AVAILABLE");
                    nopath.setStyle("-fx-font: 60 Arial; -fx-text-fill: white;");
                    nopath.setLayoutY(200);
                    nopath.setLayoutX(50);
                    root.getChildren().add(nopath);
                    
                    return;
                }
                else{
                    */
                    root.getChildren().add(new Line(node.position.getX(), node.position.getY(), neighbor.position.getX(), neighbor.position.getY()));
            }
        }
    }

    boolean isBounded(Point2D point){
        for(Rectangle rect: rectList){

            //bottom intersection

            //top intersection

            //right intersection

            //left intersection
        }
        return false;
    }



     Point2D midpoint(Line line){
        double x = (line.getStartX() + line.getEndX()) / 2;
        double y = (line.getStartY() + line.getEndY()) / 2;

        Point2D returnPoint =  new Point2D(x, y);
        if(checkIfLegal(returnPoint)){
            return returnPoint;
        }
        else
            return null;
    }

    boolean checkIfLegal(Point2D point){
        if(point.getX() <= 20 || point.getX() >= 520 || point.getY() <= 80 || point.getY() >= 580) {
            return false;
        }

        for(Rectangle rect: rectList){
            if(point.getX() == rect.getX() || point.getX() == rect.getX() + rect.getWidth()){
                if(checkBounds(point.getY(), rect.getY(), rect.getY()+ rect.getWidth()) ){
                    return false;
                }
            }
            else if(point.getY() == rect.getY() || point.getY() == rect.getY() + rect.getHeight()) {
                if (checkBounds(point.getY(), rect.getY(), rect.getY() + rect.getWidth())) {
                    return false;
                }
            }
        }

        return true;
    }

    boolean checkIfNull(Point2D point){
        if(point == null)
            return true;
        else
            return false;
    }

    boolean checkBounds( double a, double b, double c){
        if(a >=b && a <=c){
            return true;
        }
        else
            return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
