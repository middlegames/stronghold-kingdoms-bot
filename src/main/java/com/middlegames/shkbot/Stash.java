package com.middlegames.shkbot;

import org.sikuli.script.Pattern;


/**
 * @author Middle Gamer (middlegamer)
 */
public enum Stash {

	STASH("data/control/stash-new.png", Category.FOOD),
	APPLES("data/control/stash-apples.png", Category.FOOD),
	MEAT("data/control/stash-meat.png", Category.FOOD),
	CHEESE("data/control/stash-cheese.png", Category.FOOD),
	BREAD("data/control/stash-bread.png", Category.FOOD),
	FISH("data/control/stash-fish.png", Category.FOOD),
	VEG("data/control/stash-veg.png", Category.FOOD),
	ALE("data/control/stash-ale.png", Category.FOOD),
	WOOD("data/control/stash-wood.png", Category.RESOURCES),
	STONE("data/control/stash-stone.png", Category.RESOURCES),
	IRON("data/control/stash-iron.png", Category.RESOURCES),
	PITCH("data/control/stash-pitch.png", Category.RESOURCES),
	WINE("data/control/stash-wine.png", Category.BANQUETING),
	METALWARE("data/control/stash-metalware.png", Category.BANQUETING),
	CLOTHES("data/control/stash-clothes.png", Category.BANQUETING),
	FURNITURE("data/control/stash-furniture.png", Category.BANQUETING),
	SALT("data/control/stash-salt.png", Category.BANQUETING),
	SILK("data/control/stash-silk.png", Category.BANQUETING),
	SPICES("data/control/stash-spices.png", Category.BANQUETING);
	
	private Pattern pattern = null;
	private Category category = null;
	
	private Stash(String pattern, Category category) {
		this.pattern = U.pattern(pattern, 0.80f);
		this.category = category;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public Category getCategory() {
		return category;
	}
	
	public boolean isPouch() {
		return this == STASH;
	}
	
	public Goods getGoods() {
		if (isPouch()) {
			return null;
		}
		return Goods.valueOf(this.toString());
	}
}
