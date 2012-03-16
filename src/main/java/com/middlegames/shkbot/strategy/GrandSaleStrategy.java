package com.middlegames.shkbot.strategy;

import java.util.HashMap;
import java.util.Map;

import com.middlegames.shkbot.AbstractStrategy;
import com.middlegames.shkbot.Goods;
import com.middlegames.shkbot.Merchant;
import com.middlegames.shkbot.SHKException;


public class GrandSaleStrategy extends AbstractStrategy {

	@Override
	public void start() throws SHKException {
		Map<Merchant, Integer> indices = new HashMap<>();
		for (Merchant merchant : getMerchants()) {
			indices.put(merchant, 0);
		}
		Goods goods[] = Goods.values();
		for (Merchant merchant : getMerchants()) {
			if (!isRunning()) {
				break;
			}
			for (int i = indices.get(merchant) + 1; i < goods.length; i++) {
				if (!isRunning()) {
					break;
				}
				indices.put(merchant, i);
				boolean sold = merchant.sell(goods[i]);
				if (sold) {
					break;
				}
			}
			indices.put(merchant, 0);
		}
	}
	
	@Override
	public String getName() {
		return "Grand Sale Strategy";
	}
}
