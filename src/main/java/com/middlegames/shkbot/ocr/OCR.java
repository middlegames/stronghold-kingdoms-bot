package com.middlegames.shkbot.ocr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;


/**
 * Simple OCR tool. It recognize only one line strings and require glyphs
 * library to be prepared first.
 * 
 * @author Middle Gamer (middlegamer)
 */
public class OCR {

	/**
	 * Execution service.
	 */
	private static ExecutorService executor = Executors.newCachedThreadPool(new DaemonThreadFactory());

	/**
	 * Specs cache.
	 */
	private static Map<String, OCR> specs = new HashMap<>();

	/**
	 * Glyphs list.
	 */
	private List<Glyph> glyphs = null;

	/**
	 * Create OCR engine.
	 * 
	 * @param glyphs - list of glyphs that can be recognized
	 */
	private OCR(List<Glyph> glyphs) {
		this.glyphs = glyphs;
	}

	/**
	 * Get specific OCR engine (loaded with given glyphs).
	 * 
	 * @param name - glyphs library name
	 * @return OCR engine
	 */
	public static OCR getSpec(String name) {
		OCR ocr = specs.get(name);
		if (ocr == null) {
			List<Glyph> glyphs = Glyphs.getInstance().load("data/glyphs/" + name);
			ocr = new OCR(glyphs);
			specs.put(name, ocr);
		}
		return ocr;
	}

	/**
	 * Read text from region basing on the glyphs data.
	 * 
	 * @param region - region to read text for
	 * @return Recognized text
	 */
	public String read(Region region) {

		if (region.getThrowException()) {
			region.setThrowException(false);
		}

		Map<Match, Glyph> mapping = new HashMap<>();
		List<Match> matches = new ArrayList<>();

		List<FutureTask<List<Match>>> futures = new ArrayList<>();
		CountDownLatch latch = new CountDownLatch(glyphs.size());

		for (Glyph g : glyphs) {
			FutureTask<List<Match>> future = new FutureTask<>(new ParallelMatcher(g, region, mapping, latch));
			futures.add(future);
			executor.execute(future);
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (FutureTask<List<Match>> future : futures) {
			try {
				matches.addAll(future.get());
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		Collections.sort(matches, new MatchesComparator());

		StringBuilder sb = new StringBuilder();
		for (Match m : matches) {
			sb.append(mapping.get(m).getCharacter());
		}

		return sb.toString();
	}

	/**
	 * Test
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws FindFailed
	 */
	public static void main(String[] args) throws InterruptedException, FindFailed {
		Region region = new Region(0, 0, 700, 600);
		region.highlight(5.0f);
		OCR ocr = OCR.getSpec("numbers");
		System.out.println(ocr.read(region));
	}
}
