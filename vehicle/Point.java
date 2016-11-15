import java.util.*;
import javax.swing.*;
import java.io.*;
import java.math.*;

public class Point 
{
	public BigDecimal xPoint; //x-axis of Point
	public BigDecimal yPoint; //y-axis of Point
	Point() {
		this("","");
	}
	Point(Point p) {
		this(p.xPoint.toString() ,p.yPoint.toString());
	}
	Point(String x ,String y) {
		xPoint = new BigDecimal(x);
		yPoint = new BigDecimal(y);
	}

	public double getXDouble() {
		return xPoint.doubleValue();
	}
	public double getYDouble() {
		return yPoint.doubleValue();
	}
	public void subtract(Point p) {
		xPoint = xPoint.subtract(p.xPoint);
		yPoint = yPoint.subtract(p.yPoint);
	}
//	public Point newSubtract(Point p) {
//		return new Point(xPoint.subtract(p.xPoint), yPoint.subtract(p.yPoint) );
//	}
	public boolean equal(Point p) { //compare two Point is equal or not
		return (xPoint.compareTo(p.xPoint) == 0) && (yPoint.compareTo(p.yPoint) == 0);
	}
	@Override
	public String toString(){
		return String.format("(%s,%s)",xPoint,yPoint);
	}
}
