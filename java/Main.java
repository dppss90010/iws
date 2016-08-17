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

		//test Speed
		r = new Road(0,5,5,5);
		Car ca = new Car(0,5,1,1,1,1,r,m);
		r = new Road(5,0,5,5);
		ca = new Car(5,0,1,1,1,1,r,m);
		r = new Road(0,0,5,5);
		ca = new Car(0,0,1,1,1,1,r,m);
	}
	public static void run(Map m,List<Car> c) {
		int temp = 0;
		Algorithm alg = new Algorithm();
		c.add(new Car(5,0,1,1,1,0,m.road.get(3),m));
		c.add(new Car(10,5,1,1,1,0,m.road.get(1),m));
//		c.add(new Car(4,10,1,1,1,0,m.road.get(2),m));
		while(!c.isEmpty() && temp <100) { //while(1)
			//test boom
			if(temp ==0 ) {
				//c.add(new Car(10,5,1,1,1,0,m.road.get(1),m));
			}

			//detection for collision
			int csize = c.size();
			for(int i=0 ; i < csize-1 ; ++i) { //two car Compare if Collision
				for(int j=i+1 ; j<csize ; ++j) {
					for(int k=0 ; k<6;++k) { //in 6 second 
						if(alg.collision(k,c.get(i),c.get(j)) ){
							System.out.println("Warning:boom in "+ k +" second.");
						}
					}
				}
			}

			//movo Car
			for(int i=0 ; i<c.size() ; ){
				System.out.print("current:("+ c.get(i).xPoint +","+ c.get(i).yPoint +")    ");
				if(c.get(i).moveCar()) { //at disappear point
					c.remove(i);
//					System.out.println("true" +c.size());
					continue;
				}
				i++;
			}
			System.out.println("");
//			c.add(new Car(5,0,1,1,0,1,1,1,m.road.get(3)));
			temp++;
		}
	}
}

class Algorithm //Algorithm for detection collision
{
	static public final double perMove = 1.0;
	Algorithm() {
	}
	public boolean collision(double t,Car car1,Car car2) {
		//Simple Edition
		double car1_newX = preMovFunction(car1 ,1 ,t); // 1 represent X-axis
		double car1_newY = preMovFunction(car1 ,0 ,t); // 0 represent Y-axis
		double car2_newX = preMovFunction(car2 ,1 ,t); // 1 represent X-axis
		double car2_newY = preMovFunction(car2 ,0 ,t); // 0 represent Y-axis

		//Simple Edition
		double Sx = ( max(car1_newX+0.5 ,car1_newX-0.5 ,car2_newX+0.5 ,car2_newX-0.5)
					-min(car1_newX+0.5 ,car1_newX-0.5 ,car2_newX+0.5 ,car2_newX-0.5) )
				-( (Math.max(car1_newX+0.5,car1_newX-0.5)-Math.min(car1_newX+0.5,car1_newX-0.5)) 
				  +(Math.max(car2_newX+0.5,car2_newX-0.5)-Math.min(car2_newX+0.5,car2_newX-0.5)) );
		double Sy = ( max(car1_newY+0.5 ,car1_newY-0.5 ,car2_newY+0.5 ,car2_newY-0.5)
					-min(car1_newY+0.5 ,car1_newY-0.5 ,car2_newY+0.5 ,car2_newY-0.5) )
				-( (Math.max(car1_newY+0.5,car1_newY-0.5)-Math.min(car1_newY+0.5,car1_newY-0.5)) 
				  +(Math.max(car2_newY+0.5,car2_newY-0.5)-Math.min(car2_newY+0.5,car2_newY-0.5)) );
		
		if(Sx<0 && Sy<0) { 
			//if Sx and Sy smaller than 0 represent it may be collision
			return true;
		}
		return false;
	}
	public double preMovFunction(Car car,int direction,double t) { 
		//preMovFuntion return after "t" second of Car Point
		double a,b,c;
		a = car.road.endX - car.road.startX;
		b = car.road.endY - car.road.startY;
		c = a*a + b*b;
		c = Math.sqrt(c);
		a = a / c;
		b = b / c;
		if(direction == 1){ // if 1 return X-axis
			return car.xPoint + car.Speed*a*t;
		}else{
			return car.yPoint + car.Speed*b*t;
		}
	}
	static public double max(double arg1 ,double arg2 ,double arg3 ,double arg4) {
		return Math.max( Math.max(arg1,arg2) ,Math.max(arg3,arg4) );
	}
	static public double max(double arg1 ,double arg2 ,double arg3 ,double arg4 ,
		double arg5 ,double arg6 ,double arg7 ,double arg8) 
	{
		return Math.max( max(arg1,arg2,arg3,arg4) ,max(arg5,arg6,arg7,arg8) );
	}
	static public double min(double arg1 ,double arg2 ,double arg3 ,double arg4) {
		return Math.min( Math.min(arg1,arg2) ,Math.min(arg3,arg4) );
	}
	static public double min(double arg1 ,double arg2 ,double arg3 ,double arg4 ,
		double arg5 ,double arg6 ,double arg7 ,double arg8) 
	{
		return Math.min( min(arg1,arg2,arg3,arg4) ,min(arg5,arg6,arg7,arg8) );
	}
}
class Road
{
	public double startX;
	public double startY;
	public double endX;
	public double endY;
	public List<Point> shape;

