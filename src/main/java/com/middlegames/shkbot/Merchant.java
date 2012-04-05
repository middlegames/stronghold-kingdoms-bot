package com.middlegames.shkbot;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Location;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;


/**
 * Merchant class.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class Merchant {

	private static final Pattern HANDLER = U.pattern("data/control/buy-selll-handler.png", 0.9f);
	private static final Pattern TEXT_SELL = U.pattern("data/control/text-sell.png", 0.9f);
	private static final Pattern BUTTON_SELL = U.pattern("data/control/button-sell.png", 0.9f);
	private static final Pattern CLOSE_BUTTON = U.pattern("data/control/button-scout-close.png", 0.9f);
	private static final Pattern EXCHANGE_DROPDOWN = U.pattern("data/control/button-exchange-dropdown.png", 0.9f);

	private Village village = null;

	public Merchant(Village village) {
		super();
		this.village = village;
	}

	/**
	 * @return Village associated with Merchant
	 */
	public Village getVillage() {
		return village;
	}

	/**
	 * Sell given good type.
	 * 
	 * @param good
	 * @return true if good has been sold, false otherwise
	 * @throws SHKException
	 */
	public boolean sell(Goods good) throws SHKException {

		if (!village.select()) {
			Consol.error("Cannot select " + village);
			return false;
		}
		if (!Controls.openMarchant()) {
			Consol.error("Cannot open marchant tab!");
			return false;
		}

		Category category = good.getCategory();
		if (category != null) {
			category.open();
		} else {
			Consol.error("No category for good " + good);
			return false;
		}

		boolean sold = false;
		try {

			selectExchange(village.getParish());

			U.click(SHK.R.RESOURCES, good.getPattern());

			if (SHK.R.SELLAREA.find(TEXT_SELL) != null) {
				sold = sellProduct();
			}

			U.click(SHK.R.SELLAREA.getScreen(), CLOSE_BUTTON);

			// reset view to map
			Controls.openMap();

		} catch (FindFailed e) {
			Consol.error("Cannot send marchant due to error!", e);
			return false;
		}

		return sold;
	}

	private boolean selectExchange(Parish parish) throws FindFailed {

		if (SHK.R.EXCHANGES.click(EXCHANGE_DROPDOWN, 0) == 0) {
			Consol.error("Cannot click on the exchanges dropdown");
			return false;
		}

		Region list = new Region(SHK.R.EXCHANGES);
		list.setY(list.getY() + 40);
		list.setH(list.getH() - 40);
		list.setThrowException(false);
		list.highlight(1.0f);

		if (list.click(parish.getPattern(), 0) == 0) {
			Consol.error("Cannot select parish " + parish);
			return false;
		}
		return true;
	}

	private boolean sellProduct() throws FindFailed {

		Match h = SHK.R.SELLAREA.find(HANDLER);
		if (h == null) {
			return false;
		}

		SHK.R.SELLAREA.dragDrop(HANDLER, new Location(h.x + 150, h.y), 0);
		return SHK.R.SELLAREA.click(BUTTON_SELL, 0) != 0;
	}
}
