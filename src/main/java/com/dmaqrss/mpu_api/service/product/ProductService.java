package com.dmaqrss.mpu_api.service.product;

import com.dmaqrss.mpu_api.dto.product.ProductRequestDTO;
import com.dmaqrss.mpu_api.dto.product.ProductResponseDTO;
import com.dmaqrss.mpu_api.dto.product.ProductUpdateDTO;
import com.dmaqrss.mpu_api.exception.BusinessException;
import com.dmaqrss.mpu_api.mapper.ProductMapper;
import com.dmaqrss.mpu_api.model.Product;
import com.dmaqrss.mpu_api.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @CacheEvict(value = {"products", "listProducts"}, allEntries = true)
    public ProductResponseDTO create(ProductRequestDTO dto){
        log.info("[PRODUCT] Solicitação criação de novo produto: {}", dto.barCode());

        if(repository.existsByBarCode(dto.barCode())){
            log.warn("[PRODUCT] Tentativa de registo com codigo de barras ja existente: {}", dto.barCode());
            throw new BusinessException("o codigo de barras ja existe");
        }

        Product saved = repository.save(mapper.toEntity(dto));
        log.info("[PRODUCT] Produto criado com sucesso: {}", dto.barCode());
        return mapper.toResponse(saved);
    }

    @CacheEvict(value = {"products", "listProducts"}, allEntries = true)
    public void delete(Long barCode){
        log.info("[PRODUCT] Solicitação delete de produto: {}", barCode);

        Product product = repository.findByBarCode(barCode).orElseThrow(() -> {
            log.warn("[PRODUCT] tentativa de deletar produto inexistente: {}", barCode);
            return new BusinessException("o codigo de barras nao existe");
        });

        repository.delete(product);
        log.info("[PRODUCT] Produto deletado com sucesso: {}", product.getBarCode());
    }

    @CacheEvict(value = {"products", "listProducts"}, allEntries = true)
    public ProductResponseDTO update(ProductUpdateDTO dto, Long barCode){
        log.info("[PRODUCT] Solicitação update do produto: {}", barCode);

        Product product = repository.findByBarCode(barCode).orElseThrow(() -> {
            log.warn("[PRODUCT] Tentativa de update de produto inexistente: {}", barCode);
            return new BusinessException("o codigo de barras nao existe");
        });

        BeanUtils.copyProperties(dto, product);
        repository.save(product);
        log.info("[PRODUCT] Update no produto com sucesso: {}", product.getBarCode());
        return mapper.toResponse(product);
    }

    @Cacheable(value = "products", key = "#barCode")
    public ProductResponseDTO getProduct(Long barCode){
        log.info("[PRODUCT] Solicitação get do produto: {}", barCode);

        Product product = repository.findByBarCode(barCode).orElseThrow(() -> {
            log.warn("[PRODUCT] Tentativa de retorno de produto inexistente: {}", barCode);
            return new BusinessException("o codigo de barras nao existe");
        });

        log.info("[PRODUCT] Retorno do produto com sucesso: {}", product.getBarCode());
        return mapper.toResponse(product);
    }

    @Cacheable(value = "listProducts", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<ProductResponseDTO> getProducts(Pageable pageable){
        log.info("[PRODUCT] Solicitação paginação de produtos: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<ProductResponseDTO> result = repository.findAll(pageable).map(mapper::toResponse);

        log.info("[PRODUCT] Retornando {} produtos", result.getTotalElements());
        return result;
    }
}
