# Testing Document

The testing is done using JUnit and can be run from the IDE or from command line. Test coverage is monitored using Jacoco.

### Test input
As for now, very short word lists are used as test input to check that forming the datastructure and using it works as inteded.

#### Trie datastructure
The Trie class is tested to make sure that words in the given word list are found after inserting them to the trie, that the function for finding words returns true for whole words and false for subtsrings, that the function for finding substrings does return true for valid substrings and false for invalid substrings, and that searching for words that are not inserted in the trie returns false.

#### Decrypter
The tests for the Decrypter class make sure that the function for finding key value with closest frequency returns correct character (when no characters have been marked as used), that valid words and substrings are found, that substitution-function changes the characters in the string correctly and that the decryption function returns the likely decryption.

When testing the decryption function, there are two possible decryptions for the encrypted word 'tifmm' in the test trie. Either 'shell' or 'zeiss' are working decryptions, but because 'z' has normally very low frequency, 's' should be chosen before 'z', since 't' (and actually all the letters in a short word) has very high frequency.

The tests for the decryption method will change because the algorithm is to be improved later.

### Test coverage
The current test coverage can be seen below:
![test_coverage](https://user-images.githubusercontent.com/73843204/161393751-b8be86a2-3ba3-4651-ac68-2b54d858b17c.png)
