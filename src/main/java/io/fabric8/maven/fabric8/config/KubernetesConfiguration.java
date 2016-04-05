package io.fabric8.maven.fabric8.config;
/*
 * 
 * Copyright 2016 Roland Huss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;
import java.util.Map;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author roland
 * @since 22/03/16
 */
public class KubernetesConfiguration {

    // "kubernetes" or "openshift"
    @Parameter
    private String mode;

    @Parameter
    private Map<String,String> env;

    @Parameter
    private Map<String,String> labels;

    @Parameter
    private AnnotationConfiguration annotations;

    @Parameter
    private List<VolumeConfiguration> volumes;

    @Parameter
    private List<VolumeMountConfiguration> volumeMounts;

    @Parameter(defaultValue = "${project.artifactId}")
    private String rcName;

    @Parameter
    private List<ServiceConfiguration> services;

    @Parameter
    private ProbeConfiguration liveness;

    @Parameter
    private ProbeConfiguration readiness;

    @Parameter
    private MetricsConfig metrics;

    // Run container in privileged mode
    @Parameter
    private boolean containerPrivileged = false;

    // Whether to skip the generation of this descriptor
    @Parameter
    private boolean skip = false;

    // How images should be pulled (maps to ImagePullPolicy)
    @Parameter
    private String imagePullPolicy;

    // Mapping of port to names
    @Parameter
    private Map<String,Integer> ports;

    // Number of replicas to create
    @Parameter(defaultValue = "1")
    private int replicas;

    // Service account to use
    @Parameter
    private String serviceAccount;

    public String getMode() {
        return mode;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public AnnotationConfiguration getAnnotations() {
        return annotations != null ? annotations : new AnnotationConfiguration();
    }

    public List<VolumeConfiguration> getVolumes() {
        return volumes;
    }

    public List<VolumeMountConfiguration> getVolumeMounts() {
        return volumeMounts;
    }

    public List<ServiceConfiguration> getServices() {
        return services;
    }

    public ProbeConfiguration getLiveness() {
        return liveness;
    }

    public ProbeConfiguration getReadiness() {
        return readiness;
    }

    public MetricsConfig getMetrics() {
        return metrics;
    }

    public boolean isContainerPrivileged() {
        return containerPrivileged;
    }

    public boolean isSkip() {
        return skip;
    }

    public String getImagePullPolicy() {
        return imagePullPolicy;
    }

    public String getRcName() {
        return rcName;
    }

    public Map<String, Integer> getPorts() {
        return ports;
    }

    public int getReplicas() {
        return replicas;
    }

    public String getServiceAccount() {
        return serviceAccount;
    }


    // TODO: SCC

    // ===============================
    // TODO:
    // fabric8.extended.environment.metadata
    // fabric8.envProperties
    // fabric8.combineDependencies
    // fabric8.combineJson.target
    // fabric8.combineJson.project

    // fabric8.container.name	 --> alias name ?
    // fabric8.replicationController.name

    // fabric8.iconRef
    // fabric8.iconUrl
    // fabric8.iconUrlPrefix
    // fabric8.iconUrlPrefix

    // fabric8.imagePullPolicySnapshot

    // fabric8.includeAllEnvironmentVariables
    // fabric8.includeNamespaceEnvVar

    // fabric8.namespaceEnvVar

    // fabric8.provider
}
