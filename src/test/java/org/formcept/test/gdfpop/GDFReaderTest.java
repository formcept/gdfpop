package org.formcept.test.gdfpop;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.formcept.gdfpop.GDFReader;
import org.junit.Test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

/**
 * Test cases for {@link GDFReader}
 * 
 * @author Anuj (https://github.com/anujsrc)
 */
public class GDFReaderTest {

	@Test
	public void testEmptyGDFFile() {
		Graph graph = new TinkerGraph();
		GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example-1.gdf"));
		assertEquals("No vertices found", false, graph.getVertices().iterator().hasNext());
		assertEquals("No edges found", false, graph.getEdges().iterator().hasNext());
		graph.shutdown();
	}
	
	@Test
	public void testEmptyGDF() {
		Graph graph = new TinkerGraph();
		GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example-2.gdf"));
		assertEquals("No vertices found", false, graph.getVertices().iterator().hasNext());
		assertEquals("No edges found", false, graph.getEdges().iterator().hasNext());
		graph.shutdown();
	}
	
	@Test
	public void testSingleNodeGDF() {
		Graph graph = new TinkerGraph();
		GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example-3.gdf"));
		assertEquals("One vertex found", true, graph.getVertices().iterator().hasNext());
		assertEquals("Vertex Name is 'a'", "a", graph.getVertices().iterator().next().getId());
		assertEquals("No edges found", false, graph.getEdges().iterator().hasNext());
		graph.shutdown();
	}
	
	@Test
	public void testInvalidEdgeGDF() {
		Graph graph = new TinkerGraph();
		GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example-4.gdf"));
		assertEquals("One vertex found", true, graph.getVertices().iterator().hasNext());
		assertEquals("Vertex Name is 'a'", "a", graph.getVertices().iterator().next().getId());
		assertEquals("No edges found", false, graph.getEdges().iterator().hasNext());
		graph.shutdown();
	}
	
	@Test
	public void testNodeEdgeGDF() {
		Graph graph = new TinkerGraph();
		GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example-5.gdf"));
		assertTrue("First vertex found", graph.getVertex("a") != null);
		assertEquals("Vertex Name is 'a'", "a", graph.getVertex("a").getId());
		assertTrue("Second vertex found", graph.getVertex("b") != null);
		assertEquals("Vertex Name is 'b'", "b", graph.getVertex("b").getId());
		Iterator<Edge> eitr = graph.getEdges().iterator();
		assertEquals("One edge found", true, eitr.hasNext());
		Edge fed = eitr.next();
		assertEquals("Source is vertex 'a'", "a", fed.getVertex(Direction.OUT).getId());
		assertEquals("Target is vertex 'b'", "b", fed.getVertex(Direction.IN).getId());
		assertEquals("Default label is _default", "_default", fed.getLabel());
		graph.shutdown();
	}
	
	@Test
	public void testNodeEdgeWithPropGDF() {
		Graph graph = new TinkerGraph();
		GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example-6.gdf"));
		Vertex a = graph.getVertex("a");
		assertTrue("First vertex found", a != null);
		assertEquals("Vertex Name is 'a'", "a", a.getId());
		assertEquals("Vertex 'a' Weight is 1", 1, a.getProperty("weight"));
		Vertex b = graph.getVertex("b");
		assertTrue("Second vertex found", b != null);
		assertEquals("Vertex Name is 'b'", "b", b.getId());
		assertEquals("Vertex 'b' Weight is 2", 2, b.getProperty("weight"));
		Iterator<Edge> eitr = graph.getEdges().iterator();
		assertEquals("One edge found", true, eitr.hasNext());
		Edge fed = eitr.next();
		assertEquals("Source is vertex 'a'", "a", fed.getVertex(Direction.OUT).getId());
		assertEquals("Target is vertex 'b'", "b", fed.getVertex(Direction.IN).getId());
		assertEquals("Edge label is a->b", "a->b", fed.getLabel());
		assertEquals("Edge (a,b) weight is 0.01", 0.01f, fed.getProperty("weight"));
		graph.shutdown();
	}
	
