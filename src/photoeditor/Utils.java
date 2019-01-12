
package photoeditor;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Utils {
    
   public static Mat BufferedImageToMat(BufferedImage bimage)
{
    int width = bimage.getWidth();
    int height = bimage.getHeight();
    int type = CvType.CV_8UC3;
    Mat newMat = new Mat(height, width, type);
    WritableRaster raster;
    raster = bimage.getRaster();
    byte[] px = new byte[3];
    int[] rgb = new int[4];
    for (int y = 0; y < height; y++)
    {
        for (int x = 0; x < width; x++)
        {
            raster.getPixel(x, y, rgb);
            px[2] = (byte)rgb[0];    px[1] = (byte)rgb[1];
            px[0] = (byte)rgb[2];     newMat.put(y, x, px);
        }
    }
    return newMat;
}
   
  public static BufferedImage matToBufferedImage(Mat bgr) {
        int width = bgr.width();
        int height = bgr.height();
        BufferedImage image;
        WritableRaster raster;
        if (bgr.channels() == 1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            raster = image.getRaster();
            byte[] px = new byte[1];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    bgr.get(y, x, px);
                    raster.setSample(x, y, 0, px[0]);
                }
            }
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            raster = image.getRaster();
            byte[] px = new byte[3];
            int[] rgb = new int[4];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    bgr.get(y, x, px);
                    rgb[0] = px[2];
                    rgb[1] = px[1];
                    rgb[2] = px[0];
                    raster.setPixel(x, y, rgb);
                }
            }
        }
        return image;
    }
  
  
}
