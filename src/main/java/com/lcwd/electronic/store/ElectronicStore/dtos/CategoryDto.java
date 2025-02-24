package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private String categoryId;
    @NotBlank(message="Title required!! ")
    @Size(min = 4, message = "title must be of minimum 4 characters !!")
    private String title;
    @NotBlank(message = "Description required !!")
    private String description;
    @NotBlank
    private String coverImage;
}
