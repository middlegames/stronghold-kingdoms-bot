package com.middlegames.shkbot;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;


/**
 * Village class.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class Village {

	private static final Pattern VILLAGES_SWITCHER = U.pattern("data/control/system-villages-switcher.png");

	private Pattern pattern = null;
	private Parish parish = null;
	private String name = null;

	public Village(String pattern, Parish parish) {
		this.pattern = U.pattern(pattern, 0.9f);
		this.parish = parish;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public Parish getParish() {
		return parish;
	}

	public boolean select() throws SHKException {
		Region s = SHK.R.SYSTEM;
		Region v = SHK.R.VILLAGES;
		try {
			Region dropdown = s.find(VILLAGES_SWITCHER);
			if (dropdown == null) {
				Consol.error("Villages switcher has not ben found");
				return checkErrorCondition();
			}

			dropdown = dropdown.right();
			dropdown.setW(400);
			dropdown.setThrowException(false);

			s.click(dropdown, 0);
			Thread.sleep(1000);
			int click = v.click(this.getPattern(), 0);
			if (click == 0) {
				Consol.error("Village " + getName() + " not selected - pattern has not been found!");
				return checkErrorCondition();
			}

			Thread.sleep(4000);
			return true;
		} catch (FindFailed e) {
			e.printStackTrace();
			Consol.error("Village selection failed");
			return checkErrorCondition();
		} catch (InterruptedException e) {
			Consol.error("Village selection has been interrupted");
			return false;
		}
	}

	private boolean checkErrorCondition() throws SHKConnectionErrorException {
		if (SHK.isConnectionError()) {
			throw new SHKConnectionErrorException("Connection error when selecting village");
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Village[" + getName() + "]";
	}
}
