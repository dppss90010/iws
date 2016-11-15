import java.util.*;
//import javax.swing.*;
import java.io.*;
import java.math.*;
import java.net.*;
import java.lang.*;
import org.json.*;

public class Car extends Thread
{
	public static long num;
	public final String ID;
	public final String MAC;
	public Point current_location; //current location
	public Road current_road; //current road
	public Point current_start; //current road start location
	public Point current_end; //current road end location
	public int Speed; //m/s
	public Map m;
	public Car() { //initial Car
		num++;
		ID = String.valueOf(num);
		MAC = new String(String.format("AB-CD-EF-94-87-%02d",num));

		m = new Map();
		m.initmap("map.txt");
	//	for(Intersection p:m.intersection){
	//		System.out.println(p);
	//	}
	//	for(Point p : m.CarEnd){
	//		System.out.println(p);
	//	}

		//random for initial road form having EndPoint road
		Date date = new Date(); 
		Random rd=new Random();
		rd.setSeed( (int)date.getTime() ); //srand
		int num_temp = rd.nextInt( m.CarEnd.size() ); //0 ~ size-1
		current_location = new Point( m.CarEnd.get(num_temp) );

		//find road from ArrayList
		for(Road r:m.road) {
			if( r.start.equal(current_location) ) {
				current_road = r;
				current_start = r.start;
				current_end = r.end;
			}
			if( r.end.equal(current_location) ) {
				current_road = r;
				current_start = r.end;
				current_end = r.start;
			}
		}
		num_temp = rd.nextInt( 14 ); //0 ~ Max(14)-1 for Speed
		Speed = num_temp + 5; //Speed range 5~18 m/s
	}
	@Override
	public void run() {
/*		System.out.println(current_location);
		System.out.println(current_road);
		System.out.println(current_start);
		System.out.println(current_end);
*/	
		while(true){
			int s = 1; // per_move meter
			int i;
			/* print for test
			System.out.println("curSta: "+ current_start);
			System.out.println("curEnd: "+ current_end);
			*/

			//two Point X-axis difference
			double xd = current_end.xPoint.subtract(current_start.xPoint).doubleValue();
			//two Point Y-axis difference
			double yd = current_end.yPoint.subtract(current_start.yPoint).doubleValue();
			//sqrt(xd^2+yd^2)
			double z = Math.pow( xd*xd + yd*yd ,0.5);

			//print for test
/*			System.out.println("location= " +current_location);
			System.out.println("xd= " +xd);
			System.out.println("yd= " +yd);
			System.out.println("z= "  +z);*/

			//socket
			String json_test = "{\"orientation\":" +(864)+ ",\"speed\":" +Speed+ ",\"longitude\":" +current_location.yPoint+ ",\"latitude\":" +current_location.xPoint+ ",\"mac\":" +MAC+ "}";
			Client cli = new Client();
			String getMessage = cli.socket(json_test);
			System.out.println(getMessage);
			System.out.println("");
//			JSONObject json_data = new JSONObject(getMessage);
//			boolean torf = Boolean.valueOf(json_data.get("yorn").toString());
			String[] spilt_line = getMessage.split(":");
			String[] yorn = spilt_line[1].split(",");
			boolean torf = Boolean.valueOf(yorn[0]);
			if(torf){
				//random for dec speed
				Date date = new Date(); 
				Random rd=new Random();
				rd.setSeed( (int)date.getTime() ); //srand
				int num_temp = rd.nextInt( 3 )+4; //4~6
				Speed = Speed - num_temp;
				if(Speed<0)Speed = 0;
			}else{
				if(Speed<20)Speed++;
			}
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){
				System.out.println("Sleep error");
			}

			xd=xd/z;
			yd=yd/z;
			boolean flag = false;
			for(i=0 ; i<Speed ; ++i) {
				//s is per move meter and s/(lon/lat)= 1/0.000012
				current_location.xPoint = current_location.xPoint.add(new BigDecimal(xd*s*0.000012));
				current_location.yPoint = current_location.yPoint.add(new BigDecimal(yd*s*0.000012));
				//setScale 7 is after comma seven location
				current_location.xPoint = current_location.xPoint.setScale(7,BigDecimal.ROUND_HALF_UP);
				current_location.yPoint = current_location.yPoint.setScale(7,BigDecimal.ROUND_HALF_UP);

				//check if in intersection range or CarEnd
				if( checkRange(current_location,current_end) ) {
					//print for test
					//System.out.println("change: "+current_road);
					for(Point endCheck : m.CarEnd) {
						if( current_end.equal(endCheck) ) {
							System.out.println("This Car " +ID+ " End");
							flag = true; //car finish
							break;
						}
					}
					if(flag) {
						break; //car finish
					}
					//Road rt;
					for(Intersection in: m.intersection) {
						if( current_end.equal(in.location) ) {
							//random for new direction(road in intersection) 
							Date date = new Date(); 
							Random rd = new Random();
							rd.setSeed((int)date.getTime()); //srand
							int num_temp = rd.nextInt( in.roadID.size() ); //0 ~ size-1

							//
							//error m.road or in.road
							//
							while( current_road.equal( in.roadID.get(num_temp) ) ) {
								num_temp = rd.nextInt( in.roadID.size() );
							}
							current_road = in.roadID.get(num_temp);
							break;
						}
					}
					//set new current value 
					current_location = new Point(current_end);
					current_start = current_end;
					current_end = (current_start.equal(current_road.start)) ? current_road.end : current_road.start ;
					xd = current_end.xPoint.subtract(current_start.xPoint).doubleValue();
					yd = current_end.yPoint.subtract(current_start.yPoint).doubleValue();
					z = Math.pow( xd*xd + yd*yd ,0.5);
					xd=xd/z;yd=yd/z;
					i=i+4; //shift+4
				}
			}
			//System.out.println("location= " +current_location);
			System.out.println(this);
/*			WriteData w = new WriteData();
			w.writeToFile(current_location,"./car.txt");*/
			if(flag) {
				break; //caar finish
			}
			//Scanner scanner = new Scanner(System.in);
			//if(scanner.nextInt()==0){break;}

		}
		System.out.println(this);
	}
	public boolean checkRange(Point curr,Point goal){
		//System.out.println("check curr:"+ curr);
		//System.out.println("check goal:"+ goal);

		Point diff_temp = new Point(goal);
		diff_temp.subtract(curr);
		long diff_x = (long) (diff_temp.xPoint.doubleValue()*100000);
		long diff_y = (long) (diff_temp.yPoint.doubleValue()*100000);
		if( diff_x*diff_x + diff_y*diff_y <= 15*15) {
			return true;
		}
//		System.out.println("check:"+ diff_temp);
		return false;
	}
	@Override
	public String toString() {
		return String.format("MAC:%s \tCarID:%s \tSpeed:%d \tCurrent location:%s" ,MAC,ID ,Speed ,current_location);
	}
}
class Client {

