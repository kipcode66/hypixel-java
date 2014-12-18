/*
 * HypixelAPI plugin for bukkit/spigot minecraft servers inpired by 
 * Plancke's HypixelPHP project <https://github.com/Plancke/hypixel-php>.
 * Copyright (C) 2014  kipcode66
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.hypixel.api;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonElement;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonPrimitive;

public class Player extends HypixelObject {

	public Player(JsonElement array, HypixelAPI api) {
		super(array, api);
	}
	
	public Session getSession(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("player", this.getName());
		return api.getSession(map);
	}
	
	public Friends getFriends(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("player", this.getName());
		return api.getFriends(map);
	}
	
	public String getName(){
		if(!this.get("displayname",true).isJsonNull()){
			return this.get("displayname",true).getAsString();
		}
		else
		{
			JsonArray aliases = this.get("knownAliases", true, new JsonArray()).getAsJsonArray();
			if(aliases.size() == 0){
				return this.get("playername", true).getAsString();
			}
			return aliases.get(0).getAsString();
		}
	}
	
	public String getUUID(){
		JsonElement obj = this.get("uuid",true);
		return (!obj.isJsonNull())?obj.getAsString():null;
	}
	
	public Stats getStats(){
		return new Stats(this.get("stats",true,new JsonArray()), this.api);
	}
	
	public boolean isPreEULA(){
		return this.get("eulaCoins",true,new JsonPrimitive(false)).getAsBoolean();
	}
	
	public int getLevel(){
		return this.get("networkLevel", true, new JsonPrimitive(0)).getAsInt()+1;
	}
	
	public boolean isStaff(){
		JsonElement tmp = this.get("rank", true);
		if(tmp.isJsonNull())
			return false;
		String rank = tmp.getAsString();
		if(rank == "NORMAL" || rank == "null")
			return false;
		return true;
	}
	
	public int getMultiplier(){
		if(this.getRank(false) == "YOUTUBER") return 7;
		String[] ranks = new String[]{"DEFAULT", "VIP", "VIP+", "MVP", "MVP+"};
		String pre = this.getRank(true, true);
		Map<String, Integer> flip = Util.array_flip(ranks);
		int rankKey = flip.get(pre)!= null?flip.get(pre) : 0;
		int levelKey = (int) (Math.floor(this.getLevel()/25) + 1);
		return (rankKey > levelKey) ? rankKey : levelKey;
	}
	
	public String getRank(boolean pkg, boolean preEULA) {
		if(pkg){
			String[] keys = new String[]{"newPackageRank", "packageRank"};
			if(preEULA) keys = Util.array_reverse(keys);
			if(!this.isStaff()){
				if(!this.isPreEULA()){
					JsonElement tmp = this.get(keys[0],true);
					if(!tmp.isJsonNull()){
						return tmp.getAsString().replace("_PLUS", "+");
					}
				}
				else
				{
					for(String key : keys){
						JsonElement tmp = this.get(key, true);
						if(!tmp.isJsonNull()){
							return tmp.getAsString().replace("_PLUS", "+");
						}
					}
				}
			}
			else
			{
				for(String key : keys){
					JsonElement tmp = this.get(key, true);
					if(!tmp.isJsonNull()){
						return tmp.getAsString().replace("_PLUS", "+");
					}
				}
			}
		}else{
			JsonElement tmp = this.get("rank", true);
			String rank = (!tmp.isJsonNull())? tmp.getAsString():""+tmp.toString();
			if(!this.isStaff()) return this.getRank(true, preEULA);
			return rank;
		}
		return "DEFAULT";
	}

	public String getRank(boolean pkg) {
		return this.getRank(pkg, false);
	}

	public String getRank(){
		return this.getRank(true, false);
	}
	
	//TODO Achievements ...
}
