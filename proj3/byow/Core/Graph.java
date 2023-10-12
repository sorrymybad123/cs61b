package byow.Core;

import java.util.*;

public class Graph {


    public static TreeSet<edge> edges = new TreeSet<>();

    public static class disjointSet<thing>{
        private Map<thing, thing> parent;

        public disjointSet() {
            parent = new HashMap<>();
        }

        // add a element to new set
        public void makeSet(thing element) {
           if (!parent.containsKey(element)) {
               parent.put(element, element);
           }
        }

        // find the parent of the element
        public thing find(thing element) {
            if (element.equals(parent.get(element))) {
               return element;
            }

            // use path compression to optimize lookup
            parent.put(element, find(parent.get(element)));
            return parent.get(element);
        }

        // union two set
        public void union(thing element1, thing element2) {
            thing root1 = find(element1);
            thing root2 = find(element2);

            if (root1.equals(root2)) {
                parent.put(root1, root2);
            }
        }

        // determine whether two elements is in same union
        public boolean isConnected(thing element1, thing element2) {
            return find(element1).equals(find(element2));
        }

    }

    public Graph() {

    }

    /**
     * add edge to the graph
     */
    public static void addEdge(edge e) {
        edges.add(e);
    }

    /**
     * use the Kruskal to generate MST
     */
    public static TreeSet<edge> KruskalToGenerateMst(){
        // create a list to store
        TreeSet<edge> remainingEdges = new TreeSet<>();
        // create a disjoint set
        disjointSet<Position> disjointSet = new disjointSet();
        // sort the edges

        int roomsRemaining = 0;
        // collect the door
        for (Room room : Room.rooms) {
            disjointSet.makeSet(room.door);
            roomsRemaining++;
        }

        List<Position> addedP = new ArrayList<>();
        while (!edges.isEmpty() && roomsRemaining > 0) {
            // remove the first edge
            edge e = edges.first();
            edges.remove(e);

            if (!disjointSet.isConnected(e.p1, e.p)) {
                disjointSet.union(e.p1, e.p);
                // connect two room
                if (!remainingEdges.contains(e)) {
                    remainingEdges.add(e);
                }

                // find this two positions if it is connected
                if (!addedP.contains(e.p1)) {
                    roomsRemaining -= 1;
                    addedP.add(e.p1);
                }

                if (!addedP.contains(e.p)) {
                    roomsRemaining -= 1;
                    addedP.add(e.p);
                }

            }

            if (roomsRemaining == 0) {
                break;
            }

        }
        return remainingEdges;
    }
}
