# Testing Document

The unit testing is done using JUnit and can be run from the IDE or from command line. Test coverage is monitored using Jacoco.

The performance tests are done with the class PerformanceTester, and accuracy tests with AccuracyTester. They can both only be run from the GUI by pressing the button 'Performance tests' or 'Accuracy tests'. The results are printed in the output text field eventually, but running these will take couple hours.

## Unit tests

### Test input
Some tests use a very short word list to check that forming the dictionary and translating texts works as inteded. A larger word list is also used to test decrypting longer texts. Other tests use as input strings and arrays that are properly designed for the test (strings that contain special characters or arrays that have words that are of same length or identical).

### Domain

#### Node
The tests for Node class check that children can be added to the node and retrieved from it, that if a child does not exist null is returned, that the size of list of node's children is properly increased when it is full and that it is increased by 5.

#### Trie datastructure
The Trie class is tested to make sure that words in the given word list are found after inserting them to the trie, that the function for finding words returns true for whole words and false for subtsrings, that the function for finding substrings (beginning of a word) does return true for valid substrings and false for invalid substrings, and that searching for words that are not inserted in the trie returns false.

### Logic

#### Decrypter
The tests for the Decrypter class check handling of user input text and make sure that decryption of texts works as intended, that the result texts are what is expected. For example, the non-alphabetical characters should be unchanged in the result text, and if the input text contains nothing but special characters, the text is returned as it is. The actual text handling and key finding happens in other classes, TextHandler and KeyFinder, and these are tested separately.

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
The performance and accuracy testing is done using PerformanceTester and AccuracyTester classes. Both use TextFactory and Crypter classes for forming random test texts and encrypting them. The test texts are formed by picking random words from a word list (the same that is used for the actual program) and adding randomly formed error words to the text. The text is then encrypted and given to the Decrypter for decryption, and the computing time or accuracy of decryption with the original text is then measured and saved to an array. At the end, the average values of each test case are set into the output text field in the GUI.

The tests can be run from the GUI by clicking either of the buttons 'Performance tests' or 'Accuracy tests'. The running time for the tests is very long (A couple hours).

## Performance testing
The performance tests are run on an `AMD Ryzen 9 5900X` CPU with 32GB of RAM. The times are measured in nanoseconds, but the results that are printed are turned to milliseconds.

The tests are run for randomly formed ciphertexts of varying length or with increasing number of errors. The time is measured for the KeyFinder's findKey() function only (not for forming and sorting text arrays).

The decryption time depends largely on the amount of error words, but can still vary a lot between different texts. For this reason, using only one text for one test case might be very misleading. In the following tests, a random text is generated for each iteration. The time for decrypting a text is measured several times, and the median of these times is saved into the times array - then an average is counted from the medians of all the different texts of each test case.

#### Text preprocessing times
Here are average text preprocessig times in nanoseconds:
````````````````````````````````````````````
60 words average 178470 ns
100 words average 108420 ns
500 words average 241460 ns
1000 words average 917510 ns
5000 words average 9259390 ns
10000 words average 32999600 ns
````````````````````````````````````````````
This includes getting all the unique letters used in the text, getting character frequencies, forming a word array, sorting it and copying it to the second word array. Increasing text length will naturally increase preprocessing time.

#### Key finding times for test texts without errors
````````````````````````````````````````````
When all words are found in the dictionary: 
60 words average time ms: 279     min 1 max 631
100 words average time ms: 256    min 35 max 634
500 words average time ms: 45     min 2 max 134
1000 words average time ms: 185   min 9 max 446
5000 words average time ms: 214   min 17 max 452
10000 words average time ms: 210  min 9 max 446
````````````````````````````````````````````
A new text is formed for each iteration, and then its decryption time is measured 11 times and a median of these times is saved into the array - the results show the mean of the medians of all the texts, and the smallest and largest times of the whole array.

The times are measured with System.nanotime(), but they are printed out in milliseconds. The decryption time varies a lot between the texts. I believe this might be due to what kind of words there are in the text and how well the cipher character frequencies match the expected frequencies. If some text has only small words, decrypting it might take a longer time, because small words have several possible translations and only few keys are discovered with each word, which then leads to more recursive calls if the first guesses are not right. Starting the decryption with long words decreases the decryption time significantly, because longer words tend to have fewer possible translations and several keys can be found with just one word, so there would be fewer calls of the recursive function.

