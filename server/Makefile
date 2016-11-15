all:
	javac -cp ".:java-json.jar" Driver.java
	java -cp ".:java-json.jar" Driver

testerror:
	javac Point.java
	javac -cp "./json.jar:./*" Car.java
	javac -cp "./json.jar:./*" Main.java
	java -cp ./json.jar:. Main

clean:
	rm *.class
