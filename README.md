# Knight-Moves-6
A solution to Jane Street's October 2024 puzzle. It aims to efficiently find the smallest valid sum of A+B+C as quickly as possible with no care for its shortest path. The code can be tweaked to find the shortest path if desired but it significantly increases the computation time in doing so.

## Code Output
<p align="center">
  <img src="output.png"/>
</p>

## The Puzzle
### Knight Moves 6
From [https://www.janestreet.com/puzzles/knight-moves-6-index/](https://www.janestreet.com/puzzles/knight-moves-6-index/)
<p align="center">
  <img src="https://www.janestreet.com/puzzles/october-2024.png" />
</p>

Pick **distinct positive integers** *A*, *B*, and *C*, and place them in the grid above. Your goal is to create two corner-to-corner trips — one from *a1* to *f6*, and the other from *a6* to *f1* — both of which score **exactly 2024 points**.

A “trip” consists of knight’s moves. Squares may **not** be revisited within a trip.

The “score” for a trip is calculated as follows:

  - Start with *A* points.
  - Every time you make a move:
    - if your move is between two *different* integers, **multiply** your score by the value you are moving to;
    - otherwise, **increment** your score by the value you are moving to.

Can you find positive integers *A*, *B*, and *C*, as well as a pair of trips, that satisfy the criteria above? How low can you get *A* + *B* + *C*?

Please format your entry by concatenating your values for A, B, and C, followed by your *a1*-to-*f6* tour, followed by your *a6*-to-*f1* tour. For example, “1,2,253,a1,b3,c5,d3,f4,d5,f6,a6,c5,a4,b2,c4,d2,f1” would be a properly formatted entry.

To qualify for the leaderboard your value for A + B + C must be **less than 50**.
