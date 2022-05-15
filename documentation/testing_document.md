# Testing Document

The unit testing is done using JUnit and can be run from the IDE or from command line. Test coverage is monitored using Jacoco.

The performance tests are done with the class [PerformanceTester](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/src/main/java/util/PerformanceTester.java), and accuracy tests with [AccuracyTester](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/src/main/java/util/AccuracyTester.java). They can both only be run from the GUI by pressing the button 'Performance tests' or 'Accuracy tests'. The results are printed in the output text field eventually, but running these will take couple hours.

## Unit tests

### Test input
Some tests use a very short word list to check that forming the dictionary and translating texts works as inteded. A larger word list is also used to test decrypting longer texts. Other tests use as input strings and arrays that are properly designed for the test (strings that contain special characters or arrays that have words that are of same length or identical).

### Domain

#### Node
The tests for Node class check that children can be added to the node and retrieved from it, that if a child does not exist null is returned, that the size of list of node's children is properly increased when it is full and that it is increased by 5.

#### Trie
The Trie class is tested to make sure that words in the given word list are found after inserting them to the trie, that the function for finding words returns true for whole words and false for subtsrings, that the function for finding substrings (beginning of a word) does return true for valid substrings and false for invalid substrings, and that searching for words that are not inserted in the trie returns false.

### Logic

#### Decrypter
The tests for the Decrypter class check handling of user input text and make sure that decryption of texts works as intended, that the result texts are what is expected. For example, the non-alphabetical characters should be unchanged in the result text, and if the input text contains nothing but special characters, the text is returned as it is. The actual text handling and key finding happen in other classes, TextHandler and KeyFinder, and these are tested separately.

When testing the decryption function, there are two possible decryptions for the encrypted word 'tifmm' in the test trie. Either 'shell' or 'zeiss' are working decryptions, but because 'z' has normally very low frequency, 's' should be chosen before 'z', since 't' (and actually all the letters in a short word) has very high frequency.

#### TextHandler
The TextHandler class does all the text processing. The unit tests for this class check that the input text is properly prepared for the decryption and broken into word arrays so that the maximum limit for text length is not exceeded and that the word lists do not include empty Strings. It is also checked that character frequencies are approximately correctly counted, that all unique letters that are used in the text are found and that the result text is formed identically to the input text, with ciphered characters changed to key values and all other characters added as they are.

#### KeyFinder
The KeyFinder class is responsible for finding a decryption key that correctly decrypts all or most of the words in given word arrays. The tests check that the functions needed in the decryption work as intended, for example that the key values are properly chosen based on given frequency, that valid words and substrings are found from the dictionary, and that the right key is found with preset values. The findKey() method should increase the error margin if no solution is found with the initial value, and this is tested by finding a key for a text that has 20 % error words. The TextHandler class is used in some tests to produce needed arrays and Strings, but mostly these are preset for the KeyFinder.

#### Sorter
The Sorter class sorts words in an array alphabetically and from longest to shortest. The unit tests check that sorting alphabetically and by length sort the words correctly and that these together order the list as intended - namely that words are ordered from longest to shortest and that words that are same length are in alphabetical order.

### Util

#### TextFactory
The TextFactory is used to produce random texts for PerformanceTester and AccuracyTester. There are some tests for it, too, to check that forming the texts and adding error words works as intended.

#### Crypter
The Crypter class is used only for testing. It will form a cipher key and encrypt a given text. The tests check that all characters (or integers that represent the characters) have been chosen a proper value and that an encrypted text is not at least identical to the original text. The key values are chosen randomly, however, so there is a very small chance that each letter is replaced with itself.

### Test coverage
The current test coverage can be seen below:

