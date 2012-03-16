package com.middlegames.shkbot;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;


/**
 * Resources category.
 * 
 * @author Middle Gamer (middlegamer)
 */
public enum Category {

	/**
	 * All production resources - wood, stone, etc.
	 */
	RESOURCES("data/control/category-resources-1.png"),
	
	/**
	 * What can be eaten.
	 */
	FOOD("data/control/category-food-1.png"),
	
	/**
	 * Army stuff.
	 */
	WEAPON("data/control/category-weapon-1.png"),
	
	/**
	 * Venison, spices, etc.
	 */
	BANQUETING("data/control/category-bankqueting-1.png");
	
	private Pattern pattern = null;
	
	
	private Category(String pattern) {
		this.pattern = U.pattern(pattern, 0.9f);
	}
	
	/**
	 * @return Pattern to match element on screen.
	 */
	public Pattern getPattern() {
		return pattern;
	}
	
	/**
	 * Open given category. 
	 * 
	 * @return True if category has been open, false otherwise
	 */
	public boolean open() {
		try {
			return SHK.R.BARTER.click(pattern, 0) != 0;
		} catch (FindFailed e) {
			Consol.error("Cannot open goods category " + this, e);
		}
		return false;
	}
}
