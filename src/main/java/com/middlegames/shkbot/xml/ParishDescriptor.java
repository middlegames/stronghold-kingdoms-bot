package com.middlegames.shkbot.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.middlegames.shkbot.Parish;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parish")
public class ParishDescriptor {

	@XmlAttribute
	private String name = null;
	
	@XmlAttribute
	private boolean steward = false;
	
	private transient Parish parish = null;
	
	private transient Path path = null;
	
	@Override
	public String toString() {
		return name + (steward ? " (steward)" : "");
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	public Parish toObject() {
		if (parish == null) {
			try {
				String file = path.toRealPath() + "\\select.png";
				if (!Files.exists(Paths.get(file))) {
					throw new RuntimeException("Missing select.png file for parish " + this + " in directory " + path);
				}
				parish = new Parish(file);
				parish.setName(name);
				parish.setSteward(steward);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return parish;
	}

	public boolean isSteward() {
		return steward;
	}
}