	private String host;
	private int port;
	Client() {
		this("csie2.cs.ccu.edu.tw" ,10002);
	}
	Client(String host ,int port) {
		this.host = new String(host);
		this.port = port;
	}

	public String socket(String json_test) {
		try{
			Socket sock = new Socket(host ,port);
			PrintWriter pout = new PrintWriter(sock.getOutputStream(),true);
			InputStream in = sock.getInputStream();
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));

			pout.println(json_test);
			String line = "";
			if( (line = bin.readLine()) != null){
				//System.out.println(line);
			}
			else {
				System.out.println("Socket does not get message.");
			}
			sock.close();
			return line;
		}
		catch(IOException ioe){
			System.err.println(ioe);
		}
		return "";
	}
}
class WriteData {
	public void writeToFile(Point p ,String filename) {
		File file = new File(filename);
		try {
			FileWriter fw = new FileWriter(filename);
			fw.write(String.format("(\"%s\",\"%s\") \r\n"
						,p.xPoint ,p.yPoint) );
//			fw.write(new String("test\r\n"));
			fw.flush();
			fw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
class Map
{
	public List<Road> road;
	public List<Intersection> intersection; 
	public List<Point> CarEnd;
	Map(){
		road = new ArrayList<Road>(2);;
		intersection = new ArrayList<Intersection>(2);; 
		CarEnd = new ArrayList<Point>(2);;
	}
	void initmap(String filename) {
		Point size1;
		Point size2;
		String mapbuf = readFile(filename); //read Data
		String[] line = mapbuf.split("\n"); //split in line
		
		//split size and get size
		String[] temp1 = line[0].split("\"");
		size1 = new Point(temp1[1],temp1[3]);
		size2 = new Point(temp1[5],temp1[7]);
		
		//get road
		int linelen = line.length;
		for(int i=1 ; i<linelen ; ++i) {
			String[] temp2 = line[i].split("\"");
			//Road r = new Road(temp2[1],new Point(temp2[3],temp2[5]),new Point(temp2[7],temp2[9]));
			int tlen = temp2.length;
			for(int j=11 ; j<tlen-4 ; j+=4) {
				Road r = new Road(temp2[1]+"_"+((j-11)>>2),new Point(temp2[j],temp2[j+2]),new Point(temp2[j+4],temp2[j+6])); //raodID_0~roadID_n
				road.add(r);
			}
		}
		
		//get Intersection
		/*
		  depart with start point and End point
		  each point compare with other point
		*/
		int Roadnum = road.size();
		for(int i=0 ; i<Roadnum ; ++i) { //Roadnum-1 simulate bubble sort

			//start point
			int flag = 0;
			Point temppoint = road.get(i).start;
			for(int j=0 ; j<intersection.size() ; ++j) {
				if( intersection.get(j).location.equal(temppoint) ) {
					++flag;
					break;
				}
			}
			if(flag == 0) {
				Intersection intersect = new Intersection(temppoint);
				for(int j=i+1 ; j<Roadnum ; ++j) {
					if( road.get(j).start.equal(temppoint) ) {
						if(flag == 0) {//first equal
							intersect.addRoad( road.get(i) );
							intersect.addRoad( road.get(j) );
							++flag; //add flag to avoid duplicate
						}
						else {
							intersect.addRoad( road.get(j) );
							++flag;
						}
					}
					else if( road.get(j).end.equal(temppoint) ) {
						if(flag == 0) {//first equal
							intersect.addRoad( road.get(i) );
							intersect.addRoad( road.get(j) );
							++flag;
						}
						else {
							intersect.addRoad( road.get(j) );
							++flag;
						}
					}
				}
				if(flag == 0) {
					//print for test
					//System.out.println("Car end: "+temppoint);
					CarEnd.add(temppoint);
				}
				else {
					intersection.add(intersect);
				}
			}
			if(flag==0 && i==Roadnum-1){
				CarEnd.add(temppoint);
			}
			//print for test
			/*
			if(flag>1){
				System.out.println("Intersection: "+temppoint);
			}*/

			//end point
			flag = 0;
			temppoint = road.get(i).end;
			for(int j=0 ; j<intersection.size() ; ++j) {
				if( intersection.get(j).location.equal(temppoint) ) {
					++flag;
					break;
				}
			}
			if(flag == 0) {
				Intersection intersect = new Intersection(temppoint);
				for(int j=i+1 ; j<Roadnum ; ++j) {
					if( road.get(j).start.equal(temppoint) ) {
						if(flag == 0) {//first equal
							intersect.addRoad( road.get(i) );
							intersect.addRoad( road.get(j) );
							++flag;
						}
						else {
							intersect.addRoad( road.get(j) );
							++flag;
						}
					}
					else if( road.get(j).end.equal(temppoint) ) {
						if(flag == 0) {//first equal
							intersect.addRoad( road.get(i) );
							intersect.addRoad( road.get(j) );
							++flag;
						}
						else {
							intersect.addRoad( road.get(j) );
							++flag;
						}
					}
				}
				if(flag == 0) {
					//print for test
					//System.out.println("Car end: "+temppoint);
					CarEnd.add(temppoint);
				}
				else {
					intersection.add(intersect);
				}
			}
			if(flag==0 && i==Roadnum-1){
				CarEnd.add(temppoint);
			}
			//print for test
			/*
			if(flag>1){
				System.out.println("Intersection: "+temppoint);
			}*/

		}
		//
		//compare last one if it is car end?
		//
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
class Road
{
	public String ID;
	public Point start;
	public Point end;
	Road(String id ,Point p1 ,Point p2) {
		ID = id;
		start = p1;
		end = p2;
	}
	
	public Point getStartPoint() { //return start Point
		return start;
	}
	public Point getEndPoint() { //return end Point
		return end;
	}
	public boolean equal(Road r) { //compare tow road
		return this.ID.equals(r.ID);
	}
	@Override
	public String toString() {
		return String.format("start:%s ,end:%s",start,end);
	}
}

class Intersection
{
	public Point location;
	public List<Road> roadID;
	Intersection(Point p) {
		location = p;
		roadID = new ArrayList<Road>(2);
	}

	public Point getPoint() {
		return location;
	}
	public void addRoad(Road r) { //add road to Intersection (road ArrayList)
		roadID.add(r);
	}
	@Override
	public String toString() {
		return String.format("%s num:%d" ,location,roadID.size());
	}
}
