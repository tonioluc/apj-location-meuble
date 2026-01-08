import bean.CGenUtil;
import produits.Ingredients;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import utilitaire.UtilDB;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.util.Hashtable;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) throws Exception {
        generateBarcodes(args);
    }

    public static void generateBarcodes(String[] args) throws Exception {
        Connection connection = new UtilDB().GetConn();
        try {
            connection.setAutoCommit(false);
            Ingredients ingredient = new Ingredients();
            ingredient.setNomTable("as_ingredients");

            Ingredients[] Ingredientss = (Ingredients[]) CGenUtil.rechercher(
                    ingredient, null, null, connection, "");

            String path = args[0];
            String folder = args[1];

            for (Ingredients ingredients1 : Ingredientss) {
                String file_name = ingredients1.getId() + ".png";
                String barcodeText = ingredients1.getId();
                String filePath = path + folder + file_name;

                ingredients1.setFilepath(folder + file_name);
                ingredients1.updateToTable(connection);

                Hashtable<EncodeHintType, String> hintMap = new Hashtable<>();
                hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                
                int barcodeWidth = 1600;   
                int barcodeHeight = 400;  
                BitMatrix bitMatrix = new MultiFormatWriter().encode(
                        barcodeText, BarcodeFormat.CODE_128, barcodeWidth, barcodeHeight, hintMap);

                BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                int width = barcodeImage.getWidth();

                int height = barcodeImage.getHeight() + 150;  

                BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                Graphics2D g = combinedImage.createGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, width, height);

                int barcodeY = 20;
                g.drawImage(barcodeImage, 0, barcodeY, null);

                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 38));
                FontMetrics fontMetrics = g.getFontMetrics();

                int totalTextWidth = 0;
                for (char c : barcodeText.toCharArray()) {
                    totalTextWidth += fontMetrics.charWidth(c) + 5;
                }
                totalTextWidth -= 5;

                int x = (width - totalTextWidth) / 2;
                int y = barcodeY + barcodeImage.getHeight() + 50; 

                for (char c : barcodeText.toCharArray()) {
                    g.drawString(String.valueOf(c), x, y);
                    x += fontMetrics.charWidth(c) + 5;
                }
                g.dispose();
                ImageIO.write(combinedImage, "PNG", new File(filePath));
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            System.out.println("Erreur lors de la génération du code-barres : " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}