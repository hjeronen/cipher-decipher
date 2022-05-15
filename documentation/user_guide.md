# User guide

## Program execution
The program can be executed by running the jar file (loadable as a [release](https://github.com/hjeronen/cipher-decipher/releases/tag/Tiralabra)), running it from an IDE or from command line with command `gradle run`.

### Initial view

![initial_view](https://user-images.githubusercontent.com/73843204/167311808-4917ea38-660e-4f08-be9f-d31c41580017.png)

NOTE: if the window gets too small to show the whole text, drag it bigger or double click it from the top bar.

### Input and output
The program accepts a text as an input from the user. The text can be copy-pasted to the input text field in graphical user interface. The text is then decrypted by pushing the button 'Decrypt', and the decrypted text is printed in the output textfield.

The ciphered text is presumed to be an encryption from a text that is originally written in english - other languages are not supported. It is also presumed that normal alphabet characters a-z have been used in the encryption - the result text will also be constructed with normal alphabets a-z. Therefore, special letters like á or ñ might cause an error, or just be ignored. Other than that, it does not matter if the text contains special characters ('[].,()?!+&%' etc.) since these will be ignored. There is no limit for the length of the text that is written to the window. The length of the text that is processed will be cut down by the program, but once a working key is found, it will be used to decrypt the whole text.

The decryption will stop at the first possible solution - this might not be the only possible translation, however. Therefore, the text should be long enough to produce an accurate translation, a few words or one sentence are not enough. A possible solution is found for these, too, but it might not be the correct one. There are also some other cases where slight errors occur, see the [implementation document](https://github.com/hjeronen/cipher-decipher/blob/main/documentation/implementation_document.md) for these scenarios (under section 'Flaws and improvements').

### Decryption view

![decryption_view](https://user-images.githubusercontent.com/73843204/167311836-aeab7417-f1eb-4d5c-9e91-2af429e8f9c4.png)

## Performance and accuracy testing
Performance and accuracy tests can be run from the GUI by pressing the button 'Performance tests' or 'Accuracy tests'. The results are printed in the lower 'output' textfield, but running these tests will take a very long time.
