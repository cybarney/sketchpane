
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
//import sun.jvm.hotspot.ObjectHistogram;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
public class SketchPane extends BorderPane {
	
	private ArrayList<Shape> shapeList;
	private ArrayList<Shape> tempList;
	private Button undoButton;
	private Button eraseButton;
	private Label fillColorLabel;
	private Label strokeColorLabel;
	private Label strokeWidthLabel;
	private ComboBox<String> fillColorCombo;
	private ComboBox<String> strokeWidthCombo;
	private ComboBox<String> strokeColorCombo;
	private RadioButton radioButtonLine;
	private RadioButton radioButtonRectangle;
	private RadioButton radioButtonCircle;
	private Pane sketchCanvas;
	private Color[] colors;
	private String[] strokeWidth;
	private String[] colorLabels;
	private Color currentStrokeColor;
	private Color currentFillColor;
	private int currentStrokeWidth;
	private Line line;
	private Circle circle;
	private Rectangle rectangle;
	private double x1;
	private double y1;
	
	public SketchPane() {
		x1=0;
		y1=0;
		// Colors, labels, and stroke widths that are available to the user
		colors = new Color[] {Color.BLACK, Color.GREY, Color.YELLOW, Color.GOLD, Color.ORANGE, Color.DARKRED, Color.PURPLE, Color.HOTPINK, Color.TEAL, Color.DEEPSKYBLUE, Color.LIME} ;
        colorLabels = new String[] {"black", "grey", "yellow", "gold", "orange", "dark red", "purple", "hot pink", "teal", "deep sky blue", "lime"};
        fillColorLabel = new Label("Fill Color:");
        strokeColorLabel = new Label("Stroke Color:");
        strokeWidthLabel = new Label("Stroke Width:");
        strokeWidth = new String[] {"1", "3", "5", "7", "9", "11", "13"};    
        shapeList = new ArrayList<Shape>();
        tempList = new ArrayList<Shape>();
        fillColorCombo = new ComboBox<String>();
        strokeWidthCombo = new ComboBox<String>();
        strokeColorCombo = new ComboBox<String>();
        //Implements radio buttons along with putting them in a toggle group.
        
        ToggleGroup toggle = new ToggleGroup();
        	radioButtonLine = new RadioButton("Line");	
       		radioButtonLine.setToggleGroup(toggle);
        	radioButtonRectangle = new RadioButton("Rectangle");
        	radioButtonRectangle.setToggleGroup(toggle);
        	radioButtonCircle = new RadioButton("Circle");
        	radioButtonCircle.setToggleGroup(toggle);
        	radioButtonLine.setSelected(true);//Sets line radio button as default
       
        //Implements undo and erase button along with their handlers.
        undoButton = new Button("Undo");
        undoButton.setOnAction(new ButtonHandler());
        eraseButton = new Button("Erase");
       	eraseButton.setOnAction(new ButtonHandler());
        
        //Adds color choices and stroke width to combo boxes, and sets default variables.
        	strokeWidthCombo.getItems().addAll(strokeWidth);
        	strokeWidthCombo.setValue("1");
        	fillColorCombo.getItems().addAll(colorLabels);
        	fillColorCombo.setValue("black");
        	strokeColorCombo.getItems().addAll(colorLabels);
        	strokeColorCombo.setValue("black");
        	currentStrokeColor=Color.BLACK;
        	currentFillColor=Color.WHITE;
        	currentStrokeWidth=1;
        //Binds comboBoxes with handlers
        	strokeWidthCombo.setOnAction(new WidthHandler());
        	strokeColorCombo.setOnAction(new ColorHandler());
        	fillColorCombo.setOnAction(new ColorHandler());
        //Initializes sketchCanvas
        	sketchCanvas = new Pane();
        	sketchCanvas.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        	
        //Instantiates HBox for comboBoxes
        	HBox comboBoxes = new HBox(20);
        	comboBoxes.setMinSize(20,40);
        	comboBoxes.setAlignment(Pos.CENTER);
        	//Change background color to lightGrey
        	comboBoxes.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null)));
        	comboBoxes.getChildren().addAll(fillColorLabel, fillColorCombo, strokeWidthLabel, strokeWidthCombo, strokeColorLabel, strokeColorCombo);
        
        //Instantiate HBox to hold radio buttons and buttons.
        	HBox buttonHolder = new HBox(20);
        	buttonHolder.setMinSize(20, 40);
        	buttonHolder.setAlignment(Pos.CENTER);
        	buttonHolder.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null)));
        	buttonHolder.getChildren().addAll(radioButtonLine, radioButtonRectangle, radioButtonCircle, undoButton,eraseButton);
       //Add sketch canvas and the two HBoxes to border pane.
