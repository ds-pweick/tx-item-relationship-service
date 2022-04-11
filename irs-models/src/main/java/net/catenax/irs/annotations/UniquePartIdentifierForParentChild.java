//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.irs.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import net.catenax.irs.dtos.PartRelationship;
import net.catenax.irs.validators.UniquePartIdentifierForParentChildValidator;

/**
 * Custom annotation to validate {@link PartRelationship} input.
 */
@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UniquePartIdentifierForParentChildValidator.class)
@ExcludeFromCodeCoverageGeneratedReport
public @interface UniquePartIdentifierForParentChild {
    /***
     * Defines the message that will be showed when the input data is not valid.
     * @return Validation message.
     */
    String message() default "Invalid part relationship";

    /**
     * Groups to which the constraint belongs.
     *
     * @return see {@link Class}
     */
    Class<?>[] groups() default { };

    /**
     * An implementation of {@link Payload}.
     *
     * @return see {@link Class}
     */
    Class<? extends Payload>[] payload() default { };
}
