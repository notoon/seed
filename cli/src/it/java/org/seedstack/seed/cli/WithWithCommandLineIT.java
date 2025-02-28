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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.seedstack.seed.cli.api.WithCommandLine;
import org.seedstack.seed.it.AbstractSeedIT;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class WithWithCommandLineIT extends AbstractSeedIT {
    private static boolean passedBeforeClass = false;
    private static boolean passedBefore = false;

    @Inject
    private Fixture fixture;

    @BeforeClass
    public static void beforeClass() {
        assertThat(passedBeforeClass).isFalse();
        assertThat(passedBefore).isFalse();
        assertThat(DummyCommandLineHandler.called).isFalse();
        passedBeforeClass = true;
    }

    @Before
    public void before() {
        assertThat(passedBeforeClass).isTrue();
        assertThat(passedBefore).isFalse();
        assertThat(DummyCommandLineHandler.called).isFalse();
        passedBefore = true;
    }

    @Test
    @WithCommandLine(args = { "arg0", "arg1", "--option=value" }, expectedExitCode = 255, command = "dummy")
    public void test_with_annotation() {
        assertThat(passedBeforeClass).isTrue();
        assertThat(passedBefore).isTrue();
        assertThat(DummyCommandLineHandler.called).isTrue();
        assertThat(fixture).isNotNull();
    }
}
