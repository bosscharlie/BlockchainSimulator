package simulator;

import java.util.ArrayList;
import simulator.event.Event;
import simulator.event.FoundBlock;
import simulator.tool.FileIO;

//事件列表
public class EventList {
	
	private ArrayList<Event> eventList;
	
	public EventList() {
		this.eventList = new ArrayList<Event>();
	}
	
	//添加新事件，事件按最早发生时间到最早发生时间的顺序添加
	public void pushEvent(Event newEvent) {
		double newEventTime = newEvent.getEventTime();
		
		//检查是否有重复的Found事件，队列里一个节点只有一个Found事件
		if(newEvent.getEventType().equals("FoundBlock")) {
			for(int i=0;i < this.eventList.size();i++) {
				if(this.eventList.get(i).getNode().getName().equals(newEvent.getNode().getName())
						&&this.eventList.get(i).getEventType().equals("FoundBlock")){

					this.eventList.remove(i);
					break;
				}
			}
		}
		for(int i = 0; i < this.eventList.size(); i++) {
			Event event =  this.eventList.get(i);
			double eventTime = event.getEventTime();
			
			if(newEventTime < eventTime) {
				this.eventList.add(i,newEvent);
				return;
			}
		}
		this.eventList.add(newEvent);
	}
	
	// 返回最早发生的event
	public Event popEvent() {
		if (this.eventList.size() == 0) {
			return null;
		}
		Event event = this.eventList.get(0);
		FileIO.outEventJson(event);
		this.eventList.remove(0);
		return event;
	}
	
	//根据ID删除事件
	public void removeEvent(int eventID) {
		for(int i = 0; i < this.eventList.size(); i++) {
			Event event = this.eventList.get(i);
			int _eventID =  event.getEventID();
			if(eventID == _eventID) {
				System.out.println("イベント削除");
				this.eventList.remove(i);
				break;
			}
		}
	}
	
	//查找节点的发现区块事件
	public FoundBlock getEventFound(String nodeName) {
		for(Event event : eventList) {
			String _nodeName = event.getNode().getName();
			if(!event.getEventType().equals("FoundBlock")) {
				continue;
			}	
			if(nodeName.equals(_nodeName)) {
				return (FoundBlock)event;
			}
		}
		return null;
	}
	
	public ArrayList<Event> getEventList(){
		return this.eventList;
	}
}
