package View;

public class Point2D {

	private double x, y;

	public Point2D(double x, double y) {

		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return x;
	}
	
	public void setX(double newX){
		this.x = newX;
	}
	
	public double getY(){
		return y;
	}
	
	public void setY(double newY){
		this.y = newY;
	}
	
	public Point2D add(Point2D other){
		return new Point2D(x+other.x,y+other.y);
	}
/*	
	public Point2D add(Vector2D other){
		return new Point2D(x+other.getX(),y+other.getY());
	}
	
	public Vector2D toVector(){
		return new Vector2D(x,y);
	}
*/
	
}
