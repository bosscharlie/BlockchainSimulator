package simulator.tool;

import simulator.configuration.Configuration;
import simulator.datastructure.Block;
import simulator.datastructure.Blockchain;
import simulator.datastructure.Genesis;
import simulator.event.Event;
import simulator.event.FoundBlock;
import simulator.event.ReceiveBlock;
import simulator.network.Network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class FileIO {
	
	
	private static String DIRECTORY_NAME = "outputFile";
	
	private static final String FILE_NAME1 = "blockchain.json";
	private static final String FILE_NAME2 = "event.json";
	private static final String FILE_NAME3 = "adjacencyMatrix.csv";
	private static final String FILE_NAME4 = "attackLog.txt";
	private static final String NEW_LINE= "\r\n";
	private static final String COMMA = ",";
	
	//初始化处理
	public static void startProcess() {
		FileWriter fw = null;
		try {
			File file = new File("./" + FileIO.DIRECTORY_NAME + "/" + FileIO.FILE_NAME2);
			fw = new FileWriter(file, true);
			fw.append("[");
			fw.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
		try {
			File file = new File("./" + FileIO.DIRECTORY_NAME + "/" + "block.json");
			fw = new FileWriter(file, true);
			fw.append("[");
			fw.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
		FileIO.outBlockJson(Genesis.getGenesis());  //创建创世区块
		
	}
	
	public static void endProcess() {
		FileWriter fw = null;
		try {
			File file = new File("./" + FileIO.DIRECTORY_NAME + "/" + FileIO.FILE_NAME2);
			fw = new FileWriter(file, true);
			fw.append("]");
			fw.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
		try {
			File file = new File("./" + FileIO.DIRECTORY_NAME + "/" + "block.json");
			fw = new FileWriter(file, true);
			fw.append("]");
			fw.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void createDirectory() {
		File newDirectory = new File(FileIO.DIRECTORY_NAME);
		int directoryNumber = 0;
		while(true) {
			if (newDirectory.exists()){
				directoryNumber++;
				newDirectory = new File(FileIO.DIRECTORY_NAME + directoryNumber);
				continue;
			}
			if (newDirectory.mkdir()){
				if(directoryNumber != 0) {
					DIRECTORY_NAME += directoryNumber;
				}
			}
			break;
		}
	}
	
	static boolean firstBlock = false;
	public static void outBlockJson(Block block) {
		FileWriter fw = null;
		try {
			File file = new File("./" + FileIO.DIRECTORY_NAME + "/" + "block.json");
			fw = new FileWriter(file, true);
			
			if(firstBlock) {
				fw.append(",");
				fw.append(NEW_LINE);
			}
			fw.append("{");
			fw.append(NEW_LINE);
			fw.append("  \"hash\":\""+block.getHash()+"\",");
			fw.append(NEW_LINE);
			fw.append("  \"previousHash\":\""+ block.getPreviousHash() +"\",");
			fw.append(NEW_LINE);
			fw.append("  \"timestamp\":\""+ block.getTimestamp() +"\",");
			fw.append(NEW_LINE);
			fw.append("  \"receiveTime\":\""+ block.getReceiveBlockTime() +"\",");
			fw.append(NEW_LINE);
			fw.append("  \"height\":\""+ block.getHeight() + "\",");
			fw.append(NEW_LINE);
			fw.append("  \"miner\":\""+ block.getMiner().getName() +"\",");
			fw.append(NEW_LINE);
			fw.append("  \"reward\":\""+ Configuration.BLOCK_REWARD +"\"");
			fw.append(NEW_LINE);
			fw.append("}");
			fw.close();
			firstBlock = true;
		} catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	public static void outBlockJson(Blockchain blockchain) {
		FileWriter fw;
		try {
            fw = new FileWriter("./" + FileIO.DIRECTORY_NAME + "/" + FileIO.FILE_NAME1);
            ArrayList<Block> stack = new ArrayList<Block>();
			stack.add(blockchain.getGenesis());
			fw.append("[");
			while(stack.size()!=0) {
				Block block = stack.get(stack.size()-1);
				stack.remove(stack.size()-1);
				fw.append("{");
				fw.append(NEW_LINE);
				fw.append("  \"hash\":\""+block.getHash()+"\",");
				fw.append(NEW_LINE);
				fw.append("  \"previousHash\":\""+ block.getPreviousHash() +"\",");
				fw.append(NEW_LINE);
				fw.append("  \"timestamp\":\""+ block.getTimestamp() +"\",");
				fw.append(NEW_LINE);
				fw.append("  \"height\":\""+ block.getHeight() +"\",");
				fw.append(NEW_LINE);
				fw.append("  \"miner\":\""+ block.getMiner().getName() +"\"");
				fw.append(NEW_LINE);
				fw.append("}");
				for(Block NB : block.getNextBlocks()) {
					stack.add(NB);
				}
				if(stack.size()!=0) {
					fw.append(COMMA);
					fw.append(NEW_LINE);
				}
			}
			fw.append("]");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
		
	public static void OutAdjacencyMatrix() {
		int adjacencyMatrix[][] = Network.getAdjacencyMatrix();
		FileWriter fw;
		try {
			fw = new FileWriter("./" + FileIO.DIRECTORY_NAME + "/" + FileIO.FILE_NAME3);
			for(int i=0;i<adjacencyMatrix.length;i++) {
				for(int j=0;j<adjacencyMatrix.length;j++) {
					String strMatrix = String.valueOf(adjacencyMatrix[i][j]);
					fw.append(strMatrix);
					if(adjacencyMatrix[i].length-1!=j) {
						fw.append(COMMA);
					}
				}
				if(adjacencyMatrix.length-1 != i) {
					fw.append(NEW_LINE);
				}
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static boolean first_event = false;
	public static void outEventJson(Event event) {
		FileWriter fw = null;
		try {
			File file = new File("./" + FileIO.DIRECTORY_NAME + "/" + FileIO.FILE_NAME2);
			fw = new FileWriter(file, true);
			
			if(event.getEventType().equals("ReceiveBlock")) {
				ReceiveBlock receiveBlock = (ReceiveBlock)event;
				if(receiveBlock.getFrom()==receiveBlock.getNode()) {
					fw.close();	
					return;
				}
			}
			if(first_event) {
				fw.append(",");
				fw.append(NEW_LINE);
			}
			fw.append("{");
			fw.append(NEW_LINE);
			fw.append("  \"eventID\":\"" + event.getEventID() + "\",");
			fw.append(NEW_LINE);
			fw.append("  \"time\":\"" + event.getEventTime() + "\",");
			fw.append(NEW_LINE);
			fw.append("  \"type\":\"" + event.getEventType() + "\",");
			fw.append(NEW_LINE);
			fw.append("  \"node\":\"" + event.getNode().getName() + "\"");
			if(event.getEventType().equals("ReceiveBlock")) {
				ReceiveBlock receiveBlock = (ReceiveBlock)event;
				fw.append(",");
				fw.append(NEW_LINE);
				fw.append("  \"height\":\"" + receiveBlock.getBlock().getHeight() + "\",");
				fw.append(NEW_LINE);
				fw.append("  \"miner\":\"" + receiveBlock.getBlock().getMiner().getName() + "\",");
				fw.append(NEW_LINE);
				fw.append("  \"hash\":\"" + receiveBlock.getBlock().getHash() + "\",");
				fw.append(NEW_LINE);
				fw.append("  \"from\":\"" + receiveBlock.getFrom().getName() + "\"");
				fw.append(NEW_LINE);						
			}else if(event.getEventType().equals("FoundBlock")) {
				FoundBlock foundBlock = (FoundBlock)event;
				fw.append(",");
				fw.append(NEW_LINE);
				fw.append("  \"height\":\"" + foundBlock.getBlock().getHeight() + "\",");
				fw.append(NEW_LINE);
				fw.append("  \"miner\":\"" + foundBlock.getBlock().getMiner().getName() + "\",");
				fw.append(NEW_LINE);
				fw.append("  \"hash\":\"" + foundBlock.getBlock().getHash() + "\"");
				fw.append(NEW_LINE);
				
			}else {
				fw.append(NEW_LINE);
			}
			fw.append("}");		
			fw.close();	
			first_event = true;		
		} catch (IOException e) {
				e.printStackTrace();
		}
	}

	public static void deleteDirectory() {
		try {
            delete(DIRECTORY_NAME);
        }catch(Throwable th) {
            th.printStackTrace();
        }
	}
	
	public static void delete(String path) {
        File filePath = new File(path);
        String[] list = filePath.list();
        for(String file : list) {
            File f = new File(path + File.separator + file);
            if(f.isDirectory()) {
                delete(path + File.separator + file);
            }else {
                f.delete();
            }
        }
        filePath.delete();
    }
	
	public static void outAttackLog(String text) {
		FileWriter fw = null;
		try {
			File file = new File("./" + FileIO.DIRECTORY_NAME + "/" + FileIO.FILE_NAME4);
			fw = new FileWriter(file, true);
			fw.append(text);
			fw.append(NEW_LINE);
			
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
