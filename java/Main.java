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
		//test for map size
		System.out.println("size X:"+ m.sizeX +"\tY:"+ m.sizeY);

		//test the road of map
/*		for (Road temp : m.road){
			System.out.println(temp);
			System.out.println("shape:");
			for(Point p:temp.shape)
				System.out.print(p);
			System.out.println("\n");
		}

		//test the Intersection of map
		for(Intersection temp2 : m.intersect) {
			System.out.println("x:"+ temp2.x +" y:"+ temp2.y);
			for(Road temp : temp2.road) {
				System.out.println(temp);
			}
		}*/

		//test for map dispear point
/*		for(Point p : m.disPoint)
			System.out.println(p);*/

		//go run
		run(m,c);
	}
	public static void run(Map m,List<Car> c) {
		c.add(new Car(5,0,1,1,0,1,1,1,m.road.get(3)));
		while(!c.isEmpty()) { //while(1)
			for(int i=0 ; i<c.size() ; ){
				if(c.get(i).moveCar(m)) { //at disapear point
					c.remove(i);
//					System.out.println("true" +c.size());
					continue;
				}
				i++;
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
	public void addShape(Point p) {
		shape.add(p);
	}
	public boolean equal(Road r) { //comapre if two road equal or not
		return (this.startX == r.startX) && (this.startY == r.startY)
			&& (this.endX == r.endX) && (this.endY == r.endY) ;
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

	public boolean moveCar(Map m) {
		System.out.println("current:("+ xPoint +","+ yPoint +")");
		xPoint += xSpeed;
		yPoint += ySpeed;
		Point temp = new Point(xPoint ,yPoint);
		System.out.println("temp="+ temp);
		
		for(Point p : m.disPoint) {
			System.out.println("p="+p);
			if(p.equal(temp))
				return true; //if at disapear point return true
		}
//		if(m.disPoint.contains(temp))
//			return true;
		return false; //not at disapear point
	}
}

class Intersection
{
	public float x;
	public float y;
	public int roadnum;
	public double range;
	public List<Road> road;

	Intersection() {
		this(0,0,0);
	}
	Intersection(float ix ,float iy ,int rnum) {
		this(ix ,iy ,rnum ,0.5);
	}
	Intersection(float ix ,float iy ,int rnum ,double r) {
		x = ix;
		y = iy;
		roadnum = rnum;
		road = new ArrayList<Road>(2);
		range = r;
	}

	public void addRoad(Road r) {
		road.add(r);
		roadnum = road.size();
	}
	public boolean pointInRange(float ix ,float iy) {
		return xInRange(ix) && yInRange(iy) ;
	}
	public boolean pointInRange(Point p) {
		return xInRange(p.xPoint) && yInRange(p.yPoint);
	}
	public boolean xInRange(float ix) {
		return (x-range <= ix) && (ix <= x+range);
	}
	public boolean yInRange(float iy) {
		return (y-range <= iy) && (iy <= y+range);
	}
	@Override
	public String toString() {
		return String.format("(%.3f,%.3f) num:%d",x,y,roadnum);
	}
}
class Map
{
	public List<Intersection> intersect;
	public List<Road> road;
	public List<Point> disPoint;  //car disapear point
	public float sizeX;
	public float sizeY;

	Map() {
		road = new ArrayList<Road>(2);
		intersect = new ArrayList<Intersection>(2);
		disPoint = new ArrayList<Point>(2);
		String buf = readFile();
		getData(buf);
		getIntersect();
		getDisapearPoint();
	}
	
	private void getDisapearPoint() { //get car disapear point
		for(Road r : road) {
			disPoint.add(new Point(r.endX ,r.endY));
		}
	}

	private void getIntersect() { //analysis map for Intersection
		int roadnum = road.size();
		int i = 0 ,j = 0 ,k=0 ,w=0;
		Road temp,temp2;
		for(i=0 ; i<roadnum-1 ; i++) { //similar bubble sort for comparing two road component from road ArrayList
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

						//determine if new intersection or not
						if(k == intersect.size()) { //not find 
							//new intersection
							Intersection temp_intersect = new Intersection(point.xPoint ,point.yPoint ,2);
							temp_intersect.addRoad(temp); //add road to intersection
							temp_intersect.addRoad(temp2);
							intersect.add(temp_intersect); //add intersecion to ArrayList of Map
						}
						else { //find
							//find point in the intersection ArrayList
							for(Road temp3 : intersect.get(k).road) {
								if( temp3.equal(temp) ) {
									//if temp_road in List add temp2_road to List
									intersect.get(k).addRoad(temp2);
									break;
								}
								else if( temp3.equal(temp2) ) {
									//if temp_road2 in List add temp_road to List
									intersect.get(k).addRoad(temp);
									break;
								}
							}
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

		//split file in line by( "\n")
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
		//split
		line_count = 0;
		for(String a : line) {
			// split line in word (by "(")
			String[] b = a.split("\\(");
			Road r;
			point_index = 0;
			temp_point = new ArrayList<Float>(4);

			//split
			for(String c : b) {
				//split in subword by "," 
				String[] d = c.split(",");

				int temp2 = 0; 
				for(String f : d){ //loop for get digit
					if(line_count == 0) {  //first line for map size
						if( Character.isDigit(f.charAt(0)) && temp2 == 0) {
							sizeX = Float.parseFloat(f); //ex:10.0
							temp2 = (temp2 + 1) % 2;
//							System.out.println(sizeX);
						}
						else if ( Character.isDigit(f.charAt(0)) && temp2 == 1) {
							sizeY = Float.parseFloat( f.substring( 0,f.indexOf(")")) ); //substring for delete ')'
							temp2 = (temp2 + 1) % 2;
//							System.out.println(sizeY);
						}
					}
					else{ //other line for road
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
			if(line_count != 0) { //not first line ( size of map )
				
				//last two number of point not be a straight line
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

	private int checkLine(String line ,int num) { //check line format
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

		//check bracket '('  ')'  and '<'  '>'
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
	private String readFile() {
		return this.readFile("map2.txt");
	}
	private String readFile(String filename) { //read file
		String buf = new String();
		FileReader fin;
		try {
			fin = new FileReader(filename);
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
	public float xPoint; //x-axis of Point 
	public float yPoint; //y-axis of Point
	Point() {
		this(0,0);
	}
	Point(Point p) {
		this(p.xPoint ,p.yPoint);
	}
	Point(float x,float y) {
		xPoint = x;
		yPoint = y;
	}

	public boolean equal(Point p) { //compare two Point is equal or not
		return (xPoint == p.xPoint) && (yPoint == p.yPoint);
	}
	@Override
	public String toString(){
		return String.format("(%f,%f)",xPoint,yPoint);
	}
}
