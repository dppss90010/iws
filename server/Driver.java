import java.net.*;
import java.io.*;
import java.util.Calendar;
import java.util.Scanner;
import java.lang.*;
import org.json.*;


class Client implements Runnable{
	int num;
	Socket cli;
	InputStream in; 
	String line,jsontest;
	Thread cal_thrd=new Thread();
	int cal_vel=0;
	float cal_deg=0,cal_long=0,cal_lat=0;
	Calendar cal_time;
	String cal_UID;
	boolean yorn=false;
	boolean connect_yorn = true;
	int radius = 0;
	Calculate cal ;
	JSONObject json_data,return_data;
	String json_test;
	float inter_lon=0,inter_lat=0;
	int i=0,R=6371;
	double ari_time; 
	//FileWriter fw;// = new FileWriter("Data.txt");
	
	public Client(Socket cli,float inter_lon,float inter_lat,int radius){     //constructor
		cal_time = Calendar.getInstance();
		this.cli = cli;
		this.inter_lon = inter_lon;
		this.inter_lat = inter_lat;
		this.radius = radius;
		System.out.println("create a connection");
		try{
				in = cli.getInputStream();
		}catch(IOException ioe){
				System.out.println("get input stream fail");
				connect_yorn = false;
		}
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		String line = "";
		try{
				line = bin.readLine();
		}catch(IOException ioe){
				System.out.println("get string fail");
				connect_yorn = false;
		}
		try{
			json_data = new JSONObject(line);
		}catch(JSONException je){
				System.out.println("Get data fail!");
				connect_yorn = false;
		}
		//System.out.println(line);
		//String process to get cal_data
		line = json_data.toString();
		System.out.println(line);
		try{
				this.cal_deg = Float.parseFloat(json_data.get("orientation").toString());
		}catch(JSONException je){
				System.out.println("Get orientation fail!");
				connect_yorn = false;}
		try{
				this.cal_vel = (Integer)json_data.get("speed");
		}catch(JSONException je){
				System.out.println("Get speed fail!");
				connect_yorn = false;}
		try{
				this.cal_long = Float.parseFloat(json_data.get("longitude").toString());
		}catch(JSONException je){
				System.out.println("Get longitude fail!");
				connect_yorn = false;}
		try{
				this.cal_lat = Float.parseFloat(json_data.get("latitude").toString());
		}catch(JSONException je){
				System.out.println("Get latitude fail!");
				connect_yorn = false;}
		try{
				this.cal_UID = json_data.get("mac").toString();
		}catch(JSONException je){
				System.out.println("Get mac fail!");
				connect_yorn = false;}

		cal_time = Calendar.getInstance();
	}
	public void run(){   //test for connection
		double lat_dis,lon_dis;
		cal = new Calculate(this.cal_vel,this.cal_deg,this.cal_long,this.cal_lat,this.cal_time,this.cal_UID,this.inter_lon,this.inter_lat,this.radius);
		cal_thrd = new Thread(cal);
		cal_thrd.start();             
		try{
				cal_thrd.join();
		}catch(InterruptedException ioe){
			System.out.println("calculate fail!");
		}
		yorn = cal.will_col();

		replace(cal_vel,cal_deg,cal_long,cal_lat,cal_time,cal_UID);
		lat_dis = Math.toRadians(this.cal_lat - this.inter_lat);
		lon_dis = Math.toRadians(this.cal_long - this.inter_lon);
		double a = Math.sin(lat_dis/2)*Math.sin(lat_dis/2)+Math.cos(Math.toRadians(this.inter_lat))*Math.cos(Math.toRadians(this.cal_lat))*Math.sin(lon_dis/2)*Math.sin(lon_dis/2);
		double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
		double distance = R * c * 1000;
		ari_time = distance/(double)this.cal_vel;
		try{				
			PrintWriter pout = new PrintWriter(cli.getOutputStream(),true);
			//pout.println(Boolean.toString(yorn));

			//json_test = "{\"yorn\":ture,\"latitude\":23,\"longitude\":120}";
			json_test ="{\"yorn\":"+yorn+",\"latitude\":"+this.inter_lat+",\"longitude\":"+this.inter_lon+",\"time\":"+ari_time+"}";
			pout.println(json_test);

			//pout.println(return_data.toString());
			cli.close();
			System.out.println("Finish one connection!");
		}catch(IOException ioe){
			System.out.println("sent data to client fail");
		}
	}
	
