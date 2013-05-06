package uk.co.mobsoc.spores;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.getspout.spoutapi.Spout;
import org.getspout.spoutapi.block.SpoutChunk;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.MaterialData;
/**
 * Block Populator which, if Sprout is also running, changed villagers wheat farms into a multi-crop farm
 * @author triggerhapp
 *
 */
public class OverworldCropPop extends DelayedPopulator {

	public void populate_post(World world, Random rand, Chunk chunk) {
		if(SproutLoader.knownPlants.size()==0){ return; }
		for(int x = 0; x<16;x++){
			for(int z=0;z<16;z++){
				for(int y = 64; y<80; y++){
					if(chunk.getBlock(x, y, z).getType()==Material.CROPS){
						if(rand.nextInt(10)<7){
							String name = SproutLoader.knownPlants.get(rand.nextInt(SproutLoader.knownPlants.size()));
							CustomBlock block = MaterialData.getCustomBlock(name);
							// TODO : Assumption of Material.Crops. Needs fix. Also probably won't grow this way?
							setBlockDelayedLocal(chunk, x, y ,z,Material.CROPS, block);
							
						}
					}
				}
			}
		}
	}

}
