import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class MainProgram extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private MyCanvas canvas = new MyCanvas();
	
	int x = 800, y = 700;
	int x_origin = x/2, y_origin = y/2;
	
	
	public static void main(String[] args){
		MainProgram p = new MainProgram();
	}
	
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
	

	
	private class MyCanvas extends Canvas{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g){
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
			
			g.setColor(Color.CYAN);
			g.drawLine(x/2, 0, x/2, y);
			g.drawLine(0, y/2, x, y/2);
			
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