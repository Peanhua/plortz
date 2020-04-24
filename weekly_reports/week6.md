# Week 6

Replaced Math with Meth. Meth being my own class implementing some of the Math functions.

Added one more data structure (list collection) to optimize the A* a bit. Added simple benchmarking system and measured the newly added data structure to verify that it was indeed a good idea. The benchmark for the important case (inserting into the beginning of a list) shows dramatic increase in performance, the ArrayList uses 17463111181 nanoseconds for the benchmark, while the new one uses 1331947 nanoseconds, 1331947/17463111181 ~= 0.0076%. Although the improvement in this particular area of A* is great, it's only small portion of the complete A* algorithm, so the difference is not going to be huge in reality, but the numbers look nice :)

Used approximately 6 hours this week, which is not as much as was planning to, next week should see better times. Because this week was slow, the next week goals are pretty much the same as was for this week: More work on gathering timing data and writing the final documentation (implementation and testing). Couple bugs to be fixed (hopefully), add missing tests and do some code cleanup/refactoring for the water adding system, and probably some polishing.
