package simulator.datastructure;
import simulator.node.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// 区块信息
public class Block implements Cloneable{
	
	private String hash;
	private String previousHash;
	private double timestamp; //区块生成时间
	private int height;
	private Node miner;  //产生该区块的矿工
	private Block previousBlock;
	private ArrayList<Block> nextBlocks;  //后继区块
	private double receiveBlockTime;
	private Set<Node> transmittedNodes = new HashSet<>(); //已被传播到的节点
	
	//生成创世区块
	public Block(
			String hash,
			String previousHash,
			double timestamp,
			int height,
			Node miner) {
			
		this.hash = hash;
		this.previousHash = previousHash;
		this.timestamp = timestamp;
		this.height = height;
		this.miner = miner;
		this.nextBlocks = new ArrayList<Block>();
	}
	
	// 创建一般类型的区块
	public Block(
			String hash,
			Block previousBlock,
			double timestamp,
			Node miner) {
				
		this.hash = hash;
		this.previousHash = previousBlock.getHash();
		this.timestamp = timestamp;
		this.height = previousBlock.getHeight() + 1;
		this.miner = miner;
		this.nextBlocks = new ArrayList<Block>();
	}
	
	public void addNextBlock(Block block) {
		this.nextBlocks.add(block);
	}
	
	public ArrayList<Block> getNextBlocks() {
		return nextBlocks;
	}
	
	public void setPreviousBlock(Block previousBlock) {
		this.previousBlock = previousBlock;
	}

	public Block getPreviousBlock() {
		return this.previousBlock;
	}

	public void setReceiveBlockTime(double time) {
		this.receiveBlockTime = time;
	}
	
	public double getReceiveBlockTime() {
		return this.receiveBlockTime;
	}
	
	public String getHash() {
		return this.hash;
	}
	
	public String getPreviousHash() {
		return this.previousHash;
	}
	
	
	public double getTimestamp() {
		return this.timestamp;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public Node getMiner() {
		return this.miner;
	}
	
	public void print() {
		System.out.println("{");
		System.out.println("  ”hash” : " + this.hash + ",");
		System.out.println("  ”previousHash” : " + this.previousHash + ",");
		System.out.println("  ”timestamp” : " + this.timestamp + ",");
		System.out.println("  ”height;” : " + this.height + ",");
		System.out.println("  ”miner” : " + this.miner.getName() + ",");
		System.out.println("}");
	}
	
	public static Block cloneBlock(Block block) {
		if(block == null) {
			return null;
		}
		Block cloneBlock = new Block(
				block.getHash(),
				block.getPreviousHash(),
				block.getTimestamp(),
				block.getHeight(),
				block.getMiner());
		cloneBlock.setTransmittedNodes(block.getTransmittedNodes());
		return cloneBlock;
	}
	
	//该区块已经被传播到的节点列表
	public Set<Node> getTransmittedNodes(){
		return this.transmittedNodes;
	}
	
	public void addTransmittedNodes(Node node) {
		this.transmittedNodes.add(node);
	}
	
	public void setTransmittedNodes(Set<Node> transmittedNodes) {
		this.transmittedNodes = transmittedNodes;
	}
	
	//验证某一节点是否接收过该区块了
	public boolean verifyNode(Node node) {
		return this.transmittedNodes.contains(node);
	}
	
}

