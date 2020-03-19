/*
 * Copyright (c) 2016 Uber Technologies, Inc. (hoodie-dev-group@uber.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uber.hoodie.metrics;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.uber.hoodie.config.HoodieWriteConfig;
import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Implementation of Graphite reporter, which connects to the Graphite server, and send metrics to
 * that server.
 */
public class MetricsGraphiteReporter extends MetricsReporter {

  private final MetricRegistry registry;
  private final GraphiteReporter graphiteReporter;
  private final HoodieWriteConfig config;
  private String serverHost;
  private int serverPort;

  private static Logger logger = LogManager.getLogger(MetricsGraphiteReporter.class);

  public MetricsGraphiteReporter(HoodieWriteConfig config, MetricRegistry registry) {
    this.registry = registry;
    this.config = config;

    // Check the serverHost and serverPort here
    this.serverHost = config.getGraphiteServerHost();
    this.serverPort = config.getGraphiteServerPort();
    if (serverHost == null || serverPort == 0) {
      throw new RuntimeException(
          String.format("Graphite cannot be initialized with serverHost[%s] and serverPort[%s].",
              serverHost, serverPort));
    }

    this.graphiteReporter = createGraphiteReport();
  }

  @Override
  public void start() {
    if (graphiteReporter != null) {
      graphiteReporter.start(30, TimeUnit.SECONDS);
    } else {
      logger.error("Cannot start as the graphiteReporter is null.");
    }
  }

  @Override
  public void report() {
    if (graphiteReporter != null) {
      graphiteReporter.report();
    } else {
      logger.error("Cannot report metrics as the graphiteReporter is null.");
    }
  }

  @Override
  public Closeable getReporter() {
    return graphiteReporter;
  }

  private GraphiteReporter createGraphiteReport() {
    Graphite graphite = new Graphite(
        new InetSocketAddress(serverHost, serverPort));
    String reporterPrefix = config.getGraphiteMetricPrefix();
    return GraphiteReporter.forRegistry(registry)
        .prefixedWith(reporterPrefix)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .filter(MetricFilter.ALL)
        .build(graphite);
  }
}
