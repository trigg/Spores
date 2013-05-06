package uk.co.mobsoc.spores;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.getspout.spoutapi.material.CustomBlock;
/**
 * Configuration information about ores from different plugins, including info on how to spawn it in any world
 * @author triggerhapp
 *
 */
public class OreData {
	public static ArrayList<OreData> ores = new ArrayList<OreData>();
	String blockName;
	CustomBlock block=null;
	int maxy, miny;
	int maxore, minore;
	int chance;
	int maxveins, minveins;
	boolean hasWarned=false;
	public BiomeData biomeData;
	public static void readFromConfig(ConfigurationSection conf) {
		for(String section : conf.getKeys(false)){
			System.out.println("Loading Ore : "+section);
			OreData od = new OreData();
			od.blockName = conf.getString(section+".material").replace("/", ".");
			od.maxy = conf.getInt(section+".maximumHeight");
			od.miny = conf.getInt(section+".minimumHeight");
			od.maxveins = conf.getInt(section+".maximumVeinsPerChunk");
			od.minveins = conf.getInt(section+".minimumVeinsPerChunk");
			od.chance = conf.getInt(section+".veinSpawnChance");
			od.maxore = conf.getInt(section+".maximumOresPerVein");
			od.minore = conf.getInt(section+".minimumOresPerVein");
			od.biomeData = BiomeData.createFrom(conf.getString(section+".biomewhiteist",""),conf.getString(section+".biomeblacklist",""));
			synchronized(ores){
				ores.add(od);
			}
		}
	}
	
	public static void makeDefaultConfig(ConfigurationSection conf, String fullName){
		fullName = fullName.replace(".", "/");
		if(conf.getConfigurationSection("ores."+fullName)==null){
			conf.set("ores."+fullName+".material", fullName);
			conf.set("ores."+fullName+".maximumHeight",0);
			conf.set("ores."+fullName+".minimumHeight",0);
			conf.set("ores."+fullName+".maximumVeinsPerChunk",0);
			conf.set("ores."+fullName+".minimumVeinsPerChunk",0);
			conf.set("ores."+fullName+".veinSpawnChance",0);
			conf.set("ores."+fullName+".maximumOresPerVein",0);
			conf.set("ores."+fullName+".minimumOresPerVein",0);
			conf.set("ores."+fullName+".biomeblacklist","hell,ocean,mushroom_island,mushroom_shore");
			conf.set("ores."+fullName+".biomewhitelist","forest_hills,forest,extreme_hills");
		}
	}
}
