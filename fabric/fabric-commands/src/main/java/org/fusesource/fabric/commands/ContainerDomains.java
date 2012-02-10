/**
 * Copyright (C) FuseSource, Inc.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fusesource.fabric.commands;

import java.util.List;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.fusesource.fabric.api.Container;
import org.fusesource.fabric.commands.support.FabricCommand;

@Command(name = "container-domains", scope = "fabric", description = "Lists the JMX domains an container has")
public class ContainerDomains extends FabricCommand {

    @Argument(index = 0, name="container", description="The container name", required = true, multiValued = false)
    private String container = null;

    protected Object doExecute() throws Exception {
        checkFabricAvailable();
        Container found = getContainer(container);
        if (found == null) {
            throw new IllegalArgumentException("Container " + container + " does not exist.");
        }

        List<String> domains = found.getJmxDomains();
        for (String domain : domains) {
            System.out.println(domain);
        }
        return null;
    }

}
