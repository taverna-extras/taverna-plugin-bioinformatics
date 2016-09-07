package net.sf.taverna.t2.activities.soaplab;
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

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

/**
 * Convenience methods for simpler calling of SOAP services using the Axis
 * client.
 * 
 * Note that for complex scenarious you might want to construct the Call object
 * yourself.
 * 
 * @author Stian Soiland
 * 
 */
public class Soap {
	/**
	 * Invoke the web service, passing no parameters, and return the result.
	 *  
	 * @see callWebService(String target, String operation,	Object[] parameters)
	 * 
	 */
	public static Object callWebService(String target, String operation)
			throws ServiceException, RemoteException {
		return callWebService(target, operation, new Object[0]);
	}

	/**
	 * Invoke the web service, passing a single String parameter, and return the result.
	 *  
	 * @see callWebService(String target, String operation,	Object[] parameters)
	 * 
	 */
	public static Object callWebService(String target, String operation,
			String parameter) throws ServiceException, RemoteException {
		return callWebService(target, operation, new String[] { parameter });
	}

	/**
	 * Invoke the web service and return the result.
	 * 
	 * @param target The full URL to the service, example "http://www.ebi.ac.uk/soaplab/services/AnalysisFactory"
	 * @param operation The operation name, example "getAvailableCategories"
	 * @param parameters A (possibly empty) list of parameters
	 * @return The result returned from calling the webservice operation
	 * @throws ServiceException If web service facilities are not available
	 * @throws RemoteException If remote call failed
	 */
	public static Object callWebService(String target, String operation,
			Object[] parameters) throws ServiceException, RemoteException {
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(target);
		// No need to do new Qname(operation) with unspecified namespaces
		call.setOperationName(operation);
		return call.invoke(parameters);
	}
}
