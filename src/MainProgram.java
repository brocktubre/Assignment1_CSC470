/**
 * 	Author: 	Brock Tubre
 * 	Date: 		Feburary 06, 2015
 * 	CWID:		102-10-73
 * 	Assignment: 3
 * 	File path: 	/Volumes/Partition512/School/CSC470/Assignment
 * 
 * 	Description: This program projects a 3D objects onto the screen using nothing but the line draw tool
 * 	The program runs in Eclipse Version: Luna Service Release 1 (4.4.1). 
 */

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import java.awt.Image;
import java.math.*;
import java.util.ArrayList;
import java.util.Random;

public class MainProgram extends JFrame {

	// Obj object defining each 3D object
	public class Obj {
		// the points of the polygon,
		double[][] polygon_points = new double[8][3];
		double[][] screen_points = new double[8][3];
		double[] midpoints = new double[3];
		double[] offset = new double[2];
		boolean isSet; // boolean to know which objects are selected
		boolean cullingIsSet;
		boolean fillingIsSet;
		boolean wireFrameSet;
		boolean[] drawCulling = new boolean[6];
		boolean[] drawFilling = new boolean[6];
		double[][] fill_polygon_points = new double[8][3];
		double[][] fill_screen_points = new double[8][3];
		String name;
	}

	Obj pyramid = new Obj();
	Obj box = new Obj();
	Obj cube = new Obj();

	public class Segment {
		double x0, y0, x1, y1, z0, z1;
		double maxY, minY, dx, initial_x, maxX, minX, minZ, maxZ;

	}

	// deinfes each segment for polygon fill
	Segment segP0P1 = new Segment();
	Segment segP0P2 = new Segment();
	Segment segP1P2 = new Segment();

	// List of segments
	ArrayList<Segment> Global_ET = new ArrayList<Segment>();

	// initialization of the window width and the window height
	final int WIDTH = 800;
	final int HEIGHT = 600;
	int count = 0;

	// Z buffer table
	double[][] ZBuffer = new double[WIDTH][HEIGHT];
	// stores negative into a varible
	double neg_inf = Double.NEGATIVE_INFINITY;

	// initialization of x origin and y origin, also screen width and size
	double eye_z, increment;
	double x, y, x_origin, y_origin, z_origin;
	// double duffering, takes an image of the screen and redraws the image when
	// keys are pressed
	private Image dbImage;
	private Graphics dbg;

	// Random colors for filling
	int randR = randInt(0, 255);
	int randG = randInt(0, 255);
	int randB = randInt(0, 255);
	int rave = 0;

	// Action listener for the key board inputs
	public class MyActionListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			// grabs the most recent key that is pressed
			int keyCode = e.getKeyCode();
			count = 1;

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
			// if "C" key is pressed
			case KeyEvent.VK_C:
				BackFaceCullingOn();
				break;
			// if "X" key is pressed
			case KeyEvent.VK_X:
				BackFaceCullingOff();
				break;
			// if "P" key is pressed
			case KeyEvent.VK_P:
				rave = 0;
				FillObjectOn();
				break;
			// if "O" key is pressed
			case KeyEvent.VK_O:
				FillObjectOff();
				break;
			// if "O" key is pressed
			case KeyEvent.VK_W:
				ToggleWireframeOn();
				break;
			// if "O" key is pressed
			case KeyEvent.VK_Q:
				ToggleWireframeOff();
				break;
			// if "O" key is pressed
			case KeyEvent.VK_A:
				rave = 1;
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
		eye_z = 2000.0;
		// The scale of increment size
		increment = 10;

		// P0
		pyramid.polygon_points[0][0] = 0; // x coordinates of P1
		pyramid.polygon_points[0][1] = 100; // y coordinates of P1
		pyramid.polygon_points[0][2] = 300; // z coordinates of P1

		// P1
		pyramid.polygon_points[1][0] = 100; // x coordinates of P2
		pyramid.polygon_points[1][1] = -100; // y coordinates of P2
		pyramid.polygon_points[1][2] = 200; // z coordinates of P2

		// P2
		pyramid.polygon_points[2][0] = -100; // x coordinates of P3
		pyramid.polygon_points[2][1] = -100; // y coordinates of P3
		pyramid.polygon_points[2][2] = 200; // z coordinates of P3

		// P3
		pyramid.polygon_points[3][0] = 100; // x coordinates of P4
		pyramid.polygon_points[3][1] = -100; // y coordinates of P4
		pyramid.polygon_points[3][2] = 400; // z coordinates of P4

		// P4
		pyramid.polygon_points[4][0] = -100; // x coordinates of P5
		pyramid.polygon_points[4][1] = -100; // y coordinates of P5
		pyramid.polygon_points[4][2] = 400; // z coordinates of P5

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

		// P1
		cube.polygon_points[0][0] = 150;
		cube.polygon_points[0][1] = 100;
		cube.polygon_points[0][2] = 200;

		// P2
		cube.polygon_points[1][0] = 250;
		cube.polygon_points[1][1] = 100;
		cube.polygon_points[1][2] = 200;

		// P3
		cube.polygon_points[2][0] = 150;
		cube.polygon_points[2][1] = 20;
		cube.polygon_points[2][2] = 200;

		// P4
		cube.polygon_points[3][0] = 250;
		cube.polygon_points[3][1] = 20;
		cube.polygon_points[3][2] = 200;

		// P5
		cube.polygon_points[4][0] = 150;
		cube.polygon_points[4][1] = 100;
		cube.polygon_points[4][2] = 300;

		// P6
		cube.polygon_points[5][0] = 250;
		cube.polygon_points[5][1] = 100;
		cube.polygon_points[5][2] = 300;

		// P7
		cube.polygon_points[6][0] = 150;
		cube.polygon_points[6][1] = 20;
		cube.polygon_points[6][2] = 300;

		// P8
		cube.polygon_points[7][0] = 250;
		cube.polygon_points[7][1] = 20;
		cube.polygon_points[7][2] = 300;

		// sets the offset for each object
		pyramid.offset[0] = box.offset[0] = cube.offset[0] = x_origin;
		pyramid.offset[1] = box.offset[1] = cube.offset[1] = y_origin;

		// Sets the original perspective projection polygon points and stores
		// them as screen points.
		PerspectiveProjection(pyramid, 5);
		PerspectiveProjection(box, 8);
		PerspectiveProjection(cube, 8);

		// must set each object before adding the intial offset
		// sets the objects back to false (not selected)
		pyramid.isSet = box.isSet = cube.isSet = true;
		AddOffset();
		pyramid.isSet = box.isSet = cube.isSet = false;

		pyramid.wireFrameSet = box.wireFrameSet = cube.wireFrameSet = true;

		// name of the objects
		box.name = "box";
		pyramid.name = "pyramid";
		cube.name = "cube";

		for (int i = 0; i < WIDTH; i++)
			for (int j = 0; j < HEIGHT; j++)
				ZBuffer[i][j] = neg_inf;

