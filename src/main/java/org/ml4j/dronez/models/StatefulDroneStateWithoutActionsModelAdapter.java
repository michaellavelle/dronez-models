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
package org.ml4j.dronez.models;

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
import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.SpinAction;
import org.ml4j.dronez.SpinActionExtractor;
import org.ml4j.dronez.UpDownAction;
import org.ml4j.dronez.UpDownActionExtractor;
import org.ml4j.mdp.Model;
/**
 * Adapts a stateless (in terms of drone state) model which includes recent actions as part of drone state,
 * into a stateful model which does not contain recent actions in the state.
 * 
 * @author Michael Lavelle
 *
 */
public class StatefulDroneStateWithoutActionsModelAdapter implements Model<DroneState, DroneState, DroneAction>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Model<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> droneModel;
	private List<DroneAction> takenActions;
	private int iteration = 0;
	private DroneState previousDroneState;
	private int modelRecentActionCount;
		
	public StatefulDroneStateWithoutActionsModelAdapter(Model<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> droneModel,int modelRecentActionCount)
	{
		this.droneModel = droneModel;
		this.takenActions = new ArrayList<DroneAction>();
		this.modelRecentActionCount = modelRecentActionCount;
	}
	
	@Override
	public DroneState getInitialState() {
		DroneStateWithRecentActions state = droneModel.getInitialState();
		return new DroneState(state.getLeftRightPositionVelocity(),state.getUpDownPositionVelocity(),state.getForwardBackPositionVelocity(),state.getSpinPositionVelocity());
	}
	
	private <A extends NumericAction> List<A> getRecentActions(ActionExtractor<A> extractor)
	{
		List<DroneAction> recentActions = new ArrayList<DroneAction>();
		int actionTakenCount = Math.min(iteration,modelRecentActionCount);
		int notTakenActionCount = modelRecentActionCount - actionTakenCount;
		int iterationStart = iteration - actionTakenCount;
		for (int i = 0; i < notTakenActionCount; i++)
		{
			recentActions.add(new DroneAction(LeftRightAction.NO_OP,UpDownAction.NO_OP,ForwardBackAction.NO_OP,SpinAction.NO_OP));
		}
		for (int j = iterationStart; j < iteration; j++)
		{
			recentActions.add(takenActions.get(j));
		}
		return extractor.getActions(recentActions);
	}

	@Override
	public DroneState getState(DroneState initialState, DroneAction action) {
		
		if (previousDroneState != null)
		{
			// Check equality
			boolean equal = initialState.getLeftRightPositionVelocity().getPosition() == previousDroneState.getLeftRightPositionVelocity().getPosition()
					&& initialState.getLeftRightPositionVelocity().getVelocity() == previousDroneState.getLeftRightPositionVelocity().getVelocity() &&
							initialState.getUpDownPositionVelocity().getPosition() == previousDroneState.getUpDownPositionVelocity().getPosition()
							&& initialState.getUpDownPositionVelocity().getVelocity() == previousDroneState.getUpDownPositionVelocity().getVelocity() &&
									initialState.getForwardBackPositionVelocity().getPosition() == previousDroneState.getForwardBackPositionVelocity().getPosition()
									&& initialState.getForwardBackPositionVelocity().getVelocity() == previousDroneState.getForwardBackPositionVelocity().getVelocity() &&
											initialState.getSpinPositionVelocity().getPosition() == previousDroneState.getSpinPositionVelocity().getPosition()
											&& initialState.getSpinPositionVelocity().getVelocity() == previousDroneState.getSpinPositionVelocity().getVelocity();
			// Check that the initialState passed in is the same as that recorded by this stateful model
			if (!equal)
			{
				throw new RuntimeException("State passed into this stateful model does not match the expected state");
			}
		}
		
		
		PositionVelocityWithRecentActions<LeftRightAction> lr = new PositionVelocityWithRecentActions<LeftRightAction>(initialState.getLeftRightPositionVelocity().getPosition(),initialState.getLeftRightPositionVelocity().getVelocity(),getRecentActions(new LeftRightActionExtractor()));;
		PositionVelocityWithRecentActions<UpDownAction> ud = new PositionVelocityWithRecentActions<UpDownAction>(initialState.getUpDownPositionVelocity().getPosition(),initialState.getUpDownPositionVelocity().getVelocity(),getRecentActions(new UpDownActionExtractor()));;;
		PositionVelocityWithRecentActions<ForwardBackAction> fb = new PositionVelocityWithRecentActions<ForwardBackAction>(initialState.getForwardBackPositionVelocity().getPosition(),initialState.getForwardBackPositionVelocity().getVelocity(),getRecentActions(new ForwardBackActionExtractor()));;;
		PositionVelocityWithRecentActions<SpinAction> sp = new PositionVelocityWithRecentActions<SpinAction>(initialState.getSpinPositionVelocity().getPosition(),initialState.getSpinPositionVelocity().getVelocity(),getRecentActions(new SpinActionExtractor()));;;

		
		DroneStateWithRecentActions initialStateWithRecentActions = new DroneStateWithRecentActions(lr,ud,fb,sp);
		DroneStateWithRecentActions state = droneModel.getState(initialStateWithRecentActions, action);
		
		takenActions.add(action);
		iteration++;
		DroneState newDroneState =  new DroneState(state.getLeftRightPositionVelocity(),state.getUpDownPositionVelocity(),state.getForwardBackPositionVelocity(),state.getSpinPositionVelocity());
		this.previousDroneState = newDroneState;
		return newDroneState;
	}
}
