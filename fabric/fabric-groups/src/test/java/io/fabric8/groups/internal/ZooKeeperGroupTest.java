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
package io.fabric8.groups.internal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import io.fabric8.groups.NodeState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZooKeeperGroupTest {

    private static final String PATH = "/singletons/test/" + ZooKeeperGroupTest.class.getSimpleName();

    private CuratorFramework curator;
    private ZooKeeperGroup<NodeState> group;

    private int findFreePort() throws Exception {
        ServerSocket ss = new ServerSocket(0);
        int port = ss.getLocalPort();
        ss.close();
        return port;
    }

    @Before
    public void setUp() throws Exception {
        int port = findFreePort();
        curator = CuratorFrameworkFactory.builder()
                .connectString("localhost:" + port)
                .retryPolicy(new RetryOneTime(1))
                .build();
        //curator.start();
        group = new ZooKeeperGroup<>(curator, PATH, NodeState.class);
        //group.start();
        // Starting curator and group is not necessary for the current tests.
    }

    @After
    public void tearDown() throws IOException {
        group.close();
        curator.close();
        group = null;
        curator = null;
    }

    private static void putChildData(ZooKeeperGroup<NodeState> group, String path, String container) throws Exception {
        NodeState node = new NodeState("test", container);
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ZooKeeperGroup.MAPPER.writeValue(data, node);
        ChildData<NodeState> child = new ChildData<>(path, new Stat(), data.toByteArray(), node);
        group.currentData.put(path, child);
    }

    @Test
    public void testMembers() throws Exception {
        putChildData(group, PATH + "/001", "container1");
        putChildData(group, PATH + "/002", "container2");
        putChildData(group, PATH + "/003", "container3");

        Map<String, NodeState> members = group.members();
        assertThat(members.size(), equalTo(3));
        assertThat(members.get(PATH + "/001").getContainer(), equalTo("container1"));
        assertThat(members.get(PATH + "/002").getContainer(), equalTo("container2"));
        assertThat(members.get(PATH + "/003").getContainer(), equalTo("container3"));
    }

    @Test
    public void testMembers_withStaleNodes() throws Exception {
        putChildData(group, PATH + "/001", "container1"); // stale
        putChildData(group, PATH + "/002", "container1");
        putChildData(group, PATH + "/003", "container2"); // stale
        putChildData(group, PATH + "/004", "container3"); // stale
        putChildData(group, PATH + "/005", "container2");
        putChildData(group, PATH + "/006", "container3");

        Map<String, NodeState> members = group.members();
        assertThat(members.size(), equalTo(3));
        assertThat(members.get(PATH + "/002").getContainer(), equalTo("container1"));
        assertThat(members.get(PATH + "/005").getContainer(), equalTo("container2"));
        assertThat(members.get(PATH + "/006").getContainer(), equalTo("container3"));
    }

    @Test
    public void testIsMaster() throws Exception {
        putChildData(group, PATH + "/001", "container1");
        putChildData(group, PATH + "/002", "container2");
        putChildData(group, PATH + "/003", "container3");

        group.setId(PATH + "/001");
        assertThat(group.isMaster(), equalTo(true));
        group.setId(PATH + "/002");
        assertThat(group.isMaster(), equalTo(false));
    }

    @Test
    public void testIsMaster_withStaleNodes1() throws Exception {
        putChildData(group, PATH + "/001", "container1"); // stale
        putChildData(group, PATH + "/002", "container1");
        putChildData(group, PATH + "/003", "container2"); // stale
        putChildData(group, PATH + "/004", "container3"); // stale
        putChildData(group, PATH + "/005", "container2");
        putChildData(group, PATH + "/006", "container3");

        group.setId(PATH + "/002");
        assertThat(group.isMaster(), equalTo(true));
        group.setId(PATH + "/005");
        assertThat(group.isMaster(), equalTo(false));
    }

    @Test
    public void testIsMaster_withStaleNodes2() throws Exception {
        putChildData(group, PATH + "/001", "container1"); // stale
        putChildData(group, PATH + "/002", "container2");
        putChildData(group, PATH + "/003", "container1");
        putChildData(group, PATH + "/004", "container3"); // stale
        putChildData(group, PATH + "/005", "container3");

        group.setId(PATH + "/002");
        assertThat(group.isMaster(), equalTo(true));
        group.setId(PATH + "/003");
        assertThat(group.isMaster(), equalTo(false));
    }

    @Test
    public void testMaster() throws Exception {
        putChildData(group, PATH + "/001", "container1");
        putChildData(group, PATH + "/002", "container2");
        putChildData(group, PATH + "/003", "container3");

        NodeState master = group.master();
        assertThat(master, notNullValue());
        assertThat(master.getContainer(), equalTo("container1"));
    }

    @Test
    public void testMaster_withStaleNodes1() throws Exception {
        putChildData(group, PATH + "/001", "container1"); // stale
        putChildData(group, PATH + "/002", "container1");
        putChildData(group, PATH + "/003", "container2"); // stale
        putChildData(group, PATH + "/004", "container3"); // stale
        putChildData(group, PATH + "/005", "container2");
        putChildData(group, PATH + "/006", "container3");

        NodeState master = group.master();
        assertThat(master, notNullValue());
        assertThat(master.getContainer(), equalTo("container1"));
    }

    @Test
    public void testMaster_withStaleNodes2() throws Exception {
        putChildData(group, PATH + "/001", "container1"); // stale
        putChildData(group, PATH + "/002", "container2");
        putChildData(group, PATH + "/003", "container1");
        putChildData(group, PATH + "/004", "container3"); // stale
        putChildData(group, PATH + "/005", "container3");

        NodeState master = group.master();
        assertThat(master, notNullValue());
        assertThat(master.getContainer(), equalTo("container2"));
    }

    @Test
    public void testSlaves() throws Exception {
        putChildData(group, PATH + "/001", "container1");
        putChildData(group, PATH + "/002", "container2");
        putChildData(group, PATH + "/003", "container3");

        List<NodeState> slaves = group.slaves();
        assertThat(slaves.size(), equalTo(2));
        assertThat(slaves.get(0).getContainer(), equalTo("container2"));
        assertThat(slaves.get(1).getContainer(), equalTo("container3"));
    }

    @Test
    public void testSlaves_withStaleNodes1() throws Exception {
        putChildData(group, PATH + "/001", "container1"); // stale
        putChildData(group, PATH + "/002", "container1");
        putChildData(group, PATH + "/003", "container2"); // stale
        putChildData(group, PATH + "/004", "container3"); // stale
        putChildData(group, PATH + "/005", "container2");
        putChildData(group, PATH + "/006", "container3");

        List<NodeState> slaves = group.slaves();
        assertThat(slaves.size(), equalTo(2));
        assertThat(slaves.get(0).getContainer(), equalTo("container2"));
        assertThat(slaves.get(1).getContainer(), equalTo("container3"));
    }

    @Test
    public void testSlaves_withStaleNodes2() throws Exception {
        putChildData(group, PATH + "/001", "container1"); // stale
        putChildData(group, PATH + "/002", "container2");
        putChildData(group, PATH + "/003", "container1");
        putChildData(group, PATH + "/004", "container3"); // stale
        putChildData(group, PATH + "/005", "container3");

        List<NodeState> slaves = group.slaves();
        assertThat(slaves.size(), equalTo(2));
        assertThat(slaves.get(0).getContainer(), equalTo("container1"));
        assertThat(slaves.get(1).getContainer(), equalTo("container3"));
    }

}
