package dev.yuri.votacao.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * Validador personalizado que verifica se a data de fim é maior que a data de início
 * em um objeto que possui dois campos de data.
 *
 * Esta classe implementa a interface {@link ConstraintValidator} para realizar a validação
 * de objetos anotados com a anotação {@link DataFimMaiorQueDataInicio}.
 * A validação verifica se a data de fim é posterior à data de início.
 */
public class DataFimMaiorQueDataInicioValidator implements ConstraintValidator<DataFimMaiorQueDataInicio, Object> {
    private String dataInicioField;
    private String dataFimField;

    /**
     * Inicializa o validador com os nomes dos campos de data de início e data de fim
     * definidos na anotação {@link DataFimMaiorQueDataInicio}.
     *
     * @param constraintAnnotation A anotação {@link DataFimMaiorQueDataInicio} associada.
     */
    @Override
    public void initialize(DataFimMaiorQueDataInicio constraintAnnotation) {
        this.dataInicioField = constraintAnnotation.dataInicioField();
        this.dataFimField = constraintAnnotation.dataFimField();
    }

    /**
     * Verifica se a data de fim é posterior à data de início. Caso contrário, adiciona uma violação de
     * constraint ao contexto de validação.
     *
     * @param object O objeto que está sendo validado.
     * @param context O contexto de validação que pode ser usado para adicionar mensagens de erro.
     * @return {@code true} se a validação for bem-sucedida, {@code false} caso contrário.
     */
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

    /**
     * Obtém o valor de um campo de um objeto usando reflexão.
     *
     * @param object O objeto de onde o valor será obtido.
     * @param fieldName O nome do campo.
     * @return O valor do campo.
     */
    private LocalDateTime getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (LocalDateTime) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erro ao acessar o campo: " + fieldName, e);
        }
    }

    /**
     * Adiciona uma violação de constraint ao contexto de validação, com uma mensagem personalizada
     * para o campo de data de fim.
     *
     * @param context O contexto de validação.
     * @param message A mensagem de erro a ser associada à violação.
     * @param fieldNames Os nomes dos campos a serem associados à violação.
     * @return {@code false}, indicando que a validação falhou.
     */
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
