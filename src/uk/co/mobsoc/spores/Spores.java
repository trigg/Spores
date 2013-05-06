package uk.co.mobsoc.spores;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.MaterialData;

import com.almuradev.sprout.api.crop.Sprout;
import com.almuradev.sprout.api.io.SproutRegistry;
import com.almuradev.sprout.plugin.SproutPlugin;
/**
 * Spores Plugin
 * @author triggerhapp
 *
 */
public class Spores extends JavaPlugin{
	public static Spores plugin;
	private ChatColor b=ChatColor.BLUE,g=ChatColor.GREEN;
	private String options = b+"["+g+"generate"+b+"]";

	public void onEnable(){
		plugin = this;
		this.saveDefaultConfig();
		OreData.readFromConfig(getConfig().getConfigurationSection("ores"));
		TreeData.readFromConfig(getConfig().getConfigurationSection("trees"));
		new SproutLoader();
		new GenListener();
	}
	
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	Player player = null;
    	if(sender instanceof Player){
    		player = (Player) sender;
    	}
     	if(args.length==0){
    		sender.sendMessage("/spores "+options);
    		return true;
    	}else{
    		if(args[0].equalsIgnoreCase("generate")){
    			for(CustomBlock bd : MaterialData.getCustomBlocks()){
    				String[] sections = bd.getFullName().replace(".","/").split("/");
    				if(sections.length<=1){ System.out.println("Skipping : "+bd.getFullName()); continue; }
    				String lS = sections[sections.length-1];
    				if(lS.toLowerCase().indexOf("ore")>=0){
    					OreData.makeDefaultConfig(getConfig(), bd.getFullName());
    				}else if(lS.toLowerCase().indexOf("log")>=0){
    					// Attempt to magically get Leaves for log
    					TreeData.makeDefaultConfig(getConfig(), bd.getFullName());
    				}else if(lS.toLowerCase().indexOf("leaf")>=0 || lS.toLowerCase().indexOf("leave")>=0){
    					getConfig().set("notconfig.knownleaves."+bd.getFullName().replace(".", "/"), true);
    				}
    			}
    			saveConfig();
    			return true;
    		}
    		
    		sender.sendMessage("/spores "+options);
    		return true;
    	}
    }
}
