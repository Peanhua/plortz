# Testing

## Unit testing
JUnit is used for unit testing. The tests can be run with:
```
mvn test
```

### Code coverage
Code coverage report can be generated using Jacoco:
```
mvn jacoco:report
```
The user interface code (everything under plortz.ui) is excluded from the code coverage report.

There is a lack of unit tests performed on certain areas that are considered unimportant:
* The main class responsible of starting the program.
* Not implemented methods, that just throw an "UnsupportedOperation" exception.
* Overrides of toString() method, those are used only for debugging purposes.
* Some simple getter and setter methods.


## Manual testing
Manual testing has been performed mostly using the command line user interface, as it allows using scripts. For example:
```
echo -e 'new 500 500\nperlin 1 0.0125\nperlin 0.5 0.025\nperlin 0.25 0.05\nperlin 0.125 0.1\nperlin 0.0625 0.2\nsave foo.tga\n' | mvn compile exec:java -Dexec.args="--no-gui" && display foo.tga
```
