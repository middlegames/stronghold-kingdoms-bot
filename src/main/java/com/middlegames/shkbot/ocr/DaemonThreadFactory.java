package com.middlegames.shkbot.ocr;

import java.util.concurrent.ThreadFactory;


/**
 * Daemon threads factory.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class DaemonThreadFactory implements ThreadFactory {

	private int i = 0;

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "matcher-thread-" + ++i);
		t.setDaemon(true);
		return t;
	}
}