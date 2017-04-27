# Social Music Network

System Software module coursework for second year undergraduate.

The software aims to be a social network using Music.

## Installation

The project is written in Java using a Maven build system. The project consists of three modules:
* client: The GUI client that the user uses.
* protocol: Contains the common protocol core. Client/Server event messages and the protocol implementation.
* server: The multi-threaded server that communicates with the client.

Since it is a Maven project, it can be built in command line using the ```mvn compile``` command.

You can also build using any IDE you want as Maven is IDE independant. Consult your IDE's documentation to know how to build a Maven project in your IDE.
