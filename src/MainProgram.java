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
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import java.awt.Image;
import java.math.*;

public class MainProgram extends JFrame{
	
	final int WIDTH = 1024;
	final int HEIGHT = 700;
	// Initials the canvas object
	//private MyCanvas canvas = new MyCanvas();
	
	int x, y, x_origin, y_origin, increment;
	double x_midpoint, y_midpoint;
	// the points of the polygon, a 2D array 
	int[][] polygon_points = new int[5][3];
	double[][] scale_polygon_points = new double[5][3];
	// double duffering, takes an image of the screen and redraws the image when keys are pressed
	private Image dbImage;
	private Graphics dbg;
	
	// Action listener for the key board inputs
	public class MyActionListener extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			int keyCode = e.getKeyCode();
			
			// translates the object RIGHT using the "R" key
			if(keyCode == e.VK_R){
				if(polygon_points[2][0] < x){
					for(int i = 0; i < 5; i++){
						polygon_points[i][0] += increment;
					}
				}
				x_midpoint += increment;

			}
			// translates the object LEFT using the "L" key
			if(keyCode == e.VK_L){
				if(polygon_points[1][0] > 0){
					for(int i = 0; i < 5; i++){
						polygon_points[i][0] -= increment;
					}
				}
				x_midpoint -= increment;
			}
			// translates the object UP using the "U" key
			if(keyCode == e.VK_U){
				if((polygon_points[0][1] - 20) > 0){
					for(int i = 0; i < 5; i++){
						polygon_points[i][1] -= increment;
					}
				}
				y_midpoint -= increment;
			}
			// translates the object DOWN using the "D" key
			if(keyCode == e.VK_D){
				if(polygon_points[1][1] < y || polygon_points[2][1] < y){
					for(int i = 0; i < 5; i++){
						polygon_points[i][1] += increment;
					}
				}
				y_midpoint += increment;
			}
			// translates the object FORWARD using the "F" key
			if(keyCode == e.VK_F){
				double[][] polygon_points_temps = scale_polygon_points;
				
				if(polygon_points[2][0] < x && polygon_points[1][0] > 0 && (polygon_points[0][1] - 50) > 0 && polygon_points[1][1] < y && polygon_points[2][1] < y){
					for(int i = 0; i < 5; i++){
						polygon_points_temps[i][0] *= 1.2;
						polygon_points_temps[i][1] *= 1.2;
						polygon_points[i][0] = (int)(polygon_points_temps[i][0] + x_midpoint);
						polygon_points[i][1] = (int)(polygon_points_temps[i][1] + y_midpoint);
					}
				}
			}
			// translates the object BACKWARD using the "B" key
			if(keyCode == e.VK_B){
				double[][] polygon_points_temps = scale_polygon_points;
				
				if((polygon_points[2][0] - polygon_points[1][0]) > 100){
					for(int i = 0; i < 5; i++){
						polygon_points_temps[i][0] *= 0.9;
						polygon_points_temps[i][1] *= 0.9;
						polygon_points[i][0] = (int)(polygon_points_temps[i][0] + x_midpoint);
						polygon_points[i][1] = (int)(polygon_points_temps[i][1] + y_midpoint);
					}
					
				}
			}
			// rotates the object counter clockwise using the "<" key
			if(keyCode == e.VK_COMMA){
				double theta = 25.0;
				double[][] polygon_points_temps = scale_polygon_points;
				
				if(polygon_points[2][0] < x && polygon_points[1][0] > 0 && (polygon_points[0][1] - 50) > 0 && polygon_points[1][1] < y && polygon_points[2][1] < y){
					for(int i = 0; i < 5; i++){
						polygon_points_temps[i][0] = (polygon_points_temps[i][0] * Math.cos(theta) - polygon_points_temps[i][1] * Math.sin(theta));
						polygon_points_temps[i][1] = (polygon_points_temps[i][0] * Math.sin(theta) + polygon_points_temps[i][1] * Math.cos(theta));
						polygon_points[i][0] = (int)(polygon_points_temps[i][0] + x_midpoint);
						polygon_points[i][1] = (int)(polygon_points_temps[i][1] + y_midpoint);
					}
				}
			}
			// rotates the object clockwise using the "> " key
			if(keyCode == e.VK_PERIOD){
				double theta = -25.0;
				double[][] polygon_points_temps = scale_polygon_points;
				
				if(polygon_points[2][0] < x && polygon_points[1][0] > 0 && (polygon_points[0][1] - 50) > 0 && polygon_points[1][1] < y && polygon_points[2][1] < y){
					for(int i = 0; i < 5; i++){
						polygon_points_temps[i][0] = (polygon_points_temps[i][0] * Math.cos(theta) - polygon_points_temps[i][1] * Math.sin(theta));
						polygon_points_temps[i][1] = (polygon_points_temps[i][0] * Math.sin(theta) + polygon_points_temps[i][1] * Math.cos(theta));
						polygon_points[i][0] = (int)(polygon_points_temps[i][0] + x_midpoint);
						polygon_points[i][1] = (int)(polygon_points_temps[i][1] + y_midpoint);
					}
				}
			}
				
		}
		
		public void keyReleased(KeyEvent e){
			
		}
		
	}
	
	
	// This is the Mouse Handler that will reset the pyramid to the origin
	public class MyMouseHandler extends MouseAdapter{
		public void mousePressed (MouseEvent e){
			int mouse_x = e.getX(); // retrieves the x coordinates
			int mouse_y = e.getY(); // retrieves the y coordinates
			if(mouse_x > (x - 150) && mouse_x < ((x - 150) + 100)){
				Reset();
			}		
		}
	}
	
	// main program function that declare the canvas attributes
	public MainProgram(){
		
		// Intializes the canvas size and also the origin of the canvas. 
		x = WIDTH;
		y = HEIGHT;
		x_origin = x/2; 
		y_origin = y/2;
		x_midpoint = x/2;
		y_midpoint = y/2;
		increment = 10;
		// P1
		polygon_points[0][0] = x_origin;
		polygon_points[0][1] = y_origin - 133;
		polygon_points[0][2] = 0;
					
		// P2
		polygon_points[1][0] = x_origin - 100;
		polygon_points[1][1] = y_origin + 67;
		polygon_points[1][2] = 0;
					
		// P3
		polygon_points[2][0] = x_origin + 100;
		polygon_points[2][1] = y_origin + 67;
		polygon_points[2][2] = 0;
					
		// P4
		polygon_points[3][0] = x_origin + 45;
		polygon_points[3][1] = y_origin + 30;
		polygon_points[3][2] = 0;
					
		// P5
		polygon_points[4][0] = x_origin - 45;
		polygon_points[4][1] = y_origin + 30;
		polygon_points[4][2] = 0;
		
		// calculates the scaled points
		for(int i = 0; i < 5; i++){
				scale_polygon_points[i][0] = (double)(polygon_points[i][0] - x_origin);
				scale_polygon_points[i][1] = (double)(polygon_points[i][1] - y_origin);
				//System.out.println("P" + (i+1) + "=" + scale_polygon_points[i][0] + "," + scale_polygon_points[i][1]);
		}

		
		
		addKeyListener(new MyActionListener());
		addMouseListener(new MyMouseHandler());
		setLayout(new BorderLayout());
		setSize(x, y);
		setTitle("3D Pyramid - Assignment 1");
		//add("Center", canvas);
		setBackground(new Color(45,57,95));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		
	}
	
	public void Reset(){
		dispose(); // gets rid of old JFrame window
		new MainProgram(); // restarts program
	}
	

	// the MyCanvas class extends canvas and lets us draw onto the canvas
	//private class MyCanvas extends Canvas{
	
		public void paint(Graphics g){
			dbImage = createImage(getWidth(), getHeight());
			dbg = dbImage.getGraphics();
			paintComponent(dbg);
			g.drawImage(dbImage, 0, 0, this);
			
		}

		//@Override
		public void paintComponent (Graphics g){
			
			// Draws the X and Y coordinants plane. 
			g.setColor(Color.WHITE);
			g.drawLine(x/2, 0, x/2, y);
			g.drawLine(0, y/2, x, y/2);
			
			// Draws each point P1 (x, y), P2(x, y) ... 
			for(int i = 0; i < 5; i++){
				g.setColor(Color.CYAN);
				g.drawString(("P" + (i+1)), polygon_points[i][0], polygon_points[i][1]);
				g.setColor(Color.ORANGE);
				g.drawString(("(" + polygon_points[i][0] + "," + polygon_points[i][1] + ")"), polygon_points[i][0] + 20, polygon_points[i][1]);
				//g.drawString(x_midpoint + ", " + y_midpoint, (int)x_midpoint, (int)y_midpoint);
			}
			//g.drawString(x_origin + ", " + y_origin, x_origin, y_origin);
			
			
			
			// Draws the 3D triangle
			g.setColor(Color.BLACK);
			g.drawLine(polygon_points[0][0], polygon_points[0][1], polygon_points[1][0], polygon_points[1][1]); // P1 to P2
			g.drawLine(polygon_points[2][0], polygon_points[2][1], polygon_points[0][0], polygon_points[0][1]); // P1 to P3
			g.drawLine(polygon_points[3][0], polygon_points[3][1], polygon_points[0][0], polygon_points[0][1]); // P1 to P4
			g.drawLine(polygon_points[4][0], polygon_points[4][1], polygon_points[0][0], polygon_points[0][1]); // P1 to P5
			g.setColor(Color.YELLOW);
			g.drawLine(polygon_points[1][0], polygon_points[1][1], polygon_points[4][0], polygon_points[4][1]); // P2 to P4
			g.setColor(Color.GREEN);
			g.drawLine(polygon_points[1][0], polygon_points[1][1], polygon_points[2][0], polygon_points[2][1]); // P2 to P3
			g.setColor(Color.RED);
			g.drawLine(polygon_points[3][0], polygon_points[3][1], polygon_points[4][0], polygon_points[4][1]); // P4 to P5
			g.setColor(Color.BLUE);
			g.drawLine(polygon_points[2][0], polygon_points[2][1], polygon_points[3][0], polygon_points[3][1]); // P3 to P4
			
			g.setColor(Color.WHITE);
			g.drawRect(WIDTH - 150, HEIGHT - 50, 100, 25);
			g.fillRect(WIDTH - 150, HEIGHT - 50, 100, 25);
			g.setColor(Color.BLACK);
			g.drawString("RESET", WIDTH - 120, HEIGHT - 32);
			
			//find midpoint
			//g.setColor(Color.PINK);
			//g.drawLine(polygon_points[0][0], polygon_points[0][1], (polygon_points[1][0]+polygon_points[2][0])/ 2, (polygon_points[1][1]+polygon_points[2][1])/2);
			//g.drawLine(polygon_points[1][0], polygon_points[1][1], (polygon_points[0][0]+polygon_points[2][0])/ 2, (polygon_points[0][1]+polygon_points[2][1])/2);
			//g.drawLine(polygon_points[2][0], polygon_points[2][1], (polygon_points[0][0]+polygon_points[1][0])/ 2, (polygon_points[0][1]+polygon_points[1][1])/2);
			
			repaint();
		}
	//}	
		
	// main program
	public static void main(String[] args){
		new MainProgram();
	}
}