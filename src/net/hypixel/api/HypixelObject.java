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

import org.bukkit.craftbukkit.libs.com.google.gson.JsonElement;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonNull;

public class HypixelObject {
	protected JsonElement JSONArray = null;
	protected HypixelAPI api;
	
	public HypixelObject(JsonElement array, HypixelAPI api){
		this.JSONArray = array;
		this.api = api;
	}
	
	public boolean isNull(){
		if(this.getRaw() != null && this.getRaw().isJsonNull()) return true;
		return this.getRaw() == null;
	}
	public JsonElement getRaw(){
		return this.JSONArray;
	}
	public JsonElement get(String key, boolean implicit, JsonElement defaultReturn){
		if(isNull()) return defaultReturn;
		if(!implicit){
			JsonElement ret = this.JSONArray;
			for(String split : key.split(".")){
				if(!ret.isJsonNull() && ret.isJsonObject() && ret.getAsJsonObject().has(split)){
					ret = ret.getAsJsonObject().get(split);
				}else if(!ret.isJsonNull() && ret.isJsonArray() && ret.getAsJsonArray().size() > Integer.valueOf(split)){
					ret = ret.getAsJsonArray().get(Integer.valueOf(split));
				}else{
					return defaultReturn;
				}
			}
			return !ret.isJsonNull() ? ret: defaultReturn;
		}
		return JSONArray.isJsonArray()? defaultReturn:JSONArray.getAsJsonObject().has(key) ? JSONArray.getAsJsonObject().get(key): defaultReturn;
	}
	@SuppressWarnings("deprecation")
	public JsonElement get(String key, boolean implicit){
		return get(key, implicit, new JsonNull());
	}
	@SuppressWarnings("deprecation")
	public JsonElement get(String key){
		return get(key, false, new JsonNull());
	}
	public String getId(){
		JsonElement elem = this.get("_id", true);
        return (elem != null)?elem.getAsString(): null;
    }
}
