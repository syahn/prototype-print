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

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("print", new PrintRequest());

        return "index";
    }


    @RequestMapping(value = "/preview")
    public String viewPreview(HttpServletResponse response, @ModelAttribute PrintRequest print, Model model){
//        System.out.print("url" + print.getIn() + url);
//        model.addAttribute("print", print);

        print.setIn("C:/Users/NAVER/prototype/target/classes/static/html/sample_7.html");
        print.setName("C:/Users/NAVER/prototype/target/classes/static/images/sample_7.png");
//        System.out.print(print.getIn() + print.getName() + "hello");
        converter.createImage(print, response);

        return "preview";
    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    public ResponseEntity<?> uploadAttachment(@RequestPart MultipartFile sourceFile) throws IOException {
//        String sourceFileName = sourceFile.getOriginalFilename();
//        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
//        File destinationFile; String destinationFileName;
//        do {
//            destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
//            destinationFile = new File("C:/attachments/" + destinationFileName);
//        }  while (destinationFile.exists());
//        destinationFile.getParentFile().mkdirs();
//        sourceFile.transferTo(destinationFile);
//
//        UploadResponse response = new UploadResponse();
//        response.setFileName(sourceFile.getOriginalFilename());
//        response.setFileSize(sourceFile.getSize());
//        response.setFileContentType(sourceFile.getContentType());
//        response.setAttachmentUrl("http://localhost:8080/attachments/" + destinationFileName);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }OK

    @RequestMapping(value = "/preview/pdfview", method = RequestMethod.POST)
    public void pdf(HttpServletResponse response, @ModelAttribute PrintRequest print) {
    /*@RequestBody WkhtmlRequest request, */
        converter.create(print, response);
    }
}
