/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.core.api;

/**
 * SEED core error codes.
 *
 * @author adrien.lauer@mpsa.com
 */
public enum CoreErrorCode implements ErrorCode {
    UNABLE_TO_INSTANTIATE_MODULE,
    UNABLE_TO_INJECT_LOGGER,
    UNABLE_TO_FIND_CLASSLOADER,
    UNABLE_TO_LOAD_SEED_BOOTSTRAP,
    UNEXPECTED_EXCEPTION,
    UNABLE_TO_CREATE_DIAGNOSTIC_COLLECTOR,
    MISSING_SEED_ENTRY_POINT,
    MULTIPLE_SEED_ENTRY_POINTS,
    RETROW_EXCEPTION_AFTER_DIAGNOSTIC_FAILURE,
    MISSING_GENERIC_PARAMETER
}
