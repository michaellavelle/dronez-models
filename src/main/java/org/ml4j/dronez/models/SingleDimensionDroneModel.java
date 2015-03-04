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

import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.PositionDeltaWithVelocity;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.VelocityAndRecentActions;
import org.ml4j.mdp.Model;

/**
 * SingleDimensionDroneModel which delegates calculations to an initial-position-agnostic Model
 * 
 * @author Michael Lavelle
 *
 */
public class SingleDimensionDroneModel<A extends NumericAction> implements Model<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Model<VelocityAndRecentActions<A>,PositionDeltaWithVelocity,A> positionDeltaModel;

	public SingleDimensionDroneModel(Model<VelocityAndRecentActions<A>,PositionDeltaWithVelocity,A> positionDeltaModel,double minimumPosition,double maximumPosition,double minimumVelocity,double maximumVelocity,List<A> allActions )
	{
		this.minimumPosition = minimumPosition;
		this.maximumPosition = maximumPosition;
		this.minimumVelocity = minimumVelocity;
		this.maximumVelocity = maximumVelocity;
		this.positionDeltaModel = positionDeltaModel;
	}
	private double minimumPosition;
	private double maximumPosition;
	private double minimumVelocity;
	private double maximumVelocity;
	private List<A> allActions;
	
	@Override
	public PositionVelocityWithRecentActions<A> getInitialState() {
		
		double positionRange = maximumPosition - minimumPosition;
		double randomPosition = Math.random() * positionRange - minimumPosition;
		double velocityRange = maximumVelocity - minimumVelocity;
		double randomVelocity = Math.random() * velocityRange - minimumVelocity;
		List<A> randomActions = new ArrayList<A>();
		for (int i = 0; i < PositionVelocityWithRecentActions.RECENT_ACTION_COUNT;i++)
		{
			int randomIndex = (int)(Math.random() * allActions.size());
			randomActions.add(allActions.get(randomIndex));
		}
		
		
		return new PositionVelocityWithRecentActions<A>(randomPosition,randomVelocity,randomActions);
	}

	@Override
	public PositionVelocityWithRecentActions<A> getState(PositionVelocityWithRecentActions<A> currentState, A action) {

		PositionDeltaWithVelocity endPositionDeltaState = positionDeltaModel.getState(new VelocityAndRecentActions<A>(currentState.getVelocity(),currentState.getRecentActions()), action);
	
		List<A> initialStateRecentActions = currentState.getRecentActions();
		List<A> finalStateRecentActions = new ArrayList<A>();
		for (int i = 1; i < initialStateRecentActions.size(); i++)
		{
			finalStateRecentActions.add(initialStateRecentActions.get(i));
		}
		finalStateRecentActions.add(action);
		// Impose limits on position, simulating physical barriers such as walls
		
		double nextPosition = currentState.getPosition() + endPositionDeltaState.getPosition();
		if (nextPosition < minimumPosition)
		{
			nextPosition = minimumPosition;
		}
		if (nextPosition > maximumPosition)
		{
			nextPosition = maximumPosition;
		}
		return new PositionVelocityWithRecentActions<A>(nextPosition,endPositionDeltaState.getVelocity(),finalStateRecentActions);
	
	}

}
