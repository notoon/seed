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

import com.google.inject.Key;
import com.google.inject.name.Names;
import jodd.props.Props;
import jodd.props.PropsEntries;
import jodd.props.PropsEntry;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.seedstack.seed.security.spi.SecurityConcern;
import org.seedstack.seed.web.api.security.SecurityFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Guice module to initialize a Shiro environment for the Web entry point.
 * 
 * @author yves.dautremay@mpsa.com
 */
@SecurityConcern
class WebSecurityModule extends ShiroWebModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroWebModule.class);
    private static final String PROPERTIES_PREFIX = "org.seedstack.seed.security.urls";
    private static final Map<String, Key<? extends Filter>> DEFAULT_FILTERS = new HashMap<String, Key<? extends Filter>>();

    static {
        DEFAULT_FILTERS.put("anon", ANON);
        DEFAULT_FILTERS.put("authc", AUTHC);
        DEFAULT_FILTERS.put("authcBasic", AUTHC_BASIC);
        DEFAULT_FILTERS.put("logout", LOGOUT);
        DEFAULT_FILTERS.put("noSessionCreation", NO_SESSION_CREATION);
        DEFAULT_FILTERS.put("perms", PERMS);
        DEFAULT_FILTERS.put("port", PORT);
        DEFAULT_FILTERS.put("rest", REST);
        DEFAULT_FILTERS.put("roles", ROLES);
        DEFAULT_FILTERS.put("ssl", SSL);
        DEFAULT_FILTERS.put("user", USER);
    }
    private final String applicationName;
    private final Props props;
    private final Collection<Class<? extends Filter>> customFilters;

    WebSecurityModule(ServletContext servletContext, Props props, Collection<Class<? extends Filter>> customFilters, String applicationName) {
        super(servletContext);
        this.props = props;
        this.customFilters = customFilters;
        this.applicationName = applicationName;
    }

    @Override
    protected void configureShiroWeb() {
        PropsEntries propsEntries = props.entries().activeProfiles().section(PROPERTIES_PREFIX);
        Iterator<PropsEntry> entries = propsEntries.iterator();
        while (entries.hasNext()) {
            LOGGER.info("Binding urls to filters...");
            PropsEntry entry = entries.next();
            String url = org.apache.commons.lang.StringUtils.removeStart(entry.getKey(), PROPERTIES_PREFIX + ".");
            String[] filters = StringUtils.split(entry.getValue(), StringUtils.DEFAULT_DELIMITER_CHAR, '[', ']', true, true);
            addFilterChain(url, getFilterKeys(filters));
        }
        if (applicationName != null) {
            bindConstant().annotatedWith(Names.named("shiro.applicationName")).to(applicationName);
        }
    }
    
    @SuppressWarnings("unchecked")
    private Key<? extends Filter>[] getFilterKeys(String[] filters) {
        Key<? extends Filter>[] keys = new Key[filters.length];
        int index = 0;
        for (String filter : filters) {
            String[] nameConfig = toNameConfigPair(filter);
            Key<? extends Filter> currentKey = findKey(nameConfig[0]);
            if (currentKey != null) {
                if (!org.apache.commons.lang.StringUtils.isEmpty(nameConfig[1])) {
                    keys[index] = config((Key<? extends PathMatchingFilter>) currentKey, nameConfig[1]);
                } else {
                    keys[index] = currentKey;
                }
            } else {
                addError("The filter [" + nameConfig[0] + "] could not be found as a default filter or as a class annotated with SecurityFilter");
            }
            index++;
        }
        return keys;
    }

    private Key<? extends Filter> findKey(String filterName) {
        Key<? extends Filter> currentKey = null;
        if (DEFAULT_FILTERS.containsKey(filterName)) {
            currentKey = DEFAULT_FILTERS.get(filterName);
        } else {
            for (Class<? extends Filter> filterClass : customFilters) {
                String name = filterClass.getAnnotation(SecurityFilter.class).value();
                if (filterName.equals(name)) {
                    currentKey = Key.get(filterClass);
                }
            }
        }
        return currentKey;
    }
    
    /**
     * This method is copied from the same method in Shiro in class DefaultFilterChainManager.
     */
    private String[] toNameConfigPair(String token) throws ConfigurationException {

        String[] pair = token.split("\\[", 2);
        String name = StringUtils.clean(pair[0]);

        if (name == null) {
            throw new IllegalArgumentException("Filter name not found for filter chain definition token: " + token);
        }
        String config = null;

        if (pair.length == 2) {
            config = StringUtils.clean(pair[1]);
            //if there was an open bracket, it assumed there is a closing bracket, so strip it too:
            config = config.substring(0, config.length() - 1);
            config = StringUtils.clean(config);

            //backwards compatibility prior to implementing SHIRO-205:
            //prior to SHIRO-205 being implemented, it was common for end-users to quote the config inside brackets
            //if that config required commas.  We need to strip those quotes to get to the interior quoted definition
            //to ensure any existing quoted definitions still function for end users:
            if (config != null && config.startsWith("\"") && config.endsWith("\"")) {
                String stripped = config.substring(1, config.length() - 1);
                stripped = StringUtils.clean(stripped);

                //if the stripped value does not have any internal quotes, we can assume that the entire config was
                //quoted and we can use the stripped value.
                if (stripped != null && stripped.indexOf('"') == -1) {
                    config = stripped;
                }
                //else:
                //the remaining config does have internal quotes, so we need to assume that each comma delimited
                //pair might be quoted, in which case we need the leading and trailing quotes that we stripped
                //So we ignore the stripped value.
            }
        }
        
        return new String[]{name, config};

    }
}
