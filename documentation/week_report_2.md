# Week report 2

### What have I done this week?
I have created and implemented the trie data structure according to the examples in 'Trie' wikipedia article. I tried some different approaches to decrypting a cipher text which were not very successful, so I made just a basic backtracking algorithm to try out all possible substitutions. This is obviously not at all efficient, and I plan to improve it with the frequency analysis, but I did not have time to fully implement that this week. Also the backtracking function does not return anything right now, instead it adds all possible decryptions (actual words that can be found from the dictionary) to a result list - this too will be changed.

I had written some code for text analysis (find indexes for all used letters in ciphertext etc.) with my previous attempts that I decided to leave in the decryption method for now because I thought I might need these later - and that's why this function is now very long and confusing. I should probably break it into smaller functions. Also it now handles all the words in the cipher text separately and prints out the possible decryptions, this is to be changed later so that the decryption is somehow coordinated for the whole text and the method returns the decrypted text.

I also added unit tests for functions that probably won't change much in the future, wrote JavaDocs and configured the project to use jacoco and checkstyle.

### How has the project progressed?
There is a working trie datastructure for the dictionary, a preliminary version of the decryption method that will be improved, a rudimentary graphical userinterface and unit tests for the trie class and for some methods in the decrypter class. There are no unit tests for the decryption method yet because it is still very much work in progress.

### What did I learn this week/today?
I have probably heard of StringBuilder class before but it did not occur to me to use it here. It certainly makes some things easier.

### What has been unclear or problematic?
I did have some practical questions:
1. Previously I have had problems using JavaFX with Gradle, but Swing seems to work fine, so I used it for the ui. Is this ok or will it cause problems later?
2. Should the userinterface be tested/included in the test report?
3. Is there some specific set of checkstyle rules that should be used? I could not get [the example project's](https://github.com/TiraLabra/Testing-and-rmq) ruleset to work (it gave 'Unable to create Root Module' error when I copied the rules from there) but the ruleset from the course [Ohjelmistotekniikka](https://github.com/ohjelmistotekniikka-hy/syksy-2020/blob/main/web/checkstyle.md) works just fine (except that I have 2 methods that are too long).

### What next?
Next week I will implement rest of the frequency analysis and finish the decryption method. Perhaps the decrypter class should be divided into two classes, one for handling the text analysis and one doing the decryption.

### Working hours
This week I spent approximately 20 hours on the project.
