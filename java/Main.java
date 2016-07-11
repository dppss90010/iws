import java.util.*;
import javax.swing.*;
import java.io.*;

public class Main
{
	public static void main(String[] args) {
		Road r =new Road();
		Intersection in = new Intersection();
		List<Car> c = new ArrayList<Car>(1);
		Map m =new Map();
//		System.out.println("size X:"+ m.sizeX +"\tY:"+ m.sizeY);
//		for (Road temp : m.road){
//			System.out.println(temp);
//			System.out.println("shape:");
//			for(Point p:temp.shape)
//				System.out.print(p);
//			System.out.println("\n");
//		}
		for(Intersection temp2 : m.intersect) {
			System.out.println("x:"+ temp2.x +" y:"+ temp2.y);
			for(Road temp : temp2.road) {
				System.out.println(temp);
			}
		}
		run(m,c);
	}
	public static void run(Map m,List<Car> c) {
		for(;;) {
			c.add(new Car(5,0,1,1,1,1,1,1,m.road.get(3)));
			for(Car car : c){
				car.moveCar(m);
			}
		}
	}
}

class Road
{
	public float startX;
	public float startY;
	public float endX;
	public float endY;
	public List<Point> shape;

	Road() {
		this(0,0,0,0);
	}
	Road(float x,float y,float endx,float endy) {
		startX = x;
		startY = y;
		endX = endx;
		endY = endy;
		shape = new ArrayList<Point>(2);
	}
	void addShape(Point p) {
		shape.add(p);
	}
	@Override
	public String toString() {
		return String.format("start:(%.3f,%.3f) ,end(%.3f,%.3f)",startX,startY,endX,endY);
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

	Car() {
		this(0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,new Road() );
	}
	Car(float x ,float y ,float length ,float width
		,float xs ,float ys ,float xAcce ,float yAcce ,Road r) {
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

	void moveCar(Map m) {
		System.out.println(xPoint +" "+ yPoint);
		xPoint += xSpeed;
		yPoint += ySpeed;
	}
}
class Intersection
{
	public float x;
	public float y;
	int roadnum;
	public List<Road> road;

	Intersection() {
		this(0,0,0);
	}
	Intersection(float ix,float iy,int rnum) {
		x = ix;
		y = iy;
		roadnum = rnum;
		road = new ArrayList<Road>(2);
	}
	void addRoad(Road r) {
		road.add(r);
		roadnum = road.size();
	}
}
class Map
{
	public List<Intersection> intersect;
	public List<Road> road;
	public float sizeX;
	public float sizeY;

	Map() {
		road = new ArrayList<Road>(2);
		intersect = new ArrayList<Intersection>(2);
		String buf = readFile();
		getData(buf);
		getIntersect();
	}

	private void getIntersect() {
		int roadnum = road.size();
		int i = 0 ,j = 0 ,k=0 ,w=0;
		Road temp,temp2;
		for(i=0 ; i<roadnum-1 ; i++) {
			temp = road.get(i);
//			System.out.println(temp);
			for(j=i+1 ; j<roadnum ; j++) {
				temp2 = road.get(j);
				Point point = getSolFun(temp,temp2);
//				System.out.println(point);
				if(point != null) {
					if( Math.max(temp.startX,temp.endX) >= point.xPoint 
						&& Math.min(temp.startX,temp.endX) <= point.xPoint 
						&& Math.max(temp.startY,temp.endY) >= point.yPoint 
						&& Math.min(temp.startY,temp.endY) <= point.yPoint
						&& Math.max(temp2.startX,temp2.endX) >= point.xPoint 
						&& Math.min(temp2.startX,temp2.endX) <= point.xPoint 
						&& Math.max(temp2.startY,temp2.endY) >= point.yPoint 
						&& Math.min(temp2.startY,temp2.endY) <= point.yPoint ) {

						for(k=0 ; k<intersect.size() ; k++) {
							if(point.xPoint == intersect.get(k).x && point.yPoint == intersect.get(k).y) {
								break;
							}
						}
						if(k == intersect.size()) { //not find
							Intersection temp_intersect = new Intersection(point.xPoint ,point.yPoint ,2);
							temp_intersect.addRoad(temp); //add road to intersection
							temp_intersect.addRoad(temp2);
							intersect.add(temp_intersect); //add intersecion to ArrayList of Map
						}
						else {
							
						}

					}
				}
			}
		}
	}
	private Point getSolFun(Road r1,Road r2) {
		int tmep = 0;
		float x = getSolFunX(r1,r2);
		float y = getSolFunY(r1,r2);
		
		if(x == 987654321)
			return null;

		return new Point(x,y);
	}
	private float getSolFunX(Road r1,Road r2) { // y = ax+b for soluton x
		float a1 = getSolFunA(r1);
		float a2 = getSolFunA(r2);
		float b1 = getSolFunB(r1);
		float b2 = getSolFunB(r2);

		if(a1 == 987654321 && a2!=987654321) //r1 is vertical
			return r1.startX;
		else if(a1 != 987654321 && a2==987654321) //r2 is vertical
			return r2.startX;
		else if(a1 == a2)
			return 987654321; //a1 and a2 parallel
		return (b2-b1) / (a1-a2);
	}
	private float getSolFunY(Road r1,Road r2) { // y = ax+b for soluton y
		float a1 = getSolFunA(r1);
		float a2 = getSolFunA(r2);
		float b1 = getSolFunB(r1);
		float b2 = getSolFunB(r2);

		if(a1 == 987654321 && a2!=987654321) //r1 is vertical
			return r1.startX*a2 + b2;
		else if(a1 != 987654321 && a2==987654321) //r2 is vertical
			return r2.startX*a1 + b1;
		else if(a1 == a2)
			return 987654321; //a1 and a2 parallel
		return getSolFunX(r1,r2)*a1 + b1;
	}
	private float getSolFunA(Road r) { // y = ax+b for soluton a
		if((r.endX - r.startX) == 0)
			return 987654321;
		return (r.endY-r.startY) / (r.endX-r.startX); //slope for function
	}
	private float getSolFunB(Road r) { // y = ax+b for soluton b
		float a = getSolFunA(r);
		if(a == 987654321) // vertical
			return a;
		return (r.endY - (a * r.endX)); //intercept for function
	}
	private void getData(String buf) {
		int line_count = 0 ;
		int point_index = 0;
		List<Float> temp_point ;

		//split file in line 
		String[] line = buf.split("\n");  
		line_count = 0;
		for(String a : line) {
			//check line
			if(checkLine(a ,line_count + 1) < 0){
				System.out.println("line:"+ (line_count+1) + " is not correct format.");
				return;  //exit
			}
			line_count++;
		}

		//get each line of need data
		// split line into string of word (by "(")
		line_count = 0;
		for(String a : line) {
			String[] b = a.split("\\("); // split line into string of word (by "(")
			Road r;
			point_index = 0;
			temp_point = new ArrayList<Float>(4);

			//split in subword by ","
			for(String c : b) {
				String[] d = c.split(",");

				int temp2 = 0;
				for(String f : d){
					if(line_count == 0) {
						if( Character.isDigit(f.charAt(0)) && temp2 == 0) {
							sizeX = Float.parseFloat(f); //ex:10.0,
							temp2 = (temp2 + 1) % 2;
//							System.out.println(sizeX);
						}
						else if ( Character.isDigit(f.charAt(0)) && temp2 == 1) {
							sizeY = Float.parseFloat( f.substring( 0,f.indexOf(")")) );
							temp2 = (temp2 + 1) % 2;
//							System.out.println(sizeY);
						}
					}
					else{
						if( Character.isDigit(f.charAt(0)) && temp2 == 0) {
							temp_point.add( Float.parseFloat(f) );
							temp2 = (temp2 + 1) % 2;
//							System.out.println("index:"+ point_index +" "+ temp_point.get(point_index));
							point_index++ ;
						}
						else if ( Character.isDigit(f.charAt(0)) && temp2 == 1) {
							temp_point.add( Float.parseFloat( f.substring( 0,f.indexOf(")")) ) );
							temp2 = (temp2 + 1) % 2;
//							System.out.println("index:"+ point_index +" "+ temp_point.get(point_index));
							point_index++ ;
						}
					}

				}

			}

			//add road to map road ArrayList
			if(line_count != 0) { //not first line ( size of map)
				
				//last two number of point not be a line
				for(int i=4 ; i<temp_point.size()-2 ; i=i+2 ){ 
					r = new Road(temp_point.get(i).floatValue() ,temp_point.get(i+1).floatValue()
							,temp_point.get(i+2).floatValue() ,temp_point.get(i+3).floatValue());
					road.add(r);
				}
			}

			line_count++; //add count of line
		}

//		System.out.println("X:"+ sizeX +"Y:"+ sizeY);
	}

	private int checkLine(String line ,int num) {
		int return_num = -1;
		if(num < 1){
			return_num = -1; //error line number
		}
		else if(num == 1){
			if(line.indexOf("size") < 0)
				return_num = -2; //not find
			else
				return_num = 1;
		}
		else {
			if(line.indexOf("road") < 0)
				return_num = -3; //not find
			else
				return_num = 1;
		}

		//check bracket
		int count_bracket = 0;
		int count_compare_bracket = 0;
		for(int i=0 ; i < line.length() ; i++) {
			if(line.charAt(i) == '(' ) 
				count_bracket++;
			else if(line.charAt(i) == ')' )
				count_bracket--;

			if(line.charAt(i) == '<')
				count_compare_bracket++;
			else if(line.charAt(i) == '>')
				count_compare_bracket--;
		}
		if(count_bracket != 0 ||  count_compare_bracket != 0)
			return_num = -4;

//		System.out.println( return_num );
		return return_num;
	}
	private String readFile(){
		//read file
		String buf = new String();
		FileReader fin;
		try {
			fin = new FileReader("map2.txt");
			int word;
			while ( fin.ready() ) {
				word = fin.read();
//				System.out.print((char)word);
				buf = buf + (char)word;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("buf:\n"+buf); test read file
		return buf;
	}
}
class Point 
{
	public float xPoint;
	public float yPoint;
	Point() {
		this(0,0);
	}
	Point(float x,float y) {
		xPoint = x;
		yPoint = y;
	}
	@Override
	public String toString(){
		return String.format("(%f,%f)",xPoint,yPoint);
	}
}
