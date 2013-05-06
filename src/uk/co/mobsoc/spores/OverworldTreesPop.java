package uk.co.mobsoc.spores;

import java.util.ArrayList;
import java.util.Random;


import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.MaterialData;

public class OverworldTreesPop extends DelayedPopulator {

	@SuppressWarnings("unchecked")
	@Override
	public void populate_post(World world, Random rand, Chunk chunk) {
		ArrayList<TreeData> copy;
		synchronized(TreeData.trees){
			copy = (ArrayList<TreeData>) TreeData.trees.clone();
		}
		for(TreeData td : copy){
			if(td.blockLog==null){
				// Set block if not used before
				td.blockLog=MaterialData.getCustomBlock(td.blockLogName);
			}
			if(td.blockLeaf==null){
				// Set block if not used before
				td.blockLeaf=MaterialData.getCustomBlock(td.blockLeafName);
			}
			if(td.blockLeaf==null || td.blockLog == null){
				// Block isn't in any of the plugins we added...
				if(!td.hasWarned){
					System.out.println("Cannot find materials : "+td.blockLogName+" & "+td.blockLeafName);
					td.hasWarned=true;
				}
				return;
			}
			Biome b = chunk.getBlock(7, 64, 7).getBiome();
			// Bad assumption, but it might be worse checking every single block of the chunk!
			if(!td.biomeData.biomeAllowed(b)){ continue; }

			td.style = TreeData.Style.SURFACE;
			for(int trees=0; trees < td.maxtrees; trees++){
				if(trees < td.mintrees || rand.nextInt(100) < td.chance){
					
					int x = rand.nextInt(15);
					int z = rand.nextInt(15);
					if(td.style == TreeData.Style.SURFACE){
						int y = getSurfaceTreeStart(chunk, x, z);
						if(y>0 && chunk.getBlock(x,y-1,z).getType()==Material.GRASS){
							makeTree(chunk, x,y,z,td.blockLog, td.blockLeaf);
						}
					}else if(td.style == TreeData.Style.CAVERN){
						
					}else if(td.style == TreeData.Style.NETHER){
						
					}else{
						System.out.println("Unknown Style");
					}
				}
			}
		}
	}
	
	private void makeTree(Chunk c,int x, int y, int z, CustomBlock blockLog,
			CustomBlock blockLeaf) {
		// TODO : Randomised... or at least interesting.
		System.out.println("New tree at "+x+" "+y+" "+z);
		for(int canopyy = 0; canopyy<3; canopyy++){
			for(int canopyx = -4+canopyy ; canopyx <(4-canopyy); canopyx ++){
				for(int canopyz = -4+canopyy ; canopyz <(4-canopyy); canopyz ++){
					setBlockDelayedLocal(c, x+canopyx, y+6+canopyy, z+canopyz, Material.LOG, blockLeaf);
					setBlockDelayedLocal(c, x+canopyx, y+6-canopyy, z+canopyz, Material.LOG, blockLeaf);
				}
			}
		}
		for(int trunk = 0; trunk < 7; trunk ++){
			setBlockDelayedLocal(c, x, y+trunk, z, Material.LOG, blockLog);
		}
	}

	public int getSurfaceTreeStart(Chunk chunk, int x, int z){
		for(int y = chunk.getWorld().getMaxHeight()-1; y>60; y--){
			Material t = chunk.getBlock(x, y, z).getType();
			if(!isAboveGroundBlock(t)){
				return y+1;
			}
		}
		return -1;
	}
	
	public boolean isAboveGroundBlock(Material t){
		return (t==Material.AIR || t==Material.DEAD_BUSH || t==Material.LONG_GRASS ||
				t==Material.RED_ROSE || t==Material.YELLOW_FLOWER || t==Material.BROWN_MUSHROOM||
				t==Material.RED_MUSHROOM);
	}

}
