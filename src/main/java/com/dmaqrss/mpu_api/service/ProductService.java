package com.dmaqrss.mpu_api.service;

import com.dmaqrss.mpu_api.dto.product.ProductRequestDTO;
import com.dmaqrss.mpu_api.dto.product.ProductResponseDTO;
import com.dmaqrss.mpu_api.dto.product.ProductUpdateDTO;
import com.dmaqrss.mpu_api.exception.BusinessException;
import com.dmaqrss.mpu_api.mapper.ProductMapper;
import com.dmaqrss.mpu_api.model.Product;
import com.dmaqrss.mpu_api.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    public ProductResponseDTO create(ProductRequestDTO dto){
        if(repository.existsByBarCode(dto.barCode())){
            throw new BusinessException("o codigo de barras ja existe");
        }
        Product saved = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public void delete(Long barCode){
        Product product = repository.findByBarCode(barCode).orElseThrow(() -> new BusinessException("o codigo de barras nao existe"));
        repository.delete(product);
    }

    public ProductResponseDTO update(ProductUpdateDTO dto, Long barCode){
        Product product = repository.findByBarCode(barCode).orElseThrow(() -> new BusinessException("o codigo de barras nao existe"));
        BeanUtils.copyProperties(dto, product);
        repository.save(product);
        return mapper.toResponse(product);
    }

    public ProductResponseDTO getProduct(Long barCode){
        Product product = repository.findByBarCode(barCode).orElseThrow(() -> new BusinessException("o codigo de barras nao existe"));
        return mapper.toResponse(product);
    }

    public Page<ProductResponseDTO> getProducts(Pageable pageable){
        return repository.findAll(pageable).map(mapper::toResponse);

    }
}
