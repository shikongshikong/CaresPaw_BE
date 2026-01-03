package com.example.carespawbe.service.Expert.videoCalling;

import com.example.carespawbe.config.JitsiProperties;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class JitsiMeetService {
    private final JitsiProperties props;

    public JitsiMeetService(JitsiProperties props) {
        this.props = props;
    }

    public String buildRoomName(Long appointmentId) {
        // đảm bảo unique + khó đoán hơn (bạn có thể add random suffix)
        return "app-" + appointmentId;
    }

    public String buildJoinUrl(String roomName) {
        // encode phòng để an toàn
        String encoded = URLEncoder.encode(roomName, StandardCharsets.UTF_8);
        return props.getBaseUrl().replaceAll("/+$", "") + "/" + encoded;
    }

    public String maybeGenerateJwt(String roomName, String displayName, String role) {
        // OPTIONAL:
        // Nếu bạn dùng Jitsi JWT: generate token tại đây
        // Hiện tại return null cho đơn giản.
        return null;
    }
}

