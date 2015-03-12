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
package org.ml4j.dronez.models.factories;

import java.util.ArrayList;
import java.util.List;

import org.ml4j.dronez.DroneAction;
import org.ml4j.dronez.DroneState;
import org.ml4j.dronez.DroneStateActionSequenceDisplayer;
import org.ml4j.dronez.DroneStateWithRecentActions;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.models.learning.DroneModelLearner;
import org.ml4j.dronez.util.StateActionSequenceHistoryConvertingLoader;
import org.ml4j.mapping.LabeledData;
import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateAction;
import org.ml4j.mdp.StateActionSequenceHistory;

/**
 * Demonstrates how clients can obtain and use drone Models by id, using a SerializedModelFactory
 * 
 * The ids and classes of models creatable by the ModelFactory must be registered before factory use.
 * 
 * @author Michael Lavelle
 *
 */
public class ModelClientDemo {
	
	private static String modelId = "droneModel_12032015_3";

	public static void main(String[] args)
	{
		
		// Create a model factory which allows us to create Models given an id
		ModelFactory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> droneModelFactory = createModelFactory();		
		
		// Create Model by id
		Model<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> model = droneModelFactory.createModel(modelId);
		
		int modelRecentActionCount = PositionVelocityWithRecentActions.DEFAULT_RECENT_ACTION_COUNT;
		
		
		// Load a state action history that we can use to evaluate the Model
		StateActionSequenceHistory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> history = StateActionSequenceHistoryConvertingLoader.getStateActionSequenceHistory("flight_11032015_2",modelRecentActionCount);

		// Replay the history ( 150ms between actions), and display the Model-generated trajectory along with the actual trajectory
		DroneStateActionSequenceDisplayer<DroneStateWithRecentActions,DroneAction> displayer = new DroneStateActionSequenceDisplayer<DroneStateWithRecentActions,DroneAction>();
		displayer.setTargetTrajectory(new ModelGeneratedDroneStateTrajectory(model,getInitialState(history,modelRecentActionCount),getHistoricalActions(history),modelRecentActionCount),true);
		history.replay(displayer, 150);
		
	}
	
	private static DroneState getInitialState(StateActionSequenceHistory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> history,int recentActionCount)
	{
		DroneStateWithRecentActions s =  history.getStateActionStateSequence(recentActionCount).getData().getState();
		return new DroneState(s.getLeftRightPositionVelocity(),s.getUpDownPositionVelocity(),s.getForwardBackPositionVelocity(),s.getSpinPositionVelocity());
	}
	
	private static List<DroneAction> getHistoricalActions(StateActionSequenceHistory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> history)
	{
		List<DroneAction> actions = new ArrayList<DroneAction>();
		for (LabeledData<StateAction<DroneStateWithRecentActions, DroneAction>, DroneStateWithRecentActions> data : history.getLabeledDataSet())
		{
			actions.add(data.getData().getAction());
		}
		return actions;
	}

	private static ModelFactory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> createModelFactory() {
		
		// Create a serialized model factory
		SerializedModelFactory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> modelFactory = new SerializedModelFactory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction>();
		// Register a serialized model with the model factory
		modelFactory.registerModel(DroneModelLearner.MODEL_CLASS, modelId);
		
		return modelFactory;
	}
	

	
	
	
}
