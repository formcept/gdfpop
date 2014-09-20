gdfpop
======

GDF file reader that can write data from a GDF stream to TinkerPop 2.x graph

#### Usage
GDFpop provides [GDFReader](https://github.com/anujsrc/gdfpop/blob/dev/src/main/java/org/formcept/gdfpop/GDFReader.java) that takes care of parsing the GDF file stream and populating TinkerPop 2.x Graph that is provided as an input. For more details, please take a look at the [GDFReaderTest](https://github.com/anujsrc/gdfpop/blob/dev/src/test/java/org/formcept/test/gdfpop/GDFReaderTest.java). For example-

```
// initialize TinkerPop Graph
Graph graph = new TinkerGraph();
// read in the GDF stream
GDFReader.inputGraph(graph, GDFReader.class.getResourceAsStream("graph-example.gdf"), "'");
// use blueprints as usual
Vertex a = graph.getVertex("a");
```

There are two optional parameters-

1. **buf**: Takes in a buffer size for [BatchGraph](https://github.com/tinkerpop/blueprints/blob/master/blueprints-core/src/main/java/com/tinkerpop/blueprints/util/wrappers/batch/BatchGraph.java). See [BatchGraph](https://github.com/tinkerpop/blueprints/blob/master/blueprints-core/src/main/java/com/tinkerpop/blueprints/util/wrappers/batch/BatchGraph.java) for more details.
2. **quote**: You can specify the quote character that is being used for the values. Default is double quotes.
