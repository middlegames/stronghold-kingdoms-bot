package com.middlegames.shkbot.xml;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class PersistenceEngine {

	private static final Class<?>[] CLASSES = new Class[] {
		VillageDescriptor.class,
		ParishDescriptor.class,
	};

	private static PersistenceEngine instance = new PersistenceEngine();

	private List<VillageDescriptor> villages = null;
	private List<ParishDescriptor> parishes = null;

	private Unmarshaller unmarshaller = null;

	private PersistenceEngine() {
		try {
			JAXBContext context = JAXBContext.newInstance(CLASSES);
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static PersistenceEngine getInstance() {
		return instance;
	}

	public List<VillageDescriptor> getVillageDescriptors() {
		if (villages == null) {
			villages = new ArrayList<>();

			Path path = Paths.get("data/village");
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, "*")) {
				Iterator<Path> dsi = ds.iterator();
				while (dsi.hasNext()) {
					Path p = dsi.next();
					VillageDescriptor vd = (VillageDescriptor) unmarshaller.unmarshal(Paths.get(p + "/village.xml").toFile());
					vd.setPath(p);
					villages.add(vd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return villages;
	}

	public List<ParishDescriptor> getParishDescriptors() {
		if (parishes == null) {
			parishes = new ArrayList<>();

			Path path = Paths.get("data/parish");
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, "*")) {
				Iterator<Path> dsi = ds.iterator();
				while (dsi.hasNext()) {
					Path p = dsi.next();
					ParishDescriptor pd = (ParishDescriptor) unmarshaller.unmarshal(Paths.get(p + "/parish.xml").toFile());
					pd.setPath(p);
					parishes.add(pd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return parishes;
	}

	public static void main(String[] args) {
		List<VillageDescriptor> villages = new PersistenceEngine().getVillageDescriptors();
		for (VillageDescriptor vd : villages) {
			System.out.println(vd);
		}
	}
}
