package simulator.configuration;

//仿真相关各项参数的配置
public class Configuration {	

	//仿真时长
	public static final double SIMULATION_TIME = 100;

	//产生区块的时间间隔
	public static final double BLOCK_INTERVAL = 10;

	//模拟系统使用的共识协议
	public static final String CONSENSUS = "PoW";

	//区块大小
	public static final double BLOCK_SIZE = 8;

	//挖到区块的奖励
	public static final double BLOCK_REWARD = 10;
	
	//仿真网络的配置

	//网络中的节点数目
	public static final int NUMBER_OF_NODES = 5;
	
	//已列表形式设定各节点的哈希率
	public static final double[] HASHRATE_LIST = 
		{
				0.2,0.2,0.1,0.1,0.2,
		};
	
	//区块传播延迟
	public static final double BLOCK_DELAY = 2;
	
	//可以以邻接矩阵的形式对网络拓扑进行配置
	public static final int[][] ADJACENCY_MATRIX = 
		{
				{0,1},
				{1,0}
		};	
	
	public static final int[][] ADJACENCY_MATRIX2 = 
		{
				{0,1,1,1,1},
				{1,0,1,1,1},
				{1,1,0,1,1},
				{1,1,1,0,1},
				{1,1,1,1,0}
		};
}


