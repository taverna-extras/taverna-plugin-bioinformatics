package org.biomart.martservice;
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

/**
 * An interface for receiving results of a Query.
 *
 * @author David Withers
 */
public interface ResultReceiver {

	/**
	 * Receives a single row from the results of executing a Query.
	 *
	 * This method will be called frequently and should not block.
	 *
	 * @param resultRow
	 */
	public void receiveResult(Object[] resultRow, long index) throws ResultReceiverException;

	/**
	 * Receives an error for a single row from the results of executing a Query.
	 *
	 * This method will be called frequently and should not block.
	 *
	 * @param resultRow
	 */
	public void receiveError(String errorMessage, long index) throws ResultReceiverException;

}
