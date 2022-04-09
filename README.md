# cipher-decipher

([Data structures project 2022](https://tiralabra.github.io/2022_p4/index) repository)

A simple-substitution cipher decryption program. Does not work very efficiently yet. For testing the program, use [sample text](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/sample_text.txt) as input, and the smaller dictionary, which does not include 4 words that are used in the text, which is the exact amount of words that are allowed to be missing for this text. Will take about 30 sec to calculate, depending on hardware - results and time not guaranteed for other texts or with bigger dictionary.

There are currently two dictionaries for the program in this repo. The first, [dictionary_old](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/dictionary_old.txt) was copied from here: https://github.com/dwyl/english-words, but they state that the original was from here: https://web.archive.org/web/20131118073324/http://www.infochimps.com/datasets/word-list-350000-simple-english-words-excel-readable. The second, currently in use [dictionary](https://github.com/hjeronen/cipher-decipher/blob/main/cipher-decipher/dictionary.txt) was copied from here: https://github.com/filiph/english_words/blob/master/data/word-freq-top5000.csv, but I added some more words that were missing.

## Documentation
* [Project Specification](https://github.com/hjeronen/cipher-decipher/blob/main/documentation/project_specification.md)
* [Testing Document](https://github.com/hjeronen/cipher-decipher/blob/main/documentation/testing_document.md)
* [Implementation Document](https://github.com/hjeronen/cipher-decipher/blob/main/documentation/implementation_document.md)

### Weekly Reports
* [Week report 1](https://github.com/hjeronen/cipher-decipher/blob/main/documentation/week_report_1.md)
* [Week report 2](https://github.com/hjeronen/cipher-decipher/blob/main/documentation/week_report_2.md)
* [Week report 3](https://github.com/hjeronen/cipher-decipher/blob/main/documentation/week_report_3.md)
* [Week report 4](https://github.com/hjeronen/cipher-decipher/blob/main/documentation/week_report_4.md)
