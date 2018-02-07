package project.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Grayscale {
    public static BufferedImage Convert(String imageFile) {
        File img = new File(imageFile);
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(img);
        } catch (IOException ex) {}
        if (bf != null) {
            int width = bf.getWidth();
            int height = bf.getHeight();
            BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int color = bf.getRGB(i, j);
                    int blue = color & 0xff;
                    int green = (color & 0xff00) >> 8;
                    int red = (color & 0xff0000) >> 16;
                    int alpha = (color & 0xff000000) >> 24;
                    int grayed = (blue + green + red) / 3;
                    output.setRGB(i, j, grayed | grayed << 8 | grayed << 16 | alpha << 24);
                }
            }
            return output;
        }
        return null;
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\cosmin.stoica\\Downloads\\";
        String filename = "2017-07-28";
        String extension = "png";
        BufferedImage out = Grayscale.Convert(path + filename + '.' + extension);
        File imageFile = new File(path + filename + "-1." + extension);
        try {
            if (out != null) {
                ImageIO.write(out, "png", imageFile);
            }
        } catch (IOException ex) {}
    }
}
