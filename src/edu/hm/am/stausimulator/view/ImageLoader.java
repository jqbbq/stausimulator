package edu.hm.am.stausimulator.view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import edu.hm.am.stausimulator.view.model.Cell;

public class ImageLoader {

	private static final String PREFIX = "images/";

	private static Map<String, Image> images;

	private static boolean isInited = false;

	private static void init() {
		images = new HashMap<>();
		isInited = true;

		// preload Images
		load("car.png");
		load("car2.png");
		load("step.png");
		load("play.png");
		load("stop.png");

		put("car.png", Scalr.resize((BufferedImage) get("car.png"), (int) Math.floor(Cell.CELL_WIDTH * 0.8), (int) Math.floor(Cell.CELL_HEIGHT * 0.8)));
		put("car2.png", Scalr.resize((BufferedImage) get("car2.png"), (int) Math.floor(Cell.CELL_WIDTH * 0.8), (int) Math.floor(Cell.CELL_HEIGHT * 0.8)));
		put("step.png", Scalr.resize((BufferedImage) get("step.png"), 20, 20));
		put("play.png", Scalr.resize((BufferedImage) get("play.png"), 20, 20));
		put("stop.png", Scalr.resize((BufferedImage) get("stop.png"), 20, 20));
	}

	public static boolean load(String name) {
		if (!isInited) {
			init();
		}
		try {
			images.put(name, ImageIO.read(new File(PREFIX + name)));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Image get(String name) {
		if (!isInited) {
			init();
		}
		if (images.get(name) == null) {
			load(name);
		}
		return images.get(name);
	}

	public static void put(String name, Image image) {
		if (!isInited) {
			init();
		}
		images.put(name, image);
	}

}
