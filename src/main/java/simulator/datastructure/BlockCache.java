package simulator.datastructure;

import java.util.ArrayList;

//对区块链做本地缓存，便于进行快速查找
public class BlockCache {
	private ArrayList<Block> blockCache;
	private int cacheSize = 1000;
	
	public BlockCache() {
		this.blockCache = new ArrayList<Block>();
	}
	
	public void addBlock(Block block) {
		this.blockCache.add(block);
		if(this.cacheSize <= this.blockCache.size()) { //FIFO替换
			this.blockCache.remove(0);
		}
	}
	
	public Block serchHash(String hash) {
		
		for(int i=0; this.blockCache.size() > i; i++) {
			Block block = blockCache.get(this.blockCache.size()-i-1);
			if(block.getHash().equals(hash)) {			
				return block;
			}
		}
		return null;
	}
	
	public Block serchHeight(int height) {
		for(int i=0; this.blockCache.size() < i; i++) {
			Block block = blockCache.get(this.blockCache.size()-i-1);
			if(block.getHeight() == height) {
				return block;
			}
		}
		return null;
	}
}
