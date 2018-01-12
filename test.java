package assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;

import net.datastructures.AdjacencyMapGraph;
import net.datastructures.ArrayQueue;
import net.datastructures.Edge;
import net.datastructures.Entry;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Map;
import net.datastructures.PositionalList;
import net.datastructures.Vertex;

public class test {
	static ParisMetro pm = new ParisMetro();
	static Graph<String, Integer> g = pm.get_Graph();
	static Vertex<String> v = pm.getVertex("10");
	static Vertex<String> end_v = pm.getVertex("238");
	
	
	/**/
	public static Graph<String, Integer> reduced_graph(String s){
		Vertex<String> v = pm.getVertex(s);
		Map<Vertex<String>, Integer> map = GraphAlgorithms.shortestPathLengths(g, v);
		Map<Vertex<String>, Edge<Integer>> reduced_map=  GraphAlgorithms.spTree(g, v, map);
		ArrayList<String[]> arrayList = new ArrayList<>();
		Iterator<Entry<Vertex<String>, Edge<Integer>>> reduced_entrySet = reduced_map.entrySet().iterator(); 
		Entry<Vertex<String>, Edge<Integer>> temp = reduced_entrySet.next();
		
		while (reduced_entrySet.hasNext()) {
			String[] edge = new String[3];
			edge[0] = g.endVertices(temp.getValue())[0].getElement();
			edge[1] = g.endVertices(temp.getValue())[1].getElement();
			edge[2] = temp.getValue().getElement().toString();
			arrayList.add(edge);
			temp = reduced_entrySet.next();
		}
		
		Graph<String, Integer> reduced_graph = new AdjacencyMapGraph<>(true);
		TreeSet<String> lables = new TreeSet<>();
		for (String[] edge1 : arrayList) {
			lables.add(edge1[0]);
			lables.add(edge1[1]);
		}
		HashMap<String, Vertex<String>> verts = new HashMap<>();
		for (String label : lables)
			verts.put(label, reduced_graph.insertVertex(label));

		// now add edges to the graph
		for (String[] edge1 : arrayList) {
			Integer cost = (edge1.length == 2 ? 1 : Integer.parseInt(edge1[2]));
			reduced_graph.insertEdge(verts.get(edge1[0]), verts.get(edge1[1]), cost);
		}
		return reduced_graph;
	}
	
	
	public static void main(String args[]){
		
		
		/*
		Iterator<Entry<Vertex<String>, Edge<Integer>>> it_e = reduced_map.entrySet().iterator();
		Entry<Vertex<String>, Edge<Integer>> entry = it_e.next();
		
		
		
		
		while (it_e.hasNext()) {
			System.out.println(entry.getKey().getElement());
			System.out.println(g.endVertices(entry.getValue())[0].getElement() + "  " + 
			g.endVertices(entry.getValue())[1].getElement() + "\n\n");
			entry = it_e.next();
		}
		*/
		Graph<String, Integer> reduce = reduced_graph(v.getElement());
		Map<Vertex<String>, Edge<Integer>> forest = GraphAlgorithms.DFSComplete(reduce);
		PositionalList<Edge<Integer>> path = GraphAlgorithms.constructPath(reduce, v, end_v, forest);
		Iterator<Entry<Vertex<String>, Edge<Integer>>> it = forest.entrySet().iterator();
		Entry<Vertex<String>, Edge<Integer>> entry = it.next();
		System.out.println(reduce);
		}
}
