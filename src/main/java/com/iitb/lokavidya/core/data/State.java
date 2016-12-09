package com.iitb.lokavidya.core.data;

import gui.Call;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class State {

	public List<Segment> changedState;
	public List<Segment> getChangedState() {
		return changedState;
	}

	public void setChangedState(List<Segment> changedState) {
		System.out.println("Changedstatesize: "+changedState.size());
		for(Segment s:changedState)
		{
			this.changedState.add(s);
			
		}
	}
	
	public State(State st) {
		// TODO Auto-generated constructor stub
		
		setChangedState(st.getChangedState());
	}
	
	public State(Project p) {
		// TODO Auto-generated constructor stub
		changedState=new ArrayList<Segment>();	
		
		try {
			
			for(Segment s:p.copyOrderingSequence())
				changedState.add(s);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public State() {
		// TODO Auto-generated constructor stub
		changedState=new ArrayList<Segment>();
	}
}
