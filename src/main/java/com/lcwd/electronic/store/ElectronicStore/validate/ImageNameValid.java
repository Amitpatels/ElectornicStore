package com.lcwd.electronic.store.ElectronicStore.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.lang.reflect.Field;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {

    String message() default "Invalid Image Name !!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
