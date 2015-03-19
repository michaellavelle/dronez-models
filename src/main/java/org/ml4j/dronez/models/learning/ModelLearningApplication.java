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

import org.ml4j.dronez.DroneAction;
import org.ml4j.dronez.DroneStateWithRecentActions;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.util.StateActionSequenceHistoryConvertingLoader;
import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateActionSequenceHistory;
import org.ml4j.util.SerializationHelper;
/**
 * Standalone application which deserializes state action sequence history,  uses a ModelLearner ( to be swapped
 * with an implementation we must write,  learns a Model and serializes it.
 * 
 * @author Michael Lavelle
 *
 * @param <S> Initial State
 * @param <A> Action
 * @param <T> Post-Action State
 */
public class ModelLearningApplication<S extends Serializable,T extends Serializable,A extends Serializable> {

	private	SerializationHelper serializationHelper = new SerializationHelper(ModelLearningApplication.class.getClassLoader(),"org/ml4j/dronez/models");

	
	private ModelLearner<S,T,A> modelLearner;
	
	
	public ModelLearningApplication(ModelLearner<S,T,A> modelLearner)
	{
		this.modelLearner = modelLearner;
	}
	
	
	public static void main(String[] args)
	{
		//int recentActionCount = PositionVelocityWithRecentActions.DEFAULT_RECENT_ACTION_COUNT;
		int recentActionCount = 10;
		
		
		// Load our state action sequence history
		StateActionSequenceHistory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> history = StateActionSequenceHistoryConvertingLoader.getStateActionSequenceHistory("flight_1426776162484",recentActionCount);
				
	
		// Consider only the first five recent actions, and ignore latest action
		boolean[] recentActionsAndLatestActionMask 
		 = new boolean[]{true,true,true,true,true,false,false,false,false,false,false };
		
		
		// Create our model learner.
		ModelLearner<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> modelLearner = new DroneModelLearner(recentActionCount,recentActionsAndLatestActionMask);
		
		// Create a model learning application
		ModelLearningApplication<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> modelLearningApplication = new ModelLearningApplication<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction>(modelLearner);
		
		
		
		// Learn model from history, and serialize
		modelLearningApplication.learnAndSerializeModel("droneModel_19032015_6",history);
	

	}
	
	public void learnAndSerializeModel(String modelName,StateActionSequenceHistory<S,T,A> droneStateHistory)
	{
			
		// Learn model from the model learner
		Model<S,T,A> model = modelLearner.learnModel(droneStateHistory);
		
		// Serialize the model, with a key of modelName
		serializationHelper.serialize(model, modelName);
			
	}
	
}
