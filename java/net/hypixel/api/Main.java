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
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonElement;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public Logger log = Logger.getLogger("Minecraft");
	public HypixelAPI api;
	
	@Override
	public void onEnable(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("api_key","1123cb7c-e926-4b1f-8216-c8be1fb0d458");
		api = new HypixelAPI(this, map);
		
		log.info(getConfig().getName()+" Enabled.");
	}
	
	@Override
	public void onDisable(){
		
		log.info(getConfig().getName()+" Disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args){
		if(cmd.getLabel().equalsIgnoreCase("testhp")){
			if(args.length >= 2 && args[0].equalsIgnoreCase("guild")){
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("byPlayer", args[1]);
				Guild g = api.getGuild(map2);
				sender.sendMessage("-=-=-=-=-= "+g.getName()+" =-=-=-=-=-");
				sender.sendMessage("Coins: "+g.getCoins());
				//JsonElement id = g.get("uuid", true);
				//sender.sendMessage("Guild id : "+((id != null && !id.isJsonNull())?id.getAsString():null));
				sender.sendMessage("tag : "+g.getTag());
				sender.sendMessage("members "+g.getMemberCount()+"/"+g.getMaxMembers());
				JsonObject members = g.getMemberList().getList().getAsJsonObject();
				for(Entry<String,JsonElement> rank : members.entrySet()){
					sender.sendMessage(rank.getKey());
					for(JsonElement member :rank.getValue().getAsJsonArray()){
						sender.sendMessage("\t"+member.getAsJsonObject().get("name").getAsString());
					}
				}
			}
			if(args.length >= 2 && args[0].equalsIgnoreCase("player")){
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("name", args[1]);
				Player p = api.getPlayer(map2);
				sender.sendMessage("Name : "+p.getName());
				sender.sendMessage("UUID : "+p.getUUID());
				sender.sendMessage("Level : "+p.getLevel());
				sender.sendMessage("Rank : "+p.getRank(false));
				sender.sendMessage("Multiplier : "+p.getMultiplier());
			}
			if(args.length >= 2 && args[0].equalsIgnoreCase("session")){
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("player", args[1]);
				Session ses = api.getSession(map2);
				sender.sendMessage("Game : "+ses.getGame());
				sender.sendMessage("Server : "+ses.getServer());
				for(String player :ses.getPlayers()){
					sender.sendMessage("\t"+player);
				}
			}
			if(args.length <= 1){
				return false;
			}
		}
		return true;
	}
}
