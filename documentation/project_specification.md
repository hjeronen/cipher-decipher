# Project Specification

Degree programme: Bachelor of Science (Computer Science) (Tietojenkäsittelytieteen kandiohjelma ja maisteriohjelma)

The used programming language is Java, but I also understand Python.

The code, comments and documentation is in English (but my native language is Finnish).

### Data Structures and Algorithms
At least trie datastructure will be used and a backtracking algorithm. A trie tree will be created using arrays that contain nodes that contain the value of the node, the parent and an array of child nodes.

### The Problem
The program will decrypt a simple-substitution cipher for a text that is written in English using frequency analysis. Trie data structure will be used to save a word list (or some short dictionary) and the words in a ciphered text, which will then be compared. Trie tree was chosen because it is faster to search through than e.g. a hashmap. A backtracking algorithm was chosen to try out character substitutions because it will go through all options if necessary in a reasonable time.

### Program Input and Its Use
The program takes a ciphered text as an input, analyzes the frequency of each letter and then stores it in a trie tree. Then using the known frequencies of letters in English the algorithm will try to substitute the ciphered letters according to their expected frequencies (e.g. the most common letter in the ciphered text will be replaced by the most common letter in English, i.e. 'e'). If this does not produce any sensical words, new substitution is tried out. The program gives the (hopefully) decrypted text as an output.

### Expected Time and Space Complexities of the Program
The time complexity of searching a string of length m in a trie tree is O(m), but the strings will be short - the longest word in English is 45-letters (though it might not be in the used word list). The trie tree containing a word list in English has therefore the max depth of 45, and each level has the maximum of 26^i nodes, i being the number of the level. Therefore, the maximum possible size of this trie tree is the sum of 26^i, i = 0...45. This is quite a large number and the maximum possible size of the dictionary tree, though in reality each 26 letters will not have 26 children (there is no words like 'xlgj' or 'woaei'). The cipher tree will most likely be much smaller unless it contains every word from the word list (or the dictionary).

If n is the number of different characters used in the cipher text, each of which will appear at maximum once per each node on each level, given the depth m which is the length of the longest used word, therefore the number of levels i in the tree (so m = i), the space complexity of cipher-tree will be O(sum n^i, i = 1...45). This would also be the time complexity of going through the cipher tree once with recursive algorithm. The value of a node may be changed 26 times at most, and each of its children have 25 possible values, and their children 24 and so on, so the recursion is repeated at most 26 * 25 * 24 * ... * 1 = 26! times. However, this will not depend on user input - also the recursive function will loop through the children of each node, and there are at most 26 children per node, so this does not depend on the input length either and can be considered to happen in constant time. So the time complexity of the backtracking is also O(sum n^i, i = 1...45).

Forming a trie tree will require looping through the input text, handling each character in it - so if the length of the text is l, then the time complexity would be O(l). Because the maximum number of different characters used is 26, the previously mentioned n (different characters in cipher text) is at most 26 and n <= l, then the largest time complexity for the algorithm is O(l) (except if l is not significantly larger than n).

### Sources
* [Backtracking](https://en.wikipedia.org/wiki/Backtracking)
* [Frequency Analysis](https://www.101computing.net/frequency-analysis/)
* [Substitution cipher](https://en.wikipedia.org/wiki/Substitution_cipher)
* [Trie](https://en.wikipedia.org/wiki/Trie)
