
import com.barcodelib.barcode.QRCode;
import java.awt.Desktop;
import java.io.File;


public class teste {

    int udm = 0, resol = 72, rot = 0;
    float mi = 0.000f, md = 0.000f, ms = 0.000f, min = 0.000f, tam = 5.00f;
    
    public static void main(String[] args) {
        new teste().gerarQR("https://www.youtube.com/");
    }
    
    public void gerarQR(String d){
        try {
            QRCode c = new QRCode();
            c.setData(d);
            c.setDataMode(QRCode.MODE_BYTE);
            c.setUOM(udm);
            c.setLeftMargin(mi);
            c.setRightMargin(md);
            c.setTopMargin(ms);
            c.setBottomMargin(min);
            c.setResolution(resol);
            c.setRotate(rot);
            c.setModuleSize(tam);
            
            String arquivo =System.getProperty("user.home") + "/teste.gif";
            c.renderBarcode(arquivo);
            
            Desktop dk = Desktop.getDesktop();
            dk.open(new File(arquivo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
