/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.security.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;
import com.google.inject.spi.PrivateElements;
import org.apache.shiro.mgt.SecurityManager;
import org.seedstack.seed.core.api.SeedException;
import org.seedstack.seed.security.api.Scope;
import org.seedstack.seed.security.internal.configure.SeedSecurityConfigurer;
import org.seedstack.seed.security.internal.data.DataSecurityModule;
import org.seedstack.seed.security.internal.securityexpr.SecurityExpressionModule;
import org.seedstack.seed.security.spi.SecurityConcern;
import org.seedstack.seed.security.spi.SecurityErrorCodes;
import org.seedstack.seed.security.spi.data.DataSecurityHandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@SecurityConcern
class SecurityModule extends AbstractModule {
    private static final Key<org.apache.shiro.mgt.SecurityManager> SECURITY_MANAGER_KEY = Key.get(SecurityManager.class);

    private final Map<String, Class<? extends Scope>> scopeClasses;
    private final SeedSecurityConfigurer securityConfigurer;
    private final boolean elDisabled;
    private final Collection<Class<? extends DataSecurityHandler<?>>> dataSecurityHandlers;
    private final Collection<SecurityProvider> securityProviders;


    SecurityModule(SeedSecurityConfigurer securityConfigurer, Map<String, Class<? extends Scope>> scopeClasses, Collection<Class<? extends DataSecurityHandler<?>>> dataSecurityHandlers, boolean elDisabled, Collection<SecurityProvider> securityProviders) {
        this.securityConfigurer = securityConfigurer;
        this.scopeClasses = scopeClasses;
        this.dataSecurityHandlers = dataSecurityHandlers;
        this.elDisabled = elDisabled;
        this.securityProviders = securityProviders;
    }

    @Override
    protected void configure() {
        install(new SecurityInternalModule(securityConfigurer, scopeClasses));
        install(new SecurityAopModule());

        if (!elDisabled) {
            install(new SecurityExpressionModule());
            install(new DataSecurityModule(dataSecurityHandlers));
        }

        Module installedMainModule = null;
        for (SecurityProvider securityProvider : securityProviders) {
            Module mainSecurityModule = securityProvider.provideMainSecurityModule();
            if (mainSecurityModule != null) {
                if (installedMainModule == null) {
                    installedMainModule = mainSecurityModule;
                    install(mainSecurityModule);
                } else {
                    throw SeedException
                            .createNew(SecurityErrorCodes.MULTIPLE_MAIN_SECURITY_MODULES)
                            .put("first", installedMainModule.getClass().getCanonicalName())
                            .put("second", mainSecurityModule.getClass().getCanonicalName());
                }
            }

            Module additionalSecurityModule = securityProvider.provideAdditionalSecurityModule();
            if (additionalSecurityModule != null) {
                install(removeSecurityManager(additionalSecurityModule));
            }
        }

        if (installedMainModule == null) {
            install(new DefaultShiroModule());
        }
    }

    private Module removeSecurityManager(Module module) {
        List<Element> elements = Elements.getElements(module);
        // ShiroModule is only a private module
        final PrivateElements privateElements = (PrivateElements) elements.iterator().next();

        return new PrivateModule() {
            @Override
            protected void configure() {
                for (Element element : privateElements.getElements()) {
                    if (!(element instanceof ConstructorBinding) || !((ConstructorBinding) element).getKey().equals(SECURITY_MANAGER_KEY)) {
                        element.applyTo(binder());
                    }
                }

                for (Key<?> exposedKey : privateElements.getExposedKeys()) {
                    if (!exposedKey.equals(SECURITY_MANAGER_KEY)) {
                        expose(exposedKey);
                    }
                }
            }
        };
    }
}
