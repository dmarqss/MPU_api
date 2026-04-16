package com.dmaqrss.mpu_api.service.product;

import com.dmaqrss.mpu_api.dto.product.ProductRequestDTO;
import com.dmaqrss.mpu_api.dto.product.ProductResponseDTO;
import com.dmaqrss.mpu_api.dto.product.ProductUpdateDTO;
import com.dmaqrss.mpu_api.exception.BusinessException;
import com.dmaqrss.mpu_api.mapper.ProductMapper;
import com.dmaqrss.mpu_api.model.Product;
import com.dmaqrss.mpu_api.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @CacheEvict(value = {"products", "listProducts"}, allEntries = true, beforeInvocation = true)
    public ProductResponseDTO create(ProductRequestDTO dto){
        if(repository.existsByBarCode(dto.barCode())){
            throw new BusinessException("o codigo de barras ja existe");
        }
        Product saved = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    @Caching(evict = {
            @CacheEvict(value = "products", key = "#barCode", beforeInvocation = true),
            @CacheEvict(value = "listProducts", allEntries = true, beforeInvocation = true)})
    public void delete(Long barCode){
        Product product = repository.findByBarCode(barCode).orElseThrow(() -> new BusinessException("o codigo de barras nao existe"));
        repository.delete(product);
    }

    @Caching(evict = {
            @CacheEvict(value = "products", key = "#barCode", beforeInvocation = true),
            @CacheEvict(value = "listProducts", allEntries = true, beforeInvocation = true)})
    public ProductResponseDTO update(ProductUpdateDTO dto, Long barCode){
        Product product = repository.findByBarCode(barCode).orElseThrow(() -> new BusinessException("o codigo de barras nao existe"));
        BeanUtils.copyProperties(dto, product);
        repository.save(product);
        return mapper.toResponse(product);
    }

    @Cacheable(value = "products", key = "#barCode")
    public ProductResponseDTO getProduct(Long barCode){
        Product product = repository.findByBarCode(barCode).orElseThrow(() -> new BusinessException("o codigo de barras nao existe"));
        return mapper.toResponse(product);
    }

    @Cacheable(value = "listProducts", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<ProductResponseDTO> getProducts(Pageable pageable){
        return repository.findAll(pageable).map(mapper::toResponse);
    }
}
