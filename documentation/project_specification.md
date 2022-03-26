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
The time complexity of searching a string of length m with alphabet size of d in a trie tree is O(dm), but here we know the alphabet size is 26 and the strings will be at most 45 characters, which is the length of the longest word in English (though it might not be in the used dictionary). The time complexity of going through the trie tree with recursive algorithm is then O(nm), where n is the number of words and the m is the length of the longest word (so this would be O(n * 45) = O(n)).

The trie tree containing a word list in English has therefore the max depth of 45, and each level has the maximum of 26^i nodes, i being the number of the level. Therefore, the maximum possible size of this trie tree is the sum of 26^i, i = 0...45, so O(sum 26^i, i = 0...45). This is quite a large number and the maximum possible size of the dictionary tree, though in reality each 26 letters will not have 26 children (there is no words like 'xlgj' or 'woaei'). The cipher tree will most likely be much smaller unless it contains every word from the dictionary.

Forming a trie tree will require looping through the input text, handling each character in it - so if the length of the text is l, then the time complexity would be O(l).

### Sources
* [Backtracking](https://en.wikipedia.org/wiki/Backtracking)
* [Frequency Analysis](https://www.101computing.net/frequency-analysis/)
* [Substitution cipher](https://en.wikipedia.org/wiki/Substitution_cipher)
* [Trie](https://en.wikipedia.org/wiki/Trie)
* The used dictionary was copied from here: https://github.com/dwyl/english-words, they state that the original source is this: https://web.archive.org/web/20131118073324/http://www.infochimps.com/datasets/word-list-350000-simple-english-words-excel-readable
