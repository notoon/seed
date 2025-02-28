/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.it.spi;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.util.List;
import java.util.Map;

/**
 * A simple plugin for the {@link org.seedstack.seed.it.SeedITRunner} to add a Rules to the test.
 *
 * @author yves.dautremay@mpsa.com
 */
public interface ITRunnerPlugin {
    /**
     * The plugin can provide the class rules to apply to the test class. The provided
     * rules will be given to the kernel injector so the rules can use Inject
     * annotation.
     *
     * @param testClass the test class definition
     * @return A list of class rules to be applied to the test. An empty list if no
     * rule is to be applied.
     */
    List<Class<? extends TestRule>> provideClassRulesToApply(TestClass testClass);

    /**
     * The plugin can provide the rules to apply to the test object. The provided
     * rules will be given to the kernel injector so the rules can use Inject
     * annotation.
     *
     * @param testClass the test class definition
     * @param target    the test object
     * @return A list of rules to be applied to the test. An empty list if no
     * rule is to be applied.
     */
    List<Class<? extends TestRule>> provideTestRulesToApply(TestClass testClass, Object target);

    /**
     * The plugin can provide the rules to apply to each test method. The provided
     * rules will be given to the kernel injector so the rules can use Inject
     * annotation.
     *
     * @param testClass the test class definition
     * @param target    the test object
     * @return A list of rules to be applied to each test method. An empty list if no
     * rule is to be applied.
     */
    List<Class<? extends MethodRule>> provideMethodRulesToApply(TestClass testClass, Object target);

    /**
     * The plugin can provide a default configuration for the started kernel.
     *
     * @param testClass the test class definition
     * @param method    the test method if the kernel is created per test, null otherwise.
     * @return the default configuration map
     */
    Map<String, String> provideDefaultConfiguration(TestClass testClass, FrameworkMethod method);

    /**
     * The plugin can choose a kernel mode for the test. If multiple plugins require incompatible modes, an exception
     * will be thrown by the SEED IT runner.
     *
     * @param testClass the test class definition
     * @return the requested kernel mode
     */
    ITKernelMode kernelMode(TestClass testClass);
}
