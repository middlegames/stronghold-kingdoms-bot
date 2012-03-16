package com.middlegames.shkbot.ocr;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;


/**
 * Java wrapper for tesseract process. This shit doesn't 
 * work as it should ;( Have no idea how to configure 
 * Tesseract or change image to recognize text CORRECTLY, 
 * already tried to learn tesserat new patterns, but it 
 * still complains about the BOX file issues ;/
 * 
 * @author Middle Gamer (middlegamer)
 */
public class Tesseract {

	private static Screen screen = new Screen();

	private static String saveImage(Region region) {

		ScreenImage image = screen.capture(region);
		String text = null;

		File img = null;
		File tmp = null;
		try {
			img = new File(image.getFilename());
			tmp = File.createTempFile("sx-tess-", ".tif");

			BufferedImage bi = ImageIO.read(img);

			Graphics2D g2 = null;
			BufferedImage rescaled = new BufferedImage(
					bi.getWidth() * 3,
					bi.getHeight() * 3,
					BufferedImage.TYPE_BYTE_BINARY);
			g2 = rescaled.createGraphics();
			g2.setRenderingHint(
					RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(
					bi, 0, 0, 
					rescaled.getWidth(), 
					rescaled.getHeight(), null);
			g2.dispose();

			ImageIO.write(rescaled, "png", tmp);

			rescaled.flush();

			FileUtils.copyFile(tmp, new File("d:/usr/desktop/dddd.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(img);


		//		String command = "C:\\Program Files\\Tesseract-OCR\\tesseract.exe";
		//		ProcessBuilder pb = new ProcessBuilder(
		//				command, tmp.getPath(), tmp.getPath(), 
		//				"C:\\Program Files\\Tesseract-OCR\\tessdata\\configs\\nobatch", 
		//				"C:\\Program Files\\Tesseract-OCR\\tessdata\\configs\\skbot");
		//		try {
		//			pb.start();
		//			Thread.sleep(1000);
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
		//
		//		try {
		//			FileReader fr = new FileReader(tmp.getPath() + ".txt");
		//			BufferedReader br = new BufferedReader(fr);
		//			text = br.readLine();
		//			br.close();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}

		return text;
	}

	// b/w dithering
	public static BufferedImage processImage(BufferedImage inputImage) {

		// Create a binary image for the results of processing

		int w = inputImage.getWidth();
		int h = inputImage.getHeight();
		BufferedImage outputImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

		// Work on a copy of input image because it is modified by diffusion

		WritableRaster input = inputImage.copyData(null);
		WritableRaster output = outputImage.getRaster();

		final int threshold = 128;
		float value, error;

		for (int y = 0; y < h; ++y)
			for (int x = 0; x < w; ++x) {

				value = input.getSample(x, y, 0);

				// Threshold value and compute error

				if (value < threshold) {
					output.setSample(x, y, 0, 0);
					error = value;
				} else {
					output.setSample(x, y, 0, 1);
					error = value - 255;
				}

				// Spread error amongst neighbouring pixels

				if ((x > 0) && (y > 0) && (x < (w-1)) && (y < (h-1))) {
					value = input.getSample(x+1, y, 0);
					input.setSample(x+1, y, 0, clamp(value + 0.4375f * error));
					value = input.getSample(x-1, y+1, 0);
					input.setSample(x-1, y+1, 0, clamp(value + 0.1875f * error));
					value = input.getSample(x, y+1, 0);
					input.setSample(x, y+1, 0, clamp(value + 0.3125f * error));
					value = input.getSample(x+1, y+1, 0);
					input.setSample(x+1, y+1, 0, clamp(value + 0.0625f * error));
				}

			}
		return outputImage;

	}

	// Forces a value to a 0-255 integer range

	public static int clamp(float value) {
		return Math.min(Math.max(Math.round(value), 0), 255);
	}

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(3000);
		System.out.println(Tesseract.saveImage(new Region(520,335,78,328)));
	}
}
