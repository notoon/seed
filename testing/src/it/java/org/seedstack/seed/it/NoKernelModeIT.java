/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.it;

import org.junit.Test;
import org.seedstack.seed.core.api.Configuration;
import org.seedstack.seed.it.fixtures.TestKernelRule;
import org.seedstack.seed.it.fixtures.WithTestAnnotation;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class NoKernelModeIT extends AbstractSeedIT {
    @Inject
    TestKernelRule testKernelRule;

    @Configuration(value = "testKey")
    private String testKey;

    @Configuration(value = "foo1", mandatory = false)
    private String foo1;

    @Configuration(value = "foo2", mandatory = false)
    private String foo2;

    @Test
    @WithTestAnnotation(key = "foo1", value = "bar1")
    public void method_annotation_is_used_by_rule_1() {
        assertThat(testKernelRule).isNotNull();
        assertThat(testKey).isEqualTo("testValue");
        assertThat(foo1).isEqualTo("bar1");
    }

    @Test
    @WithTestAnnotation(key = "foo2", value = "bar2")
    public void method_annotation_is_used_by_rule_2() {
        assertThat(testKernelRule).isNotNull();
        assertThat(testKey).isEqualTo("testValue");
        assertThat(foo1).isNull();
        assertThat(foo2).isEqualTo("bar2");
    }
}
