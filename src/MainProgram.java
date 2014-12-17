import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class MainProgram extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private MyCanvas canvas = new MyCanvas();
	
	int x = 600, y = 400;
	int x_origin = x/2, y_origin = y/2;
	
	public static void main(String[] args){
		MainProgram p = new MainProgram();
	}
	
	public MainProgram(){
		
		setLayout(new BorderLayout());
		setSize(x, y);
		setTitle("3D Pyramid - Assignment 1");
		add("Center", canvas);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}
	

	
	private class MyCanvas extends Canvas{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g){
			int[][] polygon_points = new int[5][2];

			// P1
			polygon_points[0][0] = x_origin;
			polygon_points[0][1] = y_origin + 100;
			
			// P2
			polygon_points[1][0] = x_origin - 75;
			polygon_points[1][1] = y_origin - 100;
			
			// P3
			polygon_points[2][0] = x_origin + 75;
			polygon_points[2][1] = y_origin - 100;
			
			// P4
			polygon_points[3][0] = 226;
			polygon_points[3][1] = 275;
			
			// P5
			polygon_points[4][0] = 374;
			polygon_points[4][1] = 275;
			
			g.setColor(Color.BLUE);
			g.drawLine(x/2, 0, x/2, y);
			g.drawLine(0, y/2, x, y/2);
			
			g.setColor(Color.BLACK);
			g.drawLine(polygon_points[0][0], polygon_points[0][1], polygon_points[1][0], polygon_points[1][1]);
			g.drawLine(polygon_points[1][0], polygon_points[1][1], polygon_points[2][0], polygon_points[2][1]);
			g.drawLine(polygon_points[2][0], polygon_points[2][1], polygon_points[0][0], polygon_points[0][1]);
		}
	}	
}