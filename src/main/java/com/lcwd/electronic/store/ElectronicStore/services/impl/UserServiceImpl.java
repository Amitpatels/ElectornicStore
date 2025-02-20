package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.PageableResponseUtility;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
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
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;



    @Override
    public UserDto createUser(UserDto userDto) {

        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);

        UserDto createdDto = entityToDto(savedUser);

        return createdDto;
    }


    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with given id!!!"));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        user.setUserProfileImageName(userDto.getUserProfileImageName());

        User updatedUser = userRepository.save(user);

        return entityToDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with give id!!!"));

        //delete userProfile Image first
        String imageFullPath = imageUploadPath + user.getUserProfileImageName();
        try{
            Path path = Paths.get(imageFullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("User profile image was not found in path : {}",imageFullPath);
            ex.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }



        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<User> page = userRepository.findAll(pageable);

        List<User> users = page.getContent();

        List<UserDto> userDtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

        PageableResponse<UserDto> pageableResponse = PageableResponseUtility.getPageableResponse(page, UserDto.class);

        return pageableResponse;

    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with given id!!!"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmailId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email!!!"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> usersDto = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return usersDto;
    }

    private UserDto entityToDto(User savedUser) {
         /*return UserDto.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .about(savedUser.getAbout())
                .gender(savedUser.getGender())
                .userProfileImageName(savedUser.getUserProfileImageName())
                .build();
          */
        return modelMapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
        /*return User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .about(userDto.getAbout())
                .password(userDto.getPassword())
                .userProfileImageName(userDto.getUserProfileImageName())
                .build();
         */
        return modelMapper.map(userDto,User.class);
    }
}
