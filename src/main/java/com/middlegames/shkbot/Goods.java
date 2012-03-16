package com.middlegames.shkbot;

import org.sikuli.script.Pattern;


/**
 * @author Middle Gamer (middlegamer)
 */
public enum Goods {
	
	ALE("data/control/goods-ale.png", Category.FOOD),
	APPLES("data/control/goods-apples.png", Category.FOOD),
	BREAD("data/control/goods-bread.png", Category.FOOD),
	CHEESE("data/control/goods-cheese.png", Category.FOOD),
	CLOTHES("data/control/goods-clothes.png", Category.BANQUETING),
	FISH("data/control/goods-fish.png", Category.FOOD),
	FURNITURE("data/control/goods-furniture.png", Category.BANQUETING),
	IRON("data/control/goods-iron.png", Category.RESOURCES),
	MEAT("data/control/goods-meat.png", Category.FOOD),
	METALWARE("data/control/goods-metalware.png", Category.BANQUETING),
	PITCH("data/control/goods-pitch.png", Category.RESOURCES),
	SALT("data/control/goods-salt.png", Category.BANQUETING),
	SILK("data/control/goods-silk.png", Category.BANQUETING),
	SPICES("data/control/goods-spices.png", Category.BANQUETING),
	STONE("data/control/goods-stone.png", Category.RESOURCES),
	VEG("data/control/goods-veg.png", Category.FOOD),
	VENISON("data/control/goods-venison.png", Category.BANQUETING),
	WINE("data/control/goods-wine.png", Category.BANQUETING),
	WOOD("data/control/goods-wood.png", Category.RESOURCES);
	
	// TODO add weapon
	
	private Pattern pattern = null;
	private Category category = null;
	
	private Goods(String pattern, Category category) {
		this.pattern = U.pattern(pattern, 0.9f);
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public Pattern getPattern() {
		return pattern;
	}
}