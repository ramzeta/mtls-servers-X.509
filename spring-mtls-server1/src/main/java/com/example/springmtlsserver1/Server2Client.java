package com.example.springmtlsserver1;

import nl.altindag.ssl.SSLFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.security.PublicKey;
import java.security.cert.CertificateNotYetValidException;
import java.util.HexFormat;
import java.util.stream.Stream;

/*
1. HttpClient
2. RestTemplate
3. WebClient
*/

@Component
public class Server2Client {

    public String getHellowFromServer2() throws IOException, InterruptedException {
        // ver en el debug la INFO
        System.setProperty("javax.net.debug", "ssl,handshake");

        // Create SSLFactory
        var sslConfig = SSLFactory.builder()
                .withDefaultTrustMaterial() // cacerts que son los certificados de confianza
                .withTrustMaterial(Path.of("src/main/resources/s1_ts.p12"), "admin123".toCharArray()) // truststore
                .withIdentityMaterial(Path.of("src/main/resources/s1_ks.p12"), "admin123".toCharArray()) // keystore
                .build().getSslContext();

        // Create HttpClient
        var httpClient = HttpClient.newBuilder()
                .sslContext(sslConfig)
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(java.time.Duration.ofSeconds(10))
                .build();

        // Send request
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://server2:8082/hello"))
                .build();

        // Get response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        response.headers().map().forEach((k, v) -> System.out.println(k + ":" + v));

        response.sslSession().ifPresent(sslSession -> {
            System.out.println("SSL Session: " + sslSession);
            System.out.println("SSL Session Protocol: " + sslSession.getProtocol());

            try {
                System.out.println("SSL Session Peer Principal: " + sslSession.getPeerPrincipal());
                Stream.of(sslSession.getPeerCertificates())
                        .map(c -> (java.security.cert.X509Certificate) c)
                        .forEach(c -> {
                            try {
                                c.checkValidity();
                                PublicKey publicKey = c.getPublicKey();
                                System.out.println("SSL Session Peer Certificate Public Key: " + HexFormat.of().formatHex(publicKey.getEncoded()));
                            } catch (CertificateNotYetValidException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            }
        });

        return response.body();

    }
}
