package com.order.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.order.dto.OrderRequest;
import com.order.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	//@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
	//@TimeLimiter(name = "inventory")
	//@Retry(name = "inventory")
	public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
		return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
	}
	
	public CompletableFuture<String> fallbackMethod(@RequestBody OrderRequest orderRequest, RuntimeException runtimeException) {
	    return CompletableFuture.supplyAsync(()->"Something goes wrong, please retry later");
	}
}
