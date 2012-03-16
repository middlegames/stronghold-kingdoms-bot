package com.middlegames.shkbot;
import java.awt.event.InputEvent;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;


/**
 * @author Middle Gamer (middlegamer)
 */
public class Map {

	public static boolean select(Pattern pattern) {
		return select0(pattern);
	}
	
	public static boolean select(Match match) {
		return select0(match);
	}
	
	private static boolean select0(Object pattern) {
		Region m = SHK.R.MAP;
		try {
			m.hover(pattern);
			Thread.sleep(500);
			m.mouseDown(InputEvent.BUTTON1_MASK);
			Thread.sleep(500);
			m.mouseUp(InputEvent.BUTTON1_MASK);
			Thread.sleep(300);
			return true;
		} catch (FindFailed e) {
			Consol.error("Map cannot point to location!", e);
		} catch (InterruptedException e) {
			Consol.error("Map pointer has been interrupted!", e);
		}
		return false;	
	}
}
