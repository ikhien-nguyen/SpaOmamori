package com.spa.paymentservice.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * Xac thuc token JWT do user-service phat hanh (dung chung jwt.signerKey).
 * PHAI verify chu ky bang MACVerifier - chi SignedJWT.parse() (khong verify)
 * la lo bao mat nghiem trong, cho phep gia mao token tuy y.
 */
@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            MACVerifier verifier = new MACVerifier(signerKey.getBytes());
            if (!signedJWT.verify(verifier)) {
                throw new JwtException("Invalid token signature");
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime == null || expirationTime.before(new Date())) {
                throw new JwtException("Token expired");
            }

            return new Jwt(
                    token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    expirationTime.toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims());

        } catch (ParseException | JOSEException e) {
            throw new JwtException("Invalid token");
        }
    }
}
