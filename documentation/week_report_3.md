# Week report 3

### What have I done this week?
I have implemented frequency analysis to the program - the recursive algorithm now chooses the key value that is unused and that has the closest frequency to the ciphered letter's frequency. The algorithm finds the correct encyption if all the necessary words are in the word list, and it works fast if the wordlist is short (5000 words). However, the computation time is very long with a very long wordlist (40k words) because there are so many possible decryptions for each word. Also, the decryption should still return some solution even if all the correctly decrypted words are not found in the dictionary. These issues will need to be adressed next.

I have also started writing the testing document and added a test for the decryption method to check that it finds the correct and most likely solution. The current test coverage is quite good, but still I should propably write more and better tests.

### How has the project progressed?
I have a working backtracking algorithm that uses frequency analysis to try out substitutions. This will need to be improved, however.

### What did I learn this week/today?
After discussing with the course assistant I realized I should take into account situations where the decryption might be right but the words are not found in the word list. For this, and also for more efficient decrypting, there should be some sort of error tolerance.

### What has been unclear or problematic?
Based on the discussion I have some sort of idea what to do next, so if there are any big issues I will ask about them next week.

### What next?
Next week I will add some sort of error tolerance - certain amount of words are allowed not to be found in the dictionary - and more efficient backtracking. I'm not sure yet how to do this, but I will figure it out in the beginning of next week.

### Working hours
This week I spent approximately 15 hours on the project.
