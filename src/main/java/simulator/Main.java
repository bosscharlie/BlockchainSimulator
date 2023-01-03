package simulator;

import simulator.network.NetworkGenerator;
import simulator.tool.FileIO;

public class Main {
	public static void main(String[] args) {
		long start_point = System.currentTimeMillis();
		
		//文件目录设置
		FileIO.deleteDirectory();
		FileIO.createDirectory();
		FileIO.startProcess();

		//从配置文件中生成网络拓扑结构
		// NetworkGenerator.inputNetworkConfig();

		//从配置文件中生成节点列表
		NetworkGenerator.inputNodeConfig();

		//生成全连接网络
		NetworkGenerator.generateFullyConnectedNetwork();

		//生成环状网络
		// NetworkGenerator.generateRingNetwork();
		
		//仿真运行
		Scheduler.InitEventList();
		Scheduler.processEvent();

		//手动设计事件列表
		// Event newEvent = new FoundBlock(addBlockTime,this,block);
		// Scheduler.addNewEvent(newEvent); //添加一个发送区块事件
		
		//输出节点网络拓扑
		FileIO.OutAdjacencyMatrix();
		FileIO.endProcess();
		
		long end_point = System.currentTimeMillis();
		long time = end_point - start_point;
	    System.out.println("simulation time: " + time);	    
	}

}
