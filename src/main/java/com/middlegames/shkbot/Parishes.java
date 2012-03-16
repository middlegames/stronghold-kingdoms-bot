package com.middlegames.shkbot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.middlegames.shkbot.xml.ParishDescriptor;
import com.middlegames.shkbot.xml.PersistenceEngine;


/**
 * Parishes cache.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class Parishes {

	private static Map<String, Parish> parishes = null;
	
	public static Parish get(String name) {
		if (parishes == null) {
			init();
		}
		Parish p = parishes.get(name);
		if (p == null) {
			throw new IllegalArgumentException("Parish " + name + " has not been found!");
		}
		return p;
	}
	
	private synchronized static void init() {
		parishes = new HashMap<>();
		PersistenceEngine engine = PersistenceEngine.getInstance();
		List<ParishDescriptor> descriptors = engine.getParishDescriptors();
		for (ParishDescriptor pd : descriptors) {
			Parish parish = pd.toObject();
			parishes.put(parish.getName(), parish);
		}
	}
	
	static {
		init();
	}
	
	public static void main(String[] args) {
		Parish p = Parishes.get("Cabrach");
		System.out.println(p.getPattern());	
	}
}
