package dev.yuri.votacao.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DataFimMaiorQueDataInicioValidator.class)
public @interface DataFimMaiorQueDataInicio {
    String message() default "A data de fim deve ser maior que a data de início";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String dataInicioField();
    String dataFimField();
}