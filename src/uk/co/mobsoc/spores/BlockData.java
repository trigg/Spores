package uk.co.mobsoc.spores;

import org.bukkit.Material;
import org.bukkit.World;
import org.getspout.spoutapi.material.CustomBlock;

public class BlockData {
	public BlockData(World world, int x, int y, int z, Material m,
			CustomBlock cb) {
		worldName = world.getName();
		this.x=x; this.y=y; this.z=z;
		id = m.getId();
		block = cb;
	}
	String worldName;
	long x,y,z;
	int id;
	CustomBlock block;
}
