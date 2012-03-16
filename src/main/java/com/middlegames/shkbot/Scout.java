package com.middlegames.shkbot;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Location;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;


/**
 * Scout class.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class Scout {

	private static final Pattern VILLAGE_SHIELD = U.pattern("data/control/village-shield.png");
	private static final Pattern SCOUT_BUTTON = U.pattern("data/control/button-scout.png", 0.9f);
	private static final Pattern ATTACK_BUTTON = U.pattern("data/control/button-attack.png", 0.9f);
	private static final Pattern TARGET_ROLL = U.pattern("data/control/target-roll.png", 0.9f);
	private static final Pattern GO_BUTTON = U.pattern("data/control/button-scout-go.png", 0.9f);
	private static final Pattern CLOSE_BUTTON = U.pattern("data/control/button-scout-close.png", 0.9f);
	private static final Pattern SCOUT_ICON = U.pattern("data/control/icon-scout.png", 0.9f);
	private static final Pattern SCROLL_HANDLE = U.pattern("data/control/scroll-handle.png", 0.9f);

	/**
	 * Execution service used to search for stashes and highlight matches.
	 */
	private static ExecutorService executor = Executors.newCachedThreadPool(new ExecutorThreadFactory());

	/**
	 * Stashes to be searched (all).
	 */
	private static final EnumSet<Stash> STASHES = EnumSet.allOf(Stash.class);

	/**
	 * Thread factory for execution service.
	 * 
	 * @author Middle Gamer (middlegamer)
	 */
	private static class ExecutorThreadFactory implements ThreadFactory {

		/**
		 * Thread number.
		 */
		private AtomicInteger i = new AtomicInteger(0);

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "StashSearchThread-" + i.getAndIncrement());
			t.setDaemon(true);
			return t;
		}
	}

	/**
	 * Container for search result.
	 * 
	 * @author Middle Gamer (middlegamer)
	 */
	private static class MatchResult {

		private Stash stash = null;
		private Match match = null;

		public MatchResult(Stash stash, Match match) {
			super();
			this.stash = stash;
			this.match = match;
		}

		public Stash getStash() {
			return stash;
		}

		public Match getMatch() {
			return match;
		}
	}

	private static class NearestSearch implements Callable<MatchResult> {

		private Stash stash = null;

		public NearestSearch(Stash stash) {
			this.stash = stash;
		}

		@Override
		public MatchResult call() throws Exception {
			Match match = null;
			try {
				match = SHK.R.MAP.find(stash.getPattern());
				if (match != null) {
					executor.execute(new Highlighter(match));
				}
			} catch (FindFailed e) {
				return null;
			}
			return new MatchResult(stash, match);
		}
	}

	/**
	 * Asynchronous highligher.
	 * 
	 * @author Middle Gamer (middlegamer)
	 */
	private static class Highlighter implements Runnable {

		private Match match = null;

		public Highlighter(Match match) {
			this.match = match;
		}

		@Override
		public void run() {
			match.highlight(1.0f);
		}
	}

	private Village village = null;

	private int radius = 200;

	private boolean scouting = false;
	private Stash target = null;

	public Scout(Village village) {
		this.village = village;
	}

	public Village getVillage() {
		return village;
	}

	public int getSearchRadius() {
		return radius;
	}

	public void setSearchRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * Send only one scout to the nearest stash.
	 * 
	 * @return
	 * @throws SHKException
	 */
	public boolean scout() throws SHKException {
		village.select();
		Controls.openMap();
		MatchResult location = nearest();
		if (location != null) {
			target = location.getStash();
			executor.execute(new Highlighter(location.getMatch()));
			return sendScout(location.getMatch(), false);
		} else {
			target = null;
		}
		Consol.info("None shash has been found");
		return false;
	}

	/**
	 * Send only one scout to the specified stash.
	 * 
	 * @param stash
	 * @return
	 * @throws SHKException
	 */
	public boolean scout(Stash stash) throws SHKException {
		village.select();
		Controls.openMap();
		Match location = square(stash, radius);
		if (location != null) {
			target = stash;
			location.highlight(1.0f);
			return sendScout(location, false);
		}
		Consol.info(String.format("Stash %s has not been found", stash));
		return false;
	}

	/**
	 * Send all scouts to the given stash.
	 * 
	 * @param stash
	 * @return
	 * @throws SHKException
	 */
	public boolean forage(Stash stash) throws SHKException {
		Consol.info("Foreaging " + stash);
		village.select();
		Controls.openMap();
		Match location = square(stash, radius);
		if (location != null) {
			target = stash;
			location.highlight(1.0f);
			return sendScout(location, true);
		}
		Consol.info(String.format("Stash %s to forage has not been found", stash));
		return false;
	}

	/**
	 * Send all scouts to the nearest stash.
	 * 
	 * @return
	 * @throws SHKException
	 */
	public boolean forage() throws SHKException {
		village.select();
		Controls.openMap();
		MatchResult location = nearest();
		if (location != null) {
			target = location.getStash();
			executor.execute(new Highlighter(location.getMatch()));
			return sendScout(location.getMatch(), true);
		} else {
			target = null;
		}
		Consol.info("None stash has been found in the nearest area to forage");
		return false;
	}

	private boolean sendScout(Match location, boolean sendAll) throws SHKPopupHangException {
		Map.select(location);
		try {

			// check if selected target FOR SURE is a stash (can be also other's
			// player village or AI castle or wolfs lair)
			Match m;
			if ((m = SHK.R.DEVICE.find(TARGET_ROLL)) != null) {
				m.highlight(1.0f);
				Consol.error("Incorrect selection - target scroll has been found");
				return false;
			}
			if ((m = SHK.R.DEVICE.find(ATTACK_BUTTON)) != null) {
				m.highlight(1.0f);
				Consol.error("Incorrect selection - attack button has been found");
				return false;
			}

			// finally if selected target is a stash - send scout

			SHK.R.DEVICE.click(SCOUT_BUTTON, 0);
			SHK.R.SENDSCOUT.wait(SCOUT_ICON, 2);

			// TODO check if there are scouts available

			Thread.sleep(2000);

			if (!sendAll) {
				// send only one scout
				Match handle = SHK.R.SENDSCOUT.find(SCROLL_HANDLE);
				if (handle != null) {
					SHK.R.SENDSCOUT.dragDrop(handle, new Location(handle.x - 100, handle.y), 0);
				}
			}

			SHK.R.SENDSCOUT.click(GO_BUTTON, 0);
			Thread.sleep(2000);

			// in case if form has not been closed (e.g. no scouts available)
			boolean closed = false;
			int i = 0;
			do {
				Match close = SHK.R.MAP.find(CLOSE_BUTTON);
				if (close != null) {
					SHK.R.MAP.click(close, 0);
					Thread.sleep(2000);
				} else {
					closed = true;
					break;
				}
				close = SHK.R.MAP.find(CLOSE_BUTTON);
				if (close == null) {
					closed = true;
					break;
				}
			} while (i++ < 5);

			if (!closed) {
				if (i >= 5) {
					throw new SHKPopupHangException("Popup hand when scouting " + location);
				} else {
					return false;
				}
			}

			scouting = true;
			return true;

		} catch (FindFailed e) {
			Consol.error("Cannot send scout, resource not found!", e);
		} catch (InterruptedException e) {
			Consol.error("Send scout interrupted!", e);
		}

		return false;
	}

	/**
	 * Perform a square search for given stash.
	 * 
	 * @param stash - stash to be found
	 * @param radius - radius to search
	 * @return return {@link Match} object if stash has been found, null
	 *         otherwise
	 */
	private Match square(Stash stash, int radius) {

		Region m = SHK.R.MAP;
		Match p = null;

		try {
			p = m.find(stash.getPattern());
			if (p != null) {
				return p;
			}

			Region s = getShieldRegion();
			Match t = s.find(VILLAGE_SHIELD);
			if (t == null) {
				Consol.error("Cannot find village shield!");
				return null;
			} else {
				executor.submit(new Highlighter(t));
			}

			t.setX(t.getX() + 12);
			t.setY(t.getY() + 44);

			m.dragDrop(t, new Location(t.x - radius, t.y), 0);
			p = m.find(stash.getPattern());
			if (p != null) {
				return p;
			}

			m.dragDrop(t, new Location(t.x, t.y - radius), 0);
			p = m.find(stash.getPattern());
			if (p != null) {
				return p;
			}

			m.dragDrop(t, new Location(t.x + radius, t.y), 0);
			m.dragDrop(t, new Location(t.x + radius, t.y), 0);
			p = m.find(stash.getPattern());
			if (p != null) {
				return p;
			}

			m.dragDrop(t, new Location(t.x, t.y + radius), 0);
			m.dragDrop(t, new Location(t.x, t.y + radius), 0);
			p = m.find(stash.getPattern());
			if (p != null) {
				return p;
			}

			m.dragDrop(t, new Location(t.x - radius, t.y), 0);
			m.dragDrop(t, new Location(t.x - radius, t.y), 0);
			p = m.find(stash.getPattern());
			return p;

		} catch (FindFailed e) {
			Consol.error("Square search failed!", e);
		}

		return null;
	}

	protected static Region getShieldRegion() {
		int x = (int) (SHK.R.MAP.getW() - 70) / 2;
		int y = (int) (SHK.R.MAP.getH() - 70) / 2 + 117 - 30;
		int w = 70;
		int h = 70;
		Region r = new Region(x, y, w, h);
		r.setThrowException(false);
		return r;
	}

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(4000);
		getShieldRegion().highlight(2.0f);
	}

	public MatchResult nearest() {

		Consol.info("Nearest search");

		Region s = getShieldRegion();
		Match t = null;
		try {
			t = s.find(VILLAGE_SHIELD);
		} catch (FindFailed e) {
			Consol.error("Find failed for village shield");
		}

		if (t == null) {
			Consol.error("Cannot find village shield!");
			return null;
		}

		t.setX(t.getX() + 12);
		t.setY(t.getY() + 44);

		List<Future<MatchResult>> futures = new ArrayList<Future<MatchResult>>();
		for (Stash stash : STASHES) {
			futures.add(executor.submit(new NearestSearch(stash)));
		}

		List<MatchResult> matches = new ArrayList<MatchResult>();
		List<Future<MatchResult>> remove = new ArrayList<Future<MatchResult>>();

		do {
			for (Future<MatchResult> future : futures) {
				if (future.isDone()) {
					try {
						MatchResult result = future.get();
						Match match = result.getMatch();
						if (match != null) {
							matches.add(result);
							Consol.info("Match found " + result.getStash());
						}
					} catch (Exception e) {
						Consol.error("Cannot execute future task", e);
					}
					remove.add(future);
				}
			}

			futures.removeAll(remove);
			remove.clear();

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (!futures.isEmpty());

		// village coordinates

		double ox = t.getX();
		double oy = t.getY();

		// find nearest match

		double dm = Double.MAX_VALUE;
		MatchResult mm = null;

		for (MatchResult result : matches) {

			Match match = result.getMatch();

			double x = match.getCenter().getX();
			double y = match.getCenter().getY();
			double dx = Math.pow(Math.abs(x - ox), 2);
			double dy = Math.pow(Math.abs(y - oy), 2);
			double d = Math.sqrt(dx + dy);

			if (d < dm) {
				dm = d;
				mm = result;
			}
		}

		// highlight target

		if (mm != null) {
			executor.execute(new Highlighter(mm.getMatch()));
		}

		return mm;
	}

	public boolean isScouting() {
		return scouting;
	}

	public Stash getTarget() {
		return target;
	}
}
