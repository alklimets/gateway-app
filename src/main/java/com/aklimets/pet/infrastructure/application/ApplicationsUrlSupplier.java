package com.aklimets.pet.infrastructure.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApplicationsUrlSupplier {

    private final Map<String, String> URLS_MAP;

    public ApplicationsUrlSupplier(@Value("${pet.project.url.collection}") String urlCollection) {
        this.URLS_MAP = Arrays.stream(urlCollection.split(","))
                .map(item -> item.split("="))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1], (a,b) -> a));
    }

    public String supplyUrl(String appName) {
        return URLS_MAP.get(appName);
    }

    public boolean appExists(String appName) {
        return URLS_MAP.containsKey(appName);
    }
}
