
# languagetool-dictionarybuilder-gui
A program with GUI to create dictionaries for languagetool.org API. A dictionary is used in the API for spell checking and word suggestions.


# Warning: This program has not been created by the developers of languagetool.org.

You can find languagetool's official repository [here.](https://github.com/languagetool-org/languagetool)

If you just want to run it, download `dictionarybuilder.jar`. Then a simple `java -jar dictionarybuilder.jar` will make it run.

GUI Preview:

![preview](https://i.imgur.com/4mOGiCT.png)

*Info file path:* The `info` file. Usually in folder `\src\main\resources\org\languagetool\resource\<language>\hunspell\<language>.info`.

The greek one looks like this:

    #
    # Dictionary properties.
    # Converted from Greek hunspell file,
    # see README_el_GR.txt for license info.
    #
    
    fsa.dict.separator=+
    fsa.dict.encoding=iso-8859-7
    
    fsa.dict.encoder=SUFFIX

*Input Dictionary File:* A text file with all the words of the language. Each word in a new line.

*Output Dictionary File:* The `.dict` file that is going to be generate it. After generation, place it into `\src\main\resources\org\languagetool\resource\<language>\hunspell\<language>.dict`

