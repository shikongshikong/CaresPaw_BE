//package com.example.carespawbe.controller;
//
//import com.example.carespawbe.service.LocationSyncService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/location")
//@RequiredArgsConstructor
//public class LocationController {
//
//    private final LocationSyncService locationSyncService;
//
//    @PostMapping("/sync-ghn")
//    public String syncGhn() {
//        locationSyncService.syncAllFromGhn();
//        return "SYNC GHN OK";
//    }
//}
