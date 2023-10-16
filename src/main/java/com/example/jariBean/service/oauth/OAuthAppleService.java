package com.example.jariBean.service.oauth;

import com.example.jariBean.config.jwt.JwtProcess;
import com.example.jariBean.dto.oauth.AppleOAuthInfo;
import com.example.jariBean.repository.TokenRepository;
import com.example.jariBean.repository.user.UserRepository;
import com.example.jariBean.service.oauth.OAuthKakaoService.SocialUserInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class OAuthAppleService extends OAuthService{

    private final String CLIENT_ID = "com.99degree.jari-bean";
    private final String REGISTRATION = "apple";

    @Value("${APPLE_KEY_ID}")
    private String APPLE_KEY_ID;

    @Value("${APPLE_ISS}")
    private String APPLE_ISS;

    @Value("${APPLE_AUTH_KEY}")
    private String APPLE_AUTH_KEY;

    public OAuthAppleService(UserRepository userRepository, TokenRepository tokenRepository, JwtProcess jwtProcess) {
        super(userRepository, tokenRepository, jwtProcess);
    }

    @Override
    public String getAccessToken(String code) {
        MultiValueMap<String, String> bodyValue = new LinkedMultiValueMap<>();
        bodyValue.add("client_id", CLIENT_ID);
        bodyValue.add("client_secret", createClientSecret());
        bodyValue.add("code", code);
        bodyValue.add("grant_type", "authorization_code");

        WebClient client = WebClient.create();
        return client.post()
                .uri("https://appleid.apple.com/auth/token")
                .contentType(APPLICATION_FORM_URLENCODED)
                .bodyValue(bodyValue)
                .retrieve()
                .bodyToMono(AppleOAuthInfo.class)
                .block()
                .getId_token();
    }

    @Override
    public SocialUserInfo getUserInfo(String accessToken) {

        // extract sub of jwt payload
        String encodedPayload = accessToken.split(("\\."))[1];
        byte[] payloadBytes = Base64.decodeBase64(encodedPayload);
        String payload = new String(payloadBytes);
        JSONObject jsonPayload = new JSONObject(payload);
        String sub = jsonPayload.get("sub").toString();

        return SocialUserInfo.create(
                REGISTRATION,
                sub,
                "Guest",
                "https://img.jari-bean.com/a0155280-ad92-4a29-9965-8f41b2aad98dVector.png"
        );
    }

    public String createClientSecret() {
        return Jwts.builder()
                // header
                .setHeaderParam("kid", APPLE_KEY_ID)
                .setHeaderParam("alg", "ES256")
                .setHeaderParam("typ", "JWT")
                // payload
                .setIssuer(APPLE_ISS)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .setAudience("https://appleid.apple.com")
                .setSubject("com.99degree.jari-bean")
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {

        APPLE_AUTH_KEY = APPLE_AUTH_KEY.replace("\\n", "\n");
        Reader pemReader = new StringReader(APPLE_AUTH_KEY);

        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = null;
        try {
            object = (PrivateKeyInfo) pemParser.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            return converter.getPrivateKey(object);
        } catch (PEMException e) {
            throw new RuntimeException(e);
        }

    }
}
