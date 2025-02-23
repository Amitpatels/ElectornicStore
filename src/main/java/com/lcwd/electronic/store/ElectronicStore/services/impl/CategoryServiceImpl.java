package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.PageableResponseUtility;
import com.lcwd.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.cover.image.path}")
    private String imageUploadPath;

    Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category = dtoToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return entityToDto(category);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception !!"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found exception !!"));

        //delete product image first
        String imageFullPath = imageUploadPath + category.getCoverImage();
        try{
            Path path = Paths.get(imageFullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Product image was not found in path : {}",imageFullPath);
            ex.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> page = categoryRepository.findAll(pageable);

        List<Category> categories = page.getContent();

        List<CategoryDto> categoryDtos = categories.stream().map(category -> entityToDto(category)).collect(Collectors.toList());

        PageableResponse<CategoryDto> pageableResponse = PageableResponseUtility.getPageableResponse(page,CategoryDto.class);

        return pageableResponse;
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception !!"));
        return mapper.map(category,CategoryDto.class);
    }

    private Category dtoToEntity(CategoryDto categoryDto){
        return mapper.map(categoryDto,Category.class);
    }

    private CategoryDto entityToDto(Category category){
        return mapper.map(category,CategoryDto.class);
    }

}
