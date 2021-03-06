/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.streampipes.manager.monitoring.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.streampipes.storage.management.StorageManager;

public enum JobManager {
	
	INSTANCE;

	private List<MonitoringJob<?>> currentJobs;
	
	private final ScheduledExecutorService scheduler;
	
	JobManager()
	{
		this.currentJobs = new ArrayList<>();
		this.scheduler = Executors.newScheduledThreadPool(2);
	}
	
	public void addJob(MonitoringJob<?> job)
	{
		currentJobs.add(job);
		scheduler.scheduleAtFixedRate(new MonitoringJobExecutor(job), 10, 10, TimeUnit.MINUTES);
	}
	
	public void removeJob(MonitoringJob<?> job)
	{
		currentJobs.remove(job);
	}
	
	public List<MonitoringJob<?>> getCurrentJobs()
	{
		return currentJobs;
	}
	
	public void prepareMonitoring() {
		StorageManager.INSTANCE.getPipelineElementStorage().getAllDataProcessors().forEach(s -> addJob(new SepaMonitoringJob(s)));
		StorageManager.INSTANCE.getPipelineElementStorage().getAllDataSinks().forEach(s -> addJob(new SecMonitoringJob(s)));
		//TODO: add seps StorageManager.INSTANCE.getStorageAPI().getAllSECs().forEach(s -> currentJobs.add(new SecMonitoringJob(s)));
	}
}
