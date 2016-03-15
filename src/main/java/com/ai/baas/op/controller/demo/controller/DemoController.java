package com.ai.baas.op.controller.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;



@RestController
@RequestMapping("/demo")
public class DemoController {

    private static final Logger LOG = Logger.getLogger(DemoController.class);

    @RequestMapping("/commlabel")
    public ModelAndView index(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("demo/commlabel");
        return view;
    }
    
    
    
    @RequestMapping("/parent")
    public ModelAndView parent(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("demo/parent");
        return view;
    }


}
