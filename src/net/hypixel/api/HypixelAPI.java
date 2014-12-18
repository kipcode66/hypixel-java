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

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonElement;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;

public class HypixelAPI {
	
	static final String PATH_SEPARATOR = System.getProperty("file.separator");
	static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static int int MAX_CACHE_TIME = 999999999999;
	private Map<String,String> options = new HashMap<String,String>();
	
	public HypixelAPI(Plugin plugin, Map<String, String> input){
		File dataFolder = plugin.getDataFolder();
		options.put("api_key", "");
		options.put("cache_time", "600");
		options.put("cache_folder_player", dataFolder.getAbsolutePath()+PATH_SEPARATOR+"cache"+PATH_SEPARATOR+"HypixelAPI"+PATH_SEPARATOR+"player"+PATH_SEPARATOR);
		options.put("cache_uuid_table", "uuid_table.json");
		options.put("cache_folder_guild", dataFolder.getAbsolutePath()+PATH_SEPARATOR+"cache"+PATH_SEPARATOR+"HypixelAPI"+PATH_SEPARATOR+"guild"+PATH_SEPARATOR);
		options.put("cache_byPlayer_table", "byPlayer_table.json");
		options.put("cache_byName_table", "byName_table.json");
		options.put("cache_folder_friends", dataFolder.getAbsolutePath()+PATH_SEPARATOR+"cache"+PATH_SEPARATOR+"HypixelAPI"+PATH_SEPARATOR+"friends"+PATH_SEPARATOR);
		options.put("cache_folder_sessions", dataFolder.getAbsolutePath()+PATH_SEPARATOR+"cache"+PATH_SEPARATOR+"HypixelAPI"+PATH_SEPARATOR+"sessions"+PATH_SEPARATOR);
		options.
		options.put("version", "1.1");
		
		for(String key : input.keySet()){
			if(options.keySet().contains(key)){
				options.put(key, input.get(key));
			}
		}
		
		init(new File(options.get("cache_folder_player")), 
				new File(options.get("cache_folder_guild")),
				new File(options.get("cache_folder_friends")),
				new File(options.get("cache_folder_sessions")));
	}
	
	private void init(File cache_folder_player,	File cache_folder_guild, 
			File cache_folder_friends, File cache_folder_sessions){
		
		if(!cache_folder_player.exists()){
			cache_folder_player.mkdirs();
			cache_folder_player.setReadable(true);
			cache_folder_player.setWritable(true);
			cache_folder_player.setExecutable(true);
		}
		if(!cache_folder_guild.exists()){
			cache_folder_guild.mkdirs();
			cache_folder_guild.setReadable(true);
			cache_folder_guild.setWritable(true);
			cache_folder_guild.setExecutable(true);
		}
		if(!cache_folder_friends.exists()){
			cache_folder_friends.mkdirs();
			cache_folder_friends.setReadable(true);
			cache_folder_friends.setWritable(true);
			cache_folder_friends.setExecutable(true);
		}
		if(!cache_folder_sessions.exists()){
			cache_folder_sessions.mkdirs();
			cache_folder_sessions.setReadable(true);
			cache_folder_sessions.setWritable(true);
			cache_folder_sessions.setExecutable(true);
		}
	}
	
	public void set(Map<String, String> input){
		for(String key : input.keySet()){
			if(options.keySet().contains(key)){
				options.put(key, input.get(key));
			}
		}
	}
	
	public String getVersion(){
		return options.get("version");
	}
	
	public void setKey(String key){
		options.put("api_key", key);
	}
	
	public String getKey(){
		return options.get("api_key");
	}
	
	public Map<String, String> getOptions(){
		return options;
	}
	
