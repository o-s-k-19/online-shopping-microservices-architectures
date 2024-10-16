package com.order;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam() int size, @RequestParam() int page) {
		return ResponseEntity.ok().body(orderService.findAll(size, page));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOneById(@PathVariable() long id) {
		try {
			return ResponseEntity.ok().body(orderService.findById(id));
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
	@TimeLimiter(name = "inventory")
	@Retry(name = "inventory")
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(orderRequest));
		} catch (OutOfStockException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public ResponseEntity<String> fallbackMethod(@RequestBody OrderRequest orderRequest,
			RuntimeException runtimeException) {
		return ResponseEntity.internalServerError().body("Something goes wrong, please retry later");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOneById(@PathVariable() long id) {
		try {
			orderService.deleteById(id);
			return ResponseEntity.ok().body(String.format("Order with id %s is deleted successfully !", id));
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

}
