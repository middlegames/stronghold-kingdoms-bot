package com.middlegames.shkbot.ocr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Read glyphs.
 * 
 * @author Middle Gamer (middlegamer)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "glyphs")
public class Glyphs {

	private static final Class<?>[] CLASSES = new Class[] {
		Glyphs.class,
		Glyph.class,
	};

	private static Glyphs instance = new Glyphs();

	private transient Unmarshaller unmarshaller = null;

	@XmlElement(name = "glyph")
	private List<Glyph> glyphs = null;

	public Glyphs() {
		try {
			JAXBContext context = JAXBContext.newInstance(CLASSES);
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static Glyphs getInstance() {
		return instance;
	}

	public List<Glyph> getGlyphs() {
		return glyphs;
	}

	public List<Glyph> load(String path) {
		Path p = Paths.get(path);
		if (p.toFile().isDirectory()) {
			return loadFromDir(p);
		} else {
			return loadFromGLP(p);
		}
	}

	private List<Glyph> loadFromDir(Path p) {

		String root = p.toAbsolutePath().toString();

		Object o = null;
		p = Paths.get(p + "/glyphs.xml");

		try {
			o = unmarshaller.unmarshal(Files.newInputStream(p));
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}

		List<Glyph> glyphs = ((Glyphs) o).getGlyphs();
		for (Glyph g : glyphs) {
			g.relativize(root);
		}

		return glyphs;
	}

	private static List<Glyph> loadFromGLP(Path p) {
		// TODO read glyps from glp file
		return null;
	}

	public static void main(String[] args) {
		for (Glyph g : Glyphs.getInstance().load("src/main/resources/numbers")) {
			System.out.println(g);
		}
	}
}
