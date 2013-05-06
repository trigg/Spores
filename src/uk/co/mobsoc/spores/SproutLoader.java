package uk.co.mobsoc.spores;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.almuradev.sprout.api.crop.Sprout;
import com.almuradev.sprout.api.io.SproutRegistry;
import com.almuradev.sprout.plugin.SproutPlugin;
/*
 * Since softdep seems to be ignored when you set plugin.yml/load:startup, load in sprout info after the first tick. 
 * On a down side, all villages spawned before first tick (near spawn area) will have wheat-only, and the new blocks will take effect further out
 */
public class SproutLoader implements Runnable{
	public static ArrayList<String> knownPlants = new ArrayList<String>(); 
	public SproutLoader(){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Spores.plugin, this, 1);
	}

	@Override
	public void run() {
		Plugin p = Bukkit.getPluginManager().getPlugin("Sprout");
		if(p!=null){
			SproutPlugin s = (SproutPlugin) p; 
			SproutRegistry reg = s.getSproutRegistry();
			for(Sprout sprout : reg.getAll()){
				knownPlants.add(sprout.getBlockSource());
			}
		}
	}

}
