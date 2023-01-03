package simulator.event;

import simulator.node.Node;

//初始化节点事件
public class InitNode extends Event{

	public InitNode(double eventTime,Node node) {
		super(eventTime,node);
	}

	public void process() {

		//触发节点的初始化处理逻辑
		super.node.initNode(this);
	}
}
