/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.cli.api;

import org.seedstack.seed.core.api.ErrorCode;

/**
 * Enumerates all error codes for command line support.
 *
 * @author adrien.lauer@mpsa.com
 */
public enum CliErrorCode implements ErrorCode {
    COMMAND_LINE_HANDLER_NOT_FOUND,
    EXCEPTION_OCCURRED_BEFORE_CLI_TEST,
    NO_COMMAND_SPECIFIED,
    ERROR_PARSING_COMMAND_LINE,
    UNABLE_TO_INJECT_OPTION,
    MISSING_ARGUMENTS,
    UNABLE_TO_INJECT_ARGUMENTS,
    UNSUPPORTED_OPTION_FIELD_TYPE,
    ODD_NUMBER_OF_OPTION_ARGUMENTS,
    WRONG_NUMBER_OF_OPTION_ARGUMENTS,
    UNEXPECTED_EXCEPTION
}
