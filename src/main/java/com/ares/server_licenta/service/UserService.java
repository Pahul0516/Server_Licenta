package com.ares.server_licenta.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Base64;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class UserService {

    // Your exact Supabase URL matching the "iss" claim in your token
    private final String supabaseUrl = "https://dqxlpviizbfilkinbsda.supabase.co";

    public UUID getUserIdFromToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header format.");
        }

        String token = bearerToken.replace("Bearer ", "").trim();

        try {
            // 1. Decode the token to inspect the Key ID (kid)
            DecodedJWT jwt = JWT.decode(token);
            String kid = jwt.getKeyId();

            // 2. Fetch the public key properties (x, y coordinates) from Supabase's JWKS endpoint
            ECPublicKey publicKey = fetchECPublicKeyFromJwks(kid);

            // 3. Initialize the verifier explicitly for ECDSA P-256 (ES256)
            Algorithm algorithm = Algorithm.ECDSA256(publicKey, null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(supabaseUrl + "/auth/v1")
                    .withAudience("authenticated")
                    .build();

            // 4. Verify signature and timestamps cryptographically
            DecodedJWT verifiedJwt = verifier.verify(token);

            // 5. Return the true user UUID
            return UUID.fromString(verifiedJwt.getSubject());

        } catch (Exception e) {
            throw new RuntimeException("JWT verification failed: " + e.getMessage(), e);
        }
    }

    private ECPublicKey fetchECPublicKeyFromJwks(String kid) throws Exception {
        URL url = new URL(supabaseUrl + "/auth/v1/.well-known/jwks.json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        JSONObject jwks = new JSONObject(result.toString());
        JSONArray keys = jwks.getJSONArray("keys");
        JSONObject targetKey = null;

        for (int i = 0; i < keys.length(); i++) {
            if (keys.getJSONObject(i).getString("kid").equals(kid)) {
                targetKey = keys.getJSONObject(i);
                break;
            }
        }

        if (targetKey == null) {
            throw new IllegalArgumentException("No matching public key found for kid: " + kid);
        }

        // Decode the X and Y coordinates from the Supabase public key
        byte[] xBytes = Base64.getUrlDecoder().decode(targetKey.getString("x"));
        byte[] yBytes = Base64.getUrlDecoder().decode(targetKey.getString("y"));

        BigInteger x = new BigInteger(1, xBytes);
        BigInteger y = new BigInteger(1, yBytes);

        // Hardcoded mathematical parameters of the standard NIST P-256 (secp256r1) Elliptic Curve
        BigInteger p = new BigInteger("ffffffff00000001000000000000000000000000ffffffffffffffffffffffff", 16);
        BigInteger a = new BigInteger("ffffffff00000001000000000000000000000000fffffffffffffffffffffffc", 16);
        BigInteger b = new BigInteger("5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b", 16);

        // Generator coordinates (G)
        BigInteger gx = new BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16);
        BigInteger gy = new BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16);

        // Order (n) and cofactor (h)
        BigInteger n = new BigInteger("ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632551", 16);
        int h = 1;

        // Reconstruct the curve structure explicitly in Java memory
        ECFieldFp field = new ECFieldFp(p);
        EllipticCurve curve = new EllipticCurve(field, a, b);
        ECPoint g = new ECPoint(gx, gy);
        ECParameterSpec ecParameters = new ECParameterSpec(curve, g, n, h);

        // Build the final public key specification using coordinates
        ECPoint w = new ECPoint(x, y);
        ECPublicKeySpec keySpec = new ECPublicKeySpec(w, ecParameters);

        KeyFactory kf = KeyFactory.getInstance("EC");
        return (ECPublicKey) kf.generatePublic(keySpec);
    }

}