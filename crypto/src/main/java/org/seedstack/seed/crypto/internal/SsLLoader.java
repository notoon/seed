/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.crypto.internal;

import org.seedstack.seed.core.api.SeedException;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * This class allows to initialize various classes from the Java Cryptography Architecture.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
class SsLLoader {

    /**
     * Gets the {@link javax.net.ssl.KeyManager}s from the ssl KeyStore.
     *
     * @return an array of KeyManagers
     */
    KeyManager[] getKeyManagers(KeyStore keyStore, char[] password) {
        KeyManagerFactory keyManagerFactory;
        try {
            keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw SeedException.wrap(e, CryptoErrorCodes.ALGORITHM_CANNOT_BE_FOUND);
        }

        try {
            keyManagerFactory.init(keyStore, password);
            return keyManagerFactory.getKeyManagers();
        } catch (UnrecoverableKeyException e) {
            throw SeedException.wrap(e, CryptoErrorCodes.UNRECOVERABLE_KEY);
        } catch (NoSuchAlgorithmException e) {
            throw SeedException.wrap(e, CryptoErrorCodes.ALGORITHM_CANNOT_BE_FOUND);
        } catch (KeyStoreException e) {
            throw SeedException.wrap(e, CryptoErrorCodes.UNEXPECTED_EXCEPTION);
        }
    }

    /**
     * Gets the {@link javax.net.ssl.TrustManager}s from the ssl TrustStore.
     *
     * @return an array of KeyManagers
     */
    TrustManager[] getTrustManager(KeyStore trustStore) {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            return trustManagerFactory.getTrustManagers();
        } catch (NoSuchAlgorithmException e) {
            throw SeedException.wrap(e, CryptoErrorCodes.ALGORITHM_CANNOT_BE_FOUND);
        } catch (KeyStoreException e) {
            throw SeedException.wrap(e, CryptoErrorCodes.UNEXPECTED_EXCEPTION);
        }
    }

    /**
     * Gets an SSLContext configured and initialized.
     *
     * <p>If no keyStore is configured, a default keyStore will be generated.
     * <b>The generated keyStore is not intended to be used in production !</b>
     * It won't work on JRE which don't include sun.* packages like the IBM JRE.
     * </p>
     *
     * @return SSLContext
     */
    SSLContext getSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers) {
        try {
            SSLContext sslContext = SSLContext.getInstance(protocol);
            sslContext.init(keyManagers, trustManagers, null);
            return sslContext;

        } catch (NoSuchAlgorithmException e) {
            throw SeedException.wrap(e, CryptoErrorCodes.ALGORITHM_CANNOT_BE_FOUND);
        } catch (Exception e) {
            throw SeedException.wrap(e, CryptoErrorCodes.UNEXPECTED_EXCEPTION);
        }
    }
}
