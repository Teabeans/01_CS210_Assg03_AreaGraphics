// CS210 Area drawing and comparison device
// Timothy / Lum
// Student ID: 950 682 660
// 2017.04.23 - Spring quarter
//
// Program which creates takes user inputs to generate a comparison between two objects.
//
// This software is published under the GNU general public license
// https://www.gnu.org/licenses/gpl.txt
// https://en.wikipedia.org/wiki/GNU_General_Public_License

import java.awt.*; // Abstract Window Toolkit, for graphics tools
import java.util.*; // For random and scanner class

public class AreaGraphics {

// Set window dimensions here
	static final int PANEL_WIDTH = 1200;
	static final int PANEL_HEIGHT = 600;

// Setting a constant for pi, but as a float (not a double)
	static final float floatPI = (float) Math.PI;

// Scanner entity defined here
	static Scanner consoleInput = new Scanner(System.in);
	
// Set background RGB values here (0-255 inclusive corresponds to 0 to 100% respectively. Exceeding 255 will throw an exception)
	static int bgRed = 35;
	static int bgGreen = 35;
	static int bgBlue = 35;
// This defines the color variable "backgroundColor" based on the above inputs
	static Color backgroundColor = new Color (bgRed, bgGreen, bgBlue);

// Set the major and minor axes intervals here. Sizes are in pixels.
	static int axesMajorInterval = 100;
	static int axesMinorInterval = 10;
	
// Set axes RGB values here (0-255 inclusive corresponds to 0 to 100% respectively. Exceeding 255 will throw an exception)
	static int originAxesRed = 255;
	static int originAxesGreen = 255;
	static int originAxesBlue = 255;

// Set axes transparency values for origin, major, and minor subdivisions here (0 to 100 inclusive corresponds to percent opacity
// NOT percent transparency. Going out of range will throw an exception)
	static int axesOriginOpacity = 50;
	static int axesMajorOpacity = 25;
	static int axesMinorOpacity = 5;

// Calculates and defines the origin axes actual color based on background color, line color, and line transparency.
	static Color originAxes = new Color (
// background color moves toward foreground color by fraction of opacity
// (0% opacity = no movement towards foreground, 100% opacity means complete movement towards foreground)
		(bgRed + ((originAxesRed-bgRed)*(axesOriginOpacity))/100),
		(bgGreen + ((originAxesGreen-bgGreen)*(axesOriginOpacity))/100),
		(bgBlue + ((originAxesBlue-bgBlue)*(axesOriginOpacity))/100)
		);

// Calculates and defines the major axes subdivision actual color based on background color, line color, and line transparency.
	static Color majorAxes = new Color (
// background color moves toward foreground color by fraction of opacity
// (0% opacity = no movement towards foreground, 100% opacity means complete movement towards foreground)
		(bgRed + ((originAxesRed-bgRed)*(axesMajorOpacity))/100),
		(bgGreen + ((originAxesGreen-bgGreen)*(axesMajorOpacity))/100),
		(bgBlue + ((originAxesBlue-bgBlue)*(axesMajorOpacity))/100)
		);

// Calculates and defines the minor axes actual color based on background color, line color, and line transparency.
	static Color minorAxes = new Color (
// background color moves toward foreground color by fraction of opacity
// (0% opacity = no movement towards foreground, 100% opacity means complete movement towards foreground)
		(bgRed + ((originAxesRed-bgRed)*(axesMinorOpacity))/100),
		(bgGreen + ((originAxesGreen-bgGreen)*(axesMinorOpacity))/100),
		(bgBlue + ((originAxesBlue-bgBlue)*(axesMinorOpacity))/100)
		);

	static int scratchpad1;
	static int scratchpad2;
	static float scratchpadArea;
	
	static String firstShape = "Foo Bar Baz";
	static int firstShapeDimension1 = 0;
	static int firstShapeDimension2 = 0;
	static float firstShapeArea = 0;
	
	static String secondShape = "Foo Bar Baz";
	static int secondShapeDimension1 = 0;
	static int secondShapeDimension2 = 0;
	static float secondShapeArea = 0;

