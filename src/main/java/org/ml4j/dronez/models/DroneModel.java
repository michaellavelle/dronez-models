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

import org.ml4j.dronez.DroneAction;
import org.ml4j.dronez.DroneStateWithRecentActions;
import org.ml4j.dronez.ForwardBackAction;
import org.ml4j.dronez.LeftRightAction;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.SpinAction;
import org.ml4j.dronez.UpDownAction;
import org.ml4j.mdp.Model;
/**
 * Stateless (in terms of drone state) DroneModel which delegates to 4 single-dimension Models for left/right,up/down,forward/back and spin dimensions.
 * 
 * @author Michael Lavelle
 *
 */
public class DroneModel implements Model<DroneStateWithRecentActions, DroneStateWithRecentActions, DroneAction> {

	private Model<PositionVelocityWithRecentActions<LeftRightAction>, PositionVelocityWithRecentActions<LeftRightAction>, LeftRightAction> leftRightDroneModel;
	private Model<PositionVelocityWithRecentActions<UpDownAction>, PositionVelocityWithRecentActions<UpDownAction>, UpDownAction> upDownDroneModel;
	private Model<PositionVelocityWithRecentActions<ForwardBackAction>, PositionVelocityWithRecentActions<ForwardBackAction>, ForwardBackAction> forwardBackDroneModel;
	private Model<PositionVelocityWithRecentActions<SpinAction>, PositionVelocityWithRecentActions<SpinAction>, SpinAction> spinDroneModel;

	
	public DroneModel(Model<PositionVelocityWithRecentActions<LeftRightAction>, PositionVelocityWithRecentActions<LeftRightAction>, LeftRightAction> leftRightDroneModel,
						Model<PositionVelocityWithRecentActions<UpDownAction>, PositionVelocityWithRecentActions<UpDownAction>, UpDownAction> upDownDroneModel,
						Model<PositionVelocityWithRecentActions<ForwardBackAction>, PositionVelocityWithRecentActions<ForwardBackAction>, ForwardBackAction> forwardBackDroneModel,
						Model<PositionVelocityWithRecentActions<SpinAction>, PositionVelocityWithRecentActions<SpinAction>, SpinAction> spinDroneModel)
	{
		this.leftRightDroneModel = leftRightDroneModel;
		this.upDownDroneModel = upDownDroneModel;
		this.forwardBackDroneModel = forwardBackDroneModel;
		this.spinDroneModel = spinDroneModel;
	}
	
	
	/**
	 * 
	 */ 
	private static final long serialVersionUID = 1L;

	@Override
	public DroneStateWithRecentActions getInitialState() {
		return new DroneStateWithRecentActions(leftRightDroneModel.getInitialState(),upDownDroneModel.getInitialState(),forwardBackDroneModel.getInitialState(),spinDroneModel.getInitialState());
	}

	@Override
	public DroneStateWithRecentActions getState(DroneStateWithRecentActions state, DroneAction action) {
		return new DroneStateWithRecentActions(leftRightDroneModel.getState(state.getLeftRightPositionVelocity(), action.getLeftRightAction()),upDownDroneModel.getState(state.getUpDownPositionVelocity(), action.getUpDownAction()),forwardBackDroneModel.getState(state.getForwardBackPositionVelocity(), action.getForwardBackAction()),spinDroneModel.getState(state.getSpinPositionVelocity(), action.getSpinAction()));
	}

}
