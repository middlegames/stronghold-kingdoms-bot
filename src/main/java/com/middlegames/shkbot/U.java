package com.middlegames.shkbot;

import static com.middlegames.shkbot.SHK.LOG;

import java.io.IOException;
import java.nio.file.Paths;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;


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

	/**
	 * Find specific pattern in given region.
	 * 
	 * @param where - where to search for what
	 * @param what - what to search
	 * @return Match or null if not found
	 */
	public static Match find(Region where, Pattern what) {
		if (where.getThrowException()) {
			where.setThrowException(false);
		}
		Match found = null;
		try {
			found = where.find(what);
		} catch (FindFailed e) {
			e.printStackTrace();
		}
		if (LOG.isDebugEnabled()) {
			if (found == null) {
				LOG.debug("Pattern " + what + " has not been found");
			} else {
				LOG.debug("Found pattern " + what + " in " + found);
				found.highlight(1.0f);
			}
		}
		return found;
	}

	/**
	 * Click on specific pattern inside specific region.
	 * 
	 * @param where - where to click
	 * @param what - what to click
	 * @return True if pattern has been clicked, false otherwise
	 */
	public static boolean click(Region where, Pattern what) {
		return click0(where, what);
	}

	/**
	 * Click on specific region inside other region.
	 * 
	 * @param where - where to click
	 * @param what - what to click
	 * @return True if region has been clicked, false otherwise
	 */
	public static boolean click(Region where, Region what) {
		return click0(where, what);
	}

	/**
	 * Click on specific object inside other region.
	 * 
	 * @param where - where to click
	 * @param what - what to click
	 * @return True if object has been clicked, false otherwise
	 */
	private static boolean click0(Region where, Object what) {
		if (where.getThrowException()) {
			where.setThrowException(false);
		}
		int click = 0;
		try {
			click = where.click(what, 0);
		} catch (FindFailed e) {
			e.printStackTrace();
		}
		if (LOG.isDebugEnabled()) {
			if (click == 0) {
				LOG.debug("Cannot click on " + what + " in " + where);
			} else {
				LOG.debug("Clicked on " + what + " in " + where);
			}
		}
		if (click == 0) {
			Consol.error("Cannot click on " + what);
		}
		return click != 0;
	}

	/**
	 * Check if specific pattern exists in given region.
	 * 
	 * @param where - where to check
	 * @param what - what to search
	 * @return TRue if pattern exists, false otherwise.
	 */
	public static boolean exists(Region where, Pattern what) {
		return where.exists(what) != null;
	}

}