	public static void main(String[] args) {

// Asks the first shape question and assigns only the valid response to the firstShape variable.
		positFirstQuestion();
		userSpecifyParameters(firstShape);
		firstShapeDimension1 = scratchpad1;
		firstShapeDimension2 = scratchpad2;
		firstShapeArea = scratchpadArea;

// Reports the state of the firstShape and firstShape area variable.
		System.out.printf("The area of the " + firstShape + " is (%.3f) square units. \n\n", firstShapeArea);

// Posit the second shape question - the second question function must receive the first choice, else it won't know what the user selected.		
		positSecondQuestion();
		userSpecifyParameters(secondShape);
		secondShapeDimension1 = scratchpad1;
		secondShapeDimension2 = scratchpad2;
		secondShapeArea = scratchpadArea;
		
// Reports the state of the secondShape and secondShape area variable.
		System.out.printf("The area of the " + secondShape + " is (%.3f) square units. \n\n", secondShapeArea);

// Establishing the drawing canvas		
// Creates a new drawing panel (see Building Java Program, page 197) using the inputs above. 
		DrawingPanel applicationWindow = new DrawingPanel(PANEL_WIDTH, PANEL_HEIGHT);
		Graphics g = applicationWindow.getGraphics();

// Set window background color based on above values
		applicationWindow.setBackground(backgroundColor);

// Draw the panel axes.
		drawAxes(g);

// Draw the sheet formatting.
		drawFormatting(g);
		
// Method that draws all the relevant data
		compareAreas(g);
	}
	
	static void drawFormatting(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawLine(PANEL_WIDTH/2-1, 0, PANEL_WIDTH/2-1, 500);
		g.drawLine(PANEL_WIDTH/2+1, 0, PANEL_WIDTH/2+1, 500);
		g.drawLine(0,  497,  PANEL_WIDTH,  498);
		g.drawLine(0,  500,  PANEL_WIDTH,  500);
	}
	
