package assignment;

import java.io.*;


public class read_file {
	private static String filename = "src/assignment/a4.txt";
	public static int Num_of_Vertices;
	public static int Num_of_Edges;
	public static String[][] edges;
	public static String[] vertices;
	
	public static void readMetro(){
		File file = new File(filename);
		int counter = 0;
		try {
			//read file
			FileReader fr = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fr);
			String line = bufferedReader.readLine();
			//split first line [num_of_vertices, num_of_edges]
			Num_of_Edges = Integer.parseInt(line.split("\\s+")[1]);
			Num_of_Vertices = Integer.parseInt(line.split("\\s+")[0]);
			//initialize 
			vertices = new String[Num_of_Vertices];
			edges = new String[Num_of_Edges][];
			//read vertices list
			line = bufferedReader.readLine();
			while (line.compareTo("$") != 0) {
				vertices[counter++] = line.substring(5, line.length());
				line = bufferedReader.readLine();
			}
			//skip $
			line = bufferedReader.readLine();
			counter = 0;
			while (line != null) {
				edges[counter++] = line.split("\\s+");
				line = bufferedReader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}