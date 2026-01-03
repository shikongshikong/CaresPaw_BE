package com.example.carespawbe.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.jitsi")
public class JitsiProperties {
    /**
     * Example:
     *  - https://meet.jit.si
     *  - https://jitsi.yourdomain.com
     */
    private String baseUrl = "https://meet.jit.si";

    /**
     * Optional: if you enable Jitsi JWT authentication on your deployment.
     */
    private boolean jwtEnabled = false;

    /**
     * Optional secret for signing JWT (only if you manage JWT yourself).
     */
    private String jwtSecret;

}