	public void replace(int cal_vel,float cal_deg,float cal_long,float cal_lat ,Calendar cal_time,String cal_UID){
		int i,current=0;
		for(i=0;i<100;i++){
			if(Data.car_UID[i].equals(cal_UID)){
					current = i;
					break;
			}
			if(Data.car_UID[i].equals("")){
				current = i;
				break;
			}
			if(Data.car_time[current].compareTo(Data.car_time[i])>0){
				current = i;
			}
			else continue;
		}
		Data.car_vel[current] = cal_vel;
		Data.car_long[current] = cal_long;
		Data.car_lat[current] = cal_lat;
		Data.car_deg[current] = cal_deg;
		Data.car_UID[current] = cal_UID;
		Data.car_time[current] = cal_time;
	}
}

class Calculate implements Runnable{    //predict the collision
	int cal_vel;
	float cal_deg,cal_long,cal_lat;
	Calendar cal_time;	
	String cal_UID;
	boolean collision = false;
	float inter_lon,inter_lat;
	double lon_dis=0,lat_dis=0,distance=0,ari_time=0,ari_in=0,ari_out=0;
	int R = 6371;
	int radius = 0;
	public Calculate(int cal_vel ,float cal_deg,float cal_long,float cal_lat,Calendar cal_time,String cal_UID,float inter_lon,float inter_lat,int radius){   //constructo
		//System.out.println(cal_vel);
		this.cal_vel = cal_vel;
		this.cal_deg = cal_deg;
		this.cal_long = cal_long;
		this.cal_lat = cal_lat;
		this.cal_time = cal_time;
		this.cal_UID = cal_UID;
		this.inter_lon = inter_lon;
		this.inter_lat = inter_lat;
		this.radius = radius;

		lat_dis = Math.toRadians(this.cal_lat - this.inter_lat);
		lon_dis = Math.toRadians(this.cal_long - this.inter_lon);
		double a = Math.sin(lat_dis/2)*Math.sin(lat_dis/2)+Math.cos(Math.toRadians(this.inter_lat))*Math.cos(Math.toRadians(this.cal_lat))*Math.sin(lon_dis/2)*Math.sin(lon_dis/2);
		double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
		distance = R * c * 1000;
		ari_time = distance/(double)this.cal_vel;
		ari_in = ari_time - (double)radius/this.cal_vel;
		ari_out = ari_time + (double)radius/this.cal_vel;
	}
	
	public void run(){
		int i;
		double a,c,dis,cal_time=0,cal_in=0,cal_out=0;
		double bef_dis=-1;
		int dif_time;

		/*
		collision predict calculation
		and set collision
		*/
		for(i=0;i<100;i++){
				if(cal_UID.equals(Data.car_UID[i])){
						lat_dis = Math.toRadians(Data.car_lat[i] - this.inter_lat);
						lon_dis = Math.toRadians(Data.car_long[i] - this.inter_lon);
						a =  Math.sin(lat_dis/2)*Math.sin(lat_dis/2)+Math.cos(Math.toRadians(this.inter_lat))*Math.cos(Math.toRadians(Data.car_lat[i]))*Math.sin(lon_dis/2)*Math.sin(lon_dis/2);
						c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
						bef_dis = R * c * 1000;
						break;
				}
		}
		if(bef_dis <= distance && bef_dis != -1){
				bef_dis = -1;
				Data.car_dir[i] = false;
		}else if(bef_dis > distance){
				Data.car_dir[i] = true;
		}
		if(bef_dis != -1){
			for(i=0;i<100;i++){
					if(Data.car_UID[i].equals("")){ 
							break;
					}
					if(Data.car_UID[i].equals(cal_UID) || Data.car_dir[i] == false){
							continue;
					}
					if(this.cal_time.getTimeInMillis() - Data.car_time[i].getTimeInMillis() > 20000){
							continue;
					}
					lat_dis = Math.toRadians(Data.car_lat[i] - this.inter_lat);
					lon_dis = Math.toRadians(Data.car_long[i] - this.inter_lon);
					a =  Math.sin(lat_dis/2)*Math.sin(lat_dis/2)+Math.cos(Math.toRadians(this.inter_lat))*Math.cos(Math.toRadians(Data.car_lat[i]))*Math.sin(lon_dis/2)*Math.sin(lon_dis/2);
					c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
					dis = R * c * 1000;
					cal_time = dis/Data.car_vel[i];
					dif_time = (int)(this.cal_time.getTimeInMillis() - Data.car_time[i].getTimeInMillis());
					cal_time = cal_time - (double)dif_time/1000;
					cal_in = cal_time - (double)radius/Data.car_vel[i];
					cal_out = cal_time + (double)radius/Data.car_vel[i];

					if(cal_in > ari_in && cal_in < ari_out){
							collision = true;
							break;
					}else if(cal_out > ari_in && cal_out < ari_out){
							collision = true;
							 break;
					}else if(cal_in < ari_in && cal_out > ari_out){
							collision = true;
							break;}
			}
		}
	}
	public boolean will_col(){    //will collide or not
		return collision;
	}
}

