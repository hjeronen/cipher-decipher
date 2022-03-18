# Week report 1

### What have I done this week?
This week I did research on some of the subjects, and after choosing the topic I read mostly wikipedia-articles on that or any related subject. I was on the fence between two topics and wasted some time considering how to solve the other one (creating an interpreter for regular-expressions). However, I chose to make a program that decrypts a simple-substitution cipher using frequency analysis. As an introductory exercise, I decrypted some sample texts I found online and tried to find some example algorithms.

### How has the project progressed?
I have initialized a GitHub-repository and created a Gradle project.

### What did I learn this week/today?
I learned about frequency analysis and trie data structure, which were both new to me.

### What has been unclear or problematic?
I had some initial thoughts on how to approach the problem - the assignment mentioned using a backtracking algorithm, and this I am familiar with, but I'm not yet sure how to exactly code the solution. I assume some English dictionary would be saved in one trie-tree, and the cipher text would be broken into words and then saved in another trie-tree, which is then gone through with the backtracking algorithm changing the letters (or node values, in the order indicated by the frequency analysis) and then checking if the resulting strings match the words in the dictionary tree?

It also seemed a bit difficult to estimate the time and space complexity for the project specification documentation because my plan of execution is still a little fuzzy.

### What next?
I will figure out the core logic for the solution and try to find more source materials if necessary.
