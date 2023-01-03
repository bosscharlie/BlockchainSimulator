package simulator.network;


import simulator.configuration.Configuration;
import simulator.node.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//拓扑网络生成器，提供了多种生成网络的方式
public class NetworkGenerator {
	
	private static final String NETWORK_SETTING = "./inputFile/network.csv";
	private static final String NODE_SETTING = "./inputFile/nodeInfo.csv";
	
	//从邻接矩阵中创建节点列表
	public static void generateNode() {
		ArrayList<Node> nodeList = new ArrayList<Node>();
		int network[][] = Network.getAdjacencyMatrix();
		int nodeId = 0;
		for(int i=0;i<network[0].length;i++) { //列数代表节点总数
			
			Node node = new DefaultNode(String.valueOf(nodeId),0.1);
			
			nodeList.add(node);
			nodeId++;
		}
		Network.initNodeList(nodeList);
	}
	
	//从外部文件读入模拟区块链网络
	public static void inputNetworkFile() {
		int network[][] = new int[Configuration.NUMBER_OF_NODES][Configuration.NUMBER_OF_NODES];
		
		File file = new File(NetworkGenerator.NETWORK_SETTING);
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
		    String line;
		   
		    int _line = 0;
		    while ( ( line = br.readLine()) != null ) {
		    	String[] cols = line.split(",");
		    	
		    	for(int i=0;i<cols.length;i++) {
		    		if(cols[i].equals("1")) {
		    			network[_line][i] = 1;
		    		}
		    	}
		        _line++;
		    }
		    br.close();
		} catch(Exception e) {
		      e.printStackTrace();
		}
		Network.initAdjacencyMatrix(network);
		
	}
	
	//从外部文件读入节点列表
	public static void inputNodeFile() {
		ArrayList<Node> nodeList = new ArrayList<Node>();
		int nodeId = 0;
		File file = new File(NetworkGenerator.NODE_SETTING);
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
		    String line;
		   
		    while ( ( line = br.readLine()) != null ) {
		    	String[] cols = line.split(",");
		    	
		    	for(int i=0;i<cols.length;i++) {
					//默认所有的节点都是同一类型，可以更换自己的节点类型
		    		Node node = new DefaultNode(String.valueOf(nodeId),Double.parseDouble(cols[i]));
		    		nodeList.add(node);
		    		nodeId++;
		    	}
		    }
			br.close();
		} catch(Exception e) {
		      e.printStackTrace();
		}
		Network.initNodeList(nodeList);
	}
	
	//从Configuration中生成网络
	public static void inputNetworkConfig() {
		Network.initAdjacencyMatrix(Configuration.ADJACENCY_MATRIX);
	}
	
	//从Configuration中生成节点列表
	public static void inputNodeConfig() {
		ArrayList<Node> nodeList = new ArrayList<Node>();
		int nodeId = 0;
		for(int i=0;i < Configuration.NUMBER_OF_NODES;i++) {
			if(i==0) { //生成一个自私挖矿的节点
    			Node node = new SelfishNode(String.valueOf(nodeId),Configuration.HASHRATE_LIST[i]);
    			nodeList.add(node);
    			nodeId++;
    		}else {
    			Node node = new DefaultNode(String.valueOf(nodeId),Configuration.HASHRATE_LIST[i]);
    			nodeList.add(node);
    			nodeId++;
    		}
		}
		Network.initNodeList(nodeList);
	}
	
	//生成环形网络
	public static void generateRingNetwork() {
		int ringNetwork[][] = new int[Configuration.NUMBER_OF_NODES][Configuration.NUMBER_OF_NODES];
		for(int i=0;i < ringNetwork.length;i++) {
			ringNetwork[i][(i+1)%ringNetwork.length] = 1;
			ringNetwork[i][(ringNetwork.length+i-1)%ringNetwork.length] = 1;
		}
		Network.initAdjacencyMatrix(ringNetwork);
	}
	
	//生成全相连网络
	public static void generateFullyConnectedNetwork() {
		int fullyConnectedNetwork[][] = new int[Configuration.NUMBER_OF_NODES][Configuration.NUMBER_OF_NODES];
		for(int i=0;i < fullyConnectedNetwork.length;i++) {
			for(int j=0;j < fullyConnectedNetwork.length;j++) {
				if(i==j) {
					continue;
				}
				fullyConnectedNetwork[i][j] = 1;
			}
		}
		Network.initAdjacencyMatrix(fullyConnectedNetwork);
	}
			
	//生成线性网络
	public static void generateLineNetwork() {
		int lineNetwork[][] = new int[Configuration.NUMBER_OF_NODES][Configuration.NUMBER_OF_NODES];
		for(int i=0;i < lineNetwork.length-1;i++) {
			lineNetwork[i][(i+1)%lineNetwork.length] = 1;
			lineNetwork[i+1][(i)%lineNetwork.length] = 1;
		}
		Network.initAdjacencyMatrix(lineNetwork);
	}
}
