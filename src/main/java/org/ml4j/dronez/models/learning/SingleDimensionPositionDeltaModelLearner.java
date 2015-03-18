package org.ml4j.dronez.models.learning;

import java.util.List;

import org.ml4j.algorithms.impl.RegularizationContext;
import org.ml4j.algorithms.impl.SimpleRegularizationContext;
import org.ml4j.algorithms.supervised.LinearRegressionMultiAlgorithm;
import org.ml4j.algorithms.supervised.LinearRegressionMultiHypothesisFunction;
import org.ml4j.algorithms.supervised.impl.LinearRegressionMultiAlgorithmImpl;
import org.ml4j.dronez.DroneStateActionLinearRegressionFeaturesMapper;
import org.ml4j.dronez.DroneStatePositionVelocityLabelMapper;
import org.ml4j.dronez.LinearApproximationDeltaPositionWithVelocityModel;
import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.PositionDeltaWithVelocity;
import org.ml4j.dronez.VelocityAndRecentActions;
import org.ml4j.mdp.Model;
import org.ml4j.mdp.StateActionSequenceHistory;

/**
 * @author Michael Lavelle
 *
 */
public class SingleDimensionPositionDeltaModelLearner<A extends NumericAction> implements ModelLearner<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> {

	private static final double MAX_POSITION_DELTA = 0.5d;
	private static final double MIN_POSITION_DELTA = -0.5d;

	private List<A> allActions;
	private int recentActionCount;
	
	public SingleDimensionPositionDeltaModelLearner(List<A> allActions,int recentActionCount)
	{
		this.allActions = allActions;
		this.recentActionCount = recentActionCount;
	}

	
	@Override
	public Model<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> learnModel(
			StateActionSequenceHistory<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> stateActionStateHistory) {
		
		// Use Linear Regression to predict next position and velocity from current velocity, recent actions, and the latest action.
		
		RegularizationContext context = new SimpleRegularizationContext(10);
		LinearRegressionMultiAlgorithm<RegularizationContext> modelLearningRegressionAlgorithm =  new LinearRegressionMultiAlgorithmImpl();;
		LinearRegressionMultiHypothesisFunction learnedDistanceToTargetHyp = modelLearningRegressionAlgorithm
				.getOptimalHypothesisFunction(
						stateActionStateHistory
								.getInitialStateActionHistory(new DroneStateActionLinearRegressionFeaturesMapper2<A>(recentActionCount)),
								stateActionStateHistory.getEndStateHistory(new DroneStatePositionVelocityLabelMapper()), context);

		System.out.println("Learned Delta Position/Velocity Hypothesis Function");

		System.out.println(learnedDistanceToTargetHyp);

		// Obtain a deterministic model
		Model<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> linearApproxDistanceToTargetModel = new LinearApproximationDeltaPositionWithVelocityModel<A>(
				allActions, new DroneStatePositionVelocityLabelMapper(),
				new DroneStateActionLinearRegressionFeaturesMapper2<A>(recentActionCount), learnedDistanceToTargetHyp,
				MAX_POSITION_DELTA - MIN_POSITION_DELTA, null, null, null, null, recentActionCount);
		
	
		return linearApproxDistanceToTargetModel;
	}

}
