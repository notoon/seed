/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.rest.api.hal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class HalRepresentationTest {

    @Test
    public void test_hal_links() {
        HalRepresentation halRepresentation = new HalRepresentation();
        halRepresentation.link("curies", new Link("http://docs.acme.com/relations/{rel}").templated().name("acme"));
        Assertions.assertThat(halRepresentation.getLink("curies")).isInstanceOf(Link.class);
        halRepresentation.link("curies", new Link("http://example.org/{rel}").templated().name("example"));
        Assertions.assertThat(halRepresentation.getLink("curies")).isInstanceOf(List.class);
        Assertions.assertThat(((List<?>) halRepresentation.getLink("curies"))).hasSize(2);
        halRepresentation.link("curies", new Link("http://example2.org/{rel}").templated().name("example2"));
        Assertions.assertThat(((List<?>) halRepresentation.getLink("curies"))).hasSize(3);
    }

    private static final String EXPECTED = "{\"_links\":{\"objects\":{\"href\":\"/pok\"}},\"_embedded\":{\"objects\":[{\"name\":\"toto\"}]}}";

    static class Person {
        @JsonProperty("name")
        private String name;

        public Person(String name) {
            this.name = name;
        }
    }

    @Test
    public void test_hal_link_serialization() throws Exception {
        HalRepresentation halRepresentation = new HalRepresentation();
        halRepresentation.link("objects", "/pok");
        halRepresentation.embedded("objects", Lists.newArrayList(new Person("toto")));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        outputStream.write(objectMapper.writeValueAsBytes(halRepresentation));
        outputStream.flush();

        Assertions.assertThat(outputStream.toString()).isEqualTo(EXPECTED);
    }
}
