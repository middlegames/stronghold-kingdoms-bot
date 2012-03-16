package com.middlegames.shkbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.middlegames.shkbot.xml.PersistenceEngine;
import com.middlegames.shkbot.xml.VillageDescriptor;


/**
 * Villages cache.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class Villages {

	private static Map<String, Village> villages = null;
	
	public static Village get(String name) {
		if (villages == null) {
			init();
		}
		Village v = villages.get(name);
		if (v == null) {
			throw new IllegalArgumentException("Village " + name + " has not been found!");
		}
		return v;
	}
	
	public static List<Village> all() {
		List<Village> r = new ArrayList<>();
		for (Entry<String, Village> entry : villages.entrySet()) {
			r.add(entry.getValue());
		}
		return r;
	}
	
	private synchronized static void init() {
		villages = new HashMap<>();
		PersistenceEngine engine = PersistenceEngine.getInstance();
		List<VillageDescriptor> descriptors = engine.getVillageDescriptors();
		for (VillageDescriptor pd : descriptors) {
			Village village = pd.toObject();
			villages.put(village.getName(), village);
		}
		Consol.info("Number of " + villages.size() + " villages has been found in storage");
	}
	
	static {
		init();
	}
	
	public static void main(String[] args) {
		List<Village> p = Villages.all();
		System.out.println(p.size());	
	}
}
