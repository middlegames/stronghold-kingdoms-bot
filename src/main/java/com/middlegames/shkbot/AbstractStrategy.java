package com.middlegames.shkbot;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract botting strategy.
 * 
 * @author Middle Gamer (middlegamer)
 */
public abstract class AbstractStrategy implements Strategy {

	/**
	 * Strategy listeners.
	 */
	private List<StrategyListener> listeners = new ArrayList<>();

	/**
	 * All villages.
	 */
	private List<Village> villages = null;

	/**
	 * Scouts assigned to villages.
	 */
	private List<Scout> scouts = new ArrayList<>();

	/**
	 * Merchants assigned to villages.
	 */
	private List<Merchant> merchants = new ArrayList<>();

	/**
	 * Is strategy running.
	 */
	private boolean running = false;

	public AbstractStrategy() {
		villages = Villages.all();
		for (Village v : villages) {
			scouts.add(new Scout(v));
			merchants.add(new Merchant(v));
		}
	}

	public void addStrategyListener(StrategyListener l) {
		listeners.add(l);
	}

	public boolean removeStrategyListener(StrategyListener l) {
		return listeners.remove(l);
	}

	public StrategyListener[] getStrategyListeners() {
		return listeners.toArray(new StrategyListener[listeners.size()]);
	}

	public List<Village> getVillages() {
		return villages;
	}

	public List<Merchant> getMerchants() {
		return merchants;
	}

	public List<Scout> getScouts() {
		return scouts;
	}

	private void waitForSHK() {
		while (!SHK.isOpen()) {
			Consol.info("Waiting for SHK window to be open.");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		waitForSHK();
		running = true;
		for (StrategyListener l : listeners) {
			l.startPerformed();
		}
		do {
			try {
				start();
				break;
			} catch (SHKConnectionErrorException e) {
				SHK.closeConnectionError();
				SHK.playWorld();
				waitForSHK();
			} catch (SHKPopupHangException e) {
				// TODO
			} catch (SHKException e) {
				Consol.error("SHK exception!", e);
				throw new RuntimeException(e);
			}
		} while (true);
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		this.running = false;
		for (StrategyListener l : listeners) {
			l.stopPerformed();
		}
	}

	@Override
	public String toString() {
		return getName();
	}
}
