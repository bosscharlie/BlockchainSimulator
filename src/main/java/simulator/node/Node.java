package simulator.node;

import simulator.Scheduler;
import simulator.datastructure.Block;
import simulator.datastructure.Blockchain;
import simulator.event.Event;
import simulator.event.FoundBlock;
import simulator.event.ReceiveBlock;
import simulator.node.consensus.Consensus;
import simulator.node.consensus.PoW;
import simulator.network.Network;
import simulator.tool.FileIO;
import java.util.ArrayList;

public abstract class Node {
	protected Blockchain blockchain;
	private String nodeID;
	private Consensus consensus;
	protected ArrayList<Block> unpublishedBlocks; //保存未发布区块
	
	//生成创世区块的节点
	public Node(String genesis) {
		this.nodeID = genesis;
	}
	
	//生成一般节点，设置节点名称及hashrate哈希率
	public Node(String name, double hashrate) {
		this.blockchain = new Blockchain(name,this);
		this.nodeID = name;
		this.consensus = new PoW(this, hashrate); //使用PoW共识
		this.unpublishedBlocks = new ArrayList<Block>();
	}
	
	public double getHashrate() {
		PoW _pow =  (PoW)this.consensus;
		return _pow.getHashrate();
	}

	public abstract void initNode(Event event);
	
	public abstract void receiveBlock(Event event);
	
	public abstract void foundBlock(Event event);

	//传播区块
	public void PropagateBlock(Block block) {
		ArrayList<Node> nodeList =  Network.getAdjacencyNode(this.getName()); //得到相邻的节点列表
		double delay = Network.getBlockDelay(); //区块传输延迟
		Block newBlock = block;
		for(int i = 0; i < nodeList.size(); i++) {
			Node destinationNode = nodeList.get(i);
			if(block.verifyNode(destinationNode)) {
				continue;
			}		
			Event newEvent = new ReceiveBlock(Scheduler.getSimulationTime() + delay,destinationNode,newBlock,this); //添加一个接收区块事件
			Scheduler.addNewEvent(newEvent);
		}	
	}
	
	//发布未发布的区块
	public void PublishBlock() {
		ArrayList<Node> nodeList =  Network.getAdjacencyNode(this.getName());
		double delay = Network.getBlockDelay();
		for(int j=0;this.unpublishedBlocks.size()>j;j++){
			Block newBlock = this.unpublishedBlocks.get(j);
			this.addNewBlock(newBlock);
			for(int i = 0; i < nodeList.size(); i++) {
				Node destinationNode = nodeList.get(i);	
				//错误检查
				if(this.getName().equals(destinationNode.getName())) {
					//自己发送给自己
					System.exit(0);
				}
				
				Event newEvent = new ReceiveBlock(Scheduler.getSimulationTime() + delay,destinationNode,newBlock,this);
				Scheduler.addNewEvent(newEvent);
			}
		}
		this.unpublishedBlocks = new ArrayList<Block>();
	}
	
	//开始挖矿生成区块
	public void StartMining(Block block) {
		double addBlockTime = block.getTimestamp();
		Event newEvent = new FoundBlock(addBlockTime,this,block);
		Scheduler.addNewEvent(newEvent); //添加一个发送区块事件
	}

	// 停止挖矿
	public void StopMining() {
		FoundBlock foundEvent = Scheduler.getFoundEvent(this);
		if(foundEvent!=null) {
			int eventID = foundEvent.getEventID();
			Scheduler.removeEvent(eventID); //把后续还没执行的挖矿事件从事件队列中移除
		}
	}

	//生成新区块
	public Block generateNewBlock(double startTime) {
		Block previousBlock = blockchain.getLatestBlock();
		if(unpublishedBlocks.size()!=0) {
			System.exit(0);
			if(unpublishedBlocks.get(unpublishedBlocks.size()-1).getHeight()>=previousBlock.getHeight()) {
				previousBlock = unpublishedBlocks.get(unpublishedBlocks.size()-1);
			}
		}

		//根据共识协议的规则生成新区块
		Block nextBlock = this.consensus.generateBlock(previousBlock,startTime);
		return nextBlock;
	}
	
	public Block generateNewBlock() {
		Block previousBlock = blockchain.getLatestBlock();
		
		if(unpublishedBlocks.size()!=0) {
			System.exit(0);
			if(unpublishedBlocks.get(unpublishedBlocks.size()-1).getHeight()>=previousBlock.getHeight()) { //selfish mining，未发布的连比原来的链更长，则在自己的未发布的区块上继续生成新的区块
				previousBlock = unpublishedBlocks.get(unpublishedBlocks.size()-1);
				
			}
		}
		Block nextBlock = this.consensus.generateBlock(previousBlock);
		return nextBlock;
	}
	
	public Block generateNewBlock(Block previousBlock) {
		
		Block nextBlock = this.consensus.generateBlock(previousBlock);
		
		return nextBlock;
	}
	
	//在节点的区块链上添加一个新的区块
	public void addNewBlock(Block block) {
		Block cloneBlock = Block.cloneBlock(block);
		this.blockchain.addBlock(cloneBlock);
	}
	
	public Blockchain getBlockchain() {
		return this.blockchain;
	}
	
	public String getName() {
		return this.nodeID;
	}
	
	// Localchain中添加一个新的区块
	public void addUnpublishedBlocks(Block block) {
		this.unpublishedBlocks.add(block);
	}
	
	// Localchain的长度
	public int getHeightunpublishedBlock() {
		int height = -1;
		if(this.unpublishedBlocks.size()!=0) {
			height = this.unpublishedBlocks.get(0).getHeight();
		}
		return height;
	}
	
	// 重置本地未公开的区块
	public void clearUnpublishedBlocks() {
		this.unpublishedBlocks = new ArrayList<Block>();
	}

	
	// 找出公有链和本地私有链长度的差，也就是地私有未发布的区块的长度
	public int getDifferenceLen(Block vertex) {
		Block latestBlock = this.blockchain.getLatestBlock();
		int local = 0;
		if(vertex != null) {
			local = vertex.getHeight();
		}
		if(this.unpublishedBlocks.size()!=0) {
			Block unpublishedBlock = this.unpublishedBlocks.get(this.unpublishedBlocks.size()-1);
			local = unpublishedBlock.getHeight();
		}
		return local - latestBlock.getHeight();
	}
	// 给定一个区块所在链，和本地私有链长度之差
	public int getPublicBranch(Block vertex) {
		int local = vertex.getHeight();
		if(this.unpublishedBlocks.size()!=0) {
			Block unpublishedBlock = this.unpublishedBlocks.get(this.unpublishedBlocks.size()-1);
			local = unpublishedBlock.getHeight();
		}
		return local - vertex.getHeight();
	}
	
	// 私有链上分叉的长度
	public int getPrivateBranch(Block vertex) {
		Block latestBlock = this.blockchain.getLatestBlock();
		return latestBlock.getHeight() - vertex.getHeight();
	}

	public void outputLog(String log) {
		FileIO.outAttackLog(log);
	}
	
}
