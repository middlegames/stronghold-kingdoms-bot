package com.middlegames.shkbot;

import java.awt.Dimension;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;


/**
 * 
 * 
 * @author Middle Gamer (middlegamer)
 */
public class SHK {

	public static class R {

		public static final Screen SCREEN = new Screen();
		public static final Region SHIELD = new Region(0, 24, 78, 88);

		private static final Dimension D = new Dimension(SCREEN.getW(), SCREEN.getH());

		public static final Region SYSTEM = getSystem();
		public static final Region TABS = getTabs();
		public static final Region CONTROLS = getControls();
		public static final Region BARTER = getBarter();
		public static final Region RESOURCES = getResources();
		public static final Region SELLAREA = getSellarea();
		public static final Region BUYAREA = getBuyarea();
		public static final Region EXCHANGES = getExchanges();
		public static final Region MAP = getMap();
		public static final Region DEVICE = getDevice();
		public static final Region VILLAGES = getVillages();
		public static final Region SENDSCOUT = getSendScout();
		public static final Region WINDOW_CONTROLS = getWindowControls();

		static {
			init();
		}

		private R() {
		}

		private static Region getSystem() {
			int x = (int) (D.getWidth() - 480);
			int y = 0;
			int w = 480;
			int h = 78;
			return new Region(x, y, w, h);
		}

		private static Region getTabs() {
			int x = (int) (D.getWidth() - 472);
			int y = 69;
			int w = 472;
			int h = 57;
			return new Region(x, y, w, h);
		}

		private static Region getControls() {
			int x = (int) (D.getWidth() - 467);
			int y = 106;
			int w = 472;
			int h = 43;
			return new Region(x, y, w, h);
		}

		private static Region getBarter() {
			int px = (int) ((D.getWidth() - 991) / 2);
			int py = (int) ((D.getHeight() - 141 - 30 - 565) / 2);
			int x = px + 35;
			int y = py + 190;
			int w = 355;
			int h = 88;
			return new Region(x, y, w, h);
		}

		private static Region getResources() {
			int px = (int) ((D.getWidth() - 991) / 2);
			int py = (int) ((D.getHeight() - 141 - 30 - 565) / 2);
			int x = px + 65;
			int y = py + 324;
			int w = 188;
			int h = 317;
			return new Region(x, y, w, h);
		}

		private static Region getSellarea() {
			int px = (int) ((D.getWidth() - 991) / 2);
			int py = (int) ((D.getHeight() - 141 - 30 - 565) / 2);
			int x = px + 628;
			int y = py + 375;
			int w = 335;
			int h = 185;
			return new Region(x, y, w, h);
		}

		private static Region getBuyarea() {
			int px = (int) ((D.getWidth() - 991) / 2);
			int py = (int) ((D.getHeight() - 141 - 30 - 565) / 2);
			int x = px + 628;
			int y = py + 176;
			int w = 335;
			int h = 185;
			return new Region(x, y, w, h);
		}

		private static Region getExchanges() {
			int px = (int) ((D.getWidth() - 991) / 2);
			int py = (int) ((D.getHeight() - 141 - 30 - 565) / 2);
			int x = px + 375;
			int y = py + 215;
			int w = 228;
			int h = 455;
			return new Region(x, y, w, h);
		}

		private static Region getMap() {
			int x = 0;
			int y = 115;
			int w = (int) (D.getWidth() - 198);
			int h = (int) (D.getHeight() - y - 30);
			return new Region(x, y, w, h);
		}

		private static Region getDevice() {
			int x = (int) (D.getWidth() - 200);
			int y = 115;
			int w = 200;
			int h = (int) (D.getHeight() - y - 30);
			return new Region(x, y, w, h);
		}

		private static Region getVillages() {
			int x = (int) (D.getWidth() - 402);
			int y = 75;
			int w = 360;
			int h = 300;
			return new Region(x, y, w, h);
		}

		private static Region getSendScout() {
			int x = (int) (D.getWidth() - 199 - 699) / 2;
			int y = (int) (D.getHeight() - 117 - 30 - 482) / 2 + 117;
			int w = 699;
			int h = 482;
			return new Region(x, y, w, h);
		}

		private static Region getWindowControls() {
			int x = (int) (D.getWidth() - 100);
			int y = 0;
			int w = 100;
			int h = 30;
			return new Region(x, y, w, h);
		}

		private static void init() {
			SCREEN.setThrowException(false);
			SHIELD.setThrowException(false);
			SYSTEM.setThrowException(false);
			TABS.setThrowException(false);
			CONTROLS.setThrowException(false);
			BARTER.setThrowException(false);
			RESOURCES.setThrowException(false);
			SELLAREA.setThrowException(false);
			BUYAREA.setThrowException(false);
			MAP.setThrowException(false);
			DEVICE.setThrowException(false);
			VILLAGES.setThrowException(false);
			EXCHANGES.setThrowException(false);
			SENDSCOUT.setThrowException(false);
			WINDOW_CONTROLS.setThrowException(false);
		}
	}

	/**
	 * SHK logo in left window's corner.
	 */
	private static final Pattern LOGO = U.pattern("data/control/sk-shield-logo.png", 1.0f);
	private static final Pattern CONNECTION_ERROR = U.pattern("data/control/connection-error.png");
	private static final Pattern LOGIN_SHIELD = U.pattern("data/control/shk-login-screen-shield.png");
	private static final Pattern PLAY_BUTTON = U.pattern("data/control/play-button.png");
	private static final Pattern CLOSE_WINDOW_BUTTON = U.pattern("data/control/close-window-button.png");

	public static boolean isOpen() {
		return SHK.R.SHIELD.exists(LOGO) != null;
	}

	/**
	 * @return true in case of SHK connection error, false otherwise
	 */
	public static boolean isConnectionError() {
		Match m = null;
		try {
			m = R.SCREEN.find(CONNECTION_ERROR);
			if (m != null) {
				m.highlight(2.0f);
			}
		} catch (FindFailed e) {
			return false;
		}
		return m != null;
	}

	public static boolean closeConnectionError() {
		int i = 0;
		final Region screen = R.SCREEN;
		do {
			try {
				if (screen.click(CONNECTION_ERROR, 0) != 0) {
					screen.type(CONNECTION_ERROR, "\n", 0); // enter
				}
			} catch (FindFailed e) {
				throw new RuntimeException(e);
			}
			boolean vanish = screen.waitVanish(CONNECTION_ERROR, 10);
			if (vanish) {
				return true;
			}
		} while (i++ < 5);
		return false;
	}

	public static boolean playWorld() {
		int i = 0;
		final Region screen = R.SCREEN;
		do {
			try {
				Match found = screen.wait(LOGIN_SHIELD, 10);
				if (found == null) {
					continue;
				}
				return screen.click(PLAY_BUTTON, 0) != 0;
			} catch (FindFailed e) {
				throw new RuntimeException(e);
			}
		} while (i++ < 5);
		return false;
	}

	public static boolean closeWindowAndCancel() {
		// TODO
		return false;
	}

	public static void main(String[] args) throws Exception {
		Thread.sleep(1000);
		R.getWindowControls().highlight(2.0f);
		Thread.sleep(2000);
	}
}
