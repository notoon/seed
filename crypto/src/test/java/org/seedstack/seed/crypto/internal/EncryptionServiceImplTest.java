/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 10 juin 2015
 */
package org.seedstack.seed.crypto.internal;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * Unit test for {@link EncryptionServiceImpl}
 *
 * @author thierry.bouvet@mpsa.com
 */
public class EncryptionServiceImplTest {

    /**
     * Test method for {@link EncryptionServiceImpl#encrypt(byte[])}. Test encrypt without any problem.
     *
     * @throws Exception if an error occurred
     */
    @Test
    public void testEncrypt(@Mocked final PublicKey publicKey, @Mocked final Cipher cipher)
            throws Exception {

        EncryptionServiceImpl asymetricCrypting = new EncryptionServiceImpl("alias", publicKey, null);
        final String toCrypt = "text to crypt";
        asymetricCrypting.encrypt(toCrypt.getBytes());

        new Verifications() {
            {
                cipher.doFinal(toCrypt.getBytes());
                times = 1;
            }
        };
    }


    /**
     * Test method for {@link EncryptionServiceImpl#encrypt(byte[])}. Test encrypt without any problem.
     *
     * @throws Exception if an error occurred
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEncryptWithoutPublicKey() throws Exception {
        EncryptionServiceImpl asymetricCrypting = new EncryptionServiceImpl("alias", null, null);
        final String toCrypt = "text to crypt";
        asymetricCrypting.encrypt(toCrypt.getBytes());
    }

    /**
     * Test method for {@link EncryptionServiceImpl#encrypt(byte[])}. Test encrypt Cipher problem.
     *
     * @throws Exception if an error occurred
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEncryptWithNoSuchAlgorithmException(@Mocked final PublicKey publicKey, @Mocked final Cipher cipher) throws Exception {

        new Expectations() {
            {
                Cipher.getInstance("RSA/ECB/PKCS1PADDING");
                result = new NoSuchAlgorithmException("dummy exception");
            }
        };
        EncryptionServiceImpl asymetricCrypting = new EncryptionServiceImpl("alias", publicKey, null);
        final String toCrypt = "text to crypt";
        asymetricCrypting.encrypt(toCrypt.getBytes());

    }

    /**
     * Test method for {@link EncryptionServiceImpl#encrypt(byte[])}. Test encrypt Cipher problem.
     *
     * @throws Exception if an error occurred
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEncryptWithNoSuchPaddingException(@Mocked final PublicKey publicKey, @Mocked final Cipher cipher) throws Exception {

        new Expectations() {
            {
                Cipher.getInstance("RSA/ECB/PKCS1PADDING");
                result = new NoSuchPaddingException("dummy exception");
            }
        };
        EncryptionServiceImpl asymetricCrypting = new EncryptionServiceImpl("alias", publicKey, null);
        final String toCrypt = "text to crypt";
        asymetricCrypting.encrypt(toCrypt.getBytes());
    }

    /**
     * Test method for {@link EncryptionServiceImpl#encrypt(byte[])}. Test encrypt Cipher problem.
     *
     * @throws Exception if an error occurred
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEncryptWithIllegalBlockSizeException(@Mocked final PublicKey publicKey,
                                                         @Mocked final Cipher cipher) throws Exception {

        final String toCrypt = "text to crypt";

        new Expectations() {
            {
                Cipher.getInstance("RSA/ECB/PKCS1PADDING");
                result = cipher;

                cipher.doFinal(toCrypt.getBytes());
                result = new IllegalBlockSizeException("dummy exception");
            }
        };
        EncryptionServiceImpl asymetricCrypting = new EncryptionServiceImpl("alias", publicKey, null);
        asymetricCrypting.encrypt(toCrypt.getBytes());
    }

    /**
     * Test method for {@link EncryptionServiceImpl#encrypt(byte[])}. Test encrypt Cipher problem.
     *
     * @throws Exception if an error occurred
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEncryptWithBadPaddingException(@Mocked final PublicKey publicKey,
                                                   @Mocked final Cipher cipher) throws Exception {

        final String toCrypt = "text to crypt";

        new Expectations() {
            {
                Cipher.getInstance("RSA/ECB/PKCS1PADDING");
                result = cipher;

                cipher.doFinal(toCrypt.getBytes());
                result = new BadPaddingException("dummy exception");
            }
        };
        EncryptionServiceImpl asymetricCrypting = new EncryptionServiceImpl("alias", publicKey, null);
        asymetricCrypting.encrypt(toCrypt.getBytes());

    }

    /**
     * Test method for {@link EncryptionServiceImpl#decrypt(byte[])}.
     *
     * @throws Exception if an error occurred
     */
    @Test
    public void testDecrypt(@Mocked final Key key, @Mocked final Cipher cipher)
            throws Exception {
        final String toDecrypt = "ADEF0985C";

        EncryptionServiceImpl asymetricCrypting = new EncryptionServiceImpl("alias", null, key);
        asymetricCrypting.decrypt(toDecrypt.getBytes());
        new Verifications() {
            {
                cipher.doFinal(toDecrypt.getBytes());
                times = 1;
            }
        };

    }

    /**
     * Test method for {@link EncryptionServiceImpl#decrypt(byte[])}. Test witout keystore so it's not possible.
     *
     * @throws Exception if an error occurred
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDecryptWithoutPrivateKey() throws Exception {
        final String toDecrypt = "ADEF0985C";

        EncryptionServiceImpl asymetricCrypting = new EncryptionServiceImpl("alias", null, null);
        asymetricCrypting.decrypt(toDecrypt.getBytes());

    }

}
