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
import org.ml4j.dronez.ForwardBackAction;
import org.ml4j.dronez.LeftRightAction;
import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.UpDownAction;
import org.ml4j.dronez.histories.ForwardBackStateActionSequenceHistory;
import org.ml4j.dronez.histories.LeftRightStateActionSequenceHistory;
import org.ml4j.dronez.histories.UpDownStateActionSequenceHistory;
import org.ml4j.dronez.models.DroneModel;
import org.ml4j.dronez.models.DummySpinModel;
import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateActionSequenceHistory;
import org.ml4j.util.SerializationHelper;

/**
 * DroneModelLearner which uses 4 instances of SingleDimensionDroneModelLearner to generate independent Models for left/right,up/down,forward/back and spin dimensions,
 * which can then be used to construct a DroneModel 
 *
 * 
 * @author Michael Lavelle
 *
 */
public class DroneModelLearner implements ModelLearner<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> {

	public final static Class<? extends Model<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction>> MODEL_CLASS = DroneModel.class;
	
	private <A extends NumericAction> SingleDimensionDroneModelLearner<A> createSingleDimensionDroneModelLearner(Class<A> clazz,double minimumPosition,double maximumPosition,double minimumVelocity,double maximumVelocity)
	{
		return new SingleDimensionDroneModelLearner<A>(serializationHelper,minimumPosition,maximumPosition,minimumVelocity,maximumVelocity,clazz.getSimpleName(),recentActionCount,recentActionsAndLatestActionMask);
	}

	private int recentActionCount;
	private SerializationHelper serializationHelper;
	
	private boolean[] recentActionsAndLatestActionMask;
	public DroneModelLearner(int recentActionCount,boolean[] recentActionsAndLatestActionMask,SerializationHelper serializationHelper)
	{
		this.recentActionCount = recentActionCount;
		this.recentActionsAndLatestActionMask = recentActionsAndLatestActionMask;
		this.serializationHelper = serializationHelper;
	}
	
	@Override
	public Model<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> learnModel(
			StateActionSequenceHistory<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> stateActionStateHistory) {
		
		
		// Learn 4 models
		
		Model<PositionVelocityWithRecentActions<LeftRightAction>,PositionVelocityWithRecentActions<LeftRightAction>,LeftRightAction> leftRightModel 
				= createSingleDimensionDroneModelLearner(LeftRightAction.class,-2.5,2.5,-0.5,0.5).learnModel(new LeftRightStateActionSequenceHistory(stateActionStateHistory));
		
		Model<PositionVelocityWithRecentActions<UpDownAction>,PositionVelocityWithRecentActions<UpDownAction>,UpDownAction> upDownModel 
		= createSingleDimensionDroneModelLearner(UpDownAction.class,-1,1,-0.5,0.5).learnModel(new UpDownStateActionSequenceHistory(stateActionStateHistory));

		Model<PositionVelocityWithRecentActions<ForwardBackAction>,PositionVelocityWithRecentActions<ForwardBackAction>,ForwardBackAction> forwardBackModel 
		= createSingleDimensionDroneModelLearner(ForwardBackAction.class,0,4,-0.5,0.5).learnModel(new ForwardBackStateActionSequenceHistory(stateActionStateHistory));

		//Model<PositionVelocityWithRecentActions<SpinAction>,PositionVelocityWithRecentActions<SpinAction>,SpinAction> spinModel 
		//= createSingleDimensionDroneModelLearner(SpinAction.class,0,2 * Math.PI,-0.5,0.5).learnModel(new SpinStateActionSequenceHistory(stateActionStateHistory));

		
		return new DroneModel(leftRightModel,upDownModel,forwardBackModel,new DummySpinModel(recentActionCount));
	}

}
