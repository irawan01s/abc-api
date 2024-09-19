package com.abc.api.controllers;

import com.abc.api.entities.Product;
import com.abc.api.payload.request.products.ProductCreateRequest;
import com.abc.api.payload.request.products.ProductSearchRequest;
import com.abc.api.payload.request.products.ProductUpdateRequest;
import com.abc.api.payload.response.PagingResponse;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.products.ProductResponse;
import com.abc.api.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<WebResponse<List<ProductResponse>>> getAll(@RequestParam(value = "title", required = false) String title,
                                                                     @RequestParam(value = "subtitle", required = false) String  subtitle,
                                                                     @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                     @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        ProductSearchRequest request = ProductSearchRequest.builder()
                .page(page)
                .size(size)
                .title(title)
                .subtitle(subtitle)
                .build();

        Page<ProductResponse> productResponses = productService.getAll(request);
        return ResponseEntity.ok(WebResponse.<List<ProductResponse>>builder()
                .status(true)
                .data(productResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(productResponses.getNumber())
                        .totalPage(productResponses.getTotalPages())
                        .size(productResponses.getSize())
                        .build())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<ProductResponse>> getById(@PathVariable Long id) {
        ProductResponse product = productService.getById(id);

        return ResponseEntity.ok(WebResponse.<ProductResponse>builder()
                .status(true)
                .data(product)
                .build());
    }

    @GetMapping("/category/{categoryId}")
    public WebResponse<List<Product>> getByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getByCategory(categoryId);
        return WebResponse.<List<Product>>builder()
                .status(true).data(products).build();
    }

    @PostMapping
    public ResponseEntity<WebResponse<ProductResponse>> createProduct(@RequestBody ProductCreateRequest request) {
        ProductResponse product = productService.create(request);

        return ResponseEntity.ok().body(WebResponse.<ProductResponse>builder()
                .status(true)
                .data(product).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<ProductResponse>> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        ProductResponse response = productService.update(id, request);
        return ResponseEntity.ok().body(WebResponse.<ProductResponse>builder()
                .status(true)
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<?>> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().body(WebResponse.builder()
                .status(true)
                .build());
    }

//    private ProductResponse toProductResponse(Product product) {
//        return ProductResponse.builder()
//                .id(product.getId())
//                .title(product.getTitle())
//                .subtitle(product.getSubtitle())
//                .priceMin(product.getPriceMin())
//                .priceMax(product.getPriceMax())
//                .unit(product.getUnit())
//                .description(product.getDescription())
//                .location(product.getLocation())
//                .notes(product.getNotes())
//                .createdAt(product.getCreatedAt())
//                .updatedAt(product.getUpdatedAt())
//                .category(CategoryResponse.builder()
//                        .id(product.getCategory().getId())
//                        .name(product.getCategory().getName())
//                        .description(product.getCategory().getDescription())
//                        .build())
//                .build();
//    }
}
