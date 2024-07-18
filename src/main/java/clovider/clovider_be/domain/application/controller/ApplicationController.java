package clovider.clovider_be.domain.application.controller;

import clovider.clovider_be.domain.application.service.ApplicationCommandService;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("application")
public class ApplicationController {

    @Autowired
    private ApplicationCommandService applicationCommandService;

    @Autowired
    private ApplicationQueryService applicationQueryService;
}
