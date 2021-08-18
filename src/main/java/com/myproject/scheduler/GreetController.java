package com.myproject.scheduler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GreetController {



    @RequestMapping("/greet")
    public ModelAndView showview()
    {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("result.jsp");
        mv.addObject("result",
                "GeeksForGeeks Welcomes "
                        + "you to Spring!");
        return mv;
    }
}