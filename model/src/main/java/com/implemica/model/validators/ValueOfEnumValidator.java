package com.implemica.model.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Implements {@link ConstraintValidator} for validation {@link String} field is the name of enum.
 *
 * @see ValueOfEnum
 * @see ConstraintValidator
 */
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

    /**
     * Contains a list of names enum.
     */
    private List<String> acceptedValues;

    /**
     * Initializes {@link #acceptedValues} names of enum from {@link ValueOfEnum}.
     *
     * @param annotation annotation instance for a given constraint declaration.
     */
    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Checks that the {@code value} is in {@code acceptedValue}.
     *
     * @param value object to validate.
     * @param context context in which the constraint is evaluated.
     *
     * @return false if value does not pass the constraint
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value);
    }
}
