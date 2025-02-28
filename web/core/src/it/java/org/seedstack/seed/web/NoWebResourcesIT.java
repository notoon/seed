/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.web;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.seedstack.seed.it.AbstractSeedWebIT;

import java.net.URL;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class NoWebResourcesIT extends AbstractSeedWebIT {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addAsResource("configuration/org.seedstack.seed.web.no-resources.properties", "META-INF/configuration/org.seedstack.seed.web.no-resources.properties").setWebXML("WEB-INF/web.xml");
    }

    @Test
    @RunAsClient
    public void no_resource_is_served(@ArquillianResource URL baseURL) throws Exception {
        expect().statusCode(404).when().get(baseURL.toString() + "resources/test.js");
    }
}
