package org.ml4j.dronez.models;

import java.util.ArrayList;
import java.util.List;

import org.ml4j.dronez.NumericAction;
import org.ml4j.dronez.TargetRelativePositionWithVelocityAndRecentActions;
import org.ml4j.mdp.Model;

/**
 * 
 * @author Michael Lavelle
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
	
	public MockDimModelWithoutDelay(Model<TargetRelativePositionWithVelocityAndRecentActions<A>, TargetRelativePositionWithVelocityAndRecentActions<A>, A> model,A noOpAction,int modelWithDelayRecentActionCount,int delayToRemoveInIterations)
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

	
	@Override
	public TargetRelativePositionWithVelocityAndRecentActions<A> getInitialState() {
		
		TargetRelativePositionWithVelocityAndRecentActions<A> state = model.getInitialState();
		//System.out.println()
		return state;
		//return new TargetRelativePositionWithVelocityAndRecentActions<A>(state.getPosition(),state.getVelocity(),fillOutModelActions(state.getRecentActions(),noOpAction));
	}

	private List<A> fillOutModelActions(List<A> recentActions, A action) {
		
		List<A> newRecentActions = new ArrayList<A>();
		newRecentActions.addAll(recentActions);
		for (int i = 0; i < delayToRemoveInIterations; i++)
		{
			newRecentActions.remove(0);
		}
		newRecentActions.add(action);
		int remaining = recentActions.size() - newRecentActions.size();
		for (int i = 0; i < remaining;i++)
		{
			newRecentActions.add(noOpAction);
		}
		return newRecentActions;
	}
	
	


	@Override
	public TargetRelativePositionWithVelocityAndRecentActions<A> getState(
			TargetRelativePositionWithVelocityAndRecentActions<A> state, A action) {
		
		
		TargetRelativePositionWithVelocityAndRecentActions<A> newState
		
		 = new TargetRelativePositionWithVelocityAndRecentActions<A>(state.getPosition(),state.getVelocity(),fillOutModelActions(state.getRecentActions(),action));
	
		int in = newState.getRecentActionCount() - delayToRemoveInIterations;
		
		// action not used here other than to build up model history
		return model.getState(newState, action);
	}

}
