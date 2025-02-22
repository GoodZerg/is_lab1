package com.is.back.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactController {

    /**
     * Отдает главную страницу React-приложения.
     *
     * @return Имя шаблона (index.html).
     */
    @GetMapping(value = {"/", "/{path:[^\\.]*}"})
    public String index() {
        return "forward:/index.html";
    }
}