class Savedata implements Runnable{
		FileWriter fw;
		int i;
		//public Savedata(){}
		public void run(){
				while(true){
					i = 0;
					try{
						Thread.sleep(1000);
					}catch(InterruptedException ite){
							System.out.println("Wait fail!");
					}
					try{
							fw = new FileWriter("data.txt");
							while(Data.car_UID[i] != ""){
									fw.write(Data.car_vel[i]+","+Data.car_lat[i]+","+Data.car_long[i]+","+Data.car_time[i].getTimeInMillis()+","+Data.car_UID[i]+"\n");
									i++;
							}
							fw.flush();
							fw.close();
					}catch(IOException io){
							System.out.println("Write file fail");
					}
				}
		}
}

public class Driver{
	public static void main(String[] args){	
		int i = -1;
		float inter_lon=0,inter_lat=0;
		int radius = 0;

		Scanner scanner = new Scanner(System.in);
		Scanner iscanner = new Scanner(System.in);
		/*System.out.println("Input the longitude of the intersection:");
		inter_lon = scanner.nextFloat();
		System.out.println("Input the latitude of the intersection:");
		inter_lat = scanner.nextFloat();
		System.out.println("Input the radius of the intersection:");
		radius = iscanner.nextInt();*/

		//park
		inter_lon = 120.4716958f;
		inter_lat = 23.5561425f;
		//park

		//inter_lon = 120.473582f;
		//inter_lat = 23.560552f;
		radius = 10;
		Thread[] thrd=new Thread[100];
		Thread savethr = new Thread();
		ServerSocket sock = null;
		Socket cli = null;
		Client client = null;
		Savedata savedata = new Savedata();
		Data car_data = new Data();
		savethr = new Thread(savedata);
		savethr.start();
		try{
			sock = new ServerSocket(10002);
		}catch(IOException ioe){
			System.out.println("set port fail");
		}
		while(true){
			try{
					if(i == 99){
						i = -1;
					}
					i++;
					cli = sock.accept();
					client = new Client(cli,inter_lon,inter_lat,radius);
					//thrd[i] = new Thread(client);
					if(client.connect_yorn == true){
						thrd[i] = new Thread(client);
						//thrd[i] = new Thread(new Client(cli,inter_lon,inter_lat));
						thrd[i].start();
					}
					else{
							i--;
					}
			}
			catch(IOException ioe){
				//System.err.println(ioe);
				System.out.println("client accepts fail");
			}	
		}
	}
}

class Data{
		 public static int[] car_vel = new int[100];
		 public static float[] car_deg = new float[100];
		 public static float[] car_long = new float[100];
		 public static float[] car_lat = new float[100];
		 public static Calendar[] car_time = new Calendar[100];
		 public static String[] car_UID = new String[100];
		 public static boolean[] car_dir = new boolean[100];
		 public Data(){
		 		 int i;
		 		 for(i=0;i<100;i++){
		 		 		 car_vel[i] = 0;
		 		 		 car_deg[i] = 0;
		 		 		 car_long[i] = 0;
		 		 		 car_lat[i] = 0;
		 		 		 //car_time[i] = new Calendar();
		 		 		 car_UID[i] = "";
		 		 		 car_dir[i] = false;
				 }
		 }
}