	static void drawAxes(Graphics g) {
// Draw X axes and associated horizontal gridlines
		drawXAxis(g);
// Draw Y axes and associated vertical gridlines
		drawYAxis(g);
// Draw origin
		g.setColor(originAxes);
		g.setFont(new Font("Serif", Font.PLAIN, 20));
		g.drawString("(0,0)", 10, 20);
		drawCircleCentered(g, 0, 0, 5);
	}
	
// Function to draw x axis and associated horizontal gridlines
	static void drawXAxis(Graphics g) {
// Draw major horizontal lines using the majorAxes color
		for (int i = axesMajorInterval ; i <= PANEL_HEIGHT+axesMajorInterval ; i = i + axesMajorInterval) {
			g.setColor(majorAxes);
			drawHorizontalLine(g, i);
// Draws the axes labels along the Y axis.
			g.setColor(originAxes);
			g.setFont(new Font("Serif", Font.PLAIN, 15));
			g.drawString(""+i, 0, i);
			
// Draw minor horizontal lines using the minorAxes color
			for (int j = i - axesMajorInterval + axesMinorInterval ; j < i ; j = j + axesMinorInterval) {
				g.setColor(minorAxes);
				drawHorizontalLine(g, j);
			}
		}
// Draw the origin line at height = 0
		g.setColor(originAxes);
		drawHorizontalLine(g, 0);
	}

// Function to draw y axis and associated horizontal gridlines	
	static void drawYAxis(Graphics g) {
// Draw major vertical lines using the majorAxes color
		for (int i = 0 + axesMajorInterval ; i < PANEL_WIDTH+axesMajorInterval ; i = i + axesMajorInterval) {
			g.setColor(majorAxes);
			drawVerticalLine(g, i);
// Draw the axes labels for the Y axis.
			g.setColor(originAxes);
			g.setFont(new Font("Serif", Font.PLAIN, 15));
			g.drawString(""+i, i, 15);
					
// Draw minor vertical lines using the minorAxes color
			for (int j = i - axesMajorInterval + axesMinorInterval ; j < i ; j = j + axesMinorInterval) {
				g.setColor(minorAxes);
				drawVerticalLine(g, j);
			}
		}
// Draw the origin line at height = 0
		g.setColor(originAxes);
		drawVerticalLine(g, 0);
	}

// Function that draws a horizontal line from one end of the canvas to the other.
	static void drawHorizontalLine(Graphics g, int yCoord) {
		g.drawLine(0, yCoord, PANEL_WIDTH, yCoord);
	}

// Function that draws a vertical line from one end of the canvas to the other.
	static void drawVerticalLine(Graphics g, int xCoord) {
		g.drawLine(xCoord, 0, xCoord, PANEL_HEIGHT);
	}
	
// Function that converts Java graphic x coordinates to Cartesian coordinates, centered on the drawing space.
	static int cartCoordX (int coordinate) {
		return (PANEL_WIDTH/2 + coordinate);
	}

// Function that converts Java graphic y coordinates to Cartesian coordinates, centered on the drawing space.
	static int cartCoordY (int coordinate) {
		return ((PANEL_HEIGHT/2) - coordinate);
	}

// Function to draw circles from a centerpoint and radius
	static void drawCircleCentered(Graphics g, int centerX, int centerY, int radius) {
		g.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
	}
	
// Asks first shape question, also passes back the result of the user response method.
	static void positFirstQuestion() { 
		System.out.print("Please choose your first shape (rectangle, triangle, or circle): ");
// Method that filters user inputs and returns a valid response (rectangle, triangle, circle). The response is immediately returned to main().
		String userSelection = "Foo Bar Baz";
// While loop that will continue while the firstShape variable is not equal to "rectangle", "triangle", or "circle"
		while (userSelection != "rectangle" && userSelection != "triangle" && userSelection != "circle") {
// Scans the next input as a string and assigns it to variable userSelection
			userSelection = consoleInput.next();
// If the user types "rectangle", "triangle", "circle", or any permutation therein, pass the input to lowercasing().
// The returned result is a clean user input for the shape. Pass this value back to main().
			if (userSelection.equalsIgnoreCase("rectangle") || userSelection.equalsIgnoreCase("triangle") || userSelection.equalsIgnoreCase("circle")) {
				firstShape = lowercasing(userSelection);
				userSelection = firstShape;
			}
// All other user inputs would be invalid, so respond as such.
// Return to the top of the loop after responding.	
			else {
				System.out.print("I don't understand what shape that is. Please choose a shape: ");
			}
		}
	}


// Asks second shape question, both passes back the result of the user response method and passes forward the first shape input.
	static void positSecondQuestion() {
		System.out.print("Please choose a second shape unlike the first: ");
// Method that filters user inputs and returns a valid response (rectangle, triangle, circle, minus the first shape which is passed to it). The result is immediately returned to main().
		String userSelection = "Foo Bar Baz";
// While loop that will continue while the secondShape variable is not equal to "rectangle", "triangle", or "circle"
		while (userSelection != "rectangle" && userSelection != "triangle" && userSelection != "circle") {
// Scans the next input as a string and assigns it to variable userInput			
			userSelection = consoleInput.next(); 
// If the user types the same thing as the previous selection, they will be prompted as such and the while loop starts again.
			if (userSelection.equalsIgnoreCase(firstShape)) {
				System.out.print("You already selected " + firstShape + ". Please try again: ");
			}
// If the user types "rectangle", "triangle", "circle", or any permutation therein, pass the input to lowercasing().
// The returned result is a clean user input for the shape. Pass this value back to main().
			else if (userSelection.equalsIgnoreCase("rectangle") || userSelection.equalsIgnoreCase("triangle") || userSelection.equalsIgnoreCase("circle")) {
				secondShape = lowercasing(userSelection);
				userSelection = secondShape;
			}
// All other user inputs would be invalid, so respond as such.
// Return to the top of the while loop after responding.	
			else {
				System.out.print("I don't understand what shape that is. Please choose a shape: ");
			}
		}
	}

		
// Method that parses valid inputs from ChooseShape2(). This method both selects which shape the user picked and
// converts untidy user input to clean values. Passes the cleaned value back to ChooseShape2() (which passes it to main())
	static String lowercasing(String goodInput) {
		if (goodInput.equalsIgnoreCase("rectangle")) {
			return("rectangle");
		}
		else if (goodInput.equalsIgnoreCase("triangle")) {
			return("triangle");
		}
		else {
			return("circle");
		}
	}


// This method controls the selection of the correct parameter prompts.
	static void userSpecifyParameters(String objectName) { 
// If the method is passed the word "rectangle", it will activate the "rectangleCase()" method, which prompts for rectangle dimensions
// and returns the area. This value (the area), is immediately sent back to main().
		if (objectName.equalsIgnoreCase("Rectangle")) {			

// Assign console input to local shapeWidth variable
			System.out.print("Specify rectangle width (500 max.):  ");
			scratchpad1 = consoleInput.nextInt();

// Assign console input to local shapeHeight variable			
			System.out.print("Specify rectangle height (400 max.): ");
			scratchpad2 = consoleInput.nextInt();
					
// Calculate the area
			scratchpadArea = scratchpad1 * scratchpad2;
		}
	
// If the method is passed the word "triangle", it will activate the "triangleCase()" method, which prompts for triangle dimensions
// and returns the area. This value (the area), is immediately sent back to main().		
		else if (objectName.equalsIgnoreCase("Triangle")) {
			System.out.print("Specify triangle base (500 max.):    ");
// Assign console input to local shapeBase variable
			scratchpad1 = consoleInput.nextInt();

// Assign console input to local shapeHeight variable
			System.out.print("Specify triangle height (400 max.):  ");
			scratchpad2 = consoleInput.nextInt();

// Calculate the area as (Base * Height / 2)
			scratchpadArea = (scratchpad1 * scratchpad2)/2;
		}
			
// Otherwise, this function assumes it has received "circle", and will activate the"circleCase()" method, which prompts for circle radius
// and returns the area. This value (the area), is immediately sent back to main().	 
		else {
			System.out.print("Specify circle radius (275 max.):    ");
// Assign console input to local shapeRadius variable
			scratchpad1 = consoleInput.nextInt();

// Calculate the area as the constant pi times the radius times the radius. Return the value to userSpecifyParameters().
			scratchpadArea = (floatPI * (scratchpad1 * scratchpad1));
		}
	}



// Writes the comparison statement between the two shapes and their areas. Accepts the two shapes and their respective areas as parameters.	
	static void compareAreas(Graphics g) {
		String shape1Statement = "The area of the " + firstShape + " (" + firstShapeArea + " square units) is ";

// Determines the relationship of the first shape's area to the second shape's area.
		String comparison;
		if (firstShapeArea > secondShapeArea) {
			comparison = "greater than";
		}
		else if (firstShapeArea < secondShapeArea) {
			comparison = "less than";
		}
// If the first shape's area is neither greater than nor less than the second shape's area, it must be equal to it.
		else {
			comparison = "equal to";
		}

		String shape2Statement;
		shape2Statement = (" the area of the " + secondShape + " (" + secondShapeArea + " square units).");
		
		String summaryStatement;
		summaryStatement = shape1Statement+comparison+shape2Statement;

		if (firstShapeArea > secondShapeArea) {
			g.setColor(Color.GREEN);
			drawFirstShape(g);
			g.setColor(Color.RED);
			drawSecondShape(g);
		}
		else if (firstShapeArea < secondShapeArea){
			g.setColor(Color.RED);
			drawFirstShape(g);
			g.setColor(Color.GREEN);
			drawSecondShape(g);
		}
		else {
			g.setColor(Color.ORANGE);
			drawFirstShape(g);
			drawSecondShape(g);
		}
		
		
		
		

// Writes the summary statement.
		g.setColor(Color.WHITE);		
		g.setFont(new Font("Serif", Font.PLAIN, 20));
		g.drawString(summaryStatement, 50, 560);
	}
	
