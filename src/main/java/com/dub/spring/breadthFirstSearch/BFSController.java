package com.dub.spring.breadthFirstSearch;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class BFSController {
	
	@Autowired
	private GraphServices graphServices;
	
	/** Initialize graph for both automatic and stepwise search */
	@RequestMapping(value="/initGraph")
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
		
		BFSGraph graph = graphServices.jsonToBFS(jsonEdges, jsonVertices);
			
		session.setAttribute("graph", graph);
			
		BFSResponse bfsResponse = new BFSResponse();
		bfsResponse.setStatus(BFSResponse.Status.OK);
	
		System.out.println("graph constructed");
		
		System.out.println("sourcename: "+ sourcename);
	
		// initialize queue for search loop
		graph.searchInit(sourcename);
	
		// here the graph is ready for the search loop
		
		graph.display();
	
		System.out.println("initGraph completed: " + graph.isInit());
			
		return bfsResponse;
	}

	
	@RequestMapping(value="/searchGraph")
	@ResponseBody
	public BFSResponse searchGraph(@RequestBody SearchRequest message, 
											HttpServletRequest request) 
	{	
		BFSResponse bfsResponse = new BFSResponse();
		
		HttpSession session = request.getSession();
		BFSGraph graph = (BFSGraph)session.getAttribute("graph");
					
		// snapshots for display
		List<JSONSnapshot> snapshots = new ArrayList<>();
				
		while (!graph.isFinished()) {
			graph.searchStep();
			JSONSnapshot snapshot = graphServices.graphToJSON(graph);
			//snapshot.displayAdj();
			snapshots.add(snapshot);
			
		}// while
				
		bfsResponse.setSnapshots(snapshots);
		bfsResponse.setStatus(BFSResponse.Status.OK);
		
		return bfsResponse;
	}// searchGraph
	
}
