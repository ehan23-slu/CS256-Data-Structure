# Letterman Reboot — Path Finding

## Overview

A Java-based word transformation program developed for CS 256: Data Structures at St. Lawrence University (Fall 2024).

The program searches for a valid transformation path between two words using a dictionary and a set of permitted character modifications. It supports stack-based and queue-based search strategies.

## Features

- **Stack-Based Search:** Explores possible word transformations using a stack (depth-first traversal).
- **Queue-Based Search:** Explores word transformations using a queue (breadth-first traversal).
- **Word Modifications:** Supports changing a character, swapping adjacent characters, and inserting or deleting characters.
- **Path Reconstruction:** Displays a valid transformation sequence from the starting word to the target word.
- **Output Options:** Supports word-sequence output and individual modification instructions.
- **Input Validation:** Handles invalid command-line arguments and word-selection conditions.

## Data Structures and Algorithms

- `ArrayDeque` for stack and queue operations
- `ArrayList` for dictionary storage
- Graph traversal and pathfinding
- Predecessor tracking and path reconstruction

## Implementation

The program treats dictionary words as possible states in a search problem. Two words are considered connected when one can be transformed into the other using an allowed modification.

An `ArrayDeque` manages the words waiting to be explored. The search strategy determines whether words are processed in stack or queue order. Previously discovered words are tracked to prevent repeated exploration.

Once the target word is discovered, the program reconstructs the transformation path.

## Technologies

- Java
- Java Collections Framework
- Apache Ant

## Project Structure

- `src/Main.java` — Application entry point
- `src/Config.java` — Command-line arguments and configuration
- `src/Letterman.java` — Dictionary processing, search, and path reconstruction
- `build.xml` — Ant build configuration

## Running the Project

Requires Java and Apache Ant, along with the Getopt dependency included in the project's `lib/` directory.

From the project directory:

```bash
ant
java -jar project1.jar --queue --begin chip --end stop --change < dictionary.txt
```

Replace `dictionary.txt` with a valid dictionary input file. The example words must exist in that file.

## Academic Context

Developed as Programming Project 1 for CS 256: Data Structures, Fall 2024, St. Lawrence University.
