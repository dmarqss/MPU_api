package com.dmaqrss.mpu_api.service.product;

import com.dmaqrss.mpu_api.dto.product.ProductRequestDTO;
import com.dmaqrss.mpu_api.dto.product.ProductResponseDTO;
import com.dmaqrss.mpu_api.dto.product.ProductUpdateDTO;
import com.dmaqrss.mpu_api.exception.BusinessException;
import com.dmaqrss.mpu_api.mapper.ProductMapper;
import com.dmaqrss.mpu_api.model.Product;
import com.dmaqrss.mpu_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    ProductService productService;

    @Test
    void shouldCreateProductSucessfully(){
        ProductRequestDTO request = new ProductRequestDTO("test",
                "test",
                2,
                BigDecimal.ONE,
                "test",
                1234L);

        Product product = new Product();

        ProductResponseDTO responseDTO = new ProductResponseDTO("test",
                "test",
                2,
                BigDecimal.ONE,
                "test",
                1234L);

        when(repository.existsByBarCode(request.barCode())).thenReturn(Boolean.FALSE);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponse(product)).thenReturn(responseDTO);
        when(mapper.toEntity(request)).thenReturn(product);

        ProductResponseDTO result =  productService.create(request);

        assertEquals(responseDTO, result);
        verify(repository).save(product);
        verify(mapper).toResponse(product);
    }

    @Test
    void shoudThrowExeptionWhenCreateProductFalid(){
        ProductRequestDTO request = new ProductRequestDTO("test",
                "test",
                2,
                BigDecimal.ONE,
                "test",
                1234L);

        when(repository.existsByBarCode(request.barCode())).thenReturn(Boolean.TRUE);

        assertThrows(BusinessException.class, () -> productService.create(request));
        verify(repository, never()).save(any());
    }

    @Test
    void shoudDeleteProductSucessfuly(){
        Product product = new Product();
        product.setBarCode(1234L);

        when(repository.findByBarCode(product.getBarCode())).thenReturn(Optional.of(product));

        productService.delete(product.getBarCode());

        verify(repository).delete(product);
    }

    @Test
    void shoudThrowExeptionWhenDeleteProductFailed(){
        Product product = new Product();
        product.setBarCode(1234L);

        when(repository.findByBarCode(product.getBarCode())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> productService.delete(product.getBarCode()));
        verify(repository, never()).delete(any());
    }

    @Test
    void shouldUpadateProductSucessfully(){
        ProductUpdateDTO request = new ProductUpdateDTO("test",
                "test",
                2,
                BigDecimal.ONE,
                "test");

        Product product = new Product();
        product.setBarCode(1234L);

        ProductResponseDTO responseDTO = new ProductResponseDTO("test",
                "test",
                2,
                BigDecimal.ONE,
                "test",
                1234L);

        when(repository.findByBarCode(product.getBarCode())).thenReturn(Optional.of(product));
        when(mapper.toResponse(product)).thenReturn(responseDTO);

        productService.update(request, product.getBarCode());

        assertEquals("test", product.getName());
        assertEquals("test", product.getDescription());
        assertEquals(2, product.getAmount());
        assertEquals(BigDecimal.ONE, product.getPrice());
        assertEquals("test", product.getType());
        verify(repository).save(product);
        verify(mapper).toResponse(product);
    }

    @Test
    void shoudThrowExeptionWhenUpdateProductFailed(){
        ProductUpdateDTO request = new ProductUpdateDTO("test",
                "test",
                2,
                BigDecimal.ONE,
                "test");

        Product product = new Product();
        product.setBarCode(1234L);

        when(repository.findByBarCode(product.getBarCode())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> productService.update(request, product.getBarCode()));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldGetProductSucessfully(){
        Product product = new Product();
        product.setBarCode(1234L);

        ProductResponseDTO responseDTO = new ProductResponseDTO("test",
                "test",
                2,
                BigDecimal.ONE,
                "test",
                1234L);

        when(repository.findByBarCode(product.getBarCode())).thenReturn(Optional.of(product));
        when(mapper.toResponse(product)).thenReturn(responseDTO);

        productService.getProduct(product.getBarCode());

        verify(mapper).toResponse(product);
    }

    @Test
    void shoudThrowExeptionWhenGetProductFailed(){
        Product product = new Product();
        product.setBarCode(1234L);

        when(repository.findByBarCode(product.getBarCode())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> productService.getProduct(product.getBarCode()));
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void shouldGetPagedProductsSucessfully() {
        Pageable pageable = PageRequest.of(0, 10);

        Product product = new Product();
        ProductResponseDTO dto = new ProductResponseDTO("test",
                "test",
                2,
                BigDecimal.ONE,
                "test",
                1234L);

        Page<Product> page = new PageImpl<>(List.of(product));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(product)).thenReturn(dto);

        Page<ProductResponseDTO> result = productService.getProducts(pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findAll(pageable);
        verify(mapper).toResponse(product);
    }

    @Test
    void shouldReturnEmptyPageSucessfully() {
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(Page.empty());

        Page<ProductResponseDTO> result = productService.getProducts(pageable);

        assertTrue(result.isEmpty());
    }

}
