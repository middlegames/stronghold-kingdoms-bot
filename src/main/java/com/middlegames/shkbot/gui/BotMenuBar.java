package com.middlegames.shkbot.gui;

import java.awt.Event;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.middlegames.shkbot.Consol;
import com.middlegames.shkbot.Strategy;
import com.middlegames.shkbot.StrategyRunner;
import com.middlegames.shkbot.strategy.GrandSaleStrategy;
import com.middlegames.shkbot.strategy.ScoutAndSellStrategy;


public class BotMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("serial")
	private static class ActioStartStrategy extends AbstractAction {

		private Strategy strategy = null;

		public ActioStartStrategy(Strategy strategy) {
			super(strategy.getName());
			this.strategy = strategy;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Starting " + strategy);
			StrategyRunner.start(strategy);
			BotUI.getFrame().setState(Frame.ICONIFIED);
		}
	}

	@SuppressWarnings("serial")
	private static class ActionStopStrategy extends AbstractAction implements HotkeyListener {

		private static JIntellitype hook = JIntellitype.getInstance();

		protected void initGlobalKeyboardHook() {
			hook.registerSwingHotKey(3, Event.ALT_MASK + Event.SHIFT_MASK, (int) 'C');
			hook.addHotKeyListener(this);
		}

		public ActionStopStrategy() {
			super("Stop");
			initGlobalKeyboardHook();
		}

		@Override
		public void onHotKey(int key) {
			Consol.info("Hot key detected - turning system down");
			stopStrategy();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			stopStrategy();
		}

		private void stopStrategy() {
			System.out.println("Stopping strategy");
			StrategyRunner.stop();
			BotUI.getFrame().setState(Frame.NORMAL);
		}
	}

	@SuppressWarnings("serial")
	private static class ActionExit extends AbstractAction {

		public ActionExit() {
			super("Exit");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Consol.info("Exit SHK Bot");
			System.exit(0);
		}
	}

	@SuppressWarnings("serial")
	private static class BotMenu extends JMenu {

		public BotMenu() {
			super("Bot");
			add(new JMenuItem(new ActionExit()));
		}
	}

	@SuppressWarnings("serial")
	private static class StrategyMenu extends JMenu {

		public StrategyMenu() {
			super("Strategy");

			JMenu strategies = new JMenu("Start Strategy");
			strategies.add(new JMenuItem(new ActioStartStrategy(new GrandSaleStrategy())));
			strategies.add(new JMenuItem(new ActioStartStrategy(new ScoutAndSellStrategy())));

			add(strategies);
			add(new JMenuItem(new ActionStopStrategy()));
		}
	}

	public BotMenuBar() {
		super();
		add(new BotMenu());
		add(new StrategyMenu());
	}
}
