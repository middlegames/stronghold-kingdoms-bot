package com.middlegames.shkbot.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.middlegames.shkbot.Parishes;
import com.middlegames.shkbot.Village;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "village")
public class VillageDescriptor {

	@XmlAttribute
	private String name = null;
	
	@XmlAttribute
	private String parish = null;
	
	private transient Path path = null;
	
	private transient Village village = null;
	
	@Override
	public String toString() {
		return name + " (" + parish + ")";
	}

	public String getName() {
		return name;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	public Village toObject() {
		if (village == null) {
			try {
				String file = path.toRealPath() + "\\select.png";
				if (!Files.exists(Paths.get(file))) {
					throw new RuntimeException("Missing select.png file for village " + this + " in directory " + path);
				}
				village = new Village(file, Parishes.get(parish));
				village.setName(name);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return village;
	}
}
