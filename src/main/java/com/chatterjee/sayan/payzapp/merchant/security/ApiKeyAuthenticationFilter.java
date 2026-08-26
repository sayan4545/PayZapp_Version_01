package com.chatterjee.sayan.payzapp.merchant.security;

import com.chatterjee.sayan.payzapp.common.exceptions.RateLimitException;
import com.chatterjee.sayan.payzapp.common.exceptions.ResourceNotFoundException;
import com.chatterjee.sayan.payzapp.common.rateLimiting.RateLimitResult;
import com.chatterjee.sayan.payzapp.common.rateLimiting.RateLimiter;
import com.chatterjee.sayan.payzapp.merchant.cache.ApiKeyCache;
import com.chatterjee.sayan.payzapp.merchant.cache.ApiKeyCacheEntry;
import com.chatterjee.sayan.payzapp.merchant.entities.ApiKey;
import com.chatterjee.sayan.payzapp.merchant.repositories.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String BASIC = "Basic ";
    private final ApiKeyRepository apiKeyRepository;
    private final BCryptPasswordEncoder BCRYPT = new  BCryptPasswordEncoder();
    private final MerchantContext merchantContext;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final ApiKeyCache apiKeyCache;

    private final RateLimiter rateLimiter;

    @Value("${app.rate-limit.use-case.api-key.requests-per-minute:60}")
    private Integer REQUEST_PER_MINUTE;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Incoming request : {}", request.getRequestURI());
        try {


            String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith(BASIC)) {
                filterChain.doFilter(request, response);
                return;
            }

            String[] credentials = decode(header);
            if (credentials == null) {
                throw new BadRequestException("Malformed API key header");
            }
            String keyId = credentials[0];
            String rawSecret = credentials[1];
            ApiKeyCacheEntry apiKeyEntry = apiKeyCache.get(keyId)
                    .orElseGet(()-> loadAndCache(keyId));

            if (apiKeyEntry == null || !apiKeyEntry.enabled() || secretMatches(rawSecret, apiKeyEntry)) {
                throw new BadRequestException("Invalid or missing api key");
            }

           /*
           Implementing rate limiting here. If Rate limited, it will throw an
           exception, RateLimitException()
            */

            RateLimitResult rateLimitResult = rateLimiter.check("apiKey:"+keyId,REQUEST_PER_MINUTE,60);
            if(!rateLimitResult.isAllowed()) throw new RateLimitException("Too many request, try after a minute", rateLimitResult.retryAfterSeconds());

            /*
            Setting the request per limit and the time remaining through the response
            object through its header.
             */

            response.setHeader("X-RateLimit-limit",String.valueOf(REQUEST_PER_MINUTE));
            response.setHeader("X-RateLimit-remaining",String.valueOf(rateLimitResult.remaining()));

            var auth = new UsernamePasswordAuthenticationToken(keyId, null,
                    List.of(new SimpleGrantedAuthority("API_KEY_ROLE")));

            SecurityContextHolder.getContext().setAuthentication(auth);

            merchantContext.setMerchantId(apiKeyEntry.merchantId());
            merchantContext.setKeyId(apiKeyEntry.keyId());

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }



    }
    private ApiKeyCacheEntry loadAndCache(String keyId) {
        ApiKey apiKey = apiKeyRepository.findByKeyId(keyId).orElse(null);
        if(apiKey == null) {
            return null;
        }
        ApiKeyCacheEntry apiKeyCacheEntry = new ApiKeyCacheEntry(apiKey.getKeyId(),
                apiKey.getKeySecretHash(),
                apiKey.getPreviousKeySecretHash(),
                apiKey.getGracePeriodExpiresAt(),
                apiKey.getMerchant().getId(),
                apiKey.getEnvironment(),
                apiKey.getEnabled());
        apiKeyCache.put(keyId, apiKeyCacheEntry);
        return apiKeyCacheEntry;
    }
    private Boolean secretMatches(String rawSecret,ApiKeyCacheEntry apiKeyEntry){
        if(BCRYPT.matches(rawSecret,apiKeyEntry.keySecretHash())){
            return true;
        }
        //boolean isInGracePeriod = apiKey.getGracePeriodExpiresAt() !=null && LocalDateTime.now().isBefore(apiKey.getGracePeriodExpiresAt());

        return apiKeyEntry.isInGracePeriod() &&
                apiKeyEntry.previousKeySecretHash() !=null &&
                BCRYPT.matches(rawSecret,apiKeyEntry.previousKeySecretHash());
    }

    private String[] decode (String header){
        String encoded = header.substring(BASIC.length());
        String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        int colon = decoded.indexOf(':');
        if(colon<1) return null;
        return new String[]{decoded.substring(0, colon), decoded.substring(colon+1)};
    }
}
