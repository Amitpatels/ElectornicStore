package com.lcwd.electronic.store.ElectronicStore.helper;

import com.lcwd.electronic.store.ElectronicStore.dtos.common.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class PageableResponseUtility {

    public static <U,V>PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){
        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }

    public static Pageable getPageableSortObject(int pageNumber, int pageSize, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNumber,pageSize,sort);
    }
}
