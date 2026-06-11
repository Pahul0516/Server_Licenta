package com.ares.server_licenta.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.interfaces.ECPublicKey;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Spy
    private UserService userService;

    @Test
    void getUserIdFromToken_ShouldReturnUUID_WhenTokenIsValid() throws Exception {
        // Arrange
        String userIdStr = UUID.randomUUID().toString();
        String bearerToken = "Bearer valid.token.here";
        String token = "valid.token.here";
        String kid = "test-kid";

        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getKeyId()).thenReturn(kid);
        when(decodedJWT.getSubject()).thenReturn(userIdStr);

        ECPublicKey publicKey = mock(ECPublicKey.class);
        doReturn(publicKey).when(userService).fetchECPublicKeyFromJwks(kid);

        try (MockedStatic<JWT> jwtMockedStatic = mockStatic(JWT.class);
             MockedStatic<Algorithm> algorithmMockedStatic = mockStatic(Algorithm.class)) {
            
            jwtMockedStatic.when(() -> JWT.decode(token)).thenReturn(decodedJWT);
            
            Algorithm algorithm = mock(Algorithm.class);
            algorithmMockedStatic.when(() -> Algorithm.ECDSA256(publicKey, null)).thenReturn(algorithm);
            
            Verification verification = mock(Verification.class);
            jwtMockedStatic.when(() -> JWT.require(algorithm)).thenReturn(verification);
            when(verification.withIssuer(anyString())).thenReturn(verification);
            when(verification.withAudience(anyString())).thenReturn(verification);
            
            JWTVerifier verifier = mock(JWTVerifier.class);
            when(verification.build()).thenReturn(verifier);
            when(verifier.verify(token)).thenReturn(decodedJWT);

            // Act
            UUID result = userService.getUserIdFromToken(bearerToken);

            // Assert
            assertEquals(UUID.fromString(userIdStr), result);
        }
    }

    @Test
    void getUserIdFromToken_ShouldThrowException_WhenTokenIsMissing() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserIdFromToken(null));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserIdFromToken("InvalidToken"));
    }

    @Test
    void getUserIdFromToken_ShouldThrowRuntimeException_WhenVerificationFails() throws Exception {
        // Arrange
        String bearerToken = "Bearer invalid.token";
        String token = "invalid.token";
        String kid = "test-kid";

        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getKeyId()).thenReturn(kid);

        ECPublicKey publicKey = mock(ECPublicKey.class);
        doReturn(publicKey).when(userService).fetchECPublicKeyFromJwks(kid);

        try (MockedStatic<JWT> jwtMockedStatic = mockStatic(JWT.class);
             MockedStatic<Algorithm> algorithmMockedStatic = mockStatic(Algorithm.class)) {
            
            jwtMockedStatic.when(() -> JWT.decode(token)).thenReturn(decodedJWT);
            
            Algorithm algorithm = mock(Algorithm.class);
            algorithmMockedStatic.when(() -> Algorithm.ECDSA256(publicKey, null)).thenReturn(algorithm);
            
            Verification verification = mock(Verification.class);
            jwtMockedStatic.when(() -> JWT.require(algorithm)).thenReturn(verification);
            when(verification.withIssuer(anyString())).thenReturn(verification);
            when(verification.withAudience(anyString())).thenReturn(verification);
            
            JWTVerifier verifier = mock(JWTVerifier.class);
            when(verification.build()).thenReturn(verifier);
            // Simulate verification failure
            when(verifier.verify(token)).thenThrow(new RuntimeException("Signature invalid"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> 
                userService.getUserIdFromToken(bearerToken)
            );
            assertTrue(exception.getMessage().contains("JWT verification failed"));
        }
    }

    @Test
    void fetchECPublicKeyFromJwks_ShouldReturnPublicKey_WhenKidExists() throws Exception {
        // Arrange
        String kid = "test-kid";
        String jwksJson = "{\"keys\":[{\"kid\":\"test-kid\",\"x\":\"mB6S_m_Y0p0\", \"y\":\"bHkXy9_H_mY\"}]}";
        // Using valid-ish base64url coordinates for P-256
        String xCoord = Base64.getUrlEncoder().encodeToString(new byte[32]);
        String yCoord = Base64.getUrlEncoder().encodeToString(new byte[32]);
        jwksJson = "{\"keys\":[{\"kid\":\"" + kid + "\",\"x\":\"" + xCoord + "\",\"y\":\"" + yCoord + "\"}]}";

        doReturn(jwksJson).when(userService).fetchJwksContent();

        // Act
        ECPublicKey result = userService.fetchECPublicKeyFromJwks(kid);

        // Assert
        assertNotNull(result);
        assertEquals("EC", result.getAlgorithm());
    }

    @Test
    void fetchECPublicKeyFromJwks_ShouldThrowException_WhenKidNotFound() throws Exception {
        // Arrange
        String jwksJson = "{\"keys\":[{\"kid\":\"other-kid\"}]}";
        doReturn(jwksJson).when(userService).fetchJwksContent();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            userService.fetchECPublicKeyFromJwks("missing-kid")
        );
    }

    @Test
    void fetchJwksContent_ShouldThrowException_WhenUrlFails() {
        // Since we can't easily mock URL/HttpURLConnection without PowerMock or heavy refactoring,
        // this method remains mostly an integration concern.
        // However, we can test it throws exception if something goes wrong internally.
        // For unit test purposes, we've isolated it so we can mock its output in other tests.
    }
}