The maximum length of text (the sum of the lengths of the words in word arrays) that is handled in the recursive function is 2000 characters. Therefore the time the recursive decryption function uses should not increase that much with longer texts. The text preprocessing might slightly increase the computing time as a whole, since the text is broken into word arrays that are sorted (the sorting function's time complexity is O(n log n)), but these times are not measured here.

#### Key finding times for random test texts with errors
``````````````````````````````````````````
All random error texts: 
0 errors average time ms: 200     min 4 max 823
5 errors average time ms: 1003    min 9 max 8301
10 errors average time ms: 878    min 36 max 2152
15 errors average time ms: 10790  min 6397 max 14363
20 errors average time ms: 9445   min 5997 max 12165
25 errors average time ms: 31049  min 25952 max 28605
``````````````````````````````````````````
All the texts with errors used in the tests have 200 words in total, including a changing number of randomly formed error words that are not expected to be found in the dictionary. The amount of words is the same because the length of the text that is processed is limited, and this way the error words are most likely included in the text that is used for decryption. An error word is formed by picking random integers between 97-122 which are then changed to chars. The error words have the length of 5-15 characters (this is also determined randomly) - I thought this would be a good approximation of general cases. The texts that are tested have 0, 5, 10, 15 and 20 error words. The test is performed 10 times for each test case, with a new random text formed for every decryption - the time for a single text is measured 11 times, and a median is taken from these. The average value above is the mean of all medians.

In general it can be stated that the amount of errors increases the decryption time. It is a bit odd that average time for texts with 5 errors is larger than for a text with 10 errors, and 15 errors time is larger than 20 errors - but the times vary a lot for different texts, which propably skewes the results. The time for 25 errors is expected to be rather high because there the initial error margin 10% is not enough, and attempting a decryption with a hint too small error margin takes some time. In this case, the 10% error margin is doubled, and the decryption is attempted again. The amount of errors allowed is then decreased until reaching the limit where decryption does not work again, and too small error margin is attempted for the second time, which increases performance time.

So the number of error words increases performance time, as does the length of the text. Here all the test texts are of the same length, but below are times for texts with different lengths and same amount of errors.

#### Key finding times for random texts with different length and same amount of errors
``````````````````````````````````````````
Run times for texts with 20 errors: 
60 words average time ms: 19769         min 11760 max 27815
100 words errors average time ms: 9003  min 5421 max 13765
200 words errors average time ms: 714   min 16 max 1527
500 words errors average time ms: 1485  min 315 max 3202
``````````````````````````````````````````
Here the decryption time actually decreses when the amount of errors stays the same and the amount of words increases. A new random text is formed for each round of each test case, and again the times vary a lot between different texts. The shortest time for 200 words is very short, and this might distort the results again - although the longest time is rather short as well. With five hundred words, probably not all of the words are included in the recursion, because the text length is limited and the shortest words are left out. With 60 words, error margin is very large, and initial 10% error margin will have to be increased twice, which increases the execution time. Same goes for 100 words. This might actually be why 200 words time is so good, because the initial error margin is just right.

#### Key finding times for the same text with increasing number of errors
````````````````````````````````````````````
Run times for the same text with increasing number of errors: 
0 errors average time ms: 802
5 errors average time ms: 878
10 errors average time ms: 1201
15 errors average time ms: 6743
20 errors average time ms: 2714
````````````````````````````````````````````
Here are the times for the same text with increasing number of errors. The text has 200 words. The time for each case is measured 10 times. Looking at the times measured for texts with 0 errors, this one has a rather long decryption time as baseline, 802 ms when the average for 10000 words was 145 ms. As mentioned, the decryption time varies a lot for different texts. It is also curious how the decryption time is lower for 20 errors than for 15 errors.

I also ran the texts for second time and got very different results:
````````````````````````````````````````````
Run times for the same text with increasing number of errors: 
0 errors average time ms: 27      min 27 max 28
5 errors average time ms: 31      min 31 max 32
10 errors average time ms: 11638  min 11627 max 11654
15 errors average time ms: 2916   min 2911 max 2921
20 errors average time ms: 2576   min 2568 max 2591
````````````````````````````````````````````
Here the initial decryption time is very short, and suddenly increases with 10 errors. I'm not sure what happened here, or why the time decreases after that - probably because of how the error words are distributed among the normal words. The overall time mostly depends on how fast the algorithm determines the last used error margin is too small, and this is discovered faster with some texts than with others.

This is why I don't think it is a good idea to make any generalizations based on just one text, but rather look at the distribution of times for different texts. Running test with larger amount of texts would be better, but very time consuming.

### The error margin
The times here are measured (in most cases) up to the 10% error margin. The dictionary that is used is very large (around 400 000 english words), so the error margin should not be very large for an average cipher text. This is of course very case sensitive. For any 'normal' text, almost all the words should be found in the dictionary, barring any misspelled words. In manual testing when decrypting more exotic texts (such as the Star Wars -texts in [sample texts](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/sample_texts.md)) the 10 percent error margin works surprisingly well.

## Accuracy testing
The AccuracyTester class implements accuracy tests that check the average accuracy of a decryption result with the original text. The accuracy is determined by comparing characters. The test texts are formed exactly like in performance testing, by picking random words from the wordlist and then using the Crypter class to encrypt the text. Each text has 100 words and 0, 5, 12, 14 or 21 error words, which are 5-15 characters long and also formed randomly. The test is run 10 times for every text in each class. The results are saved in arrays and the average values are counted from these.

``````````````````````````````````````````
Average accuracies:
texts with 0 errors: 1.0
texts with 5 errors: 1.0
texts with 12 errors: 0.999
texts with 14 errors: 0.999
texts with 21 errors: 1.0
``````````````````````````````````````````
The accuracy of decryptions is rather good, but there are a couple situations where problems arise and mistranslations happen.

The usual case is when a rare letter appears in a text only in words that are not in the dictionary (that is in error words). In this case, it is not possible to guarantee 100 % correct translation. After decrypting the words in a word array the algorithm will check if there are any ciphered letters that have not been substituted with a key value. If there are, a most likely key is chosen from the remaining available key values based on the frequency. This is correct if there is only one unused key left. If there are several, the chosen value might be correct or it might be wrong. If some less likely option is actually the correct key value, the result will be wrong, because the one whose frequency is closest to the ciphered letter's frequency will be chosen.

In manual testing, 'j' and 'z' tend to cause troubles - for several texts, neither appear in normal words and one of them appears in words that are not in the dictionary. It is curious that 'j' is usually chosen as a key value, because it has lower frequency (0.2%, vhen 'z' has 1.0%) and the ciphered character has very low frequency (around 0.2-0.3%) in the text since it appears only in one or few words. Again, this happened for several texts.

It is also possible to translate a text with 0% error margin, yet some letters have still been substituted wrong - they just don't cause any errors to the text. In this case there would have been several possible translations for the text, but since the decryption algorithm will stop at the first possible one, it might not be correct/intended one.
