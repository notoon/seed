/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.shell.commands;

import org.seedstack.seed.core.spi.command.CommandDefinition;
import org.seedstack.seed.core.spi.command.Command;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * This command converts a map to a properties flat format.
 *
 * @author adrien.lauer@mpsa.com
 */
@CommandDefinition(scope = "", name = "properties", description = "Convert a map to properties format")
public class PropertiesCommand implements Command {
    @Override
    public Object execute(Object object) throws IOException {
        if (object == null) {
            return null;
        }

        if (object instanceof Map) {
            StringWriter stringWriter = new StringWriter();
            Properties properties = new Properties();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
                properties.put(
                        entry.getKey() == null ? "null" : entry.getKey().toString(),
                        entry.getValue() == null ? "null" : entry.getValue().toString()
                );
            }
            properties.putAll((Map) object);
            properties.store(stringWriter, null);

            return stringWriter.getBuffer().toString();
        } else {
            throw new IllegalArgumentException("input object need to implement java.util.Map");
        }
    }
}
