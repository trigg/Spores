package uk.co.mobsoc.spores;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.getspout.spoutapi.material.CustomBlock;

import uk.co.mobsoc.spores.TreeData.Style;
/**
 * Configuration information about logs and leaves from different plugins, including info on how to spawn it in any world
 * @author triggerhapp
 *
 */
public class TreeData {
	public enum Style{
		SURFACE, CAVERN, NETHER
	};
	public static ArrayList<TreeData> trees = new ArrayList<TreeData>();
	public Style style;
	public BiomeData biomeData;

	public static void readFromConfig(ConfigurationSection conf) {
		for(String section : conf.getKeys(false)){
			System.out.println("Loading Tree : "+section);
			TreeData td = new TreeData();
			td.blockLeafName = conf.getString(section+".leafmaterial").replace("/", ".");
			td.blockLogName = conf.getString(section+".logmaterial").replace("/", ".");
			td.biomeData = BiomeData.createFrom(conf.getString(section+".biomewhiteist",""),conf.getString(section+".biomeblacklist",""));
			td.maxtrees = conf.getInt(section+".maximumTreesPerChunk");
			td.mintrees = conf.getInt(section+".minimumTreesPerChunk");
			td.chance = conf.getInt(section+".treeSpawnChance");

			synchronized(trees){
				trees.add(td);
			}
		}
	}

	public CustomBlock blockLog;
	public String blockLogName;
	public CustomBlock blockLeaf;
	public String blockLeafName;
	public boolean hasWarned;
	public int mintrees, maxtrees, chance;

	public static void makeDefaultConfig(FileConfiguration conf,
			String logName) {
		logName = logName.replace(".", "/");
		if(conf.getConfigurationSection("trees."+logName)==null){
			conf.set("trees."+logName+".logmaterial", logName);
			conf.set("trees."+logName+".logmaterial", "leafMaterialHere");

			conf.set("trees."+logName+".maximumTreesPerChunk",0);
			conf.set("trees."+logName+".minimumTreesPerChunk",0);
			conf.set("trees."+logName+".treeSpawnChance",0);
			conf.set("trees."+logName+".biomeblacklist","hell,ocean,mushroom_island,mushroom_shore");
			conf.set("trees."+logName+".biomewhitelist","forest_hills,forest,extreme_hills");
		}
	}

}
