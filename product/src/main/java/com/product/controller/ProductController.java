package com.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createProduct(@RequestBody ProductRequest productResquest) {
		productService.createProduct(productResquest);
	}
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ProductResponse> getListProducts(){
		return productService.getAllProducts();
	}
}
