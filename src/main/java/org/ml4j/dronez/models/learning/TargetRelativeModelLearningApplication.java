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

import java.io.Serializable;
import java.util.Arrays;

import org.ml4j.dronez.ForwardBackAction;
import org.ml4j.dronez.LeftRightAction;
import org.ml4j.dronez.LinearApproximationDeltaPositionWithVelocityModel;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.SpinAction;
import org.ml4j.dronez.UpDownAction;
import org.ml4j.dronez.models.SingleDimensionDroneDistanceToTargetPositionModel;
import org.ml4j.util.SerializationHelper;
/**
 * 
 * @author Michael Lavelle
 *

 */
public class TargetRelativeModelLearningApplication<S extends Serializable,T extends Serializable,A extends Serializable> {

	private	static SerializationHelper serializationHelper = new SerializationHelper(TargetRelativeModelLearningApplication.class.getClassLoader(),"org/ml4j/dronez/models");

	
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		LinearApproximationDeltaPositionWithVelocityModel<LeftRightAction> leftRightDeltaModel = serializationHelper.deserialize(LinearApproximationDeltaPositionWithVelocityModel.class, "droneDeltaPositionLeftRightActionModel");
		LinearApproximationDeltaPositionWithVelocityModel<UpDownAction> upDownDeltaModel = serializationHelper.deserialize(LinearApproximationDeltaPositionWithVelocityModel.class, "droneDeltaPositionUpDownActionModel");
		LinearApproximationDeltaPositionWithVelocityModel<ForwardBackAction> forwardBackDeltaModel = serializationHelper.deserialize(LinearApproximationDeltaPositionWithVelocityModel.class, "droneDeltaPositionForwardBackActionModel");
		LinearApproximationDeltaPositionWithVelocityModel<SpinAction> spinDeltaModel = serializationHelper.deserialize(LinearApproximationDeltaPositionWithVelocityModel.class, "droneDeltaPositionSpinActionModel");

		//int recentActionCount = PositionVelocityWithRecentActions.DEFAULT_RECENT_ACTION_COUNT;
		int recentActionCount = 10;
		
		SingleDimensionDroneDistanceToTargetPositionModel<LeftRightAction> distanceToTargetPositionLeftRightModel = new SingleDimensionDroneDistanceToTargetPositionModel<LeftRightAction>(leftRightDeltaModel,-2.5,2.5,-0.5,0.5,Arrays.asList(LeftRightAction.ALL_ACTIONS),recentActionCount);
		SingleDimensionDroneDistanceToTargetPositionModel<UpDownAction> distanceToTargetPositionUpDownModel = new SingleDimensionDroneDistanceToTargetPositionModel<UpDownAction>(upDownDeltaModel,-2.5,2.5,-0.5,0.5,Arrays.asList(UpDownAction.ALL_ACTIONS),recentActionCount);
		SingleDimensionDroneDistanceToTargetPositionModel<ForwardBackAction> distanceToTargetPositionForwardBackModel = new SingleDimensionDroneDistanceToTargetPositionModel<ForwardBackAction>(forwardBackDeltaModel,-2.5,2.5,-0.5,0.5,Arrays.asList(ForwardBackAction.ALL_ACTIONS),recentActionCount);
		SingleDimensionDroneDistanceToTargetPositionModel<SpinAction> distanceToTargetPositionSpinModel = new SingleDimensionDroneDistanceToTargetPositionModel<SpinAction>(spinDeltaModel,-2.5,2.5,-0.5,0.5,Arrays.asList(SpinAction.ALL_ACTIONS),recentActionCount);

		
		
		// Learn model from history, and serialize
		serializationHelper.serialize(distanceToTargetPositionLeftRightModel, "distanceToTargetPositionLeftRightModel_19032015_1");
		serializationHelper.serialize(distanceToTargetPositionUpDownModel, "distanceToTargetPositionUpDownModel_19032015_1");
		serializationHelper.serialize(distanceToTargetPositionForwardBackModel, "distanceToTargetPositionForwardBackModel_19032015_1");
		serializationHelper.serialize(distanceToTargetPositionSpinModel, "distanceToTargetPositionSpinModel_19032015_1");

	}
	
	
}
