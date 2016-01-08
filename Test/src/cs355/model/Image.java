package cs355.model;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class Image {
    int width;
    int height;
    int[][] pixels;

    public Image(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = new int[height][width];

        int i = 0;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                this.pixels[r][c] = pixels[i];
                i += 3;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public BufferedImage getBufferedImage() {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr = bi.getRaster();
        int[] pixels = new int[width*height];

        int i = 0;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                pixels[i++] = this.pixels[r][c];
            }
        }
        wr.setPixels(0, 0, width, height, pixels);
        bi.setData(wr);
        return bi;
    }

    public void adjustBrightness(int amount) {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                pixels[r][c] += amount;
                if (pixels[r][c] > 255) {
                    pixels[r][c] = 255;
                }
            }
        }
    }

    public void adjustContrast(int amount) {
        double scalar = Math.pow((double) (amount+100) / 100.00, 4);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                double p = (double) pixels[r][c];
                pixels[r][c] = (int) (scalar*(p-128)+128);
                if (pixels[r][c] > 255) {
                    pixels[r][c] = 255;
                } else if (pixels[r][c] < 0) {
                    pixels[r][c] = 0;
                }
            }
        }
    }

    public void uniformBlur() {
        int[][] newPixels = new int[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int pr = Math.max(r-1, 0);
                int pc = Math.max(c-1, 0);
                int nr = Math.min(r+1, height-1);
                int nc = Math.min(c+1, width-1);

                int sum = pixels[pr][pc] +
                          pixels[pr][c ] +
                          pixels[pr][nc] +
                          pixels[r ][pc] +
                          pixels[r ][c ] +
                          pixels[r ][nc] +
                          pixels[nr][pc] +
                          pixels[nr][c ] +
                          pixels[nr][nc];

                newPixels[r][c] = sum/9;
            }
        }

        pixels = newPixels;
    }

    public void medianFilter() {
        int[][] newPixels = new int[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int pr = Math.max(r-1, 0);
                int pc = Math.max(c-1, 0);
                int nr = Math.min(r+1, height-1);
                int nc = Math.min(c+1, width-1);

                int[] vals = new int[]{
                    pixels[pr][pc],
                    pixels[pr][c ],
                    pixels[pr][nc],
                    pixels[r ][pc],
                    pixels[r ][c ],
                    pixels[r ][nc],
                    pixels[nr][pc],
                    pixels[nr][c ],
                    pixels[nr][nc]
                };
                Arrays.sort(vals);

                newPixels[r][c] = vals[4];
            }
        }

        pixels = newPixels;
    }

    public void sharpen() {
        int[][] newPixels = new int[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int pr = Math.max(r-1, 0);
                int pc = Math.max(c-1, 0);
                int nr = Math.min(r+1, height-1);
                int nc = Math.min(c+1, width-1);

                int sum = -pixels[pr][c ] +
                        -pixels[r ][pc] +
                        6*pixels[r ][c ] +
                        -pixels[r ][nc] +
                        -pixels[nr][c ];

                newPixels[r][c] = sum/2;

                if (newPixels[r][c] > 255) {
                    newPixels[r][c] = 255;
                } else if (newPixels[r][c] < 0) {
                    newPixels[r][c] = 0;
                }
            }
        }

        pixels = newPixels;
    }

    public void detectEdges() {
        int[][] newPixels = new int[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int pr = Math.max(r-1, 0);
                int pc = Math.max(c-1, 0);
                int nr = Math.min(r+1, height-1);
                int nc = Math.min(c+1, width-1);

                int x = -pixels[pr][pc] +
                        pixels[pr][nc] +
                        -2*pixels[r][pc] +
                        2*pixels[r][nc] +
                        -pixels[nr][pc] +
                        pixels[nr][nc];

                int y = pixels[pr][pc] +
                        2*pixels[pr][c] +
                        pixels[pr][nc] +
                        -pixels[nr][pc] +
                        -2*pixels[nr][c] +
                        -pixels[nr][nc];

                newPixels[r][c] = (int) Math.sqrt(x*x + y*y);

                if (newPixels[r][c] > 255) {
                    newPixels[r][c] = 255;
                } else if (newPixels[r][c] < 0) {
                    newPixels[r][c] = 0;
                }
            }
        }

        pixels = newPixels;
    }
}
