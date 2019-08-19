package org.intermine.security.authserver.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encryption class contains methods to encrypt and decrypt
 * data using some special algorithms and static keys.
 * These methods are used to encode user password, used to
 * generate client credentials.
 *
 * @author Rahul Yadav
 *
 */
public class Encryption {

    /**
     * A static key used to encrypt plain text.
     */
    private static String KEY = "430P2U55YZ289Q35";

    /**
     * An initialization vector (IV) an arbitrary number used along with a secret key for data encryption.
     */
    private static String IV = "3qpow9o0xaapfdjl";

    /**
     * <p>Encrypt the String using AES algorithm.
     * </p>
     *
     * @param plain data string.
     * @return Hex encoded encrypted string
     * @throws InvalidKeyException                Invalid key.
     * @throws InvalidAlgorithmParameterException Invalid Algorithm.
     * @throws NoSuchAlgorithmException           Didn't find Algorithm.
     * @throws NoSuchPaddingException             Didn't find Padding.
     * @throws IllegalBlockSizeException          Block padding Error.
     * @throws BadPaddingException                Padding Error.
     */
    public static String EncryptAESCBCPCKS5Padding(String plain)
            throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        byte[] key = KEY.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        byte[] iv = IV.getBytes();
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        // initialize the cipher for encrypt mode
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

        // encrypt the message
        byte[] encrypted = cipher.doFinal(plain.getBytes());
        return hexEncode(encrypted);
    }

    /**
     * <p>Decrypt the String which is encoded by AES algorithm
     * </p>
     *
     * @param encryptText data string .
     * @return Original String
     * @throws InvalidKeyException                Invalid key.
     * @throws InvalidAlgorithmParameterException Invalid Algorithm.
     * @throws NoSuchAlgorithmException           Didn't find Algorithm.
     * @throws NoSuchPaddingException             Didn't find padding.
     * @throws IllegalBlockSizeException          Block padding Error.
     * @throws BadPaddingException                Padding Error
     */
    public static String DecryptAESCBCPCKS5Padding(String encryptText)
            throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        byte[] key = KEY.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        byte[] iv = IV.getBytes();
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

        byte[] original = cipher.doFinal(hexDecode(encryptText));

        return new String(original);
    }

    /**
     * <p> Hex decodes a encrypted String.
     * </p>
     *
     * @param input String to decode
     * @return byte array
     */
    private static byte[] hexDecode(String input) {

        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException("Hex Decoder exception", e);
        }

    }

    /**
     * <p>Hex encodes a byte array. Returns an empty
     * string if the input array is null or empty.
     * </p>
     *
     * @param input bytes to encode
     * @return string containing hex representation of input byte array
     */
    private static String hexEncode(byte[] input) {

        if (input == null || input.length == 0) {
            return "";
        }

        int inputLength = input.length;
        StringBuilder output = new StringBuilder(inputLength * 2);

        for (byte anInput : input) {
            int next = anInput & 0xff;
            if (next < 0x10) {
                output.append("0");
            }

            output.append(Integer.toHexString(next));
        }

        return output.toString();
    }

    /**
     * <p>Encrypt the byte array data using SHA algorithm.
     * </p>
     *
     * @param plain byte array of plain text
     * @return String encrypted data
     */
    public static String SHA1(byte[] plain) throws NoSuchAlgorithmException {

        String enc;

        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();

//        md.update(plain.getBytes());
        md.update(plain);
        byte[] encPasswordByte = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte anEncPasswordByte : encPasswordByte) {
            sb.append(Integer.toString((anEncPasswordByte & 0xff) + 0x100, 16).substring(1));
        }
        enc = sb.toString();

        return enc;
    }
}
