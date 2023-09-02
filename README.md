# Crypto Coin List - Android Application - Kotlin
Sample android app using mvvm clean architecture with Hilt DI

## Architecture
This application follows basic SOLID Principles and clean code approach and also uses MVVM pattern.
## Modularization
This application follows modularization strategy know as "by layer"
## Features
A list of technologies/ features used within the project:
* [MVVM]()
* [Clean Code]()
* [Jetpack Compose]()
* [Compose Navigation Component]()
* [Compose Navigation Args]()
* [Lazy Column]()
* [Flow, StateFlow]()
* [Hilt]()
* [Retrofit]()
* [Mockito, MockWebServer]()
* [JUnit4]()
* [Coin paprika API service](https://api.coinpaprika.com/v1/coins)

## Functionality
1. When the app is started, load and display a list of coins.
2. Order entries by name.
3. Filter the list based on tags.
4. Display any extra coin info in some kind of popup/fragment when a coin entry is clicked, based on the second API request, with the said ID as the query.
5. Provide some kind of refresh option that reloads the list.
6. Display an error message if the list cannot be loaded (e.g., no network).
7. Instrumentation and unit tests are implemented. (Hilt DI used for instrumentation testing)
8. ** Coin list entries restricted to 8 items, due to the restrictions over the limit of api calls. Please modify it using Constants.DISPLAY_LIST_ITEMS
9. ** Instrumentation tests - 12 nos. Unit tests - 17 nos


# Prerequisites
1. Compiler android sdk 33
2. Java sdk 11
3. Gradle 7.5



