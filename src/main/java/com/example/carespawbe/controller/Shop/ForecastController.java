package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.ForecastRequest;
import com.example.carespawbe.dto.Shop.ForecastResponse;
import com.example.carespawbe.service.Shop.GM11Service;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/forecast")
//@CrossOrigin(origins = "*") // nếu bạn muốn giới hạn domain FE thì thay bằng domain FE
public class ForecastController {

    private final GM11Service gm11Service;

    public ForecastController(GM11Service gm11Service) {
        this.gm11Service = gm11Service;
    }

    @PostMapping("/getGMPredictResult")
    public ForecastResponse gm11(@Valid @RequestBody ForecastRequest req) {
        double[] forecast = gm11Service.gm11(req.getData(), req.getPredictN(), req.getTrainLen());

// nếu dùng cho UNITS: làm tròn + clamp >=0
        for (int i = 0; i < forecast.length; i++) {
            forecast[i] = Math.max(0, Math.round(forecast[i]));
        }

        int n = req.getData().size();
        int trainLen = (req.getTrainLen() == null) ? (int) Math.floor(n * 0.8) : req.getTrainLen();
        if (trainLen < 2) trainLen = 2;
        if (trainLen > n) trainLen = n;

        int predictN = (req.getPredictN() == null) ? 5 : Math.max(0, req.getPredictN());

        return new ForecastResponse(forecast, trainLen, predictN);
    }
}
