//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.irs.exceptions;

import net.catenax.irs.annotations.ExcludeFromCodeCoverageGeneratedReport;

/**
 * General entity not found exception.
 */
@ExcludeFromCodeCoverageGeneratedReport
public class EntityNotFoundException extends RuntimeException {
    /**
     * Generate a new instance of a {@link EntityNotFoundException}
     *
     * @param message Exception message.
     */
    public EntityNotFoundException(final String message) {
        super(message);
    }

    public EntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
