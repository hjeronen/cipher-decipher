# Implementation Document

## Project structure

### Database or the dictionary file
The dictionary or list of words is read from a file and constructed into a trie when the program starts.

### Trie
The words in the dictionary are saved in trie-tree, where each node has a character as its value and a list of child nodes. A word is therefore saved into the tree one letter in each node. The node with the final letter of the word is marked as final with a boolean value.

The words are searched by choosing from a node's children the one with the next letter in the word. The search starts from the root which has no value and progresses into its child that has the first letter of the word. If a node does not have a child with the required value, then the word is not in the tree.

### Decrypter
The decrypter class forms the dictionary into a trie when starting the program. The [decrypt(String text)](https://github.com/hjeronen/cipher-decipher/blob/7dc9964e83bb87116f5c0a79b595b6f798ffca05/cipher-decipher/src/main/java/logic/Decrypter.java#L113) function takes a ciphered text as an argument and decrypts it. First the text is changed to lower case and all non-letters are scrapped, then each word is saved into two arrays, one holding the words where substitutions are made and one the original ciphered words.

The actual decryption is done with the [findDecryption(int j, int i, int errors)](https://github.com/hjeronen/cipher-decipher/blob/7dc9964e83bb87116f5c0a79b595b6f798ffca05/cipher-decipher/src/main/java/logic/Decrypter.java#L193) function by using backtracking and frequency analysis. A key value for a ciphered character is chosen based on the cipher character's frequency in the ciphered text. The function [getClosestAvailableKey(char[] used, double freq)](https://github.com/hjeronen/cipher-decipher/blob/7dc9964e83bb87116f5c0a79b595b6f798ffca05/cipher-decipher/src/main/java/logic/Decrypter.java#L300) returns the character with closest frequency that is still available and that has not been tried for this character yet. The key is changed into the cipher character's place and the recursion moves to the next character in the word. If it looks like the key is not a suitable substitution, it is marked into the list of tried keys and a new key is fetched.

The uninformatively named argument j marks the index of a character in the word that is being decrypted, and the argument i marks the index of the ciphered word in the array where the ciphered text was saved as words. When all the characters in the word are changed, or the j is the same as word length, the recursion checks if the word is found and in this case moves to the next word. If the word is not found, the recursion backtracks and tries new key values for the characters.

If all the available key values have been tried and no suitable combination has been found, the word is marked as error, and the recursion moves to the next word. If there are too many errors, recursion will backtrack to the last working point. This backtracking will need to be improved, however.

### GUI
The userinterface is done with JavaFX and Swing because that seems to be the only one that works for my computer and Gradle installation. When the program starts the 'input' and 'output' texts indicate which textfield the user can paste the ciphered text and where the decrypted text will be printed. By pressing the 'Decrypt' button, the ciphered text in the upper field will be passed to the decryption class, and the result string will be placed to the lower field.

## Implemented time and space complexities

## Possible flaws and improvements
At this moment the backtracking is too slow.

## Sources
* [Backtracking](https://en.wikipedia.org/wiki/Backtracking)
* [Frequency Analysis](https://www.101computing.net/frequency-analysis/)
* [Substitution cipher](https://en.wikipedia.org/wiki/Substitution_cipher)
* [Trie](https://en.wikipedia.org/wiki/Trie)
* The dictionary_old was copied from here: https://github.com/dwyl/english-words, they state that the original source is this: https://web.archive.org/web/20131118073324/http://www.infochimps.com/datasets/word-list-350000-simple-english-words-excel-readable
* The other dictionary was copied from here: https://github.com/filiph/english_words/blob/master/data/word-freq-top5000.csv, with own additions
