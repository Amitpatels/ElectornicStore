package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;

import java.util.List;

public interface ProductService {

    ProductDto create(ProductDto productDto);

    ProductDto update(ProductDto productDto, String productId);

    ProductDto getProductById(String productId);

    void delete(String productId);

    PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir);

    ProductDto createProductWithCategoryId(ProductDto productDto, String categoryId);

    ProductDto  updateCategoryIdWithProductId(String categoryId,String productId);

    PageableResponse<ProductDto> getAllCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir);
}
