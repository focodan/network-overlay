all: ./cs455/overlay/*/*.java
	javac ./cs455/overlay/*/*.java
clean:
	rm ./cs455/overlay/*/*.class 