	public JsonObject fetch(String request, String key, String val){
		String ret = "{}";
		try {
			ret = RequestManager.fileGetContents("https://api.hypixel.net/" + request + "?key=" + this.getKey() + "&" + key + "=" + val);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new JsonParser().parse(ret).getAsJsonObject();
		return new JsonParser().parse(ret).getAsJsonObject();
	}
	
	public Player getPlayer(Map<String,String> keypair){
		Map<String,String> pairs = new HashMap<String,String>();
		pairs.put("name", "");
		pairs.put("uuid", "");
		
		for(String key : keypair.keySet()){
			if(pairs.keySet().contains(key)){
				pairs.put(key, keypair.get(key));
			}
		}
		
		for(String key : pairs.keySet()){ String val = pairs.get(key);
			val = val.toLowerCase();
			if(val != ""){
				if(key == "uuid"){
					String filename = options.get("cache_folder_player")+options.get("cache_uuid_table");
					JsonObject content = new JsonParser().parse(getContent(filename)).getAsJsonObject();
					if(content.has(val)){
						JsonObject tmp_obj = content.get(val).getAsJsonObject();
						if(new Date().getTime() - Integer.valueOf(options.get("cache_time")) < tmp_obj.get("timestamp").getAsLong()){
							Map<String, String> k = new HashMap<String, String>();
							k.put("name", tmp_obj.get("name").getAsString());
							getPlayer(k);
						}
					}
					JsonObject response = fetch("player", key, val);
					if(response.get("success").getAsBoolean() && !response.get("player").isJsonNull()){
						JsonObject tmp_arr = new JsonObject();
						tmp_arr.addProperty("timestamp", new Date().getTime());
						tmp_arr.addProperty("name", response.getAsJsonObject().get("player").getAsJsonObject().get("displayname").getAsString());
						content.add(val, tmp_arr);
						setContent(filename, content.toString());
						return new Player(response.getAsJsonObject("player"),this);
					}
				}
				if(key == "name"){
					String filename = this.options.get("cache_folder_player")+key+PATH_SEPARATOR+this.getCacheFileName(val)+".json";
					File file = new File(filename);
					if(file.exists()){
						if(new Date().getTime() - Integer.valueOf(options.get("cache_time")) < file.lastModified()){
							JsonObject content = new JsonParser().parse(getContent(filename)).getAsJsonObject();
							return new Player(content, this);
						}
					} else {
						file.getParentFile().mkdirs();
					}
					JsonObject response = fetch("player", key, val);
					if(response.get("success").getAsBoolean() && !response.get("player").isJsonNull()){
						setContent(filename, response.getAsJsonObject("player").toString());
						return new Player(response.getAsJsonObject("player"), this);
					}
				}
			}
		}
		
		return new Player(null, this);
	}
	
	public Guild getGuild(Map<String,String> keypair){
		Map<String,String> pairs = new HashMap<String,String>();
		pairs.put("byName", "");
		pairs.put("byPlayer", "");
		pairs.put("id", "");
		
		for(String key : keypair.keySet()){
			if(pairs.keySet().contains(key)){
				pairs.put(key, keypair.get(key));
			}
		}
		
		for(String key : pairs.keySet()){ String val = pairs.get(key);
			if(val != ""){
				val = val.replaceAll(" ", "%20");
				if(key == "byPlayer" || key == "byName"){
					String filename = options.get("cache_folder_guild") + options.get("cache_"+key+"_table");
					JsonElement tmp_cont = new JsonParser().parse(getContent(filename));
					JsonObject content = (tmp_cont != null && !tmp_cont.isJsonNull())?tmp_cont.getAsJsonObject(): new JsonObject();
					if(content.has(val)){
						JsonObject tmp_obj = content.get(val).getAsJsonObject();
						if(new Date().getTime() - Integer.valueOf(options.get("cache_time")) < tmp_obj.get("timestamp").getAsLong()){
							Map<String, String> k = new HashMap<String, String>();
							k.put("id", tmp_obj.get("guild").getAsString());
							return getGuild(k);
						}
					}
					JsonObject response = fetch("findGuild", key, val);
					if(response.get("success").getAsBoolean() && !response.get("guild").isJsonNull()){
						JsonObject tmp_arr = new JsonObject();
						tmp_arr.addProperty("timestamp", new Date().getTime());
						tmp_arr.addProperty("guild", response.getAsJsonObject().get("guild").getAsString());
						content.add(val, tmp_arr);
						setContent(filename, content.toString());
						Map<String, String> map = new HashMap<String, String>(); 
						map.put("id", response.get("guild").getAsString());
						return this.getGuild(map);
					}
				}
				if(key == "id"){
					String filename = options.get("cache_folder_guild")+key+PATH_SEPARATOR+val+".json";
					File file = new File(filename);
					if(file.exists()){
						if(new Date().getTime() - Integer.valueOf(options.get("cache_time")) < file.lastModified()){
							JsonObject content = new JsonParser().parse(getContent(filename)).getAsJsonObject();
							return new Guild(content.getAsJsonObject("guild"), this);
						} 
					}else {
						file.getParentFile().mkdirs();
					}
					JsonObject response = fetch("guild", key, val);
					if(response.get("success").getAsBoolean() && !response.get("guild").isJsonNull()){
						setContent(filename, response.getAsJsonObject("guild").toString());
						return new Guild(response.getAsJsonObject("guild"), this);
					}
				}
			}
		}
		return new Guild(null, this);
	}
	
	public Session getSession(Map<String, String> keypair){
		Map<String,String> pairs = new HashMap<String,String>();
		pairs.put("player", "");
		
		for(String key : keypair.keySet()){
			if(pairs.keySet().contains(key)){
				pairs.put(key, keypair.get(key));
			}
		}
		
		for(String key : pairs.keySet()){ String val = pairs.get(key);
			val = val.toLowerCase();
			if(val != ""){
				if(key == "player"){
					String filename = this.options.get("cache_folder_sessions")+key+PATH_SEPARATOR+this.getCacheFileName(val)+".json";
					File file = new File(filename);
					if(file.exists()){
						if(new Date().getTime() - Integer.valueOf(options.get("cache_time")) < file.lastModified()){
							JsonObject content = new JsonParser().parse(getContent(filename)).getAsJsonObject();
							return new Session(content, this);
						}
					}else{
						file.getParentFile().mkdirs();
					}
					JsonObject response = fetch("session", key, val);
					if(response.get("success").getAsBoolean() && !response.get("session").isJsonNull()){
						setContent(filename, response.getAsJsonObject("session").toString());
						return new Session(response.get("session"), this);
					}
				}
			}
		}
		return new Session(null, this);
	}
	
	public Friends getFriends(Map<String, String> keypair){
		Map<String,String> pairs = new HashMap<String,String>();
		pairs.put("player", "");
		
		for(String key : keypair.keySet()){
			if(pairs.keySet().contains(key)){
				pairs.put(key, keypair.get(key));
			}
		}
		
		for(String key : pairs.keySet()){ String val = pairs.get(key);
			val = val.toLowerCase();
			if(val != ""){
				if(key == "player"){
					String filename = this.options.get("cache_folder_friends")+key+PATH_SEPARATOR+this.getCacheFileName(val)+".json";
					File file = new File(filename);
					if(file.exists()){
						if(new Date().getTime() - Integer.valueOf(options.get("cache_time")) < file.lastModified()){
							JsonObject content = new JsonParser().parse(getContent(filename)).getAsJsonObject();
							return new Friends(content, this);
						}
					} else {
						file.getParentFile().mkdirs();
					}

					JsonObject response = fetch("friends", key, val);
					if(response.get("success").getAsBoolean() && !response.get("records").isJsonNull()){
						setContent(filename, response.getAsJsonObject("records").toString());
						return new Friends(response.get("records"), this);
					}
				}
			}
		}
		return new Friends(null, this);
	}
	
	public String getContent(String filename){
		File f = new File(filename);
		f.getParentFile().mkdirs();
		JsonArray content = new JsonArray();
		if(!f.exists()){
			RequestManager.fwrite(f, content.toString());
		} else {
			return RequestManager.fread(f, f.length());
		}
		return "";
	}
	
	public void setContent(String filename, String content){
		File f = new File(filename);
		f.getParentFile().mkdirs();
		RequestManager.fwrite(f, content);
	}
	
	public String getCacheFileName(String input){
		if(input.length() < 3){
			return Util.implode(PATH_SEPARATOR, Util.str_split(input, 1));
		}
		return input.substring(0, 1)+PATH_SEPARATOR+input.substring(1, 2)+PATH_SEPARATOR+input.substring(2);
	}
}
