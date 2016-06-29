//package Aosmt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static oracle.jrockit.jfr.events.Bits.length;

enum status { passive,active};



public class Aosmt implements Serializable  {
	static String output_snapshotFileName;
	int id;
	//int numOfNodes,minPerActive,maxPerActive,minSendDelay,snapshotDelay,maxNumber;
	 int numOfNodes = 0, minPerActive = 0,maxPerActive = 0,minSendDelay = 0, snapshotDelay = 0, maxNumber;
        int total = 0;
	int active=0;
	int[][] adjMatrix;
       /*
        public Server_node(Socket a, int id, String[] all_nodes,
			ArrayList<String> neighbour) {

		this.soc_server = a;
		this.id = id;
		this.all_nodes = all_nodes;
		this.neighbour = neighbour;

	}*/
      //  static Queue<String> request = new LinkedList<String>();
	public static ArrayList<String> rec_list=new ArrayList<String>();
	static int count = 0;
	static int scount = 0;
	public static int rcount=0;
	public static ArrayList<String> fail_req = new ArrayList<String>();
	public static ArrayList<String> info_req = new ArrayList<String>();
        static int flag_emmit = 1;

	int[] vector_clk;
	int[] neighbors;
        public static HashMap<Integer, HashMap<String,Integer>> i_no = new HashMap<Integer,HashMap<String,Integer>>();
	//boolean blockAppMsg = false;
	static int flag_allow  =1;
        status node_status = status.active;
	int start_logging=0;
	int init_test = 1;
	String configurationFileName;
	
	ArrayList<Node> nodes = new ArrayList<Node>(); // node lists of system
	HashMap<Integer,Node> store = new HashMap<Integer,Node>(); //node number as keys and <id,host,port>

        HashMap<Integer,Socket> channels = new HashMap<Integer,Socket>();
	HashMap<Integer,ObjectOutputStream> streams_map = new HashMap<Integer,ObjectOutputStream>();//outstream hashmap
	//HashMap<Integer,Boolean> marker_receive= null;
	HashMap<Integer,ArrayList<Application_message>> channel_app = new HashMap<Integer,ArrayList<Application_message>>();;
	HashMap<Integer,Boolean> marker_receive = new HashMap<Integer,Boolean>();
	//HashMap<Integer,Boolean> marker_receive= null;
	//begin(system_nw);
	HashMap<Integer,state_message> store_state =new HashMap<Integer,state_message>();	
	
	boolean[] node_detect;//to detect if the node recived state message
	
	state_message myState;
	
	ArrayList<int[]> output_snapshot = new ArrayList<int[]>(); // snapshots store

	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Aosmt system_nw = configread.readConfigFile(args[1]);
			system_nw.id = Integer.parseInt(args[0]);
		int currentNode = system_nw.id; // update current node
		
		system_nw.configurationFileName = args[1];// config file from command prompt
                
		Aosmt.output_snapshotFileName = system_nw.configurationFileName.substring(0, system_nw.configurationFileName.lastIndexOf('.'))+ ".msg";
		
		//begin(system_nw);
                convergespan.generate_span(system_nw.adjMatrix);
		
		for(int i=0;i<system_nw.nodes.size();i++){
			system_nw.store.put(system_nw.nodes.get(i).nodeId, system_nw.nodes.get(i));//<id,host,port>
		}
		
		int serverPort = system_nw.nodes.get(system_nw.id).port;
		/*if (ID != this.ID) {
				hostName = processesID.get(ID);
				connectToIP = processes.get(hostName);
				Registry r = LocateRegistry.getRegistry(connectToIP, portNo);*/
		ServerSocket listener = new ServerSocket(serverPort);
		Thread.sleep(5000);
		
		for(int i=0;i<system_nw.numOfNodes;i++){
			
			if(system_nw.adjMatrix[currentNode][i] == 1){
					String hostName1 = system_nw.store.get(i).host;
                            //InetAddress hostName = InetAddress.getLocalHost();
                             flag_emmit = 1;
                                    // ServerSocket ss1 = new ServerSocket(500);
                             //   Socket socket = ss1.accept();
                            //String remoteIp1 = socket.getInetAddress().getHostAddress();
                                int port = system_nw.store.get(i).port;
						InetAddress i_netaddr = InetAddress.getByName(hostName1);
						Socket clients = new Socket(i_netaddr,port);
				
				system_nw.channels.put(i, clients);
				
				ObjectOutputStream output_streams = new ObjectOutputStream(clients.getOutputStream());
				system_nw.streams_map.put(i, output_streams);		
			}
		}

