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
package io.fabric8.api;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FabricService {

    final String DEFAULT_REPO_URI = "https://maven.repository.redhat.com/ga";

    /**
     * Adapt the {@link FabricService} to another type
     */
    <T> T adapt(Class<T> type);

    String getEnvironment();

    Map<String, Map<String, String>> substituteConfigurations(Map<String, Map<String, String>> configurations);

    Map<String, Map<String, String>> substituteConfigurations(Map<String, Map<String, String>> configurations, String profileId);

    /**
     * Track configuration changes.
     * @param callback The Callback to call when a configuration change is detected.
     */
    void trackConfiguration(Runnable callback);

    /**
     * Un-Track configuration changes.
     * @param callback The Callback to ignore.
     */
    void untrackConfiguration(Runnable callback);

    /**
     * Gets the existing {@link Container}s.
     * @return An array of @{link Container}s
     */
    Container[] getContainers();

    Container[] getAssociatedContainers(String versionId, String profileId);
    
    /**
     * Finds the {@link Container} with the specified name.
     * @param name  The name of the {@link Container}.
     * @return      The {@link Container}.
     */
    Container getContainer(String name);

    public void startContainer(String containerId);

    public void startContainer(String containerId, boolean force);

    public void startContainer(Container container);

    public void startContainer(Container container, boolean force);

    public void stopContainer(String containerId);

    public void stopContainer(String containerId, boolean force);

    public void stopContainer(Container container);

    public void stopContainer(Container container, boolean force);

    public void destroyContainer(String containerId);

    public void destroyContainer(String containerId, boolean force);

    public void destroyContainer(Container container);

    public void destroyContainer(Container container, boolean force);

    /**
     * Creates one or more new {@link Container}s with the specified {@link CreateContainerOptions}.
     * @param options   The options for the creation of the {@link Container}.
     * @return          An array of metadata for the created {@llink Container}s
     */
    CreateContainerMetadata[] createContainers(CreateContainerOptions options);

    CreateContainerMetadata[] createContainers(CreateContainerOptions options, CreationStateListener listener);

    Set<Class<? extends CreateContainerBasicOptions>> getSupportedCreateContainerOptionTypes();

    Set<Class<? extends CreateContainerBasicMetadata>> getSupportedCreateContainerMetadataTypes();

    /**
     * Returns the default {@link Version}.
     */
    String getDefaultVersionId();

    /**
     * Sets the default {@link Version}.
     */
    void setDefaultVersionId(String versionId);

    /**
     * Returns the default {@link Version}.
     */
    Version getDefaultVersion();

    /**
     * Returns the default {@link Version}.
     */
    Version getRequiredDefaultVersion();

    /**
     * Lookup a container provider by name. Returns provider which should be checked for
     * validity ({@link io.fabric8.api.ContainerProvider#isValidProvider()}).
     *
     * @param scheme the name of the container provider
     * @return the provider for the given scheme or null if there is none available. The provider may <b>not</b> be
     * valid in current environment!
     */
    ContainerProvider getProvider(String scheme);

    /**
     * Returns a list of {@see ContainerProvider}s valid in current environment
     *
     * @return
     */
    Map<String, ContainerProvider> getValidProviders();

    /**
     * Returns a list of all registered {@see ContainerProvider}s
     * @return
     */
    Map<String, ContainerProvider> getProviders();

    /**
     * Returns the current maven proxy repository to use to create new container
     */
    URI getMavenRepoURI();

    List<URI> getMavenRepoURIs();

    /**
     * Returns the current maven proxy repository to use to deploy new builds to the fabric
     */
    URI getMavenRepoUploadURI();

    /**
     * Returns the URL of the root of the REST API for working with fabric8. From this root you can add paths like
     * ("/containers", "/container/{containerId}, "/versions", "/version/{versionId}/profiles", "/version/{versionId}/profile/{profileId}") etc
     */
    String getRestAPI();

    /**
     * Returns the URL of the git master
     */
    String getGitUrl();

    /**
     * Returns the name of container which is current git master
     */
    String getGitMaster();

    /**
     * Returns the URL of the web console
     */
    String getWebConsoleUrl();

    /**
     * Returns the pseudo url of the Zookeeper. It's not an actual url as it doesn't contain a scheme.
     * It's of the format <p>ip:port</p>
     */
    String getZookeeperUrl();

    /**
     * Returns the user used to connect to Zookeeper.
     */
    String getZooKeeperUser();

    /**
     * Returns the password used to connect to Zookeeper.
     */
    String getZookeeperPassword();

    /**
     * Returns the {@link Container} on which the method is executed.
     */
    Container getCurrentContainer();

    /**
     * Returns the name of the current {@link Container}.
     */
    String getCurrentContainerName();

    /**
     * Returns the fabric provisioning requirements if there are any defined
     * or empty requirements if none are defined.
     */
    FabricRequirements getRequirements();

    /**
     * Returns the status of the fabric <a href="http://fabric8.io/gitbook/requirements.html">auto scaler</a>
     */
    AutoScaleStatus getAutoScaleStatus();


    /**
     * Stores the fabric provisioning requirements
     */
    void setRequirements(FabricRequirements requirements) throws IOException;

    /**
     * Get the profile statuses of the fabric in terms of the current number of instances and their max/min requirements
     */
    FabricStatus getFabricStatus();

    /**
     * Get the patch support service which allow easy upgrades
     */
    PatchService getPatchService();

    /**
     * Get hte port service.
     */
    PortService getPortService();

    /**
     * Get the default JVM options used when creating containers
     */
    String getDefaultJvmOptions();

    /**
     * Set the default JVM options used when creating containers
     */
    void setDefaultJvmOptions(String jvmOptions);


    /**
     * Returns the web app URL for the given web application (from its id)
     */
    String containerWebAppURL(String webAppId, String name);

    /**
     * Returns the web app URL of the given webAppId, profile and version
     */
    String profileWebAppURL(String webAppId, String profileId, String versionId);

    /**
     * Returns the configuration value for the given key
     * @return  the value stored for that key
     */
    String getConfigurationValue(String versionId, String profileId, String pid, String key);

    /**
     * Sets the configuration value for the given key
     */
    void setConfigurationValue(String versionId, String profileId, String pid, String key, String value);

    /**
     * Scales the given profile up or down in the number of instances required
     *
     *
     * @param profile the profile ID to change the requirements
     * @param numberOfInstances the number of instances to increase or decrease
     * @return true if the requiremetns changed
     */
    boolean scaleProfile(String profile, int numberOfInstances) throws IOException;

    /**
     * Creates a new {@link io.fabric8.api.ContainerAutoScaler} instance
     * using the available container providers to determine the best way to auto-scale;
     * or null if there are no suitable {@link ContainerProvider} instances available
     * with the correct configuration to enable this capability.
     * @param requirements
     * @param profileRequirements
     */
    ContainerAutoScaler createContainerAutoScaler(FabricRequirements requirements, ProfileRequirements profileRequirements);

    /**
     * Order container to <em>leave</em> Fabric, cleaning all the entries it did when joining. Should be called
     * only for containers that were not created using {@code fabric:container-create}
     */
    void leave();

}
