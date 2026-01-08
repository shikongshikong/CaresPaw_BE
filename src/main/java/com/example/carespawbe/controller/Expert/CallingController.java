//package com.example.carespawbe.controller.Expert;
//
//import com.example.carespawbe.dto.Expert.videoCall.JoinCallResponse;
//import com.example.carespawbe.service.Expert.videoCalling.JitsiMeetService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/calling")
//public class CallingController {
//
//    private final JitsiMeetService jitsiMeetService;
//
//    public CallingController(JitsiMeetService jitsiMeetService) {
//        this.jitsiMeetService = jitsiMeetService;
//    }
//
//    @PostMapping("/{id}/join")
//    public JoinCallResponse joinCall(@PathVariable("id") Long id, HttpServletRequest request) {
//        Long userId = (Long) request.getAttribute("userId");
//        return jitsiMeetService.joinCall(userId, id);
//    }
//}
