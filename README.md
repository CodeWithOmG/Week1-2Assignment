# Week1-2Assignment

## Overview
This repository contains Java implementations of real-world system design problems using hash tables and related data structures.
The project demonstrates how hashing techniques can be applied to solve practical problems such as caching, rate limiting, autocomplete systems, and inventory management.

All programs are implemented in Java and can be executed individually since each file contains its own main() method.

## Implemented Problems
1.Username Availability Checker

Checks if a username is already taken.

Suggests alternative usernames.

Uses HashMap for fast lookup.

2.Flash Sale Inventory Manager

Simulates product purchases during a flash sale.

Prevents overselling.

Uses HashMap and Queue for stock tracking and waiting lists.

3.DNS Cache with TTL

Simulates a DNS caching system.

Stores domain-IP mappings with expiration time (TTL).

Uses HashMap for fast access.

4.Plagiarism Detection System

Detects similar text using n-gram hashing.

Stores document patterns for comparison.

5.Real-Time Analytics Dashboard

Tracks page views in real time.

Displays most visited pages using frequency counting.

6.Distributed Rate Limiter

Limits the number of requests from a client.

Prevents excessive requests in distributed systems.

7.Autocomplete System

Suggests search queries based on a prefix.

Uses hashing and prefix matching.

8.Parking Lot Management using Open Addressing

Demonstrates hash table collision handling using linear probing.

Assigns parking spots using hashing.

9.Two-Sum Transaction Detection

Finds pairs of numbers that sum to a target value.

Uses HashMap to achieve efficient lookup.

10.Multi-Level Cache System

Simulates L1 and L2 caching layers.

Demonstrates cache hit, miss, and promotion.

## Technologies Used
Java

HashMap

HashSet

Queue

Arrays

Basic hashing techniques

## How to Run
Each Java file contains its own main() method.

To run a program:

1. Open the project in IntelliJ IDEA.

2. Navigate to the desired Java file.

3. Right-click the file.

4. Click Run.

Example:
Run UsernameAvailabilityChecker.java to test the username checker system.

## Concepts Demonstrated
Hash Tables

Constant-time lookup O(1)

Collision handling

Frequency counting

Caching strategies

Rate limiting

Prefix-based search

Real-world system simulation

