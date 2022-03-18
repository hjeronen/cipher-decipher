# Project Specification

Degree programme: Bachelor of Science (Tietojenkäsittelytieteen kandiohjelma ja maisteriohjelma)

The used programming language is Java, but I also understand Python.

The code, comments and documentation is in English (but my native language is Finnish).

### Data Structures and Algorithms
At least trie datastructure will be used and a backtracking algorithm.

### The Problem
Using frequency analysis to decrypt a substitution cipher for a reasonably long text that is written in English. Trie data structure will be used to save a word-list and the words in a ciphered text, which will then be compared. Trie-tree is used because it is faster to search through than e.g. a hashmap. A backtracking algorithm is used to try out character substitutions.

### Program Input and Its Use
The program takes a ciphered text as an input, analyze the frequency of each letter and then store it in a trie-tree. Then using the known frequencies of letters in English the algorithm will try to substitute the ciphered letters according to their expected frequencies (e.g. the most common letter in the ciphered text will be replaced by the most common letter in English, i.e. 'e'). If this does not produce any sensical words, new substitution is tried out. The program gives the (hopefully) decrypted text as an output.

### Expected Time and Space Complexities of the Program
The time complexity of searching a string of length m in a trie-tree is O(m).

### Sources