	Road() {
		this(0,0,0,0);
	}
	Road(double x,double y,double endx,double endy) {
		startX = x;
		startY = y;
		endX = endx;
		endY = endy;
		shape = new ArrayList<Point>(2);
	}
	public Point getStartPoint() { //return start Point
		return new Point(startX,startY);
	}
	public Point getEndPoint() { //return end Point
		return new Point(endX,endY);
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
	public double xPoint;
	public double yPoint;
	public double carLength;
	public double carWidth;
	public double xSpeed;
	public double ySpeed;
	public double Speed;
	public double xAcceleration;
	public double yAcceleration;
	public double Acceleration;
	public Road road;
	public Map map;
	static public final double perSpeed = 1.0;

	Car() {
		this(0 ,0 ,0 ,0 ,0 ,0 ,new Road() ,new Map());
	}
	Car(double x ,double y ,double length ,double width
		,double s ,double Acce  ,Road r ,Map m) {
		xPoint = x;
		yPoint = y;
		carLength =length;
		carWidth = width;
		Speed = s;
		Acceleration = Acce;
		road = r;
		map = m;
	}

	public void movFunction() {
		//move car location and parting Speed to X-axis and Y-axis
		double a,b,c;
		a = road.endX - road.startX;
		b = road.endY - road.startY;
		c = a*a + b*b;
		c = Math.sqrt(c);
		a = a / c;
		b = b / c;
		xPoint += perSpeed*a;
		yPoint += perSpeed*b;
	}
	public boolean isAtDisPoint() { //if it is at disappear Point
		Point temp = new Point(xPoint ,yPoint);
		for(Point p : map.disPoint) {
//			System.out.println("disappear point:"+p);
			if(p.equal(temp))
				return true; //if at disappear point return true
		}
		return false;
	}
	public boolean isAtRoadEnd() {
		Point temp = new Point(xPoint ,yPoint);
		for(Road r : map.road) {
			if( temp.equal(r.getEndPoint()) )
				return true;
		}
		return false;
	}
	public boolean findRoadStart() {
		Point temp = new Point(xPoint ,yPoint);
		for(Road r : map.road) {
			if( temp.equal(r.getStartPoint()) ) {
				road = r;
				return true;
			}
		}
		return false;
	}
	public boolean isIntersection() { 
		//if is at Intersection and it would be random for turn around or not

		Point temp = new Point(xPoint ,yPoint);
		for(Intersection in : map.intersect) { //find if the Point is Intersection or not
			if( temp.equal(in.getPoint()) ) {
				int num_temp;
				Date date = new Date();
				Random rd=new Random();
				rd.setSeed((int)date.getTime());
				num_temp = rd.nextInt(in.roadnum); //0 ~ (roadnum-1)
				road = in.road.get(num_temp);
				return true;
			}
		}
		return false;
	}
	public boolean moveCar() { 
		//if return true represent that car at disappear point

		for(double i=0 ; i<Speed ; i++) {
			movFunction();
			if( isAtRoadEnd() ){ //decide if at road end or not
				if(isAtDisPoint()){ //decide if at map edge or other Car disappear Point
					return true;
				}
				if( !findRoadStart() ) { //find if at raod end is another Road start Point
					System.out.println("error:not find road start Point");
					System.exit(1);
				}
			}
			if( isIntersection() ) { //decide is Intersection or not
				System.out.print(" find Intersection ");
			}
		}

//		if(m.disPoint.contains(temp))
//			return true;
		return false; //not at disappear point
	}
}

class Intersection
{
	public double x;
	public double y;
	public int roadnum;
	public double range;
	public List<Road> road;

