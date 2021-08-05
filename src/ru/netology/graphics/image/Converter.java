package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    protected int maxHeight;
    protected int maxWidth;
    protected double maxRatio;
    protected TextColorSchema colorSchema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int[] imgRes = getImgResolution(img);
        int imgW = imgRes[0];
        int imgH = imgRes[1];
        char[][] chars = convertImgToChars(img, imgW, imgH);
        return convertCharsToString(chars, imgW, imgH);
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        colorSchema = schema;
    }

    private int[] getImgResolution(BufferedImage img) throws BadImageSizeException {
        double imgW = img.getWidth();
        double imgH = img.getHeight();
        int newImgW;
        int newImgH;
        double wRatio = imgH / imgW;
        if (wRatio > maxRatio) {
            throw new BadImageSizeException(wRatio, maxRatio);
        }
        if (imgW > maxWidth || imgH > maxHeight) {
            newImgH = (int) (imgH / (imgW / maxWidth));
            newImgW = (int) (imgW / (imgH / maxHeight) * wRatio);
        } else {
            newImgW = (int) imgW;
            newImgH = (int) imgH;
        }
        int[] imgSizes = new int[2];
        imgSizes[0] = newImgW;
        imgSizes[1] = newImgH;
        return imgSizes;
    }

    private char[][] convertImgToChars(BufferedImage img, int imgW, int imgH) {
        Image scaleImage = img.getScaledInstance(imgW, imgH, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(imgW, imgH, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaleImage, 0, 0, null);
        var bwRaster = bwImg.getRaster();
        char[][] chars = new char[imgW][imgH];
        Schema schema = new Schema();
        for (int w = 0; w < imgW; w++) {
            for (int h = 0; h < imgH; h++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                chars[w][h] = c;
            }
        }
        return chars;
    }

    private String convertCharsToString(char[][] chars, int imgW, int imgH) {
        StringBuilder builder = new StringBuilder();
        for (int h = 0; h < imgH; h++) {
            for (int w = 0; w < imgW; w++) {
                builder.append(chars[w][h]);
                builder.append(chars[w][h]);
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
