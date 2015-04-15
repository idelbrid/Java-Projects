//Ian Delbridge
//CSC 172
//Pawlicki
//Project4
//5/02/14
//Lab Section: MW6:15
//Lab TAs: Ciaran Downey, Chris Frederickson, Jin

I drew the road map by maintaining the position of each intersection as a value in the Vertex class, so when I just ran through
the edges for the graph, I painted a line for every edge based on the x and y position of its vertices that it connects. Just
when I run through prim's algorithm, I make sure to follow that with a "highlightMST" function that sets a flag in each edge
covered by the MST that tells my panel that is actually drawing it that the edge needs to be green, not blue. Similarly, after
I perform dijkstra's algorithm, I make sure to run a "highlightPath" function that sets a flag in each edge that the path travels
through to be red instead of green or blue.

To generate the minimum spanning tree, I used Prim's algorithm. So since I had already written Dijkstra's algorithm at the time,
I just altered Dijkstra's to change a vertex's distance just to be the weight of the smallest edge that could connect it to the 
tree. That is, I run through the graph, and set every vertex's distance and known to be "infinite" and false accordingly. Then
I make my starting node have path of itself, with distance zero, and put it in a priority queue. Then, until the priority queue
is empty, continue popping the minimum provided that it's a new node, and lowering the distances of the adjacent edges if 
necessary. Once all nodes have been touched, I stop. I also have a line that highlights the MST.

The time complexity of this algorithm is Elog(V) where E is the number of Edges and V is the number of Vertices. That's because 
I have a heap insert (At most log(E) time which is in the end log(V)). Then I have this repeated E/V times for each vertex, so 
multiply log(V)*E/V * V == Elog(V). 

To find the shortest path, I used Dijkstra's algorithm. It's pretty much the same as Prim's in this case, but instead of having 
the distance be the distance to ANY node in my MST, I define it as the distance from my starting node. I would repeat everything 
I said two paragraphs above, except, I have the stopping condition of hitting the target node, and breaking out of the while 
loop early. I also have a line in there that highlights the path in a different color. 

The run-time analysis is exactly the same: Elog(V) where E is the number of Edges and V is the number of Vertices. 

TO RUN: For ease of grading and user utility, the infile is a command line argument to the program. If "infile.txt" is the 
infile, use command line "infile.txt". 

FURTHER, I have made a small background to make the map/graph resemble an actual map more, but I wanted its use optional. So
using it is a command line argument: if you don't want the background, use no second argument. If you want it, use the argument
"picture". 

Finally, NOTE: that if "monroe-county.tab" is changed, i.e. you test it with a different set of values or a different file name,
the program might not work. I have written the program around the file, and I WOULD have to alter some aspects of the program
to get it to work with any old file containing tons of intersections and roads. 

:D



 