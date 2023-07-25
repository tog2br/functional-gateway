package com.example.functional.gateway;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.functional.interfaces.ThreeFunction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Non-blocking, functional gateway to perform HTTP requests, using reactive client
 *
 * <p>Inject the dependency into your code to create an instance.
 *
 * @author Thiago de Luca
 */
@Component
public class FunctionalGateway {

    /**
     * Default constructor
     */
    public FunctionalGateway() {}
    private final int TIMEOUT = 5;
    private final int RETRIES = 3;

    /**
     * Functional Interface to handler an exception
     */
    @SuppressWarnings("rawtypes")
	private final Function<ClientResponse, Mono> handlerException = (response) -> response.createException();

    /**
     * Functional Interface to make a request
     */
    private final ThreeFunction<String, Object, HttpMethod, WebClient.ResponseSpec> doRequest = (url, body, method) -> {
        return WebClient.create()
                .method(method)
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .body((body != null)?BodyInserters.fromValue(body):null)
                .retrieve();
    };

    /**
     * Method to perform an HTTP reactive request and return an object of type Mono
     *
     * @param url the path url
     * @param body the body object to send
     * @param method the {@code HttpMethod} to send request
     * @param clazz the {@code Class} to return
     * @return {@code Mono} of class or {@code Mono.error}
     */
    @SuppressWarnings("rawtypes")
	public Mono doRequestReturnMono(String url, Object body, HttpMethod method, Class clazz) {
        return this.doRequestReturnMono(url, body, method, clazz, null, null);
    }

    /**
     * Variant of {@link #doRequestReturnMono(String url, Object body, HttpMethod method, Class clazz)}
     * that accepts a timeout and retries values.
     *
     * @param url the path url
     * @param body the body object to send
     * @param method the {@code HttpMethod} to send request
     * @param clazz the {@code Class} to return
     * @param timeout in seconds, default is 5
     * @param retries number of retries, default is 3
     * @return {@code Mono} of class or {@code Mono.error}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Mono doRequestReturnMono(String url, Object body, HttpMethod method, Class clazz, Integer timeout, Integer retries) {
        return doRequest.apply(url, body, method)
                .onStatus(HttpStatusCode::isError, handlerException::apply)
                .bodyToMono(clazz)
                .timeout(Duration.ofSeconds(Optional.ofNullable(timeout).orElse(TIMEOUT)))
                .retry(Optional.ofNullable(retries).orElse(RETRIES));
    }

    /**
     * Method to perform an HTTP reactive request and return an object of type Flux
     *
     * @param url the path url
     * @param body the body object to send
     * @param method the {@code HttpMethod} to send request
     * @param clazz the {@code Class} to return
     * @return {@code Flux} of class or {@code Mono.error}
     */
    @SuppressWarnings({ "rawtypes" })
    public Flux doRequestReturnFlux(String url, Object body, HttpMethod method, Class clazz) {
        return this.doRequestReturnFlux(url, body, method, clazz, null, null);
    }

    /**
     * Variant of {@link #doRequestReturnFlux(String url, Object body, HttpMethod method, Class clazz)}
     * that accepts a timeout and retries values.
     *
     * @param url the path url
     * @param body the body object to send
     * @param method the {@code HttpMethod} to send request
     * @param clazz the {@code Class} to return
     * @param timeout in seconds, default is 5
     * @param retries number of retries, default is 3
     * @return {@code Flux<T>} of class or {@code Mono.error}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Flux doRequestReturnFlux(String url, Object body, HttpMethod method, Class clazz, Integer timeout, Integer retries) {
        return doRequest.apply(url, body, method)
                .onStatus(HttpStatusCode::isError, handlerException::apply)
                .bodyToFlux(clazz)
                .timeout(Duration.ofSeconds(Optional.ofNullable(timeout).orElse(TIMEOUT)))
                .retry(Optional.ofNullable(retries).orElse(RETRIES));
    }
}