/**
 * 	Author: 	Brock Tubre
 * 	Date: 		December 19, 2014
 * 	CWID:		102-10-73
 * 	Assignment: 1
 * 	File path: 	/Volumes/Partition512/School/CSC470/Assignment1
 * 
 * 	Description: This program projects a 3D pyramid that renders a wireframe. It allows the user to scale the obejct using SHIFT up and SHIFT down.
 * 	The program runs in Eclipse Version: Luna Service Release 1 (4.4.1). 
 */

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class MainProgram extends JFrame{

	private static final long serialVersionUID = 1L;
	
	// Initials the canvas object
	private MyCanvas canvas = new MyCanvas();
	
	// Intializes the canvas size and also the origin of the canvas. 
	int x = 1024, y = 500;
	int x_origin = x/2, y_origin = y/2;
	
	// main program
	public static void main(String[] args){
		MainProgram p = new MainProgram();
	}
	
	// main program function that declare the canvas attributes
	public MainProgram(){
		
		setLayout(new BorderLayout());
		setSize(x, y);
		setTitle("3D Pyramid - Assignment 1");
		add("Center", canvas);
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}
	

	// the MyCanvas class extends canvas and lets us draw onto the canvas
	private class MyCanvas extends Canvas{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g){
			
			// the points of the polygon, a 2D array 
			int[][] polygon_points = new int[5][3];

			// P1
			polygon_points[0][0] = x_origin;
			polygon_points[0][1] = y_origin - 100;
			polygon_points[0][2] = 0;
			
			// P2
			polygon_points[1][0] = x_origin + 100;
			polygon_points[1][1] = y_origin + 100;
			polygon_points[1][2] = 0;
			
			// P3
			polygon_points[2][0] = x_origin - 100;
			polygon_points[2][1] = y_origin + 100;
			polygon_points[2][2] = 0;
			
			// P4
			polygon_points[3][0] = x_origin + 45;
			polygon_points[3][1] = y_origin + 25;
			polygon_points[3][2] = 0;
			
			// P5
			polygon_points[4][0] = x_origin - 45;
			polygon_points[4][1] = y_origin + 25;
			polygon_points[4][2] = 0;
			
			// Draws the X and Y corrordinants. 
			g.setColor(Color.CYAN);
			g.drawLine(x/2, 0, x/2, y);
			g.drawLine(0, y/2, x, y/2);
			
			// Draws the 3D triangle
			g.setColor(Color.WHITE);
			g.drawLine(polygon_points[0][0], polygon_points[0][1], polygon_points[1][0], polygon_points[1][1]); // P1 to P2
			g.drawLine(polygon_points[2][0], polygon_points[2][1], polygon_points[0][0], polygon_points[0][1]); // P3 to P1
			g.drawLine(polygon_points[3][0], polygon_points[3][1], polygon_points[0][0], polygon_points[0][1]); // P2 to P1
			g.drawLine(polygon_points[4][0], polygon_points[4][1], polygon_points[0][0], polygon_points[0][1]); // P3 to P1
			g.setColor(Color.BLUE);
			g.drawLine(polygon_points[1][0], polygon_points[1][1], polygon_points[3][0], polygon_points[3][1]); // P5 to P3
			g.setColor(Color.GREEN);
			g.drawLine(polygon_points[1][0], polygon_points[1][1], polygon_points[2][0], polygon_points[2][1]); // P2 to P3
			g.setColor(Color.RED);
			g.drawLine(polygon_points[3][0], polygon_points[3][1], polygon_points[4][0], polygon_points[4][1]); // P4 to P5
			g.setColor(Color.YELLOW);
			g.drawLine(polygon_points[4][0], polygon_points[4][1], polygon_points[2][0], polygon_points[2][1]); // P2 to P4
		}
	}	
}