package com.dub.site.breadthFirstSearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


/** for the stepwise implementation the queue has to be a field */

/** Graph has Vertices and Adjacency Lists */
public class BFSGraph extends Graph implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Vertex> vertices;
	
	private int index = 0;
	
	@JsonIgnore
	private Queue<Integer> queue;
	
	private boolean init;
	
	int time = 0;
	
	
	public BFSGraph() {
		vertices = new ArrayList<>();
		queue = new SimpleQueue<>();
		init = false;
	}
	
	// deep copy
	public BFSGraph(BFSGraph source) {
		this.queue = new SimpleQueue<>();
		this.vertices = new ArrayList<>();
		init = false;
		
		for (Vertex vertex : source.getVertices()) {
			BFSVertex bfsVertex = (BFSVertex)vertex;
			BFSVertex v = new BFSVertex(bfsVertex);// deep copy
			this.getVertices().add(v);	
		}

	}
	
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	public void setVertices(List<Vertex> vertices) {
		this.vertices = vertices;
	}
	

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	
	// main search method
	public void search(List<StepResult> snapshots) {
		System.out.println("search begin");
		
		// after initial push
		while (!queue.isEmpty()) {
			StepResult result = searchStep();
			snapshots.add(result);
		}
		
		System.out.println("search completed");
	}// search
	
	public void searchInit(String vertexName) {
		
		int lind = 0;
		
		for (lind = 0; lind < this.vertices.size(); lind++) {
			if (this.vertices.get(lind).getName().equals(vertexName)) {
				queue.push_back(lind);
				System.out.println("Start search from: " 
											+ this.vertices.get(lind).getName());
				break;
			}
		}// for
		
		if (lind == this.vertices.size()) {
			throw new RuntimeException("initialization error");
		}
		
		init = true;
	}
	
	public StepResult searchStep() {
		
		StepResult result = new StepResult();// empty container
		
		if (queue.isEmpty()) {
			result.setStatus(StepResult.Status.FINISHED);
			return result;
		}
		
		index = queue.pop_front();
		List<Integer> adj = this.vertices.get(index).getAdjacency();
		BFSVertex u = (BFSVertex)this.vertices.get(index);
			
		for (Integer vi : adj) {
			BFSVertex v = (BFSVertex)this.getVertices().get(vi);
			if (v.getColor().equals(BFSVertex.Color.BLACK)) {
				v.setColor(BFSVertex.Color.GREEN);
				v.setD(u.getD() + 1);
				v.setParent(index);
				queue.push_back(vi);// push current adjacency index
			}
		}// for
		
		u.setColor(BFSVertex.Color.BLUE);
		
		this.display();
		
		BFSGraph snapshot = new BFSGraph(this);

		result.setGraph(snapshot);
		result.setStatus(StepResult.Status.STEP);
		
		return result;
		
	}// searchStep
	
	public void display() {// used for debugging only
		for (Vertex v : vertices) {
			System.out.println(((BFSVertex)v).getName() + " " + ((BFSVertex)v).getColor() 
			+ " " + ((BFSVertex)v).getD());
		}
		System.out.println();
	}
	
}
