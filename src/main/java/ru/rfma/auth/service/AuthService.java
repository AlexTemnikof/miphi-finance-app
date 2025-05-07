package ru.rfma.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.rfma.auth.dto.JwtRequest;
import ru.rfma.auth.dto.JwtRequestReg;
import ru.rfma.auth.dto.JwtResponse;
import ru.rfma.auth.entity.Client;
import ru.rfma.auth.entity.JwtAuthentication;
import ru.rfma.auth.providers.JwtProvider;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientService clientService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final Client client = clientService.getByLogin(authRequest.getLogin());

        if (Arrays.equals(client.getPassword(), authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(client);
            final String refreshToken = jwtProvider.generateRefreshToken(client);
            refreshStorage.put(client.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Wrong credentials");
        }
    }

    public JwtResponse register(@NonNull JwtRequestReg regRequest) throws AuthException {
        final Client clientByLoginOptional = clientService.getByLogin(regRequest.login());
        if (clientByLoginOptional != null) {
            throw new AuthException("The user with such login exists");
        }
        final Client clientByEmailOptional = clientService.getByLogin(regRequest.email());
        if (clientByEmailOptional != null) {
            throw new AuthException("The user with such email exists");
        }
        final Client client = clientService.create(regRequest);
        final String accessToken = jwtProvider.generateAccessToken(client);
        final String refreshToken = jwtProvider.generateRefreshToken(client);
        refreshStorage.put(client.getLogin(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String id = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(id);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Client client = clientService.getById(Integer.valueOf(id));
                final String accessToken = jwtProvider.generateAccessToken(client);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String id = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(id);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Client client = clientService.getById(Integer.valueOf(id));
                final String accessToken = jwtProvider.generateAccessToken(client);
                final String newRefreshToken = jwtProvider.generateRefreshToken(client);
                refreshStorage.put(client.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Invalid Jwt token");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public Integer tryGetClientIdFromRequest(final HttpServletRequest request) {
        final Enumeration<String> headers = request.getHeaders("X-CLIENT-ID");
        String userId = null;

        if (headers.hasMoreElements()) {
            userId = headers.nextElement();
        }

        return userId != null ? Integer.valueOf(userId) : null;
    }

}