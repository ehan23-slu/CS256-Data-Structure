# SLU-QL — Relational Database Engine

## Overview

A Java-based relational database engine developed for CS 256: Data Structures at St. Lawrence University (Fall 2024).

The project implements a simplified SQL-like query interface for creating and managing tables, retrieving records, generating search indexes, and joining relational data.

## Features

- **Table Management:** Creates and removes relational tables with defined column names and data types.
- **Data Operations:** Supports inserting and deleting records.
- **Conditional Queries:** Retrieves records using comparison conditions.
- **Index Generation:** Builds hash-based and binary-search-tree-based indexes for supported queries.
- **Table Joins:** Combines matching records from two tables.
- **Multiple Data Types:** Supports integer, double, boolean, and string values.
- **Quiet Mode:** Supports summary-only output for query operations.

## Data Structures and Algorithms

- `ArrayList` for table rows and column metadata
- `HashMap` for hash-based indexing
- `TreeMap` for ordered, tree-based indexing
- Hash-based matching for table joins
- Comparators and polymorphism for typed data operations

## Implementation

The database maintains tables consisting of rows and typed columns. User commands are parsed and dispatched to the corresponding table operations.

The engine supports two indexing approaches:

- **Hash Index:** Maps column values to matching row positions for indexed lookup.
- **Tree-Based Index:** Maintains ordered keys for comparison-based retrieval.

For table joins, the implementation builds a hash-based lookup structure for matching records between two tables.

The system also supports conditional filtering, index generation, and updates to stored table data.

## Supported Commands

| Command | Description |
|---|---|
| `CREATE` | Create a new table |
| `INSERT INTO` | Insert rows into a table |
| `DELETE FROM` | Delete rows matching a condition |
| `GENERATE FOR` | Generate a hash or BST index |
| `PRINT FROM` | Retrieve selected records |
| `JOIN` | Join two tables |
| `REMOVE` | Remove a table |
| `QUIT` | Exit the program |

## Technologies

- Java
- Java Collections Framework
- Apache Ant

## Project Structure

- `src/Main.java` — Command parsing and execution
- `src/BSTTreeMap.java` — Database-level table management
- `src/Table.java` — Table operations, queries, indexing, and joins
- `src/Row.java` — Row representation
- `src/TableEntry.java` — Typed database values
- `src/RowComparators.java` — Data comparison operations
- `build.xml` — Ant build configuration

## Running the Project

Requires Java and Apache Ant.

From the project directory:

```bash
ant
java -jar project3.jar
```

Enter supported commands through standard input.

Example:

```text
CREATE students 2 string int name age
INSERT INTO students 2 ROWS
Alice 21
Bob 22
PRINT FROM students 2 name age ALL
QUIT
```

## Academic Context

Developed as Programming Project 3 for CS 256: Data Structures, Fall 2024, St. Lawrence University.
