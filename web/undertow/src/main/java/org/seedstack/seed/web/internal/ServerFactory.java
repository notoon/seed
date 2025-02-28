/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.web.internal;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentManager;
import org.seedstack.seed.core.api.SeedException;
import org.seedstack.seed.crypto.internal.SslConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.Options;
import org.xnio.SslClientAuthMode;

import javax.servlet.ServletException;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class ServerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerFactory.class);

    public Undertow createServer(ServerConfig serverConfig, DeploymentManager manager) {
        PathHandler path = null;
        try {
            path = Handlers.path(Handlers.redirect(serverConfig.getContextPath()))
                    .addPrefixPath(serverConfig.getContextPath(), manager.start());
        } catch (ServletException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return configureBuilder(serverConfig)
                .setHandler(path)
                .build();
    }

    private Undertow.Builder configureBuilder(ServerConfig serverConfig) {
        Undertow.Builder builder;
        if (serverConfig.isHttpsEnabled()) {
            builder = configureHttpsListener(serverConfig);
        } else {
            builder = configureHttpListener(serverConfig.getHost(), serverConfig.getPort());
        }

        if (serverConfig.getBufferSize() != null) {
            builder.setBufferSize(serverConfig.getBufferSize());
        }
        if (serverConfig.getBuffersPerRegion() != null) {
            builder.setBuffersPerRegion(serverConfig.getBuffersPerRegion());
        }
        if (serverConfig.getIoThreads() != null) {
            builder.setIoThreads(serverConfig.getIoThreads());
        }
        if (serverConfig.getWorkerThreads() != null) {
            builder.setWorkerThreads(serverConfig.getWorkerThreads());
        }
        if (serverConfig.getDirectBuffers() != null) {
            builder.setDirectBuffers(serverConfig.getDirectBuffers());
        }
        builder.setServerOption(UndertowOptions.ENABLE_HTTP2, serverConfig.isHttp2Enabled());
        return builder;
    }

    private Undertow.Builder configureHttpListener(String host, int port) {
        return Undertow.builder().addHttpListener(port, host);
    }

    private Undertow.Builder configureHttpsListener(ServerConfig serverConfig) {
        SslConfig ssl = serverConfig.getSslConfig();
        if (ssl == null || serverConfig.getSslContext() == null) {
            throw SeedException.createNew(UndertowErrorCode.MISSING_SSL_CONFIGURATION);
        }
        try {

            return Undertow.builder()
                    .addHttpsListener(serverConfig.getPort(), serverConfig.getHost(), serverConfig.getSslContext())
                    .setSocketOption(Options.SSL_CLIENT_AUTH_MODE, SslClientAuthMode.valueOf(ssl.getClientAuthMode()));

        } catch (Exception e) {
            throw SeedException.wrap(e, UndertowErrorCode.UNEXPECTED_EXCEPTION);
        }
    }
}
