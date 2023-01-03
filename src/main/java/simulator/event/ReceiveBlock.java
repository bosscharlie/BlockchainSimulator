package simulator.event;

import simulator.datastructure.Block;
import simulator.node.Node;

//节点接收区块事件
public class ReceiveBlock extends Event{
	
	private Block block;
	private Node from;
	
	
	public ReceiveBlock(double eventTime,Node node,Block block,Node from) {
		super(eventTime,node);
		this.block = block;
		this.from = from;
	}
	
	public void process() {
		//触发节点的接收区块处理逻辑
		super.node.receiveBlock(this);
	}
	
	public Block getBlock() {
		return this.block;
	}
	
	public Node getFrom() {
		return this.from;
	}
}
