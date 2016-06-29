//package Aosmt;


//import aos4.Node;
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
import static oracle.jrockit.jfr.events.Bits.length;

/**
 *
 * @author mayur
 */
public class configread implements Serializable {

	public static Aosmt readConfigFile(String filename) throws IOException {
		Aosmt myobj = new Aosmt();
		int count = 0,flag = 0, flag1 = 0, flag2 =1, flagx=0, flagnodes =0;
		int currentNode = 0;
                 int numOfNodes = 0, minPerActive = 0,maxPerActive = 0,minSendDelay = 0, snapshotDelay = 0, maxNumber;
                int[][] adjMatrix = null;
                flagnodes =1;
                ArrayList<Node> nodes = new ArrayList<Node>();
                //String fileName = "C:\\Users\\mayur\\Documents\\NetBeansProjects\\aosproj1\\src\\aosproj1\\config.txt";
		String curDir = System.getProperty("user.dir"); // got from http://www.codecodex.com/wiki/Get_the_current_working_directory
		String fileName1 = curDir+"/"+filename;
                flag1=1;
                String error_para = "insufficient parameters";
		String line = null;
		try {
			
			FileReader myfile = new FileReader(fileName1); //default file read
                                                                                                                flag1=1;
                                             //Thread.sleep(2000);
			BufferedReader bufferedReader1 = new BufferedReader(myfile);
			while((line = bufferedReader1.readLine()) != null) {
				/*else if (Integer.parseInt(x.split("\\s+")[1]) > Integer
				.parseInt(y.split("\\s+")[1])) {
			return true;*/
				// Ignore comments and consider only those lines which are not comments
				if(!line.startsWith("#")){
					flagx =1;
	                                    if(line.contains("#")){
						String[] split_line = line.split("#");
                                                String[] split_linex = line.split(".*");
                                                        flag2=1;
                                                        //Thread.sleep(2000);
						String[] split_line1 = split_line[0].split("\\s+");
						if(flag == 0 ){
                                                    if (get_length(split_line1) < 7 && flag1 == 1){
							myobj.numOfNodes = Integer.parseInt(split_line1[0]);
                                                            myobj.snapshotDelay = Integer.parseInt(split_line1[4]);
                                                               myobj.minSendDelay = Integer.parseInt(split_line1[3]);
							flag++;
                                                        if (myobj.numOfNodes <= 5){
                                                        System.out.println("insuffiecient nodes");
                                                        flag2 = 1;
                                                        }
                                                                myobj.minPerActive = Integer.parseInt(split_line1[1]);
                                                            myobj.maxPerActive = Integer.parseInt(split_line1[2]);
                                                        myobj.maxNumber = Integer.parseInt(split_line1[5]);
							//flag++;
						}
                                                }
						else if(flag == 1 )
						{	if (count < myobj.numOfNodes )	{					
							myobj.nodes.add(new Node(Integer.parseInt(split_line1[0]),split_line1[1],Integer.parseInt(split_line1[2])));
							if(flag2 == 1){
                                                            count++;}
                                                       // count++;
                                                                                               System.out.println(error_para);

							if(count == myobj.numOfNodes && flagnodes == 1){
								flag = 2;
							}
                                                }
                                                }
						else if(flag == 2){
							insert(split_line1,-1,myobj, currentNode);
							// for(String i:split_line1){
                                                    //    adjMatrix[currentNode][Integer.parseInt(i)] = 1;
                                                     //   }
                                                        currentNode++;
						}
					}
					else {
						String[] split_line = line.split("\\s+");
						//String[] split_line = line.split("#");
                                                if(flag == 0){
                                                    if (split_line.length < 7 && flag1 == 1){
							myobj.numOfNodes = Integer.parseInt(split_line[0]);
							myobj.minSendDelay = Integer.parseInt(split_line[3]);
                                                            myobj.snapshotDelay = Integer.parseInt(split_line[4]);
							 if (myobj.numOfNodes <= 5){
                                                        System.out.println("insuffiecient nodes");
                                                        flag2 = 1;
                                                        }
                                                        myobj.maxNumber = Integer.parseInt(split_line[5]);
							flag++;
                                                        if (flag == 3 && flag2 == 1){
                                                        System.out.println("flags out of bound:"+flag);
                                                        }
                                                        myobj.minPerActive = Integer.parseInt(split_line[1]);
							myobj.maxPerActive = Integer.parseInt(split_line[2]);
                                                        
							myobj.adjMatrix = new int[myobj.numOfNodes][myobj.numOfNodes];
                                                    }
                                                    }
						else if(flag == 1)
						{
                                                    if (count < myobj.numOfNodes && flag2 ==1){
							myobj.nodes.add(new Node(Integer.parseInt(split_line[0]),split_line[1],Integer.parseInt(split_line[2])));
							//count++;
                                                        if (flag2 == 1 || flag == 1){
                                                        count++;}
							if(count == myobj.numOfNodes && flagnodes ==1){
								flag = 2;
							}
                                                    }
						}//Thread.sleep(2000);
						else if(flag == 2 ){
							insert(split_line,-1,myobj,currentNode);
					//		for(String i:split_line){
                                         //               adjMatrix[currentNode][Integer.parseInt(i)] = 1;
                                         //               }
                                                        currentNode++;
						}
					}
				}
			if(line.length() == 0)
                                                    continue;
                        }
			System.out.println("numOfNodes : "+myobj.numOfNodes
                                +"\n minPerActive:"+myobj.minPerActive
                                +"\n maxPerActive:"+myobj.maxPerActive
                                +"\n minSendDelay:"+myobj.minSendDelay
                                +"\n snapshotDelay:"+myobj.snapshotDelay);
                        
                        //Thread.sleep(2000);
			bufferedReader1.close();  
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file" );                
		}
		catch(IOException ex) {
			System.out.println("Error reading file");                  
		}
		
                
                neighbourhood(1,myobj);
                
                printmatrix(myobj);
		return myobj;
	}
        
        
        
        
        
  static void neighbourhood(int mode,Aosmt myobj){
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
      
    }      
        
  
  
  
  
  
   static void printmatrix(Aosmt myobj){
   
   
                   for(int i=0;i<myobj.numOfNodes;i++){
			for(int j=0;j<myobj.numOfNodes;j++){
				System.out.print(myobj.adjMatrix[i][j]);
                                System.out.print(" ");
                                
			}
                        for(int k=0; k < myobj.nodes.size();k++){
                        //System.out.println("nodes::");
                        //System.out.print(myobj.nodes.get(k).nodeId);
                        }
        System.out.println();
		}
   
   }
   
 static int  get_length(String[] str_input) {
 
      int len = 0;
       len = str_input.length;
     return len;
  } 
 
 
 
 
 
 
 
 
	static void insert(String[] input, int checknode, Aosmt myobj,int nodecount) {
		if (checknode == 2){
            for(String i:input){
			myobj.adjMatrix[myobj.numOfNodes][Integer.parseInt(i)] = 1;
		}
                }
                else if(checknode == -1 ){
                
                for(String i:input){
			myobj.adjMatrix[nodecount][Integer.parseInt(i)] = 1;
		}
                }
                else{
                for (int i = 1; i < myobj.numOfNodes; i++)
        {
                    myobj.adjMatrix[i][Integer.parseInt(input(i))] = 0;
        }   
                } 
                   
	}

//	public static void main(String[] args) throws IOException{
//		Aosmt m = configread.readConfigFile("config1.txt");
//		printmatrix(m);
//			System.out.println();
//		}
//
//	}

    private static String input(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

