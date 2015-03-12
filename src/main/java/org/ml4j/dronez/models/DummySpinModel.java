package org.ml4j.dronez.models;

import java.util.ArrayList;
import java.util.List;

import org.ml4j.dronez.PositionVelocityWithRecentActions;
import org.ml4j.dronez.SpinAction;
import org.ml4j.mdp.Model;

public class DummySpinModel implements Model<PositionVelocityWithRecentActions<SpinAction>,PositionVelocityWithRecentActions<SpinAction>,SpinAction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private int recentActionCount = PositionVelocityWithRecentActions.DEFAULT_RECENT_ACTION_COUNT;
	
	@Override
	public PositionVelocityWithRecentActions<SpinAction> getInitialState() {
		return new PositionVelocityWithRecentActions<SpinAction>(0,0,getActions());
	}
	
	
	public DummySpinModel(int recentActionCount)
	{
		this.recentActionCount = recentActionCount;
	}
	

	

	public int getRecentActionCount() {
		return recentActionCount;
	}


	@Override
	public PositionVelocityWithRecentActions<SpinAction> getState(
			PositionVelocityWithRecentActions<SpinAction> arg0, SpinAction arg1) {
		return new PositionVelocityWithRecentActions<SpinAction>(0,0,getActions());
	}
	
	public List<SpinAction> getActions()
	{
		List<SpinAction> actions = new ArrayList<SpinAction>();
		for (int i = 0; i < recentActionCount; i++)
		{
			actions.add(SpinAction.NO_OP);
		}
		return actions;
	}

}