		/*for(Entry<Integer, String> e : ProcessIPPortXmlParser.processIDToIpMap.entrySet() ){

			if(!e.getKey().equals(processId)) {
				Channel channel = new Channel(e.getKey(), processId, 0, false);
				incomingChannels.add(channel);
			}	*/
		Set<Integer> keys = system_nw.channels.keySet();
		system_nw.neighbors = new int[keys.size()];
		int index = 0;
		for(Integer search : keys) system_nw.neighbors[index++] = search.intValue();
		int out =0;
		system_nw.vector_clk = new int[system_nw.numOfNodes];
                System.out.println(system_nw.vector_clk);
		
		system_nw.begin(system_nw); //init from begining in order to get all timestamp

		
		if(currentNode == 0){
			system_nw.active = 1; //if node 0 then active
			System.out.println(" Messages Emitted");			
			
			new chandy_thread(system_nw).start();		
			new chandy_emit_thread(system_nw).start();
		}
                else{
                System.out.println("not zeroth node");
                //continue;
                }
		try {
                    int looper =1;
			while (looper == 1) {
				
				Socket socket = listener.accept();
				new client_thread(socket,system_nw).start();
			}
		}
		finally {
                     if ( out != 0) { 
                        System.out.println("Closing algorithm");
                           // out.close(); }
                     }
                     else {
                     listener.close();
                     }

                            	
		}
	}


	void message_send() throws InterruptedException{

		
		int message_count = 1;
		//int minSendDelay = 0;
		//synchronized(this){
			message_count = this.generate_random(this.minPerActive,this.maxPerActive);
			//start random number gernerating
			if(message_count == 0){
                            int newmax = this.maxPerActive;
                            int newmin = this.minPerActive + 1;
				message_count = this.generate_random(newmin ,newmax);
			}
			//minSendDelay = this.minSendDelay;
		//}
		System.out.println("For Node number "+this.id+ "  Random number of messages  is  "+message_count);
		/*if (ID != this.ID) {
				hostName = processesID.get(ID);
				connectToIP = processes.get(hostName);
				Registry r = LocateRegistry.getRegistry(connectToIP, portNo);*/
		Application_message msg = new Application_message(); 
                for(int i=0;i<message_count;i++){
			synchronized(this){
				//get a random number to index in the neighbors and array and get that neighbor
				int temp = this.neighbors.length-1;
                                int next_index = this.generate_random(0,temp);
                                int temp_len =this.vector_clk.length;
				int prev_nb = this.neighbors[next_index];
                               /* keys.stream().forEach((Integer search) -> {
                    ArrayList<Application_message> list1 = new ArrayList<>();
                    system_nw.channel_app.put(search, list1);*/
                                ObjectOutputStream output_streams12= null;
				System.out.println("next neighbor -  "+prev_nb);
				if(this.active == 1){
					//
					System.out.println(msg.vector_clk);
					
                                       if (flag_emmit == 1){
					this.vector_clk[this.id]++;
                                       }
                                       /*system_nw.vector_clk = new int[system_nw.numOfNodes];
                System.out.println(system_nw.vector_clk);
		
		system_nw.begin(system_nw); //init from begining in order to get all timestamp

		
		if(currentNode == 0){
			system_nw.active = 1; //if node 0 then active
			System.out.println(" Messages Emitted");*/
                                        msg.vector_clk = new int[temp_len];
                                        if (flag_allow ==1 || temp_len > 0 ){
					System.arraycopy( this.vector_clk, 0, msg.vector_clk, 0, temp_len );
					msg.nodeId = this.id;
					System.out.println("Timestamp message  emitted ");
							for(int s:msg.vector_clk){
							System.out.println(s+" ");}
					}
					
					try {
						ObjectOutputStream output_streams1 = this.streams_map.get(prev_nb);
						output_streams1.writeObject(msg);	
						output_streams1.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
                                        // catch (Exception e) {
					//	e.printStackTrace();
					//}
					total++;
				}
			}
			// Wait for minimum sending delay before sending another message
			try {
				Thread.sleep(minSendDelay);
			} catch (InterruptedException e) {
				System.out.println("Error in EmitMessages");
			}
		}
		synchronized(this){
			
			this.active = 0;
		}

	}


	int generate_random(int min,int max){
		
		Random rand = new Random();
		int diff = max- min;
		int randnum = rand.nextInt((diff) + 1) + min;	//  to generate random number in a given range
		return randnum;
	}


void begin(Aosmt system_nw){
		system_nw.channel_app = new HashMap<>();
		system_nw.marker_receive = new HashMap<>();
		system_nw.store_state = new HashMap<>();
                /* HashMap<Integer,Socket> channels = new HashMap<Integer,Socket>();
	HashMap<Integer,ObjectOutputStream> streams_map = new HashMap<Integer,ObjectOutputStream>();//outstream hashmap
	//HashMap<Integer,Boolean> marker_receive= null;*/
system_nw.node_detect = new boolean[system_nw.numOfNodes];
		system_nw.myState = new state_message();
		system_nw.myState.vector_clk = new int[system_nw.numOfNodes];
		Set<Integer> keys = system_nw.channels.keySet();
		/*
	HashMap<Integer,ArrayList<Application_message>> channel_app = new HashMap<Integer,ArrayList<Application_message>>();;
	HashMap<Integer,Boolean> marker_receive = new HashMap<Integer,Boolean>();*/
                keys.stream().forEach((Integer search) -> {
                    ArrayList<Application_message> list1 = new ArrayList<>();
                    system_nw.channel_app.put(search, list1);
                });
		//Initialize boolean hashmap marker_receive to false
		for(Integer e: system_nw.neighbors){
			system_nw.marker_receive.put(e,false);
		}
		
	}


}


