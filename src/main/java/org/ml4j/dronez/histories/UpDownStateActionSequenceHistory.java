package org.ml4j.dronez.histories;

import org.ml4j.dronez.DroneAction;
import org.ml4j.dronez.DroneStateWithRecentActions;
import org.ml4j.dronez.UpDownAction;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.mapping.LabeledData;
import org.ml4j.mdp.StateAction;
import org.ml4j.mdp.StateActionSequenceHistory;

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
/**
 * Encapuslates the state action sequence history for the Up/Down dimension.
 * 
 * Constructs this history given an all-dimensions history
 * 
 * @author Michael Lavelle
 *
 */
public class UpDownStateActionSequenceHistory extends StateActionSequenceHistory<PositionVelocityWithRecentActions<UpDownAction>, PositionVelocityWithRecentActions<UpDownAction>, UpDownAction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UpDownStateActionSequenceHistory(StateActionSequenceHistory<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> history)
	{
		int iteration = 0;
		for (LabeledData<StateAction<DroneStateWithRecentActions, DroneAction>, DroneStateWithRecentActions> data : history.getLabeledDataSet())
		{
			onStateActionStateSequence(iteration++, data.getData().getState().getUpDownPositionVelocity(), data.getData().getAction().getUpDownAction(), data.getLabel().getUpDownPositionVelocity());
		}
	}
	

}
