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
package io.fabric8.insight.metrics.model;

import java.util.Date;
import java.util.Map;

public class QueryResult {

    private final Server server;
    private final Query query;
    private final Date timestamp;
    private final Map<String, Result<?>> results;

    public QueryResult(Server server, Query query, Date timestamp, Map<String, Result<?>> results) {
        this.server = server;
        this.query = query;
        this.timestamp = timestamp;
        this.results = results;
    }

    public Server getServer() {
        return server;
    }

    public Query getQuery() {
        return query;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Map<String, Result<?>> getResults() {
        return results;
    }
}