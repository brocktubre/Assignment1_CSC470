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
	float eye_z, increment;
	int x, y, x_origin, y_origin, z_origin;
	// the points of the polygon, a 2D array
	float[][] polygon_points = new float[5][3];
	float[][] screen_points = new float[5][2];
	float[][] offset = new float[5][3];
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
			// if "G" key is pressed
			case KeyEvent.VK_G:
				ScaleUp();
				break;
			// if "N" key is pressed
			case KeyEvent.VK_N:
				ScaleDown();
				break;
			// if "UP" key is pressed
			case KeyEvent.VK_UP:
				RotateXClockwise();
				break;
			// if "DOWN" key is pressed
			case KeyEvent.VK_DOWN:
				RotateXCounterClockwise();
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

		// Worthless function
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

	// constructor - Lays out the original Polygon points for the pyramid
	public MainProgram() {

		// Intializes the canvas size and also the origin of the canvas.
		x = WIDTH; // canvas width
		y = HEIGHT; // canvas height
		// The oiring of the canvas (400, 300, 1000)
		x_origin = x / 2; // X origin of canvas
		y_origin = y / 2; // Y origin of canvas
		z_origin = 0; // Z origin of canvas
		// The distance the eye is to the screen
		eye_z = 3000;
		// The scale of increment size
		increment = 10;

		// P1
		polygon_points[0][0] = 0; // x coordinates of P1
		polygon_points[0][1] = -150; // y coordinates of P1
		polygon_points[0][2] =  300; // z coordinates of P1

		// P2
		polygon_points[1][0] =  -100; // x coordinates of P2
		polygon_points[1][1] =  100; // y coordinates of P2
		polygon_points[1][2] =  200; // z coordinates of P2

		// P3
		polygon_points[2][0] = 100; // x coordinates of P3
		polygon_points[2][1] = 100; // y coordinates of P3
		polygon_points[2][2] = 200; // z coordinates of P3

		// P4
		polygon_points[3][0] = 100; // x coordinates of P4
		polygon_points[3][1] = 100; // y coordinates of P4
		polygon_points[3][2] = 400; // z coordinates of P4

		// P5
		polygon_points[4][0] = -100; // x coordinates of P5
		polygon_points[4][1] = 100; // y coordinates of P5
		polygon_points[4][2] = 400; // z coordinates of P5
		
		// Sets offset for the screen points once they are ready to be drawn
		for(int i = 0; i < 5; i++){
			offset[i][0] = x_origin;
			offset[i][1] = y_origin;
		}

		for (int i = 0; i < 5; i++) {
			screen_points[i][0] = (eye_z * polygon_points[i][0])
					/ (eye_z + polygon_points[i][2]);
			screen_points[i][1] = (eye_z * polygon_points[i][1])
					/ (eye_z + polygon_points[i][2]);
		}

		// adds everything to the canvas and sets its attributes
		addKeyListener(new MyActionListener());
		addMouseListener(new MyMouseHandler());
		setLayout(new BorderLayout());
		setSize((int) x, (int) y);
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
		DrawPlanes(g);
		DrawPyramid(g);
		DrawResetButton(g);
		// DrawPoints(g);

		repaint();

	}

	// translates the object RIGHT using the "R" key
	/*
	 * This  adds increment to the all of the x values moving the object
	 * to the right
	 */
	public void MoveRight() {
		// if (polygon_points[2][0] < x) {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] += increment;
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// translates the object LEFT using the "L" key
	/* This simply decreases all of the x values moving the object to the left */
	/*
	 * This subtracts increment to the all of the x values moving the
	 * object to the right
	 */
	public void MoveLeft() {
		// if (polygon_points[1][0] > 0) {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] -= increment;
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// translates the object UP using the "U" key
	/* This simply decreases all of the y values moving the object to the up */
	/*
	 * This subtracts increment to the all of the y values moving the
	 * object to the right
	 */
	public void MoveUp() {
		// if ((polygon_points[0][1] - 20) > 0) {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][1] -= increment;
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// translates the object DOWN using the "D" key
	/*
	 * This increase as all of the y values moving the object to the down
	 */
	public void MoveDown() {
		// if (polygon_points[1][1] < y || polygon_points[2][1] < y) {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][1] += increment;
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// translates the object FORWARD using the "F" key
	/*
	 * This increase as all of the x and y using multiplication values
	 * moving the object foward
	 */
	public void MoveForward() {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][2] -= increment * 5;
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// translates the object BACKWARDS using the "B" key
	/*
	 * This  decrese as all of the x and y using multiplication values
	 * moving the object backwards
	 */
	public void MoveBackward() {

		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][2] += increment * 5;
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// scales the object Up using the up arrow key
	/*
	 * This  increases as all of the x and y using multiplication values
	 * scaling the object up
	 */
	public void ScaleUp() {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] *= 2;
				polygon_points[i][1] *= 2;
				polygon_points[i][2] *= 2;
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// scales the object Down using the down arrow key
	/*
	 * This decreases as all of the x and y using multiplication values
	 * scaling the object down
	 */
	public void ScaleDown() {
		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] /= 2;
				polygon_points[i][1] /= 2;
				polygon_points[i][2] /= 2;
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	/*
	 * Change description
	 */
	public void RotateXClockwise() {
		double angle = 25.0;

		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][1] = (polygon_points[i][1]
						* (float) Math.cos(angle) - polygon_points[i][2]
						* (float) Math.sin(angle));
				polygon_points[i][2] = (polygon_points[i][1]
						* (float) Math.sin(angle) + polygon_points[i][2]
						* (float) Math.cos(angle));
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	/*
	 * Change description
	 */
	public void RotateXCounterClockwise() {
		double angle = -25.0;

		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][1] = (polygon_points[i][1]
						* (float) Math.cos(angle) - polygon_points[i][2]
						* (float) Math.sin(angle));
				polygon_points[i][2] = (polygon_points[i][1]
						* (float) Math.sin(angle) + polygon_points[i][2]
						* (float) Math.cos(angle));
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}

	}

	/*
	 * Change description
	 */
	public void RotateYClockwise() {
		double angle = 25.0;

		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] = (polygon_points[i][0]
						* (float) Math.cos(angle) - polygon_points[i][2]
						* (float) Math.sin(angle));
				polygon_points[i][2] = (polygon_points[i][0]
						* (float) Math.sin(angle) + polygon_points[i][2]
						* (float) Math.cos(angle));
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// rotates the object on the Y axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateYCounterClockwise() {
		double angle = -25.0;

		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] = (polygon_points[i][0]
						* (float) Math.cos(angle) - polygon_points[i][2]
						* (float) Math.sin(angle));
				polygon_points[i][2] = (polygon_points[i][0]
						* (float) Math.sin(angle) + polygon_points[i][2]
						* (float) Math.cos(angle));
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// rotates the object on the Z axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateZClockwise() {
		double angle = 25.0;

		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] = (polygon_points[i][0]
						* (float) Math.cos(angle) - polygon_points[i][1]
						* (float) Math.sin(angle));
				polygon_points[i][1] = (polygon_points[i][0]
						* (float) Math.sin(angle) + polygon_points[i][1]
						* (float) Math.cos(angle));
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}
		}
	}

	// rotates the object on the Z axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateZCounterClockwise() {
		double angle = -25.0;

		if (true) {
			for (int i = 0; i < 5; i++) {
				polygon_points[i][0] = (polygon_points[i][0]
						* (float) Math.cos(angle) - polygon_points[i][1]
						* (float) Math.sin(angle));
				polygon_points[i][1] = (polygon_points[i][0]
						* (float) Math.sin(angle) + polygon_points[i][1]
						* (float) Math.cos(angle));
				screen_points[i][0] = (eye_z * polygon_points[i][0])
						/ (eye_z + polygon_points[i][2]);
				screen_points[i][1] = (eye_z * polygon_points[i][1])
						/ (eye_z + polygon_points[i][2]);
			}

		}
	}

	// Draws the oyramid onto the canvas
	/*
	 * Draws the pyramid
	 */
	public void DrawPyramid(Graphics g) {
		
		// Draws the 3D triangle
		g.setColor(Color.BLACK);
		g.drawLine((int)(screen_points[0][0] + offset[0][0]), (int)(screen_points[0][1] + offset[0][1]),
				(int)(screen_points[1][0] + offset[1][0]), (int)(screen_points[1][1] + offset[1][1])); // P1 to
																		// P2
		g.drawLine((int)(screen_points[2][0] + offset[2][0]), (int)(screen_points[2][1] + offset[2][1]),
				(int)(screen_points[0][0] + offset[0][0]), (int)(screen_points[0][1] + offset[0][1])); // P1 to
																		// P3
		g.drawLine((int)(screen_points[3][0] + offset[3][0]), (int)(screen_points[3][1] + offset[3][1]),
				(int)(screen_points[0][0] + offset[0][0]), (int)(screen_points[0][1] + offset[0][1])); // P1 to
																		// P4
		g.drawLine((int)(screen_points[4][0] + offset[4][0]), (int)(screen_points[4][1] + offset[4][1]),
				(int)(screen_points[0][0] + offset[0][0]), (int)(screen_points[0][1] + offset[0][1])); // P1 to
																		// P5
		g.setColor(Color.YELLOW);
		g.drawLine((int)(screen_points[1][0] + offset[1][0]), (int)(screen_points[1][1] + offset[1][1]),
				(int)(screen_points[4][0] + offset[4][0]), (int)(screen_points[4][1] + offset[4][1])); // P2 to
																		// P4
		g.setColor(Color.GREEN);
		g.drawLine((int)(screen_points[1][0] + offset[1][0]), (int)(screen_points[1][1] + offset[1][1]),
				(int)(screen_points[2][0] + offset[2][0]), (int)(screen_points[2][1] + offset[2][1])); // P2 to
																		// P3
		g.setColor(Color.RED);
		g.drawLine((int)(screen_points[3][0] + offset[3][0]), (int)(screen_points[3][1] + offset[3][1]),
				(int)(screen_points[4][0] + offset[4][0]), (int)(screen_points[4][1] + offset[4][1])); // P4 to
																		// P5
		g.setColor(Color.BLUE);
		g.drawLine((int)(screen_points[2][0] + offset[2][0]), (int)(screen_points[2][1] + offset[2][1]),
				(int)(screen_points[3][0] + offset[3][0]), (int)(screen_points[3][1] + offset[3][1])); // P3 to
																		// P4
	}

	// Draws each point P1 (x, y), P2(x, y) ...
	/*
	 * Draws each one of the projected points
	 */
	public void DrawPoints(Graphics g) {
		for (int i = 0; i < 5; i++) {
			g.setColor(Color.CYAN);
			g.drawString(("P" + (i + 1)), (int) screen_points[i][0],
					(int) screen_points[i][1]);
			g.setColor(Color.ORANGE);
			g.drawString(("(" + (int) screen_points[i][0] + ","
					+ (int) screen_points[i][1] + ")"),
					(int) screen_points[i][0] + 20, (int) screen_points[i][1]);
		}
	}

	// Draws the X and Y coordinants plane.
	/*
	 * Draws the X and Y planes
	 */
	public void DrawPlanes(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawLine((int) (x / 2), 0, (int) (x / 2), (int) y);
		g.drawLine(0, (int) (y / 2), (int) x, (int) (y / 2));
	}

	// Draws the reset button onto the canvas
	/*
	 * Draws the Reset button onto the canvas
	 */
	public void DrawResetButton(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect(WIDTH - 150, HEIGHT - 50, 100, 25);
		g.fillRect(WIDTH - 150, HEIGHT - 50, 100, 25);
		g.setColor(Color.BLACK);
		g.drawString("RESET", WIDTH - 120, HEIGHT - 32);
	}

	// This function will trigger if the reset button
	/*
	 * Gets rid of old canvas and prduces new cavnas, reseting the screen
	 */
	public void ResetPyramid() {
		dispose(); // gets rid of old JFrame window
		new MainProgram(); // restarts program
	}

	public static void main(String[] args) {
		new MainProgram();

	}

}