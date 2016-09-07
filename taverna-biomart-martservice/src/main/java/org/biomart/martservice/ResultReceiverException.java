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
 *
 * @author David Withers
 */
public class ResultReceiverException extends Exception {
	private static final long serialVersionUID = 7151337259555845771L;

	/**
	 * Constructs a new exception with no detail message.
	 *
	 */
	public ResultReceiverException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause (a null value is permitted, and indicates that the
	 *            cause is nonexistent or unknown)
	 */
	public ResultReceiverException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 *
	 * @param message
	 *            the detail message
	 */
	public ResultReceiverException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause
	 *            the cause (a null value is permitted, and indicates that the
	 *            cause is nonexistent or unknown)
	 */
	public ResultReceiverException(Throwable cause) {
		super(cause);
	}

}