		// adds everything to the canvas and sets its attributes
		addKeyListener(new MyActionListener());
		addMouseListener(new MyMouseHandler());
		setLayout(new BorderLayout());
		setSize((int) x, (int) y);
		setTitle("3D Pyramid - Assignment 3");
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
		FindMidPoint();
		if (pyramid.isSet) {
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] += increment;
				PerspectiveProjection(pyramid, 5);
			}
		}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] += increment;
				PerspectiveProjection(box, 8);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] += increment;
				PerspectiveProjection(cube, 8);
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
		FindMidPoint();
		if (pyramid.isSet) {
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] -= increment;
				PerspectiveProjection(pyramid, 5);
			}
		}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] -= increment;
				PerspectiveProjection(box, 8);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] -= increment;
				PerspectiveProjection(cube, 8);
			}
		AddOffset();
	}

	// translates the object DOWN using the "D" key
	/*
	 * This decreases as all of the y values moving the object to the down
	 */
	public void MoveDown() {
		FindMidPoint();
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][1] -= increment;
				PerspectiveProjection(pyramid, 5);

			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][1] -= increment;
				PerspectiveProjection(box, 8);

			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][1] -= increment;
				PerspectiveProjection(cube, 8);

			}
		AddOffset();
	}

	// translates the object UP using the "U" key
	/*
	 * This increment to the all of the y values moving the object to the up
	 */
	public void MoveUp() {
		FindMidPoint();
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][1] += increment;
				PerspectiveProjection(pyramid, 5);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][1] += increment;
				PerspectiveProjection(box, 8);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][1] += increment;
				PerspectiveProjection(cube, 8);
			}
		AddOffset();

	}

	// translates the object FORWARD using the "F" key
	/*
	 * This increase as all of the x and y using multiplication values moving
	 * the object foward
	 */
	public void MoveForward() {
		FindMidPoint();
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][2] -= increment * 15;
				PerspectiveProjection(pyramid, 5);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][2] -= increment * 15;
				PerspectiveProjection(box, 8);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][2] -= increment * 15;
				PerspectiveProjection(cube, 8);
			}
		AddOffset();
	}

	// translates the object BACKWARDS using the "B" key
	/*
	 * This decrese as all of the x and y using multiplication values moving the
	 * object backwards
	 */
	public void MoveBackward() {
		FindMidPoint();
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][2] += increment * 15;
				PerspectiveProjection(pyramid, 5);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][2] += increment * 15;
				PerspectiveProjection(box, 8);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][2] += increment * 15;
				PerspectiveProjection(cube, 8);
			}
		AddOffset();
	}

	// Scales the object Up using the "G" key
	/*
	 * This increases as all of the x, y, and z using multiplication, scaling
	 * the object up
	 */
	public void ScaleUp() {
		FindMidPoint();
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] -= pyramid.midpoints[0];
				pyramid.polygon_points[i][1] -= pyramid.midpoints[1];
				pyramid.polygon_points[i][2] -= pyramid.midpoints[2];
				pyramid.polygon_points[i][0] *= 1.2;
				pyramid.polygon_points[i][1] *= 1.2;
				pyramid.polygon_points[i][2] *= 1.2;
				pyramid.polygon_points[i][0] += pyramid.midpoints[0];
				pyramid.polygon_points[i][1] += pyramid.midpoints[1];
				pyramid.polygon_points[i][2] += pyramid.midpoints[2];
				PerspectiveProjection(pyramid, 5);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] -= box.midpoints[0];
				box.polygon_points[i][1] -= box.midpoints[1];
				box.polygon_points[i][2] -= box.midpoints[2];
				box.polygon_points[i][0] *= 1.2;
				box.polygon_points[i][1] *= 1.2;
				box.polygon_points[i][2] *= 1.2;
				box.polygon_points[i][0] += box.midpoints[0];
				box.polygon_points[i][1] += box.midpoints[1];
				box.polygon_points[i][2] += box.midpoints[2];
				PerspectiveProjection(box, 8);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] -= cube.midpoints[0];
				cube.polygon_points[i][1] -= cube.midpoints[1];
				cube.polygon_points[i][2] -= cube.midpoints[2];
				cube.polygon_points[i][0] *= 1.2;
				cube.polygon_points[i][1] *= 1.2;
				cube.polygon_points[i][2] *= 1.2;
				cube.polygon_points[i][0] += cube.midpoints[0];
				cube.polygon_points[i][1] += cube.midpoints[1];
				cube.polygon_points[i][2] += cube.midpoints[2];
				PerspectiveProjection(cube, 8);
			}
		AddOffset();

	}

	// Scales the object Down using the "N" key
	/*
	 * This decreases as all of the x, y, and z using division, scaling the
	 * object down
	 */
	public void ScaleDown() {
		FindMidPoint();
		if (pyramid.isSet)
			for (int i = 0; i < 5; i++) {
				pyramid.polygon_points[i][0] -= pyramid.midpoints[0];
				pyramid.polygon_points[i][1] -= pyramid.midpoints[1];
				pyramid.polygon_points[i][2] -= pyramid.midpoints[2];
				pyramid.polygon_points[i][0] /= 1.2;
				pyramid.polygon_points[i][1] /= 1.2;
				pyramid.polygon_points[i][2] /= 1.2;
				pyramid.polygon_points[i][0] += pyramid.midpoints[0];
				pyramid.polygon_points[i][1] += pyramid.midpoints[1];
				pyramid.polygon_points[i][2] += pyramid.midpoints[2];
				PerspectiveProjection(pyramid, 5);
			}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.polygon_points[i][0] -= box.midpoints[0];
				box.polygon_points[i][1] -= box.midpoints[1];
				box.polygon_points[i][2] -= box.midpoints[2];
				box.polygon_points[i][0] /= 1.2;
				box.polygon_points[i][1] /= 1.2;
				box.polygon_points[i][2] /= 1.2;
				box.polygon_points[i][0] += box.midpoints[0];
				box.polygon_points[i][1] += box.midpoints[1];
				box.polygon_points[i][2] += box.midpoints[2];
				PerspectiveProjection(box, 8);
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.polygon_points[i][0] -= cube.midpoints[0];
				cube.polygon_points[i][1] -= cube.midpoints[1];
				cube.polygon_points[i][2] -= cube.midpoints[2];
				cube.polygon_points[i][0] /= 1.2;
				cube.polygon_points[i][1] /= 1.2;
				cube.polygon_points[i][2] /= 1.2;
				cube.polygon_points[i][0] += cube.midpoints[0];
				cube.polygon_points[i][1] += cube.midpoints[1];
				cube.polygon_points[i][2] += cube.midpoints[2];
				PerspectiveProjection(cube, 8);
			}
		AddOffset();
	}

	/*
	 * Rotates object around the X axis clockwise
	 */
	public void RotateXClockwise() {
		double angle = 0.10;
		double y, z;
		double Ty, Tz;

		FindMidPoint();

		if (pyramid.isSet) {
			for (int i = 0; i < 5; i++) {
				Ty = pyramid.polygon_points[i][1] - pyramid.midpoints[1];
				Tz = pyramid.polygon_points[i][2] - pyramid.midpoints[2];
				y = Ty * Math.cos(angle) - Tz * Math.sin(angle);
				z = Ty * Math.sin(angle) + Tz * Math.cos(angle);
				y += pyramid.midpoints[1];
				z += pyramid.midpoints[2];
				pyramid.polygon_points[i][1] = y;
				pyramid.polygon_points[i][2] = z;
				PerspectiveProjection(pyramid, 5);
			}

		}
		if (box.isSet) {
			for (int i = 0; i < 8; i++) {
				Ty = box.polygon_points[i][1] - box.midpoints[1];
				Tz = box.polygon_points[i][2] - box.midpoints[2];
				y = Ty * Math.cos(angle) - Tz * Math.sin(angle);
				z = Ty * Math.sin(angle) + Tz * Math.cos(angle);
				y += box.midpoints[1];
				z += box.midpoints[2];
				box.polygon_points[i][1] = y;
				box.polygon_points[i][2] = z;
				PerspectiveProjection(box, 8);
			}
		}
		if (cube.isSet) {
			for (int i = 0; i < 8; i++) {
				Ty = cube.polygon_points[i][1] - cube.midpoints[1];
				Tz = cube.polygon_points[i][2] - cube.midpoints[2];
				y = Ty * Math.cos(angle) - Tz * Math.sin(angle);
				z = Ty * Math.sin(angle) + Tz * Math.cos(angle);
				y += cube.midpoints[1];
				z += cube.midpoints[2];
				cube.polygon_points[i][1] = y;
				cube.polygon_points[i][2] = z;
				PerspectiveProjection(cube, 8);
			}
		}
		AddOffset();
	}

	/*
	 * Rotates object around the X axis counter clockwise
	 */
	public void RotateXCounterClockwise() {
		double angle = -0.10;
		double y, z;
		double Ty, Tz;

		FindMidPoint();

		if (pyramid.isSet) {
			for (int i = 0; i < 5; i++) {
				Ty = pyramid.polygon_points[i][1] - pyramid.midpoints[1];
				Tz = pyramid.polygon_points[i][2] - pyramid.midpoints[2];
				y = Ty * Math.cos(angle) - Tz * Math.sin(angle);
				z = Ty * Math.sin(angle) + Tz * Math.cos(angle);
				y += pyramid.midpoints[1];
				z += pyramid.midpoints[2];
				pyramid.polygon_points[i][1] = y;
				pyramid.polygon_points[i][2] = z;
				PerspectiveProjection(pyramid, 5);
			}

		}
		if (box.isSet) {
			for (int i = 0; i < 8; i++) {
				Ty = box.polygon_points[i][1] - box.midpoints[1];
				Tz = box.polygon_points[i][2] - box.midpoints[2];
				y = Ty * Math.cos(angle) - Tz * Math.sin(angle);
				z = Ty * Math.sin(angle) + Tz * Math.cos(angle);
				y += box.midpoints[1];
				z += box.midpoints[2];
				box.polygon_points[i][1] = y;
				box.polygon_points[i][2] = z;
				PerspectiveProjection(box, 8);
			}
		}
		if (cube.isSet) {
			for (int i = 0; i < 8; i++) {
				Ty = cube.polygon_points[i][1] - cube.midpoints[1];
				Tz = cube.polygon_points[i][2] - cube.midpoints[2];
				y = Ty * Math.cos(angle) - Tz * Math.sin(angle);
				z = Ty * Math.sin(angle) + Tz * Math.cos(angle);
				y += cube.midpoints[1];
				z += cube.midpoints[2];
				cube.polygon_points[i][1] = y;
				cube.polygon_points[i][2] = z;
				PerspectiveProjection(cube, 8);
			}
		}
		AddOffset();
	}

	/*
	 * Rotates object around the Y axis clockwise
	 */
	public void RotateYClockwise() {
		double angle = 0.10;
		double x, z;
		double Tx, Tz;

		FindMidPoint();
		if (pyramid.isSet) {
			for (int i = 0; i < 5; i++) {
				Tx = pyramid.polygon_points[i][0] - pyramid.midpoints[0];
				Tz = pyramid.polygon_points[i][2] - pyramid.midpoints[2];
				x = Tx * Math.cos(angle) - Tz * Math.sin(angle);
				z = Tx * Math.sin(angle) + Tz * Math.cos(angle);
				x += pyramid.midpoints[0];
				z += pyramid.midpoints[2];
				pyramid.polygon_points[i][0] = x;
				pyramid.polygon_points[i][2] = z;
				PerspectiveProjection(pyramid, 5);
			}
		}
		if (box.isSet) {
			for (int i = 0; i < 8; i++) {
				Tx = box.polygon_points[i][0] - box.midpoints[0];
				Tz = box.polygon_points[i][2] - box.midpoints[2];
				x = Tx * Math.cos(angle) - Tz * Math.sin(angle);
				z = Tx * Math.sin(angle) + Tz * Math.cos(angle);
				x += box.midpoints[0];
				z += box.midpoints[2];
				box.polygon_points[i][0] = x;
				box.polygon_points[i][2] = z;
				PerspectiveProjection(box, 8);
			}
		}
		if (cube.isSet) {
			for (int i = 0; i < 8; i++) {
				Tx = cube.polygon_points[i][0] - cube.midpoints[0];
				Tz = cube.polygon_points[i][2] - cube.midpoints[2];
				x = Tx * Math.cos(angle) - Tz * Math.sin(angle);
				z = Tx * Math.sin(angle) + Tz * Math.cos(angle);
				x += cube.midpoints[0];
				z += cube.midpoints[2];
				cube.polygon_points[i][0] = x;
				cube.polygon_points[i][2] = z;
				PerspectiveProjection(cube, 8);
			}
		}
		AddOffset();
	}

	/*
	 * Rotates object around the Y axis counter clockwise
	 */
	public void RotateYCounterClockwise() {
		double angle = -0.10;
		double x, z;
		double Tx, Tz;

		FindMidPoint();
		if (pyramid.isSet) {
			for (int i = 0; i < 5; i++) {
				Tx = pyramid.polygon_points[i][0] - pyramid.midpoints[0];
				Tz = pyramid.polygon_points[i][2] - pyramid.midpoints[2];
				x = Tx * Math.cos(angle) - Tz * Math.sin(angle);
				z = Tx * Math.sin(angle) + Tz * Math.cos(angle);
				x += pyramid.midpoints[0];
				z += pyramid.midpoints[2];
				pyramid.polygon_points[i][0] = x;
				pyramid.polygon_points[i][2] = z;
				PerspectiveProjection(pyramid, 5);
			}
		}
		if (box.isSet) {
			for (int i = 0; i < 8; i++) {
				Tx = box.polygon_points[i][0] - box.midpoints[0];
				Tz = box.polygon_points[i][2] - box.midpoints[2];
				x = Tx * Math.cos(angle) - Tz * Math.sin(angle);
				z = Tx * Math.sin(angle) + Tz * Math.cos(angle);
				x += box.midpoints[0];
				z += box.midpoints[2];
				box.polygon_points[i][0] = x;
				box.polygon_points[i][2] = z;
				PerspectiveProjection(box, 8);
			}
		}
		if (cube.isSet) {
			for (int i = 0; i < 8; i++) {
				Tx = cube.polygon_points[i][0] - cube.midpoints[0];
				Tz = cube.polygon_points[i][2] - cube.midpoints[2];
				x = Tx * Math.cos(angle) - Tz * Math.sin(angle);
				z = Tx * Math.sin(angle) + Tz * Math.cos(angle);
				x += cube.midpoints[0];
				z += cube.midpoints[2];
				cube.polygon_points[i][0] = x;
				cube.polygon_points[i][2] = z;
				PerspectiveProjection(cube, 8);
			}
		}
		AddOffset();
	}

	/*
	 * rotates the object on the Z axis clockwise using the greater than key
	 */
	public void RotateZClockwise() {
		double angle = 0.10;
		double x, y;
		double Tx, Ty;

		FindMidPoint();
		if (pyramid.isSet) {
			for (int i = 0; i < 5; i++) {
				Tx = pyramid.polygon_points[i][0] - pyramid.midpoints[0];
				Ty = pyramid.polygon_points[i][1] - pyramid.midpoints[1];
				x = Tx * Math.cos(angle) - Ty * Math.sin(angle);
				y = Tx * Math.sin(angle) + Ty * Math.cos(angle);
				x += pyramid.midpoints[0];
				y += pyramid.midpoints[1];
				pyramid.polygon_points[i][0] = x;
				pyramid.polygon_points[i][1] = y;
				PerspectiveProjection(pyramid, 5);
			}
		}
		if (box.isSet) {
			for (int i = 0; i < 8; i++) {
				Tx = box.polygon_points[i][0] - box.midpoints[0];
				Ty = box.polygon_points[i][1] - box.midpoints[1];
				x = Tx * Math.cos(angle) - Ty * Math.sin(angle);
				y = Tx * Math.sin(angle) + Ty * Math.cos(angle);
				x += box.midpoints[0];
				y += box.midpoints[1];
				box.polygon_points[i][0] = x;
				box.polygon_points[i][1] = y;
				PerspectiveProjection(box, 8);
			}
		}
		if (cube.isSet) {
			for (int i = 0; i < 8; i++) {
				Tx = cube.polygon_points[i][0] - cube.midpoints[0];
				Ty = cube.polygon_points[i][1] - cube.midpoints[1];
				x = Tx * Math.cos(angle) - Ty * Math.sin(angle);
				y = Tx * Math.sin(angle) + Ty * Math.cos(angle);
				x += cube.midpoints[0];
				y += cube.midpoints[1];
				cube.polygon_points[i][0] = x;
				cube.polygon_points[i][1] = y;
				PerspectiveProjection(cube, 8);
			}
		}
		AddOffset();

	}

	/*
	 * rotates the object on the Z axis counter clockwise using the less than
	 * symbol
	 */
	public void RotateZCounterClockwise() {
		double angle = -0.10;
		double x, y;
		double Tx, Ty;

		FindMidPoint();
		if (pyramid.isSet) {
			for (int i = 0; i < 5; i++) {
				Tx = pyramid.polygon_points[i][0] - pyramid.midpoints[0];
				Ty = pyramid.polygon_points[i][1] - pyramid.midpoints[1];
				x = Tx * Math.cos(angle) - Ty * Math.sin(angle);
				y = Tx * Math.sin(angle) + Ty * Math.cos(angle);
				x += pyramid.midpoints[0];
				y += pyramid.midpoints[1];
				pyramid.polygon_points[i][0] = x;
				pyramid.polygon_points[i][1] = y;
				PerspectiveProjection(pyramid, 5);
			}
		}
		if (box.isSet) {
			for (int i = 0; i < 8; i++) {
				Tx = box.polygon_points[i][0] - box.midpoints[0];
				Ty = box.polygon_points[i][1] - box.midpoints[1];
				x = Tx * Math.cos(angle) - Ty * Math.sin(angle);
				y = Tx * Math.sin(angle) + Ty * Math.cos(angle);
				x += box.midpoints[0];
				y += box.midpoints[1];
				box.polygon_points[i][0] = x;
				box.polygon_points[i][1] = y;
				PerspectiveProjection(box, 8);
			}
		}
		if (cube.isSet) {
			for (int i = 0; i < 8; i++) {
				Tx = cube.polygon_points[i][0] - cube.midpoints[0];
				Ty = cube.polygon_points[i][1] - cube.midpoints[1];
				x = Tx * Math.cos(angle) - Ty * Math.sin(angle);
				y = Tx * Math.sin(angle) + Ty * Math.cos(angle);
				x += cube.midpoints[0];
				y += cube.midpoints[1];
				cube.polygon_points[i][0] = x;
				cube.polygon_points[i][1] = y;
				PerspectiveProjection(cube, 8);
			}
		}
		AddOffset();

	}

	/*
	 * Finds the Mid point of each object for in place rotation
	 */
	public void FindMidPoint() {
		double minX, maxX, minY, maxY, minZ, maxZ;

		if (pyramid.isSet) {
			minX = pyramid.polygon_points[0][0];
			maxX = pyramid.polygon_points[0][0];
			minY = pyramid.polygon_points[0][1];
			maxY = pyramid.polygon_points[0][1];
			minZ = pyramid.polygon_points[0][2];
			maxZ = pyramid.polygon_points[0][2];

			for (int i = 0; i < 5; i++) {
				if (minX < pyramid.polygon_points[i][0])
					minX = pyramid.polygon_points[i][0];
				if (maxX > pyramid.polygon_points[i][0])
					maxX = pyramid.polygon_points[i][0];
				if (minY < pyramid.polygon_points[i][1])
					minY = pyramid.polygon_points[i][1];
				if (maxY > pyramid.polygon_points[i][1])
					maxY = pyramid.polygon_points[i][1];
				if (minZ < pyramid.polygon_points[i][2])
					minZ = pyramid.polygon_points[i][2];
				if (maxZ > pyramid.polygon_points[i][2])
					maxZ = pyramid.polygon_points[i][2];
			}

			pyramid.midpoints[0] = (minX + maxX) / 2;
			pyramid.midpoints[1] = (minY + maxY) / 2;
			pyramid.midpoints[2] = (minZ + maxZ) / 2;
		}
		if (box.isSet) {
			minX = box.polygon_points[0][0];
			maxX = box.polygon_points[0][0];
			minY = box.polygon_points[0][1];
			maxY = box.polygon_points[0][1];
			minZ = box.polygon_points[0][2];
			maxZ = box.polygon_points[0][2];

			for (int i = 0; i < 8; i++) {
				if (minX < box.polygon_points[i][0])
					minX = box.polygon_points[i][0];
				if (maxX > box.polygon_points[i][0])
					maxX = box.polygon_points[i][0];
				if (minY < box.polygon_points[i][1])
					minY = box.polygon_points[i][1];
				if (maxY > box.polygon_points[i][1])
					maxY = box.polygon_points[i][1];
				if (minZ < box.polygon_points[i][2])
					minZ = box.polygon_points[i][2];
				if (maxZ > box.polygon_points[i][2])
					maxZ = box.polygon_points[i][2];
			}
			box.midpoints[0] = (minX + maxX) / 2;
			box.midpoints[1] = (minY + maxY) / 2;
			box.midpoints[2] = (minZ + maxZ) / 2;
		}
		if (cube.isSet) {
			minX = cube.polygon_points[0][0];
			maxX = cube.polygon_points[0][0];
			minY = cube.polygon_points[0][1];
			maxY = cube.polygon_points[0][1];
			minZ = cube.polygon_points[0][2];
			maxZ = cube.polygon_points[0][2];

			for (int i = 0; i < 8; i++) {
				if (minX < cube.polygon_points[i][0])
					minX = cube.polygon_points[i][0];
				if (maxX > cube.polygon_points[i][0])
					maxX = cube.polygon_points[i][0];
				if (minY < cube.polygon_points[i][1])
					minY = cube.polygon_points[i][1];
				if (maxY > cube.polygon_points[i][1])
					maxY = cube.polygon_points[i][1];
				if (minZ < cube.polygon_points[i][2])
					minZ = cube.polygon_points[i][2];
				if (maxZ > cube.polygon_points[i][2])
					maxZ = cube.polygon_points[i][2];
			}
			cube.midpoints[0] = (minX + maxX) / 2;
			cube.midpoints[1] = (minY + maxY) / 2;
			cube.midpoints[2] = (minZ + maxZ) / 2;
		}
	}

	/*
	 * Calculates the perspective project points for converting 3D to 2D
	 */
	public void PerspectiveProjection(Obj obj, int points) {

		for (int i = 0; i < points; i++) {
			obj.screen_points[i][0] = (eye_z * obj.polygon_points[i][0])
					/ (eye_z + obj.polygon_points[i][2]);
			obj.screen_points[i][1] = (eye_z * obj.polygon_points[i][1])
					/ (eye_z + obj.polygon_points[i][2]);
			obj.screen_points[i][2] = obj.polygon_points[i][2]
					/ (eye_z + obj.polygon_points[i][2]);
		}
	}

	/*
	 * This function is called by pressing the "C" key. This function will
	 * toggle back face culling and remove faces that should not be shown
	 */
	public void BackFaceCullingOn() {
		if (!pyramid.cullingIsSet && pyramid.isSet)
			pyramid.cullingIsSet = true;
		if (!box.cullingIsSet && box.isSet)
			box.cullingIsSet = true;
		if (!cube.cullingIsSet && cube.isSet)
			cube.cullingIsSet = true;

	}

	/*
	 * This function is called by pressing the "C" key. This function will
	 * toggle back face culling and remove faces that should not be shown
	 */
	public void BackFaceCullingOff() {
		if (pyramid.isSet && pyramid.cullingIsSet)
			pyramid.cullingIsSet = false;
		if (box.isSet && box.cullingIsSet)
			box.cullingIsSet = false;
		if (cube.isSet && cube.cullingIsSet)
			cube.cullingIsSet = false;

	}

	/*
	 * This function is called by pressing the "P" key. This function will
	 * toggle polygon filling
	 */
	public void FillObjectOn() {
		if (!pyramid.fillingIsSet && pyramid.isSet)
			pyramid.fillingIsSet = true;
		if (!box.fillingIsSet && box.isSet)
			box.fillingIsSet = true;
		if (!cube.fillingIsSet && cube.isSet)
			cube.fillingIsSet = true;

	}

	/*
	 * This function is called by pressing the "O" key. This function will
	 * toggle polygon filling
	 */
	public void FillObjectOff() {
		if (pyramid.isSet && pyramid.fillingIsSet)
			pyramid.fillingIsSet = false;
		if (box.isSet && box.fillingIsSet)
			box.fillingIsSet = false;
		if (cube.isSet && cube.fillingIsSet)
			cube.fillingIsSet = false;

	}

	/*
	 * This function is called by pressing the "W" key. This function will
	 * toggle wire frame of each of the objects
	 */
	public void ToggleWireframeOn() {
		if (pyramid.cullingIsSet && pyramid.isSet)
			pyramid.cullingIsSet = false;
		if (box.cullingIsSet && box.isSet)
			box.cullingIsSet = false;
		if (cube.cullingIsSet && cube.isSet)
			cube.cullingIsSet = false;
		if (!pyramid.wireFrameSet && pyramid.isSet)
			pyramid.wireFrameSet = true;
		if (!box.wireFrameSet && box.isSet)
			box.wireFrameSet = true;
		if (!cube.wireFrameSet && cube.isSet)
			cube.wireFrameSet = true;
	}

	/*
	 * This function is called by pressing the "Q" key. This function will
	 * toggle wire frame of each of the objects
	 */
	public void ToggleWireframeOff() {
		if (pyramid.cullingIsSet && pyramid.isSet)
			pyramid.cullingIsSet = false;
		if (box.cullingIsSet && box.isSet)
			box.cullingIsSet = false;
		if (cube.cullingIsSet && cube.isSet)
			cube.cullingIsSet = false;
		if (pyramid.wireFrameSet && pyramid.isSet)
			pyramid.wireFrameSet = false;
		if (box.wireFrameSet && box.isSet)
			box.wireFrameSet = false;
		if (cube.wireFrameSet && cube.isSet)
			cube.wireFrameSet = false;
	}

	/*
	 * This function calculates the surface normal for the back face culling the
	 * back face culling on, only exposing the faces that are in front.
	 */
	public void CalculateSurfaceNormal() {
		double[] alpha = new double[3]; // stores the alpha values
		double[] beta = new double[3]; // stores the beta values
		double[] normal = new double[3]; // stores the surface normal values
		double plane_offset; // stores the plane offset value

		if (pyramid.isSet) {
			alpha[0] = pyramid.polygon_points[1][0] // alpha X
					- pyramid.polygon_points[0][0];
			alpha[1] = pyramid.polygon_points[1][1] // alpha Y
					- pyramid.polygon_points[0][1];
			alpha[2] = pyramid.polygon_points[1][2] // alpha Z
					- pyramid.polygon_points[0][2];

			beta[0] = pyramid.polygon_points[2][0] // beta X
					- pyramid.polygon_points[0][0];
			beta[1] = pyramid.polygon_points[2][1] // beta Y
					- pyramid.polygon_points[0][1];
			beta[2] = pyramid.polygon_points[2][2] // beta Z
					- pyramid.polygon_points[0][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2]; // surface
																	// noraml X
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]); // surface
																	// noraml Y
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1]; // surface
																	// noraml Z

			plane_offset = pyramid.polygon_points[0][0] * normal[0]
					- pyramid.polygon_points[0][1] * normal[1]
					+ pyramid.polygon_points[0][2] * normal[2];

			double test_draw = (-eye_z * normal[2]) - plane_offset;

			// deteremines wether the polygon will or will not be drawn
			// This is the front face
			if (test_draw < 0) {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[0] = false;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[0] = false;
			} else {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[0] = true;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[0] = true;
			}

			alpha[0] = pyramid.polygon_points[2][0]
					- pyramid.polygon_points[0][0];
			alpha[1] = pyramid.polygon_points[2][1]
					- pyramid.polygon_points[0][1];
			alpha[2] = pyramid.polygon_points[2][2]
					- pyramid.polygon_points[0][2];

			beta[0] = pyramid.polygon_points[4][0]
					- pyramid.polygon_points[0][0];
			beta[1] = pyramid.polygon_points[4][1]
					- pyramid.polygon_points[0][1];
			beta[2] = pyramid.polygon_points[4][2]
					- pyramid.polygon_points[0][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = pyramid.polygon_points[0][0] * normal[0]
					- pyramid.polygon_points[0][1] * normal[1]
					+ pyramid.polygon_points[0][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			// deteremines wether the polygon will or will not be drawn
			// This is the left face
			if (test_draw < 0) {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[1] = false;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[1] = false;
			} else {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[1] = true;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[1] = true;
			}

			alpha[0] = pyramid.polygon_points[4][0]
					- pyramid.polygon_points[0][0];
			alpha[1] = pyramid.polygon_points[4][1]
					- pyramid.polygon_points[0][1];
			alpha[2] = pyramid.polygon_points[4][2]
					- pyramid.polygon_points[0][2];

			beta[0] = pyramid.polygon_points[3][0]
					- pyramid.polygon_points[0][0];
			beta[1] = pyramid.polygon_points[3][1]
					- pyramid.polygon_points[0][1];
			beta[2] = pyramid.polygon_points[3][2]
					- pyramid.polygon_points[0][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = pyramid.polygon_points[0][0] * normal[0]
					- pyramid.polygon_points[0][1] * normal[1]
					+ pyramid.polygon_points[0][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			// deteremines wether the polygon will or will not be drawn
			// This is the back face
			if (test_draw < 0) {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[2] = false;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[2] = false;
			} else {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[2] = true;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[2] = true;
			}

			alpha[0] = pyramid.polygon_points[3][0]
					- pyramid.polygon_points[0][0];
			alpha[1] = pyramid.polygon_points[3][1]
					- pyramid.polygon_points[0][1];
			alpha[2] = pyramid.polygon_points[3][2]
					- pyramid.polygon_points[0][2];

			beta[0] = pyramid.polygon_points[1][0]
					- pyramid.polygon_points[0][0];
			beta[1] = pyramid.polygon_points[1][1]
					- pyramid.polygon_points[0][1];
			beta[2] = pyramid.polygon_points[1][2]
					- pyramid.polygon_points[0][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = pyramid.polygon_points[0][0] * normal[0]
					- pyramid.polygon_points[0][1] * normal[1]
					+ pyramid.polygon_points[0][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			// deteremines wether the polygon will or will not be drawn
			// This is the right face
			if (test_draw < 0) {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[3] = false;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[3] = false;
			} else {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[3] = true;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[3] = true;
			}

			alpha[0] = pyramid.polygon_points[3][0]
					- pyramid.polygon_points[1][0];
			alpha[1] = pyramid.polygon_points[3][1]
					- pyramid.polygon_points[1][1];
			alpha[2] = pyramid.polygon_points[3][2]
					- pyramid.polygon_points[1][2];

			beta[0] = pyramid.polygon_points[4][0]
					- pyramid.polygon_points[1][0];
			beta[1] = pyramid.polygon_points[4][1]
					- pyramid.polygon_points[1][1];
			beta[2] = pyramid.polygon_points[4][2]
					- pyramid.polygon_points[1][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = pyramid.polygon_points[1][0] * normal[0]
					- pyramid.polygon_points[1][1] * normal[1]
					+ pyramid.polygon_points[1][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			// deteremines wether the polygon will or will not be drawn
			// This is the bottom face (base)
			if (test_draw < 0) {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[4] = false;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[4] = false;
			} else {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[4] = true;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[4] = true;
			}

			alpha[0] = pyramid.polygon_points[3][0]
					- pyramid.polygon_points[2][0];
			alpha[1] = pyramid.polygon_points[3][1]
					- pyramid.polygon_points[2][1];
			alpha[2] = pyramid.polygon_points[3][2]
					- pyramid.polygon_points[2][2];

			beta[0] = pyramid.polygon_points[4][0]
					- pyramid.polygon_points[2][0];
			beta[1] = pyramid.polygon_points[4][1]
					- pyramid.polygon_points[2][1];
			beta[2] = pyramid.polygon_points[4][2]
					- pyramid.polygon_points[2][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = pyramid.polygon_points[2][0] * normal[0]
					- pyramid.polygon_points[2][1] * normal[1]
					+ pyramid.polygon_points[2][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			// deteremines wether the polygon will or will not be drawn
			// This is the bottom face (base)
			if (test_draw < 0) {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[5] = false;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[5] = false;
			} else {
				if (pyramid.cullingIsSet)
					pyramid.drawCulling[5] = true;
				if (pyramid.fillingIsSet)
					pyramid.drawFilling[5] = true;
			}

		}

		if (box.isSet) {
			alpha[0] = box.polygon_points[3][0] - box.polygon_points[1][0];
			alpha[1] = box.polygon_points[3][1] - box.polygon_points[1][1];
			alpha[2] = box.polygon_points[3][2] - box.polygon_points[1][2];

			beta[0] = box.polygon_points[2][0] - box.polygon_points[1][0];
			beta[1] = box.polygon_points[2][1] - box.polygon_points[1][1];
			beta[2] = box.polygon_points[2][2] - box.polygon_points[1][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = box.polygon_points[1][0] * normal[0]
					- box.polygon_points[1][1] * normal[1]
					+ box.polygon_points[1][2] * normal[2];

			double test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				box.drawCulling[0] = false;
			} else {
				box.drawCulling[0] = true;
			}

			alpha[0] = box.polygon_points[2][0] - box.polygon_points[0][0];
			alpha[1] = box.polygon_points[2][1] - box.polygon_points[0][1];
			alpha[2] = box.polygon_points[2][2] - box.polygon_points[0][2];

			beta[0] = box.polygon_points[6][0] - box.polygon_points[0][0];
			beta[1] = box.polygon_points[6][1] - box.polygon_points[0][1];
			beta[2] = box.polygon_points[6][2] - box.polygon_points[0][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = box.polygon_points[0][0] * normal[0]
					- box.polygon_points[0][1] * normal[1]
					+ box.polygon_points[0][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				box.drawCulling[1] = false;
			} else {
				box.drawCulling[1] = true;
			}

			alpha[0] = box.polygon_points[6][0] - box.polygon_points[4][0];
			alpha[1] = box.polygon_points[6][1] - box.polygon_points[4][1];
			alpha[2] = box.polygon_points[6][2] - box.polygon_points[4][2];

			beta[0] = box.polygon_points[7][0] - box.polygon_points[4][0];
			beta[1] = box.polygon_points[7][1] - box.polygon_points[4][1];
			beta[2] = box.polygon_points[7][2] - box.polygon_points[4][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = box.polygon_points[4][0] * normal[0]
					- box.polygon_points[4][1] * normal[1]
					+ box.polygon_points[4][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				box.drawCulling[2] = false;
			} else {
				box.drawCulling[2] = true;
			}

			alpha[0] = box.polygon_points[7][0] - box.polygon_points[1][0];
			alpha[1] = box.polygon_points[7][1] - box.polygon_points[1][1];
			alpha[2] = box.polygon_points[7][2] - box.polygon_points[1][2];

			beta[0] = box.polygon_points[3][0] - box.polygon_points[1][0];
			beta[1] = box.polygon_points[3][1] - box.polygon_points[1][1];
			beta[2] = box.polygon_points[3][2] - box.polygon_points[1][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = box.polygon_points[1][0] * normal[0]
					- box.polygon_points[1][1] * normal[1]
					+ box.polygon_points[1][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				box.drawCulling[3] = false;
			} else {
				box.drawCulling[3] = true;
			}

			alpha[0] = box.polygon_points[7][0] - box.polygon_points[3][0];
			alpha[1] = box.polygon_points[7][1] - box.polygon_points[3][1];
			alpha[2] = box.polygon_points[7][2] - box.polygon_points[3][2];

			beta[0] = box.polygon_points[6][0] - box.polygon_points[3][0];
			beta[1] = box.polygon_points[6][1] - box.polygon_points[3][1];
			beta[2] = box.polygon_points[6][2] - box.polygon_points[3][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = box.polygon_points[3][0] * normal[0]
					- box.polygon_points[3][1] * normal[1]
					+ box.polygon_points[3][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				box.drawCulling[4] = false;
			} else {
				box.drawCulling[4] = true;
			}

			alpha[0] = box.polygon_points[1][0] - box.polygon_points[5][0];
			alpha[1] = box.polygon_points[1][1] - box.polygon_points[5][1];
			alpha[2] = box.polygon_points[1][2] - box.polygon_points[5][2];

			beta[0] = box.polygon_points[0][0] - box.polygon_points[5][0];
			beta[1] = box.polygon_points[0][1] - box.polygon_points[5][1];
			beta[2] = box.polygon_points[0][2] - box.polygon_points[5][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = box.polygon_points[5][0] * normal[0]
					- box.polygon_points[5][1] * normal[1]
					+ box.polygon_points[5][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				box.drawCulling[5] = false;
			} else {
				box.drawCulling[5] = true;
			}

		}

		if (cube.isSet) {
			alpha[0] = cube.polygon_points[3][0] - cube.polygon_points[1][0];
			alpha[1] = cube.polygon_points[3][1] - cube.polygon_points[1][1];
			alpha[2] = cube.polygon_points[3][2] - cube.polygon_points[1][2];

			beta[0] = cube.polygon_points[2][0] - cube.polygon_points[1][0];
			beta[1] = cube.polygon_points[2][1] - cube.polygon_points[1][1];
			beta[2] = cube.polygon_points[2][2] - cube.polygon_points[1][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = cube.polygon_points[1][0] * normal[0]
					- cube.polygon_points[1][1] * normal[1]
					+ cube.polygon_points[1][2] * normal[2];

			double test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				cube.drawCulling[0] = false;
			} else {
				cube.drawCulling[0] = true;
			}

			alpha[0] = cube.polygon_points[2][0] - cube.polygon_points[0][0];
			alpha[1] = cube.polygon_points[2][1] - cube.polygon_points[0][1];
			alpha[2] = cube.polygon_points[2][2] - cube.polygon_points[0][2];

			beta[0] = cube.polygon_points[6][0] - cube.polygon_points[0][0];
			beta[1] = cube.polygon_points[6][1] - cube.polygon_points[0][1];
			beta[2] = cube.polygon_points[6][2] - cube.polygon_points[0][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = cube.polygon_points[0][0] * normal[0]
					- cube.polygon_points[0][1] * normal[1]
					+ cube.polygon_points[0][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				cube.drawCulling[1] = false;
			} else {
				cube.drawCulling[1] = true;
			}

			alpha[0] = cube.polygon_points[6][0] - cube.polygon_points[4][0];
			alpha[1] = cube.polygon_points[6][1] - cube.polygon_points[4][1];
			alpha[2] = cube.polygon_points[6][2] - cube.polygon_points[4][2];

			beta[0] = cube.polygon_points[7][0] - cube.polygon_points[4][0];
			beta[1] = cube.polygon_points[7][1] - cube.polygon_points[4][1];
			beta[2] = cube.polygon_points[7][2] - cube.polygon_points[4][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = cube.polygon_points[4][0] * normal[0]
					- cube.polygon_points[4][1] * normal[1]
					+ cube.polygon_points[4][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				cube.drawCulling[2] = false;
			} else {
				cube.drawCulling[2] = true;
			}

			alpha[0] = cube.polygon_points[7][0] - cube.polygon_points[1][0];
			alpha[1] = cube.polygon_points[7][1] - cube.polygon_points[1][1];
			alpha[2] = cube.polygon_points[7][2] - cube.polygon_points[1][2];

			beta[0] = cube.polygon_points[3][0] - cube.polygon_points[1][0];
			beta[1] = cube.polygon_points[3][1] - cube.polygon_points[1][1];
			beta[2] = cube.polygon_points[3][2] - cube.polygon_points[1][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = cube.polygon_points[1][0] * normal[0]
					- cube.polygon_points[1][1] * normal[1]
					+ cube.polygon_points[1][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				cube.drawCulling[3] = false;
			} else {
				cube.drawCulling[3] = true;
			}

			alpha[0] = cube.polygon_points[7][0] - cube.polygon_points[3][0];
			alpha[1] = cube.polygon_points[7][1] - cube.polygon_points[3][1];
			alpha[2] = cube.polygon_points[7][2] - cube.polygon_points[3][2];

			beta[0] = cube.polygon_points[6][0] - cube.polygon_points[3][0];
			beta[1] = cube.polygon_points[6][1] - cube.polygon_points[3][1];
			beta[2] = cube.polygon_points[6][2] - cube.polygon_points[3][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = cube.polygon_points[3][0] * normal[0]
					- cube.polygon_points[3][1] * normal[1]
					+ cube.polygon_points[3][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				cube.drawCulling[4] = false;
			} else {
				cube.drawCulling[4] = true;
			}

			alpha[0] = cube.polygon_points[1][0] - cube.polygon_points[5][0];
			alpha[1] = cube.polygon_points[1][1] - cube.polygon_points[5][1];
			alpha[2] = cube.polygon_points[1][2] - cube.polygon_points[5][2];

			beta[0] = cube.polygon_points[0][0] - cube.polygon_points[5][0];
			beta[1] = cube.polygon_points[0][1] - cube.polygon_points[5][1];
			beta[2] = cube.polygon_points[0][2] - cube.polygon_points[5][2];

			normal[0] = alpha[1] * beta[2] - beta[1] * alpha[2];
			normal[1] = (alpha[0] * beta[2] - beta[0] * alpha[2]);
			normal[2] = alpha[0] * beta[1] - beta[0] * alpha[1];

			plane_offset = cube.polygon_points[5][0] * normal[0]
					- cube.polygon_points[5][1] * normal[1]
					+ cube.polygon_points[5][2] * normal[2];

			test_draw = (-eye_z * normal[2]) - plane_offset;

			if (test_draw < 0) {
				cube.drawCulling[5] = false;
			} else {
				cube.drawCulling[5] = true;
			}

		}
	}
	
	/*
	 * This function does the calculation for polygon filling
	 */
	public void CalculateFill(double a0, double b0, double c0, double a1,
			double b1, double c1, double a2, double b2, double c2,
			Graphics2D g, Obj obj) {

		// crates the arrays needed to store the values 
		double[] startpointsY = new double[3];
		double[] endpointsY = new double[3];
		double[] startpointsZ = new double[3];
		double[] endpointsZ = new double[3];
		ArrayList<Segment> Global_ET = new ArrayList<Segment>();
		
		// These are the x, y, and z value of the points that are passed into the function
		segP0P1.x0 = a0; // P0.x
		segP0P1.y0 = b0; // P0.y
		segP0P1.z0 = c0; // P0.z
		segP0P1.x1 = a1; // P1.x
		segP0P1.y1 = b1; // P1.y
		segP0P1.z1 = c1; // P1.z
		segP0P2.x0 = a0; // P0.x
		segP0P2.y0 = b0; // P0.y
		segP0P2.z0 = c0; // P0.z
		segP0P2.x1 = a2; // P2.x
		segP0P2.y1 = b2; // P2.y
		segP0P2.z1 = c2; // P2.z
		segP1P2.x0 = a1; // P1.x
		segP1P2.y0 = b1; // P1.y
		segP1P2.z0 = c1; // P1.z
		segP1P2.x1 = a2; // P2.x
		segP1P2.y1 = b2; // P2.y
		segP1P2.z1 = c2; // P2.z

		// Here are the points after they have been projected and prospected
		segP0P1.x0 = (eye_z * segP0P1.x0) / (eye_z + segP0P1.z0);
		segP0P1.y0 = (eye_z * segP0P1.y0) / (eye_z + segP0P1.z0);
		segP0P1.z0 = segP0P1.z0 / (eye_z + segP0P1.z1);
		segP0P1.x1 = (eye_z * segP0P1.x1) / (eye_z + segP0P1.z1);
		segP0P1.y1 = (eye_z * segP0P1.y1) / (eye_z + segP0P1.z1);
		segP0P1.z1 = segP0P1.z1 / (eye_z + segP0P1.z1);
		segP0P2.x0 = (eye_z * segP0P2.x0) / (eye_z + segP0P2.z0);
		segP0P2.y0 = (eye_z * segP0P2.y0) / (eye_z + segP0P2.z0);
		segP0P2.z0 = segP0P2.z0 / (eye_z + segP0P2.z1);
		segP0P2.x1 = (eye_z * segP0P2.x1) / (eye_z + segP0P2.z1);
		segP0P2.y1 = (eye_z * segP0P2.y1) / (eye_z + segP0P2.z1);
		segP0P2.z1 = segP0P2.z1 / (eye_z + segP0P2.z1);
		segP1P2.x0 = (eye_z * segP1P2.x0) / (eye_z + segP1P2.z0);
		segP1P2.y0 = (eye_z * segP1P2.y0) / (eye_z + segP1P2.z0);
		segP1P2.z0 = segP1P2.z0 / (eye_z + segP1P2.z1);
		segP1P2.x1 = (eye_z * segP1P2.x1) / (eye_z + segP1P2.z1);
		segP1P2.y1 = (eye_z * segP1P2.y1) / (eye_z + segP1P2.z1);
		segP1P2.z1 = segP1P2.z1 / (eye_z + segP1P2.z1);

		// Finds the find and max values of each line segement
		segP0P1.maxY = segP0P1.y0;
		segP0P1.initial_x = segP0P1.x0;
		segP0P1.maxX = segP0P1.x0;
		segP0P1.maxZ = segP0P1.z0;

		if (segP0P1.maxY < segP0P1.y1) {
			segP0P1.maxY = segP0P1.y1;
			segP0P1.initial_x = segP0P1.x1;
			segP0P1.maxX = segP0P1.x1;
			segP0P1.maxZ = segP0P1.z1;
		}

		segP0P1.minY = segP0P1.y0;
		segP0P1.minX = segP0P1.x1;
		segP0P1.minZ = segP0P1.z1;

		if (segP0P1.minY > segP0P1.y1){
			segP0P1.minY = segP0P1.y1;
			segP0P1.minX = segP0P1.x0;
			segP0P1.minZ = segP0P1.z0;
		}

		segP0P1.dx = -((segP0P1.x1 - segP0P1.x0) / (segP0P1.y1 - segP0P1.y0));

		segP0P2.maxY = segP0P2.y0;
		segP0P2.initial_x = segP0P2.x0;
		segP0P2.maxX = segP0P2.x0;
		segP0P2.maxZ = segP0P2.z0;

		if (segP0P2.maxY < segP0P2.y1) {
			segP0P2.maxY = segP0P2.y1;
			segP0P2.initial_x = segP0P2.x1;
			segP0P2.maxX = segP0P2.x1;
			segP0P2.maxZ = segP0P2.z1;
		}

		segP0P2.minY = segP0P2.y0;
		segP0P2.minX = segP0P2.x1;
		segP0P2.minZ = segP0P2.z1;

		if (segP0P2.minY > segP0P2.y1){
			segP0P2.minY = segP0P2.y1;
			segP0P2.minX = segP0P2.x0;
			segP0P2.minZ = segP0P2.z0;
		}

		segP0P2.dx = -((segP0P2.x1 - segP0P2.x0) / (segP0P2.y1 - segP0P2.y0));

		segP1P2.maxY = segP1P2.y0;
		segP1P2.initial_x = segP1P2.x0;
		segP1P2.maxX = segP1P2.x0;
		segP1P2.maxZ = segP1P2.z0;

		if (segP1P2.maxY < segP1P2.y1) {
			segP1P2.maxY = segP1P2.y1;
			segP1P2.initial_x = segP1P2.x1;
			segP1P2.maxX = segP1P2.x1;
			segP1P2.maxZ = segP1P2.z1;
		}

		segP1P2.minY = segP1P2.y0;
		segP1P2.minX = segP1P2.x1;
		segP1P2.minZ = segP1P2.z1;

		if (segP1P2.minY > segP1P2.y1){
			segP1P2.minY = segP1P2.y1;
			segP1P2.minX = segP1P2.x0;
			segP1P2.minZ = segP1P2.z0;
		}

		segP1P2.dx = -((segP1P2.x1 - segP1P2.x0) / (segP1P2.y1 - segP1P2.y0));

		Segment[] seg_list = new Segment[3];

		seg_list[0] = segP0P1;
		seg_list[1] = segP0P2;
		seg_list[2] = segP1P2;

		// Find the starting and end Y values to prepare the pixel drawing
		if (seg_list[0].maxY >= seg_list[1].maxY
				&& seg_list[0].maxY >= seg_list[2].maxY
				&& seg_list[0].minY >= seg_list[1].minY
				&& seg_list[0].minY >= seg_list[2].minY) {
			startpointsY[0] = seg_list[0].maxY;
			endpointsY[0] = seg_list[0].minY;
			Global_ET.add(seg_list[0]);

			if (seg_list[1].maxY >= seg_list[2].maxY
					&& seg_list[1].minY >= seg_list[2].minY) {
				startpointsY[1] = seg_list[1].maxY;
				endpointsY[1] = seg_list[1].minY;
				startpointsY[2] = seg_list[2].maxY;
				endpointsY[2] = seg_list[2].minY;
				Global_ET.add(seg_list[1]);
				Global_ET.add(seg_list[2]);
			} else {
				startpointsY[1] = seg_list[2].maxY;
				endpointsY[1] = seg_list[2].minY;
				startpointsY[2] = seg_list[1].maxY;
				endpointsY[2] = seg_list[1].minY;
				Global_ET.add(seg_list[2]);
				Global_ET.add(seg_list[1]);
			}

		} else if (seg_list[1].maxY >= seg_list[0].maxY
				&& seg_list[1].maxY >= seg_list[2].maxY
				&& seg_list[1].minY >= seg_list[0].minY
				&& seg_list[1].minY >= seg_list[2].minY) {
			startpointsY[0] = seg_list[1].maxY;
			endpointsY[0] = seg_list[1].minY;
			Global_ET.add(seg_list[1]);

			if (seg_list[2].maxY >= seg_list[0].maxY
					&& seg_list[2].minY >= seg_list[0].minY) {
				startpointsY[1] = seg_list[2].maxY;
				endpointsY[1] = seg_list[2].minY;
				startpointsY[2] = seg_list[0].maxY;
				endpointsY[2] = seg_list[0].minY;
				Global_ET.add(seg_list[2]);
				Global_ET.add(seg_list[0]);
			} else {
				startpointsY[1] = seg_list[0].maxY;
				endpointsY[1] = seg_list[0].minY;
				startpointsY[2] = seg_list[2].maxY;
				endpointsY[2] = seg_list[2].minY;
				Global_ET.add(seg_list[0]);
				Global_ET.add(seg_list[2]);
			}

		} else if (seg_list[2].maxY >= seg_list[0].maxY
				&& seg_list[2].maxY >= seg_list[1].maxY
				&& seg_list[2].minY >= seg_list[0].minY
				&& seg_list[2].minY >= seg_list[1].minY) {
			startpointsY[0] = seg_list[2].maxY;
			endpointsY[0] = seg_list[2].minY;
			Global_ET.add(seg_list[2]);

			if (seg_list[0].maxY >= seg_list[1].maxY
					&& seg_list[0].minY >= seg_list[1].minY) {
				startpointsY[1] = seg_list[0].maxY;
				endpointsY[1] = seg_list[0].minY;
				startpointsY[2] = seg_list[1].maxY;
				endpointsY[2] = seg_list[1].minY;
				Global_ET.add(seg_list[0]);
				Global_ET.add(seg_list[1]);
			} else {
				startpointsY[1] = seg_list[1].maxY;
				endpointsY[1] = seg_list[1].minY;
				startpointsY[2] = seg_list[0].maxY;
				endpointsY[2] = seg_list[0].minY;
				Global_ET.add(seg_list[1]);
				Global_ET.add(seg_list[0]);
			}
		}
		
		// grabs the top maxY value and the minimum minY value
		double start_scan_line = Global_ET.get(0).maxY;
		double end_scan_line = Global_ET.get(2).minY;
		// stores the starting and ending X values to start drawing with
		double[] drawX = new double[3];

		drawX[0] = Global_ET.get(0).initial_x;
		drawX[1] = Global_ET.get(1).initial_x;
		drawX[2] = Global_ET.get(2).initial_x;

		// setst he color for each obj depending on what is it
		if (obj.name == "box")
			g.setColor(new Color(232, 223, 9));
		if (obj.name == "pyramid")
			g.setColor(new Color(100, 73, 200));
		if (obj.name == "cube")
			g.setColor(new Color(15, 125, 90));

		// g.setColor(new Color(randR, randG, randB));
		// g.setColor(new Color(232, 223, 9));
		// RANDOM color
		if (rave > 0)
			g.setColor(new Color(randInt(0, 255), randInt(0, 255), randInt(0,
					255)));
		
		// zBuffer stuff that doesnt work
		double[] A = new double[WIDTH];
		double[] B = new double[WIDTH];
		double[] C = new double[WIDTH];
		
//		A[0] = Global_ET.get(0).maxZ + (Global_ET.get(1).maxZ - Global_ET.get(0).maxZ) / (Global_ET.get(1).maxY - Global_ET.get(0).maxY);
//		B[0] = Global_ET.get(0).maxZ + (Global_ET.get(2).maxZ - Global_ET.get(0).maxZ) / (Global_ET.get(2).maxY - Global_ET.get(0).maxY);
//		C[0] = Global_ET.get(1).maxZ + (Global_ET.get(2).maxZ - Global_ET.get(1).maxZ) / (Global_ET.get(2).maxY - Global_ET.get(1).maxY);
//		
//		for(int i = 1; i < (int)(Global_ET.get(1).maxZ - Global_ET.get(0).maxZ); i++)
//			A[i] = A[i] + ( (Global_ET.get(1).maxZ - Global_ET.get(0).maxZ) /(Global_ET.get(1).maxY - Global_ET.get(0).maxY));
//		for(int i = 1; i < (int)(Global_ET.get(2).maxZ - Global_ET.get(0).maxZ); i++)
//			B[i] = B[i] + ( (Global_ET.get(2).maxZ - Global_ET.get(0).maxZ) /(Global_ET.get(2).maxY - Global_ET.get(0).maxY));
//		for(int i = 1; i < (int)(Global_ET.get(2).maxZ - Global_ET.get(1).maxZ); i++)
//			C[i] = C[i] + ( (Global_ET.get(2).maxZ - Global_ET.get(1).maxZ) /(Global_ET.get(2).maxY - Global_ET.get(1).maxY));
		
		/*
		 * Here is where the magic happens. The loop with run from the maxY to the minY
		 * drawing each pixel as the scan line is decressed. 
		 */
		for (int i = (int) start_scan_line; i > end_scan_line; i--) {
			if ((i <= startpointsY[1] && i >= endpointsY[1])
					&& (i <= startpointsY[0] && i >= endpointsY[0])) {
				if (drawX[0] > drawX[1]) {
					for (int j = (int) drawX[1]; j < drawX[0]; j++) {
						int c = j;
						if(c < 0)
							c = 0;
						if(c > 255)
							c = 255;
						g.setColor(new Color(100, 73, c));
						obj.fill_screen_points[0][0] = (j + obj.offset[0]);
						obj.fill_screen_points[0][1] = (obj.offset[1] - i);
						g.drawLine((int) obj.fill_screen_points[0][0],
								(int) obj.fill_screen_points[0][1],
								(int) obj.fill_screen_points[0][0],
								(int) obj.fill_screen_points[0][1]);
					}
				} else {
					for (int j = (int) drawX[0]; j < drawX[1]; j++) {
						int c = j;
						if(c < 0)
							c = 0;
						if(c > 255)
							c = 255;
						g.setColor(new Color(100, 73, c));
						obj.fill_screen_points[0][0] = (j + obj.offset[0]);
						obj.fill_screen_points[0][1] = (obj.offset[1] - i);
						g.drawLine((int) obj.fill_screen_points[0][0],
								(int) obj.fill_screen_points[0][1],
								(int) obj.fill_screen_points[0][0],
								(int) obj.fill_screen_points[0][1]);
					}
				}
				drawX[0] += Global_ET.get(0).dx;
				drawX[1] += Global_ET.get(1).dx;

			} else if ((i <= startpointsY[1] && i >= endpointsY[1])
					&& (i <= startpointsY[2] && i >= endpointsY[2])) {
				if (drawX[2] >= drawX[1]) {
					for (int j = (int) drawX[1]; j < drawX[2]; j++) {
						int c = j;
						if(c < 0)
							c = 0;
						if(c > 255)
							c = 255;
						g.setColor(new Color(100, 73, c));
						obj.fill_screen_points[0][0] = (j + obj.offset[0]);
						obj.fill_screen_points[0][1] = (obj.offset[1] - i);
						g.drawLine((int) obj.fill_screen_points[0][0],
								(int) obj.fill_screen_points[0][1],
								(int) obj.fill_screen_points[0][0],
								(int) obj.fill_screen_points[0][1]);
					}
				} else {
					for (int j = (int) drawX[2]; j < drawX[1]; j++) {
						int c = j;
						if(c < 0)
							c = 0;
						if(c > 255)
							c = 255;
						g.setColor(new Color(100, 73, c));
						obj.fill_screen_points[0][0] = (j + obj.offset[0]);
						obj.fill_screen_points[0][1] = (obj.offset[1] - i);
						g.drawLine((int) obj.fill_screen_points[0][0],
								(int) obj.fill_screen_points[0][1],
								(int) obj.fill_screen_points[0][0],
								(int) obj.fill_screen_points[0][1]);
					}
				}
				drawX[1] += Global_ET.get(1).dx;
				drawX[2] += Global_ET.get(2).dx;
			}
		}
	}

	public void CalculateZBuffer(double a0, double b0, double c0, double a1,
			double b1, double c1, double a2, double b2, double c2,
			Graphics2D g, Obj obj) {

	}

	/*
	 * Adds the appropriate offset so the camera is looking at the origin of the
	 * screen
	 */
	public void AddOffset() {

		if (pyramid.isSet) {
			for (int i = 0; i < 5; i++) {
				pyramid.screen_points[i][0] = pyramid.offset[0]
						+ pyramid.screen_points[i][0];
				pyramid.screen_points[i][1] = pyramid.offset[1]
						- pyramid.screen_points[i][1];

			}
		}
		if (box.isSet)
			for (int i = 0; i < 8; i++) {
				box.screen_points[i][0] = box.offset[0]
						+ box.screen_points[i][0];
				box.screen_points[i][1] = box.offset[1]
						- box.screen_points[i][1];
			}
		if (cube.isSet)
			for (int i = 0; i < 8; i++) {
				cube.screen_points[i][0] = cube.offset[0]
						+ cube.screen_points[i][0];
				cube.screen_points[i][1] = cube.offset[1]
						- cube.screen_points[i][1];
			}
	}

	/*
	 * Draws the pyramid onto the canvas
	 */
	public void DrawPyramid(Graphics g2) {

		Graphics2D g = (Graphics2D) g2;

		if (pyramid.fillingIsSet) {

			CalculateFill(pyramid.polygon_points[0][0],
					pyramid.polygon_points[0][1], pyramid.polygon_points[0][2],
					pyramid.polygon_points[1][0], pyramid.polygon_points[1][1],
					pyramid.polygon_points[1][2], pyramid.polygon_points[2][0],
					pyramid.polygon_points[2][1], pyramid.polygon_points[2][2],
					g, pyramid);
			CalculateFill(pyramid.polygon_points[0][0],
					pyramid.polygon_points[0][1], pyramid.polygon_points[0][2],
					pyramid.polygon_points[1][0], pyramid.polygon_points[1][1],
					pyramid.polygon_points[1][2], pyramid.polygon_points[3][0],
					pyramid.polygon_points[3][1], pyramid.polygon_points[3][2],
					g, pyramid);
			CalculateFill(pyramid.polygon_points[0][0],
					pyramid.polygon_points[0][1], pyramid.polygon_points[0][2],
					pyramid.polygon_points[2][0], pyramid.polygon_points[2][1],
					pyramid.polygon_points[2][2], pyramid.polygon_points[4][0],
					pyramid.polygon_points[4][1], pyramid.polygon_points[4][2],
					g, pyramid);
			CalculateFill(pyramid.polygon_points[0][0],
					pyramid.polygon_points[0][1], pyramid.polygon_points[0][2],
					pyramid.polygon_points[3][0], pyramid.polygon_points[3][1],
					pyramid.polygon_points[3][2], pyramid.polygon_points[4][0],
					pyramid.polygon_points[4][1], pyramid.polygon_points[4][2],
					g, pyramid);
			CalculateFill(pyramid.polygon_points[1][0],
					pyramid.polygon_points[1][1], pyramid.polygon_points[1][2],
					pyramid.polygon_points[2][0], pyramid.polygon_points[2][1],
					pyramid.polygon_points[2][2], pyramid.polygon_points[4][0],
					pyramid.polygon_points[4][1], pyramid.polygon_points[4][2],
					g, pyramid);
			CalculateFill(pyramid.polygon_points[1][0],
					pyramid.polygon_points[1][1], pyramid.polygon_points[1][2],
					pyramid.polygon_points[2][0], pyramid.polygon_points[2][1],
					pyramid.polygon_points[2][2], pyramid.polygon_points[3][0],
					pyramid.polygon_points[3][1], pyramid.polygon_points[3][2],
					g, pyramid);

		}
		g.setStroke(new BasicStroke(2));

		// These lines are drawn if back face culling is turned on and the
		// pyramid is selected

		if (pyramid.cullingIsSet) {
			g.setColor(Color.GREEN);
			CalculateSurfaceNormal();
			if (pyramid.drawCulling[0]) {
				g.drawLine((int) pyramid.screen_points[0][0],
						(int) pyramid.screen_points[0][1],
						(int) pyramid.screen_points[2][0],
						(int) pyramid.screen_points[2][1]); // P0 to P2
				g.drawLine((int) pyramid.screen_points[1][0],
						(int) pyramid.screen_points[1][1],
						(int) pyramid.screen_points[0][0],
						(int) pyramid.screen_points[0][1]); // P0 to P1
				g.drawLine((int) pyramid.screen_points[2][0],
						(int) pyramid.screen_points[2][1],
						(int) pyramid.screen_points[1][0],
						(int) pyramid.screen_points[1][1]); // P2 to P1
			}
			if (pyramid.drawCulling[1]) {
				g.drawLine((int) pyramid.screen_points[0][0],
						(int) pyramid.screen_points[0][1],
						(int) pyramid.screen_points[2][0],
						(int) pyramid.screen_points[2][1]); // P0 to P2
				g.drawLine((int) pyramid.screen_points[4][0],
						(int) pyramid.screen_points[4][1],
						(int) pyramid.screen_points[0][0],
						(int) pyramid.screen_points[0][1]); // P0 to P4
				g.drawLine((int) pyramid.screen_points[2][0],
						(int) pyramid.screen_points[2][1],
						(int) pyramid.screen_points[4][0],
						(int) pyramid.screen_points[4][1]); // P2 to P4

			}
			if (pyramid.drawCulling[2]) {
				g.drawLine((int) pyramid.screen_points[3][0],
						(int) pyramid.screen_points[3][1],
						(int) pyramid.screen_points[0][0],
						(int) pyramid.screen_points[0][1]); // P0 to P3
				g.drawLine((int) pyramid.screen_points[4][0],
						(int) pyramid.screen_points[4][1],
						(int) pyramid.screen_points[0][0],
						(int) pyramid.screen_points[0][1]); // P0 to P4
				g.drawLine((int) pyramid.screen_points[3][0],
						(int) pyramid.screen_points[3][1],
						(int) pyramid.screen_points[4][0],
						(int) pyramid.screen_points[4][1]); // P3 to P4

			}
			if (pyramid.drawCulling[3]) {
				g.drawLine((int) pyramid.screen_points[3][0],
						(int) pyramid.screen_points[3][1],
						(int) pyramid.screen_points[0][0],
						(int) pyramid.screen_points[0][1]); // P0 to P3
				g.drawLine((int) pyramid.screen_points[1][0],
						(int) pyramid.screen_points[1][1],
						(int) pyramid.screen_points[0][0],
						(int) pyramid.screen_points[0][1]); // P0 to P1
				g.drawLine((int) pyramid.screen_points[1][0],
						(int) pyramid.screen_points[1][1],
						(int) pyramid.screen_points[3][0],
						(int) pyramid.screen_points[3][1]); // P1 to P3

			}
			if (pyramid.drawCulling[4]) {
				g.drawLine((int) pyramid.screen_points[1][0],
						(int) pyramid.screen_points[1][1],
						(int) pyramid.screen_points[3][0],
						(int) pyramid.screen_points[3][1]); // P1 to P3
				g.drawLine((int) pyramid.screen_points[3][0],
						(int) pyramid.screen_points[3][1],
						(int) pyramid.screen_points[4][0],
						(int) pyramid.screen_points[4][1]); // P3 to P4
				g.drawLine((int) pyramid.screen_points[3][0],
						(int) pyramid.screen_points[3][1],
						(int) pyramid.screen_points[2][0],
						(int) pyramid.screen_points[2][1]); // P2 to P4

			}
			if (pyramid.drawCulling[5]) {
				g.drawLine((int) pyramid.screen_points[2][0],
						(int) pyramid.screen_points[2][1],
						(int) pyramid.screen_points[4][0],
						(int) pyramid.screen_points[4][1]); // P2 to P4

				g.drawLine((int) pyramid.screen_points[2][0],
						(int) pyramid.screen_points[2][1],
						(int) pyramid.screen_points[1][0],
						(int) pyramid.screen_points[1][1]); // P2 to P1
				g.drawLine((int) pyramid.screen_points[3][0],
						(int) pyramid.screen_points[3][1],
						(int) pyramid.screen_points[2][0],
						(int) pyramid.screen_points[2][1]); // P2 to P4

			}
		}

		if (pyramid.wireFrameSet && !pyramid.cullingIsSet) {
			g.setColor(Color.GREEN);
			g.drawLine((int) pyramid.screen_points[0][0],
					(int) pyramid.screen_points[0][1],
					(int) pyramid.screen_points[2][0],
					(int) pyramid.screen_points[2][1]); // P0 to P2

			g.drawLine((int) pyramid.screen_points[1][0],
					(int) pyramid.screen_points[1][1],
					(int) pyramid.screen_points[0][0],
					(int) pyramid.screen_points[0][1]); // P0 to P2

			g.drawLine((int) pyramid.screen_points[3][0],
					(int) pyramid.screen_points[3][1],
					(int) pyramid.screen_points[0][0],
					(int) pyramid.screen_points[0][1]); // P0 to P3

			g.drawLine((int) pyramid.screen_points[4][0],
					(int) pyramid.screen_points[4][1],
					(int) pyramid.screen_points[0][0],
					(int) pyramid.screen_points[0][1]); // P0 to P4
			g.drawLine((int) pyramid.screen_points[2][0],
					(int) pyramid.screen_points[2][1],
					(int) pyramid.screen_points[4][0],
					(int) pyramid.screen_points[4][1]); // P2 to P4

			g.drawLine((int) pyramid.screen_points[2][0],
					(int) pyramid.screen_points[2][1],
					(int) pyramid.screen_points[1][0],
					(int) pyramid.screen_points[1][1]); // P2 to P1

			g.drawLine((int) pyramid.screen_points[3][0],
					(int) pyramid.screen_points[3][1],
					(int) pyramid.screen_points[4][0],
					(int) pyramid.screen_points[4][1]); // P3 to P4

			g.drawLine((int) pyramid.screen_points[1][0],
					(int) pyramid.screen_points[1][1],
					(int) pyramid.screen_points[3][0],
					(int) pyramid.screen_points[3][1]); // P1 to P3
			g.drawLine((int) pyramid.screen_points[3][0],
					(int) pyramid.screen_points[3][1],
					(int) pyramid.screen_points[2][0],
					(int) pyramid.screen_points[2][1]); // P1 to P3
		}

	}

	// Draws the box onto the canvas
	/*
	 * Draws the box object onto the canvas
	 */
	public void DrawBox(Graphics g2) {

		Graphics2D g = (Graphics2D) g2;

		if (box.fillingIsSet) {
			CalculateFill(box.polygon_points[0][0], box.polygon_points[0][1],
					box.polygon_points[0][2], box.polygon_points[1][0],
					box.polygon_points[1][1], box.polygon_points[1][2],
					box.polygon_points[3][0], box.polygon_points[3][1],
					box.polygon_points[3][2], g, box);
			CalculateFill(box.polygon_points[0][0], box.polygon_points[0][1],
					box.polygon_points[0][2], box.polygon_points[2][0],
					box.polygon_points[2][1], box.polygon_points[2][2],
					box.polygon_points[3][0], box.polygon_points[3][1],
					box.polygon_points[3][2], g, box);
			CalculateFill(box.polygon_points[0][0], box.polygon_points[0][1],
					box.polygon_points[0][2], box.polygon_points[6][0],
					box.polygon_points[6][1], box.polygon_points[6][2],
					box.polygon_points[4][0], box.polygon_points[4][1],
					box.polygon_points[4][2], g, box);
			CalculateFill(box.polygon_points[0][0], box.polygon_points[0][1],
					box.polygon_points[0][2], box.polygon_points[2][0],
					box.polygon_points[2][1], box.polygon_points[2][2],
					box.polygon_points[6][0], box.polygon_points[6][1],
					box.polygon_points[6][2], g, box);
			CalculateFill(box.polygon_points[4][0], box.polygon_points[4][1],
					box.polygon_points[4][2], box.polygon_points[6][0],
					box.polygon_points[6][1], box.polygon_points[6][2],
					box.polygon_points[7][0], box.polygon_points[7][1],
					box.polygon_points[7][2], g, box);
			CalculateFill(box.polygon_points[5][0], box.polygon_points[5][1],
					box.polygon_points[5][2], box.polygon_points[7][0],
					box.polygon_points[7][1], box.polygon_points[7][2],
					box.polygon_points[1][0], box.polygon_points[1][1],
					box.polygon_points[1][2], g, box);
			CalculateFill(box.polygon_points[3][0], box.polygon_points[3][1],
					box.polygon_points[3][2], box.polygon_points[7][0],
					box.polygon_points[7][1], box.polygon_points[7][2],
					box.polygon_points[1][0], box.polygon_points[1][1],
					box.polygon_points[1][2], g, box);
			CalculateFill(box.polygon_points[4][0], box.polygon_points[4][1],
					box.polygon_points[4][2], box.polygon_points[7][0],
					box.polygon_points[7][1], box.polygon_points[7][2],
					box.polygon_points[5][0], box.polygon_points[5][1],
					box.polygon_points[5][2], g, box);
			CalculateFill(box.polygon_points[0][0], box.polygon_points[0][1],
					box.polygon_points[0][2], box.polygon_points[1][0],
					box.polygon_points[1][1], box.polygon_points[1][2],
					box.polygon_points[5][0], box.polygon_points[5][1],
					box.polygon_points[5][2], g, box);
			CalculateFill(box.polygon_points[0][0], box.polygon_points[0][1],
					box.polygon_points[0][2], box.polygon_points[4][0],
					box.polygon_points[4][1], box.polygon_points[4][2],
					box.polygon_points[5][0], box.polygon_points[5][1],
					box.polygon_points[5][2], g, box);
			CalculateFill(box.polygon_points[2][0], box.polygon_points[2][1],
					box.polygon_points[2][2], box.polygon_points[3][0],
					box.polygon_points[3][1], box.polygon_points[3][2],
					box.polygon_points[7][0], box.polygon_points[7][1],
					box.polygon_points[7][2], g, box);
			CalculateFill(box.polygon_points[2][0], box.polygon_points[2][1],
					box.polygon_points[2][2], box.polygon_points[3][0],
					box.polygon_points[3][1], box.polygon_points[3][2],
					box.polygon_points[6][0], box.polygon_points[6][1],
					box.polygon_points[6][2], g, box);
		}
		if (box.cullingIsSet) {
			g.setColor(Color.BLUE);
			CalculateSurfaceNormal();
			if (box.drawCulling[0]) {
				g.drawLine((int) box.screen_points[0][0],
						(int) box.screen_points[0][1],
						(int) box.screen_points[1][0],
						(int) box.screen_points[1][1]); // P0 to P1
				g.drawLine((int) box.screen_points[1][0],
						(int) box.screen_points[1][1],
						(int) box.screen_points[3][0],
						(int) box.screen_points[3][1]); // P3 to P1
				g.drawLine((int) box.screen_points[3][0],
						(int) box.screen_points[3][1],
						(int) box.screen_points[0][0],
						(int) box.screen_points[0][1]); // P6 to P4
				g.drawLine((int) box.screen_points[0][0],
						(int) box.screen_points[0][1],
						(int) box.screen_points[2][0],
						(int) box.screen_points[2][1]); // P0 to P4
				g.drawLine((int) box.screen_points[2][0],
						(int) box.screen_points[2][1],
						(int) box.screen_points[3][0],
						(int) box.screen_points[3][1]); // P5 to P1
				g.drawLine((int) box.screen_points[3][0],
						(int) box.screen_points[3][1],
						(int) box.screen_points[0][0],
						(int) box.screen_points[0][1]); // P0 to P2
			}
			if (box.drawCulling[3]) {
				// Right Face
				g.drawLine((int) box.screen_points[5][0],
						(int) box.screen_points[5][1],
						(int) box.screen_points[1][0],
						(int) box.screen_points[1][1]); // P4 to P5

				g.drawLine((int) box.screen_points[5][0],
						(int) box.screen_points[5][1],
						(int) box.screen_points[7][0],
						(int) box.screen_points[7][1]); // P5 to P7
				g.drawLine((int) box.screen_points[7][0],
						(int) box.screen_points[7][1],
						(int) box.screen_points[1][0],
						(int) box.screen_points[1][1]); // P5 to P7
				g.drawLine((int) box.screen_points[3][0],
						(int) box.screen_points[3][1],
						(int) box.screen_points[1][0],
						(int) box.screen_points[1][1]); // P4 to P5
				g.drawLine((int) box.screen_points[3][0],
						(int) box.screen_points[3][1],
						(int) box.screen_points[7][0],
						(int) box.screen_points[7][1]); // P5 to P7
				g.drawLine((int) box.screen_points[7][0],
						(int) box.screen_points[7][1],
						(int) box.screen_points[1][0],
						(int) box.screen_points[1][1]); // P5 to P7

			}
			if (box.drawCulling[2]) {
				// Back Face
				g.drawLine((int) box.screen_points[4][0],
						(int) box.screen_points[4][1],
						(int) box.screen_points[5][0],
						(int) box.screen_points[5][1]); // P0 to P1
				g.drawLine((int) box.screen_points[5][0],
						(int) box.screen_points[5][1],
						(int) box.screen_points[7][0],
						(int) box.screen_points[7][1]); // P3 to P1
				g.drawLine((int) box.screen_points[7][0],
						(int) box.screen_points[7][1],
						(int) box.screen_points[4][0],
						(int) box.screen_points[4][1]); // P6 to P4
				g.drawLine((int) box.screen_points[4][0],
						(int) box.screen_points[4][1],
						(int) box.screen_points[6][0],
						(int) box.screen_points[6][1]); // P0 to P4
				g.drawLine((int) box.screen_points[6][0],
						(int) box.screen_points[6][1],
						(int) box.screen_points[7][0],
						(int) box.screen_points[7][1]); // P5 to P1
				g.drawLine((int) box.screen_points[7][0],
						(int) box.screen_points[7][1],
						(int) box.screen_points[4][0],
						(int) box.screen_points[4][1]); // P0 to P2

			}
			if (box.drawCulling[1]) {
				// Right Face
				g.drawLine((int) box.screen_points[4][0],
						(int) box.screen_points[4][1],
						(int) box.screen_points[0][0],
						(int) box.screen_points[0][1]); // P4 to P5
				g.drawLine((int) box.screen_points[4][0],
						(int) box.screen_points[4][1],
						(int) box.screen_points[6][0],
						(int) box.screen_points[6][1]); // P5 to P7
				g.drawLine((int) box.screen_points[6][0],
						(int) box.screen_points[6][1],
						(int) box.screen_points[0][0],
						(int) box.screen_points[0][1]); // P5 to P7
				g.drawLine((int) box.screen_points[2][0],
						(int) box.screen_points[2][1],
						(int) box.screen_points[0][0],
						(int) box.screen_points[0][1]); // P4 to P5
				g.drawLine((int) box.screen_points[2][0],
						(int) box.screen_points[2][1],
						(int) box.screen_points[6][0],
						(int) box.screen_points[6][1]); // P5 to P7
				g.drawLine((int) box.screen_points[6][0],
						(int) box.screen_points[6][1],
						(int) box.screen_points[0][0],
						(int) box.screen_points[0][1]); // P5 to P7

			}
			if (box.drawCulling[5]) {
				// Top Face
				g.drawLine((int) box.screen_points[0][0],
						(int) box.screen_points[0][1],
						(int) box.screen_points[1][0],
						(int) box.screen_points[1][1]); // P4 to P5
				g.drawLine((int) box.screen_points[5][0],
						(int) box.screen_points[5][1],
						(int) box.screen_points[1][0],
						(int) box.screen_points[1][1]); // P5 to P7
				g.drawLine((int) box.screen_points[5][0],
						(int) box.screen_points[5][1],
						(int) box.screen_points[0][0],
						(int) box.screen_points[0][1]); // P5 to P7
				g.drawLine((int) box.screen_points[0][0],
						(int) box.screen_points[0][1],
						(int) box.screen_points[4][0],
						(int) box.screen_points[4][1]); // P4 to P5
				g.drawLine((int) box.screen_points[4][0],
						(int) box.screen_points[4][1],
						(int) box.screen_points[5][0],
						(int) box.screen_points[5][1]); // P5 to P7
				g.drawLine((int) box.screen_points[0][0],
						(int) box.screen_points[0][1],
						(int) box.screen_points[5][0],
						(int) box.screen_points[5][1]); // P5 to P7

			}
			if (box.drawCulling[4]) {
				// Bottom Face
				g.drawLine((int) box.screen_points[2][0],
						(int) box.screen_points[2][1],
						(int) box.screen_points[3][0],
						(int) box.screen_points[3][1]); // P4 to P5
				g.drawLine((int) box.screen_points[7][0],
						(int) box.screen_points[7][1],
						(int) box.screen_points[3][0],
						(int) box.screen_points[3][1]); // P5 to P7
				g.drawLine((int) box.screen_points[7][0],
						(int) box.screen_points[7][1],
						(int) box.screen_points[2][0],
						(int) box.screen_points[2][1]); // P5 to P7
				g.drawLine((int) box.screen_points[2][0],
						(int) box.screen_points[2][1],
						(int) box.screen_points[6][0],
						(int) box.screen_points[6][1]); // P4 to P5
				g.drawLine((int) box.screen_points[6][0],
						(int) box.screen_points[6][1],
						(int) box.screen_points[7][0],
						(int) box.screen_points[7][1]); // P5 to P7
				g.drawLine((int) box.screen_points[2][0],
						(int) box.screen_points[2][1],
						(int) box.screen_points[7][0],
						(int) box.screen_points[7][1]); // P5 to P7

			}
		}

		if (box.wireFrameSet && !box.cullingIsSet) {
			g.setColor(Color.BLUE);
			// Front face
			g.drawLine((int) box.screen_points[0][0],
					(int) box.screen_points[0][1],
					(int) box.screen_points[1][0],
					(int) box.screen_points[1][1]); // P0 to P1
			g.drawLine((int) box.screen_points[1][0],
					(int) box.screen_points[1][1],
					(int) box.screen_points[3][0],
					(int) box.screen_points[3][1]); // P3 to P1
			g.drawLine((int) box.screen_points[3][0],
					(int) box.screen_points[3][1],
					(int) box.screen_points[0][0],
					(int) box.screen_points[0][1]); // P6 to P4
			g.drawLine((int) box.screen_points[0][0],
					(int) box.screen_points[0][1],
					(int) box.screen_points[2][0],
					(int) box.screen_points[2][1]); // P0 to P4
			g.drawLine((int) box.screen_points[2][0],
					(int) box.screen_points[2][1],
					(int) box.screen_points[3][0],
					(int) box.screen_points[3][1]); // P5 to P1
			g.drawLine((int) box.screen_points[3][0],
					(int) box.screen_points[3][1],
					(int) box.screen_points[0][0],
					(int) box.screen_points[0][1]); // P0 to P2
			// Back Face
			g.drawLine((int) box.screen_points[4][0],
					(int) box.screen_points[4][1],
					(int) box.screen_points[5][0],
					(int) box.screen_points[5][1]); // P0 to P1
			g.drawLine((int) box.screen_points[5][0],
					(int) box.screen_points[5][1],
					(int) box.screen_points[7][0],
					(int) box.screen_points[7][1]); // P3 to P1
			g.drawLine((int) box.screen_points[7][0],
					(int) box.screen_points[7][1],
					(int) box.screen_points[4][0],
					(int) box.screen_points[4][1]); // P6 to P4
			g.drawLine((int) box.screen_points[4][0],
					(int) box.screen_points[4][1],
					(int) box.screen_points[6][0],
					(int) box.screen_points[6][1]); // P0 to P4
			g.drawLine((int) box.screen_points[6][0],
					(int) box.screen_points[6][1],
					(int) box.screen_points[7][0],
					(int) box.screen_points[7][1]); // P5 to P1
			g.drawLine((int) box.screen_points[7][0],
					(int) box.screen_points[7][1],
					(int) box.screen_points[4][0],
					(int) box.screen_points[4][1]); // P0 to P2
			// Right Face
			g.drawLine((int) box.screen_points[4][0],
					(int) box.screen_points[4][1],
					(int) box.screen_points[0][0],
					(int) box.screen_points[0][1]); // P4 to P5
			g.drawLine((int) box.screen_points[4][0],
					(int) box.screen_points[4][1],
					(int) box.screen_points[6][0],
					(int) box.screen_points[6][1]); // P5 to P7
			g.drawLine((int) box.screen_points[6][0],
					(int) box.screen_points[6][1],
					(int) box.screen_points[0][0],
					(int) box.screen_points[0][1]); // P5 to P7
			g.drawLine((int) box.screen_points[2][0],
					(int) box.screen_points[2][1],
					(int) box.screen_points[0][0],
					(int) box.screen_points[0][1]); // P4 to P5
			g.drawLine((int) box.screen_points[2][0],
					(int) box.screen_points[2][1],
					(int) box.screen_points[6][0],
					(int) box.screen_points[6][1]); // P5 to P7
			g.drawLine((int) box.screen_points[6][0],
					(int) box.screen_points[6][1],
					(int) box.screen_points[0][0],
					(int) box.screen_points[0][1]); // P5 to P7
			// Left Face
			g.drawLine((int) box.screen_points[5][0],
					(int) box.screen_points[5][1],
					(int) box.screen_points[1][0],
					(int) box.screen_points[1][1]); // P4 to P5

			g.drawLine((int) box.screen_points[5][0],
					(int) box.screen_points[5][1],
					(int) box.screen_points[7][0],
					(int) box.screen_points[7][1]); // P5 to P7
			g.drawLine((int) box.screen_points[7][0],
					(int) box.screen_points[7][1],
					(int) box.screen_points[1][0],
					(int) box.screen_points[1][1]); // P5 to P7
			g.drawLine((int) box.screen_points[3][0],
					(int) box.screen_points[3][1],
					(int) box.screen_points[1][0],
					(int) box.screen_points[1][1]); // P4 to P5
			g.drawLine((int) box.screen_points[3][0],
					(int) box.screen_points[3][1],
					(int) box.screen_points[7][0],
					(int) box.screen_points[7][1]); // P5 to P7
			g.drawLine((int) box.screen_points[7][0],
					(int) box.screen_points[7][1],
					(int) box.screen_points[1][0],
					(int) box.screen_points[1][1]); // P5 to P7
			// Top Face
			g.drawLine((int) box.screen_points[0][0],
					(int) box.screen_points[0][1],
					(int) box.screen_points[1][0],
					(int) box.screen_points[1][1]); // P4 to P5
			g.drawLine((int) box.screen_points[5][0],
					(int) box.screen_points[5][1],
					(int) box.screen_points[1][0],
					(int) box.screen_points[1][1]); // P5 to P7
			g.drawLine((int) box.screen_points[5][0],
					(int) box.screen_points[5][1],
					(int) box.screen_points[0][0],
					(int) box.screen_points[0][1]); // P5 to P7
			g.drawLine((int) box.screen_points[0][0],
					(int) box.screen_points[0][1],
					(int) box.screen_points[4][0],
					(int) box.screen_points[4][1]); // P4 to P5
			g.drawLine((int) box.screen_points[4][0],
					(int) box.screen_points[4][1],
					(int) box.screen_points[5][0],
					(int) box.screen_points[5][1]); // P5 to P7
			g.drawLine((int) box.screen_points[0][0],
					(int) box.screen_points[0][1],
					(int) box.screen_points[5][0],
					(int) box.screen_points[5][1]); // P5 to P7
			// Bottom Face
			g.drawLine((int) box.screen_points[2][0],
					(int) box.screen_points[2][1],
					(int) box.screen_points[3][0],
					(int) box.screen_points[3][1]); // P4 to P5
			g.drawLine((int) box.screen_points[7][0],
					(int) box.screen_points[7][1],
					(int) box.screen_points[3][0],
					(int) box.screen_points[3][1]); // P5 to P7
			g.drawLine((int) box.screen_points[7][0],
					(int) box.screen_points[7][1],
					(int) box.screen_points[2][0],
					(int) box.screen_points[2][1]); // P5 to P7
			g.drawLine((int) box.screen_points[2][0],
					(int) box.screen_points[2][1],
					(int) box.screen_points[6][0],
					(int) box.screen_points[6][1]); // P4 to P5
			g.drawLine((int) box.screen_points[6][0],
					(int) box.screen_points[6][1],
					(int) box.screen_points[7][0],
					(int) box.screen_points[7][1]); // P5 to P7
			g.drawLine((int) box.screen_points[2][0],
					(int) box.screen_points[2][1],
					(int) box.screen_points[7][0],
					(int) box.screen_points[7][1]); // P5 to P7
		}

	}

	/*
	 * Draws the cube object onto canvas
	 */
	public void DrawCube(Graphics g2) {

		Graphics2D g = (Graphics2D) g2;

		if (cube.fillingIsSet) {
			CalculateFill(cube.polygon_points[0][0], cube.polygon_points[0][1],
					cube.polygon_points[0][2], cube.polygon_points[1][0],
					cube.polygon_points[1][1], cube.polygon_points[1][2],
					cube.polygon_points[3][0], cube.polygon_points[3][1],
					cube.polygon_points[3][2], g, cube);
			CalculateFill(cube.polygon_points[0][0], cube.polygon_points[0][1],
					cube.polygon_points[0][2], cube.polygon_points[2][0],
					cube.polygon_points[2][1], cube.polygon_points[2][2],
					cube.polygon_points[3][0], cube.polygon_points[3][1],
					cube.polygon_points[3][2], g, cube);
			CalculateFill(cube.polygon_points[0][0], cube.polygon_points[0][1],
					cube.polygon_points[0][2], cube.polygon_points[6][0],
					cube.polygon_points[6][1], cube.polygon_points[6][2],
					cube.polygon_points[4][0], cube.polygon_points[4][1],
					cube.polygon_points[4][2], g, cube);
			CalculateFill(cube.polygon_points[0][0], cube.polygon_points[0][1],
					cube.polygon_points[0][2], cube.polygon_points[2][0],
					cube.polygon_points[2][1], cube.polygon_points[2][2],
					cube.polygon_points[6][0], cube.polygon_points[6][1],
					cube.polygon_points[6][2], g, cube);
			CalculateFill(cube.polygon_points[4][0], cube.polygon_points[4][1],
					cube.polygon_points[4][2], cube.polygon_points[6][0],
					cube.polygon_points[6][1], cube.polygon_points[6][2],
					cube.polygon_points[7][0], cube.polygon_points[7][1],
					cube.polygon_points[7][2], g, cube);
			CalculateFill(cube.polygon_points[5][0], cube.polygon_points[5][1],
					cube.polygon_points[5][2], cube.polygon_points[7][0],
					cube.polygon_points[7][1], cube.polygon_points[7][2],
					cube.polygon_points[1][0], cube.polygon_points[1][1],
					cube.polygon_points[1][2], g, cube);
			CalculateFill(cube.polygon_points[3][0], cube.polygon_points[3][1],
					cube.polygon_points[3][2], cube.polygon_points[7][0],
					cube.polygon_points[7][1], cube.polygon_points[7][2],
					cube.polygon_points[1][0], cube.polygon_points[1][1],
					cube.polygon_points[1][2], g, cube);
			CalculateFill(cube.polygon_points[4][0], cube.polygon_points[4][1],
					cube.polygon_points[4][2], cube.polygon_points[7][0],
					cube.polygon_points[7][1], cube.polygon_points[7][2],
					cube.polygon_points[5][0], cube.polygon_points[5][1],
					cube.polygon_points[5][2], g, cube);
			CalculateFill(cube.polygon_points[0][0], cube.polygon_points[0][1],
					cube.polygon_points[0][2], cube.polygon_points[1][0],
					cube.polygon_points[1][1], cube.polygon_points[1][2],
					cube.polygon_points[5][0], cube.polygon_points[5][1],
					cube.polygon_points[5][2], g, cube);
			CalculateFill(cube.polygon_points[0][0], cube.polygon_points[0][1],
					cube.polygon_points[0][2], cube.polygon_points[4][0],
					cube.polygon_points[4][1], cube.polygon_points[4][2],
					cube.polygon_points[5][0], cube.polygon_points[5][1],
					cube.polygon_points[5][2], g, cube);
			CalculateFill(cube.polygon_points[2][0], cube.polygon_points[2][1],
					cube.polygon_points[2][2], cube.polygon_points[3][0],
					cube.polygon_points[3][1], cube.polygon_points[3][2],
					cube.polygon_points[7][0], cube.polygon_points[7][1],
					cube.polygon_points[7][2], g, cube);
			CalculateFill(cube.polygon_points[2][0], cube.polygon_points[2][1],
					cube.polygon_points[2][2], cube.polygon_points[3][0],
					cube.polygon_points[3][1], cube.polygon_points[3][2],
					cube.polygon_points[6][0], cube.polygon_points[6][1],
					cube.polygon_points[6][2], g, cube);
		}
		if (cube.cullingIsSet) {
			g.setColor(Color.CYAN);
			CalculateSurfaceNormal();
			if (cube.drawCulling[0]) {
				g.drawLine((int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1],
						(int) cube.screen_points[1][0],
						(int) cube.screen_points[1][1]); // P0 to P1
				g.drawLine((int) cube.screen_points[1][0],
						(int) cube.screen_points[1][1],
						(int) cube.screen_points[3][0],
						(int) cube.screen_points[3][1]); // P3 to P1
				g.drawLine((int) cube.screen_points[3][0],
						(int) cube.screen_points[3][1],
						(int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1]); // P6 to P4
				g.drawLine((int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1],
						(int) cube.screen_points[2][0],
						(int) cube.screen_points[2][1]); // P0 to P4
				g.drawLine((int) cube.screen_points[2][0],
						(int) cube.screen_points[2][1],
						(int) cube.screen_points[3][0],
						(int) cube.screen_points[3][1]); // P5 to P1
				g.drawLine((int) cube.screen_points[3][0],
						(int) cube.screen_points[3][1],
						(int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1]); // P0 to P2
			}
			if (cube.drawCulling[3]) {
				// Right Face
				g.drawLine((int) cube.screen_points[5][0],
						(int) cube.screen_points[5][1],
						(int) cube.screen_points[1][0],
						(int) cube.screen_points[1][1]); // P4 to P5

				g.drawLine((int) cube.screen_points[5][0],
						(int) cube.screen_points[5][1],
						(int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1],
						(int) cube.screen_points[1][0],
						(int) cube.screen_points[1][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[3][0],
						(int) cube.screen_points[3][1],
						(int) cube.screen_points[1][0],
						(int) cube.screen_points[1][1]); // P4 to P5
				g.drawLine((int) cube.screen_points[3][0],
						(int) cube.screen_points[3][1],
						(int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1],
						(int) cube.screen_points[1][0],
						(int) cube.screen_points[1][1]); // P5 to P7

			}
			if (cube.drawCulling[2]) {
				// Back Face
				g.drawLine((int) cube.screen_points[4][0],
						(int) cube.screen_points[4][1],
						(int) cube.screen_points[5][0],
						(int) cube.screen_points[5][1]); // P0 to P1
				g.drawLine((int) cube.screen_points[5][0],
						(int) cube.screen_points[5][1],
						(int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1]); // P3 to P1
				g.drawLine((int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1],
						(int) cube.screen_points[4][0],
						(int) cube.screen_points[4][1]); // P6 to P4
				g.drawLine((int) cube.screen_points[4][0],
						(int) cube.screen_points[4][1],
						(int) cube.screen_points[6][0],
						(int) cube.screen_points[6][1]); // P0 to P4
				g.drawLine((int) cube.screen_points[6][0],
						(int) cube.screen_points[6][1],
						(int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1]); // P5 to P1
				g.drawLine((int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1],
						(int) cube.screen_points[4][0],
						(int) cube.screen_points[4][1]); // P0 to P2

			}
			if (cube.drawCulling[1]) {
				// Right Face
				g.drawLine((int) cube.screen_points[4][0],
						(int) cube.screen_points[4][1],
						(int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1]); // P4 to P5
				g.drawLine((int) cube.screen_points[4][0],
						(int) cube.screen_points[4][1],
						(int) cube.screen_points[6][0],
						(int) cube.screen_points[6][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[6][0],
						(int) cube.screen_points[6][1],
						(int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[2][0],
						(int) cube.screen_points[2][1],
						(int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1]); // P4 to P5
				g.drawLine((int) cube.screen_points[2][0],
						(int) cube.screen_points[2][1],
						(int) cube.screen_points[6][0],
						(int) cube.screen_points[6][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[6][0],
						(int) cube.screen_points[6][1],
						(int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1]); // P5 to P7

			}
			if (cube.drawCulling[5]) {
				// Top Face
				g.drawLine((int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1],
						(int) cube.screen_points[1][0],
						(int) cube.screen_points[1][1]); // P4 to P5
				g.drawLine((int) cube.screen_points[5][0],
						(int) cube.screen_points[5][1],
						(int) cube.screen_points[1][0],
						(int) cube.screen_points[1][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[5][0],
						(int) cube.screen_points[5][1],
						(int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1],
						(int) cube.screen_points[4][0],
						(int) cube.screen_points[4][1]); // P4 to P5
				g.drawLine((int) cube.screen_points[4][0],
						(int) cube.screen_points[4][1],
						(int) cube.screen_points[5][0],
						(int) cube.screen_points[5][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[0][0],
						(int) cube.screen_points[0][1],
						(int) cube.screen_points[5][0],
						(int) cube.screen_points[5][1]); // P5 to P7

			}
			if (cube.drawCulling[4]) {
				// Bottom Face
				g.drawLine((int) cube.screen_points[2][0],
						(int) cube.screen_points[2][1],
						(int) cube.screen_points[3][0],
						(int) cube.screen_points[3][1]); // P4 to P5
				g.drawLine((int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1],
						(int) cube.screen_points[3][0],
						(int) cube.screen_points[3][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1],
						(int) cube.screen_points[2][0],
						(int) cube.screen_points[2][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[2][0],
						(int) cube.screen_points[2][1],
						(int) cube.screen_points[6][0],
						(int) cube.screen_points[6][1]); // P4 to P5
				g.drawLine((int) cube.screen_points[6][0],
						(int) cube.screen_points[6][1],
						(int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1]); // P5 to P7
				g.drawLine((int) cube.screen_points[2][0],
						(int) cube.screen_points[2][1],
						(int) cube.screen_points[7][0],
						(int) cube.screen_points[7][1]); // P5 to P7

			}
		}

		if (cube.wireFrameSet && !cube.cullingIsSet) {
			g.setColor(Color.CYAN);
			// Front face
			g.drawLine((int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1],
					(int) cube.screen_points[1][0],
					(int) cube.screen_points[1][1]); // P0 to P1
			g.drawLine((int) cube.screen_points[1][0],
					(int) cube.screen_points[1][1],
					(int) cube.screen_points[3][0],
					(int) cube.screen_points[3][1]); // P3 to P1
			g.drawLine((int) cube.screen_points[3][0],
					(int) cube.screen_points[3][1],
					(int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1]); // P6 to P4
			g.drawLine((int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1],
					(int) cube.screen_points[2][0],
					(int) cube.screen_points[2][1]); // P0 to P4
			g.drawLine((int) cube.screen_points[2][0],
					(int) cube.screen_points[2][1],
					(int) cube.screen_points[3][0],
					(int) cube.screen_points[3][1]); // P5 to P1
			g.drawLine((int) cube.screen_points[3][0],
					(int) cube.screen_points[3][1],
					(int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1]); // P0 to P2
			// Back Face
			g.drawLine((int) cube.screen_points[4][0],
					(int) cube.screen_points[4][1],
					(int) cube.screen_points[5][0],
					(int) cube.screen_points[5][1]); // P0 to P1
			g.drawLine((int) cube.screen_points[5][0],
					(int) cube.screen_points[5][1],
					(int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1]); // P3 to P1
			g.drawLine((int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1],
					(int) cube.screen_points[4][0],
					(int) cube.screen_points[4][1]); // P6 to P4
			g.drawLine((int) cube.screen_points[4][0],
					(int) cube.screen_points[4][1],
					(int) cube.screen_points[6][0],
					(int) cube.screen_points[6][1]); // P0 to P4
			g.drawLine((int) cube.screen_points[6][0],
					(int) cube.screen_points[6][1],
					(int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1]); // P5 to P1
			g.drawLine((int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1],
					(int) cube.screen_points[4][0],
					(int) cube.screen_points[4][1]); // P0 to P2
			// Right Face
			g.drawLine((int) cube.screen_points[4][0],
					(int) cube.screen_points[4][1],
					(int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1]); // P4 to P5
			g.drawLine((int) cube.screen_points[4][0],
					(int) cube.screen_points[4][1],
					(int) cube.screen_points[6][0],
					(int) cube.screen_points[6][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[6][0],
					(int) cube.screen_points[6][1],
					(int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[2][0],
					(int) cube.screen_points[2][1],
					(int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1]); // P4 to P5
			g.drawLine((int) cube.screen_points[2][0],
					(int) cube.screen_points[2][1],
					(int) cube.screen_points[6][0],
					(int) cube.screen_points[6][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[6][0],
					(int) cube.screen_points[6][1],
					(int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1]); // P5 to P7
			// Left Face
			g.drawLine((int) cube.screen_points[5][0],
					(int) cube.screen_points[5][1],
					(int) cube.screen_points[1][0],
					(int) cube.screen_points[1][1]); // P4 to P5

			g.drawLine((int) cube.screen_points[5][0],
					(int) cube.screen_points[5][1],
					(int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1],
					(int) cube.screen_points[1][0],
					(int) cube.screen_points[1][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[3][0],
					(int) cube.screen_points[3][1],
					(int) cube.screen_points[1][0],
					(int) cube.screen_points[1][1]); // P4 to P5
			g.drawLine((int) cube.screen_points[3][0],
					(int) cube.screen_points[3][1],
					(int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1],
					(int) cube.screen_points[1][0],
					(int) cube.screen_points[1][1]); // P5 to P7
			// Top Face
			g.drawLine((int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1],
					(int) cube.screen_points[1][0],
					(int) cube.screen_points[1][1]); // P4 to P5
			g.drawLine((int) cube.screen_points[5][0],
					(int) cube.screen_points[5][1],
					(int) cube.screen_points[1][0],
					(int) cube.screen_points[1][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[5][0],
					(int) cube.screen_points[5][1],
					(int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1],
					(int) cube.screen_points[4][0],
					(int) cube.screen_points[4][1]); // P4 to P5
			g.drawLine((int) cube.screen_points[4][0],
					(int) cube.screen_points[4][1],
					(int) cube.screen_points[5][0],
					(int) cube.screen_points[5][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[0][0],
					(int) cube.screen_points[0][1],
					(int) cube.screen_points[5][0],
					(int) cube.screen_points[5][1]); // P5 to P7
			// Bottom Face
			g.drawLine((int) cube.screen_points[2][0],
					(int) cube.screen_points[2][1],
					(int) cube.screen_points[3][0],
					(int) cube.screen_points[3][1]); // P4 to P5
			g.drawLine((int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1],
					(int) cube.screen_points[3][0],
					(int) cube.screen_points[3][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1],
					(int) cube.screen_points[2][0],
					(int) cube.screen_points[2][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[2][0],
					(int) cube.screen_points[2][1],
					(int) cube.screen_points[6][0],
					(int) cube.screen_points[6][1]); // P4 to P5
			g.drawLine((int) cube.screen_points[6][0],
					(int) cube.screen_points[6][1],
					(int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1]); // P5 to P7
			g.drawLine((int) cube.screen_points[2][0],
					(int) cube.screen_points[2][1],
					(int) cube.screen_points[7][0],
					(int) cube.screen_points[7][1]); // P5 to P7
		}

	}

	/*
	 * Draws the X and Y planes
	 */
	public void DrawPlanes(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawLine((int) (x / 2), 0, (int) (x / 2), (int) y);
		g.drawLine(0, (int) (y / 2), (int) x, (int) (y / 2));
	}

	/*
	 * Draws the Reset, blue, green, and red selector buttons onto the canvas
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
		// Green button
		g.setColor(Color.GREEN);
		g.drawOval(WIDTH - 110, HEIGHT - 85, 25, 25);
		g.fillOval(WIDTH - 110, HEIGHT - 85, 25, 25);

		// Red button
		g.setColor(Color.CYAN);
		g.drawOval(WIDTH - 70, HEIGHT - 85, 25, 25);
		g.fillOval(WIDTH - 70, HEIGHT - 85, 25, 25);

		// Green button
		if (pyramid.isSet) {
			g.setColor(Color.BLACK);
			g.drawOval(WIDTH - 110, HEIGHT - 85, 25, 25);
			g.setColor(new Color(5, 69, 0));
			g.fillOval(WIDTH - 110, HEIGHT - 85, 25, 25);
		}
		// Blue button
		if (box.isSet) {
			g.setColor(Color.BLACK);
			g.drawOval(WIDTH - 150, HEIGHT - 85, 25, 25);
			g.setColor(new Color(7, 3, 61));
			g.fillOval(WIDTH - 150, HEIGHT - 85, 25, 25);
		}
		// Red button
		if (cube.isSet) {
			g.setColor(Color.BLACK);
			g.drawOval(WIDTH - 70, HEIGHT - 85, 25, 25);
			g.setColor(new Color(3, 71, 71));
			g.fillOval(WIDTH - 70, HEIGHT - 85, 25, 25);
		}

	}

	// This function will trigger if the reset button
	/*
	 * Gets rid of old canvas and prduces new cavnas, reseting the screen
	 */
	// Produces a random number for filling the triangle
	public static int randInt(int min, int max) {
		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public void ResetPyramid() {
		dispose(); // gets rid of old JFrame window
		new MainProgram(); // restarts program
	}

	public static void main(String[] args) {
		new MainProgram();

	}

}