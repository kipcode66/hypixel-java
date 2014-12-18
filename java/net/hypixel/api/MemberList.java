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

import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonElement;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;

public class MemberList extends HypixelObject {
	
	protected JsonObject list;
	protected int count = 0;

	public MemberList(JsonElement array, HypixelAPI api) {
		super(array, api);
		JsonObject tmp_list = new JsonObject();
		tmp_list.add("GUILDMASTER", new JsonArray());
		tmp_list.add("OFFICER", new JsonArray());
		tmp_list.add("MEMBER", new JsonArray());
		count = array.getAsJsonArray().size();
		JsonArray arr = array.getAsJsonArray();
		for(JsonElement el : arr){
			String rank = el.getAsJsonObject().get("rank").getAsString();
			if(!tmp_list.has(rank)){
				tmp_list.add(rank, new JsonArray());
			}
			tmp_list.getAsJsonArray(rank).add(el);
		}
		list = tmp_list;
	}
	
	/**
	 * 
	 * @return a <code>JsonObject</code> with the format : <br>
	 * <pre>
	 * {
	 * 	"GUILDMASTER" : [{"name":"player name"}, ...],
	 * 	"OFFICER" : [{"name":"player name"}, ...],
	 * 	"MEMBER" : [{"name":"player name"}, ...]
	 * }
	 * </pre>
	 */
	public JsonObject getList(){
		return this.list;
	}
	
	public int getMemberCount(){
		return this.count;
	}
}
