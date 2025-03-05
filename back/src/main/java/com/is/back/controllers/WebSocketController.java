package com.is.back.controllers;

import com.is.back.dto.CityDTO;
import com.is.back.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private CityService cityService;

    public void sendCitiesUpdate() {
        List<CityDTO> cities = cityService.getAllCities();
        template.convertAndSend("/topic/cities", cities);
    }
}
