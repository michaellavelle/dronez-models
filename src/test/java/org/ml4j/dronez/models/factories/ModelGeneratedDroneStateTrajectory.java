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
import org.ml4j.mdp.Model;
import org.ml4j.mdp.Trajectory;
/**
 * A trajectory of DroneState which is generated by repeatedly
 * applying a Model to the previous state on each action
 * 
 * @author Michael Lavelle
 *
 */
public class ModelGeneratedDroneStateTrajectory implements Trajectory<DroneState> {

	private Model<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> model;
	
	private DroneState initialDroneState;
	private DroneStateWithRecentActions currentState;
	private List<DroneAction> actions;
	private int recentActionCount;
	
	public ModelGeneratedDroneStateTrajectory(Model<DroneStateWithRecentActions,DroneStateWithRecentActions,DroneAction> model,DroneState initialState,List<DroneAction> actions,int recentActionCount)
	{
		this.model = model;
		this.initialDroneState = initialState;
		this.actions = actions;
		this.recentActionCount = recentActionCount;
	}
	
	@Override
	public DroneState getTarget(long iteration) {
		
		if (iteration < recentActionCount)
		{
			return initialDroneState;
		}
		
		
		if (iteration == recentActionCount)
		{
			PositionVelocityWithRecentActions<LeftRightAction> lr = new PositionVelocityWithRecentActions<LeftRightAction>(initialDroneState.getLeftRightPositionVelocity().getPosition(),initialDroneState.getLeftRightPositionVelocity().getVelocity(),getRecentActions((int)iteration,new LeftRightActionExtractor()));
			PositionVelocityWithRecentActions<UpDownAction> ud = new PositionVelocityWithRecentActions<UpDownAction>(initialDroneState.getUpDownPositionVelocity().getPosition(),initialDroneState.getUpDownPositionVelocity().getVelocity(),getRecentActions((int)iteration,new UpDownActionExtractor()));
			PositionVelocityWithRecentActions<ForwardBackAction> fb = new PositionVelocityWithRecentActions<ForwardBackAction>(initialDroneState.getForwardBackPositionVelocity().getPosition(),initialDroneState.getForwardBackPositionVelocity().getVelocity(),getRecentActions((int)iteration,new ForwardBackActionExtractor()));
			PositionVelocityWithRecentActions<SpinAction> s = new PositionVelocityWithRecentActions<SpinAction>(initialDroneState.getSpinPositionVelocity().getPosition(),initialDroneState.getSpinPositionVelocity().getVelocity(),getRecentActions((int)iteration,new SpinActionExtractor()));
		
			
			currentState = new DroneStateWithRecentActions(lr,ud,fb,s);
		}
		else
		{
			currentState = model.getState(currentState, actions.get((int)iteration));
		}
		return new DroneState(currentState.getLeftRightPositionVelocity(),currentState.getUpDownPositionVelocity(),currentState.getForwardBackPositionVelocity(),currentState.getSpinPositionVelocity());
	}


	private <A> List<A> getRecentActions(int iteration,ActionExtractor<A> actionExtractor)
	{
		List<DroneAction> recentActions = new ArrayList<DroneAction>();
		for (int i = 0; i < recentActionCount; i++)
		{
			recentActions.add(actions.get(iteration - recentActionCount + i));
		}
		return actionExtractor.getActions(recentActions);
	}
	
}
