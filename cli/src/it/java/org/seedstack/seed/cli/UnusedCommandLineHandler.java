/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.cli;

import org.seedstack.seed.cli.api.CliCommand;
import org.seedstack.seed.cli.api.CommandLineHandler;

/**
 * @author adrien.lauer@mpsa.com
 */
@CliCommand("test2")
public class UnusedCommandLineHandler implements CommandLineHandler {
    static boolean called = false;

    @Override
    public Integer call() throws Exception {
        called = true;
        return 0;
    }
}
