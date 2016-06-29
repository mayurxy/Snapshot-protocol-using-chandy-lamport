//package Aosmt;
/**
 *
 * @author mayur
 */

import java.util.LinkedList;
import java.util.Queue;
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
//Tree stores the n value and l
class Tree{
	int n;
	int l;
	
	public Tree(int i, int j) {
		this.n = i;
		this.l = j;
	}
}
public class convergespan {
	
	static int[] prev;
     
	
	
	static void generate_span(int[][] adjMatrix){
               int flag =0, count=0, flag_span =0;
		boolean[] visited = new boolean[adjMatrix.length];
		prev = new int[adjMatrix.length];//getting parent node for current
		Queue<Tree> queue = new LinkedList<>();
		
                /*V[at]=true;
		
		// recursively visit every node connected to this that we have not already visited
		for (int i=0; i<N; ++i)
			/*if (G\Tree[at][i] && !Tree[i])
			{
				System.out.printf("Going to node %d...",i);
				
			}*/
                
                queue.add(new Tree(0,0));
		prev[0] = 0; //zeroth node beginning
		flag_span =1;
		/*if (G\Tree[at][i] && !Tree[i])
			{
				System.out.printf("Going to node %d...",i);
				
			}*/
                visited[0] = true;
		while(!queue.isEmpty()){
                    flag =1;
                    Tree u = queue.remove();
                        count++;
                        if(flag ==1){
                        System.out.println("spanning begin");
                        count ++;
                        }
                        /*for (int i=0; i<N; ++i)
				if (G[at][i] && !V[i])
				{
					Q.offer(i);
					V[i]=true;
					
					System.out.printf("Adding node %d to the queue in the BFS%n", i);
				}*/
			for(int i=0;i<adjMatrix[u.n].length;i++){
				if(adjMatrix[u.n][i] == 1 && visited[i] == false){
					queue.add(new Tree(i,u.l+1));
					if(flag ==1){
                                        count++;
                                        System.out.println("inner if loop");
                                        }
                                        convergespan.prev[i] = u.n;
					visited[i] = true;
				}
			}
		}
	}
        
        
     //		buildSpanningTree(adjMatrix);
//		for(int i=0;i<adjMatrix.length;i++)
//		System.out.println("Node  "+i+" Parent is "+get_ancestor(i));   
        
        
        
public static int get_ancestor(int id) {
		return prev[id];
	}

}
