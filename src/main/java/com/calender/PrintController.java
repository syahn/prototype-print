package com.calender;

/**
 * Created by NAVER on 2017-07-06.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Created by saltfactory on 10/29/15.
 */
@Controller
@EnableAutoConfiguration
public class PrintController {

    @Autowired
    private PrintConverter converter;

    @RequestMapping("/index1")
    public String index() { return "index1"; }

    @RequestMapping("/index2")
    public String index2(){
        return "index2";
    }

    @RequestMapping("/index3")
    public String index3() {
        return "index3";
    }


    @PostMapping("/preview")
    public String viewPreview(HttpServletResponse response, @RequestParam String month, @ModelAttribute PrintRequest print, Model model){

        String extendIn = "C:/Users/NAVER/prototype/target/classes/static/html/index" + month + ".html";
        String extendOut = "C:/Users/NAVER/prototype/target/classes/static/images/sample" + month + ".png";

        System.out.print(extendIn + ": " + extendOut);
        print.setIn(extendIn);
        print.setOut(extendOut);

        model.addAttribute("month", month);

        converter.createImage(print, response);

        return "preview";
    }


    @RequestMapping(value = "/preview/pdfview", method = RequestMethod.POST)
    public void pdf(HttpServletResponse response, @ModelAttribute PrintRequest print) {
    /*@RequestBody WkhtmlRequest request, */
        converter.create(print, response);
    }
}
