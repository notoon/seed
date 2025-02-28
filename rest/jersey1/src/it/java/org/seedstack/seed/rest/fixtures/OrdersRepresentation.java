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

/**
* @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
*/
public class OrdersRepresentation {
    private int currentlyProcessing;
    private int shippedToday;

    public OrdersRepresentation() {
    }

    public OrdersRepresentation(int currentlyProcessing, int shippedToday) {
        this.currentlyProcessing = currentlyProcessing;
        this.shippedToday = shippedToday;
    }

    public int getCurrentlyProcessing() {
        return currentlyProcessing;
    }

    public void setCurrentlyProcessing(int currentlyProcessing) {
        this.currentlyProcessing = currentlyProcessing;
    }

    public int getShippedToday() {
        return shippedToday;
    }

    public void setShippedToday(int shippedToday) {
        this.shippedToday = shippedToday;
    }
}
