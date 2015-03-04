package org.ml4j.dronez.models.learning;

import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.PositionDeltaWithVelocity;
import org.ml4j.dronez.VelocityAndRecentActions;
import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateActionSequenceHistory;

/**
 * Now that we have amended our DroneModel so that it uses 4 independent SingleDimensionDroneModels
 * for 4 dimensions each of which delegates to a learned Model<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A>, 
 * the revised goal of this project is to complete the implementation of this class which generates such a learned Model
 * 
 * @author Michael Lavelle
 *
 */
public class SingleDimensionPositionDeltaModelLearner<A extends NumericAction> implements ModelLearner<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> {

	
	@Override
	public Model<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> learnModel(
			StateActionSequenceHistory<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> stateActionStateHistory) {
		
		// TODO
		return null;
	}

}
