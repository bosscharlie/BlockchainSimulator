package simulator.node;

import simulator.Scheduler;
import simulator.datastructure.Block;
import simulator.event.Event;

import simulator.event.FoundBlock;
import simulator.event.ReceiveBlock;
import simulator.tool.FileIO;

//自私挖矿节点
public class SelfishNode extends Node{

	public SelfishNode(String name,double hashrate) {
		super(name, hashrate);
	}
	
	Block targetBlock;
	int comfirmation = 6;
	int fail = 0;
	int succ = 0;

	public void print_rate() {
		System.out.println("fail:"+fail);
		System.out.println("succ:"+succ);
		System.out.println("rate:"+(double)succ/(succ+fail));
	}
	
	//接收一个新区块，进行链的更新操作
	public void receiveBlockScenario(Block block,Node node) {
		node.addNewBlock(block);
		if(targetBlock==null) {
			targetBlock = block;
			node.StartMining(node.generateNewBlock(targetBlock));
		}
		//若本地的链比接收到的区块短，则把本地的私有链清空，接收其他节点挖出来的区块。
		if(super.getDifferenceLen(block)<0){
			fail++;
			super.outputLog("fail");
			node.clearUnpublishedBlocks(); //把本地的私有链清空
			targetBlock = null;
			targetBlock = super.blockchain.getLatestBlock();
			node.StartMining(node.generateNewBlock(targetBlock));
		}
	}
	
	//发现区块事件
	public void foundBlockScenario(Block block,Node node) {
		if(super.getDifferenceLen(block)<=0){
			// System.out.print(Scheduler.getSimulationTime());
			// System.out.println("Err mining after recerving.");
			node.StartMining(node.generateNewBlock(targetBlock));
			return;
		}

		node.addUnpublishedBlocks(block); //挖到一个区块后不公开，加到本地私有链中
		FileIO.outBlockJson(block);
		
		//攻击成功，本地有私有链且长度比接收到的链更长，则公布自己的私有链
		//这里设定私有链发布的阈值为2，可以进行修改
		if(node.getDifferenceLen(targetBlock)==2){
			succ++;
			node.outputLog("succ");
			node.PublishBlock(); //把自己更长的私有链公布出来，得到收益
			targetBlock = null;
			targetBlock = node.blockchain.getLatestBlock();
			node.StartMining(node.generateNewBlock(targetBlock));
			
		//否则继续进行自私挖矿
		}else {
			node.StartMining(node.generateNewBlock(block));			
		}
	}
	
	public void receiveBlock(Event event) {
		ReceiveBlock receiveBlock = (ReceiveBlock)event;
		this.receiveBlockScenario(receiveBlock.getBlock(), receiveBlock.getNode());
	}
	
	public void foundBlock(Event event) {
		FoundBlock foundBlock = (FoundBlock)event;
		this.foundBlockScenario(foundBlock.getBlock(), foundBlock.getNode());
	}
	
	//初始化节点事件，开始挖矿
	public void initNodeScenario(Node node) {
		node.StartMining(node.generateNewBlock(Scheduler.getSimulationTime()));
		targetBlock = node.blockchain.getLatestBlock();
	}

	public void initNode(Event event) {
		this.initNodeScenario(event.getNode());
	}
}