//        	this.getChildren().addAll(comboBoxes, sketchCanvas, buttonHolder);
        	this.setCenter(sketchCanvas);
        	this.setTop(comboBoxes);
        	this.setBottom(buttonHolder);
        	sketchCanvas.setOnMousePressed(new MouseHandler());
        	sketchCanvas.setOnMouseDragged(new MouseHandler());
        	sketchCanvas.setOnMouseReleased(new MouseHandler());
        	
	}
	

	private class MouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			
			
			//Line mouse handler, creates line on sketchCanvas
			if(radioButtonLine.isSelected()) {
				//Mouse is pressed
				if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1=event.getX();
					y1=event.getY();
					line = new Line(x1,y1,x1,y1);
					/*line.setStartX(x1);
					line.setStartY(y1);*/
					shapeList.add(line);
					line.setStroke(Color.BLACK);
					line.setStrokeWidth(1);
					sketchCanvas.getChildren().add(line);
				}
				else if(event.getEventType()==MouseEvent.MOUSE_DRAGGED) {//If mouse dragged
					x1=event.getX();
					y1=event.getY();
					line.setEndX(x1);
					line.setEndY(y1);
				}
				else if(event.getEventType()==MouseEvent.MOUSE_RELEASED) {//If mouse released
					
					line.setStroke(currentStrokeColor);
					line.setStrokeWidth(currentStrokeWidth);
					//sketchCanvas.getChildren().add(line);
				}
			}
			
			
			
			
			
			//Circle mouse handler, creates circle on sketchCanvas.
			if(radioButtonCircle.isSelected())	{
				//Mouse if pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1=event.getX();
					y1=event.getY();
					circle = new Circle();
					circle.setCenterX(x1);
					circle.setCenterY(y1);
					shapeList.add(circle);
					circle.setFill(Color.WHITE);
					circle.setStroke(Color.BLACK);
					sketchCanvas.getChildren().add(circle);
				}
				//Mouse if dragged
				else if(event.getEventType()== MouseEvent.MOUSE_DRAGGED) {
					circle.setRadius(getDistance(x1,y1,event.getX(),event.getY()));	//Finds distance and set's it as radisu
				}
				//Mouse is released
				else if(event.getEventType()==MouseEvent.MOUSE_RELEASED) {
					circle.setFill(currentFillColor);
					circle.setStroke(currentStrokeColor);
					circle.setStrokeWidth(currentStrokeWidth);
				}
			}
			
			// Rectange Example given!
			if (radioButtonRectangle.isSelected()) {
				//Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1 = event.getX();
					y1 = event.getY();
					rectangle = new Rectangle();
					rectangle.setX(x1);
					rectangle.setY(y1);
					shapeList.add(rectangle);
					rectangle.setFill(Color.WHITE);
					rectangle.setStroke(Color.BLACK);	
					sketchCanvas.getChildren().addAll(rectangle);
				}
				//Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					if (event.getX() - x1 <0) 
						rectangle.setX(event.getX());
					if (event.getY() - y1 <0) 
						rectangle.setY(event.getY());
					rectangle.setWidth(Math.abs(event.getX() - x1));
					rectangle.setHeight(Math.abs(event.getY() - y1));

				}
				//Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					rectangle.setFill(currentFillColor);
					rectangle.setStroke(currentStrokeColor);
					rectangle.setStrokeWidth(currentStrokeWidth);
				}
			}
		}
	}
		
	private class ButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// TASK 4: Implement the button handler
			if (event.getSource()==undoButton&&shapeList.size()!=0) {
				sketchCanvas.getChildren().remove(shapeList.get(shapeList.size()-1));
				shapeList.remove(shapeList.size()-1);
				
				
			}
				else if(event.getSource()==undoButton&&shapeList.size()==0) {//If undoButton is pressed after erase can get the shapes back.
					shapeList.addAll(tempList);
					tempList.clear();
					sketchCanvas.getChildren().addAll(shapeList);
				}
				else if(event.getSource()==eraseButton&&shapeList.size()!=0){//If erase button is pressed clears canvas, and adds all shapes to the templist.
					tempList.clear();
					tempList.addAll(shapeList);
					shapeList.clear();
					sketchCanvas.getChildren().clear();
				}
			
		}
	}

	private class ColorHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// TASK 5: Implement the color handler
		//Gets the stroke color, and te fill color.
		currentStrokeColor=colors[strokeColorCombo.getSelectionModel().getSelectedIndex()];
		currentFillColor=colors[fillColorCombo.getSelectionModel().getSelectedIndex()];
		}
	}
	
	private class WidthHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event){
			// TASK 6: Implement the stroke width handler
		//Gets stroke width
		currentStrokeWidth=Integer.parseInt(strokeWidth[strokeWidthCombo.getSelectionModel().getSelectedIndex()]);

		}
	}
	
		
	// Get the Euclidean distance between (x1,y1) and (x2,y2)
    private double getDistance(double x1, double y1, double x2, double y2)  {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}