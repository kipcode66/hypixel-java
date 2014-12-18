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

public class Guild extends HypixelObject {
	
	protected MemberList members;

	public Guild(JsonObject array, HypixelAPI api) {
		super(array, api);
	}
	
	public String getName(){
		JsonElement elem = this.get("name", true);
		return (!elem.isJsonNull())? elem.getAsString(): null;
	}
	
	public boolean canTag(){
		JsonElement elem = this.get("canTag", true);
		return (!elem.isJsonNull())? elem.getAsBoolean(): false;
	}
	
	public String getTag(){
		JsonElement elem = this.get("tag", true);
		return (!elem.isJsonNull())? elem.getAsString(): null;
	}
	
	public int getCoins(){
		JsonElement elem = this.get("coins", true);
		return (!elem.isJsonNull())? elem.getAsInt(): -1;
	}
	
	public MemberList getMemberList(){
		if(this.members != null)
			return this.members;
		if(this.JSONArray != null && !this.JSONArray.isJsonNull()) this.members = new MemberList(this.JSONArray.getAsJsonObject().getAsJsonObject("members"), this.api);
		return this.JSONArray != null && !this.JSONArray.isJsonNull()?this.getMemberList(): new MemberList(new JsonArray(), api);
	}
	
	public int getMemberCount() {
		if(this.members == null && this.JSONArray != null && !this.JSONArray.isJsonNull())
			this.members = new MemberList(this.JSONArray.getAsJsonObject().getAsJsonArray("members"), this.api);
		return this.JSONArray != null && !this.JSONArray.isJsonNull()? this.members.getMemberCount() : 0;
	}
	
	public int getMaxMembers(){
		int[] upgrades = new int[]{0,5,5,5,5,5,5,5,5,5,10,5,5,5,5,5,5,5,5,5,10};
		int base = 25, total = 0;
		JsonObject tmp = new JsonObject();
		tmp.addProperty("int", 0);
		for(int i = 0; i < this.get("memberSizeLevel", true, tmp.get("int")).getAsInt(); i++) {
            total += upgrades[i];
        }
        total += base;
        return total;
	}
}