	@Test
	public void testComplexPropWithSingleQuotesGDF() {
		Graph graph = new TinkerGraph();
		GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example-7.gdf"), "'", null);
		
		Vertex a = graph.getVertex("a");
		assertTrue("First vertex found", a != null);
		assertEquals("Vertex Name is 'a'", "a", a.getId());
		assertEquals("Vertex Label is 'Hello \"world\" !'", "Hello \"world\" !", a.getProperty("label"));
		assertEquals("Vertex Class is 1", 1, a.getProperty("class"));
		assertEquals("Vertex visible is true", true, a.getProperty("visible"));
		assertEquals("Vertex color is '114,116,177'", "114,116,177", a.getProperty("color"));
		assertEquals("Vertex width is 10.10", 10.10f, a.getProperty("width"));
		assertEquals("Vertex height is 20.24567", 20.24567d, a.getProperty("height"));
		
		Vertex b = graph.getVertex("b");
		assertTrue("Second vertex found", b != null);
		assertEquals("Vertex Name is 'b'", "b", b.getId());
		assertEquals("Vertex Label is 'Well, this is'", "Well, this is", b.getProperty("label"));
		assertEquals("Vertex Class is 2", 2, b.getProperty("class"));
		assertEquals("Vertex visible is false", false, b.getProperty("visible"));
		assertEquals("Vertex color is '219,116,251'", "219,116,251", b.getProperty("color"));
		assertEquals("Vertex width is 10.98", 10.98f, b.getProperty("width"));
		assertEquals("Vertex height is 10.986123", 10.986123d, b.getProperty("height"));
		
		Vertex c = graph.getVertex("c");
		assertTrue("Third vertex found", c != null);
		assertEquals("Vertex Name is 'c'", "c", c.getId());
		assertEquals("Vertex Label is 'A correct 'GDF' file'", "A correct 'GDF' file", c.getProperty("label"));
		assertEquals("Vertex Class is null", null, c.getProperty("class"));
		assertEquals("Vertex visible is false", false, c.getProperty("visible"));
		assertEquals("Vertex color is null", null, c.getProperty("color"));
		assertEquals("Vertex width is null", null, c.getProperty("width"));
		assertEquals("Vertex height is null", null, c.getProperty("height"));
		
		Iterator<Edge> eitr = graph.getEdges().iterator();
		List<Edge> elst = new ArrayList<Edge>();
		while(eitr.hasNext()){
			elst.add(eitr.next());
		}
		assertEquals("Three edges found", 3, elst.size());
		for(Edge ed : elst){
			// get source
			String src = (String) ed.getVertex(Direction.OUT).getId();
			if(src.equals("a")){
				assertEquals("Source is vertex 'a'", "a", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex 'b'", "b", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is _default", "_default", ed.getLabel());
				assertEquals("Edge (a,b) directed is true", true, ed.getProperty("directed"));
				assertEquals("Edge (a,b) color is 114,116,177", "114,116,177", ed.getProperty("color"));
				assertEquals("Edge (a,b) weight is 100", 100L, ed.getProperty("weight"));
			} else if(src.equals("b")){
				assertEquals("Source is vertex 'b'", "b", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex 'c'", "c", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is _default", "_default", ed.getLabel());
				assertEquals("Edge (b,c) directed is false", false, ed.getProperty("directed"));
				assertEquals("Edge (b,c) color is 219,116,251", "219,116,251", ed.getProperty("color"));
				assertEquals("Edge (b,c) weight is 300", 300L, ed.getProperty("weight"));
			} else if(src.equals("c")){
				assertEquals("Source is vertex 'c'", "c", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex 'a'", "a", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is _default", "_default", ed.getLabel());
				assertEquals("Edge (c,a) directed is null", null, ed.getProperty("directed"));
				assertEquals("Edge (c,a) color is null", null, ed.getProperty("color"));
				assertEquals("Edge (c,a) weight is 100", 100L, ed.getProperty("weight"));
			} else {
				fail("No such edge for source: " + src);
			}
		}
		
		graph.shutdown();
		
	}
	
	@Test
	public void testComplexPropWithDoubleQuotesGDF() {
		Graph graph = new TinkerGraph();
		GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example-8.gdf"), "\"", null);
		
		Vertex a = graph.getVertex("a");
		assertTrue("First vertex found", a != null);
		assertEquals("Vertex Name is 'a'", "a", a.getId());
		assertEquals("Vertex Label is 'Hello 'world' !'", "Hello 'world' !", a.getProperty("label"));
		assertEquals("Vertex Class is 1", 1, a.getProperty("class"));
		assertEquals("Vertex visible is true", true, a.getProperty("visible"));
		assertEquals("Vertex color is '114,116,177'", "114,116,177", a.getProperty("color"));
		assertEquals("Vertex width is 10.10", 10.10f, a.getProperty("width"));
		assertEquals("Vertex height is 20.24567", 20.24567d, a.getProperty("height"));
		
