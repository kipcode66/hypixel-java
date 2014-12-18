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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Util {
	public static <K, T> Map<K, T> entrySetToMap(Set<Entry<K, T>> in){
		Map<K, T> map = new HashMap<K, T>();
		for(Entry<K, T> e : in){
			map.put(e.getKey(), e.getValue());
		}
		return map;
	}
	
	public static String implode(String chr, String[] strs){
		String ret = "";
		for(int i = 0; i< strs.length; i++){
			ret += strs[i];
			if(i+1 < strs.length) ret += chr;
		}
		return ret;
	}
	
	public static String[] str_split(String str, int l){
		int listLength = (int)Math.ceil(str.length()/l);
		String[] ret = new String[(int)Math.ceil(str.length()/l)];
		for(int i = 0; i < listLength; i++){
			ret[i] = str.substring(i*l, (i+1 < listLength) ? i*l+(l-1): str.length());
		}
		
		return ret;
	}
	
	public static <T> Map<T, Integer> array_flip(T[] arg1){
		Map<T,Integer> map = new HashMap<T,Integer>();
		for(int i = 0; i<arg1.length; i++){
			T t = arg1[i];
			map.put(t, i);
		}
		return map;
	}
	
	public static <T> T[] array_reverse(T[] arg1){
		T[] ret = Arrays.copyOf(arg1, arg1.length);
		for(int i = 0; i < arg1.length; i++){
			ret[i] = arg1[arg1.length-1 - i];
		}
		return ret;
	}
	
	public static String[] replaceNull(String[] a){
		for(int i = 0; i< a.length; i++){
			a[i] = (a[i] == null)?"":a[i];
		}
		return a;
	}
}
