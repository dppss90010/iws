import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Main
{
	public static void main(String[] args){
		Road r =new Road();
		Intersection in = new Intersection();
		Car c = new Car();
		run();
		Map m =new Map();
	}
	public static void run(){
		
	}
}

class Road
{
	public float startX;
	public float startY;
	public float endX;
	public float endY;
	Road(){
		this(0,0,0,0);
	}
	Road(float x,float y,float endx,float endy){
		startX = x;
		startY = y;
		endX = endx;
		endY = endy;
	}
}

class Car
{
	public float xPoint;
	public float yPoint;
	public float carLength;
	public float carWidth;
	public float xSpeed;
	public float ySpeed;
	public float xAcceleration;
	public float yAcceleration;
	public Road road;
	public Map map;

	Car(){
		this(0,0,0,0,0,0,0,0,new Road());
	}
	Car(float x,float y,float length,float width
		,float xs,float ys,float xAcce,float yAcce,Road r){
		xPoint = x;
		yPoint = y;
		carLength =length;
		carWidth = width;
		xSpeed = xs;
		ySpeed = ys;
		xAcceleration = xAcce;
		yAcceleration = yAcce;
		road = r;
	}
	void moveCar(){
		
	}
}
class Intersection
{
	public float x;
	public float y;
	int roadnum;
	public Road[] road;

	Intersection(){
		this(0,0,0);
	}
	Intersection(float ix,float iy,int rnum){
		x = ix;
		y = iy;
		roadnum = rnum;
	}
}
class Map
{
	public ArrayList<Intersection> intersect;
	public ArrayList<Road> road;
	public float sizeX;
	public float sizeY;

	Map(){
		int temp=0 ,tempx=0 ,tempy=0 ,tempxend=0 ,tempyend=0;
		String buf = new String();
		FileReader fin;
		try {
			fin = new FileReader("map.txt");
			int word;
			while (fin.ready()){
				word = fin.read();
				System.out.print((char)word);
				buf = buf + (char)word;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("buf:\n"+buf);
		int index = 0;
		while( buf.charAt(index)==' ' && index<buf.length() ) //ignore space before size
			index ++;
		while(buf.charAt(index)!='(')
			index++;
		index++; // ignore '('
//		System.out.println("now:"+buf.charAt(index));
		while(Character.isDigit(buf.charAt(index))){
//			System.out.println(buf.charAt(index));
			temp = temp*10 + buf.charAt(index)-'0';
			index++;
		}
		sizeX = temp ; //set X point of map

		index++; //ignore ','

		temp = 0;
		while(Character.isDigit(buf.charAt(index))){
			temp = temp*10 + buf.charAt(index)-'0';
			index++;
		}
		sizeY = temp;

		System.out.println("test:"+temp);
	}
}
