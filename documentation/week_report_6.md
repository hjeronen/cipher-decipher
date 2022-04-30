# Week report 6

### What have I done this week?
This week I mostly wrote unit tests for all the classes, updated the testing document and added performance tests for texts with errors. I also separated text handling into its own class, but I should probably still divide the Decrypter class into at least one subclass. I should also clean out the comments as suggested in both peer reviews...

### How has the project progressed?
There is a separate class for text processing (all special characters are cleaned out, text is broken into word arrays and these are sorted etc.). There are also unit tests for the TextHandler, Sorter and Crypter classes. I added performance tests for decrypting randomly formed texts with errors.

### What did I learn this week/today?
Running performance tests was very interesting. Obviously the number of errors in the text increases runtime, but the average times are considerably longer when the error words are very short or very long. I would like to run more tests to check how the word lengths affect decryption time.

### What has been unclear or problematic?
Should text preprocessing time (forming and sorting the word list) be separated from the actual decryption time in performance testing?

### What next?
I will try out adjusting the error margin as suggested in last week's feedback. Also I thought I should write a tester class for testing the accuracy of decryptions, and perhaps more performance tests for text preprocessing. I will need to clean out the comments from the code and do some more refactoring.

### Working hours
This week I used approximately 10 hours for the project. Next week I should have a lot more time to use, since all other courses have ended.
