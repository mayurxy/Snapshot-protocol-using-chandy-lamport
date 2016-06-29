//package Aosmt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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

public class chandy { 
    //method where protocol starts 
    int flag_sendmk;
    /* HashMap<Integer,Socket> channels = new HashMap<Integer,Socket>();
	HashMap<Integer,ObjectOutputStream> streams_map = new HashMap<Integer,ObjectOutputStream>();//outstream hashmap
	//HashMap<Integer,Boolean> marker_receive= null;
	HashMap<Integer,ArrayList<Application_message>> channel_app = new HashMap<Integer,ArrayList<Application_message>>();;
	HashMap<Integer,Boolean> marker_receive = new HashMap<Integer,Boolean>();*/
	public static void start(Aosmt system_nw) {
		synchronized(system_nw){
			
			system_nw.node_detect[system_nw.id] = true;
			int flag_sendmk =1;
			send_marker(system_nw,system_nw.id, flag_sendmk);
		}
	}

	public static void send_marker(Aosmt system_nw, int channel_number1, int flag){
		
		synchronized(system_nw){
			if(system_nw.node_status == status.active){
                             int flag_marker =1, flag_allow= 1, flag_check1=1, flag_update =1;	
                            System.out.println("Received first Marker message ");
			if(flag == 1){	
                            system_nw.marker_receive.put(channel_number1, true);
				system_nw.node_status = status.passive; // change node status to passsive
				flag_marker =1;
                                system_nw.myState.active = system_nw.active;
				flag_allow =1;
                                /*HashMap<String, Integer> smsg = new HashMap<String, Integer>(
								Node.getSend());
						HashMap<String, Integer> rmsg = new HashMap<String, Integer>(Node.getRcv());*/
                                system_nw.myState.vector_clk = system_nw.vector_clk;
				system_nw.myState.nodeId = system_nw.id;
                        }       
                                if (flag_allow == 1 ){
                                    /*if(flag_marker == 1){
					system_nw.node_status = status.active;
					system_nw.start_logging = 0;}*/
                                        if(flag_marker ==1){
				System.out.println("Node "+system_nw.id+" is sending the  timestamp");
				for(ArrayList<Application_message> a:system_nw.channel_app.values()){
					System.out.println("state:"+a.isEmpty());
				}
                                /*for(int i=0;i<system_nw.numOfNodes;i++){
                                                    int vector_max = system_nw.vector_clk[i];
							system_nw.vector_clk[i] = Math.max(vector_max, ((Application_message) msg).vector_clk[i]);
						}*/
				for(int k:system_nw.myState.vector_clk){
					System.out.print(k+" ");
				}
                                        }
                                }
				int[] clock_vector_print = new int[system_nw.myState.vector_clk.length];
				for(int i=0;i<clock_vector_print.length;i++){
					clock_vector_print[i] = system_nw.myState.vector_clk[i]; 
				}
                                        

				system_nw.output_snapshot.add(clock_vector_print);

				/*int final_numofnodes =system_nw.numOfNodes;
                                                        if(system_nw.store_state.size() == final_numofnodes ){
								System.out.println("State messages received");*/
				system_nw.start_logging = 1; // start logging messages
						for(int i : system_nw.neighbors){
					maker_message msg = new maker_message();

                                        if(flag_allow == 1 ){
                                            System.out.println("start sending marker");
                            msg.nodeId = system_nw.id;}
					ObjectOutputStream stream_op = system_nw.streams_map.get(i);
					try {
						stream_op.writeObject(msg);
                                                flag_marker =1;
					} catch (IOException e) {
					}
				}
				if(system_nw.neighbors.length == 1){ 
                                        if (system_nw.id!=0 && flag_check1 == 1){
                                            System.out.println("getting previous node");
                                            int prev = convergespan.get_ancestor(system_nw.id);	
					system_nw.myState.channel_app = system_nw.channel_app;
					if(flag_update == 1){
                                        system_nw.node_status = status.active;
					system_nw.start_logging = 0;
                                        }/*for(int i=0;i<system_nw.numOfNodes;i++){
                                                    int vector_max = system_nw.vector_clk[i];
							system_nw.vector_clk[i] = Math.max(vector_max, ((Application_message) msg).vector_clk[i]);
						}*/
                                        flag_update =1;
                                        flag_allow =1;
                                        ObjectOutputStream stream_out5 = system_nw.streams_map.get(prev);
//					
/*{ //for nodes of passive status
				System.out.println("marker received");
				system_nw.marker_receive.put(channel_number1, true);
				int i=0;
                            int flag_check1 = 1;
                            int new_flag =0;
                            System.out.println("Size of  neighbors list ::: "+system_nw.neighbors.length);
                                int get_len = system_nw.neighbors.length;
                                if (system_nw.marker_receive.get(system_nw.neighbors[i]) == true){
                                new_flag =1;
                                }
				while(i<get_len && new_flag == 1 ){
					System.out.println("Marker from neighbour");
					i++;
				}*/
					try {
						stream_out5.writeObject(system_nw.myState);
                                                flag_check1 =1;
					} catch (IOException e) {
						e.printStackTrace();
					}
					system_nw.begin(system_nw);
				}

                                }
			}
			
			else if(system_nw.node_status == status.passive){ // for node with status passive
                                System.out.println("received marker");
				system_nw.marker_receive.put(channel_number1, true);
				int flag_marker =1, now_flag =1;
                                int i=0;
				/*{ //for nodes of passive status
				System.out.println("marker received");
				system_nw.marker_receive.put(channel_number1, true);
				int i=0;
                            int flag_check1 = 1;
                            int new_flag =0;
                            System.out.println("Size of  neighbors list ::: "+system_nw.neighbors.length);
                                int get_len = system_nw.neighbors.length;
                                if (system_nw.marker_receive.get(system_nw.neighbors[i]) == true){
                                new_flag =1;
                                }
				while(i<get_len && new_flag == 1 ){
					System.out.println("Marker from neighbour");
					i++;
				}*/
				while(i<system_nw.neighbors.length && system_nw.marker_receive.get(system_nw.neighbors[i]) == true){
					//System.out.println("Received Marker msg from neighbor "+system_nw.neighbors[i]);
					i++;
                                    int flag_check1 = 1;
				}
				/*try {
						oos.writeObject(mainObj.myState);
					} catch (IOException e) {
						e.printStackTrace();
					}*/
				if(i == system_nw.neighbors.length && system_nw.id != 0){
					int prev = convergespan.get_ancestor(system_nw.id);				
					system_nw.myState.channel_app = system_nw.channel_app;
					if(flag_marker == 1){
					system_nw.node_status = status.active;
					system_nw.start_logging = 0;} // no logging
					// Send channel state to prev 
					//int now_flag =1;
                                        ObjectOutputStream stream_out5 = system_nw.streams_map.get(prev);
					try {
                                            /*try {
						oos.writeObject(mainObj.myState);
					} catch (IOException e) {
						e.printStackTrace();
					}*/
					System.out.println("");	
                                            stream_out5.writeObject(system_nw.myState);
					} catch (IOException e) {
						e.printStackTrace();
					}
					system_nw.begin(system_nw);
				}
                                /*if (now_flag ==1){
                                if (i == system_nw.neighbors.length){
                                if (flag_ marker ==1 && system_nw.id == 0){
                                system_nw.myState.channel_app = system_nw.channel_app;
					if(flag_marker == 1){
                                        system_nw.store_state.put(system_nw.id, system_nw.myState);
					system_nw.node_status = status.active;}
					system_nw.start_logging = 0;
				}
                                }
                                }
                                }*/
                                
                                
				if(i == system_nw.neighbors.length &&  system_nw.id == 0){
//					System.out.println("For node 0, all neighbours have sent marker messages.");
					system_nw.myState.channel_app = system_nw.channel_app;
					if(flag_marker == 1){
                                            /*int final_numofnodes =system_nw.numOfNodes;
                                                        if(system_nw.store_state.size() == final_numofnodes ){
								System.out.println("State messages received");*/
                                        system_nw.store_state.put(system_nw.id, system_nw.myState);
					system_nw.node_status = status.active;}
					system_nw.start_logging = 0;
				}
//				if(i != system_nw.neighbors.length){
                        //                              stream_out5.writeObject(system_nw.myState);

//				}
//				

			}
		}
	}

	
	public static boolean startbyzero(Aosmt system_nw, state_message msg) throws InterruptedException {
		int i=0,j=0,k=0;
                int  flag_marker =0, new_flag =0, flag_check =0, my_flag =1;
                int get_lenn =system_nw.node_detect.length;
		synchronized(system_nw){
			//while(get_lenn!)
                        /*if(flag_marker == 1){
					system_nw.node_status = status.active;
					system_nw.start_logging = 0;}*/
			while(i<system_nw.node_detect.length && system_nw.node_detect[i] == true && my_flag ==1){
				i++;
                                new_flag =1;
			}
			
			if(i == system_nw.node_detect.length){
				int com_size =system_nw.store_state.size();
				for(j=0;j<system_nw.store_state.size();j++){
					//active provess detection
					if(system_nw.store_state.get(j).active == 1){
					flag_check =1;	
                                            return true;
					}
				}
				/*try {
						oos.writeObject(mainObj.myState);
					} catch (IOException e) {
						e.printStackTrace();
					}*/
				if(j == system_nw.numOfNodes){
					for(k=0;k<system_nw.numOfNodes;k++){
							state_message value = system_nw.store_state.get(k);
                                            System.out.println("all noodes are passiive");
                                                        if (value.channel_app.values().stream().anyMatch((g) -> (!g.isEmpty()))) {
                                                return true;
                                                //my_flag =1;
                                            }
                                                       
												}
				}
				
				if(k == system_nw.numOfNodes){
				/*for(k=0;k<system_nw.numOfNodes;k++){
							state_message value = system_nw.store_state.get(k);*/
                                    System.out.println("Node 0 is sending finish message ");					
					send_stop(system_nw);
					return false;
				}
			}
		}
		return false;
	}


	
	public static void message_logg(int channel_number1,Application_message m, Aosmt system_nw, int flagx) {
		//synchronized(system_nw){
			int count =0, flag_new =1, myflag=1;
                        /*int x =channel_number1).isEmpty();
                        if (system_nw.channel_app.get(x)){
                                if(system_nw.marker_receive.get(x) != true){
                                system_nw.channel_app.get(channel_number1).add(m);
                                flag_new =1;
                                count++;
                                }
                        }*/
                        
                		if(!(system_nw.channel_app.get(channel_number1).isEmpty()) && system_nw.marker_receive.get(channel_number1) != true){
				 flag_new =1;
                                    system_nw.channel_app.get(channel_number1).add(m);
			}
			// or create a list and add the message into it
			else if((system_nw.channel_app.get(channel_number1).isEmpty()) && system_nw.marker_receive.get(channel_number1) != true){
				/*
                        if (system_nw.channel_app.get(x)){
                                if(system_nw.marker_receive.get(x) != true){
                                system_nw.channel_app.get(channel_number1).add(m);
                                flag_new =1;
                                count++;
                                }
                        }*/
                            ArrayList<Application_message> msgs = system_nw.channel_app.get(channel_number1);
				msgs.add(m);
                                if(flagx ==1){
                                System.out.println("log message requested");
                                }/*try {
						oos.writeObject(mainObj.myState);
					} catch (IOException e) {
						e.printStackTrace();
					}*/
				system_nw.channel_app.put(channel_number1, msgs);
			}
	//	}
	}

	
	public static void send_to_prev(Aosmt system_nw, state_message stateMsg) {
		synchronized(system_nw){
			int prev = convergespan.get_ancestor(system_nw.id);
                        int flagv =1;
                  //      ObjectOutputStream stream_out5 =null;
			// Send stateMsg to the prev
			if(flagv == 1){
                      //  ObjectOutputStream stream_out5 = system_nw.streams_map.get(prev);
                        System.out.println("send to prev node");
                        }
                        ObjectOutputStream stream_out5 = system_nw.streams_map.get(prev);
                        try {
				stream_out5.writeObject(stateMsg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//Method to send finish message to all the neighbors of the current Node
	public static void send_stop(Aosmt system_nw) {
                    int flag =0, my_flag=0;
            synchronized(system_nw){
			new file_write(system_nw).Write();
			if(flag == 1){
                        System.out.println("sending stop messages");
                                
                        }/*if(i == mainObj.neighbors.length &&  mainObj.id == 0){
//					System.out.println("For node 0, all neighbours have sent marker messages.");
					mainObj.myState.channelStates = mainObj.channelStates;
					mainObj.stateMessages.put(mainObj.id, mainObj.myState);*/
                        for(int s : system_nw.neighbors){
				stop_message msg = new stop_message();
				my_flag =1;
                                ObjectOutputStream stream_out5 = system_nw.streams_map.get(s);
				try {
					stream_out5.writeObject(msg);
                                        flag =1;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
			System.exit(1);
		}
	}
}


class file_write {
	Aosmt system_nw;
        int my_marker =0, flag_file =0, flag_check =0;
	public file_write(Aosmt system_nw) {
		this.system_nw = system_nw;
	}


	public void Write() {
		String fileName = Aosmt.output_snapshotFileName+"-"+system_nw.id+".out";
		synchronized(system_nw.output_snapshot){
			try {
				File file = new File(fileName);
				FileWriter fileWriter;
					/*if(file.length()!=0){
                bufferedWriter.write("\n");
            }*/
                                if(file.exists()){
					fileWriter = new FileWriter(file,true);
                                        flag_file =1;
                                }/*if(i == mainObj.neighbors.length &&  mainObj.id == 0){
//					System.out.println("For node 0, all neighbours have sent marker messages.");
					mainObj.myState.channelStates = mainObj.channelStates;
					mainObj.stateMessages.put(mainObj.id, mainObj.myState);*/
				else
				{       flag_check =1;
					fileWriter = new FileWriter(file);
				}
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				/*if(file.length()!=0){
                bufferedWriter.write("\n");
            }*/
   int n= system_nw.output_snapshot.size();
				for(int i=0;i<n;i++){
					for(int j:system_nw.output_snapshot.get(i)){
						bufferedWriter.write(j+" ");
						//bufferedWriter.write("\n");
					}
					if(i<(system_nw.output_snapshot.size()-1)){
	            bufferedWriter.write("\n");
					}
				}		/*if(i == mainObj.neighbors.length &&  mainObj.id == 0){
//					System.out.println("For node 0, all neighbours have sent marker messages.");
					mainObj.myState.channelStates = mainObj.channelStates;
					mainObj.stateMessages.put(mainObj.id, mainObj.myState);*/
                                
				system_nw.output_snapshot.clear();
				//bufferedWriter.write("\n");
				bufferedWriter.close();
			}
			catch(IOException ex) {
				System.out.println("Error writing to file ");
				
			}
		}
	}

}
