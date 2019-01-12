/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoeditor;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static photoeditor.Utils.matToBufferedImage;
import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_16S;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;

/**
 *
 * @author Aluno
 */
public class TelaPrincipalController implements Initializable
{

    @FXML
    private HBox statuspanel;
    @FXML
    private ImageView imgv;

    private Image imagem = null;
    //  private ArrayList<String> str = new String() 
    private final ArrayList<Image> aux_imagem = new ArrayList();
    @FXML
    private BorderPane painelprincipal;
    @FXML
    private MenuItem evtPB;
    @FXML
    private MenuItem evtEH;
    @FXML
    private MenuItem evtEV;
    @FXML
    private Spinner<Integer> sbparametro;
    @FXML
    private Button bAplicar;
    private int op_funcao;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        sbparametro.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 21, 7, 2));
    }

    @FXML
    private void evtAbrir(ActionEvent event)
    {

        FileChooser fc = new FileChooser();
        fc.setTitle("Open Resource File");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.JPEG"),
                new ExtensionFilter("All Files", "*.*"),
                new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));

        fc.setInitialDirectory(new File("D:\\"));
        File arq = fc.showOpenDialog(null);

        if (arq != null)
        {
            imagem = new Image(arq.toURI().toString());
            aux_imagem.add(imagem);
            imgv.setFitWidth(imagem.getWidth());
            imgv.setFitHeight(imagem.getHeight());
            ((Stage) (painelprincipal.getScene().getWindow()))
                    .setTitle("MyPaintFx " + arq.getAbsolutePath());
        }
        imgv.setImage(imagem);
    }

    @FXML
    private void evtSalvar(ActionEvent event)
    {
        if (imagem != null)
        {
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("IMAGE files (*.png)", "*.png");
            fc.getExtensionFilters().add(extFilter);
            File file = fc.showSaveDialog(null);
            try
            {
                if (file != null)
                    ImageIO.write(SwingFXUtils.fromFXImage(imagem, null), "png", file);
            } catch (Exception e)
            {
            }
        } else
            JOptionPane.showMessageDialog(null, "Não há imagem para salvar!", "Erro", JOptionPane.ERROR_MESSAGE);

    }

    @FXML
    private void evtFechar(ActionEvent event)
    {
        Platform.exit();
    }

    @FXML
    private void evtTonsCinza(ActionEvent event)
    {
        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(imagem, null);
        int pixel[] =
        {
            0, 0, 0, 0
        };
        WritableRaster raster = bimg.getRaster();
        int cinza;
        for (int i = 0; i < bimg.getHeight(); i++)
        {
            for (int j = 0; j < bimg.getWidth(); j++)
            {
                raster.getPixel(j, i, pixel);
                cinza = (int) (pixel[0] * 0.21 + pixel[1] * 0.72 + pixel[2] * 0.07);
                pixel[0] = pixel[1] = pixel[2] = cinza;
                raster.setPixel(j, i, pixel);
            }
        }
        imagem = SwingFXUtils.toFXImage(bimg, null);
        imgv.setImage(imagem);
        Image aux = SwingFXUtils.toFXImage(bimg, null);
        aux_imagem.add(aux);

    }

    @FXML
    private void evtPretoBranco(ActionEvent event)
    {
        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(imagem, null);
        int pixel[] =
        {
            0, 0, 0, 0
        };
        WritableRaster raster = bimg.getRaster();

        for (int i = 0; i < bimg.getHeight(); i++)
        {
            for (int j = 0; j < bimg.getWidth(); j++)
            {
                raster.getPixel(j, i, pixel);
                if (pixel[0] + pixel[1] + pixel[2] > 127)
                    pixel[0] = pixel[1] = pixel[2] = 255;
                else
                    pixel[0] = pixel[1] = pixel[2] = 0;
                raster.setPixel(j, i, pixel);

            }
        }
        imagem = SwingFXUtils.toFXImage(bimg, null);
        imgv.setImage(imagem);
        Image aux = SwingFXUtils.toFXImage(bimg, null);
        aux_imagem.add(aux);

    }

    @FXML
    private void evtEspelhoHorizontal(ActionEvent event)
    {

        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(imagem, null);
        int pixel[] =
        {
            0, 0, 0, 0
        }, pixel_aux[] =
        {
            0, 0, 0, 0
        };

        WritableRaster raster = bimg.getRaster();

        for (int i = 0; i < bimg.getHeight(); i++)
        {
            for (int j = 0; j < bimg.getWidth() / 2; j++)
            {
                raster.getPixel(j, i, pixel);
                raster.getPixel(bimg.getWidth() - j - 1, i, pixel_aux);

                raster.setPixel(j, i, pixel_aux);
                raster.setPixel(bimg.getWidth() - j - 1, i, pixel);
            }
        }
        imagem = SwingFXUtils.toFXImage(bimg, null);
        imgv.setImage(imagem);
        Image aux = SwingFXUtils.toFXImage(bimg, null);
        aux_imagem.add(aux);
    }

    @FXML
    private void evtEspelhoVertical(ActionEvent event)
    {

        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(imagem, null);
        int pixel[] =
        {
            0, 0, 0, 0
        }, pixel_aux[] =
        {
            0, 0, 0, 0
        };

        WritableRaster raster = bimg.getRaster();

        for (int i = 0; i < bimg.getHeight() / 2; i++)
        {
            for (int j = 0; j < bimg.getWidth(); j++)
            {
                raster.getPixel(j, i, pixel);
                raster.getPixel(j, bimg.getHeight() - i - 1, pixel_aux);

                raster.setPixel(j, i, pixel_aux);
                raster.setPixel(j, bimg.getHeight() - i - 1, pixel);
            }
        }
        imagem = SwingFXUtils.toFXImage(bimg, null);
        imgv.setImage(imagem);
        Image aux = SwingFXUtils.toFXImage(bimg, null);
        aux_imagem.add(aux);
    }

    @FXML
    private void evtHistograma(ActionEvent event)
    {
        sbparametro.setVisible(false);
        bAplicar.setVisible(false);
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat equaImg = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        //Converte para cinza
        Imgproc.cvtColor(imgmat, imgmat, COLOR_BGR2GRAY);

        //Aplica a Equalizacao de Histograma
        Imgproc.equalizeHist(imgmat, equaImg);

        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(equaImg), null);
        imgv.setImage(imagem);
        Image aux = imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(equaImg), null);
        aux_imagem.add(aux);

    }

    private void mediana(int parametro)
    {
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat img_gray = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());
        Mat img_mediana = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        //converte para cinza
        Imgproc.cvtColor(imgmat, img_gray, COLOR_BGR2GRAY);

        //Mediana
        Imgproc.medianBlur(img_gray, img_mediana, parametro);

        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_mediana), null);
        imgv.setImage(imagem);
        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_mediana), null);
        aux_imagem.add(aux);
    }

    @FXML
    private void evtMediana(ActionEvent event)
    {
        sbparametro.setVisible(true);
        bAplicar.setVisible(true);
        sbparametro.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 11, 1, 2));
        op_funcao = 0;
    }

    private void laplaciano(int kernel_size)
    {

        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat img_gray = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());
        Mat img_laplace = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        int scale = 1;
        int delta = 0;

        //converte para cinza
        Imgproc.cvtColor(imgmat, img_gray, COLOR_BGR2GRAY);

        //Remove ruidos pelo borramento com o filtro Gausiano
        Imgproc.GaussianBlur(img_gray, img_gray, new Size(5, 5), 1, 1, Imgproc.BORDER_DEFAULT);

        //Aplica a funcao Laplaciana
        Mat abs_dst = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        Imgproc.Laplacian(img_gray, img_laplace, CV_16S, kernel_size, scale, delta, Imgproc.BORDER_DEFAULT);
        Core.convertScaleAbs(img_laplace, abs_dst);

        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(abs_dst), null);
        imgv.setImage(imagem);
        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(abs_dst), null);
        aux_imagem.add(aux);

    }

    @FXML
    private void evtLaplaciano(ActionEvent event)
    {
        sbparametro.setVisible(true);
        bAplicar.setVisible(true);
        op_funcao = 1;
        sbparametro.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 1, 1));

    }

    @FXML
    private void evtThreshold(ActionEvent event)
    {
        sbparametro.setVisible(false);
        bAplicar.setVisible(false);
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat img_gray = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        Mat img_grayThresh = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        //converte para cinza
        Imgproc.cvtColor(imgmat, img_gray, COLOR_BGR2GRAY);

        //threshold = 120, maxValue = 255, thresholdType = THRESH_BINARY
        Imgproc.threshold(img_gray, img_grayThresh, 120, 255, Imgproc.THRESH_BINARY);

        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_grayThresh), null);
        imgv.setImage(imagem);
        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_grayThresh), null);
        aux_imagem.add(aux);
    }

    @FXML
    private void evtMedia(ActionEvent event)
    {
        sbparametro.setVisible(true);
        bAplicar.setVisible(true);

        op_funcao = 2;
        sbparametro.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1, 1));

    }

    private void Media(int param)
    {
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat img_gray = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());
        Mat img_media = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        //converte para cinza
        Imgproc.cvtColor(imgmat, img_gray, COLOR_BGR2GRAY);

        //Mediana
        Imgproc.blur(img_gray, img_media, new Size(param, param));

        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_media), null);
        imgv.setImage(imagem);
        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_media), null);
        aux_imagem.add(aux);
    }

    @FXML
    private void evtGausiana(ActionEvent event)
    {
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat img_gray = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());
        Mat img_gaus = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        //converte para cinza
        Imgproc.cvtColor(imgmat, img_gray, COLOR_BGR2GRAY);

        //borramento Gaussiano 
        Imgproc.GaussianBlur(img_gray, img_gaus, new Size(5, 5), 10, 10, Imgproc.BORDER_DEFAULT);
        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_gray), null);
        imgv.setImage(imagem);

        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_gray), null);
        aux_imagem.add(aux);
    }

    @FXML
    private void evtAplicar(ActionEvent event)
    {

        switch (op_funcao)
        {
            case 0:
                mediana(sbparametro.getValue());
                break;
            case 1:
                laplaciano(sbparametro.getValue());
                break;
            case 2:
                Media(sbparametro.getValue());
                break;
            case 3:
                rotacao_scale(sbparametro.getValue());
                break;
        }

        sbparametro.setVisible(false);
        bAplicar.setVisible(false);
    }

    @FXML
    private void evtCanny(ActionEvent event)
    {
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat img_gray = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());
        Mat cannyImg = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        //converte para cinza
        Imgproc.cvtColor(imgmat, img_gray, COLOR_BGR2GRAY);

        //Canny edge detection
        Imgproc.Canny(img_gray, cannyImg, 50, 150);
        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(cannyImg), null);
        imgv.setImage(imagem);

        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(cannyImg), null);
        aux_imagem.add(aux);
    }

    private void rotacao_scale(double angle)
    {
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat warp_img = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        boolean flag = false;
        double scale = 1;

        if (angle == 30)
        {
            flag = true;
            scale = 0.62;
        } else if (angle == 90.0)
        {
            flag = true;
            scale = 0.65;
        } else if (angle == 270)
        {
            flag = true;
            scale = 0.65;
        }

        if (!flag)
        {

            if (angle > 0 & angle <= 60)
                scale = 0.55;
            else
                if (angle > 60 & angle < 90)
                    scale = 0.60;
                else
                    if (angle > 90 & angle <= 160)
                        scale = 0.55;
            if (angle > 180 & angle <= 220)
                scale = 0.58;
            else
                if (angle > 220 & angle < 270)
                    scale = 0.60;
                else
                    if (angle > 270 && angle <= 300)
                        scale = 0.55;
                    else
                        if (angle > 300 && angle <= 330)
                            scale = 0.6;
        }
        Point center = new Point(imgmat.cols() / 2, imgmat.rows() / 2);

        //Obtem a matriz de rotacao
        Mat rot_mat = Imgproc.getRotationMatrix2D(center, angle, scale);
        //Rotaciona a image warp
        Imgproc.warpAffine(imgmat, warp_img, rot_mat, warp_img.size());

        //canvas.showImage(matToBufferedImage(warp_img));
        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(warp_img), null);
        imgv.setImage(imagem);
        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(warp_img), null);
        aux_imagem.add(aux);
    }

    @FXML
    private void evtRotacionar(ActionEvent event)
    {
        sbparametro.setVisible(true);
        bAplicar.setVisible(true);
        op_funcao = 3;
        sbparametro.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(30, 360, 30, 30));

    }

    @FXML
    private void evtCanelar(ActionEvent event)
    {

        if (aux_imagem.size() > 0)
        {
            imagem = aux_imagem.get(aux_imagem.size() - 2);
            aux_imagem.remove(aux_imagem.size() - 1);
            imgv.setImage(imagem);
        }
    }

    @FXML
    private void evtEfeitoAzul(ActionEvent event)
    {
        sbparametro.setVisible(false);
        bAplicar.setVisible(false);
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat img_gray = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        Imgproc.cvtColor(imgmat, img_gray, Imgproc.CHAIN_APPROX_TC89_KCOS);

        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_gray), null);
        imgv.setImage(imagem);

        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_gray), null);
        aux_imagem.add(aux);
    }

    @FXML
    private void evtEfeitoRoxo(ActionEvent event)
    {
        sbparametro.setVisible(false);
        bAplicar.setVisible(false);
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat img_gray = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        Imgproc.cvtColor(imgmat, img_gray, Imgproc.COLOR_BGR2Luv);
        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_gray), null);
        imgv.setImage(imagem);

        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_gray), null);
        aux_imagem.add(aux);
    }

    @FXML
    private void evtEfeitoVerde(ActionEvent event)
    {
        sbparametro.setVisible(false);
        bAplicar.setVisible(false);
        Mat imgmat = Utils.BufferedImageToMat(SwingFXUtils.fromFXImage(imagem, null));
        Mat img_gray = Mat.zeros(imgmat.rows(), imgmat.cols(), imgmat.type());

        Imgproc.cvtColor(imgmat, img_gray, Imgproc.COLOR_HLS2RGB_FULL);
        imagem = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_gray), null);
        imgv.setImage(imagem);

        Image aux = SwingFXUtils.toFXImage(Utils.matToBufferedImage(img_gray), null);
        aux_imagem.add(aux);

    }

}
