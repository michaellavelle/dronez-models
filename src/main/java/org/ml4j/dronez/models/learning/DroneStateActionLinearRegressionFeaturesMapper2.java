/*
 * Copyright 2014 the original author or authors.
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

import org.ml4j.algorithms.FeaturesMapper;
import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.VelocityAndRecentActions;
import org.ml4j.mdp.StateAction;

/**
 * <p>
 * Extracts numeric feature vectors from
 * DroneStateAction<PositionVelocity,LeftRightAction> instances that we would
 * like to be able to build a linear approximation regression model from for our
 * Drone simulation model
 * 
 * Assume here that value can be approximated by a linear regression of numeric
 * mappings of starting state, action taken and ending state.
 * </p>
 *
 * @author Michael Lavelle
 */
public class DroneStateActionLinearRegressionFeaturesMapper2<A extends NumericAction> implements
		FeaturesMapper<StateAction<VelocityAndRecentActions<A>, A>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int recentActionCount = PositionVelocityWithRecentActions.DEFAULT_RECENT_ACTION_COUNT;
	
	
	public DroneStateActionLinearRegressionFeaturesMapper2(int recentActionCount)
	{
		this.recentActionCount = recentActionCount;
	}
	
	
	
	
	
	public void setRecentActionCount(int recentActionCount) {
		this.recentActionCount = recentActionCount;
	}





	public int getRecentActionCount() {
		return recentActionCount;
	}



	@Override
	public double[] toFeaturesVector(StateAction<VelocityAndRecentActions<A>, A> data) {
		
		
		//int actionIndex = data.getAction().ordinal();
		double[] features = new double[getFeatureCount()];
		features[0] = 1d;
		features[1] = data.getState().getVelocity();
		for (int i = 0; i < recentActionCount - 5; i++) {
			features[i + 2] = data.getState().getRecentActions().get(i).getValue();
		}
		//features[features.length - 1] = actionIndex;
		return features;
		// return new double[] {1d,data.getState().getVelocity(),actionIndex};

	}

	@Override
	public int getFeatureCount() {
		// return 4;
		return 2 + recentActionCount - 5;
	}

}
