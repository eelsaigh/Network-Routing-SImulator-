# Network Routing Simulator

A Java-based simulation of network packet routing using Dijkstra's Shortest Path algorithm. Designed to model network topology and automated failover mechanisms in high-availability systems.

## Key Features

* Weighted Graph Engine: Models network nodes (routers) and edges (links) with variable latency/cost.
* Dijkstra's Algorithm Computes optimal routing paths in O(E + V log V) time.
* Failure Simulation: Automatically detects severed links ("cut cables") and reroutes traffic in real-time.

## How to Run

1. **Clone the repository**
2. **Compile:**
   ```bash
   javac NetworkSim.java

##Output Sample

--- SCENARIO 1: Normal Network Conditions ---
Calculating fastest path from Ottawa to Toronto...
Route: [Ottawa, Kingston, Toronto]

--- SCENARIO 2: Link Failure (Ottawa-Kingston Cut) ---
Recalculating route...
New Route: [Ottawa, Montreal, Toronto]
