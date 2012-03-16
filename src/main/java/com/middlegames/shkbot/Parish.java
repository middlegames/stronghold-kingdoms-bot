package com.middlegames.shkbot;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.sikuli.script.Pattern;


/**
 * Parish class.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class Parish {
	
	private Pattern pattern = null;
	private String name = null;
	private boolean steward = false;
	

	public Parish(String path) {
		if (!Files.exists(Paths.get(path))) {
			throw new RuntimeException("File not found " + path);
		}
		this.pattern = new Pattern(path).similar(0.95f);
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isSteward() {
		return steward;
	}
	
	public void setSteward(boolean steward) {
		this.steward = steward;
	}
	
}
