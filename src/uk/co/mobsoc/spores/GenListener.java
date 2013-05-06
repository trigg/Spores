package uk.co.mobsoc.spores;

import org.bukkit.Bukkit;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
/**
 * Basic listener
 * @author triggerhapp
 *
 */
public class GenListener implements Listener{
	
	public GenListener(){
		Bukkit.getPluginManager().registerEvents(this, Spores.plugin);
	}
	
	@EventHandler
	public void onWorldInit(WorldInitEvent event){
		System.out.println(event.getWorld().getName());
		if(event.getWorld().getWorldType() == WorldType.NORMAL){
			event.getWorld().getPopulators().add(new OverworldOresPop());
			event.getWorld().getPopulators().add(new OverworldTreesPop());
			event.getWorld().getPopulators().add(new OverworldCropPop());
		}
	}
}
