//package Aosmt;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
/**
 *
 * @author mayur
 */
public class Node {
	int nodeId; //node indentifier 
	String host;//host name
	int port;
	public Node(int nodeId, String host, int port) {
		
		this.nodeId = nodeId;
		this.host = host;
		this.port = port;
	}
class treenode{     // tree class for determining of spanning tree for convergecast
	int node;
	int level;
	
	public treenode(int i, int j) {
		this.node = i;
		this.level = j;
	}
}

}
