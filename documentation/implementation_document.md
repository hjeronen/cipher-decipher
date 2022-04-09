# Implementation Document

## Project structure

### Database or the dictionary file
The dictionary or list of words is read from a file and constructed into a trie when the program starts.

### Trie
The words in the dictionary are saved in trie-tree, where each node has a character as its value and a list of child nodes. A word is therefore saved into the tree one letter in each node. The node with the final letter of the word is marked as final with a boolean value.

The words are searched by choosing from a node's children the one with the next letter in the word. The search starts from the root which has no value and progresses into its child that has the first letter of the word. If a node does not have a child with the required value, then the word is not in the tree.

### Decrypter
The decrypter class forms the dictionary into a trie when starting the program. The decrypt(string) function takes a ciphered text as an argument and decrypts it. First the text is changed to lower case and all non-letters are scrapped, then each word is saved into two arrays, one holding the words where substitutions are made and one the original ciphered words.

The actual decryption is done with the findDecryption(int j, int i) function by using backtracking and frequency analysis. A key value for a ciphered character is chosen based on the cipher character's frequency in the ciphered text. The function getClosestAvailableKey(used, freq) returns the character with closest frequency that is still available and that has not been tried for this character yet. The key is changed into the cipher character's place and the recursion moves to the next character in the word. If it looks like the key is not a suitable substitution, it is marked into the list of tried keys and a new key is fetched.

The uninformatively named argument j marks the index of a character in the word that is being deciphered, and the argument i marks the index of the ciphered word in the array where the ciphered text was saved as words. When all the characters in the word are changed, or the j is the same as word length, the recursion checks if the word is found and in this case moves to the next word. If the word is not found, the recursion backtracks and tries new key values for the characters.

If all the available key values have been tried and no suitable combination has been found, the word is marked as error, and the recursion moves to the next word. If there are too many errors, recursion will backtrack to the last working point. This backtracking will need to be improved, however.

## Implemented time and space complexities

## Possible flaws and improvements
At this moment the backtracking is too slow.

## Sources
