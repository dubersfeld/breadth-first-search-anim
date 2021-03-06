package com.dub.spring.breadthFirstSearch;

import java.util.ArrayList;
import java.util.List;

/** container object for AJAX response 
 * contains snapshots of the graph created by the BFS loop 
 **/
public class BFSResponse {
	private Status status;
	private List<JSONSnapshot> snapshots;
	

	public BFSResponse() {
		status = null;
		snapshots = new ArrayList<>();
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<JSONSnapshot> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(List<JSONSnapshot> snapshots) {
		this.snapshots = snapshots;
	}





	public static enum Status {
		OK, ERROR
	}
}
