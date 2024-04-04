package com.example.springmtlsserver1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class SpringMtlsServer1Application {

    public static void main(String[] args) throws IOException, InterruptedException {

        ApplicationContext ctx = SpringApplication.run(SpringMtlsServer1Application.class, args);

        var server2Client = ctx.getBean(Server2Client.class);

        System.out.println(server2Client.getHellowFromServer2());
    }

}
