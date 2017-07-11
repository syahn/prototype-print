package com.calender;

/**
 * Created by NAVER on 2017-07-07.
 */

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;

@Service
public class PrintConverter {

    private static final Logger LOG = LoggerFactory.getLogger(PrintConverter.class);

    public void createImage(PrintRequest request, HttpServletResponse response) {
        List<String> pdfCommand = Arrays.asList(
                "wkhtmltoimage",
                "--height",
                "735",
                request.getIn(),
                request.getOut()
        );
        ProcessBuilder pb = new ProcessBuilder(pdfCommand);
        Process pdfProcess;

        try {
            pdfProcess = pb.start();
            waitForProcessInCurrentThread(pdfProcess);

        } catch (IOException ex) {
            LOG.error("Could not create a PDF file because of an error occurred: ", ex);
            throw new RuntimeException("PDF generation failed");
        }
    }

    public void createPdf(PrintRequest request, HttpServletResponse response) {
        List<String> pdfCommand = Arrays.asList(
                "wkhtmltoimage",
                "--height",
                "735",
                request.getIn(),
                request.getOut()
        );
        ProcessBuilder pb = new ProcessBuilder(pdfCommand);
        Process pdfProcess;

        try {
            pdfProcess = pb.start();
            waitForProcessInCurrentThread(pdfProcess);

        } catch (IOException ex) {
            LOG.error("Could not create a PDF file because of an error occurred: ", ex);
            throw new RuntimeException("PDF generation failed");
        }
    }

    public void create(PrintRequest request, HttpServletResponse response) {
        List<String> pdfCommand = Arrays.asList(
                "wkhtmltopdf",
                request.getIn(),
                "-"
        );

        ProcessBuilder pb = new ProcessBuilder(pdfCommand);
        Process pdfProcess;

        try {
            pdfProcess = pb.start();

            try (InputStream in = pdfProcess.getInputStream()) {
                pdfToResponse(in, response);
                waitForProcessInCurrentThread(pdfProcess);
                requireSuccessfulExitStatus(pdfProcess);
                setResponseHeaders(response, request);

                LOG.info("Wrote PDF file to the response from request: {}", request);
            } catch (Exception ex) {
                writeErrorMessageToLog(ex, pdfProcess);
                throw new RuntimeException("PDF generation failed");
            } finally {
                pdfProcess.destroy();
            }
        } catch (IOException ex) {
            LOG.error("Could not create a PDF file because of an error occurred: ", ex);
            throw new RuntimeException("PDF generation failed");
        }
    }

    private void pdfToResponse(InputStream in, HttpServletResponse response) throws IOException {
        LOG.debug("Writing created PDF file to HTTP response");

        OutputStream out = response.getOutputStream();
        org.apache.tomcat.util.http.fileupload.IOUtils.copy(in, out);
        out.flush();
    }

    private void waitForProcessInCurrentThread(Process process) {
        try {
            process.waitFor();
            //delay
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        LOG.debug("Wkhtmltopdf ended");
    }

    private void requireSuccessfulExitStatus(Process process) {
        if (process.exitValue() != 0) {
            throw new RuntimeException("PDF generation failed");
        }
    }

    private void setResponseHeaders(HttpServletResponse response, PrintRequest request) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + request.getOut() + "\"");
    }

    private void writeErrorMessageToLog(Exception ex, Process pdfProcess) throws IOException {
        LOG.error("Could not create PDF because an exception was thrown: ", ex);
        LOG.error("The exit value of PDF process is: {}", pdfProcess.exitValue());

        String errorMessage = getErrorMessageFromProcess(pdfProcess);
        LOG.error("PDF process ended with error message: {}", errorMessage);
    }

    private String getErrorMessageFromProcess(Process pdfProcess) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(pdfProcess.getErrorStream()));
            StringWriter writer = new StringWriter();

            String line;
            while ((line = reader.readLine()) != null) {
                writer.append(line);
            }

            return writer.toString();
        } catch (IOException ex) {
            LOG.error("Could not extract error message from process because an exception was thrown", ex);
            return "";
        }
    }


    //pdf 라이브러리 활용

    final static String url = "http://localhost:8080/month_";//기본 url뒤에 월을 붙임

    public static void makeAPdf(int s, int e, int orientation) throws InterruptedException, IOException {

        // 각 월에 대한 임시 경로 생성
        String temp = "";
        for(int i=s;i<=e;i++){
            temp+=(url+Integer.toString(i)+" ");
        }

        Process wkhtml; // Create uninitialized process
        String command = null;

        //%s에 열거하여 쓰기
        if(orientation==1){
            command = String.format("wkhtmltopdf -O landscape %s C:/Users/NAVER/Desktop/prototype/target/classes/static/tempPdf/month_result.pdf",temp); // Desired command
        }
        else if(orientation==0){
            command = String.format("wkhtmltopdf %s C:/Users/NAVER/Desktop/prototype/target/classes/static/tempPdf/month_result.pdf",temp); // Desired command
        }

        wkhtml = Runtime.getRuntime().exec(command); // Start process
        IOUtils.copy(wkhtml.getErrorStream(), System.err); // Print output to console
        wkhtml.waitFor(); // Allow process to run
    }

}


