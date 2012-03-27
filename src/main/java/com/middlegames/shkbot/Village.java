package com.middlegames.shkbot;

import static com.middlegames.shkbot.SHK.LOG;

import org.sikuli.script.Pattern;
import org.sikuli.script.Region;


/**
 * Village class.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class Village {

	private static final Pattern VILLAGES_SWITCHER = U.pattern("data/control/system-villages-switcher.png");

	/**
	 * Village pattern.
	 */
	private Pattern pattern = null;

	/**
	 * Parish object.
	 */
	private Parish parish = null;

	/**
	 * Raw village name.
	 */
	private String name = null;

	public Village(String pattern, Parish parish) {
		this.pattern = U.pattern(pattern, 0.9f);
		this.parish = parish;
	}

	public boolean select() throws SHKException {

		boolean clicked = false;

		try {

			Region dropdown = U.find(SHK.R.SYSTEM, VILLAGES_SWITCHER);
			if (dropdown == null) {
				Consol.error("Villages switcher has not ben found");
				return checkErrorCondition();
			}

			dropdown = dropdown.right();
			dropdown.setW(400);

			clicked = U.click(SHK.R.SYSTEM, dropdown);
			if (!clicked) {
				LOG.warn("Cannot click on the villages dropdown");
			}

			Thread.sleep(1000);

			clicked = U.click(SHK.R.VILLAGES, getPattern());
			if (!clicked) {
				LOG.warn("Cannot select village " + getName());
				return checkErrorCondition();
			}

			Thread.sleep(4000);

			return true;
		} catch (InterruptedException e) {
			Consol.error("Village selection has been interrupted");
			return false;
		}
	}

	public Pattern getPattern() {
		return pattern;
	}

	public Parish getParish() {
		return parish;
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
