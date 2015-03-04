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

import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.models.SingleDimensionDroneModel;
import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateActionSequenceHistory;

/**
 * Now that we have amended our DroneModel so that it uses 4 independent Models for 4 dimensions, the revised goal of
 * this project is to complete the implementation of this class which generates a SingleDimensionDroneModel (also to be implemented)
 * 
 * @author Michael Lavelle
 *
 */
public class SingleDimensionDroneModelLearner<A extends NumericAction> implements ModelLearner<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> {

	@Override
	public Model<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> learnModel(
			StateActionSequenceHistory<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> stateActionStateHistory) {
				return new SingleDimensionDroneModel<A>();

	
	}

}
