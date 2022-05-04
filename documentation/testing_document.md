# Testing Document

The unit testing is done using JUnit and can be run from the IDE or from command line. Test coverage is monitored using Jacoco.

The performance tests are done with the class PerformanceTester, and they can only be run from the GUI by pressing the button 'Performance tests'. The results are printed in the output text field. Running these will take 45 minutes.

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

## Performance and Accuracy testing
The performance and accuracy testing is done using PerformanceTester and AccuracyTester classes. Both use TextFactory and Crypter classes for forming random test texts and encrypting them. The test texts are formed by picking random words from a word list (the same that is used for the actual program) and adding randomly formed error words to the text. The text is then encrypted and given to the Decrypter for decryption, and the computing time or accuracy of decryption with the original text is then measured and saved to an array. At the end, the average values of each test case are set into the output text field in the GUI.

The tests can be run from the GUI by clicking either of the buttons 'Performance tests' or 'Accuracy tests'. The running time for the tests is very long (45 minutes to 1 hour).

## Performance testing
The performance tests are run on an `AMD Ryzen 9 5900X` CPU with 32GB of RAM. The times are measured in milliseconds.

The tests are run for randomly formed ciphertexts of varying length or with increasing number of errors. The decryption time is then measured for the whole decryption function including the text preprosessing time. (I try to fix this later.)

Because the decryption time depends not only on the amount of error words but also on the length of the words, and since the words are picked at random for test texts, there is actually a lot of variance in how long it will take to decrypt a cipher text. For texts with only very short or very long words the time will be longer than for texts with even distribution of longer and shorter words. Also with texts where there are a lot of error words and these are all either very long or very short, the decryption time will be long. When there are very long words and zero or very few error words of medium length, the decryption time can be very fast.

#### Running times for test texts without errors
````````````````````````````````````````````
When all words are found in the dictionary:
100 words average time ms: 233
500 words average time ms: 199
1000 words average time ms: 242
5000 words average time ms: 389
10000 words average time ms: 780
````````````````````````````````````````````
The maximum length of text that is handled in the recursive function is 2000 characters. Therefore the time the recursive decryption function uses should not increase that much with longer texts. The sorting function's time complexity is O(n log n), however, which will increase the preprocessing time for longer texts. This propably explains why performance time is pretty much the same for 100-1000 words but starts to increase with 5000 and 10000 words.

#### Running times for test texts with errors
``````````````````````````````````````````
Average run times for texts with errors:
0 errors average time ms: 223
5 errors average time ms: 1014
10 errors average time ms: 1764
15 errors average time ms: 10590
20 errors average time ms: 12033
``````````````````````````````````````````
All the texts with errors used in the tests have 200 words in total, including a changing number of randomly formed error words that are not expected to be found in the dictionary. The amount of words is the same because the length of the text that is processed is limited, and this way the error words are most likely included in the text that is used for decryption. An error word is formed by picking random integers between 97-122 which are then changed to chars. The error words have the length of 5-15 characters (this is also determined randomly) - I thought this would be a good approximation of general cases. The texts that are tested have 0, 5, 10, 15 and 20 error words. The test is repeated 100 times for each instance (with a new random text formed for every decryption).

The number of errors increases performance time, but this depends largely on what kind of words there are, very short or very long, whether they are erroneous or not. If all the error words are long and at the beginning of the word array where the decryption starts, or if the error words are very short and at the end of the word array, the decryption time might be very long. When experimenting with [sample texts](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/sample_texts.md), text 2 has more errors than text 1, but the decryption time for text 2 was actually shorter than for text 1. I believe this depends on how error words are distributed among normal words (when sorted by length).

In the results above there does not seem to be that big of a difference between 5 and 10 error words (2.5 % and 5 % of all words) or 15 and 20 error words (7.5 % and 10 % of all words). This is probably due to how the error margin is adjusted, since it is 0 % at first, and then increased by 5 % each time until a succesful translation is found. This will be changed, so these results are not yet final.

The dictionary that is used is very large (around 400 000 english words), so the error margin should not be very large for an average cipher text. This is of course very case sensitive. For any 'normal' text, almost all the words should be found in the dictionary, barring any misspelled words, but in cases like the example texts that are captions from Star Wars -movies, there are quite many words that are not found in any dictionary.

## Accuracy testing
The AccuracyTester class implements accuracy tests that check the average accuracy of a decryption result with the original text. The test texts are formed exactly like in performance testing, by picking random words from the wordlist and then using the Crypter class to encrypt the text. Each text has 100 words and 0, 5, 12, 14 or 21 error words, which are 5-15 characters long and also formed randomly. The test is run 10 times for every text in each class. The results are saved in arrays and the average values are counted from these.

``````````````````````````````````````````
Average accuracies:
texts with 0 errors: 1.0
texts with 5 errors: 0.9993485342019544
texts with 12 errors: 1.0
texts with 14 errors: 0.9981873215462332
texts with 21 errors: 0.9994742376445845
``````````````````````````````````````````
The accuracy of decryptions is rather good, but there are a couple situations where problems arise and mistranslations happen.

In cases where the error margin is too large, some erroneous substitutions might get through. For example, if a cipher word is translated to 'fountain' when the correct translation would have been 'mountain', the one letter error might cause more error words, but if there are not many enough to break the limit, these are allowed to stay in text.

Another case where mistranslations happen is when a rare letter might appear in a text only in words that are not in the dictionary (that is in error words). In this case, it is not possible to guarantee 100 % correct translation. After decrypting the words in a word array the algorithm will check if there are any ciphered letters that have not been substituted with a key value. If there are, a most likely key is chosen from the remaining available key values based on the frequency. This is correct if there is only one unused key left. If there are several, the chosen value might be correct or it might be wrong. If some less likely option is actually the correct key value, the result will be wrong, because the one whose frequency is closer to the ciphered letter's frequency will be chosen.
