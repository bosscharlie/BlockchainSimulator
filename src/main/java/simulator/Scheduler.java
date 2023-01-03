package simulator;

import simulator.configuration.Configuration;
import simulator.datastructure.Block;
import simulator.datastructure.Blockchain;
import simulator.event.Event;
import simulator.event.FoundBlock;
import simulator.event.InitNode;
import simulator.node.Node;
import simulator.network.Network;
import java.util.ArrayList;

//事件队列调度器
public class Scheduler {
	private static EventList eventList = new EventList();
	private static int eventCounter = 0;
	private static double simulationTime = 0;
	
	//添加一个新事件
	public static void addNewEvent(Event event) {
		if(event.getEventTime() > Configuration.SIMULATION_TIME){
			return;
		}
		if(event.getEventTime() < Scheduler.simulationTime) {
			System.out.println("事件时间设置错误");
			System.exit(0);
		}
		event.setEventID(Scheduler.eventCounter);
		Scheduler.eventCounter = eventCounter + 1;
		Scheduler.eventList.pushEvent(event);
	}
	
	//处理下一个event事件
	public static void processEvent() {
		while(true) {
			//处理到列表为空
			if(eventList.getEventList().size()==0) {
				break;
			}
			Event event = Scheduler.eventList.popEvent();
			
			//检查是否已超过结束时间，事件时间超过仿真时间时发布未发布的区块
			if(Configuration.SIMULATION_TIME <  event.getEventTime()) {
				ArrayList<Node> nodeList = Network.getNodeList();
				for(int i=0;nodeList.size() > i;i++) {
					nodeList.get(i).PublishBlock();
				}
				break;
			}
			Scheduler.simulationTime = event.getEventTime();
			event.process();
		}
	}
	
	//删除一个事件
	public static void removeEvent(int eventID) {
		System.out.println(eventID);
		Scheduler.eventList.removeEvent(eventID);
	}
	
	//获取仿真时间
	public static double getSimulationTime() {
		return Scheduler.simulationTime;
	}
	
	// 返回指定节点的事件
	public static FoundBlock getFoundEvent(Node node) {
		String nodeName = node.getName();
		FoundBlock event = eventList.getEventFound(nodeName);
		return event;
	}
	
	// 初始化添加Init事件
	public static void InitEventList() {
		ArrayList<Node > nodeList = Network.getNodeList();
		for(Node node:nodeList) {
			Event initEvent = new InitNode(Scheduler.simulationTime, node);
			Scheduler.addNewEvent(initEvent);
		}
	}
	
	//一个全局链用来保存数据
	private static Blockchain blockchain = new Blockchain("main", null);
	
	public static void addBlock(Block block) {
		Scheduler.blockchain.addBlock(block);
	}
	public static Blockchain getBlockchain() {
		return Scheduler.blockchain;
	}
}
