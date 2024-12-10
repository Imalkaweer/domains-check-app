package com.example.vscode.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UrlCheckController {

    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UrlCheckController.class);
    private final String SITE_IS_UP = "Site is Up!";
    private final String SITE_IS_DOWN = "Site is Down!";
    private final String URL_IS_INCORRECT = "URL is incorrect!";

    @GetMapping("/check")
    public String getMethodName(@RequestParam String url) {
        String statusOfDomain = "";
        try {
            LOGGER.info("URL: [{}]", url);
            URL urlObject = new URI(url).toURL();
            HttpURLConnection checkConnection = (HttpURLConnection) urlObject.openConnection();
            checkConnection.setRequestMethod(HttpMethod.GET.name());
            checkConnection.connect();
            final int responseCode = checkConnection.getResponseCode() / 100;
            if (responseCode != 2 && responseCode != 3) {
                LOGGER.warn("DOMAIN Returned DIFFERENT STATUS CODE: [{}]", responseCode);
                statusOfDomain = SITE_IS_DOWN;
            } else {
                statusOfDomain = SITE_IS_UP;
            }
        } catch (MalformedURLException e) {
            LOGGER.error("DOMAIN Returned MalformedURLException: [{}]", e.getMessage());
            statusOfDomain = URL_IS_INCORRECT;
        } catch (URISyntaxException e) {
            LOGGER.error("DOMAIN Returned URISyntaxException: [{}]", e.getMessage());
            statusOfDomain = URL_IS_INCORRECT;
        } catch (IOException e) {
            LOGGER.error("DOMAIN Returned IOException: [{}]", e.getMessage());
            statusOfDomain = SITE_IS_DOWN;
        }
        return statusOfDomain;
    }

}
