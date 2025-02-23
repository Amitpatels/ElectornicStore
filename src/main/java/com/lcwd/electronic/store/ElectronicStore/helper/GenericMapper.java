package com.lcwd.electronic.store.ElectronicStore.helper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper {

    @Autowired
    private ModelMapper mapper;

    public <D, E> E dtoToEntity(D dto, Class<E> entityClass){
        return mapper.map(dto,entityClass);
    }

    public <E,D> D entityToDto(E entity, Class<D> dtoClass){
        return mapper.map(entity,dtoClass);
    }

}
