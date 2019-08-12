package org.intermine.security.authserver.security;

import org.intermine.security.authserver.service.SocialUserDetailsImpl;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CustomTokenConverter extends JwtAccessTokenConverter {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<String, Object>();
        SocialUserDetailsImpl user = (SocialUserDetailsImpl) authentication.getPrincipal();
        additionalInfo.put("name", user.getName());
        additionalInfo.put("email", user.getEmail());
        String mergeProfileId=authentication.getOAuth2Request().getRequestParameters().get("mergeProfile");
        additionalInfo.put("mergeProfileId",mergeProfileId);
        try {
            additionalInfo.put("sub", Encryption.EncryptAESCBCPCKS5Padding(user.getUserId()));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        accessToken = super.enhance(accessToken, authentication);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
        return accessToken;
    }
}
