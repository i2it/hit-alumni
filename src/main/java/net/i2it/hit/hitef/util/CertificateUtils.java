package net.i2it.hit.hitef.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 生成捐赠证书(pdf、图片)的工具类-->
 * 有如下方法：
 * ①填写pdf模板中的表单域生成新的pdf文件（itext)
 * ②将pdf转为图片格式（pdfbox）
 * <p>
 *
 * @author liuming
 * @date 2017/4/28
 */
public class CertificateUtils {

    private static String CER_FOLD_PATH;
    private static String IAMGE_FILE_PATH;

    static {
        try {
            CER_FOLD_PATH = URLDecoder.decode(CertificateUtils.class.getClassLoader().getResource("certifications/").getPath(), "utf-8");
            IAMGE_FILE_PATH = URLDecoder.decode(CertificateUtils.class.getClassLoader().getResource("donate-certificate.jpg").getPath(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void drawTextInImg(Map<String, String> data) {
        String part = "　　兹接受" + data.get("name") + "校友向哈尔滨工业大学教育发展基金会捐助人民币" + data.get("money") + "元。";
        String line1 = part.substring(0, 23);
        String line2 = part.substring(23);
        try {
            BufferedImage image = ImageIO.read(new File(IAMGE_FILE_PATH));
            Graphics graphics = image.getGraphics();
            graphics.setColor(new Color(141, 12, 7));
            graphics.setFont(new Font("华文中宋", Font.BOLD, 55));
            graphics.drawString(line1, 220, 550);
            graphics.drawString(line2, 220, 640);
            graphics.drawString("　　颁发此证，谨致谢忱。", 220, 730);
            graphics.setFont(new Font("华文中宋", Font.BOLD, 48));
            graphics.drawString("哈尔滨工业大学校友总会", 903, 820);
            graphics.drawString("哈尔滨工业大学教育发展基金会", 830, 910);
            graphics.drawString(data.get("date"), 985, 1000);
            ImageIO.write(image, "jpg", new File(CER_FOLD_PATH + data.get("out_trade_no") + ".jpg"));
            ImageIO.write(image, "jpg", new File(data.get("certificatePath") + "/" + data.get("out_trade_no") + ".jpg"));
            graphics.dispose();
            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
