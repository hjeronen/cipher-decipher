# Implementation Document

## Project structure

### Database or the dictionary file
The dictionary or list of words is read from a file and constructed into a trie when the program starts.

### Trie
The words in the dictionary are saved in trie-tree, where each node has a character as its value and a list of child nodes. A word is therefore saved into the tree one letter in each node. The node with the final letter of the word is marked as final with a boolean value.

The words are searched by choosing from a node's children the one with the next letter in the word. The search starts from the root which has no value and progresses into its child that has the first letter of the word. If a node does not have a child with the required value, then the word is not in the tree.

### Decrypter
The decrypter class forms the dictionary into a trie when starting the program. The [decrypt(String text)](https://github.com/hjeronen/cipher-decipher/blob/7dc9964e83bb87116f5c0a79b595b6f798ffca05/cipher-decipher/src/main/java/logic/Decrypter.java#L113) function takes a ciphered text as an argument and decrypts it. First the text is changed to lower case and all non-letters are scrapped, then each word is saved into two arrays, one holding the words where substitutions are made and one the original ciphered words. The words are sorted from longest to shortest to speed up execution time since longer words have fewer possible decryptions. A certain percentage of the words is allowed to be erroneus, and this is increased if no suitable decryption is found.

The actual decryption is done with the [findDecryption(int j, int i, int errors)](https://github.com/hjeronen/cipher-decipher/blob/7dc9964e83bb87116f5c0a79b595b6f798ffca05/cipher-decipher/src/main/java/logic/Decrypter.java#L193) function by using backtracking and frequency analysis. A key value for a ciphered character is chosen based on the cipher character's frequency in the ciphered text. The function [getClosestAvailableKey(char[] used, double freq)](https://github.com/hjeronen/cipher-decipher/blob/7dc9964e83bb87116f5c0a79b595b6f798ffca05/cipher-decipher/src/main/java/logic/Decrypter.java#L300) returns the character with closest frequency that is still available and that has not been tried for this character yet. The key is changed into the cipher character's place and the recursion moves to the next character in the word. If it looks like the key is not a suitable substitution, it is marked into the list of tried keys and a new key is fetched.

The uninformatively named argument j marks the index of a character in the word that is being decrypted, and the argument i marks the index of the ciphered word in the array where the ciphered text was saved as words. When all the characters in the word are changed, or the j is the same as word length, the recursion checks if the word is found and in this case moves to the next word. If the word is not found, the recursion backtracks and tries new key values for the characters.

If all the available key values have been tried and no suitable combination has been found, the word is marked as error, and the recursion moves to the next word. If there are too many errors, recursion will backtrack to the last working point.

### GUI
The userinterface is done with JavaFX and Swing because that seems to be the only one that works for my computer and Gradle installation. When the program starts the 'input' and 'output' texts indicate which textfield the user can paste the ciphered text and where the decrypted text will be printed. By pressing the 'Decrypt' button, the ciphered text in the upper field will be passed to the decryption class, and the result string will be placed to the lower field.

## Implemented time and space complexities

## Flaws and improvements

## Slightly erroneous translations
So far I have identified three cases where slightly erroneous translations might occur:

1. If the error margin is too large, this might let through some erroneous translations. For example, if a word 'mountain' is mistranslated 'fountain', the 'f' might cause some translation errors for the rest of the words in the text, but if they do not break the limit, the set key values are accepted.

2. If there are letters that only occur in the text in words that are not found in the dictionary, these might be mistranslated because it is not possible to determine the absolutely correct value. Initially, these do not get translated at all, but after suitable key values are found for all other characters, the algorithm will check if there are any that are still unsubstituted, and will 'guess' a translation for these by choosing from the remaining available key values the key whose frequency is closest to the cipher letter's frequency. This might or might not be correct.

3. If some cipher letters are chosen technically wrong key values (not the ones intended), but these produce valid words that are found in the dictionary, and the text is translated successfully with 0 % error margin, the translation is slightly erroneous - that is, error free but not what was originally intended. In this case, there would have been several possible translations for the entire text (with 0 % error margin), but the algorithm will stop at the first successful one.

Case 1 is attempted to fix with tuning the error margin. In cases two and three, the program has no way of knowing the right translation - one possible fix would be to search for all possible translations and let the user choose the right one, but this solution is not implemented here. These cases are rather rare and depend on the text that is translated, what kind of words there are and how long the text is.

All these errors are rare but possible, occurring mostly with short texts.

## Performance time
The decryption time is very short if error margin is 0, just right for the text or even too large, but if the error margin is too small, determining this takes rather long time. This increases performance time because the right error margin is found by increasing and decrasing the margin and attempting the translation again. This might be resolved by keeping track which words cause errors and only resubstituting the characters that occur in them.

## Sources
* [Backtracking](https://en.wikipedia.org/wiki/Backtracking)
* [Frequency Analysis](https://www.101computing.net/frequency-analysis/)
* [Substitution cipher](https://en.wikipedia.org/wiki/Substitution_cipher)
* [Trie](https://en.wikipedia.org/wiki/Trie)
* The dictionary_long was copied from here: https://github.com/dwyl/english-words, they state that the original source is this: https://web.archive.org/web/20131118073324/http://www.infochimps.com/datasets/word-list-350000-simple-english-words-excel-readable
* The other dictionary was copied from here: https://github.com/filiph/english_words/blob/master/data/word-freq-top5000.csv, with own additions
