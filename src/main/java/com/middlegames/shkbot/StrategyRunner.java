package com.middlegames.shkbot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StrategyRunner {

	private static Strategy executing = null;
	
	private static ExecutorService executor = Executors.newCachedThreadPool();
	
	public static void start(Strategy strategy) {
		if (executing != null) {
			throw new RuntimeException("Strategy already running");
		}
		executing = strategy;
		try {
			Consol.info("Strategy runner - submit strategy");
			executor.submit(strategy);
		} catch (Exception e) {
			executing = null;
			throw e;
		}
	}
	
	public static void stop() {
		if (executing != null) {
			Consol.info("Strategy runner - stop strategy");
			executing.stop();
		}
		executing = null;
	}
	
	
}
