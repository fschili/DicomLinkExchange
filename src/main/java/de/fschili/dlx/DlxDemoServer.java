package de.fschili.dlx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DlxDemoServer {

    private final static Logger log = LoggerFactory.getLogger(DlxDemoServer.class);

    public static void main(String[] args) {
        SpringApplication.run(DlxDemoServer.class, args);
        log.info("DemoServer started!");
    }
}