package com.example.carespawbe.controller.Notification;

import com.example.carespawbe.dto.Notification.MarkReadRequest;
import com.example.carespawbe.dto.Notification.NotificationPageResponse;
import com.example.carespawbe.enums.NotificationType;
import com.example.carespawbe.service.Notification.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;
    public NotificationController(NotificationService s){this.service=s;}

    @GetMapping
    public NotificationPageResponse list(
            @RequestParam(defaultValue="ALL") String type,
            @RequestParam(defaultValue="ALL") String status,
            @RequestParam int page,
            @RequestParam int size
    ){
        NotificationType t = "ALL".equals(type) ? null : NotificationType.valueOf(type);
        return service.getMyNotifications(t, "UNREAD".equals(status), page, size); // bỏ userId
    }

    @GetMapping("/unread-count")
    public long unread() {
        return service.getMyUnreadCount(); // bỏ uid(request)
    }

    @PatchMapping("/{id}/read") public void read(@PathVariable Long id){service.markRead(id);}
    @PatchMapping("/read") public void readBatch(@RequestBody MarkReadRequest r){service.markReadBatch(r);}
    @PatchMapping("/read-all") public int readAll(){return service.markAllRead();}
}