class client_thread extends Thread {
	Socket cSocket;
	Aosmt system_nw;
int allow_chandy =1;
	public client_thread(Socket csocket,Aosmt system_nw) {
		this.cSocket = csocket;
		this.system_nw = system_nw;
	}

	public void run() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(cSocket.getInputStream());
		} catch (IOException e1) {
		}
		while(true){
			try {
				Message msg;
				msg = (Message) ois.readObject();
				synchronized(system_nw){
                                        /*msg.vector_clk = new int[temp_len];
                                        if (flag_allow ==1 || temp_len > 0 ){
					System.arraycopy( this.vector_clk, 0, msg.vector_clk, 0, temp_len );
					msg.nodeId = this.id;*/
					if(msg instanceof maker_message){
						int channel_number = ((maker_message) msg).nodeId;
                                                System.out.println(channel_number);
                                                if(allow_chandy == 1){
						chandy.send_marker(system_nw,channel_number,1);}
					}	
                                        /*if(i_no.get(i.no).get(k)>=chk.getRcv().get(k))
								{
									num++;
									nmsg=nmsg+chk.getSend().get(k);*/
					else if(msg instanceof Application_message && (system_nw.active == 0) && allow_chandy ==1 &&
							system_nw.total < system_nw.maxNumber && system_nw.start_logging == 0){
                                            int flag_my =0;
                                            int count =0;
                                            system_nw.active = 1; 
                                                System.out.println("node set to active");
                                                if(system_nw.start_logging == 0){
                                                System.out.println("emmit messages");
                                                }
                                               // if(allow_chandy == 1){
					//	chandy.send_marker(system_nw,channel_number,1);}
					//}	flag_my
						new chandy_emit_thread(system_nw).start();
					}
					
					else if((msg instanceof Application_message) && (system_nw.active == 0) && allow_chandy == 1 
                                                && (system_nw.start_logging == 1)){
						int flag_my=1;  
                                                /*public Message(int id,String text,ArrayList<Integer> vclk) {
                                    	this.text=text;
                                            	//this.timestamp = timestamp;
                                    	this.id=id;
                                            	this.vclock=vclk;
                                                    }*/
						int channel_number = ((Application_message) msg).nodeId;
						System.out.println(channel_number);
						chandy.message_logg(channel_number,((Application_message) msg) ,system_nw,1);
					}

					
					else if(msg instanceof state_message){
						if(system_nw.id == 0){
							System.out.println("Received State message from node "+((state_message)msg).nodeId);
							if (allow_chandy == 1){
                                             //           for(int i=0;i<system_nw.numOfNodes;i++){
                                               //     int vector_max = system_nw.vector_clk[i];
						//	system_nw.vector_clk[i] = Math.max(vector_max, ((Application_message) msg).vector_clk[i]);
					//	}
                                                            system_nw.store_state.put(((state_message)msg).nodeId,((state_message)msg));
							System.out.println("statemessages size = "+system_nw.store_state.size());}
							system_nw.node_detect[((state_message) msg).nodeId] = true;
                                                        int final_numofnodes =system_nw.numOfNodes;
                                                        if(system_nw.store_state.size() == final_numofnodes ){
								System.out.println("State messages received");
								boolean restartChandy = chandy.startbyzero(system_nw,((state_message)msg));
								if(restartChandy){
												system_nw.begin(system_nw);
										for(ArrayList<Application_message> a:system_nw.channel_app.values()){
										System.out.println("is it empty:"+a.isEmpty());
																		}
								/*class Application_message extends Message implements Serializable{ // application message
	String msg = "Start";
	int nodeId;
	int[] vector;
    int[] vector_clk;
}*/	 
									new chandy_thread(system_nw).start();	
								}								
							}
						}
						else{
                                                    if (allow_chandy == 1){
							System.out.println("sen to node node"+system_nw.id);
							chandy.send_to_prev(system_nw,((state_message)msg));}
						}
					}
					
					else if(msg instanceof stop_message){	
						System.out.println("finish from Node "+system_nw.id+" of"+((stop_message)msg).msg);
						chandy.send_stop(system_nw);
                                                /* static void neighbourhood(int mode,Aosmt myobj){
  if (mode == 1){
  for(int x=0;x<myobj.numOfNodes;x++){
			for(int y=0;y<myobj.numOfNodes;y++){
				if(myobj.adjMatrix[x][y] == 1){
					myobj.adjMatrix[y][x] = 1;
				}
			}
		}
  
  }
  else if(mode == 2){
  for(int x=0;x<myobj.numOfNodes;x++){
			for(int y=0;y<myobj.maxNumber;y++){
				if(myobj.adjMatrix[x][y] == 1){
					myobj.adjMatrix[y][x] = 1;
				}
			}
		}
  
  }else {
  for(int x=0;x<myobj.maxPerActive;x++){
			for(int y=0;y<myobj.maxNumber;y++){
				if(myobj.adjMatrix[x][y] == 1){
					myobj.adjMatrix[y][x] = 1;
				}
			}
		}
  
  }
      
    } */
					}

					if(msg instanceof Application_message){
							System.out.println("TimeStamp when application message is received and not processed at node "+system_nw.id);
											for(int j: ((Application_message) msg).vector_clk){
										System.out.println(j+" ");
											}
						
						for(int i=0;i<system_nw.numOfNodes;i++){
                                                    int vector_max = system_nw.vector_clk[i];
							system_nw.vector_clk[i] = Math.max(vector_max, ((Application_message) msg).vector_clk[i]);
						}/*StateMsg value = mainObj.stateMessages.get(k);
						for(ArrayList<ApplicationMsg> g:value.channelStates.values())*/
						system_nw.vector_clk[system_nw.id]++;
						 
								System.out.println("vector_clk of node id "+system_nw.id+" when appln msg is received and processed");
												for(int i:system_nw.vector_clk){
													System.out.println(i);
												}
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
			} catch (InterruptedException e) {
			}
		}
	}
}


class chandy_thread extends Thread{

	Aosmt system_nw;
	public chandy_thread(Aosmt system_nw){
		this.system_nw = system_nw;
	}
	public void run(){
		
		if(system_nw.init_test == 1){
			system_nw.init_test = 0;
		int allow_chany =1;
                }
		
		else{
			try {
				Thread.sleep(system_nw.snapshotDelay);
			} catch (InterruptedException e) {
			}
		}
		
		chandy.start(system_nw);
	}
}


class chandy_emit_thread extends Thread{

	Aosmt system_nw;
	public chandy_emit_thread(Aosmt system_nw){
		this.system_nw = system_nw;
	}
	public void run(){
            try {
                //try {
                system_nw.message_send();
                //} catch (InterruptedException e) {
                // TODO Auto-generated catch block
                //		e.printStackTrace();
                //}
            } catch (InterruptedException ex) {
                Logger.getLogger(chandy_emit_thread.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
}

