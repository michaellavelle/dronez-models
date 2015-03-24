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

import java.util.Arrays;
import java.util.List;

import org.ml4j.dronez.ForwardBackAction;
import org.ml4j.dronez.LeftRightAction;
import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.PositionDeltaWithVelocity;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.SpinAction;
import org.ml4j.dronez.UpDownAction;
import org.ml4j.dronez.VelocityAndRecentActions;
import org.ml4j.dronez.histories.SingleDimensionPositionDeltaStateActionSequenceHistory;
import org.ml4j.dronez.models.SingleDimensionDroneModel;
import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateActionSequenceHistory;
import org.ml4j.util.SerializationHelper;


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

	private	SerializationHelper serializationHelper;

	
	private double minimumPosition;
	private double maximumPosition;
	private double minimumVelocity;
	private double maximumVelocity;
	private String dimensionName;
	private int recentActionCount;
	
	private boolean[] recentActionsAndLatestActionMask;
	
	
	public SingleDimensionDroneModelLearner(SerializationHelper serializationHelper,double minimumPosition,double maximumPosition,double minimumVelocity,double maximumVelocity,String dimensionName,int recentActionCount,	
		boolean[] recentActionsAndLatestActionMask)
	{
		this.minimumPosition = minimumPosition;
		this.maximumPosition = maximumPosition;
		this.minimumVelocity = minimumVelocity;
		this.maximumVelocity = maximumVelocity;
		this.dimensionName = dimensionName;
		this.recentActionCount = recentActionCount;
		this.recentActionsAndLatestActionMask = recentActionsAndLatestActionMask;
		this.serializationHelper = serializationHelper;
	}
	
	@Override
	public Model<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> learnModel(
			StateActionSequenceHistory<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> stateActionStateHistory) {

		// Learn a position delta with velocity and recent actions model
		StateActionSequenceHistory<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> positionDeltaHistory = new SingleDimensionPositionDeltaStateActionSequenceHistory<A>(
				stateActionStateHistory);
		Model<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> delegatedModel = createSingleDimensionPositionDeltaModelLearner(
				stateActionStateHistory.getStateActionStateSequence(0).getData().getAction()).learnModel(
				positionDeltaHistory);

		
		serializationHelper.serialize(delegatedModel, "droneDeltaPosition" +  dimensionName + "Model");
		
		return new SingleDimensionDroneModel<A>(delegatedModel,minimumPosition,maximumPosition,minimumVelocity,maximumVelocity,getAllActions(stateActionStateHistory.getStateActionStateSequence(0).getData().getAction()),recentActionCount);

	}
	
	@SuppressWarnings("unchecked")
	// Refactor this as it is a workaround
	private List<A> getAllActions(A firstAction)
	{
		List<A> allActions = null;
		if (firstAction instanceof LeftRightAction) {
			allActions = (List<A>) Arrays.asList(LeftRightAction.ALL_ACTIONS);
		} else if (firstAction instanceof UpDownAction) {
			allActions = (List<A>) Arrays.asList(UpDownAction.ALL_ACTIONS);
		} else if (firstAction instanceof ForwardBackAction) {
			allActions = (List<A>) Arrays.asList(ForwardBackAction.ALL_ACTIONS);
		} else if (firstAction instanceof SpinAction) {
			allActions = (List<A>) Arrays.asList(SpinAction.ALL_ACTIONS);
		}
		return allActions;
	}

	
	private SingleDimensionPositionDeltaModelLearner<A> createSingleDimensionPositionDeltaModelLearner(A firstAction) {
		
		return new SingleDimensionPositionDeltaModelLearner<A>(getAllActions(firstAction),recentActionCount,recentActionsAndLatestActionMask);
	}

}
