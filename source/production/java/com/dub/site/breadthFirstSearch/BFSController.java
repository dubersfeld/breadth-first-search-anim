package com.dub.site.breadthFirstSearch;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dub.config.annotation.WebController;

@WebController
public class BFSController {
	
	/** Initialize graph for both automatic and stepwise search */
	@RequestMapping(value="initGraph")
	@ResponseBody
	public BFSResponse initGraph(@RequestBody GraphInitRequest message, 
				HttpServletRequest request) 
	{	
		List<JSONEdge> jsonEdges = message.getJsonEdges();
		List<JSONVertex> jsonVertices = message.getJsonVertices();
		String sourcename = message.getSourcename();
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("graph") != null) {
			session.removeAttribute("graph");
		}
	
		BFSGraph graph = new BFSGraph();
			
		for (int i = 0; i < jsonVertices.size(); i++) {
			BFSVertex v = new BFSVertex();
			v.setName(jsonVertices.get(i).getName());
			v.setColor(BFSVertex.Color.BLACK);
			graph.getVertices().add(v);
		}
		
		for (int i = 0; i < jsonEdges.size(); i++) {
			int from = jsonEdges.get(i).getFrom();
			int to = jsonEdges.get(i).getTo();
			Vertex v1 = graph.getVertices().get(from);
			//Vertex v2 = graph.getVertices().get(to);
			v1.getAdjacency().add(to);
		}
	
	
		session.setAttribute("graph", graph);
			
		BFSResponse bfsResponse = new BFSResponse();
		bfsResponse.setStatus(BFSResponse.Status.OK);
	
		System.out.println("graph constructed");
		
		System.out.println("sourcename: "+ sourcename);
	
		// initialize queue for search loop
		graph.searchInit(sourcename);
	
		// here the graph is ready for the search loop
	
		System.out.println("initGraph completed: " + graph.isInit());
			
		return bfsResponse;
	}
	
	
	@RequestMapping(value="searchGraph")
	@ResponseBody
	public BFSResponse searchGraph(@RequestBody SearchRequest message, 
											HttpServletRequest request) 
	{	
		BFSResponse bfsResponse = new BFSResponse();
		
		HttpSession session = request.getSession();
		BFSGraph graph = (BFSGraph)session.getAttribute("graph");
				
		System.out.println("searchGraph sator");
		
		// snapshots for display
		List<StepResult> snapshots = new ArrayList<>();
		
		
		graph.search(snapshots);// search loop affects a List of container
		
		System.out.println("after search loop " + snapshots.size());
				
		bfsResponse.setStatus(BFSResponse.Status.OK);
		bfsResponse.setSnapshots(snapshots);
		
		snapshots.get(0).getGraph().display();
		
		return bfsResponse;
	}// searchGraph
	
}
