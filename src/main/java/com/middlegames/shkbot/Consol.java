package com.middlegames.shkbot;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

/**
 * Later this will go to the popup.
 *
 * @author Middle Gamer (middlegamer)
 */
public class Consol {

	private static TrayIcon icon = null;
	
	static {
		init();
	}
	
	private static void init() {
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage("data/images/robot.png");
		icon = new TrayIcon(image, "SarXos SHK Bot", null);
		try {
			tray.add(icon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public static void info(String message) {
		iconMessage(message);
	}
	
	public static void error(String message) {
		iconError(message);
	}
	
	public static void error(String message, Throwable e) {
		e.printStackTrace();
		iconError(message);
	}
	
	public static void iconMessage(String message) {
		icon.displayMessage("Message", message, MessageType.INFO);
	}
	
	public static void iconError(String message) {
		icon.displayMessage("Error", message, MessageType.ERROR);
	}
}
