package simulator.node;

import simulator.event.Event;

//创世节点
public class GenesisNode extends Node{
	
	private static Node genesisNode = new GenesisNode();
	
	public GenesisNode() {
		super("Genesis",0);
	}
	
	public static Node getInstance() {
		return GenesisNode.genesisNode;
	}
	
	public void initNode(Event event) {}
	
	public void receiveBlock(Event event) {}
	
	public void foundBlock(Event event) {}
}


