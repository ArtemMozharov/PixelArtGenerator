package image_processing;

import java.awt.image.BufferedImage;

public class ImageManipulator {
    private final int width;
    private final int height;
    private int[][] resizedImage;
    private final int[][] initialImage;
    private int resizeIndex;
    private double aspectRatio;
    private ConvertingMethod convertingMethod;
    public enum ConvertingMethod  {
            BLOCKING,
        BILINEAR,
        DOWNSAMPLE
    };

    // Constructors
    public ImageManipulator(BufferedImage image, int width, ConvertingMethod convertingMethod, double contrastfactor) {
        this.width = width;
        aspectRatio = (double) image.getWidth() / (double) image.getHeight();
        this.height = (int) ((double) width / aspectRatio);
        this.initialImage = ImageProcessing.convertToGrayScaleArray(image);
        this.resizeIndex = image.getHeight() / height;
        this.convertingMethod = convertingMethod;
        this.resizedImage = new int[height][width];
        this.resizeImage();
        resizedImage = ImageProcessing.changeContrast(resizedImage, contrastfactor);

    }

    public ImageManipulator(BufferedImage image) {
        this(image, 300, ConvertingMethod.DOWNSAMPLE, 1);
    }
    public ImageManipulator(BufferedImage image, int width) {
        this(image, width, ConvertingMethod.DOWNSAMPLE, 1);
    }

    public ImageManipulator(BufferedImage image, int width, double contrastfactor) {
        this(image, width, ConvertingMethod.DOWNSAMPLE, contrastfactor);
    }



    private void resizeImage() {
        switch (convertingMethod) {
            case BLOCKING: resizeBlocking();
            case BILINEAR: resizeBilinear();
            case DOWNSAMPLE: downSample();
        }
    }

    private void resizeBilinear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float gx = x * ((float) initialImage[0].length / width);
                float gy = y * ((float) initialImage.length / height);

                int gxi = (int) gx;
                int gyi = (int) gy;

                float dx = gx - gxi;
                float dy = gy - gyi;

                // Weighted average of the four surrounding pixels
                resizedImage[y][x] =
                        (int) ((1 - dx) * (1 - dy) * initialImage[gyi][gxi] +
                                                        dx * (1 - dy) * initialImage[gyi][gxi + 1] +
                                                        (1 - dx) * dy * initialImage[gyi + 1][gxi] +
                                                        dx * dy * initialImage[gyi + 1][gxi + 1]);
            }
        }
    }


    private void resizeBlocking(){
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                for(int i = y * resizeIndex; i < y * resizeIndex + resizeIndex && i < initialImage.length; i++) {
                    for(int j = x * resizeIndex; j < x * resizeIndex + resizeIndex && j < initialImage[0].length; j++) {
                        resizedImage[y][x] += initialImage[i][j];
                    }
                }
                resizedImage[y][x] /= resizeIndex;

            }
        }
    }

    private void downSample() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                resizedImage[y][x] = initialImage[y * resizeIndex][x * resizeIndex];
            }
        }
    }

    public int[][] getResizedImage() {
        return resizedImage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ImageToTextConverter Details:\n");
        sb.append("Original Image Dimensions: ").append(initialImage.length).append("x").append(initialImage[0].length).append("\n");
        sb.append("Resized Image Dimensions: ").append(height).append("x").append(width).append("\n");
        sb.append("Resize Index: ").append(resizeIndex).append("\n");
        sb.append("Aspect Ratio: ").append(aspectRatio).append("\n");
        return sb.toString();
    }
}
