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

import org.seedstack.seed.core.api.SeedException;
import org.seedstack.seed.crypto.internal.SslConfig;

import javax.net.ssl.SSLContext;

/**
 * This class holds the properties used to configure undertow.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class ServerConfig {

    // Default configuration

    public static final String DEFAULT_HOST = "0.0.0.0";

    public static final int DEFAULT_PORT = 8080;

    public static final String DEFAULT_CONTEXT_PATH = "/";

    public static final boolean DEFAULT_HTTPS_ACTIVATION = false;

    public static final boolean DEFAULT_HTTP2_ACTIVATION = false;

    // Server configuration

    private String host;

    private int port;

    private String contextPath;

    private boolean httpsEnabled;

    private boolean http2Enabled;

    // Undertow configuration
    private Integer bufferSize;
    private Integer buffersPerRegion;
    private Integer ioThreads;
    private Integer workerThreads;
    private Boolean directBuffers;

    private SslConfig sslConfig;

    private SSLContext sslContext;

    public ServerConfig() {
        host = DEFAULT_HOST;
        port = DEFAULT_PORT;
        contextPath = DEFAULT_CONTEXT_PATH;
        httpsEnabled = DEFAULT_HTTPS_ACTIVATION;
        http2Enabled = DEFAULT_HTTP2_ACTIVATION;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        if (host != null && !"".equals(host)) {
            this.host = host;
        } else {
            throw SeedException.createNew(UndertowErrorCode.ILLEGAL_HOST_CONFIGURATION);
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (port > 0) {
            this.port = port;
        } else {
            throw SeedException.createNew(UndertowErrorCode.ILLEGAL_PORT_CONFIGURATION);
        }
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public boolean isHttpsEnabled() {
        return httpsEnabled;
    }

    public void setHttpsEnabled(boolean httpsEnabled) {
        this.httpsEnabled = httpsEnabled;
    }

    public boolean isHttp2Enabled() {
        return http2Enabled;
    }

    public void setHttp2Enabled(boolean http2Enabled) {
        this.http2Enabled = http2Enabled;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Integer getBuffersPerRegion() {
        return buffersPerRegion;
    }

    public void setBuffersPerRegion(Integer buffersPerRegion) {
        this.buffersPerRegion = buffersPerRegion;
    }

    public Integer getIoThreads() {
        return ioThreads;
    }

    public void setIoThreads(Integer ioThreads) {
        this.ioThreads = ioThreads;
    }

    public Integer getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(Integer workerThreads) {
        this.workerThreads = workerThreads;
    }

    public Boolean getDirectBuffers() {
        return directBuffers;
    }

    public void setDirectBuffers(Boolean directBuffers) {
        this.directBuffers = directBuffers;
    }

    public SslConfig getSslConfig() {
        return sslConfig;
    }

    public void setSslConfig(SslConfig sslConfig) {
        this.sslConfig = sslConfig;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }
}
