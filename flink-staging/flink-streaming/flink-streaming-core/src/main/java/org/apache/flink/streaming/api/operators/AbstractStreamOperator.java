/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.streaming.api.operators;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;
import org.apache.flink.streaming.runtime.tasks.StreamingRuntimeContext;

/**
 * Base class for all stream operators.
 * 
 * Operators that contain a user function should extend the class 
 * {@link AbstractUdfStreamOperator} instead (which is a specialized subclass of this class).
 * 
 * @param <OUT> The output type of the operator
 */
public abstract class AbstractStreamOperator<OUT> implements StreamOperator<OUT> {

	private static final long serialVersionUID = 1L;

	
	protected transient StreamingRuntimeContext runtimeContext;

	protected transient ExecutionConfig executionConfig;

	protected transient Output<StreamRecord<OUT>> output;

	protected boolean inputCopyDisabled = false;

	// A sane default for most operators
	protected ChainingStrategy chainingStrategy = ChainingStrategy.HEAD;

	// ------------------------------------------------------------------------
	//  Life Cycle
	// ------------------------------------------------------------------------
	
	@Override
	public void setup(Output<StreamRecord<OUT>> output, StreamingRuntimeContext runtimeContext) {
		this.output = output;
		this.executionConfig = runtimeContext.getExecutionConfig();
		this.runtimeContext = runtimeContext;
	}

	/**
	 * This default implementation of the interface method does nothing.
	 */
	@Override
	public void open(Configuration parameters) throws Exception {}

	/**
	 * This default implementation of the interface method does nothing.
	 */
	@Override
	public void close() throws Exception {}

	/**
	 * This default implementation of the interface method does nothing.
	 */
	@Override
	public void dispose() {}
	
	// ------------------------------------------------------------------------
	//  Context and chaining properties
	// ------------------------------------------------------------------------
	
	@Override
	public final void setChainingStrategy(ChainingStrategy strategy) {
		this.chainingStrategy = strategy;
	}

	@Override
	public final ChainingStrategy getChainingStrategy() {
		return chainingStrategy;
	}

	@Override
	public boolean isInputCopyingDisabled() {
		return inputCopyDisabled;
	}

	/**
	 * Enable object-reuse for this operator instance. This overrides the setting in
	 * the {@link org.apache.flink.api.common.ExecutionConfig}/
	 */
	public void disableInputCopy() {
		this.inputCopyDisabled = true;
	}
	
	@Override
	public StreamingRuntimeContext getRuntimeContext(){
		return runtimeContext;
	}
}
