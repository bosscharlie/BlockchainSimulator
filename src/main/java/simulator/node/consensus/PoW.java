package simulator.node.consensus;

import simulator.Scheduler;
import simulator.configuration.Configuration;
import simulator.datastructure.Block;
import simulator.node.Node;
import simulator.tool.HashGenerator;

//PoW共识，可以开发其他的共识协议
public class PoW extends Consensus{
	
	private double hashrate;
	
	public PoW(Node node, double hashrate) {
		super(node);
		this.hashrate = hashrate;
	}

	//指定一个时间生成一个新区块
	public Block generateBlock(Block previousBlock, double startTime) {
		String previoushash = previousBlock.getHash();
		String randomString =String.valueOf(Math.random());
		String hash = HashGenerator.generateHash(randomString + previoushash);
		double timestamp = startTime + this.blocktime();
		Block newBlock = new Block(hash, previousBlock, timestamp, this.node);
		newBlock.setPreviousBlock(previousBlock);
		return newBlock;
	}
	
	//按hashrate生成一个新区块
	public Block generateBlock(Block previousBlock) {
		String previoushash = previousBlock.getHash();
		String randomString =String.valueOf(Math.random());
		String hash = HashGenerator.generateHash(randomString + previoushash);
		double timestamp = Scheduler.getSimulationTime() + this.blocktime();
		Block newBlock = new Block(hash, previousBlock, timestamp, this.node);
		newBlock.setPreviousBlock(previousBlock);
		return newBlock;
	}
	
	//根据区块产生间隔和哈希率模拟区块产生的时间
	private double blocktime() {
		double lambda=0;
		lambda = Configuration.BLOCK_INTERVAL / this.hashrate;
		double time = -1.0 * lambda * Math.log(1.0 - Math.random());
		// double time = lambda;
		return time;		
	}
	
	public void setHashrate(double hashrate) {
		this.hashrate = hashrate;
	}
	
	public double getHashrate() {
		return this.hashrate;
	}
}
