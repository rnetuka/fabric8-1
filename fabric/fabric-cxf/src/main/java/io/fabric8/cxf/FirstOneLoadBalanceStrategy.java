/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.cxf;

/**
 * Always return the first physical address from the locator
 */
public class FirstOneLoadBalanceStrategy extends FabricLoadBalanceStrategySupport {

    @Override
    public String getNextAlternateAddress() {
        synchronized(alternateAddressList) {
            if (alternateAddressList.size() > 0) {
                return alternateAddressList.get(0);
            } else {
                throw new IllegalArgumentException("The AlternateAddressList is empty, please fresh the list shortly.");
            }
        }
    }
}