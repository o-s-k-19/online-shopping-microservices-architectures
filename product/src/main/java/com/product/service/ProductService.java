package com.product.service;

import java.util.List;

import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;

public interface ProductService {

	void createProduct(ProductRequest productRequest);
	
	List<ProductResponse> getAllProducts();
}
