package com.dub.spring.breadthFirstSearch;

/** container for AJAX response */
public class StepResult {
	
	private Graph graph;
	private Status status;
	

	
	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}




	public enum Status {
		STEP, FINISHED
	}
}
