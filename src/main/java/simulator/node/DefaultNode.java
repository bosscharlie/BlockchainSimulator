package simulator.node;

import simulator.Scheduler;
import simulator.datastructure.Block;
import simulator.event.Event;
import simulator.event.FoundBlock;
import simulator.event.ReceiveBlock;
import simulator.tool.FileIO;

//默认的普通节点类型
public class DefaultNode extends Node{

	public DefaultNode(String name,double hashrate) {
		super(name, hashrate);
	}
	
	//接收一个新区块
	public void receiveBlock(Event event) {
		
		Node currentNode = event.getNode();
		ReceiveBlock eventCast = (ReceiveBlock)event;
		Block newBlock = eventCast.getBlock();
		int newBlockIndex = newBlock.getHeight();
		
		//向周围相邻节点进行传播
		super.PropagateBlock(newBlock);
		
		// 之前有自己挖出来的区块
		// Found Block
		FoundBlock foundEvent = Scheduler.getFoundEvent(currentNode);
		int foundEventBlockIndex = 0;
		if(foundEvent != null) {
			Block foundEventBlock = foundEvent.getBlock();
			foundEventBlockIndex = foundEventBlock.getHeight();
		}
		
		//不再公布之前发现的未发布的区块
		//当其他节点发现更长的区块时，在本次接收到的节点后面继续挖矿
		if(newBlockIndex+1 >= foundEventBlockIndex) {
			if(super.unpublishedBlocks.size()>=2) {
				System.out.print(super.unpublishedBlocks.size());
				System.exit(0);
			}
			
			//追加一个新的区块
			currentNode.addNewBlock(newBlock);
			Block addBlock = currentNode.generateNewBlock(Scheduler.getSimulationTime());
			double addBlockTime = addBlock.getTimestamp();
			Event newEvent = new FoundBlock(addBlockTime,currentNode,addBlock);
			Scheduler.addNewEvent(newEvent);
		}else {
			currentNode.addNewBlock(newBlock);
		}
	}
	
	//处理发现区块事件，一般节点发现一个新区块后立刻对该区块进行发布
	public void foundBlock(Event event) {
		Node currentNode = event.getNode();
		FoundBlock foundBlock = (FoundBlock)event;
		Block newBlock = foundBlock.getBlock();
		if(super.blockchain.getHeight()>=newBlock.getHeight()){
			// System.out.print(Scheduler.getSimulationTime());
			// System.out.println("Err mining after recerving.");
			Block addBlock = currentNode.generateNewBlock(Scheduler.getSimulationTime());
			double addBlockTime = addBlock.getTimestamp();
			Event addEvent = new FoundBlock(addBlockTime,currentNode,addBlock);
			Scheduler.addNewEvent(addEvent);
			return;
		}
		super.unpublishedBlocks.add(newBlock);
		FileIO.outBlockJson(newBlock);
		super.PublishBlock();    //发现一个节点后立即公布
	
		//添加一个新区块发现事件，连续挖矿
		Block addBlock = currentNode.generateNewBlock(Scheduler.getSimulationTime());
		double addBlockTime = addBlock.getTimestamp();
		Event addEvent = new FoundBlock(addBlockTime,currentNode,addBlock);
		Scheduler.addNewEvent(addEvent);
	}

	public void initNodeScenario(Node node) {
		node.StartMining(node.generateNewBlock(Scheduler.getSimulationTime()));
	}
	
	public void initNode(Event event) {
		
		this.initNodeScenario(event.getNode());
	}
}
