package com.iitb.lokavidya.core.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UIUtils {

	public UIUtils() {
		// TODO Auto-generated constructor stub
	}

	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	public static ImageIcon getThumbnail(String path) {
		Icon icon = new ImageIcon(path);
		ImageIcon img = (ImageIcon) icon;
		int height = icon.getIconHeight(), width = icon.getIconWidth();
		if(width > height) {
			while (width > 125) {
				height = height * (110) / width;
				width = 110;
			}
		} else {
			while (height > 125) {
				width = width * (110) / height;
				height = 110;
			}
		}
		
		Image scaleImage = img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
		return new ImageIcon(scaleImage);
	}

	public static ImageIcon getSmallImage(String path) {
		Icon icon = new ImageIcon(path);
		ImageIcon img = (ImageIcon) icon;
		int height = icon.getIconHeight(), width = icon.getIconWidth();
		while (width > 125) {
			height = height * (70) / width;
			width = 70;
		}
		Image scaleImage = img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
		return new ImageIcon(scaleImage);
	}

	public static ImageIcon getPreview(String path, int screenWidth, int screenHeight) {
		ImageIcon img = new ImageIcon(path);
		Icon icon = new ImageIcon(path);
		// ImageIcon img=(ImageIcon)icon;
		int width = icon.getIconWidth(), height = icon.getIconHeight();
		int finalWidth = width;
		int finalHeight = height;
		if (width > screenWidth && height > screenHeight) {

			double widthRatio = (double) width / (double) screenWidth;
			double heightRatio = (double) height / (double) screenHeight;
			if (widthRatio >= heightRatio) {
				finalWidth = screenWidth;
				finalHeight = (int) ((double) height / (double) widthRatio);
				System.out.println("YES " + finalHeight + " " + height + " " + widthRatio);
			} else {
				finalWidth = (int) ((double) width / (double) heightRatio);
				finalHeight = screenHeight;
			}

		} else if (width > screenWidth) {
			double widthRatio = (double) width / (double) screenWidth;
			finalWidth = screenWidth;
			finalHeight = (int) ((double) height / (double) widthRatio);
		} else if (height > screenHeight) {
			double heightRatio = (double) height / (double) screenHeight;
			finalWidth = (int) ((double) width / (double) heightRatio);
			finalHeight = screenHeight;
		} else {
			double widthRatio = (double) screenWidth / (double) width;
			double heightRatio = (double) screenHeight / (double) height;
			if (widthRatio >= heightRatio) {
				finalWidth = (int) ((double) screenWidth * (double) heightRatio);
				finalHeight = screenHeight;
			} else {
				finalWidth = screenWidth;
				finalHeight = (int) ((double) screenHeight * (double) widthRatio);
			}
		}
		/*
		 * while(width>480){ height=height*(480)/width; width=480; }
		 */
		System.out.println("" + finalWidth + " " + finalHeight + " " + width + " " + height + " " + screenWidth + " "
				+ screenHeight);
		Image scaleImage = img.getImage().getScaledInstance(finalWidth, finalHeight, Image.SCALE_SMOOTH);
		return new ImageIcon(scaleImage);
	}

	public static BufferedImage createBufferedImage(String imgPath) {
//		File img = new File(imgPath);
//		BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
//		
//		try {
//			image = ImageIO.read(img);
//			image = resize(image, 800, 600);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return image;
		try {
			return ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
		}

	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

}
