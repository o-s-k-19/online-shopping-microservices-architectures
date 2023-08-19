package com.product.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.mapper.ProductMapper;
import com.product.model.Product;
import com.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	@Override
	@Transactional
	public void createProduct(ProductRequest productRequest) {
		log.info("Product requested : {}",productRequest);
		Product savedProduct = productRepository.save(productMapper.mapToProduct(productRequest));
		log.info("Product created : {}",savedProduct);
	}

	@Override
	@Transactional
	public List<ProductResponse> getAllProducts() {
		log.info("getting products list");
		return productRepository.findAll().stream()
		.map(productMapper::mapToProductResponse)
		.collect(Collectors.toList());
	}

}
