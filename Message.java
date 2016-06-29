//package Aosmt;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;



public class Message implements Serializable { //combine of all message
	Aosmt myobj = new Aosmt();
	int n = myobj.numOfNodes;
}


class Application_message extends Message implements Serializable{ // application message
	String msg = "Start";
	int nodeId;
	int[] vector;
    int[] vector_clk;
}

class maker_message extends Message implements Serializable{ // marker message class 
	String msg = "Marker";
	int nodeId;
}

class state_message extends Message implements Serializable{ // state message class
	int active;
	int nodeId;
	HashMap<Integer,ArrayList<Application_message>> channel_app;
	int[] vector;
    int[] vector_clk;
}


class stop_message extends Message implements Serializable{ // stop message class
	String msg = "Stop";
}