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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonElement;

public class Session extends HypixelObject {

	public Session(JsonElement array, HypixelAPI api) {
		super(array, api);
	}
	
	public List<String> getPlayers(){
		List<String> players = new ArrayList<String>();
		JsonElement tmp_ret = this.get("players", true);
		if(tmp_ret != null && !tmp_ret.isJsonNull()){
			JsonArray ret = tmp_ret.getAsJsonArray();
			for(JsonElement el : ret){
				players.add(el.getAsString());
			}
		}
		return players;
	}
	
	public String getGame(){
		JsonElement obj = this.get("gameType",true);
		return (obj != null && !obj.isJsonNull())?obj.getAsString():"";
	}
	
	public String getServer(){
		JsonElement obj = this.get("server",true);
		return (obj != null && !obj.isJsonNull())?obj.getAsString():"";
	}
}