	static void drawFirstShape(Graphics g) {
		if (firstShape.equalsIgnoreCase("Rectangle")) {	
			g.drawRect(300-(firstShapeDimension1/2), 250-(firstShapeDimension2/2), firstShapeDimension1, firstShapeDimension2);
		}
		
		else if(firstShape.equalsIgnoreCase("Triangle")) {
			drawTriangleCentered(g, firstShapeDimension1, firstShapeDimension2, 300, 250);
		}
		
		else {
			g.drawOval(300-(firstShapeDimension1), 250-(firstShapeDimension1), 2*firstShapeDimension1, 2*firstShapeDimension1);			
		}
		
		
		
		
	}
		
	static void drawSecondShape(Graphics g) {
		if (secondShape.equalsIgnoreCase("Rectangle")) {	
			g.drawRect(900-(secondShapeDimension1/2), 250-(secondShapeDimension2/2), secondShapeDimension1, secondShapeDimension2);
		}
		
		else if(secondShape.equalsIgnoreCase("Triangle")) {
			drawTriangleCentered(g, secondShapeDimension1, secondShapeDimension2, 900, 250);
		}
		
		else {
		g.drawOval(900-(secondShapeDimension1), 250-(secondShapeDimension1), 2*secondShapeDimension1, 2*secondShapeDimension1);
		}
	
	}
	
	static void drawTriangleCentered(Graphics g, int base, int height, int offsetX, int offsetY) {
		Polygon somePolygon = new Polygon();
		somePolygon.addPoint(offsetX-(base/2), offsetY+(height/2));
		somePolygon.addPoint(offsetX+(base/2), offsetY+(height/2));
		somePolygon.addPoint(offsetX, offsetY-(height/2));			
		g.drawPolygon(somePolygon);	
	}
	
}