//package com.calender;
//
///**
// * Created by NAVER on 2017-07-07.
// */
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class PrintConverter {
//
//    private static final Logger LOG = LoggerFactory.getLogger(PrintConverter.class);
//
//    public void createImage(PrintRequest request, HttpServletResponse response) {
//        List<String> pdfCommand = Arrays.asList(
//                "wkhtmltoimage",
//                "--height",
//                "735",
//                request.getIn(),
//                request.getOut()
//        );
//        ProcessBuilder pb = new ProcessBuilder(pdfCommand);
//        Process pdfProcess;
//
//        try {
//            pdfProcess = pb.start();
//            waitForProcessInCurrentThread(pdfProcess);
//
//        } catch (IOException ex) {
//            LOG.error("Could not create a PDF file because of an error occurred: ", ex);
//            throw new RuntimeException("PDF generation failed");
//        }
//    }
//
//    public void createPdf(PrintRequest request, HttpServletResponse response) {
//        List<String> pdfCommand = Arrays.asList(
//                "wkhtmltoimage",
//                "--height",
//                "735",
//                request.getIn(),
//                request.getOut()
//        );
//        ProcessBuilder pb = new ProcessBuilder(pdfCommand);
//        Process pdfProcess;
//
//        try {
//            pdfProcess = pb.start();
//            waitForProcessInCurrentThread(pdfProcess);
//
//        } catch (IOException ex) {
//            LOG.error("Could not create a PDF file because of an error occurred: ", ex);
//            throw new RuntimeException("PDF generation failed");
//        }
//    }
//
//    public void create(PrintRequest request, HttpServletResponse response) {
//        List<String> pdfCommand = Arrays.asList(
//                "wkhtmltopdf",
//                request.getIn(),
//                "-"
//        );
//
//        ProcessBuilder pb = new ProcessBuilder(pdfCommand);
//        Process pdfProcess;
//
//        try {
//            pdfProcess = pb.start();
//
//            try (InputStream in = pdfProcess.getInputStream()) {
//                pdfToResponse(in, response);
//                waitForProcessInCurrentThread(pdfProcess);
//                requireSuccessfulExitStatus(pdfProcess);
//                setResponseHeaders(response, request);
//
//                LOG.info("Wrote PDF file to the response from request: {}", request);
//            } catch (Exception ex) {
//                writeErrorMessageToLog(ex, pdfProcess);
//                throw new RuntimeException("PDF generation failed");
//            } finally {
//                pdfProcess.destroy();
//            }
//        } catch (IOException ex) {
//            LOG.error("Could not create a PDF file because of an error occurred: ", ex);
//            throw new RuntimeException("PDF generation failed");
//        }
//    }
//
//    private void pdfToResponse(InputStream in, HttpServletResponse response) throws IOException {
//        LOG.debug("Writing created PDF file to HTTP response");
//
//        OutputStream out = response.getOutputStream();
//        org.apache.tomcat.util.http.fileupload.IOUtils.copy(in, out);
//        out.flush();
//    }
//
//    private void waitForProcessInCurrentThread(Process process) {
//        try {
//            process.waitFor();
//            //delay
//        } catch (InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }
//
//        LOG.debug("Wkhtmltopdf ended");
//    }
//
//    private void requireSuccessfulExitStatus(Process process) {
//        if (process.exitValue() != 0) {
//            throw new RuntimeException("PDF generation failed");
//        }
//    }
//
//    private void setResponseHeaders(HttpServletResponse response, PrintRequest request) {
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + request.getOut() + "\"");
//    }
//
//    private void writeErrorMessageToLog(Exception ex, Process pdfProcess) throws IOException {
//        LOG.error("Could not create PDF because an exception was thrown: ", ex);
//        LOG.error("The exit value of PDF process is: {}", pdfProcess.exitValue());
//
//        String errorMessage = getErrorMessageFromProcess(pdfProcess);
//        LOG.error("PDF process ended with error message: {}", errorMessage);
//    }
//
//    private String getErrorMessageFromProcess(Process pdfProcess) {
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(pdfProcess.getErrorStream()));
//            StringWriter writer = new StringWriter();
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                writer.append(line);
//            }
//
//            return writer.toString();
//        } catch (IOException ex) {
//            LOG.error("Could not extract error message from process because an exception was thrown", ex);
//            return "";
//        }
//    }
//}
//
////
////
////
////
////public class PrintConverter {
////
////    private static final String STDINOUT = "-";
////
////    private final WrapperConfig wrapperConfig;
////
////    private final Params params;
////
////    private final List<Page> pages;
////
////    private boolean hasToc = false;
////
////
////    public PrintConverter() {
////        this(new WrapperConfig());
////    }
////
////    public PrintConverter(WrapperConfig wrapperConfig) {
////        this.wrapperConfig = wrapperConfig;
////        this.params = new Params();
////        this.pages = new ArrayList<Page>();
////    }
////
////    /**
////     * Add a page to the pdf
////     *
////     * @deprecated Use the specific type method to a better semantic
////     */
////    @Deprecated
////    public void addPage(String source, PageType type) {
////        this.pages.add(new Page(source, type));
////    }
////
////    /**
////     * Add a page from an URL to the pdf
////     */
////    public void addPageFromUrl(String source) {
////        this.pages.add(new Page(source, PageType.url));
////    }
////
////    /**
////     * Add a page from a HTML-based string to the pdf
////     */
////    public void addPageFromString(String source) {
////        this.pages.add(new Page(source, PageType.htmlAsString));
////    }
////
////    /**
////     * Add a page from a file to the pdf
////     */
////    public void addPageFromFile(String source) {
////        this.pages.add(new Page(source, PageType.file));
////    }
////
////    public void addToc() {
////        this.hasToc = true;
////    }
////
////    public void addParam(Param param, Param... params) {
////        this.params.add(param, params);
////    }
////
////    public File saveAs(String path) throws IOException, InterruptedException {
////        File file = new File(path);
////        FileUtils.writeByteArrayToFile(file, getPDF());
////        return file;
////    }
////
////    public byte[] getPDF() throws IOException, InterruptedException {
////
////        try {
////            Process process = Runtime.getRuntime().exec(getCommandAsArray());
////
////            byte[] inputBytes = IOUtils.toByteArray(process.getInputStream());
////            byte[] errorBytes = IOUtils.toByteArray(process.getErrorStream());
////
////            process.waitFor();
////
////            if (process.exitValue() != 0) {
////                throw new RuntimeException("Process (" + getCommand() + ") exited with status code " + process.exitValue() + ":\n" + new String(errorBytes));
////            }
////
////            return inputBytes;
////        } finally {
////            cleanTempFiles();
////        }
////    }
////
////    private String[] getCommandAsArray() throws IOException {
////        List<String> commandLine = new ArrayList<String>();
////
////        if (wrapperConfig.isXvfbEnabled())
////            commandLine.addAll(wrapperConfig.getXvfbConfig().getCommandLine());
////
////        commandLine.add(wrapperConfig.getWkhtmltopdfCommand());
////
////        commandLine.addAll(params.getParamsAsStringList());
////
////        if (hasToc)
////            commandLine.add("toc");
////
////        for (Page page : pages) {
////            if (page.getType().equals(PageType.htmlAsString)) {
////
////                File temp = File.createTempFile("java-wkhtmltopdf-wrapper" + UUID.randomUUID().toString(), ".html");
////                FileUtils.writeStringToFile(temp, page.getSource(), "UTF-8");
////
////                page.setSource(temp.getAbsolutePath());
////            }
////
////            commandLine.add(page.getSource());
////        }
////        commandLine.add(STDINOUT);
////        return commandLine.toArray(new String[commandLine.size()]);
////    }
////
////    private void cleanTempFiles() {
////        for (Page page : pages) {
////            if (page.getType().equals(PageType.htmlAsString)) {
////                new File(page.getSource()).delete();
////            }
////        }
////    }
////
////    public String getCommand() throws IOException {
////        return StringUtils.join(getCommandAsArray(), " ");
////    }
////
////    public static void makeAPdf() throws InterruptedException, IOException {
////        Process wkhtml; // Create uninitialized process
////        String command = "wkhtmltopdf https://calendar.naver.com/publicCalendar.nhn?publishedKey=769d54e7f884c5f930b4ec86eb15dc38 C:/Users/NAVER/Desktop/calendar.pdf"; // Desired command
////
////        wkhtml = Runtime.getRuntime().exec(command); // Start process
////        IOUtils.copy(wkhtml.getErrorStream(), System.err); // Print output to console
////
////        wkhtml.waitFor(); // Allow process to run
////    }
////
////    public static void makeImage() throws InterruptedException, IOException {
////        Process wkhtml; // Create uninitialized process
////        String command = "wkhtmltoimage https://calendar.naver.com/publicCalendar.nhn?publishedKey=769d54e7f884c5f930b4ec86eb15dc38 C:/Users/NAVER/Desktop/calendar.jpeg"; // Desired command
////
////        wkhtml = Runtime.getRuntime().exec(command); // Start process
////        IOUtils.copy(wkhtml.getErrorStream(), System.err); // Print output to console
////
////        wkhtml.waitFor(); // Allow process to run
////    }
////
////}
