package simulator.node.consensus;

import simulator.datastructure.Block;
import simulator.node.Node;

//共识协议
public abstract class Consensus {

	protected Node node;
	
	public Consensus(Node node) {
		this.node = node;
	}
	
	//根据共识协议产生新的区块
	public abstract Block generateBlock(Block previousBlock,double startTime);
	
	public abstract Block generateBlock(Block previousBlock);

	
}
