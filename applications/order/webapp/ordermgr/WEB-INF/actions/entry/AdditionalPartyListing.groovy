/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.ofbiz.order.shoppingcart.ShoppingCartEvents;
import org.ofbiz.order.shoppingcart.ShoppingCart;
import org.ofbiz.entity.*;
import org.ofbiz.base.util.*;

cart = ShoppingCartEvents.getCartObject(request);
additionalPartyRole = cart.getAdditionalPartyRoleMap();

roleData = [:];
partyData = [:];

additionalPartyRole.each { roleTypeId, partyList ->
    roleData[roleTypeId] = delegator.findByPrimaryKeyCache("RoleType", [roleTypeId : roleTypeId]);

    partyList.each { partyId ->
        partyMap = [:];
        partyMap.partyId = partyId;
        party = delegator.findByPrimaryKeyCache("Party", [partyId : partyId]);
        if (party.partyTypeId.equals("PERSON")) {
            party = party.getRelatedOneCache("Person");
            partyMap.type = "person";
            partyMap.firstName = party.firstName;
            partyMap.lastName = party.lastName;
        } else {
            party = party.getRelatedOneCache("PartyGroup");
            partyMap.type = "group";
            partyMap.groupName = party.groupName;
        }
        partyData[partyId] = partyMap;
    }
}

context.additionalPartyRoleMap = additionalPartyRole;
context.roleList = additionalPartyRole.keySet();
context.roleData = roleData;
context.partyData = partyData;
