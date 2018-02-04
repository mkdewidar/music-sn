# Music Social Network

System Software module coursework for second year undergraduate. The coursework was to create a social music platform using Java. Due to time constraints the project doesn't implement all the planned for features. The app features are influenced by Slack and Facebook.

## Features
These are the features that were implemented:
* User login and registration.
* Creating channels.
* Finding friends and sending friend requests.
* Accepting and rejecting friend requests.
* Making posts and seeing friends posts from the feed channel.
* Stable application; the client can reconnect to the server at any time.

## Installation

The project is written in Java using a Maven build system. The project consists of three modules:
* client: The GUI client that the user uses.
* protocol: Contains the common protocol core. Client/Server event messages and the protocol implementation.
* server: The multi-threaded server that communicates with the client.

Since it is a Maven project, it can be built in command line using the ```mvn compile``` command.

You can also build using any IDE you want as Maven is IDE independant. Consult your IDE's documentation to know how to build a Maven project in your IDE.
