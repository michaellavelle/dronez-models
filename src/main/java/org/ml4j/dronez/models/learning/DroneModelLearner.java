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
package org.ml4j.dronez.models.learning;

import org.ml4j.dronez.DroneAction;
import org.ml4j.dronez.DroneStateWithRecentActions;
import org.ml4j.dronez.models.DummyDroneModel;
import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateActionSequenceHistory;

/**
 * The goal of the this project is to complete the implementation of this class which generates a DroneModel (also to be implemented)
 * 
 * @author Michael Lavelle
 *
 */
public class DroneModelLearner implements ModelLearner<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> {

	public final static Class<? extends Model<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction>> MODEL_CLASS = DummyDroneModel.class;
	
	@Override
	public Model<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> learnModel(
			StateActionSequenceHistory<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> stateActionStateHistory) {
		return new DummyDroneModel();
	}

}
