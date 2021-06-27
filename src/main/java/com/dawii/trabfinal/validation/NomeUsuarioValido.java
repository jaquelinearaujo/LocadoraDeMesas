package com.dawii.trabfinal.validation;

import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NomeUsuarioValido {
	
	String message() default "Nome de usuário inválido";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}