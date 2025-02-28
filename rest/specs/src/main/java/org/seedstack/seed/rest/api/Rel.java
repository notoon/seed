/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.rest.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a resource's relation type.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Rel {

    /**
     * @return the relation type
     */
    String value();

    /**
     * Defines whether the resource should be exposed as an entry point by JSON-HOME.
     *
     * @return true if the resource should be exposed, false otherwise
     */
    boolean home() default false;
}