	Intersection() {
		this(0,0,0);
	}
	Intersection(double ix ,double iy ,int rnum) {
		this(ix ,iy ,rnum ,0.5);
	}
	Intersection(double ix ,double iy ,int rnum ,double r) {
		x = ix;
		y = iy;
		roadnum = rnum;
		road = new ArrayList<Road>(2);
		range = r;
	}

	public Point getPoint() {
		return new Point(x,y);
	}
	public void addRoad(Road r) { //add road to Intersection (road ArrayList)
		road.add(r);
		roadnum = road.size(); //update road number 
	}
	public boolean pointInRange(double ix ,double iy) { //if point in the Intersection range
		return xInRange(ix) && yInRange(iy) ;
	}
	public boolean pointInRange(Point p) {
		return xInRange(p.xPoint) && yInRange(p.yPoint); 
	}
	public boolean xInRange(double ix) { //if the X-axis of point in the Intersection range
		return (x-range <= ix) && (ix <= x+range);
	}
	public boolean yInRange(double iy) { //if the Y-axis of point in the Intersection range
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
	public List<Point> disPoint;  //car disappear point
	public double sizeX;
	public double sizeY;

	Map() {
		road = new ArrayList<Road>(2);
		intersect = new ArrayList<Intersection>(2);
		disPoint = new ArrayList<Point>(2);
		String buf = readFile();
		getData(buf);
		getIntersect();
		getDisapearPoint();
	}
	
	private void getDisapearPoint() { //get car disappear point
		for(Road r : road) {
			if( (r.endX == sizeX) || (r.endY == sizeY)
				|| (r.endX == 0) || (r.endY == 0 ) )  //if at boundary and add to disPoint
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
		double x = getSolFunX(r1,r2);
		double y = getSolFunY(r1,r2);
		
		if(x == 987654321)
			return null;

		return new Point(x,y);
	}
	private double getSolFunX(Road r1,Road r2) { // y = ax+b for soluton x
		double a1 = getSolFunA(r1);
		double a2 = getSolFunA(r2);
		double b1 = getSolFunB(r1);
		double b2 = getSolFunB(r2);

		if(a1 == 987654321 && a2!=987654321) //r1 is vertical
			return r1.startX;
		else if(a1 != 987654321 && a2==987654321) //r2 is vertical
			return r2.startX;
		else if(a1 == a2)
			return 987654321; //a1 and a2 parallel
		return (b2-b1) / (a1-a2);
	}
	private double getSolFunY(Road r1,Road r2) { // y = ax+b for soluton y
		double a1 = getSolFunA(r1);
		double a2 = getSolFunA(r2);
		double b1 = getSolFunB(r1);
		double b2 = getSolFunB(r2);

		if(a1 == 987654321 && a2!=987654321) //r1 is vertical
			return r1.startX*a2 + b2;
		else if(a1 != 987654321 && a2==987654321) //r2 is vertical
			return r2.startX*a1 + b1;
		else if(a1 == a2)
			return 987654321; //a1 and a2 parallel
		return getSolFunX(r1,r2)*a1 + b1;
	}
	private double getSolFunA(Road r) { // y = ax+b for soluton a
		if((r.endX - r.startX) == 0)
			return 987654321;
		return (r.endY-r.startY) / (r.endX-r.startX); //slope for function
	}
	private double getSolFunB(Road r) { // y = ax+b for soluton b
		double a = getSolFunA(r);
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
					r = new Road(temp_point.get(i).doubleValue() ,temp_point.get(i+1).doubleValue()
							,temp_point.get(i+2).doubleValue() ,temp_point.get(i+3).doubleValue());
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
	public double xPoint; //x-axis of Point 
	public double yPoint; //y-axis of Point
	Point() {
		this(0,0);
	}
	Point(Point p) {
		this(p.xPoint ,p.yPoint);
	}
	Point(double x,double y) {
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
