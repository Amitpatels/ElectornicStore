package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;
    @Size(min =3, max=15, message = "Invalid name!!")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "Invalid user Email!!")
    @NotBlank(message = "Email is required !!")
    private String email;
    @NotBlank(message = "Password is required!!")
    private String password;
    @Size(min=4,max=6, message = "Invalid gender!!")
    private String gender;
    @NotBlank(message = "Write something about yourself !!")
    private String about;
    @ImageNameValid
    private String userProfileImageName;
    private List<RoleDto> roles;
}