		Vertex b = graph.getVertex("b");
		assertTrue("Second vertex found", b != null);
		assertEquals("Vertex Name is 'b'", "b", b.getId());
		assertEquals("Vertex Label is 'Well, this is'", "Well, this is", b.getProperty("label"));
		assertEquals("Vertex Class is 2", 2, b.getProperty("class"));
		assertEquals("Vertex visible is false", false, b.getProperty("visible"));
		assertEquals("Vertex color is '219,116,251'", "219,116,251", b.getProperty("color"));
		assertEquals("Vertex width is 10.98", 10.98f, b.getProperty("width"));
		assertEquals("Vertex height is 10.986123", 10.986123d, b.getProperty("height"));
		
		Vertex c = graph.getVertex("c");
		assertTrue("Third vertex found", c != null);
		assertEquals("Vertex Name is 'c'", "c", c.getId());
		assertEquals("Vertex Label is 'A correct 'GDF' file'", "A correct 'GDF' file", c.getProperty("label"));
		assertEquals("Vertex Class is null", null, c.getProperty("class"));
		assertEquals("Vertex visible is false", false, c.getProperty("visible"));
		assertEquals("Vertex color is null", null, c.getProperty("color"));
		assertEquals("Vertex width is null", null, c.getProperty("width"));
		assertEquals("Vertex height is null", null, c.getProperty("height"));
		
		Iterator<Edge> eitr = graph.getEdges().iterator();
		List<Edge> elst = new ArrayList<Edge>();
		while(eitr.hasNext()){
			elst.add(eitr.next());
		}
		assertEquals("Three edges found", 3, elst.size());
		for(Edge ed : elst){
			// get source
			String src = (String) ed.getVertex(Direction.OUT).getId();
			if(src.equals("a")){
				assertEquals("Source is vertex 'a'", "a", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex 'b'", "b", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is _default", "_default", ed.getLabel());
				assertEquals("Edge (a,b) directed is true", true, ed.getProperty("directed"));
				assertEquals("Edge (a,b) color is 114,116,177", "114,116,177", ed.getProperty("color"));
				assertEquals("Edge (a,b) weight is 100", 100L, ed.getProperty("weight"));
			} else if(src.equals("b")){
				assertEquals("Source is vertex 'b'", "b", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex 'c'", "c", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is _default", "_default", ed.getLabel());
				assertEquals("Edge (b,c) directed is false", false, ed.getProperty("directed"));
				assertEquals("Edge (b,c) color is 219,116,251", "219,116,251", ed.getProperty("color"));
				assertEquals("Edge (b,c) weight is 300", 300L, ed.getProperty("weight"));
			} else if(src.equals("c")){
				assertEquals("Source is vertex 'c'", "c", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex 'a'", "a", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is _default", "_default", ed.getLabel());
				assertEquals("Edge (c,a) directed is null", null, ed.getProperty("directed"));
				assertEquals("Edge (c,a) color is null", null, ed.getProperty("color"));
				assertEquals("Edge (c,a) weight is 100", 100L, ed.getProperty("weight"));
			} else {
				fail("No such edge for source: " + src);
			}
		}
		
