# Implementation Document

## Project structure

The UML diagram of the project structure:

![class_diagram](https://user-images.githubusercontent.com/73843204/168473232-b851425c-4171-4336-b382-399cf838409e.png)

### Database or the dictionary file
The dictionary or list of words is read from a file and constructed into a trie when the program starts. (Not included in the class diagram.)

### Domain

#### Trie and Node
The words in the dictionary are saved in trie-tree, where each node has a character as its value and a list of child nodes. A word is therefore saved into the tree one letter in each node. The node with the final letter of the word is marked as final with a boolean value.

The words are searched by choosing from a node's children the one with the next letter in the word. The search starts from the root which has no value and progresses into its child that has the first letter of the word. If a node does not have a child with the required value, then the word is not in the tree.

### Logic

#### Decrypter
The Decrypter uses TextHandler for processing the input text, and KeyFinder to find a suitable key for decrypting the text. The method [decrypt(String text)](https://github.com/hjeronen/cipher-decipher/blob/defd0a9e2d8f6e0efaafded9ade2abbc38ef7e4c/cipher-decipher/src/main/java/logic/Decrypter.java#L47) takes a ciphered text as an argument and returns it decrypted. The Decrypter will load the dictionary into a Trie and pass it to the KeyFinder upon creation. It uses TextHandler for forming the arrays and Strings that are required for decrypting the text, and passes these to the KeyFinder. The TextHandler is also used for substituting the ciphered characters in the text with the found key values.

#### TextHandler
The TextHandler does all the text processing. It is used by the Decrypter to clean out all unwanted characters, find all the unique characters that are used in the ciphered text, find character frequencies and produce wordlists that do not exceed the text length limit and sort these alphabetically and by length (descending order). For sorting the word list the TextHandler uses the Sorter class.

#### Sorter
The Sorter sorts a given word list first alphabetically and then by length in descending order. Merge sort algorithm is used for both cases. Sorting the words and starting the decryption with the longest words improves decryption time since longer words have fewer possible translations and more key values are found with one word.

#### KeyFinder
The KeyFinder finds the key that is used to decrypt the cipher text. To do this it needs a String containing all the unique cipher letters that are used in the text, a String array of ciphered words and a StringBuilder array that is a copy of the cipher word array and where the character substitutions are made, and a double array that contains the frequencies of the characters in the cipher text.

The function [findKey(String[] cipherwords, StringBuilder[] words, String cipherLetters, double[] cipherFrequencies)](https://github.com/hjeronen/cipher-decipher/blob/defd0a9e2d8f6e0efaafded9ade2abbc38ef7e4c/cipher-decipher/src/main/java/logic/KeyFinder.java#L138) will set up the required arrays and use [testKeyValues(int j, int i, int errors, int[] allErrorWords)](https://github.com/hjeronen/cipher-decipher/blob/defd0a9e2d8f6e0efaafded9ade2abbc38ef7e4c/cipher-decipher/src/main/java/logic/KeyFinder.java#L247) to find key values for ciphered characters. Backtracking and frequency analysis are used to try out character substitutions. A key value for a ciphered character is chosen based on the cipher character's frequency in the ciphered text. The function [getClosestAvailableKey(char[] used, double freq)](https://github.com/hjeronen/cipher-decipher/blob/defd0a9e2d8f6e0efaafded9ade2abbc38ef7e4c/cipher-decipher/src/main/java/logic/KeyFinder.java#L349) returns the key value with closest frequency that is still available and that has not been tried for this character yet. The key is changed into the cipher character's place and the recursion moves to the next character in the word. If it looks like the value is not a suitable substitution, it is marked into the list of tried keys and a new key value is fetched.

In testKeyValues() the uninformatively named argument j marks the index of a character in the word that is being decrypted, and the argument i marks the index of the cipher word in the array where the ciphered text was saved as words. When all the characters in the word are changed, or the j is the same as word length, the recursion checks if the word is found from the dictionary and in this case moves to the next word. If the word is not found, the recursion backtracks and tries new key values for the characters.

If all the available key values have been tried and no suitable combination has been found, the word is marked as an error, and the recursion moves to the next word. If there are too many errors, recursion will backtrack to the last working point.

The decryption is first attempted with 0 percent error margin, then with 10 percent if unsuccessful. If no possible key is found with this error margin, the percentage is doubled. If 10 percent is large enough error margin (for most cases it is) then the amount of allowed errors (this.maxErrors), that is the amount of words that are not found in the dictionary with certain character substitutions, a.k.a key, is decreased by one. The error margin tuning is done this way because the testKeyValues(), the backtracking algorithm, will finish very fast if error margin is large enough to find some solution. If error margin is too small, the backtracking algorithm will take forever before it decides it is not working out. Therefore, attempting decryption with too small error margin should happen as few times as possible. When tuning the error margin percentage with binary search, the percentage is often too small for decryption, and the total time of execution increases. By lowering the amount of error words by 1 after a high enough error margin is found and repeating this each round until a too small amount of error words is discovered minimizes the amount of decryption attempts with too small error margin, providing a much better execution time than binary search without sacrificing accuracy.

When a suitable key is found, it is returned to the Decrypter class, which passes it to the TextHandler that uses it to replace all the characters in ciphered text with the key values.

### Util
The util package holds the separate tester programs and their helper classes.

#### PerformanceTester and AccuracyTester
There are separate classes for running performance and accuracy tests for the algorithm. These are PerformanceTester and AccuracyTester. Both use TextFactory to create a random test text, which is encrypted with Crypter.

#### TextFactory
The TextFactory will save the used dictionary in a String array, then form test texts by randomly picking words from the list and adding them to the result text that is then returned. The desired amount of error words is also added to the text by randomly picking the error word length (5-15 characters), then picking this amount of random characters and combining them to a word that is added to the text. The error words are added at the end of the text, but once the words are sorted by length, they will be spread out throughout the word list. The length of the text and the amount of error words are given as parameters.

#### Crypter
The Crypter class is used to encrypt a test text that was formed with TextFactory. The Crypter will form a new key by picking cipher values at random and then replace the characters in the given text with these values.

### Ui
The program only has graphical ui that is created with the class GUI.

#### GUI
The graphical user interface is done with JavaFX and Swing because that seems to be the only one that works for my computer and Gradle installation. When the program starts the 'input' and 'output' texts indicate which textfield the user can paste the ciphered text and where the decrypted text will be printed. By pressing the 'Decrypt' button, the ciphered text in the upper field will be passed to the decryption class, and the result string will be placed to the lower field.

#### Listener and TesterListener
There are separate listeners for the decryption button and the buttons that run the tester programs. The Listener class will pass the user's input text for the Decrypter and set the result text at the output text field. The tester buttons will call the respective tester classes and their method that runs the tests, then set the results to the output text field. The TesterListener uses Tester interface that is implemented by both tester classes.

## Implemented time and space complexities
The time complexity of searching a string of length m from a trie is O(m). The space complexity of a trie with a dictionary the length of n will also be O(n). The time complexity of a recursive algorithm would be in the worst case scenario O(26!n), n being the amount of words - I'm not at all sure how to estimate this, however, because one function call might cause 0-26 more calls, and one additional if word is marked as error. The time complexity of merge sort is always O(n log n), and space comlexity is O(n).

## Flaws and improvements

### Slightly erroneous translations
So far I have identified three cases where slightly erroneous translations might occur:

1. If the error margin is too large, this might let through some erroneous translations. For example, if a word 'mountain' is mistranslated 'fountain', the 'f' might cause some translation errors for the rest of the words in the text, but if they do not break the limit, the set key values are accepted.

2. If there are letters that only occur in the text in words that are not found in the dictionary, these might be mistranslated because it is not possible to determine the absolutely correct value. Initially, these do not get translated at all, but after suitable key values are found for all other characters, the algorithm will check if there are any that are still unsubstituted, and will 'guess' a translation for these by choosing from the remaining available key values the key whose frequency is closest to the cipher letter's frequency. This might or might not be correct.

3. If some cipher letters are chosen technically wrong key values (not the ones intended), but these produce valid words that are found in the dictionary, and the text is translated successfully with 0 % error margin, the translation is slightly erroneous - that is, error free but not what was originally intended. In this case, there would have been several possible translations for the entire text (with 0 % error margin), but the algorithm will stop at the first successful one.

Case 1 has been attempted to fix by tuning the error margin - once a working error margin has been found, the number of allowed error words decreases by one after each try until the decryption fails, so this sort of translation errors should not occur anymore. In cases 2 and 3, the program has no way of knowing the right translation - one possible fix would be to search for all possible translations and let the user choose the right one, but this solution is not implemented here. These cases are rather rare and depend on the text that is translated, what kind of words there are and how long the text is.

### Performance time
The decryption time is very short if error margin is large enough to find some answer, but if the error margin is just a hint too small, determining this takes a rather long time. This is why it is better to try to avoid attempting decryption with too small error margin too many times, and the amount of errors is slowly decreased once high enough limit is found at first. Still the decryption has to be attempted with a too small error margin at least once to decide the right error margin has been found, and twice if initial 10% error margin was not high enough. This is improved by keeping track of the error words: when a key is successfully found with some error margin, it seems the actual error words are properly detected, plus some actually correct words have been mismarked as errors (because too big error margin will let through slight translation errors). In later iterations it is checked if a word has been identified as an error before, otherwise it cannot be marked as error now. This improves the performance time a lot for too small error margin, because then correctly translated words cannot be marked as errors when the error margin fails for the rest of the text.

There might be a slight chance this causes errors in translation, if some error word is at first mistranslated as a proper word, and whatever errors it has caused have not broken the limit, and a working key is found with it. Then the word cannot be marked as an error later on with smaller error margin, and an erroneous translation for the text is returned. It seems very unlikely, however, that a long error word would be mistranslated as a proper word without it causing a significant amount of errors in the rest of the text.

## Sources
* [Backtracking](https://en.wikipedia.org/wiki/Backtracking)
* [Frequency Analysis](https://www.101computing.net/frequency-analysis/)
* [Merge Sort](https://raw.githubusercontent.com/pllk/tirakirja/master/tirakirja.pdf)
* [Substitution cipher](https://en.wikipedia.org/wiki/Substitution_cipher)
* [Trie](https://en.wikipedia.org/wiki/Trie)
* The [dictionary](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/src/main/resources/dictionary.txt) was copied from here: https://github.com/dwyl/english-words, they state that the original source is this: https://web.archive.org/web/20131118073324/http://www.infochimps.com/datasets/word-list-350000-simple-english-words-excel-readable
