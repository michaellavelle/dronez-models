/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ml4j.dronez.models;

import org.ml4j.dronez.DroneAction;
import org.ml4j.dronez.DroneStateWithRecentActions;
import org.ml4j.mdp.Model;
/**
 * This class is unimplemented.  The goal of this project is to provide an implementation of this class, and to learn an optimal instance using DroneModelLearner ( also to be implemented)
 * 
 * @author Michael Lavelle
 *
 */
public class DroneModel implements Model<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public DroneStateWithRecentActions getInitialState() {
		return null;
	}

	@Override
	public DroneStateWithRecentActions getState(DroneStateWithRecentActions state, DroneAction arg1) {
		return state;
	}

}
