package com.lcwd.electronic.store.ElectronicStore.controller;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.ImageResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
//@CrossOrigin(origins = "http://abc.com")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imageUploadPath;

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        ProductDto createdProduct =  productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId){
        ProductDto updatedProduct = productService.update(productDto,productId);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable("productId") String productId){
        productService.delete(productId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Product deleted successfully !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId){
        return new ResponseEntity<>(productService.getProductById(productId),HttpStatus.OK);
    }

    //search all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
            @RequestParam(value = "pageNumber", defaultValue="0",required= false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue="10", required=false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return new ResponseEntity<>(productService.getAllProduct(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    //get all live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductLive(
            @RequestParam(value = "pageNumber", defaultValue="0",required= false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue="10", required=false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return new ResponseEntity<>(productService.getAllLive(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @PathVariable String query,
            @RequestParam(value = "pageNumber", defaultValue="0",required= false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue="10", required=false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return new ResponseEntity<>(productService.searchByTitle(query,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImages(@RequestParam("productImage") MultipartFile productImage, @PathVariable String productId) throws IOException {
        String imageName = fileService.uploadFile(productImage,imageUploadPath);

        ProductDto productDto = productService.getProductById(productId);
        productDto.setProductImageName(imageName);

        ProductDto updatedProductWithImageName = productService.update(productDto,productId);

        ImageResponse response = ImageResponse.builder()
                .name(imageName)
                .message("Product Image uploaded successfully !!")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.getProductById(productId);
        logger.info("Product image name : {} ", productDto.getProductImageName());
        InputStream resource = fileService.getResource(imageUploadPath,productDto.getProductImageName());
        response.setContentType(MediaType.ALL_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
