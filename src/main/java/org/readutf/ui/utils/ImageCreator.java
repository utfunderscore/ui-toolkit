package org.readutf.ui.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageCreator {

    /**
     * Creates a blank PNG image with the specified width and height
     * and returns it as an InputStream.
     *
     * @param width  The width of the image in pixels
     * @param height The height of the image in pixels
     * @return InputStream containing the PNG image data
     */
    public static InputStream createBlankPngAsStream(int width, int height) {
        try {
            // Create a transparent buffered image
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setPaint(new Color(255, 255, 255, 0));  // Transparent background
            graphics.fillRect(0, 0, width, height);
            graphics.dispose();

            // Write the image to a byte array output stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();

            // Convert byte array output stream to input stream
            return new ByteArrayInputStream(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Example usage
    public static void main(String[] args) {
        InputStream blankPngAsStream = createBlankPngAsStream(400, 400);
        // write to file

    }
}