		graph.shutdown();
		
	}
	
	@Test
	public void testVertexNameAndEdgeIDGDF() {
		Graph graph = new TinkerGraph();
		GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example-9.gdf"), "\"", "name");
		
		Vertex one = graph.getVertex("1");
		assertTrue("Vertex 1 found", one != null);
		assertEquals("Vertex Name is 1'", "1", one.getId());
		assertEquals("Vertex also has a name property 1", "1", one.getProperty("name"));
		assertEquals("Vertex Label is marko", "marko", one.getProperty("label"));
		assertEquals("Vertex Age is 29", 29, one.getProperty("age"));
		assertEquals("Vertex Lang is null", null, one.getProperty("lang"));
		
		Vertex two = graph.getVertex("2");
		assertTrue("Vertex 2 found", two != null);
		assertEquals("Vertex Name is 2'", "2", two.getId());
		assertEquals("Vertex also has a name property 2", "2", two.getProperty("name"));
		assertEquals("Vertex Label is vadas", "vadas", two.getProperty("label"));
		assertEquals("Vertex Age is 27", 27, two.getProperty("age"));
		assertEquals("Vertex Lang is null", null, two.getProperty("lang"));
		
		Vertex three = graph.getVertex("3");
		assertTrue("Vertex 3 found", three != null);
		assertEquals("Vertex Name is 3'", "3", three.getId());
		assertEquals("Vertex also has a name property 3", "3", three.getProperty("name"));
		assertEquals("Vertex Label is lop", "lop", three.getProperty("label"));
		assertEquals("Vertex Age is null", null, three.getProperty("age"));
		assertEquals("Vertex Lang is java", "java", three.getProperty("lang"));
		
		Vertex four = graph.getVertex("4");
		assertTrue("Vertex 4 found", four != null);
		assertEquals("Vertex Name is 4'", "4", four.getId());
		assertEquals("Vertex also has a name property 4", "4", four.getProperty("name"));
		assertEquals("Vertex Label is josh", "josh", four.getProperty("label"));
		assertEquals("Vertex Age is 32", 32, four.getProperty("age"));
		assertEquals("Vertex Lang is null", null, four.getProperty("lang"));
		
		Vertex five = graph.getVertex("5");
		assertTrue("Vertex 5 found", five != null);
		assertEquals("Vertex Name is 5'", "5", five.getId());
		assertEquals("Vertex also has a name property 5", "5", five.getProperty("name"));
		assertEquals("Vertex Label is ripple", "ripple", five.getProperty("label"));
		assertEquals("Vertex Age is null", null, five.getProperty("age"));
		assertEquals("Vertex Lang is java", "java", five.getProperty("lang"));
		
		Vertex six = graph.getVertex("6");
		assertTrue("Vertex 6 found", six != null);
		assertEquals("Vertex Name is 6'", "6", six.getId());
		assertEquals("Vertex also has a name property 6", "6", six.getProperty("name"));
		assertEquals("Vertex Label is peter", "peter", six.getProperty("label"));
		assertEquals("Vertex Age is 35", 35, six.getProperty("age"));
		assertEquals("Vertex Lang is null", null, six.getProperty("lang"));
		
		Iterator<Edge> eitr = graph.getEdges().iterator();
		List<Edge> elst = new ArrayList<Edge>();
		while(eitr.hasNext()){
			elst.add(eitr.next());
		}
		assertEquals("Six edges found", 6, elst.size());
		for(Edge ed : elst){
			if(ed.getId().equals("7")){
				assertEquals("Source is vertex '1'", "1", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex '2'", "2", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is knows", "knows", ed.getLabel());
				assertEquals("Edge (1,2) name is 7", "7", ed.getProperty("name"));
				assertEquals("Edge (1,2) weight is 0.5", 0.5f, ed.getProperty("weight"));
			} else if(ed.getId().equals("8")){
				assertEquals("Source is vertex '1'", "1", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex '4'", "4", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is knows", "knows", ed.getLabel());
				assertEquals("Edge (1,4) name is 8", "8", ed.getProperty("name"));
				assertEquals("Edge (1,4) weight is 1.0", 1.0f, ed.getProperty("weight"));
			} else if(ed.getId().equals("9")){
				assertEquals("Source is vertex '1'", "1", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex '3'", "3", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is created", "created", ed.getLabel());
				assertEquals("Edge (1,3) name is 9", "9", ed.getProperty("name"));
				assertEquals("Edge (1,3) weight is 0.4", 0.4f, ed.getProperty("weight"));
			} else if(ed.getId().equals("10")){
				assertEquals("Source is vertex '4'", "4", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex '5'", "5", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is created", "created", ed.getLabel());
				assertEquals("Edge (4,5) name is 10", "10", ed.getProperty("name"));
				assertEquals("Edge (4,5) weight is 1.0", 1.0f, ed.getProperty("weight"));
			} else if(ed.getId().equals("11")){
				assertEquals("Source is vertex '4'", "4", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex '3'", "3", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is created", "created", ed.getLabel());
				assertEquals("Edge (4,3) name is 11", "11", ed.getProperty("name"));
				assertEquals("Edge (4,3) weight is 0.4", 0.4f, ed.getProperty("weight"));
			} else if(ed.getId().equals("12")){
				assertEquals("Source is vertex '6'", "6", ed.getVertex(Direction.OUT).getId());
				assertEquals("Target is vertex '3'", "3", ed.getVertex(Direction.IN).getId());
				assertEquals("Edge label is created", "created", ed.getLabel());
				assertEquals("Edge (6,3) name is 12", "12", ed.getProperty("name"));
				assertEquals("Edge (6,3) weight is 0.2", 0.2f, ed.getProperty("weight"));
			} else {
				fail("No such edge: " + ed.getId());
			}
		}
		
		graph.shutdown();
		
	}

}
