package dev.yuri.votacao.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class DataFimMaiorQueDataInicioValidator implements ConstraintValidator<DataFimMaiorQueDataInicio, Object> {

    private String dataInicioField;
    private String dataFimField;

    @Override
    public void initialize(DataFimMaiorQueDataInicio constraintAnnotation) {
        this.dataInicioField = constraintAnnotation.dataInicioField();
        this.dataFimField = constraintAnnotation.dataFimField();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }

        LocalDateTime dataInicio = getFieldValue(object, dataInicioField);
        LocalDateTime dataFim = getFieldValue(object, dataFimField);

        if (dataInicio == null || dataFim == null) {
           return true;
        }

        if (!dataFim.isAfter(dataInicio)) {
            return buildViolation(context, "A data de fim deve ser maior que a data de início.", dataFimField);
        }

        return true;
    }

    private LocalDateTime getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (LocalDateTime) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erro ao acessar o campo: " + fieldName, e);
        }
    }

    private boolean buildViolation(ConstraintValidatorContext context, String message, String... fieldNames) {
        context.disableDefaultConstraintViolation();
        for (String fieldName : fieldNames) {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
        }
        return false;
    }
}
