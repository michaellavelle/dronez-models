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
import org.ml4j.dronez.PositionDeltaWithVelocity;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.VelocityAndRecentActions;
import org.ml4j.dronez.histories.SingleDimensionPositionDeltaStateActionSequenceHistory;
import org.ml4j.dronez.models.SingleDimensionDroneModel;
import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateActionSequenceHistory;


/**
 * SingleDimensionDroneModelLearner which delegates calculations to an initial-position-agnostic Model<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A>
 * 
 * Such a Model is learned using a SingleDimensionPositionDeltaModelLearner - yet to be implemented
 * 
 * @author Michael Lavelle
 *
 */
public class SingleDimensionDroneModelLearner<A extends NumericAction> implements
		ModelLearner<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> {

	@Override
	public Model<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> learnModel(
			StateActionSequenceHistory<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> stateActionStateHistory) {

		// Learn a position delta with velocity and recent actions model
		StateActionSequenceHistory<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> positionDeltaHistory = new SingleDimensionPositionDeltaStateActionSequenceHistory<A>(
				stateActionStateHistory);
		Model<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> delegatedModel = createSingleDimensionPositionDeltaModelLearner().learnModel(
				positionDeltaHistory);

		return new SingleDimensionDroneModel<A>(delegatedModel);

	}

	private SingleDimensionPositionDeltaModelLearner<A> createSingleDimensionPositionDeltaModelLearner() {
		return new SingleDimensionPositionDeltaModelLearner<A>();
	}

}
