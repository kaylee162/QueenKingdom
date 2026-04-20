# Project 3

## Overview

Project 3 is a Java graph application that models two city systems:

- **Road Network** using intersections and roads  
- **Electrical Grid** using buildings and wire connections  

The project combines core CS 1332 data structures with practical use cases such as routing, connectivity, shortest paths, and network optimization.

---

## Core Data Structures

### Graph Representation (`Princess`)

I implemented the graph as an **adjacency list**, backed by:

- `Set<Vertex<T>>` for vertices
- `Set<Edge<T>>` for edges
- `Map<Vertex<T>, List<VertexDistance<T>>>` for neighbors

### Why adjacency list over matrix?

An adjacency list is better for sparse graphs because it has:

- Faster neighbor iteration
- Lower memory usage than `O(V²)` matrices
- Better fit for BFS, DFS, Dijkstra, Prim, and Kruskal

This project mostly performs traversal and neighbor lookups, so adjacency lists were the right design choice

---

### Disjoint Set (`DisjointSet`)

Used to track connected components of the road graph.

Implemented with:

- **Path Compression** in `find()`
- **Union by Rank** in `union()`

### Why use it?

Instead of recomputing connected components after every road insertion, union-find lets neighborhood merges happen efficiently.

This is especially useful for:

- `addRoad(...)`
- `getNeighborhoodCount()`

---

## Algorithms Implemented in GraphAlgorithms

### BFS

Used when the goal is the **fewest roads traveled**, not lowest weight.

Used in:

- `getMinBetween(from, to)`

Runtime: **O(V + E)**

---

### DFS

Standard depth-first traversal used for graph exploration.

Runtime: **O(V + E)**

---

### Dijkstra's (D's)

Used for shortest weighted paths where edge weights matter.

Used in:

- `getBestPowerSite(...)`

Runtime: **O((V + E) log V)**

---

### Prim / Kruskal

Minimum spanning tree algorithms used for grid optimization.

Used in:

- `consolidateGrid()`

Goal: keep all buildings connected while minimizing wire length.

---

## Application Layer (`QueenKingdom`)

This is the main logic class. It manages **two separate graphs**:

### Road Graph

Stores:

- `Intersection`
- roads with travel duration weights

Supports:

- adding roads
- counting neighborhoods
- grouping connected areas
- routing between buildings

### Grid Graph

Stores:

- `Building`
- wire connections with length weights

Supports:

- adding wires
- selecting best power source
- removing unnecessary wiring

---

## Important Methods

### `addRoad(a, b, duration)`

Adds a road edge and updates the disjoint set.

Returns `true` only when two **existing separate neighborhoods** become connected.

---

### `getNeighborhoodCount()`

Returns how many connected road components currently exist.

Fast because roots are tracked by union-find.

---

### `getMinBetween(from, to)`

Maps buildings to their closest intersections, then runs BFS.

Returns minimum number of roads traveled.

### Why use a BFS?

Because we care about **fewest edges**, not travel time.

---

### `calculateRoute(from, to, avoid)`

Finds a route while trying to avoid certain intersection types.

Priority:

1. Fewer avoided intersections  
2. Lower total duration

This uses lexicographic priority rather than plain shortest path.
This function mimics real routing behavior where avoiding tolls/highways may matter more than pure speed.

---

### `getBestPowerSite(candidates)`

Chooses the most central building.

For each candidate:

- Run D's
- Sum distances to all buildings
- Compute average distance

Lowest average wins 
A central power site minimizes total transmission distance.

---

### `consolidateGrid()`

Uses MST logic to remove redundant wire while keeping all buildings connected.

Returns fraction of removable wire.

### Why use an MST?

Minimum spanning trees preserve connectivity using the smallest total edge cost.

---

## File Breakdown

### `GraphAlgorithms.java`

Contains reusable implementations of:

- BFS
- DFS
- Dijkstra
- Prim
- Kruskal

### `Princess.java`

Concrete graph implementation.

Handles:

- add/remove vertex
- add/remove edge
- adjacency updates

### `DisjointSet.java`

Union-find structure for components.

### `QueenKingdom.java`

Main WaddleWorks simulation logic.

### `Building.java`

Stores:

- building name
- closest intersection

### `Intersection.java`

Stores:

- id
- intersection type

Examples:

- NORMAL
- TOLL
- BRIDGE
- TUNNEL
- ROUNDABOUT
- HIGHWAY

---

## Design Decisions

### Why two separate graphs?

Road travel and electrical wiring solve different problems.
Keeping them separate made algorithms cleaner and avoided mixing unrelated data.

### Why generic graph classes?

Reusable graph infrastructure allows algorithms to work for both systems.

### Why store buildings with nearest intersections?

Buildings are endpoints, but roads exist between intersections.  
This bridges the road layer with the application layer.

---

## Testing

Student tests included:

- Graph operations
- Disjoint set correctness
- Algorithms
- WaddleWorks functionality

I also used repeated submissions and edge-case fixes to validate spec behavior.

---

## Summary

This project demonstrates:

- Graph design
- Traversal algorithms
- Shortest path algorithms
- Minimum spanning trees
- Union-find optimization
- Applying data structures to realistic systems

It turns classic CS 1332 graph theory into a city-planning simulation with practical routing and infrastructure problems.

--- 

## Author
Kaylee Henry  
Georgia Tech
