package com.notesapp.statistic.service;

import com.notesapp.model.view.NoteView;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class NotesAppServerClient {

    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    public NotesAppServerClient(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    public NoteView getRandomNote() {
        URI uri = getServiceUri("notes-app-server", "/notes/random");

        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<NoteView>() {
                })
                .getBody();
    }

    public String getGreetingsFromPython() {
        URI uri = getServiceUri("python-server", "/hello");

        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                })
                .getBody();
    }

    private URI getServiceUri(String serviceId, String pathToResolve) {
        return discoveryClient.getInstances(serviceId).stream()
                .map(ServiceInstance::getUri)
                .findFirst()
                .map(it -> it.resolve(pathToResolve))
                .orElseThrow();
    }
}