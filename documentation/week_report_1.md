# Week report 1

### What have I done this week?
This week I did research on some of the topics, and after choosing one I read mostly wikipedia-articles on that or any related subjects. I was on the fence between two topics and used some time considering how to solve the other one (creating an interpreter for regular-expressions). However, I chose to make a program that decrypts a simple-substitution cipher using frequency analysis. I familiarized myself with fequency analysis and tried to find some example algorithms and more sources. I have also initialized the project and written the project specification document.

### How has the project progressed?
I have initialized a GitHub-repository, sorted out some configuration issues and created a Gradle project.

### What did I learn this week/today?
I learned about frequency analysis and trie data structure, which were both new to me.

### What has been unclear or problematic?
The assignment description said to use trie data structure and backtracking algorithm. I had some initial thoughts on what to do, but I'm not yet sure how to exactly code the solution. I thought I could save some English dictionary in one trie tree, and break the cipher text into words list and then save it in another trie tree, then go through it with the backtracking algorithm changing the letters (or node values, in the order indicated by the frequency analysis) and check if the resulting (sub)strings are found in the dictionary tree?

It also seemed difficult to estimate the time and space complexity for the project specification because my plan of execution is still a bit fuzzy. What I wrote is probably wrong and will change.

### What next?
I will figure out the core logic for the solution, create a first draft of the program and try to find more source materials if necessary.

### Working hours
This week I used approximately 15 hours for the course.
