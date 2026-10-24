# Star Wars Episode X: A New Heap

## Overview

A Java-based galactic warfare simulation developed for CS 256: Data Structures at St. Lawrence University (Fall 2024).

The program simulates battles between Jedi and Sith battalions deployed across multiple planets. It uses priority queues to manage troop deployments, determine eligible battles, and track battle statistics.

## Features

- **Troop Deployment Processing:** Processes deployments with timestamps, general IDs, planet IDs, force sensitivity, and troop quantities.
- **Priority-Based Battle Matching:** Matches eligible Jedi and Sith battalions according to force sensitivity and deployment order.
- **Battle Simulation:** Updates troop quantities and processes consecutive battles when possible.
- **Median Tracking:** Calculates the median number of troops lost in battles on each planet.
- **General Evaluation:** Tracks troop deployments and surviving troops for individual generals.
- **Input Modes:** Supports deployment-list and pseudorandom input formats.
- **Output Options:** Supports verbose battle reports, median statistics, and general evaluations.

## Data Structures and Algorithms

- `PriorityQueue` for deployment management
- Min-heaps and max-heaps for median tracking
- Priority-based matching
- Object-oriented simulation design

## Implementation

Each planet maintains separate priority queues for Jedi and Sith battalions. Deployments are processed according to their force sensitivity and arrival order to determine which battalions can engage in battle.

Battle resolution updates the number of surviving troops and continues until no eligible opposing battalions remain.

For median tracking, the program maintains two heaps:

- A max-heap containing the lower portion of battle-loss values
- A min-heap containing the upper portion of battle-loss values

This structure supports incremental median calculation as battles occur.

## Technologies

- Java
- Java Collections Framework
- Apache Ant

## Project Structure

- `src/Main.java` — Application entry point
- `src/Config.java` — Command-line configuration
- `src/Simulation.java` — Simulation control and event processing
- `src/Planet.java` — Planet-specific battles and median tracking
- `src/Deployment.java` — Deployment data
- `src/JediDeployment.java` — Jedi deployment representation
- `src/SithDeployment.java` — Sith deployment representation
- `src/General.java` — General-specific statistics
- `src/P2Random.java` — Pseudorandom deployment generation
- `build.xml` — Ant build configuration

## Running the Project

Requires Java and Apache Ant, along with the Getopt dependency included in the project's `lib/` directory.

From the project directory:

```bash
ant
java -jar project2.jar --verbose --median --general-eval < input.txt
```

Replace `input.txt` with a valid deployment input file.

## Academic Context

Developed as Programming Project 2 for CS 256: Data Structures, Fall 2024, St. Lawrence University.
