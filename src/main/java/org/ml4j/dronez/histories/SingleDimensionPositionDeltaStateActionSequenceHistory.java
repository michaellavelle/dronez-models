package org.ml4j.dronez.histories;

import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.PositionDeltaWithVelocity;
import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.VelocityAndRecentActions;
import org.ml4j.mapping.LabeledData;
import org.ml4j.mdp.StateAction;
import org.ml4j.mdp.StateActionSequenceHistory;

public class SingleDimensionPositionDeltaStateActionSequenceHistory<A extends NumericAction> extends StateActionSequenceHistory<VelocityAndRecentActions<A>, PositionDeltaWithVelocity, A> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SingleDimensionPositionDeltaStateActionSequenceHistory(StateActionSequenceHistory<PositionVelocityWithRecentActions<A>, PositionVelocityWithRecentActions<A>, A> history)
	{
		int iteration = 0;
		for (LabeledData<StateAction<PositionVelocityWithRecentActions<A>, A>, PositionVelocityWithRecentActions<A>> data : history.getLabeledDataSet())
		{
			double positionDelta = data.getLabel().getPosition() - data.getData().getState().getPosition();
			onStateActionStateSequence(iteration++, new VelocityAndRecentActions<A>(data.getData().getState().getVelocity(),data.getData().getState().getRecentActions()), data.getData().getAction(), new PositionDeltaWithVelocity(positionDelta,data.getLabel().getVelocity()));
		}
	}
	
	
}
