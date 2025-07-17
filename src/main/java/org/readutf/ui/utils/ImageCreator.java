package org.readutf.ui.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
            graphics.setPaint(new Color(255, 255, 255, 0)); // Transparent background
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

    /**
     * Creates a tiled image of specified width using end and center pieces
     *
     * @param endPiece The image to use on both ends (4px wide)
     * @param centerPiece The image to repeat in the middle (4px wide)
     * @param totalWidth The total width of the desired output image
     * @return A new BufferedImage with the tiled pattern
     * @throws IllegalArgumentException if width is too small or images have different heights
     */
    public static BufferedImage createTiledImage(BufferedImage endPiece, BufferedImage centerPiece, int totalWidth) {
        // Validate inputs
        if (endPiece.getHeight() != centerPiece.getHeight()) {
            throw new IllegalArgumentException("End piece and center piece must have the same height");
        }

        int endWidth = endPiece.getWidth();
        int minWidth = endWidth * 2; // Need space for both ends

        if (totalWidth < minWidth) {
            throw new IllegalArgumentException("Total width must be at least " + minWidth + " pixels");
        }
        if(centerPiece.getWidth() != 1) {
            throw new IllegalArgumentException("Center piece must be 1 pixel wide");
        }

        int height = endPiece.getHeight();
        int centerWidth = centerPiece.getWidth();

        // Create the output image
        BufferedImage result = new BufferedImage(totalWidth, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();

        try {
            // Draw left end piece
            g2d.drawImage(endPiece, 0, 0, null);

            // Calculate space for center pieces
            int centerTotalWidth = totalWidth - (2 * endWidth);
            int centerPieceCount = (int) Math.ceil((double) centerTotalWidth / centerWidth);

            // Draw center pieces
            for (int i = 0; i < centerPieceCount; i++) {
                int x = endWidth + (i * centerWidth);
                g2d.drawImage(centerPiece, x, 0, null);
            }

            // Create flipped end piece
            BufferedImage flippedEndPiece = flipHorizontally(endPiece);

            // Draw right end piece
            g2d.drawImage(flippedEndPiece, totalWidth - endWidth, 0, null);

        } finally {
            g2d.dispose();
        }

        return result;
    }

    /**
     * Creates a horizontally flipped version of the input image
     *
     * @param image The image to flip
     * @return A new BufferedImage that is horizontally flipped
     */
    private static BufferedImage flipHorizontally(BufferedImage image) {
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
        transform.translate(-image.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    /**
     * Converts a BufferedImage to a PNG format stored in a byte array
     *
     * @param bufferedImage the image to convert
     * @return byte array containing the PNG image data
     * @throws IOException if an error occurs during conversion
     */
    public static byte[] toPngBuffer(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Write the BufferedImage to the output stream in PNG format
        ImageIO.write(bufferedImage, "png", outputStream);

        // Convert output stream to byte array and return
        return outputStream.toByteArray();
    }
}
