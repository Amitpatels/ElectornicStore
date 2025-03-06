package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.GenericMapper;
import com.lcwd.electronic.store.ElectronicStore.helper.PageableResponseUtility;
import com.lcwd.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GenericMapper mapper;

    @Value("${product.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductDto create(ProductDto productDto) {

        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = productRepository.save(mapper.dtoToEntity(productDto, Product.class));
        return mapper.entityToDto(product,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found of given id !!"));
        product.setDescription(productDto.getDescription());
        product.setLive(productDto.isLive());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setTitle(productDto.getTitle());
        product.setQuantity(productDto.getQuantity());
        product.setStock(productDto.isStock());
        product.setAddedDate(productDto.getAddedDate());
        product.setProductImageName(productDto.getProductImageName());

        return mapper.entityToDto(productRepository.save(product),ProductDto.class);
    }

    @Override
    public ProductDto getProductById(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found of given id !!"));
        return mapper.entityToDto(product,ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found of given id !!"));

        //delete product image first
        String imageFullPath = imageUploadPath + product.getProductImageName();
        try{
            Path path = Paths.get(imageFullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex){
            logger.info("Product image was not found in path : {} ", imageFullPath);
            ex.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        productRepository.delete(product);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageableResponseUtility.getPageableSortObject(pageNumber,pageSize,sortBy,sortDir);
        Page<Product> pageList = productRepository.findAll(pageable);
        return PageableResponseUtility.getPageableResponse(pageList,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageableResponseUtility.getPageableSortObject(pageNumber,pageSize,sortBy,sortDir);
        Page<Product> pageList = productRepository.findByLiveTrue(pageable);
        return PageableResponseUtility.getPageableResponse(pageList,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageableResponseUtility.getPageableSortObject(pageNumber,pageSize,sortBy,sortDir);
        Page<Product> pageList = productRepository.findByTitleContaining(subTitle,pageable);
        return PageableResponseUtility.getPageableResponse(pageList,ProductDto.class);
    }

    @Override
    public ProductDto createProductWithCategoryId(ProductDto productDto, String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found for given id !!"));

        Product product  = mapper.entityToDto(productDto,Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product saveProduct = productRepository.save(product);
        return mapper.entityToDto(product,ProductDto.class);
    }

    @Override
    public ProductDto updateCategoryIdWithProductId(String categoryId,String productId) {

        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found for given id !!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found for given category id !!"));

        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.entityToDto(savedProduct,ProductDto.class);
    }

    public PageableResponse<ProductDto> getAllCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found for given category id !!"));
        Pageable pageable = PageableResponseUtility.getPageableSortObject(pageNumber,pageSize,sortBy,sortDir);
        Page<Product> pages = productRepository.findByCategory(category,pageable);
        return PageableResponseUtility.getPageableResponse(pages,ProductDto.class);
    }


}
