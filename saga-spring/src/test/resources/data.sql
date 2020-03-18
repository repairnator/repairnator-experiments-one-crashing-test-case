/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

DROP TABLE IF EXISTS SagaEventEntity;

CREATE TABLE `SagaEventEntity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sagaId` varchar(36) NOT NULL,
  `creationTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` varchar(50) NOT NULL,
  `contentJson` clob NOT NULL DEFAULT '{}',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

 CREATE INDEX running_sagas_index ON sagaEventEntity(sagaId, type);
