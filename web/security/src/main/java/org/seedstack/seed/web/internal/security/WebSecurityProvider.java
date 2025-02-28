/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.web.internal.security;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.plugin.PluginException;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequestBuilder;
import jodd.props.Props;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.seedstack.seed.core.api.SeedException;
import org.seedstack.seed.core.internal.application.ApplicationPlugin;
import org.seedstack.seed.security.internal.SecurityProvider;
import org.seedstack.seed.web.api.WebErrorCode;
import org.seedstack.seed.web.api.security.SecurityFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This plugins adds web security.
 *
 * @author yves.dautremay@mpsa.com
 */
public class WebSecurityProvider implements SecurityProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityProvider.class);

    private ServletContext servletContext;

    private Collection<Class<? extends Filter>> scannedFilters = new ArrayList<Class<? extends Filter>>();

    private String applicationId;

    private Props props;

    @SuppressWarnings("unchecked")
    @Override
    public void init(InitContext initContext) {
        ApplicationPlugin applicationPlugin = null;
        for (Plugin plugin : initContext.pluginsRequired()) {
            if (plugin instanceof ApplicationPlugin) {
                applicationPlugin = ((ApplicationPlugin) plugin);
            }
        }

        if (applicationPlugin == null) {
            throw SeedException.createNew(WebErrorCode.PLUGIN_NOT_FOUND).put("plugin", "application");
        }

        props = applicationPlugin.getProps();
        applicationId = applicationPlugin.getApplication().getId();
        for (Class<?> filterClass : initContext.scannedClassesByAnnotationClass().get(SecurityFilter.class)) {
            if (Filter.class.isAssignableFrom(filterClass)) {
                scannedFilters.add((Class<? extends Filter>) filterClass);
            } else {
                throw new PluginException("Annotated class " + filterClass.getName() + " must implement Filter to be used in a filter chain");
            }
        }
    }

    @Override
    public void provideContainerContext(Object containerContext) {
        if (containerContext instanceof ServletContext) {
            servletContext = (ServletContext) containerContext;
        }
    }

    @Override
    public void classpathScanRequests(ClasspathScanRequestBuilder classpathScanRequestBuilder) {
        classpathScanRequestBuilder.annotationType(SecurityFilter.class);
    }

    @Override
    public Module provideMainSecurityModule() {
        if (servletContext != null) {
            return Modules.combine(
                    new WebSecurityModule(servletContext, props, scannedFilters, applicationId),
                    ShiroWebModule.guiceFilterModule()
            );
        } else {
            LOGGER.warn("No servlet context available, Web security disabled.");
            return null;
        }
    }

    @Override
    public Module provideAdditionalSecurityModule() {
        return null;
    }
}