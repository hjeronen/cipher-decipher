# Week report 4

### What have I done this week?
I was sick with flu this week, so I have not accomplished very much. I tried several alternative approaches to decrypting. In the most recent version I changed the decryption method to pick words from a String array and look for substitutions for the characters through backtracking instead of keeping track of words with start and end indexes. I also added an error margin which allows some amount of words not to be found in the dictionary. The execution time is still very long.

I have started writing the implementation document, but I have not done any performance testing since the execution of the algorithm isn't nearly efficient enough.

### How has the project progressed?
Some amount of errors is now allowed.

### What did I learn this week/today?
During the whole project the workings of recursive functions have become very familiar.

### What has been unclear or problematic?
I'm not sure how to implement more efficient backtracking. I was able to successfully jump to a specific point in the recursion, such as the first appearance of a character, but determining where to jump is tricky because it is not possible to determine which of the characters in an erroneous word are wrong.

I think the safest option would be the closest index, because even if here a right value is changed into wrong one, recursion will eventually backtrack to an earlier point and the right value will be tried again - but if backtracking to earliest possible point, in worst case the first character of the text, the right value would be permanently changed to wrong one.

### What next?
The algorithm is still very slow with long dictionary or large amount of errors, so the backtracking will need to be improved.

### Working hours
This week I spent approximately 20 hours on the project.
