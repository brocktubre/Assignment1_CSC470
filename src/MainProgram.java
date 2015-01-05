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

public class MainProgram extends JFrame {

	// initialization of the window width and the window height
	final int WIDTH = 800;
	final int HEIGHT = 600;

	// initialization of x origin and y origin, also screen width and size
	int x, y, x_origin, y_origin, z_origin, eye_z, eye_x, eye_y, increment;
	int x_midpoint, y_midpoint, z_midpoint;
	// the points of the polygon, a 2D array
	int[][] polygon_points = new int[5][3];
	// double[][] scaled_polygon_points = new double[5][3];
	int[][] scaled_points = new int[5][3];
	int[][] screen_points = new int[5][2];
	// double duffering, takes an image of the screen and redraws the image when
	// keys are pressed
	private Image dbImage;
	private Graphics dbg;

	// Action listener for the key board inputs
	public class MyActionListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			// grabs the most recent key that is pressed
			int keyCode = e.getKeyCode();

			switch (keyCode) {
			// if "R" key is pressed
			case KeyEvent.VK_R:
				MoveRight();
				break;
			// if "L" key is pressed
			case KeyEvent.VK_L:
				MoveLeft();
				break;
			// if "U" key is pressed
			case KeyEvent.VK_U:
				MoveUp();
				break;
			// if "D" key is pressed
			case KeyEvent.VK_D:
				MoveDown();
				break;
			// if "F" key is pressed
			case KeyEvent.VK_F:
				MoveForward();
				break;
			// if "B" key is pressed
			case KeyEvent.VK_B:
				MoveBackward();
				break;
			// if "UP" key is pressed
			case KeyEvent.VK_UP:
				ScaleUp();
				break;
			// if "DOWN" key is pressed
			case KeyEvent.VK_DOWN:
				ScaleDown();
				break;
			// if "RIGHT" key is pressed
			case KeyEvent.VK_RIGHT:
				RotateYClockwise();
				break;
			// if "LEFT" key is pressed
			case KeyEvent.VK_LEFT:
				RotateYCounterClockwise();
				break;
			// if "COMMA" key is pressed
			case KeyEvent.VK_COMMA:
				RotateZCounterClockwise();
				break;
			// if "PERIOD" key is pressed
			case KeyEvent.VK_PERIOD:
				RotateZClockwise();
				break;
			default:
				break;
			}

		}

		public void keyReleased(KeyEvent e) {

		}
	}

	// This is the Mouse Handler that will reset the pyramid to the origin
	public class MyMouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int mouse_x = e.getX(); // retrieves the x coordinates
			int mouse_y = e.getY(); // retrieves the y coordinates
			if (mouse_x > (x - 150) && mouse_x < ((x - 150) + 100))
				ResetPyramid(); // resets if the mouse is clicked on the reset
								// button

		}
	}

	// constructor
	public MainProgram() {

		// Intializes the canvas size and also the origin of the canvas.
		x = WIDTH; // canvas width
		y = HEIGHT; // canvas height
		// The oiring of the canvas (400, 300, 1000)
		x_origin = x / 2; // X origin of canvas
		y_origin = y / 2; // Y origin of canvas
		z_origin = 0; // Z origin of canvas
		// The midpoint of the pyramid
		x_midpoint = x / 2;
		y_midpoint = y / 2;
		z_midpoint = 50;
		// The views eye corridance (400, 300, 800)
		eye_x = x_origin;
		eye_y = y_origin;
		eye_z = 100;
		// The scale of increment size
		increment = 10;

		// P1
		polygon_points[0][0] = x_origin; // x coordinates of P1
		polygon_points[0][1] = y_origin - 150; // y coordinates of P1
		polygon_points[0][2] = z_origin + 50; // z coordinates of P1

		// P2
		polygon_points[1][0] = x_origin - 100; // x coordinates of P2
		polygon_points[1][1] = y_origin + 75; // y coordinates of P2
		polygon_points[1][2] = z_origin + 25; // z coordinates of P2

		// P3
		polygon_points[2][0] = x_origin + 100; // x coordinates of P3
		polygon_points[2][1] = y_origin + 75; // y coordinates of P3
		polygon_points[2][2] = z_origin + 25; // z coordinates of P3

		// P4
		polygon_points[3][0] = x_origin + 45; // x coordinates of P4
		polygon_points[3][1] = y_origin + 30; // y coordinates of P4
		polygon_points[3][2] = z_origin + 75; // z coordinates of P4

		// P5
		polygon_points[4][0] = x_origin - 45; // x coordinates of P5
		polygon_points[4][1] = y_origin + 30; // y coordinates of P5
		polygon_points[4][2] = z_origin + 75; // z coordinates of P5

		for (int i = 0; i < 5; i++) {
			screen_points[i][0] = (int) (eye_z * (polygon_points[i][0] - eye_x)
					/ (eye_z + polygon_points[i][2]) + eye_x);
			screen_points[i][1] = (int) (eye_z * (polygon_points[i][1] - eye_y)
					/ (eye_z + polygon_points[i][2]) + eye_y);
		}

		// calculates the scaled points, from the origin or (0,0)
		ScalePoints();

		// adds everything to the canvas and sets its attributes
		addKeyListener(new MyActionListener());
		addMouseListener(new MyMouseHandler());
		setLayout(new BorderLayout());
		setSize(x, y);
		setTitle("3D Pyramid - Assignment 1");
		setBackground(new Color(45, 57, 95));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public void paint(Graphics g) {
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);

	}

	public void paintComponent(Graphics g) {
		// DrawPlanes(g);
		// DrawPoints(g);
		DrawPyramid(g);
		DrawResetButton(g);
		// FindMidPoint(g);

		repaint();

	}

	// translates the object RIGHT using the "R" key
	/*
	 * This simply adds increment to the all of the x values moving the object
	 * to the right
	 */
	public void MoveRight() {
		// if (polygon_points[2][0] < x) {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] += increment;
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
			}
			x_midpoint += increment;
			ScalePoints();
		}

	}

	// translates the object LEFT using the "L" key
	/* This simply decreases all of the x values moving the object to the left */
	public void MoveLeft() {
		// if (polygon_points[1][0] > 0) {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] -= increment;
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
			}
			x_midpoint -= increment;
			ScalePoints();
		}

	}

	// translates the object UP using the "U" key
	/* This simply decreases all of the y values moving the object to the up */
	public void MoveUp() {
		// if ((polygon_points[0][1] - 20) > 0) {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][1] -= increment;
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);
			}
			y_midpoint -= increment;
			ScalePoints();
		}

	}

	// translates the object DOWN using the "D" key
	/*
	 * This simply increase as all of the y values moving the object to the down
	 */
	public void MoveDown() {
		// if (polygon_points[1][1] < y || polygon_points[2][1] < y) {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][1] += increment;
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);
			}
			y_midpoint += increment;
			ScalePoints();
		}
	}

	// translates the object FORWARD using the "F" key
	/*
	 * This simply increase as all of the x and y using multiplication values
	 * moving the object foward
	 */
	public void MoveForward() {
		int[][] temps = scaled_points;

		if (true) {
			for (int i = 0; i < 5; i++) {
				temps[i][0] *= 1.5;
				temps[i][1] *= 1.5;
				polygon_points[i][0] = (int) (temps[i][0] + x_midpoint);
				polygon_points[i][1] = (int) (temps[i][1] + y_midpoint);
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);
			}
			//ScalePoints();
		}
	}

	// translates the object BACKWARDS using the "B" key
	/*
	 * This simply decrese as all of the x and y using multiplication values
	 * moving the object backwards
	 */
	public void MoveBackward() {
		int[][] temps = scaled_points;

		if (true) {
			for (int i = 0; i < 5; i++) {
				temps[i][0] *= 0.8;
				temps[i][1] *= 0.8;
				polygon_points[i][0] = (int) (temps[i][0] + x_midpoint);
				polygon_points[i][1] = (int) (temps[i][1] + y_midpoint);
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);

			}
			// ScalePoints();
		}
	}

	// scales the object Up using the up arrow key
	/*
	 * This simply increases as all of the x and y using multiplication values
	 * scaling the object up
	 */
	public void ScaleUp() {

		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] *= 1.2;
				polygon_points[i][1] *= 1.2;
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);

			}

		}
	}

	// scales the object Down using the down arrow key
	/*
	 * This simply decreases as all of the x and y using multiplication values
	 * scaling the object down
	 */
	public void ScaleDown() {

		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] /= 1.2;
				polygon_points[i][1] /= 1.2;
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);

			}

		}
	}

	// rotates the object on the Y axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateYClockwise() {
		double angle = 25.0;
		int[][] temps = scaled_points;

		if (true) {
			for (int i = 0; i < 5; i++) {
				temps[i][0]  = (int) (temps[i][0]
						* Math.cos(angle) - temps[i][2]
						* Math.sin(angle));
				temps[i][2] = (int) (temps[i][0]
						* Math.sin(angle) + temps[i][2]
						* Math.cos(angle));
				polygon_points[i][0] = (int)(temps[i][0] + x_midpoint);
				polygon_points[i][2] = (int)(temps[i][2] + z_midpoint);
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);
			}
		}
	}

	// rotates the object on the Y axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateYCounterClockwise() {
		double angle = -25.0;
		int[][] temps = scaled_points;

		if (true) {
			for (int i = 0; i < 5; i++) {
				temps[i][0]  = (int) (temps[i][0]
						* Math.cos(angle) - temps[i][2]
						* Math.sin(angle));
				temps[i][2] = (int) (temps[i][0]
						* Math.sin(angle) + temps[i][2]
						* Math.cos(angle));
				polygon_points[i][0] = (int)(temps[i][0] + x_midpoint);
				polygon_points[i][2] = (int)(temps[i][2] + z_midpoint);
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);
			}
		}
	}

	// rotates the object on the Z axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateZClockwise() {
		double angle = 25.0;
		int[][] temps = scaled_points;

		if (true) {
			for (int i = 0; i < 5; i++) {
				temps[i][0]  = (int) (temps[i][0]
						* Math.cos(angle) - temps[i][1]
						* Math.sin(angle));
				temps[i][1] = (int) (temps[i][0]
						* Math.sin(angle) + temps[i][1]
						* Math.cos(angle));
				polygon_points[i][0] = (int)(temps[i][0] + x_midpoint);
				polygon_points[i][1] = (int)(temps[i][1] + y_midpoint);
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);
			}
		}
	}

	// rotates the object on the Z axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateZCounterClockwise() {
		double angle = -25.0;
		int[][] temps = scaled_points;

		if (true) {
			for (int i = 0; i < 5; i++) {
				temps[i][0]  = (int) (temps[i][0]
						* Math.cos(angle) - temps[i][1]
						* Math.sin(angle));
				temps[i][1] = (int) (temps[i][0]
						* Math.sin(angle) + temps[i][1]
						* Math.cos(angle));
				polygon_points[i][0] = (int)(temps[i][0] + x_midpoint);
				polygon_points[i][1] = (int)(temps[i][1] + y_midpoint);
				screen_points[i][0] = (eye_z * (polygon_points[i][0] - eye_x)
						/ (eye_z + polygon_points[i][2]) + eye_x);
				screen_points[i][1] = (eye_z * (polygon_points[i][1] - eye_y)
						/ (eye_z + polygon_points[i][2]) + eye_y);
			}
			
		}
	}

	public void ScalePoints() {
		// calculates the scaled points, from the origin or (0,0)
		for (int i = 0; i < 5; i++) {
			scaled_points[i][0] = (polygon_points[i][0] - x_origin);
			scaled_points[i][1] = (polygon_points[i][1] - y_origin);
		}
	}

	// Draws the X and Y coordinants plane.
	public void DrawPlanes(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawLine(x / 2, 0, x / 2, y);
		g.drawLine(0, y / 2, x, y / 2);
	}

	// Draws each point P1 (x, y), P2(x, y) ...
	public void DrawPoints(Graphics g) {
		for (int i = 0; i < 5; i++) {
			g.setColor(Color.CYAN);
			g.drawString(("P" + (i + 1)), polygon_points[i][0],
					polygon_points[i][1]);
			g.setColor(Color.ORANGE);
			g.drawString(("(" + polygon_points[i][0] + ","
					+ polygon_points[i][1] + "," + polygon_points[i][2] + ")"),
					polygon_points[i][0] + 20, polygon_points[i][1]);

			g.drawString(x_midpoint + ", " + y_midpoint + ", " + z_midpoint,
					(int) x_midpoint, (int) y_midpoint);

		}
		// Draws the mid point of the pyrimad
		g.drawString(x_origin + ", " + y_origin + ", " + z_origin,
				x_origin - 100, y_origin - 100);
	}

	public void DrawPyramid(Graphics g) {
		// Draws the 3D triangle
		g.setColor(Color.BLACK);
		g.drawLine(screen_points[0][0], screen_points[0][1],
				screen_points[1][0], screen_points[1][1]); // P1 to P2
		g.drawLine(screen_points[2][0], screen_points[2][1],
				screen_points[0][0], screen_points[0][1]); // P1 to P3
		g.drawLine(screen_points[3][0], screen_points[3][1],
				screen_points[0][0], screen_points[0][1]); // P1 to P4
		g.drawLine(screen_points[4][0], screen_points[4][1],
				screen_points[0][0], screen_points[0][1]); // P1 to P5
		g.setColor(Color.YELLOW);
		g.drawLine(screen_points[1][0], screen_points[1][1],
				screen_points[4][0], screen_points[4][1]); // P2 to P4
		g.setColor(Color.GREEN);
		g.drawLine(screen_points[1][0], screen_points[1][1],
				screen_points[2][0], screen_points[2][1]); // P2 to P3
		g.setColor(Color.RED);
		g.drawLine(screen_points[3][0], screen_points[3][1],
				screen_points[4][0], screen_points[4][1]); // P4 to P5
		g.setColor(Color.BLUE);
		g.drawLine(screen_points[2][0], screen_points[2][1],
				screen_points[3][0], screen_points[3][1]); // P3 to P4
	}

	public void DrawResetButton(Graphics g) {
		// Draws to RESET button
		g.setColor(Color.WHITE);
		g.drawRect(WIDTH - 150, HEIGHT - 50, 100, 25);
		g.fillRect(WIDTH - 150, HEIGHT - 50, 100, 25);
		g.setColor(Color.BLACK);
		g.drawString("RESET", WIDTH - 120, HEIGHT - 32);
	}

	public void FindMidPoint(Graphics g) {
		// finds and draws the coordinates of the midpoint
		g.setColor(Color.PINK);
		g.drawLine(polygon_points[0][0], polygon_points[0][1],
				(polygon_points[1][0] + polygon_points[2][0]) / 2,
				(polygon_points[1][1] + polygon_points[2][1]) / 2);
		g.drawLine(polygon_points[1][0], polygon_points[1][1],
				(polygon_points[0][0] + polygon_points[2][0]) / 2,
				(polygon_points[0][1] + polygon_points[2][1]) / 2);
		g.drawLine(polygon_points[2][0], polygon_points[2][1],
				(polygon_points[0][0] + polygon_points[1][0]) / 2,
				(polygon_points[0][1] + polygon_points[1][1]) / 2);
	}

	// This function will trigger if the reset button
	public void ResetPyramid() {
		dispose(); // gets rid of old JFrame window
		new MainProgram(); // restarts program
	}

	public static void main(String[] args) {
		new MainProgram();

	}

}