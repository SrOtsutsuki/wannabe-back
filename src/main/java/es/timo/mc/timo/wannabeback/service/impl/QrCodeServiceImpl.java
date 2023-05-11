package es.timo.mc.timo.wannabeback.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import es.timo.mc.timo.wannabeback.WannabeBackApplication;
import es.timo.mc.timo.wannabeback.model.dto.ReserveDto;
import es.timo.mc.timo.wannabeback.model.dto.ReserveListDto;
import es.timo.mc.timo.wannabeback.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The type Qr code service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    /**
     * The constant ext.
     */
    private static final String ext = "png";
    /**
     * The constant LOGO.
     */
    private static final String LOGO = "casinoLogo.png";
    /**
     * The constant WIDTH.
     */
    private static final int WIDTH = 550;
    /**
     * The constant HEIGHT.
     */
    private static final int HEIGHT = 550;

    /**
     * Generate byte [ ].
     *
     * @param reserveDto the reserve dto
     * @return the byte [ ]
     */
    public byte[] generate(ReserveDto reserveDto) {
        // Create new configuration that specifies the error correction
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            // Create a qr code with the url as content and a size of WxH px
            bitMatrix = writer.encode(reserveDto.getReserveCode(), BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);

            // Load QR image
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());

            // Load logo image
            BufferedImage overly = getOverly(LOGO);

            // Calculate the delta height and width between QR code and logo
            int deltaHeight = qrImage.getHeight() - overly.getHeight();
            int deltaWidth = qrImage.getWidth() - overly.getWidth();

            // Initialize combined image
            BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) combined.getGraphics();

            // Write QR code to new image at position 0/0
            g.drawImage(qrImage, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // Write logo into combine image at position (deltaWidth / 2) and
            // (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
            // the same space for the logo to be centered
            //g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

            // Write combined image as PNG to OutputStream
            ImageIO.write(combined, ext, os);
            // return img
            return os.toByteArray();
            //Files.copy( new ByteArrayInputStream(os.toByteArray()), Paths.get(DIR + generateRandoTitle(new Random(), 9) +ext), StandardCopyOption.REPLACE_EXISTING);

        } catch (WriterException e) {
            e.printStackTrace();
            //LOG.error("WriterException occured", e);
        } catch (IOException e) {
            e.printStackTrace();
            //LOG.error("IOException occured", e);
        }
        return os.toByteArray();
    }

    /**
     * Gets overly.
     *
     * @param LOGO the logo
     * @return the overly
     * @throws IOException the io exception
     */
    private static BufferedImage getOverly(String LOGO) throws IOException {
        InputStream is = WannabeBackApplication.class.getClassLoader().getResourceAsStream("img/" + LOGO);
        return ImageIO.read(is);
    }


    /**
     * Gets matrix config.
     *
     * @return the matrix config
     */
    private static MatrixToImageConfig getMatrixConfig() {
        // ARGB Colors
        // Check Colors ENUM
        return new MatrixToImageConfig(Colors.BLACK.getArgb(), Colors.WHITE.getArgb());
    }

    /**
     * Generate rando title string.
     *
     * @param random the random
     * @param length the length
     * @return the string
     */
    private static String generateRandoTitle(Random random, int length) {
        return random.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    /**
     * Generate qrs.
     *
     * @param reserveListDto the reserve list dto
     */
    public void generateQrs(ReserveListDto reserveListDto) {
        reserveListDto.getReserveOwner().setQrCode(new ByteArrayResource(generate(reserveListDto.getReserveOwner())));
        if (reserveListDto.getReserveFriends() != null && !reserveListDto.getReserveFriends().isEmpty()) {
            reserveListDto.getReserveFriends().forEach(reserveDtoWithQrCode -> {
                reserveDtoWithQrCode.setQrCode(new ByteArrayResource(generate(reserveDtoWithQrCode)));
            });
        }
    }

    /**
     * The enum Colors.
     */
    public enum Colors {

        /**
         * Blue colors.
         */
        BLUE(0xFF40BAD0),
        /**
         * Red colors.
         */
        RED(0xFFE91C43),
        /**
         * Purple colors.
         */
        PURPLE(0xFF8A4F9E),
        /**
         * Orange colors.
         */
        ORANGE(0xFFF4B13D),
        /**
         * White colors.
         */
        WHITE(0xFFFFFFFF),
        /**
         * Black colors.
         */
        BLACK(0xFF000000);

        /**
         * The Argb.
         */
        private final int argb;

        /**
         * Instantiates a new Colors.
         *
         * @param argb the argb
         */
        Colors(final int argb) {
            this.argb = argb;
        }

        /**
         * Get argb int.
         *
         * @return the int
         */
        public int getArgb() {
            return argb;
        }
    }
}