![test_coverage](https://user-images.githubusercontent.com/73843204/167919759-89657cef-2f62-47e4-a6eb-695058d840e4.png)

The ui package, Main class, the PerformanceTester and the AccuracyTester are not included in the test report. Getters and setters in other classes are not tested, they are only used in unit testing.

## Performance and Accuracy testing
The performance and accuracy testing is done using [PerformanceTester](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/src/main/java/util/PerformanceTester.java) and [AccuracyTester](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/src/main/java/util/AccuracyTester.java) classes. Both use TextFactory and Crypter classes for forming random test texts and encrypting them. The test texts are formed by picking random words from a word list (the same that is used for the actual program) and adding randomly formed error words to the text. The text is then encrypted. In performance testing, the TextHandler is used to do the text preprocessing, and KeyFinder for finding the key to decrypt the text. The times for these processes are then measured. In accuracy testing, the cipher text is given to the Decrypter for decryption, and the accuracy of the decrypted text with the original text is then measured. At the end, the results of each test case are set into the output text field in the GUI.

The tables below show the average or median times of the tests, but the graphs show the results of all the test cases to illustrate the distribution of values.

The tests can be run from the GUI by clicking either of the buttons 'Performance tests' or 'Accuracy tests'. The running time for the tests is very long (several hours).

## Performance testing
The performance tests are run on an `AMD Ryzen 9 5900X` CPU with 32GB of RAM. The times are measured in nanoseconds, but the results that are printed are turned to milliseconds.

The tests are run for randomly formed ciphertexts of varying length or with increasing number of errors (error = a word that is not in the dictionary). The time is measured for the KeyFinder's [findKey()](https://github.com/hjeronen/cipher-decipher/blob/f38cd03bc7042ddf8954e9a5e8ef9474c6b56234/cipher-decipher/src/main/java/logic/KeyFinder.java#L138) function only, not for forming and sorting text arrays - text preprocessing times are measured separately.

The decryption time depends largely on the amount of error words, but can still vary a lot between different texts. For this reason, using only one text for one test case might be very misleading. In the following tests, a random text is generated for each iteration. The time for decrypting a text is measured several times, and the median of these times is saved into an array holding the median times for all the texts - then an average is counted from the medians of all the different texts of each test case and saved in the final result array, whose contents are presented in the tables. The median times for all the test texts are presented in the graphs.

For most test cases, the test is repeated for 10 different texts. Making generalizations would require a lot more testing, but it is very time consuming, considering that in worst case the time for decrypting a text could be up to 50-60 seconds. The most interesting thing that shows from these tests is how much the decryption time can vary between different texts and how the amount of error words (percentage of words in the text that are not found in the dictionary) affects the time, and I thought these repetitions would be enough to show that.

#### Text preprocessing times
Here are the median values for text preprocessig times in nanoseconds:
| words | median (ns) |
| --- | --- |
| 60 | 269600 |
| 100 | 308100 |
| 500 | 1618900 |
| 1000 | 1474500 |
| 5000 | 10206900 |
| 10000 | 34454500 |

<img src="https://user-images.githubusercontent.com/73843204/168471668-bc803dc6-132e-4737-a825-c3163fee46a9.png" width="600" height="400" />

This includes getting all the unique letters used in the text, getting character frequencies, forming a word array, sorting it and copying it to the second word array. Increasing text length will naturally increase preprocessing time. To get the frequencies of letters in a cipher text and all the characters that are used, the text has to be looped through, which takes O(n) time where n is the length of the text. Merge sort is used in sorting the words, which has the time complexity of O(m log m), where m is the amount of words.

#### Word lookup times from trie
Here are some times for searching words from the trie datastructure:
| word length | median (ns) |
| --- | --- |
| 1 | 400 |
| 4 | 1200 |
| 10 | 2100 |
| 19 | 3500 |

<img src="https://user-images.githubusercontent.com/73843204/168472131-86ace817-92ca-4316-bae4-8a8cffc5488a.png" width="600" height="400" />

Searching for a word from trie should happen in O(n) time where n is the length of the word. The times are measured for several times for each word, and the medians of the times are listed above. As expected, the time increases linearly with the word length.

#### Key finding times for test texts without errors
Here are the average times for finding a key for a text without any errors. The times are measured in nanoseconds, but transformed to milliseconds for the table. The scatter plot image uses nanoseconds.

| words | average (ms) | min | max |
| --- | --- | --- | --- |
| 60 | 53 | 1 | 258 |
| 100 | 232 | 11 | 743 |
| 500 | 167 | 2 | 571 |
| 1000 | 265 | 3 | 749 |
| 5000 | 186 | 2 | 426 |
| 10000 | 310 | 2 | 1020 |

<img src="https://user-images.githubusercontent.com/73843204/168466825-5179ae68-41d0-4593-89ce-2a6d2913daca.png" width="600" height="400" />

The test is repeated 10 times for each test case (60 words, 100 words...) and a new text is formed for each iteration, because the times vary a lot between different texts. The key finding time is measured 11 times for each text, and a median of these times is saved into the result array for this test case. The average, minimum and maximum times for all the texts of this test case are saved into the final result array and shown in the above results. (To clarify, all the median times for e.g. texts with 60 words are saved into the array medianTimes - the average, minimum and maximum values of this array are saved into place 0 in the array runtimesForTextsWithNoErrors, and all the contents of this array are shown in the above table. The graph shows the median times for all the different texts.)

The times are measured with System.nanotime(), but they are printed out in milliseconds. The decryption time varies a lot between the texts. I believe this might be due to what kind of words there are in the text and how well the cipher character frequencies match the expected frequencies. If some text has only small words, decrypting it might take a longer time, because small words have several possible translations and only few keys are discovered with each word, which then leads to more recursive calls if the first guesses are not right. Starting the decryption with long words decreases the decryption time significantly, because longer words tend to have fewer possible translations and several keys can be found with just one word, so there would be fewer calls of the recursive function.

The maximum length of text (the sum of the lengths of the words in word arrays) that is handled in the recursive function is 2000 characters. Therefore the time the recursive key finding function uses should not increase that much with longer texts. The text preprocessing might slightly increase the computing time as a whole, since the text is broken into word arrays that are sorted (the sorting function's time complexity is O(n log n)), but these times are not measured here.

#### Key finding times for random test texts with errors
Here are the times for randomly formed texts with increasing number of errors. The error words (5, 10, 15, 20 and 25) are marked as percentages (2.5, 5.0, 7.5, 10.0 and 12.5) of all the words in  the text (200 words each). Times are in milliseconds in the table, and in nanoseconds in the graph.

| error words (%) | average (ms) | min | max |
| --- | --- | --- | --- |
| 2.5 | 1671 | 29 | 7796 |
| 5.0 | 1020 | 25 | 2901 |
| 7.5 | 2108 | 41 | 5657 |
| 10.0 | 6087 | 28 | 51764 |
| 12.5 | 30667 | 19051 | 37803 |

<img src="https://user-images.githubusercontent.com/73843204/168471014-7cf80aa6-fa90-4e3a-9ac2-1d29f55793dd.png" width="600" height="400" />

All the texts used in the tests have 200 words in total, including a changing number of randomly formed error words that are not expected to be found in the dictionary. The amount of words is the same because the length of the text that is processed is limited, and this way the error words are most likely included in the text that is used for decryption. An error word is formed by picking random integers between 97-122 which are then changed to chars. The error words have the length of 5-15 characters (this is also determined randomly) - I thought this would be a good approximation of general cases. The test is performed 10 times for each test case, with a new random text formed for every decryption - the time for a single text is measured 11 times, and a median is taken from these. The average value above is the mean of all medians.

In general, it cannot be stated that the amount of errors increases the decryption time. The average times are pretty much the same, except for one text with 10% errors that has strangely long decryption time. Again, the times vary a lot for different texts, which propably skewes the results a bit. The minimum and average times for 25 errors are expected to be rather high because the initial error margin is 10%, which is not enough here, and attempting a decryption with a hint too small error margin takes some time. If initially too small, the 10% error margin is doubled, and the decryption is attempted again. The amount of errors allowed is then decreased until reaching the limit where it is too small for the text, and a too small error margin is attempted for the second time. The overall performance time is largely determined by how long it takes before the algorithm decides that an error margin is too small, and when this is done twice, the time for a text with >10% errors should be doubled to the times of texts with <=10% errors.

So a very large error margin definitely increases decryption time. Here all the test texts are of the same length, but below are times for texts with different lengths and same amount of errors.

#### Key finding times for random texts with different length and same amount of errors
Here the text length varies from 60 to 500 words. All the texts have 20 error words. A new text is formed for each round (10 different texts for each test case), and the averages of their medians are seen in the table. The graph shows the medians for all the texts.
| words | average (ms) | min | max |
| --- | --- | --- | --- |
| 60 | 18790 | 11333 | 27835 |
| 100 | 9713 | 6535 | 13512 |
| 200 | 821 | 16 | 1585 |
| 500 | 2282 | 15 | 8191 |

<img src="https://user-images.githubusercontent.com/73843204/168476441-cee790f9-85e3-49c1-a1b1-2b63d4913f14.png" width="600" height="400" />

Here the decryption time actually decreses when the amount of errors stays the same and the amount of words increases - this is because the proportion of errors decreases. The error words are not the same for each text, however. A new random text is formed for each round of each test case, and again the times vary a lot between different texts. With five hundred words, probably not all of the words are included in the recursion, because the text length is limited and the shortest words are left out. This could mean that some of the error words are not included if they are very short and other words are very long - or if they are all very long, some of the correct words might be left out and the error margin might be a bit higher for the text.

With 60 words, error margin is very large, and initial 10% error margin will have to be increased twice, which increases the execution time significantly. Same goes for 100 words. This is why the times for 200 and 500 words are much better, because the initial error margin is enough, and decryption with a too small error margin is attempted only once.

#### Key finding times for the same text with increasing number of errors
Here are measured some times for the same text with increasing number of errors. The text has 200 words, and the amounts of error words are 0, 5, 10, 15 and 20 words, so the error margins are 0%, 2.5%, 5%, 7.5% and 10%. The times are measured 10 times for each case of errors, and a median of these times is taken. The test text is the same for every test case, but the error words change (because of how they are added to the text).

| error words (%) | median (ms) |
| --- | --- | 
| 0.0 | 91 |
| 2.5 | 132 |
| 5.0 | 1195 |
| 7.5 | 1301 |
| 10.0 | 708 |

<img src="https://user-images.githubusercontent.com/73843204/168478766-173b03be-7d20-4fec-a7e8-d4c01dfcb5a7.png" width="600" height="400" />

Looking at the times measured for texts with 0 errors, this text has a rather fast decryption time as baseline, 91 ms. As mentioned, the decryption time varies a lot for different texts. It is also curious how the decryption time is lower for 20 (10%) errors than for 15 (7.5%) errors. But again, the decryption time also depends on what kind of error words there are and how they are placed in the word array, long ones at the beginning and short ones at the end. The error words are generated randomly, so it is not possible to say here which is better or worse.

### The error margin
The times here are measured (in most cases) up to the 10% error margin. The dictionary that is used is very large (around 400 000 english words), so the error margin should not be very large for an average cipher text. This is of course very case sensitive. For any 'normal' text, almost all the words should be found in the dictionary, barring any misspelled words. In manual testing when decrypting more exotic texts (such as the Star Wars -texts in [sample texts](https://github.com/hjeronen/cipher-decipher/blob/main/sample_texts.md)) the 10 percent error margin works surprisingly well.

## Accuracy testing
The AccuracyTester class implements accuracy tests that check the average accuracy of a decryption result with the original text. The accuracy is determined by comparing characters. The test texts are formed exactly like in performance testing, by picking random words from the wordlist and then using the Crypter class to encrypt the text. Each text has 100 words and 0, 5, 12, 14 or 21 error words, which are 5-15 characters long and also formed randomly. The test is run for 10 different text for each test case (0 error words, 5 error words...). The results are saved in arrays and the average values are counted from these.

| error words | accuracy (%) |
| --- | --- | 
| 0 | 100 |
| 5 | 99.9 |
| 12 | 100 |
| 14 | 100 |
| 21 | 99.8 |

<img src="https://user-images.githubusercontent.com/73843204/168481793-83b62643-f248-41ae-8615-14e04ba31fb5.png" width="600" height="400" />

The accuracy of decryptions is rather good, generally at least about 99%. Again the graph shows all the test cases, and some texts with 5 or 21 error words had less than 100% accuracy. There are a couple situations where problems arise and mistranslations happen.

The usual case is when a rare letter appears in a text only in words that are not in the dictionary (that is in error words). In this case, it is not possible to guarantee 100 % correct translation. After decrypting the words in a word array the algorithm will check if there are any ciphered letters that have not been set a key value. If there are, a most likely key is chosen from the remaining available key values based on the frequency. This is correct if there is only one unused key left. If there are several, the chosen value might be correct or it might be wrong. If some less likely option is actually the correct key value, the result will be wrong, because the one whose frequency is closest to the ciphered letter's frequency will be chosen.

It is also possible to translate a text with 0% error margin, yet some letters have still been substituted wrong - they just don't cause any errors to the text. In this case there would have been several possible translations for the text, but since the decryption algorithm will stop at the first possible one, it might not be the correct/intended one.
