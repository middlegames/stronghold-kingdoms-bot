package com.middlegames.shkbot.gui;

import java.awt.Dimension;
import java.awt.SplashScreen;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.middlegames.shkbot.Consol;


/**
 * 
 * @author Middle Gamer (middlegamer)
 */
public class BotUI extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * JFrame
	 */
	private static BotUI frame = null;

	/**
	 * Right panel.
	 */
	private static BotInfoPane infoPane = null;

	/**
	 * Left tree
	 */
	private static BotGovernorTree governorTree = null;

	private BotUI() {

		setJMenuBar(new BotMenuBar());

		JScrollPane scrollpane = new JScrollPane(governorTree = new BotGovernorTree());
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollpane, infoPane = new BotInfoPane());

		add(split);

		// display splash pane
		SplashScreen splash = null;
		if ((splash = SplashScreen.getSplashScreen()) != null) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			splash.close();
		}

		Consol.iconMessage("SHK Bot has been started");

		setTitle("Stronghold Kingdoms Bot");
		setMinimumSize(new Dimension(300, 200));
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame = this;
	}

	public static BotUI getFrame() {
		return frame;
	}

	public static BotGovernorTree getGovernorTree() {
		return governorTree;
	}

	public static BotInfoPane getInfoPane() {
		return infoPane;
	}

	public static void main(String[] args) {

		// plaf
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				new BotUI();
			}
		});
	}
}
