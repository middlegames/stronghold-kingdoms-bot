package com.middlegames.shkbot;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

/**
 * @author Middle Gamer (middlegamer)
 */
public class Controls {

	private static final Pattern CTRL_MAP1 = U.pattern("data/control/control-map-1.png").exact();
	private static final Pattern CTRL_MAP2 = U.pattern("data/control/control-map-2.png").exact();
	private static final Pattern CTRL_HUT1 = U.pattern("data/control/control-village-1.png").exact();
	private static final Pattern CTRL_HUT2 = U.pattern("data/control/control-village-2.png").exact();
	private static final Pattern CTRL_MARCHANT1 = U.pattern("data/control/control-marchant-1.png").exact();
	private static final Pattern CTRL_MARCHANT2 = U.pattern("data/control/control-marchant-2.png").exact();
	
	public static boolean openMap() throws SHKException {
		Consol.info("Opening map tab");
		Region r = SHK.R.TABS;
		try {
			if (r.click(CTRL_MAP1, 0) != 0) {
				return true;
			} else if (r.click(CTRL_MAP2, 0) != 0) {
				return true;
			} else {
				Consol.error("Map tab has not been found!");
				if (SHK.isConnectionError()) {
					throw new SHKConnectionErrorException("Cannot open marchant");
				}
			}
		} catch (FindFailed e) {
			Consol.error("Find failed for map tab!", e);
			throw new SHKException(e);
		}
		return false;
	}
	
	public static boolean openVillage() throws SHKException {
		Consol.info("Opening village tab");
		Region r = SHK.R.TABS;
		try {
			if (r.click(CTRL_HUT1, 0) != 0) {
				return true;
			} else if (r.click(CTRL_HUT2, 0) != 0) {
				return true;
			} else {
				Consol.error("Village tab has not been found!");
				if (SHK.isConnectionError()) {
					throw new SHKConnectionErrorException("Cannot open marchant");
				}
			}
		} catch (FindFailed e) {
			Consol.error("Find failed for village tab!", e);
			throw new SHKException(e);
		}
		return false;
	}

	public static boolean openMarchant() throws SHKException {
		openVillage();
		Consol.info("Opening marchant tab");
		Region r = SHK.R.CONTROLS;
		try {
			if (r.click(CTRL_MARCHANT1, 0) != 0) {
				return true;
			} else if (r.click(CTRL_MARCHANT2, 0) != 0) {
				return true;
			} else {
				Consol.error("Marchant tab has not been found!");
				if (SHK.isConnectionError()) {
					throw new SHKConnectionErrorException("Cannot open marchant");
				}
			}
		} catch (FindFailed e) {
			Consol.error("Find failed for marchant tab!", e);
			throw new SHKException(e);
		}
		return false;
	}
	
	public static void main(String[] args) throws InterruptedException, SHKException {
		Thread.sleep(3000);
		openMarchant();
	}
}
