# Week report 5

### What have I done this week?
This week and during the Easter break I started doing performance testing and noticed some problems with the program. Very long texts caused stack overflow errors, so I limited the length of the text that is processed in the recursion. This seemed to fix the stack overflow error. I also considered alternative ways to handle errors in the text. The current version of the algorithm is a bit "lazy" as it returns a slightly erroneus translation when using too big error margin. The easiest way to fix this was to start with the error margin of 0, in case all the words are found in the dictionary, and increase the percentage little by little if no results are found. If there are very little errors in the text, this works fast, but performance time increases the more errors are allowed in the text, and finding the rigth error margin takes some time. I did make a more accurate version of the algorithm, but too big error margin will still allow a sligthly erroneous translation. Also rare characters that only appear on error words will not get substituted at all. I will need to look into this more next week.

I also wrote my own sorting algorithm for sorting the words by length. I used merge sort according to the example code in the course material of Datastructures and Algorithms course. I also added some error handling to the program (empty spaces in the text are ignored) and as mentioned limited the size of the text that is processed. I added some more unit tests, too, but I will need to write more tests in the future.

### How has the project progressed?
I added my own sorting function (because I assumed Arrays.sort() was not allowed). There are performance tests for decrypting cipher texts of different lengths (when all the words are found in the dictionary). There are also some more unit tests.

### What did I learn this week/today?
I learned a bit about file packaging from doing the peer review. I also refreshed my memory of what I had previously learned about sorting algorithms.

### What has been unclear or problematic?
Handling error words is still a bit tricky. If an erroneous substitution produces a correct translation at first and then erroneous words, then too big error margin will allow these to stay in the text if all the other characters are substituted correctly. Also if there are rare letters that only appear in words that are not in the dictionary, it does not seem possible to get 100% correct translation for these, only best guess out of remaining key values.

### What next?
I will write performance tests for cipher texts with varying number of errors. I will also write more unit tests for the decryption class, do some fixes on the decryption function, refactor code and write more JavaDoc descriptions.

### Working hours
I didn't have that much time for the project this week, only about 12 hours.
