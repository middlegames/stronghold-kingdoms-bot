package com.middlegames.shkbot.strategy;

import com.middlegames.shkbot.AbstractStrategy;
import com.middlegames.shkbot.Consol;
import com.middlegames.shkbot.Merchant;
import com.middlegames.shkbot.SHKException;
import com.middlegames.shkbot.Scout;
import com.middlegames.shkbot.Stash;

/**
 * This strategy will search for nearest stash, forage it and sell corresponding 
 * good in stock exchange.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class ScoutAndSellStrategy extends AbstractStrategy {

	@Override
	public void start() throws SHKException {
		do {
			for (Scout scout : getScouts()) {
				if (!isRunning()) {
					break;
				}
				Consol.info("Forage from " + scout.getVillage().getName());
				if (scout.forage()) {
					Stash stash = scout.getTarget();
					if (stash == null) {
						continue;
					}
					if (!stash.isPouch()) { 
						for (Merchant merchant : getMerchants()) {
							if (!isRunning()) {
								break;
							}
							if (merchant.getVillage() == scout.getVillage()) {
								Consol.info("Sell " + stash.getGoods() + " in " + scout.getVillage().getName());
								if (merchant.sell(stash.getGoods())) {
									break;
								}
							}
						}
					}
				}
			}
		} while (isRunning());
	}
	
	@Override
	public String getName() {
		return "Scout And Sell Strategy";
	}
}
