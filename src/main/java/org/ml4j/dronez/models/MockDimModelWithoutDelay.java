package org.ml4j.dronez.models;

import java.util.ArrayList;
import java.util.List;

import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.TargetRelativePositionWithVelocityAndRecentActions;
import org.ml4j.mdp.Model;

/**
 * 
 * @author Michael Lavelle
 * Requires 4 most recent actions
 *
 */

public class MockDimModelWithoutDelay<A extends NumericAction> implements Model<TargetRelativePositionWithVelocityAndRecentActions<A>, TargetRelativePositionWithVelocityAndRecentActions<A>, A> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Model<TargetRelativePositionWithVelocityAndRecentActions<A>, TargetRelativePositionWithVelocityAndRecentActions<A>, A> model;
	private A noOpAction;
	private Integer delayToRemoveInIterations;
	
	public MockDimModelWithoutDelay(Model<TargetRelativePositionWithVelocityAndRecentActions<A>, TargetRelativePositionWithVelocityAndRecentActions<A>, A> model,A noOpAction,int delayToRemoveInIterations)
	{
		this.model = model;
		this.noOpAction = noOpAction;
		this.delayToRemoveInIterations = delayToRemoveInIterations;
		if (delayToRemoveInIterations == 0 )
		{
			throw new IllegalArgumentException("Delay to remove must be greater than zero");
		}
	}
	
	
	public int getDelayToRemoveInIterations()
	{
		if (delayToRemoveInIterations == null)
		{
			delayToRemoveInIterations = 5;
		}
		return delayToRemoveInIterations;
	}
	
	private List<A> fillOut10Actions(List<A> actions,A action)
	{
		List<A> newActions = new ArrayList<A>();
		newActions.addAll(actions);
		newActions.add(action);
		for (int i = 0; i < getDelayToRemoveInIterations() - 1; i++)
		{
			newActions.add(noOpAction);
		}
		return newActions;
	}
	
	
	@Override
	public TargetRelativePositionWithVelocityAndRecentActions<A> getInitialState() {
		
		TargetRelativePositionWithVelocityAndRecentActions<A> state = model.getInitialState();
		
		return new TargetRelativePositionWithVelocityAndRecentActions<A>(state.getPosition(),state.getVelocity(),fillOut10Actions(state.getRecentActions(),noOpAction));
	}

	@Override
	public TargetRelativePositionWithVelocityAndRecentActions<A> getState(
			TargetRelativePositionWithVelocityAndRecentActions<A> state, A arg1) {
		TargetRelativePositionWithVelocityAndRecentActions<A> newState
		
		 = new TargetRelativePositionWithVelocityAndRecentActions<A>(state.getPosition(),state.getVelocity(),fillOut10Actions(state.getRecentActions(),arg1));
	
		return model.getState(newState, arg1);
	}

}
