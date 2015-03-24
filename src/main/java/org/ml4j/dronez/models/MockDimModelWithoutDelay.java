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
	private int modelWithDelayRecentActionCount;
	
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
	
	private List<A> fillOutModelActions(List<A> actions,A action)
	{
		List<A> newActions = new ArrayList<A>();
		/*
		int modelActionCount = actions.size() - getDelayToRemoveInIterations();
		List<A> allActions = new ArrayList<A>();
		
		allActions.addAll(actions);
		allActions.add(action);
		for (int i = 0; i < modelActionCount; i++ )
		{
			newActions.add(allActions.get(i));
		}
		
		for (int i = 0; i < getDelayToRemoveInIterations() - 1; i++)
		{
			newActions.add(noOpAction);
		}
		
		*/
		

		int newRecentActionCount = modelWithDelayRecentActionCount - getDelayToRemoveInIterations() - 1;
		for (int i = actions.size() - newRecentActionCount - 1; i < actions.size(); i++)
		{
			newActions.add(actions.get(i));
		}
		
		newActions.add(action);
		for (int i = 0; i < getDelayToRemoveInIterations(); i++)
		{
			newActions.add(noOpAction);

		}
		
		return newActions;
	}
	
	
	@Override
	public TargetRelativePositionWithVelocityAndRecentActions<A> getInitialState() {
		
		TargetRelativePositionWithVelocityAndRecentActions<A> state = model.getInitialState();
		
		return new TargetRelativePositionWithVelocityAndRecentActions<A>(state.getPosition(),state.getVelocity(),fillOutModelActions(state.getRecentActions(),noOpAction));
	}

	@Override
	public TargetRelativePositionWithVelocityAndRecentActions<A> getState(
			TargetRelativePositionWithVelocityAndRecentActions<A> state, A arg1) {
		TargetRelativePositionWithVelocityAndRecentActions<A> newState
		
		 = new TargetRelativePositionWithVelocityAndRecentActions<A>(state.getPosition(),state.getVelocity(),fillOutModelActions(state.getRecentActions(),arg1));
	
		return model.getState(newState, arg1);
	}

}
