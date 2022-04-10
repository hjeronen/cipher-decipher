# Week report 4

### What have I done this week?
I was sick with flu this week, so I have not accomplished very much. I tried several alternative approaches to decrypting. In the most recent version I changed the decryption method to pick words from a String array and look for substitutions for the characters through backtracking instead of keeping track of words with start and end indexes. I also added an error margin which allows some amount of words not to be found in the dictionary. I noticed that sorting the words from longest to shortest drastically shortens computation time, and the algorithm works rather efficiently even with the longer dictionary when trying the sample texts. I will need to do more testing, however.

I have started writing the implementation document, but I have not done any formal performance testing yet.

### How has the project progressed?
Some amount of errors is now allowed, and looks like the algorithm works rather fast now (solution found hopefully in minutes, not in hours or years).

### What did I learn this week/today?
During the whole project the workings of recursive functions have become more familiar.

### What has been unclear or problematic?
I'm not sure how to implement more efficient backtracking as discussed, but instead sorting the words by length helped a lot. This is probably because there are fewer possible decryptions for longer words.

The issue with backtracking is that I was able to successfully jump to a specific point in the recursion, such as the word where a character first appears, but determining where to jump is tricky because it is not possible to determine which of the characters in an erroneous word are wrong. If backtracking to the earliest possible point, in worst case the first word or character of the text, the key value might be right but it would be permanently changed into wrong one. I thought it safer to back up to the closest index, because even if here a right value is changed into wrong one, recursion will eventually backtrack to an earlier point and the right value will be tried again - but this does not improve speed.

However, sorting the words from longest to shortest and starting the decryption from the longest words drastically shortens the execution time, and the right solution is found quickly even with the long dictionary. Is this solution enough for the project? I used Arrays.sort, should I write my own sorting function?

### What next?
I will try to test the algorithm more and start performance testing.

### Working hours
This week I spent approximately 20 hours on the project.
