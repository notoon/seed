/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.rest.fixtures;

import com.sun.jersey.api.NotFoundException;
import org.seedstack.seed.security.api.exceptions.AuthenticationException;
import org.seedstack.seed.security.api.exceptions.AuthorizationException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
@Path("/error")
public class ErroneousResource {

    @GET
    @Path("/internal")
    public Response internalError() {
        throw new RuntimeException();
    }

    @GET
    @Path("/authentication")
    public Response authentication() {
        throw new AuthenticationException("");
    }

    @GET
    @Path("/authorization")
    public Response authorization() {
        throw new AuthorizationException("");
    }

    @GET
    @Path("/notFound")
    public Response notFound() {
        throw new NotFoundException();
    }
}
