package de.ralfhergert.telemetry.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageBuilder {

	public static Image createSingleColoredImage(int width, int height, Color color) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setBackground(color);
		g.clearRect(0, 0, width, height);
		return image;
	}
}
