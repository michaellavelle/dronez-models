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
package org.ml4j.dronez.util;

import java.util.ArrayList;
import java.util.List;

import org.ml4j.dronez.ActionExtractor;
import org.ml4j.dronez.DroneAction;
import org.ml4j.dronez.DroneState;
import org.ml4j.dronez.DroneStateWithRecentActions;
import org.ml4j.dronez.ForwardBackAction;
import org.ml4j.dronez.ForwardBackActionExtractor;
import org.ml4j.dronez.LeftRightAction;
import org.ml4j.dronez.LeftRightActionExtractor;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.SpinAction;
import org.ml4j.dronez.SpinActionExtractor;
import org.ml4j.dronez.UpDownAction;
import org.ml4j.dronez.UpDownActionExtractor;
import org.ml4j.mapping.LabeledData;
import org.ml4j.mdp.StateAction;
import org.ml4j.mdp.StateActionSequenceHistory;
import org.ml4j.util.SerializationHelper;
/**
 * Utility class which loads a StateActionSequenceHistory<DroneState,DroneState,DroneAction>, and converts to a 
 * StateActionSequenceHistory<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction>
 * 
 * @author michael
 *
 */
public class StateActionSequenceHistoryConvertingLoader {

	public static  StateActionSequenceHistory<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> convert(StateActionSequenceHistory<DroneState, DroneState, DroneAction> history1,int recentActionCount)
	{
		StateActionSequenceHistory<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> history = new StateActionSequenceHistory<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction>();
		int iteration = 0;
		for (LabeledData<StateAction<DroneState, DroneAction>, DroneState> data : history1
				.getLabeledDataSet()) {
			if (iteration >= recentActionCount + 50) {
				//System.out.println(data.getData().getState().getLeftRightPositionVelocity().getPosition() + "," + data.getData().getState().getLeftRightPositionVelocity().getVelocity())
				System.out.println(data.getData().getAction().getLeftRightAction().getValue() + "," + data.getData().getState().getLeftRightPositionVelocity().getVelocity());
				PositionVelocityWithRecentActions<LeftRightAction> lr1 = new PositionVelocityWithRecentActions<LeftRightAction>(
						data.getData().getState()
								.getLeftRightPositionVelocity().getPosition(),
						data.getData().getState()
								.getLeftRightPositionVelocity().getVelocity(),
						getRecentActions(history1, iteration,
								new LeftRightActionExtractor(),recentActionCount));
				PositionVelocityWithRecentActions<UpDownAction> ud1 = new PositionVelocityWithRecentActions<UpDownAction>(
						data.getData().getState().getUpDownPositionVelocity()
								.getPosition(), data.getData().getState()
								.getUpDownPositionVelocity().getPosition(),
						getRecentActions(history1, iteration,
								new UpDownActionExtractor(),recentActionCount));
				PositionVelocityWithRecentActions<ForwardBackAction> fb1 = new PositionVelocityWithRecentActions<ForwardBackAction>(
						data.getData().getState()
								.getForwardBackPositionVelocity().getPosition(),
						data.getData().getState()
								.getForwardBackPositionVelocity().getPosition(),
						getRecentActions(history1, iteration,
								new ForwardBackActionExtractor(),recentActionCount));
				PositionVelocityWithRecentActions<SpinAction> sp1 = new PositionVelocityWithRecentActions<SpinAction>(
						data.getData().getState().getSpinPositionVelocity()
								.getPosition(), data.getData().getState()
								.getSpinPositionVelocity().getPosition(),
						getRecentActions(history1, iteration,
								new SpinActionExtractor(),recentActionCount));

				PositionVelocityWithRecentActions<LeftRightAction> lr2 = new PositionVelocityWithRecentActions<LeftRightAction>(
						data.getLabel().getLeftRightPositionVelocity()
								.getPosition(), data.getLabel()
								.getLeftRightPositionVelocity().getVelocity(),
						getRecentActions(history1, iteration,
								new LeftRightActionExtractor(),recentActionCount));
				PositionVelocityWithRecentActions<UpDownAction> ud2 = new PositionVelocityWithRecentActions<UpDownAction>(
						data.getLabel().getUpDownPositionVelocity()
								.getPosition(), data.getLabel()
								.getUpDownPositionVelocity().getPosition(),
						getRecentActions(history1, iteration,
								new UpDownActionExtractor(),recentActionCount));
				PositionVelocityWithRecentActions<ForwardBackAction> fb2 = new PositionVelocityWithRecentActions<ForwardBackAction>(
						data.getLabel().getForwardBackPositionVelocity()
								.getPosition(),
						data.getLabel().getForwardBackPositionVelocity()
								.getPosition(), getRecentActions(history1,
								iteration, new ForwardBackActionExtractor(),recentActionCount));
				PositionVelocityWithRecentActions<SpinAction> sp2 = new PositionVelocityWithRecentActions<SpinAction>(
						data.getLabel().getSpinPositionVelocity().getPosition(),
						data.getLabel().getSpinPositionVelocity().getPosition(),
						getRecentActions(history1, iteration,
								new SpinActionExtractor(),recentActionCount));

				DroneStateWithRecentActions initial = new DroneStateWithRecentActions(
						lr1, ud1, fb1, sp1);
				DroneStateWithRecentActions end = new DroneStateWithRecentActions(
						lr2, ud2, fb2, sp2);
				history.onStateActionStateSequence(
						iteration
								- recentActionCount,
						initial, data.getData().getAction(), end);
			}
			iteration++;

		}

		return history;
	}
	
	
	public static StateActionSequenceHistory<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> getStateActionSequenceHistory(
			String historyId,int recentActionCount) {
		// Load StateActionSequenceHistory<DroneState,DroneState,DroneAction>

		// Convert to a history with recent actions encapsulated as part of the state

		SerializationHelper serializationHelper = new SerializationHelper(
				StateActionSequenceHistoryConvertingLoader.class.getClassLoader(),
				"org/ml4j/dronez/histories");

		@SuppressWarnings("unchecked")
		StateActionSequenceHistory<DroneState, DroneState, DroneAction> history1 = serializationHelper
				.deserialize(StateActionSequenceHistory.class, historyId);

	
		return convert(history1,recentActionCount);
	}

	private static <A> List<A> getRecentActions(
			StateActionSequenceHistory<DroneState, DroneState, DroneAction> history,
			int currentIteration, ActionExtractor<A> actionExtractor,int recentActionCount) {

		List<DroneAction> actions = new ArrayList<DroneAction>();
		for (int iteration = currentIteration
				- recentActionCount; iteration <= currentIteration; iteration++) {
			LabeledData<StateAction<DroneState, DroneAction>, DroneState> data = history
					.getStateActionStateSequence(iteration);
			actions.add(data.getData().getAction());
		}

		return actionExtractor.getActions(actions);
	}

}
