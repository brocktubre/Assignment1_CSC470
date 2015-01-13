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

	// Obj object defining each 3D object
	public class Obj {
		// the points of the polygon, a 2D array
		double[][] polygon_points = new double[8][4];
		double[][] screen_points = new double[8][2];
		int[][] temp_screen_points = new int[8][2];
		double[] offset = new double[2];
		boolean isSet;
	}

	Obj pyramid = new Obj();
	Obj box = new Obj();
	Obj cube = new Obj();

	// initialization of the window width and the window height
	final int WIDTH = 800;
	final int HEIGHT = 600;

	// initialization of x origin and y origin, also screen width and size
	double eye_z, increment;
	double x, y, x_origin, y_origin, z_origin;
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
			if (mouse_x > (x - 150) && mouse_x < (x - 150 + 105)
					&& mouse_y > (y - 50) && mouse_y < (y - 50 + 25))
				ResetPyramid(); // resets if the mouse is clicked
			// The Red button is selected
			if (mouse_x > (x - 70) && mouse_x < (x - 70 + 25)
					&& mouse_y > (y - 85) && mouse_y < (y - 85 + 25)) {
				if (cube.isSet)
					cube.isSet = false;
				else
					cube.isSet = true;

			}
			// The Green button is selected
			if (mouse_x > (x - 110) && mouse_x < (x - 110 + 25)
					&& mouse_y > (y - 85) && mouse_y < (y - 85 + 25)) {
				if (pyramid.isSet)
					pyramid.isSet = false;
				else
					pyramid.isSet = true;
			}
			// The Blue button is selected
			if (mouse_x > (x - 150) && mouse_x < (x - 150 + 25)
					&& mouse_y > (y - 85) && mouse_y < (y - 85 + 25)) {
				if (box.isSet)
					box.isSet = false;
				else
					box.isSet = true;
			}

			// System.out.println(mouse_x);

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
		eye_z = 3000.0;
		// The scale of increment size
		increment = 10;

		// P1
		pyramid.polygon_points[0][0] = 0; // x coordinates of P1
		pyramid.polygon_points[0][1] = 100; // y coordinates of P1
		pyramid.polygon_points[0][2] = 300; // z coordinates of P1

		// P2
		pyramid.polygon_points[1][0] = -100; // x coordinates of P2
		pyramid.polygon_points[1][1] = -100; // y coordinates of P2
		pyramid.polygon_points[1][2] = 200; // z coordinates of P2

		// P3
		pyramid.polygon_points[2][0] = 100; // x coordinates of P3
		pyramid.polygon_points[2][1] = -100; // y coordinates of P3
		pyramid.polygon_points[2][2] = 200; // z coordinates of P3

		// P4
		pyramid.polygon_points[3][0] = 100; // x coordinates of P4
		pyramid.polygon_points[3][1] = -100; // y coordinates of P4
		pyramid.polygon_points[3][2] = 400; // z coordinates of P4

		// P5
		pyramid.polygon_points[4][0] = -100; // x coordinates of P5
		pyramid.polygon_points[4][1] = -100; // y coordinates of P5
		pyramid.polygon_points[4][2] = 400; // z coordinates of P5

		// Sets offset for the screen points once they are ready to be drawn
		pyramid.offset[0] = x_origin;
		pyramid.offset[1] = y_origin;

		// Sets the original perspective projection polygon points and stores
		// them as screen points.
		for (int i = 0; i < 5; i++) {
			pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
					/ (eye_z + pyramid.polygon_points[i][2]);
			pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
					/ (eye_z + pyramid.polygon_points[i][2]);
		}

		// Adds offset for screen origin
		for (int i = 0; i < 5; i++) {
			pyramid.temp_screen_points[i][0] = (int) (pyramid.offset[0] + pyramid.screen_points[i][0]);
			pyramid.temp_screen_points[i][1] = (int) (pyramid.offset[1] - pyramid.screen_points[i][1]);
		}

		// P1
		box.polygon_points[0][0] = -250;
		box.polygon_points[0][1] = 100;
		box.polygon_points[0][2] = 200;

		// P2
		box.polygon_points[1][0] = -150;
		box.polygon_points[1][1] = 100;
		box.polygon_points[1][2] = 200;

		// P3
		box.polygon_points[2][0] = -250;
		box.polygon_points[2][1] = 20;
		box.polygon_points[2][2] = 200;

		// P4
		box.polygon_points[3][0] = -150;
		box.polygon_points[3][1] = 20;
		box.polygon_points[3][2] = 200;

		// P5
		box.polygon_points[4][0] = -250;
		box.polygon_points[4][1] = 100;
		box.polygon_points[4][2] = 400;

		// P6
		box.polygon_points[5][0] = -150;
		box.polygon_points[5][1] = 100;
		box.polygon_points[5][2] = 400;

		// P7
		box.polygon_points[6][0] = -250;
		box.polygon_points[6][1] = 20;
		box.polygon_points[6][2] = 400;

		// P8
		box.polygon_points[7][0] = -150;
		box.polygon_points[7][1] = 20;
		box.polygon_points[7][2] = 400;

		box.offset[0] = x_origin;
		box.offset[1] = y_origin;

		// Sets the original perspective projection polygon points and stores
		// them as screen points.
		for (int i = 0; i < 8; i++) {
			box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
					/ (eye_z + box.polygon_points[i][2]);
			box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
					/ (eye_z + box.polygon_points[i][2]);
		}

		// Adds offset for screen origin
		for (int i = 0; i < 8; i++) {
			box.temp_screen_points[i][0] = (int) (box.offset[0] + box.screen_points[i][0]);
			box.temp_screen_points[i][1] = (int) (box.offset[1] - box.screen_points[i][1]);
		}

		// P1
		cube.polygon_points[0][0] = 250;
		cube.polygon_points[0][1] = 100;
		cube.polygon_points[0][2] = 200;

		// P2
		cube.polygon_points[1][0] = 150;
		cube.polygon_points[1][1] = 100;
		cube.polygon_points[1][2] = 200;

		// P3
		cube.polygon_points[2][0] = 250;
		cube.polygon_points[2][1] = 20;
		cube.polygon_points[2][2] = 200;

		// P4
		cube.polygon_points[3][0] = 150;
		cube.polygon_points[3][1] = 20;
		cube.polygon_points[3][2] = 200;

		// P5
		cube.polygon_points[4][0] = 250;
		cube.polygon_points[4][1] = 100;
		cube.polygon_points[4][2] = 400;

		// P6
		cube.polygon_points[5][0] = 150;
		cube.polygon_points[5][1] = 100;
		cube.polygon_points[5][2] = 400;

		// P7
		cube.polygon_points[6][0] = 250;
		cube.polygon_points[6][1] = 20;
		cube.polygon_points[6][2] = 400;

		// P8
		cube.polygon_points[7][0] = 150;
		cube.polygon_points[7][1] = 20;
		cube.polygon_points[7][2] = 400;

		cube.offset[0] = x_origin;
		cube.offset[1] = y_origin;

		// Sets the original perspective projection polygon points and stores
		// them as screen points.
		for (int i = 0; i < 8; i++) {
			cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
					/ (eye_z + cube.polygon_points[i][2]);
			cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
					/ (eye_z + cube.polygon_points[i][2]);
		}

		// Adds offset for screen origin
		for (int i = 0; i < 8; i++) {
			cube.temp_screen_points[i][0] = (int) (cube.offset[0] + cube.screen_points[i][0]);
			cube.temp_screen_points[i][1] = (int) (cube.offset[1] - cube.screen_points[i][1]);
		}

		// adds everything to the canvas and sets its attributes
		addKeyListener(new MyActionListener());
		addMouseListener(new MyMouseHandler());
		setLayout(new BorderLayout());
		setSize((int) x, (int) y);
		setTitle("3D Pyramid - Assignment 2");
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
		DrawBox(g);
		DrawCube(g);
		DrawButtons(g);

		repaint();

	}

	// translates the object RIGHT using the "R" key
	/*
	 * This adds increment to the all of the x values moving the object to the
	 * right
	 */
	public void MoveRight() {
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] += increment;
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] += increment;
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] += increment;
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
			}
		AddOffset();
	}

	// translates the object LEFT using the "L" key
	/* This simply decreases all of the x values moving the object to the left */
	/*
	 * This subtracts increment to the all of the x values moving the object to
	 * the right
	 */
	public void MoveLeft() {
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] -= increment;
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] -= increment;
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] -= increment;
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
			}
		AddOffset();
	}

	// translates the object DOWN using the "D" key
	/*
	 * This decreases as all of the y values moving the object to the down
	 */
	public void MoveDown() {
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][1] -= increment;
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);

			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][1] -= increment;
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);

			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][1] -= increment;
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);

			}
		AddOffset();
	}

	// translates the object UP using the "U" key
	/*
	 * This increment to the all of the y values moving the object to the up
	 */
	public void MoveUp() {
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][1] += increment;
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][1] += increment;
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][1] += increment;
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}
		AddOffset();

	}

	// translates the object FORWARD using the "F" key
	/*
	 * This increase as all of the x and y using multiplication values moving
	 * the object foward
	 */
	public void MoveForward() {
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][2] -= increment * 15;
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][2] -= increment * 15;
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][2] -= increment * 15;
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}
		AddOffset();
	}

	// translates the object BACKWARDS using the "B" key
	/*
	 * This decrese as all of the x and y using multiplication values moving the
	 * object backwards
	 */
	public void MoveBackward() {
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][2] += increment * 15;
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][2] += increment * 15;
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][2] += increment * 15;
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}
		AddOffset();
	}

	// scales the object Up using the up arrow key
	/*
	 * This increases as all of the x, y, and z using multiplication, scaling
	 * the object up
	 */
	public void ScaleUp() {
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] *= 2;
				pyramid.polygon_points[i][1] *= 2;
				pyramid.polygon_points[i][2] *= 2;
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] *= 2;
				box.polygon_points[i][1] *= 2;
				box.polygon_points[i][2] *= 2;
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] *= 2;
				cube.polygon_points[i][1] *= 2;
				cube.polygon_points[i][2] *= 2;
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		AddOffset();

	}

	// scales the object Down using the down arrow key
	/*
	 * This decreases as all of the x, y, and z using division, scaling the
	 * object down
	 */
	public void ScaleDown() {
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] /= 2;
				pyramid.polygon_points[i][1] /= 2;
				pyramid.polygon_points[i][2] /= 2;
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] /= 2;
				box.polygon_points[i][1] /= 2;
				box.polygon_points[i][2] /= 2;
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] /= 2;
				cube.polygon_points[i][1] /= 2;
				cube.polygon_points[i][2] /= 2;
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}
		AddOffset();
	}

	/*
	 * Change description
	 */
	public void RotateXClockwise() {
		double angle = 25.0;

		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][1] = (pyramid.polygon_points[i][1]
						* Math.cos(angle) - pyramid.polygon_points[i][2]
						* Math.sin(angle));
				pyramid.polygon_points[i][2] = (pyramid.polygon_points[i][1]
						* Math.sin(angle) + pyramid.polygon_points[i][2]
						* Math.cos(angle));
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][1] = (box.polygon_points[i][1]
						* Math.cos(angle) - box.polygon_points[i][2]
						* Math.sin(angle));
				box.polygon_points[i][2] = (box.polygon_points[i][1]
						* Math.sin(angle) + box.polygon_points[i][2]
						* Math.cos(angle));
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][1] = (cube.polygon_points[i][1]
						* Math.cos(angle) - cube.polygon_points[i][2]
						* Math.sin(angle));
				cube.polygon_points[i][2] = (cube.polygon_points[i][1]
						* Math.sin(angle) + cube.polygon_points[i][2]
						* Math.cos(angle));
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}
		AddOffset();
	}

	/*
	 * Change description
	 */
	public void RotateXCounterClockwise() {
		double angle = -25.0;

		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][1] = (pyramid.polygon_points[i][1]
						* Math.cos(angle) - pyramid.polygon_points[i][2]
						* Math.sin(angle));
				pyramid.polygon_points[i][2] = (pyramid.polygon_points[i][1]
						* Math.sin(angle) + pyramid.polygon_points[i][2]
						* Math.cos(angle));
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][1] = (box.polygon_points[i][1]
						* Math.cos(angle) - box.polygon_points[i][2]
						* Math.sin(angle));
				box.polygon_points[i][2] = (box.polygon_points[i][1]
						* Math.sin(angle) + box.polygon_points[i][2]
						* Math.cos(angle));
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][1] = (cube.polygon_points[i][1]
						* Math.cos(angle) - cube.polygon_points[i][2]
						* Math.sin(angle));
				cube.polygon_points[i][2] = (cube.polygon_points[i][1]
						* Math.sin(angle) + cube.polygon_points[i][2]
						* Math.cos(angle));
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}

		AddOffset();
	}

	/*
	 * Change description
	 */
	public void RotateYClockwise() {
		double angle = 25.0;

		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] = (pyramid.polygon_points[i][0]
						* Math.cos(angle) - pyramid.polygon_points[i][2]
						* Math.sin(angle));
				pyramid.polygon_points[i][2] = (pyramid.polygon_points[i][0]
						* Math.sin(angle) + pyramid.polygon_points[i][2]
						* Math.cos(angle));
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] = (box.polygon_points[i][0]
						* Math.cos(angle) - box.polygon_points[i][2]
						* Math.sin(angle));
				box.polygon_points[i][2] = (box.polygon_points[i][0]
						* Math.sin(angle) + box.polygon_points[i][2]
						* Math.cos(angle));
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] = (cube.polygon_points[i][0]
						* Math.cos(angle) - cube.polygon_points[i][2]
						* Math.sin(angle));
				cube.polygon_points[i][2] = (cube.polygon_points[i][0]
						* Math.sin(angle) + cube.polygon_points[i][2]
						* Math.cos(angle));
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}

		AddOffset();
	}

	// rotates the object on the Y axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateYCounterClockwise() {
		double angle = -25.0;

		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] = (pyramid.polygon_points[i][0]
						* Math.cos(angle) - pyramid.polygon_points[i][2]
						* Math.sin(angle));
				pyramid.polygon_points[i][2] = (pyramid.polygon_points[i][0]
						* Math.sin(angle) + pyramid.polygon_points[i][2]
						* Math.cos(angle));
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] = (box.polygon_points[i][0]
						* Math.cos(angle) - box.polygon_points[i][2]
						* Math.sin(angle));
				box.polygon_points[i][2] = (box.polygon_points[i][0]
						* Math.sin(angle) + box.polygon_points[i][2]
						* Math.cos(angle));
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] = (cube.polygon_points[i][0]
						* Math.cos(angle) - cube.polygon_points[i][2]
						* Math.sin(angle));
				cube.polygon_points[i][2] = (cube.polygon_points[i][0]
						* Math.sin(angle) + cube.polygon_points[i][2]
						* Math.cos(angle));
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}

		AddOffset();
	}

	// rotates the object on the Z axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateZClockwise() {
		double angle = 25.0;

		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] = (pyramid.polygon_points[i][0]
						* Math.cos(angle) - pyramid.polygon_points[i][1]
						* Math.sin(angle));
				pyramid.polygon_points[i][1] = (pyramid.polygon_points[i][0]
						* Math.sin(angle) + pyramid.polygon_points[i][1]
						* Math.cos(angle));
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] = (box.polygon_points[i][0]
						* Math.cos(angle) - box.polygon_points[i][1]
						* Math.sin(angle));
				box.polygon_points[i][1] = (box.polygon_points[i][0]
						* Math.sin(angle) + box.polygon_points[i][1]
						* Math.cos(angle));
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] = (cube.polygon_points[i][0]
						* Math.cos(angle) - cube.polygon_points[i][1]
						* Math.sin(angle));
				cube.polygon_points[i][1] = (cube.polygon_points[i][0]
						* Math.sin(angle) + cube.polygon_points[i][1]
						* Math.cos(angle));
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}

		AddOffset();

	}

	// rotates the object on the Z axis clockwise using the right arrow key
	/*
	 * Change description
	 */
	public void RotateZCounterClockwise() {
		double angle = -25.0;

		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] = (pyramid.polygon_points[i][0]
						* Math.cos(angle) - pyramid.polygon_points[i][1]
						* Math.sin(angle));
				pyramid.polygon_points[i][1] = (pyramid.polygon_points[i][0]
						* Math.sin(angle) + pyramid.polygon_points[i][1]
						* Math.cos(angle));
				pyramid.screen_points[i][0] = (eye_z * pyramid.polygon_points[i][0])
						/ (eye_z + pyramid.polygon_points[i][2]);
				pyramid.screen_points[i][1] = (eye_z * pyramid.polygon_points[i][1])
						/ (eye_z + pyramid.polygon_points[i][2]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] = (box.polygon_points[i][0]
						* Math.cos(angle) - box.polygon_points[i][1]
						* Math.sin(angle));
				box.polygon_points[i][1] = (box.polygon_points[i][0]
						* Math.sin(angle) + box.polygon_points[i][1]
						* Math.cos(angle));
				box.screen_points[i][0] = (eye_z * box.polygon_points[i][0])
						/ (eye_z + box.polygon_points[i][2]);
				box.screen_points[i][1] = (eye_z * box.polygon_points[i][1])
						/ (eye_z + box.polygon_points[i][2]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] = (cube.polygon_points[i][0]
						* Math.cos(angle) - cube.polygon_points[i][1]
						* Math.sin(angle));
				cube.polygon_points[i][1] = (cube.polygon_points[i][0]
						* Math.sin(angle) + cube.polygon_points[i][1]
						* Math.cos(angle));
				cube.screen_points[i][0] = (eye_z * cube.polygon_points[i][0])
						/ (eye_z + cube.polygon_points[i][2]);
				cube.screen_points[i][1] = (eye_z * cube.polygon_points[i][1])
						/ (eye_z + cube.polygon_points[i][2]);
			}

		AddOffset();

	}

	// Adds the appropriate offset to center the origin onto the screen
	public void AddOffset() {

		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.temp_screen_points[i][0] = (int) (pyramid.offset[0] + pyramid.screen_points[i][0]);
				pyramid.temp_screen_points[i][1] = (int) (pyramid.offset[1] - pyramid.screen_points[i][1]);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.temp_screen_points[i][0] = (int) (box.offset[0] + box.screen_points[i][0]);
				box.temp_screen_points[i][1] = (int) (box.offset[1] - box.screen_points[i][1]);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.temp_screen_points[i][0] = (int) (cube.offset[0] + cube.screen_points[i][0]);
				cube.temp_screen_points[i][1] = (int) (cube.offset[1] - cube.screen_points[i][1]);
			}
	}

	// Draws the oyramid onto the canvas
	/*
	 * Draws the pyramid
	 */
	public void DrawPyramid(Graphics g) {

		// Draws the 3D pyramid
		g.setColor(Color.GREEN);
		g.drawLine(pyramid.temp_screen_points[0][0],
				pyramid.temp_screen_points[0][1],
				pyramid.temp_screen_points[1][0],
				pyramid.temp_screen_points[1][1]); // P1 to
		// P2
		g.drawLine(pyramid.temp_screen_points[2][0],
				pyramid.temp_screen_points[2][1],
				pyramid.temp_screen_points[0][0],
				pyramid.temp_screen_points[0][1]); // P1 to
		// P3
		g.drawLine(pyramid.temp_screen_points[3][0],
				pyramid.temp_screen_points[3][1],
				pyramid.temp_screen_points[0][0],
				pyramid.temp_screen_points[0][1]); // P1 to
		// P4
		g.drawLine(pyramid.temp_screen_points[4][0],
				pyramid.temp_screen_points[4][1],
				pyramid.temp_screen_points[0][0],
				pyramid.temp_screen_points[0][1]); // P1 to
		// P5
		g.drawLine(pyramid.temp_screen_points[1][0],
				pyramid.temp_screen_points[1][1],
				pyramid.temp_screen_points[4][0],
				pyramid.temp_screen_points[4][1]); // P2 to
		// P4
		g.drawLine(pyramid.temp_screen_points[1][0],
				pyramid.temp_screen_points[1][1],
				pyramid.temp_screen_points[2][0],
				pyramid.temp_screen_points[2][1]); // P2 to
		// P3
		g.drawLine(pyramid.temp_screen_points[3][0],
				pyramid.temp_screen_points[3][1],
				pyramid.temp_screen_points[4][0],
				pyramid.temp_screen_points[4][1]); // P4 to
		// P5
		g.drawLine(pyramid.temp_screen_points[2][0],
				pyramid.temp_screen_points[2][1],
				pyramid.temp_screen_points[3][0],
				pyramid.temp_screen_points[3][1]); // P3 to
		// P4

	}

	// Draws the box onto the canvas
	/*
	 * Draws the box
	 */
	public void DrawBox(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawLine(box.temp_screen_points[0][0], box.temp_screen_points[0][1],
				box.temp_screen_points[1][0], box.temp_screen_points[1][1]);
		g.drawLine(box.temp_screen_points[1][0], box.temp_screen_points[1][1],
				box.temp_screen_points[3][0], box.temp_screen_points[3][1]);
		g.drawLine(box.temp_screen_points[3][0], box.temp_screen_points[3][1],
				box.temp_screen_points[2][0], box.temp_screen_points[2][1]);
		g.drawLine(box.temp_screen_points[2][0], box.temp_screen_points[2][1],
				box.temp_screen_points[0][0], box.temp_screen_points[0][1]);
		g.drawLine(box.temp_screen_points[4][0], box.temp_screen_points[4][1],
				box.temp_screen_points[5][0], box.temp_screen_points[5][1]);
		g.drawLine(box.temp_screen_points[5][0], box.temp_screen_points[5][1],
				box.temp_screen_points[7][0], box.temp_screen_points[7][1]);
		g.drawLine(box.temp_screen_points[7][0], box.temp_screen_points[7][1],
				box.temp_screen_points[6][0], box.temp_screen_points[6][1]);
		g.drawLine(box.temp_screen_points[6][0], box.temp_screen_points[6][1],
				box.temp_screen_points[4][0], box.temp_screen_points[4][1]);
		g.drawLine(box.temp_screen_points[0][0], box.temp_screen_points[0][1],
				box.temp_screen_points[4][0], box.temp_screen_points[4][1]);
		g.drawLine(box.temp_screen_points[1][0], box.temp_screen_points[1][1],
				box.temp_screen_points[5][0], box.temp_screen_points[5][1]);
		g.drawLine(box.temp_screen_points[2][0], box.temp_screen_points[2][1],
				box.temp_screen_points[6][0], box.temp_screen_points[6][1]);
		g.drawLine(box.temp_screen_points[3][0], box.temp_screen_points[3][1],
				box.temp_screen_points[7][0], box.temp_screen_points[7][1]);

	}

	// Draws the cube onto the canvas
	/*
	 * Draws the cube
	 */
	public void DrawCube(Graphics g) {
		g.setColor(Color.RED);
		g.drawLine(cube.temp_screen_points[0][0],
				cube.temp_screen_points[0][1], cube.temp_screen_points[1][0],
				cube.temp_screen_points[1][1]);
		g.drawLine(cube.temp_screen_points[1][0],
				cube.temp_screen_points[1][1], cube.temp_screen_points[3][0],
				cube.temp_screen_points[3][1]);
		g.drawLine(cube.temp_screen_points[3][0],
				cube.temp_screen_points[3][1], cube.temp_screen_points[2][0],
				cube.temp_screen_points[2][1]);
		g.drawLine(cube.temp_screen_points[2][0],
				cube.temp_screen_points[2][1], cube.temp_screen_points[0][0],
				cube.temp_screen_points[0][1]);
		g.drawLine(cube.temp_screen_points[4][0],
				cube.temp_screen_points[4][1], cube.temp_screen_points[5][0],
				cube.temp_screen_points[5][1]);
		g.drawLine(cube.temp_screen_points[5][0],
				cube.temp_screen_points[5][1], cube.temp_screen_points[7][0],
				cube.temp_screen_points[7][1]);
		g.drawLine(cube.temp_screen_points[7][0],
				cube.temp_screen_points[7][1], cube.temp_screen_points[6][0],
				cube.temp_screen_points[6][1]);
		g.drawLine(cube.temp_screen_points[6][0],
				cube.temp_screen_points[6][1], cube.temp_screen_points[4][0],
				cube.temp_screen_points[4][1]);
		g.drawLine(cube.temp_screen_points[0][0],
				cube.temp_screen_points[0][1], cube.temp_screen_points[4][0],
				cube.temp_screen_points[4][1]);
		g.drawLine(cube.temp_screen_points[1][0],
				cube.temp_screen_points[1][1], cube.temp_screen_points[5][0],
				cube.temp_screen_points[5][1]);
		g.drawLine(cube.temp_screen_points[2][0],
				cube.temp_screen_points[2][1], cube.temp_screen_points[6][0],
				cube.temp_screen_points[6][1]);
		g.drawLine(cube.temp_screen_points[3][0],
				cube.temp_screen_points[3][1], cube.temp_screen_points[7][0],
				cube.temp_screen_points[7][1]);

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
	public void DrawButtons(Graphics g) {

		// Reset button
		g.setColor(Color.WHITE);
		g.drawRect(WIDTH - 150, HEIGHT - 50, 105, 25);
		g.fillRect(WIDTH - 150, HEIGHT - 50, 105, 25);
		g.setColor(Color.BLACK);
		g.drawString("RESET", WIDTH - 115, HEIGHT - 32);

		// Blue button
		g.setColor(Color.BLUE);
		g.drawOval(WIDTH - 150, HEIGHT - 85, 25, 25);
		g.fillOval(WIDTH - 150, HEIGHT - 85, 25, 25);
		g.setColor(Color.WHITE);

		// Green button
		g.setColor(Color.GREEN);
		g.drawOval(WIDTH - 110, HEIGHT - 85, 25, 25);
		g.fillOval(WIDTH - 110, HEIGHT - 85, 25, 25);
		g.setColor(Color.WHITE);

		// Red button
		g.setColor(Color.RED);
		g.drawOval(WIDTH - 70, HEIGHT - 85, 25, 25);
		g.fillOval(WIDTH - 70, HEIGHT - 85, 25, 25);
		g.setColor(Color.WHITE);

		g.setColor(Color.BLACK);
		// Green button
		if (pyramid.isSet)
			g.drawOval(WIDTH - 110, HEIGHT - 85, 25, 25);
		// Blue button
		if (box.isSet)
			g.drawOval(WIDTH - 150, HEIGHT - 85, 25, 25);
		// Red button
		if (cube.isSet)
			g.drawOval(WIDTH - 70, HEIGHT - 85, 25, 25);

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