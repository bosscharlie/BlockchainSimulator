package simulator.network;

import simulator.configuration.Configuration;
import simulator.node.Node;
import java.util.ArrayList;

//仿真节点网络
public class Network {
	
	//节点的邻接矩阵
	private static int[][] adjacencyMatrix;
	
	//网络中的节点列表
	private static ArrayList<Node> nodeList = new ArrayList<Node>();
	
	//初始化邻接矩阵
	public static void initAdjacencyMatrix(int[][] adjacencyMatrix) {
		Network.adjacencyMatrix = adjacencyMatrix;
	}
	
	//初始化节点列表
	public static void initNodeList(ArrayList<Node> nodeList) {
		Network.nodeList = nodeList;
	}
	
	//获得一个节点的相邻节点
	public static ArrayList<Node> getAdjacencyNode(String name) {
		ArrayList<Node> adjacencyNode = new ArrayList<Node>();
		int nodeIndex = -1;
		for(int i=0;i<Network.nodeList.size();i++) {
			String nodeName = Network.nodeList.get(i).getName();
			if(nodeName.equals(name)) {
				nodeIndex = i;
				break;
			}
		}
		for(int i=0;i<Network.adjacencyMatrix[nodeIndex].length;i++) {
			if(Network.adjacencyMatrix[nodeIndex][i]==1) {
				adjacencyNode.add(nodeList.get(i));
			}
		}
		
		return adjacencyNode;
	}
	
	//动态扩充网络
	public static void addNewNode(Node node) {
		int _adjacencyMatrix[][] = new int[Network.adjacencyMatrix.length+1][Network.adjacencyMatrix.length+1];
		
		for(int i=0;i<Network.adjacencyMatrix.length;i++) {
			for (int j = 0; j < Network.adjacencyMatrix.length; j++) {
				_adjacencyMatrix[i][j] = adjacencyMatrix[i][j];
			}
		}
		Network.adjacencyMatrix = _adjacencyMatrix;
		Network.nodeList.add(node);
	}
	
	//添加两个节点间的连接
	public static void addConnection(int from,int to) {
		Network.adjacencyMatrix[from][to] = 1;
	}
	
	public static void deleteConnection(int from,int to) {
		Network.adjacencyMatrix[from][to] = 0;
	}
	
	
	//节点间的块传输延迟
	public static double getBlockDelay() {
		return Configuration.BLOCK_DELAY;
	}
	
	public static int[][] getAdjacencyMatrix() {
		return Network.adjacencyMatrix;
	}
	
	public static ArrayList<Node> getNodeList(){
		return Network.nodeList;
	}
	
	public static void printNetwork() {
		for(int i=0;i<Network.adjacencyMatrix.length;i++) {
			for(int j=0;j<Network.adjacencyMatrix.length;j++) {
				System.out.print(Network.adjacencyMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void printNodeList() {
		for(int i=0;i<Network.nodeList.size();i++) {
			System.out.println("id:"+Network.nodeList.get(i).getName()+"|"+ "hashrate:"+Network.nodeList.get(i).getHashrate());
		}
	}
}
