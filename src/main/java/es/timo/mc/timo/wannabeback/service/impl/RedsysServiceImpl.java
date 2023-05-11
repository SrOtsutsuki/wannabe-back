package es.timo.mc.timo.wannabeback.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.timo.mc.timo.wannabeback.configuration.RedsysProperties;
import es.timo.mc.timo.wannabeback.model.dto.PurchaseDto;
import es.timo.mc.timo.wannabeback.model.dto.RedsysParamsDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.service.RedsysService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Redsys service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class RedsysServiceImpl implements RedsysService {

    /**
     * The Redsys properties.
     */
    private final RedsysProperties redsysProperties;

    /**
     * The Ocho.
     */
    private final short OCHO = 8;

    /**
     * The Iv.
     */
    private final byte[] IV = {0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * Generate purchase redsys params dto.
     *
     * @param purchaseDto the purchase dto
     * @return the redsys params dto
     * @throws WannabeBackException the wannabe back exception
     */
    @Override
    public RedsysParamsDto generatePurchase(PurchaseDto purchaseDto) throws WannabeBackException {

        Map<String, String> params = generateParams(purchaseDto);
        RedsysParamsDto redsysParamsDto = new RedsysParamsDto();
        try {
            redsysParamsDto.setDsMerchantParameters(createMerchantParameters(params));
            redsysParamsDto.setDsSignature(createMerchantSignature(redsysProperties.getSignature(), params));
            log.info("Datos de redsys generados: {}", redsysParamsDto);
            return redsysParamsDto;
        } catch (Exception e) {
            log.error("Error al generar los datos de pago de redsys: {}" + e.getCause());
            throw new WannabeBackException("Error al generar los datos de pago de redsys", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Validate redsys params string.
     *
     * @param redsysParamsDto the redsys params dto
     * @return the string
     * @throws Exception the exception
     */
    @Override
    public String validateRedsysParams(RedsysParamsDto redsysParamsDto) throws Exception {
        String decodec = decodeMerchantParameters(redsysParamsDto.getDsMerchantParameters());
        //Se sacan los parametros
        Map<String, String> mapParams = new ObjectMapper().readValue(decodec, Map.class);
        String orderId = mapParams.get("Ds_Order");
        //Se calcula la firma
        String signatureCalculada = createMerchantSignatureNotif(redsysProperties.getSignature(), redsysParamsDto.getDsMerchantParameters(), orderId);
        if (signatureCalculada.equals(redsysParamsDto.getDsSignature())) {
            return orderId;
        } else {
            throw new WannabeBackException("Error en el calculo de la firma", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***************************** PRIVATE METHODS *********************************************/

    /**
     * Create merchant signature notif string.
     *
     * @param claveComercio  the clave comercio
     * @param merchantParams the merchant params
     * @param orderId        the order id
     * @return the string
     * @throws UnsupportedEncodingException       the unsupported encoding exception
     * @throws InvalidKeyException                the invalid key exception
     * @throws NoSuchAlgorithmException           the no such algorithm exception
     * @throws IllegalStateException              the illegal state exception
     * @throws NoSuchPaddingException             the no such padding exception
     * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
     * @throws IllegalBlockSizeException          the illegal block size exception
     * @throws BadPaddingException                the bad padding exception
     */
    private String createMerchantSignatureNotif(final String claveComercio, final String merchantParams, final String orderId) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] clave = decodeB64(claveComercio.getBytes("UTF-8"));
        String secretKc = toHexadecimal(clave, clave.length);
        byte[] secretKo = encrypt_3DES(secretKc, orderId);

        // Se hace el MAC con la clave de la operaci�n "Ko" y se codifica en BASE64
        byte[] hash = mac256(merchantParams, secretKo);
        byte[] res = encodeB64UrlSafe(hash);
        return new String(res, "UTF-8");
    }

    /**
     * Decode b 64 byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    private byte[] decodeB64(final byte[] data) {
        return Base64.decodeBase64(data);
    }

    /**
     * Encode b 64 url safe byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    private byte[] encodeB64UrlSafe(final byte[] data) {
        byte[] encode = Base64.encodeBase64(data);
        for (int i = 0; i < encode.length; i++) {
            if (encode[i] == '+') {
                encode[i] = '-';
            } else if (encode[i] == '/') {
                encode[i] = '_';
            }
        }
        return encode;
    }

    /**
     * Decode merchant parameters string.
     *
     * @param datos the datos
     * @return the string
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    private String decodeMerchantParameters(final String datos) throws UnsupportedEncodingException {
        byte[] res = decodeB64UrlSafe(datos.getBytes("UTF-8"));
        String params = new String(res, "UTF-8");
        return new String(res, "UTF-8");
    }

    /**
     * Generate params api mac sha 256.
     *
     * @param purchaseDto the purchase dto
     * @return the api mac sha 256
     */
    private Map<String, String> generateParams(PurchaseDto purchaseDto) {

        Map<String, String> params = new HashMap<>();

        String total = purchaseDto.formatTotal();
        String orderId = purchaseDto.getOrderId();

        log.info("Generado datos de redsys para la compra: {} , por un total de: {}, (datos en redsys: {}), orderId: {}", purchaseDto.getId(), purchaseDto.getTotal(), total, orderId);

        params.put("DS_MERCHANT_AMOUNT", total);
        params.put("DS_MERCHANT_CURRENCY", "978");
        params.put("DS_MERCHANT_MERCHANTCODE", redsysProperties.getMerchan_code());
        params.put("DS_MERCHANT_ORDER", orderId);
        params.put("DS_MERCHANT_TERMINAL", redsysProperties.getTerminal());
        params.put("DS_MERCHANT_TRANSACTIONTYPE", "0");
        params.put("DS_MERCHANT_PRODUCTDESCRIPTION", "COMPRA " +
                purchaseDto.getTicket().getBusiness().getName() +
                StringUtils.SPACE +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        params.put("DS_MERCHANT_URLKO", "http://wannabeapp.com/resultado_pago/ko");
        params.put("DS_MERCHANT_URLOK", "http://wannabeapp.com/resultado_pago/ok");
        return params;
    }

    /**
     * Create merchant parameters string.
     *
     * @param params the params
     * @return the string
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws JsonProcessingException      the json processing exception
     */
    private String createMerchantParameters(Map<String, String> params) throws UnsupportedEncodingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(params);
        String res = new String(Base64.encodeBase64(json.getBytes("UTF-8")), "UTF-8");
        return res;
    }

    /**
     * Decode b 64 url safe byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    public byte[] decodeB64UrlSafe(final byte[] data) {
        byte[] encode = Arrays.copyOf(data, data.length);
        for (int i = 0; i < encode.length; i++) {
            if (encode[i] == '-') {
                encode[i] = '+';
            } else if (encode[i] == '_') {
                encode[i] = '/';
            }
        }
        return Base64.decodeBase64(encode);
    }

    /**
     * Create merchant signature string.
     *
     * @param claveComercio the clave comercio
     * @param params        the params
     * @return the string
     * @throws UnsupportedEncodingException       the unsupported encoding exception
     * @throws InvalidKeyException                the invalid key exception
     * @throws NoSuchAlgorithmException           the no such algorithm exception
     * @throws IllegalStateException              the illegal state exception
     * @throws NoSuchPaddingException             the no such padding exception
     * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
     * @throws IllegalBlockSizeException          the illegal block size exception
     * @throws BadPaddingException                the bad padding exception
     * @throws JsonProcessingException            the json processing exception
     */
    private String createMerchantSignature(final String claveComercio, Map<String, String> params) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, JsonProcessingException {
        String merchantParams = createMerchantParameters(params);

        byte[] clave = Base64.decodeBase64(claveComercio.getBytes("UTF-8"));
        String secretKc = toHexadecimal(clave, clave.length);
        byte[] secretKo = encrypt_3DES(secretKc, params.get("DS_MERCHANT_ORDER"));

        // Se hace el MAC con la clave de la operaci�n "Ko" y se codifica en BASE64
        byte[] hash = mac256(merchantParams, secretKo);
        String res = new String(Base64.encodeBase64(hash), "UTF-8");
        return res;
    }

    /**
     * To hexadecimal string.
     *
     * @param datos    the datos
     * @param numBytes the num bytes
     * @return the string
     */
    private String toHexadecimal(byte[] datos, int numBytes) {
        String resultado = "";
        ByteArrayInputStream input = new ByteArrayInputStream(datos, 0, numBytes);
        String cadAux;
        int leido = input.read();
        while (leido != -1) {
            cadAux = Integer.toHexString(leido);
            if (cadAux.length() < 2)// Hay que a�adir un 0
                resultado += "0";
            resultado += cadAux;
            leido = input.read();
        }
        return resultado;
    }

    /**
     * Encrypt 3 des byte [ ].
     *
     * @param claveHex the clave hex
     * @param datos    the datos
     * @return the byte [ ]
     * @throws InvalidKeyException                the invalid key exception
     * @throws NoSuchAlgorithmException           the no such algorithm exception
     * @throws NoSuchPaddingException             the no such padding exception
     * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
     * @throws UnsupportedEncodingException       the unsupported encoding exception
     * @throws IllegalBlockSizeException          the illegal block size exception
     * @throws BadPaddingException                the bad padding exception
     */
    private byte[] encrypt_3DES(final String claveHex, final String datos) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        byte[] ciphertext = null;
        // Crea la clave
        DESedeKeySpec desKeySpec = new DESedeKeySpec(toByteArray(claveHex));
        SecretKey desKey = new SecretKeySpec(desKeySpec.getKey(), "DESede");
        // Crea un cifrador
        Cipher desCipher = Cipher.getInstance("DESede/CBC/NoPadding");

        // Inicializa el cifrador para encriptar
        desCipher.init(Cipher.ENCRYPT_MODE, desKey, new IvParameterSpec(IV));

        // Se a�aden los 0 en bytes necesarios para que sea un m�ltiplo de 8
        int numeroCerosNecesarios = OCHO - (datos.length() % OCHO);
        if (numeroCerosNecesarios == OCHO) {
            numeroCerosNecesarios = 0;
        }
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        array.write(datos.getBytes("UTF-8"), 0, datos.length());
        for (int i = 0; i < numeroCerosNecesarios; i++) {
            array.write(0);
        }
        byte[] cleartext = array.toByteArray();
        // Encripta el texto
        ciphertext = desCipher.doFinal(cleartext);
        return ciphertext;
    }

    /**
     * To byte array byte [ ].
     *
     * @param cadena the cadena
     * @return the byte [ ]
     */
    private byte[] toByteArray(String cadena) {
        //Si es impar se a�ade un 0 delante
        if (cadena.length() % 2 != 0)
            cadena = "0" + cadena;

        int longitud = cadena.length() / 2;
        int posicion = 0;
        String cadenaAux = null;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        for (int i = 0; i < longitud; i++) {
            cadenaAux = cadena.substring(posicion, posicion + 2);
            posicion += 2;
            salida.write((char) Integer.parseInt(cadenaAux, 16));
        }
        return salida.toByteArray();
    }

    /**
     * Mac 256 byte [ ].
     *
     * @param dsMerchantParameters the ds merchant parameters
     * @param secretKo             the secret ko
     * @return the byte [ ]
     * @throws NoSuchAlgorithmException     the no such algorithm exception
     * @throws InvalidKeyException          the invalid key exception
     * @throws IllegalStateException        the illegal state exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    private byte[] mac256(final String dsMerchantParameters, final byte[] secretKo) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
        // Se hace el MAC con la clave de la operaci�n "Ko" y se codifica en BASE64
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secretKo, "HmacSHA256");
        sha256HMAC.init(secretKey);
        byte[] hash = sha256HMAC.doFinal(dsMerchantParameters.getBytes("UTF-8"));
        return hash;
    }

}
