package simulator.datastructure;

import java.util.ArrayList;
import simulator.Scheduler;
import simulator.node.Node;

public class Blockchain {
	private int topHeight = 0;
	private Block topBlock;
	private Block genesisBlock;
	private String chainID;
	private BlockCache blockCache;
	private Node node;
	
	public Blockchain(String chainID,Node node) {
		this.genesisBlock = Genesis.getGenesis();
		this.chainID = chainID;
		this.node = node;
		this.blockCache = new BlockCache();
		this.topBlock = this.genesisBlock;
	}
	
	public void addBlock(Block clone_block) {
		
		Block block = Block.cloneBlock(clone_block);
		
		//之前已经处理过这个区块了
		if(block.verifyNode(this.node)) {
			return;
		}
		
		//验证区块的正确性，获取前一个区块
		Block targetBlock = this.serchBlockHash(block.getPreviousHash());
		
		//前一个区块不存在
		if(targetBlock == null) {
			System.out.print(Scheduler.getSimulationTime());
			System.out.println("Err targetBlock == null");
			System.exit(0);
			return;
		}
		
		//和前一个区块的高度不匹配
		if(targetBlock.getHeight()+1 != block.getHeight()) {
			System.out.println("Err targetBlock.getHeight()+1 != block.getHeight()");
			System.exit(0);
			return;
		}
		block.setReceiveBlockTime(Scheduler.getSimulationTime());
		
		//区块上链
		targetBlock.addNextBlock(block);
		block.setPreviousBlock(targetBlock);
		this.blockCache.addBlock(block);
		block.addTransmittedNodes(this.node);
		Scheduler.addBlock(block);
		
		//更新top block，正常更新
		if(this.topHeight < block.getHeight()) {
			this.topHeight = block.getHeight();
			this.topBlock = block;	
		//如果块的高度相同，则优先考虑自己本地的块
		}else if(this.topHeight == block.getHeight() && 
				chainID.equals(block.getMiner().getName())) 
		{
			this.topBlock = block;
		}
		
	}
	
	public Block getLatestBlock() {
		return this.topBlock;
	}
	
	public Block serchBlockHash(String hash) {
		Block block = this.serchCacheHash(hash);
		if(block != null) {
			return block;
		}
		
		ArrayList<Block> stack = new ArrayList<Block>();
		stack.add(this.genesisBlock);
		while(stack.size()!=0) {
			Block nextBlock = stack.get(stack.size()-1);
			stack.remove(stack.size()-1);			
			
			if(nextBlock.getHash().equals(hash)) {
				return nextBlock;
			}
			for(Block NB : nextBlock.getNextBlocks()) {
				stack.add(NB);
			}
		}
		return null;
	}
	
	public int getTopHeight() {
		return this.topHeight;
	}

	public int getHeight() {
		int maximum = -1;
		ArrayList<Block> stack = new ArrayList<Block>();
		stack.add(genesisBlock);
		
		while(stack.size()!=0) {
			
			Block nextBlock = stack.get(stack.size()-1);
			stack.remove(stack.size()-1);
			
			if(nextBlock.getHeight() > maximum) {
				maximum = nextBlock.getHeight();
			}
			
			for(Block NB : nextBlock.getNextBlocks()) {
				stack.add(NB);
			}
		}
		return maximum;
	}
	
	public String getChainID() {
		return this.chainID;
	}
	
	public Block getGenesis() {
		return this.genesisBlock;
	}
	
	//优先查询缓存，提高处理速度
	public Block serchCacheHash(String hash) {
		return this.blockCache.serchHash(hash);
	}
	
	public Block serchCacheHeight(int Height) {
		return this.blockCache.serchHeight(Height);
	}
}
