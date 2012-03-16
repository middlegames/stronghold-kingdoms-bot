package com.middlegames.shkbot;

public interface Strategy extends Runnable {

	public void stop();
	
	public void start() throws SHKException;
	
	public String getName();
}
