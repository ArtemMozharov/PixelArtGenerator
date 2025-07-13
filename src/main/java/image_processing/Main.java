package image_processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {




    public static void main(String[] args) throws IOException {
        String path = "src/main/resources/inputs/images/";
        BufferedImage img  = ImageIO.read(new File(path + "gruvbox_image40.png"));
        for (int i = 0; i < ImageToASCIIConverter.palettes.length; i++) {
          FileWriter fileWriter = new FileWriter(path + "ascii_palette_" + i + ".txt");
          ImageToASCIIConverter imageToASCIIConverter = new ImageToASCIIConverter(ImageToASCIIConverter.palettes[i], 3);
          char[][] text = imageToASCIIConverter.convertToASCII(img, 2, 1);
          for (int j = 0; j < text.length; j++) {
              fileWriter.write(text[j]);
              fileWriter.write('\n');
          }

          fileWriter.close();


      }






    }
}
