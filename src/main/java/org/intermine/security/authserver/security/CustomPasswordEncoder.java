package org.intermine.security.authserver.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * This class implements the default passwordEncoder of Spring security.
 * CustomPasswordEncoder encodes and matches the raw password with
 * the help of encryption methods in Encryption class.
 *
 * @author Rahul Yadav
 *
 */
public class CustomPasswordEncoder implements PasswordEncoder {

    /**
     * <p>This method encodes the raw password using AES algorithm.
     * </p>
     *
     * @param rawPassword password to encode
     * @return hashed password
     */
    @Override
    public String encode(CharSequence rawPassword) {

        String hashed = null;
        try {
            hashed = Encryption.EncryptAESCBCPCKS5Padding(rawPassword.toString());

        } catch (InvalidKeyException | NoSuchAlgorithmException |
                NoSuchPaddingException | BadPaddingException |
                IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return hashed;
    }

    /**
     * <p>This method matches raw password and encoded password.
     * </p>
     *
     * @param rawPassword raw password
     * @param encodedPassword encoded password
     * @return boolean true if matches else false
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        try {
            String hashed = Encryption.EncryptAESCBCPCKS5Padding(rawPassword.toString());

            if (hashed.matches(encodedPassword)) {
                return true;
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException |
                NoSuchPaddingException | IllegalBlockSizeException |
                BadPaddingException | InvalidAlgorithmParameterException e) {
            return false;
        }
        return false;
    }
}
