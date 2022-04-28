# Testing Document

The testing is done using JUnit and can be run from the IDE or from command line. Test coverage is monitored using Jacoco.

## Unit tests

### Test input
As for now, very short word lists are used as test input to check that forming the datastructure and using it works as inteded. Other tests use strings and arrays that are properly designed for the test (strings that contain special characters or arrays that have words that are of same length or identical).

### Domain

#### Trie datastructure
The Trie class is tested to make sure that words in the given word list are found after inserting them to the trie, that the function for finding words returns true for whole words and false for subtsrings, that the function for finding substrings (beginning of a word) does return true for valid substrings and false for invalid substrings, and that searching for words that are not inserted in the trie returns false.

### Logic

#### Decrypter
The tests for the Decrypter class make sure that the function for finding key value with closest frequency returns correct character (when no characters have been marked as used), that valid words and substrings are found, that substitution-function changes the characters in the string correctly and that the decryption function returns the likely decryption.

When testing the decryption function, there are two possible decryptions for the encrypted word 'tifmm' in the test trie. Either 'shell' or 'zeiss' are working decryptions, but because 'z' has normally very low frequency, 's' should be chosen before 'z', since 't' (and actually all the letters in a short word) has very high frequency.

The tests for the decryption method will change because the algorithm is to be improved later.

#### Sorter
The Sorter class sorts words in an array alphabetically and from longest to shortest. The unit tests check that sorting alphabetically and by length sort the words correctly and that these together order the list as intended - namely that words are ordered from longest to shortest and that words that are same length are in alphabetical order.

#### TextHandler
The TextHandler class handles all text processing. The unit tests check that all unwanted characters are cleaned out of the given text, that character frequencies are correctly calculated and that all unique alphabetical characters (and nothing else) that are used in the text are found. The forming of word lists and replacing the characters in the result text are also tested.

### Util

#### Crypter
The Crypter class is used only for testing. It will form a cipher key and encrypt a given text. The tests check that all characters (or integers that represent the characters) have been chosen a proper value and that an encrypted text is not at least identical to the original text. The key values are chosen randomly, however, so there is a very small chance that each letter is replaced with itself.

### Test coverage
The current test coverage can be seen below:

![test_coverage](https://user-images.githubusercontent.com/73843204/165744358-0df1e743-4115-42ed-a0d7-f67c562790f8.png)

The coverage for util package is not very high because the PerformanceTester class is not tested.

## Performance testing
The performance tests are run for randomly formed ciphertexts of varying length or with increasing number of errors. All the words in the dictionary are loaded into a list, and a given number of words are then randomly chosen and combined as a test text that is then encrypted. The decryption time is then measured for the whole decryption function including the text preprosessing time. (I try to fix this later.)

#### Running times for test texts without errors
(chart here)
The maximum length of text that is handled in the recursive function is 2000 characters. Therefore the time the recursive decryption function uses should not increase that much with longer texts. The sorting function's time complexity is O(n log n), however, which will increase the preprocessing time for longer texts. This propably explains why performance time is pretty much the same for 100-1000 words but starts to increase with 5000 and 10000 words.

#### Running times for test texts with errors
(chart here)
All the texts used in the test have 200 words in total, including a changing number of randomly formed error words that are not expected to be found in the dictionary. The amount of words is the same because the length of the text that is processed is limited, and this way the error words are most likely included in the text that is used for decryption. The error word is formed by picking random integers between 97-122 which are then changed to chars. The error words have the length of 1-10 characters. The texts that are tested have 0, 5, 10, 15 and 20 error words. The test is repeated 10 times for each instance (I will try to run more tests but these take forever...).
