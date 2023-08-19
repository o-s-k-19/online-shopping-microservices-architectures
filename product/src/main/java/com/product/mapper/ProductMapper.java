package com.product.mapper;

import org.springframework.stereotype.Service;

import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.model.Product;

@Service
public class ProductMapper {
	
	public ProductResponse mapToProductResponse(Product product) {
		
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	} 
	
	public Product mapToProduct(ProductRequest productRequest) {
		
		return Product.builder()
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();
		
	}

}
