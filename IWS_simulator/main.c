#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <math.h>

struct Car{ //one car
	float x;
	float y;
	float carLength;
	float carWidth;
	float xSpeed;
	float ySpeed;
	float xAcceleration;
	float yAcceleration;
};
struct Map{ //for one  Map
	float xlength;
	float ylength;
	char **map;
};
typedef struct Car Car;
typedef struct Map Map;

void run(void); //run algrithm
void initialMap(void); //initial Map
void initialCar(void); //initial Car
void drawMap(void); //draw Map
void drawCar(void); //draw Car
void moveCar(void); //move Car
void alg(void); //algrithm for check;
//main funciotn

Map map;
Car car1,car2,car3,car4;
Car car[5];

int main(int argc,char *argv[])
{
	car[0]=car1;car[1]=car2;car[2]=car3;car[3]=car4;
	//call run function
	run();
	return 0;
}

void run(void){ 
	//initial map car
	initialMap();
	initialCar();
	while(1){
		//draw map
		//draw car
		//check collision for alg
		//move car
		moveCar();
		//drawMap();
		drawCar();
//		if(getchar()=='e')
//			break;
		sleep(1);
	}
}
void initialMap(void){ //initial Map
	map.xlength = 100;
	map.ylength = 100;
	int i=0;
	map.map=(char **)malloc(sizeof(char *) * map.ylength);
	for(i=0 ; i<map.ylength ; i++){
		map.map[i] = (char *)malloc(sizeof(char) * map.xlength);
		memset(map.map[i], 0, map.xlength);
	}
	for(i=0 ; i<map.xlength ; i++){
		map.map[50][i] = 1;
		map.map[49][i] = 1;
	}
	for(i=0 ; i<map.ylength ; i++){
		map.map[i][50] = 1;
		map.map[i][49] = 1;
	}
}
void initialCar(void){ //initial Car
	car1.x = 0 ;
	car1.y = 49;
	car1.xSpeed = 1;
	car1.ySpeed = 0;
	car2.x = map.xlength-1;
	car2.y = 50;
	car2.xSpeed = -1; 
	car2.ySpeed = 0;
	car3.x = 49;
	car3.y = 0;
	car3.xSpeed = 0; 
	car3.ySpeed = 1;
	car4.x = 50;
	car4.y =map.ylength-1;
	car4.xSpeed = 0; 
	car4.ySpeed = -1;
	
	/*
	map.map[0][49] = 2;
	map.map[(int)map.ylength-1][50] = 2;
	map.map[49][0] = 2;
	map.map[50][(int)map.xlength-1] = 2;
	*/
}
void drawMap(void){ //draw Map
	int i=0 ,j=0;
	for(i=0 ; i<map.ylength ; i++){
		for(j=0 ; j<map.xlength ; j++){
			if(map.map[i][j] == 0){
				printf("@");
			}
			else if(map.map[i][j] == 1){
				printf(" ");
			}
			else if(map.map[i][j] == 2){
				printf("C");
			}
			else
				printf(" ");
		}
		printf("\n");
	}
}
void drawCar(void){ //draw Car
	printf("car1: (%f,%f)\n",car1.x,car1.y);
	printf("car2: (%f,%f)\n",car2.x,car2.y);
	printf("car3: (%f,%f)\n",car3.x,car3.y);
	printf("car4: (%f,%f)\n",car4.x,car4.y);
	printf("\n");
}
void moveCar(void){ //move Car
	car1.x = car1.x + car1.xSpeed;
	car1.y = car1.y + car1.ySpeed;
	car2.x = car2.x + car2.xSpeed;
	car2.y = car2.y + car2.ySpeed;
	car3.x = car3.x + car3.xSpeed;
	car3.y = car3.y + car3.ySpeed;
	car4.x = car4.x + car4.xSpeed;
	car4.y = car4.y + car4.ySpeed;
}
void alg(void){ //algrithm for check;
	
}
