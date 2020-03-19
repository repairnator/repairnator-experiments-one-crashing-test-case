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

package com.uber.hoodie.io;

import com.uber.hoodie.common.model.HoodieRecordPayload;
import com.uber.hoodie.common.table.HoodieTableMetaClient;
import com.uber.hoodie.common.table.HoodieTimeline;
import com.uber.hoodie.common.table.TableFileSystemView;
import com.uber.hoodie.common.util.FSUtils;
import com.uber.hoodie.common.util.HoodieAvroUtils;
import com.uber.hoodie.config.HoodieWriteConfig;
import com.uber.hoodie.exception.HoodieIOException;
import com.uber.hoodie.table.HoodieTable;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkEnv;
import org.apache.spark.util.Utils;

public abstract class HoodieIOHandle<T extends HoodieRecordPayload> {

  private static Logger logger = LogManager.getLogger(HoodieIOHandle.class);
  protected final String commitTime;
  protected final HoodieWriteConfig config;
  protected final FileSystem fs;
  protected final HoodieTable<T> hoodieTable;
  protected HoodieTimeline hoodieTimeline;
  protected final Schema schema;

  public HoodieIOHandle(HoodieWriteConfig config, String commitTime,
                        HoodieTable<T> hoodieTable) {
    this.commitTime = commitTime;
    this.config = config;
    this.fs = hoodieTable.getMetaClient().getFs();
    this.hoodieTable = hoodieTable;
    this.hoodieTimeline = hoodieTable.getCompletedCommitTimeline();
    this.schema = createHoodieWriteSchema(config);
  }

  public Path makeNewPath(String partitionPath, int taskPartitionId, String fileName) {
    Path path = new Path(config.getBasePath(), partitionPath);
    try {
      fs.mkdirs(path); // create a new partition as needed.
    } catch (IOException e) {
      throw new HoodieIOException("Failed to make dir " + path, e);
    }

    return new Path(path.toString(),
        FSUtils.makeDataFileName(commitTime, taskPartitionId, fileName));
  }

  public Path makeTempPath(String partitionPath, int taskPartitionId, String fileName, int stageId, long taskAttemptId) {
    Path path = new Path(config.getBasePath(), HoodieTableMetaClient.TEMPFOLDER_NAME);
    return new Path(path.toString(),
        FSUtils.makeTempDataFileName(partitionPath, commitTime, taskPartitionId, fileName, stageId, taskAttemptId));
  }

  /**
   * Deletes any new tmp files written during the current commit, into the partition
   */
  public static void cleanupTmpFilesFromCurrentCommit(HoodieWriteConfig config,
                                                      String commitTime,
                                                      String partitionPath,
                                                      int taskPartitionId,
                                                      HoodieTable hoodieTable) {
    FileSystem fs = hoodieTable.getMetaClient().getFs();
    try {
      FileStatus[] prevFailedFiles = fs.globStatus(new Path(String
          .format("%s/%s/%s", config.getBasePath(), partitionPath,
              FSUtils.maskWithoutFileId(commitTime, taskPartitionId))));
      if (prevFailedFiles != null) {
        logger.info("Deleting " + prevFailedFiles.length
            + " files generated by previous failed attempts.");
        for (FileStatus status : prevFailedFiles) {
          fs.delete(status.getPath(), false);
        }
      }
    } catch (IOException e) {
      throw new HoodieIOException("Failed to cleanup Temp files from commit " + commitTime,
          e);
    }
  }

  public Schema getSchema() {
    return schema;
  }

  public static Schema createHoodieWriteSchema(HoodieWriteConfig config) {
    return HoodieAvroUtils.addMetadataFields(new Schema.Parser().parse(config.getSchema()));
  }

  protected long getMaxMemoryAllowedForMerge() {
    long executorMemoryInBytes = Long.valueOf(Utils.memoryStringToMb(SparkEnv.get().conf().get(config.getSparkExecutorMemoryProp(),
        String.valueOf(1024))) * 1024 * 1024L); // 1 GB is the default conf used by Spark, look at SparkContext.scala
    double memoryFraction = Double.valueOf(SparkEnv.get().conf().get(config.getSparkExecutorMemoryFractionProp(),
        String.valueOf(0.6))); // 0.6 is the default value used by Spark, look at SparkConf.scala
    double maxMemoryFractionForMerge = config.getMaxMemoryFractionPerPartitionMerge();
    long maxMemoryForMerge = (long) Math.floor(executorMemoryInBytes * memoryFraction * maxMemoryFractionForMerge);
    logger.info("MaxMemoryInBytes allowed for compaction => " + maxMemoryForMerge);
    return maxMemoryForMerge;
  }
}
