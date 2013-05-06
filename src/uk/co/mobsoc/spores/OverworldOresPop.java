package uk.co.mobsoc.spores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.getspout.spoutapi.Spout;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutChunk;
import org.getspout.spoutapi.inventory.ItemMap;
import org.getspout.spoutapi.material.Block;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.MaterialData;

public class OverworldOresPop extends DelayedPopulator {
	ArrayList<BlockFace> bfList = new ArrayList(); 
	@SuppressWarnings("unchecked")
	@Override
	public void populate_post(World world, Random rand, Chunk chunk) {
		// Prepare ore connection directions. Could add diagonals, but I disagree with it personally :)
		if(bfList.size()<1){
			bfList.add(BlockFace.NORTH);
			bfList.add(BlockFace.SOUTH);
			bfList.add(BlockFace.EAST);
			bfList.add(BlockFace.WEST);
			bfList.add(BlockFace.UP);
			bfList.add(BlockFace.DOWN);
		}
		// Take a copy of List of ores. Synchronize the list incase it's being edited in another thread (is wgen threaded yet? I doubt it)
		// OreData is all primitives minus od.block... which should only change in this thread anyway.
		ArrayList<OreData> copy;
		synchronized(OreData.ores){
			copy = (ArrayList<OreData>) OreData.ores.clone();
		}
		for(OreData od : copy){
			if(od.block==null){
				// Set block if not used before
				od.block=MaterialData.getCustomBlock(od.blockName);
			}
			if(od.block==null){
				// Block isn't in any of the plugins we added...
				if(!od.hasWarned){
					System.out.println("Cannot find material : "+od.blockName);
					od.hasWarned=true;
				}
				return;
			}
			SpoutChunk schunk = Spout.getServer().getWorld(world.getName()).getChunkAt(chunk.getX(), chunk.getZ());
			for(int veins = 0; veins < od.maxveins; veins++){
				if(veins < od.minveins || rand.nextInt(100) <= od.chance){
					int ores = od.minore + rand.nextInt(od.maxore - od.minore);
					ArrayList<org.bukkit.block.Block> randomVein = getStoneStartPoint(chunk, rand, od.miny, od.maxy, ores);
					System.out.println("Spawning "+od.blockName+" sized "+randomVein.size());
					for(org.bukkit.block.Block b : randomVein){
						int x = b.getX() - (schunk.getX()*16);
						int z = b.getZ() - (schunk.getZ()*16);
						// TODO : Assumption of showing them as iron ore to vanilla. Needs fix
						chunk.getBlock(x,b.getY(),z).setTypeId(35);
						schunk.setCustomBlock(x, b.getY(), z,od.block);
					}
				}
			}
//		for(int x = 0; x< 16; x++){
//			for(int z = 0; z < 16; z++){
				////world.getBlockAt((chunk.getX()*16)+x,80,(chunk.getZ()*16)+z).setTypeId(id);
//				chunk.getBlock(x, 80, z).setTypeId(1);
//				schunk.setCustomBlock(x, 80, z, block);
//			}
		}
	}
	
	private ArrayList<org.bukkit.block.Block> getStoneStartPoint(Chunk c, Random rand, int miny, int maxy, int ores) {
		// TODO : Checking against stone may be causing chunks next door to be spawned. re-write to either not check stone and use setBlockDelayed OR not to stray from 0-15 x and z
		int tries = 0;
		ArrayList<org.bukkit.block.Block> possibles = new ArrayList<org.bukkit.block.Block>();
		ArrayList<org.bukkit.block.Block> vein = new ArrayList<org.bukkit.block.Block>();
		while(tries < 1000){
			int x = rand.nextInt(16);
			int z = rand.nextInt(16);
			int y = miny+rand.nextInt(maxy-miny);
			int tries2=0;
			if(c.getBlock(x, y, z).getType() == Material.STONE){		
				possibles.add(c.getBlock(x, y, z));
				while(tries2<1000 && possibles.size()>0 && vein.size()<ores){
					org.bukkit.block.Block b = possibles.get(0);
					possibles.remove(0);
					if(b.getType()==Material.STONE){
						for(BlockFace bf : shuffle(bfList)){
							possibles.add(b.getRelative(bf));
						}
						vein.add(b);
					}

				}
				return vein;
			}
			tries ++;
		}

		return vein;
	}

	private ArrayList<BlockFace> shuffle(ArrayList<BlockFace> bfList2) {
		@SuppressWarnings("unchecked")
		ArrayList<BlockFace> ret = (ArrayList<BlockFace>) bfList2.clone();
		Collections.shuffle(ret);
		return ret;
	}

}


