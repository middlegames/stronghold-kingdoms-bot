package com.middlegames.shkbot;

import java.io.IOException;
import java.nio.file.Paths;

import org.sikuli.script.Pattern;


/**
 * Utilities.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class U {

	/**
	 * Pattern from file.
	 * 
	 * @param path - image file path
	 * @param similarity - similarity fraction
	 * @return Pattern created from image
	 */
	public static Pattern pattern(String path, float similarity) {
		return pattern(path).similar(similarity);
	}
	
	/**
	 * Pattern from file.
	 * 
	 * @param path - image file path
	 * @param similarity - similarity fraction
	 * @return Pattern created from image
	 */
	public static Pattern pattern(String path) {
		try {
			return new Pattern(Paths.get(path).toRealPath().toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
