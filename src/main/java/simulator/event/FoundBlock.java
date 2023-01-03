package simulator.event;

import simulator.datastructure.Block;
import simulator.node.Node;

//发现区块事件
public class FoundBlock extends Event{
	
	private Block block;
	
	public FoundBlock(double eventTime,Node node,Block block) {
		super(eventTime,node);
		this.block = block;
	}
	
	public void process() {
		
		//触发节点的发现区块处理逻辑
		super.node.foundBlock(this);	
	}
	
	public Block getBlock() {
		return this.block;
	}

}
