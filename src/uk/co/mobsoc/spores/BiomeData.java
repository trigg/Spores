package uk.co.mobsoc.spores;

import java.util.ArrayList;

import org.bukkit.block.Biome;

public class BiomeData {
	public ArrayList<Biome> allowedBiomes = new ArrayList<Biome>();
	public boolean biomeAllowed(Biome biome){
		String s = "", sep="";
		for(Biome b: allowedBiomes){
			s=s+sep+b;
			sep=" ";
			if(b.equals(biome)){
				return true;
			}
		}
		System.out.println(biome+" -> "+s);
		return false;
	}
	public void setWhiteList(String names){
		String[] args = names.replace(" ", "").split(",");
		allowedBiomes.clear();
		for(String s : args){
			if(s.equals("")){ continue; }
			Biome b = Biome.valueOf(s.toUpperCase());
			allowedBiomes.add(b);
		}
	}
	public void setBlackList(String names){
		String[] args = names.replace(" ", "").split(",");
		allowedBiomes.clear();
		for(Biome b : Biome.values()){
			allowedBiomes.add(b);
		}
		for(String s : args){
			if(s.equals("")){ continue; }
			Biome b = Biome.valueOf(s.toUpperCase());
			allowedBiomes.remove(b);
		}
	}
	public static BiomeData createFrom(String whitelist, String blacklist) {
		BiomeData bd = new BiomeData();
		if(whitelist!=null && whitelist.length()>1){
			bd.setWhiteList(whitelist);
			return bd;
		}else{
			bd.setBlackList(blacklist);
			return bd;
		}
	}

}
