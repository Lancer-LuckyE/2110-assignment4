package assignment;

import java.util.ArrayList;
/*Reference
 *Assignment inspired by :
 *    Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *    package net.datastructures.*
 *    Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014package assignment;
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;
import net.datastructures.*;

public class ParisMetro {

	private String[][] edges;
	private Graph<String, Integer> g;
	private String[] station_name;
/**
 * construct the graph by referring the code of GraphExamples.java
 */
	public ParisMetro() {
		read_file.readMetro();
		edges = read_file.edges;
		station_name = read_file.vertices;
		// code from net.datastructure.GraphExamples
		g = new AdjacencyMapGraph<>(true);

		TreeSet<String> lables = new TreeSet<>();
		for (String[] edge : edges) {
			lables.add(edge[0]);
			lables.add(edge[1]);
		}
		HashMap<String, Vertex<String>> verts = new HashMap<>();
		for (String label : lables)
			verts.put(label, g.insertVertex(label));

		// now add edges to the graph
		for (String[] edge : edges) {
			Integer cost = (edge.length == 2 ? 1 : Integer.parseInt(edge[2]));
			g.insertEdge(verts.get(edge[0]), verts.get(edge[1]), cost);
		}
	}
	
	/**
	 * find and return the vertex with given element
	 * @param vert string contains in the vertex
	 * @return the vertex if it is found, otherwise return null
	 */
	private Vertex<String> getVertex(String vert) {
		Iterator<Vertex<String>> it_v = g.vertices().iterator();
		Vertex<String> vertex = it_v.next();
		while (it_v.hasNext()) {
			if (vertex.getElement().compareTo(vert) == 0) {		
				return vertex;
			}
			vertex = it_v.next();
		}
		return null;
	}


	/**
	 * 
	 * @return the constructed graph
	 */
	public Graph<String, Integer> get_Graph() {
		return g;
	}

	/**
	 * print every station in the line by giving a known station
	 * @param v the given station to search the line it belongs to
	 */
	public void print_line(String v){
		Stack<Vertex<String>> Line = get_Line(v);
		while (! Line.isEmpty()) {
			System.out.println(station_name[Integer.parseInt(Line.pop().getElement())]);
		}
	}
	
	/**
	 * 
	 * @param v the given station to search the line it belongs to 
	 * @return a stack of all station of this line
	 */
	public Stack<Vertex<String>> get_Line(String src){
		Vertex<String> v = getVertex(src);
		Stack<Vertex<String>> Line = new Stack<>();
		get_Line_after(Line, v);
		get_Line_before(Line, v);
		return Line;
	}
	
	
	private void get_Line_before(Stack<Vertex<String>> st, Vertex<String> vertex ) {
		Iterable<Edge<Integer>> edges = g.outgoingEdges(vertex);// get out going edges
		int count = 0;
		for (Edge<Integer> edge : edges) {// every edge in out going edges
			if (edge.getElement() > 0) {
				count++;
			}
		}
		if (count == 1) { //return
			return;
		}		
		Iterator<Edge<Integer>> it = g.edges().iterator();
		Edge<Integer> temp = it.next();
		while (it.hasNext()) {
			if (g.endVertices(temp)[0].getElement().compareTo(vertex.getElement()) == 0) {
				temp = it.next();
				break;
			}
			temp = it.next();
		}
		st.push(g.endVertices(temp)[1]);
		get_Line_before(st, g.endVertices(temp)[1]);
	}
	
	private void get_Line_after(Stack<Vertex<String>> st, Vertex<String> v) {
		Iterable<Edge<Integer>> edges = g.outgoingEdges(v);
		int count = 0;
		for (Edge<Integer> edge : edges) {
			if (edge.getElement() > 0) {
				count++;
			}
		}
		if (count == 1) {
			st.push(v);
			return;
		}
		Iterator<Edge<Integer>> it = g.edges().iterator();
		Edge<Integer> temp = it.next();
		while (it.hasNext()) {
			if (g.endVertices(temp)[0].getElement().compareTo(v.getElement()) == 0) {
				break;
			}
			temp = it.next();
		}
		get_Line_after(st, g.endVertices(temp)[1]);
		st.push(g.endVertices(temp)[0]);
	}

	
	/**
	 * Construct the subgraph of reaching other reachable vertex v from s by the shortest path
	 * @param s find the shortest path from given name s of a vertex in the graph
	 * @return a new graph that only contains shortest path from s to any other vertex
	 */
	public Graph<String, Integer> reduced_graph(Graph<String, Integer> g, String s){
		Vertex<String> v = getVertex(s);
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
	
	
	/**
	 * find the construction line and remove it from the graph by given a known station in construction
	 * @param vert the sation that is in the construction line
	 * @return copy of the graph that the construction line is removed from the original graph
	 */
	private Graph<String, Integer> remove_construction_Line(String vert) {
		Graph<String, Integer> copy = g;
		Stack<Vertex<String>> for_remove = get_Line(vert);
		while (! for_remove.isEmpty()) {
			copy.removeVertex(for_remove.pop());
		}
		return copy;
	}
	
	/**
	 * Construct the subgraph of reaching other reachable vertex v from s by the shortest path 
	 * in the graph that has been removed a construction line
	 * @param s the known source vertex
	 * @param c the station name in construction
	 * @return the sub-graph 
	 */
	public Graph<String, Integer> reduced_graph_after_construstion(String s, String c){
		Graph<String, Integer> removed = remove_construction_Line(c);
		return reduced_graph(removed, s);
	}
	
	
	public void print_sp(Graph<String, Integer> g, String src, String end){
		Stack<Vertex<String>> Line = get_sp(g, src, end);
		while (! Line.isEmpty()) {
			System.out.println(station_name[Integer.parseInt(Line.pop().getElement())]);
		}
	}
	
	private Stack<Vertex<String>> get_sp(Graph<String, Integer> g, String src, String end) {
		Graph<String, Integer> reduced = reduced_graph(g, src);//construct the reduced graph at src
		Stack<Vertex<String>> path1 = new Stack<>();
		Stack<Vertex<String>> path2 = new Stack<>();
		Vertex<String> src_v = getVertex(src);
		Vertex<String> end_v = getVertex(end);
		shortest_path_before(reduced, path1, src_v, end_v);
		shortest_path_after(reduced, path2, src_v, end_v);
		Stack<Vertex<String>> path;
		if (!path1.isEmpty() && !path2.isEmpty()) {
			path = path1;
			if (path1.size() > path2.size()) {
				path = path2;
			}
		}else {
			path = new Stack<>();
		}
		
		return path;
	}

	private void shortest_path_before(Graph<String, Integer> g, Stack<Vertex<String>> path, 
			Vertex<String> src_v, Vertex<String> end_v) {
		if ((src_v.getElement().compareTo(end_v.getElement()) == 0) || 
				(g.outDegree(src_v) == 0)) {
			return;
		}
		Iterator<Edge<Integer>> it = g.edges().iterator();
		Edge<Integer> temp = it.next();
		while (it.hasNext()) {
			if (g.endVertices(temp)[0].getElement().compareTo(src_v.getElement()) == 0) {
				temp = it.next();
				break;
			}
			temp = it.next();
		}
		path.push(g.endVertices(temp)[1]);
		shortest_path_before(g, path, g.endVertices(temp)[1], end_v);
	}
	
	private void shortest_path_after(Graph<String, Integer> g, Stack<Vertex<String>> path, 
			Vertex<String> src_v, Vertex<String> end_v) {
		if ((src_v.getElement().compareTo(end_v.getElement()) == 0) || 
				(g.outDegree(src_v) == 0)) {
			path.push(src_v);
			return;
		}
		Iterator<Edge<Integer>> it = g.edges().iterator();
		Edge<Integer> temp = it.next();
		while (it.hasNext()) {
			if (g.endVertices(temp)[0].getElement().compareTo(src_v.getElement()) == 0) {
				temp = it.next();
				break;
			}
			temp = it.next();
		}
		shortest_path_after(g, path, g.endVertices(temp)[1], end_v);
		path.push(g.endVertices(temp)[0]);
	}

	public static void main(String args[]){
		ParisMetro pm = new ParisMetro();
		pm.print_line("10");
	}
	
	

}