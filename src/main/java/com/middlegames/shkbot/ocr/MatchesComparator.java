package com.middlegames.shkbot.ocr;

import java.util.Comparator;

import org.sikuli.script.Match;


/**
 * Comparator used to sort matches position on x-axis.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class MatchesComparator implements Comparator<Match> {

	@Override
	public int compare(Match a, Match b) {
		if (a.x < b.x) {
			return -1;
		} else if (a.x > b.x) {
			return 1;
		} else {
			return 0;
		}
	}
}