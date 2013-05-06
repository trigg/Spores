package uk.co.mobsoc.spores;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.getspout.spoutapi.Spout;
import org.getspout.spoutapi.block.SpoutChunk;
import org.getspout.spoutapi.material.CustomBlock;

public abstract class DelayedPopulator extends BlockPopulator {
	private ArrayList<BlockData> toDoBlocks = new ArrayList<BlockData>();
	/**
	 *  Position a block relative to this chunk. x= 0-15, z= 0-15 are inside this chunk, any calls to this where x or z are < 0 or >15 may result in this information being stored until the chunk exists.
	 * @param x
	 * @param y
	 * @param z
	 * @param m
	 * @param cb
	 */
	
	private void setBlockDelayed(Chunk c, BlockData bd) {
		long relativex = bd.x - (c.getX()*16);
		long relativez = bd.z - (c.getZ()*16);
		if(relativex<0 || relativex > 15 || relativez < 0 || relativez > 15){
			
			if(c.getWorld().loadChunk((int)(Math.floor(bd.x/16d)), (int)(Math.floor(bd.z/16d)), false)==false){
				// 	Chunk not currently loaded. Add it to our to-do list
				toDoBlocks.add(bd);
			}else{
				// Chunk exists previously. Newly spawned Trees/Structures need to be built into it. Pray the structure isn't too big ;P
				Chunk c2 = c.getWorld().getChunkAt((int)(Math.floor(bd.x/16d)), (int)(Math.floor(bd.z/16d)));
				setBlockNow(c2, bd.x,bd.y,bd.z,bd.id,bd.block);
			}
		}else{
			// This is the current chunk. Just do it!
			setBlockNow(c, bd.x,bd.y,bd.z,bd.id,bd.block);
		}

	}
	
	public void setBlockDelayed(Chunk c, int x, int y, int z, Material m, CustomBlock cb){
		setBlockDelayed(c,new BlockData(c.getWorld(), x,y,z,m,cb));
	}
	
	public void setBlockDelayedLocal(Chunk c, int x, int y, int z,
			Material m, CustomBlock cb) {
		setBlockDelayed(c, new BlockData(c.getWorld(), x+(c.getX()*16), y , z+(c.getZ()*16), m, cb));
		
	}
	
	public void setBlockNow(Chunk chunk, long x, long y, long z, int type, CustomBlock cb){
		SpoutChunk schunk = Spout.getServer().getWorld(chunk.getWorld().getName()).getChunkAt(chunk.getX(), chunk.getZ());
		Block b = chunk.getBlock((int)(x%16), (int)y, (int)(z%16));
		b.setTypeId(type);
		if(cb!=null){
			schunk.setCustomBlock((int)x, (int)b.getY(), (int)z, cb);
		}
	}

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		populate_from_list(world, random, chunk);
		populate_post(world, random, chunk);
	}
	
	private void populate_from_list(World world, Random random, Chunk chunk) {
		@SuppressWarnings("unchecked")
		ArrayList<BlockData> copyList = (ArrayList<BlockData>) toDoBlocks.clone();
		toDoBlocks.clear();
		for(BlockData bd : copyList){
			setBlockDelayed(chunk, bd);
		}
	}



	public abstract void populate_post(World world, Random random, Chunk chunk);
}
