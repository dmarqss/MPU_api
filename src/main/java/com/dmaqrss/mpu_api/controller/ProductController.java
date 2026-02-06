package com.dmaqrss.mpu_api.controller;

import com.dmaqrss.mpu_api.dto.ProductRequestDTO;
import com.dmaqrss.mpu_api.dto.ProductResponseDTO;
import com.dmaqrss.mpu_api.dto.ProductUpdateDTO;
import com.dmaqrss.mpu_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "products")
public class ProductController {

    @Autowired
    private ProductService service;


    @PostMapping
    @Operation(
            summary = "Create a product",
            description = "Creates a new product with the given information"
    )
    public ResponseEntity<ProductResponseDTO> createUser(@RequestBody @Valid ProductRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @DeleteMapping(path = "/{barCode}")
    @Operation(
            summary = "Delete a product",
            description = "Deletes product by its barcode"
    )
    public ResponseEntity<?> delete(@PathVariable Long barCode){
        service.delete(barCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(path = "/{barCode}")
    @Operation(
            summary = "Update a product",
            description = "Updates product data by barcode"
    )
    public ResponseEntity<ProductResponseDTO> update(@RequestBody @Valid ProductUpdateDTO dto, @PathVariable Long barCode){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(dto,barCode));

    }

    @GetMapping(path = "/{barCode}")
    @Operation(
            summary = "Get product by barcode",
            description = "return a product by its barcode"
    )
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long barCode){
        return ResponseEntity.status(HttpStatus.OK).body(service.getProduct(barCode));
    }

    @GetMapping
    @Operation(
            summary = "Get a paginated list of products",
            description = "Return all products with pagination and sorting"
    )
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(@PageableDefault(page = 0, size = 2, sort = "name", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(service.getProducts(pageable));
    }
}
