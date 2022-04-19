# Project Specification

Degree programme: Bachelor of Science (Computer Science) (Tietojenkäsittelytieteen kandiohjelma ja maisteriohjelma)

The used programming language is Java, but I also understand Python.

The code, comments and documentation is in English (but my native language is Finnish).

### Data Structures and Algorithms
At least trie datastructure will be used and a backtracking algorithm. A trie tree will be created using arrays that contain nodes that contain the value of the node and an array of child nodes. Merge sort algorithm will be used for sorting the words.

### The Problem
The program will decrypt a simple-substitution cipher for a text that is written in English using frequency analysis. Trie data structure will be used to save a word list (a dictionary), from which the words are searched. Trie tree was chosen because it is faster to search through than e.g. a hashmap. User submitted ciphered text will be saved into two arrays as words and sorted by word length in descending order. A backtracking algorithm was chosen to try out character substitutions because it will go through all options if necessary. The execution time is shortened by sorting the words and trying character substitutions in the order suggested by the frequency analysis.

### Program Input and Its Use
The program takes a ciphered text as an input, analyzes the frequency of each letter and then stores it in a word array. Then using the known frequencies of letters in English the algorithm will try to substitute the ciphered letters according to their expected frequencies (e.g. the most common letter in the ciphered text will be replaced by the most common letter in English, i.e. 'e'). If this does not produce any sensical words, new substitution is tried out. The program gives the (hopefully) decrypted text as an output.

### Expected Time and Space Complexities of the Program
The time complexity of searching a string of length m from a trie is O(m). The space complexity of a trie with a dictionary the length of n will also be O(n). The time complexity of a recursive algorithm would be in the worst case scenario O(26!). The time complexity of merge sort is always O(n log n), and space comlexity is O(n).

### Sources
* [Backtracking](https://en.wikipedia.org/wiki/Backtracking)
* [Frequency Analysis](https://www.101computing.net/frequency-analysis/)
* [Merge Sort](https://raw.githubusercontent.com/pllk/tirakirja/master/tirakirja.pdf)
* [Substitution cipher](https://en.wikipedia.org/wiki/Substitution_cipher)
* [Trie](https://en.wikipedia.org/wiki/Trie)
* The dictionary_old was copied from here: https://github.com/dwyl/english-words, they state that the original source is this: https://web.archive.org/web/20131118073324/http://www.infochimps.com/datasets/word-list-350000-simple-english-words-excel-readable
* The other dictionary was copied from here: https://github.com/filiph/english_words/blob/master/data/word-freq-top5000.csv, with own additions
