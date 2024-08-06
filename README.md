
# JavaFX Game with ESP8266 Controller

## Description

This project is a game developed in JavaFX where the player controls a circle that moves on a map filled with obstacles and enemies. The player control is implemented using a steering wheel made with an ESP8266 and an MPU6050. The player can also shoot projectiles in the direction of movement to eliminate enemies. The project is structured using the MVC pattern for better maintainability and organization of the code.

## Requirements

- Java 21 or higher
- JavaFX 21
- A WiFi network
- ESP8266
- MPU6050

## Setup

### Hardware

1. Connect the MPU6050 to the ESP8266.
2. Upload the provided code to the ESP8266.

### Software

1. Clone this repository:

```sh
git clone https://github.com/ismacarbo/Invaders.git
```

2. Open the project in an IDE (such as IntelliJ IDEA).

3. Ensure that the JavaFX dependencies are configured in the project. If using Maven, add the necessary dependencies to the `pom.xml` file.

4. Configure the JavaFX module path in the `module-info.java` file.

5. Run the `Main` application.

## Usage

- Move the player by tilting the ESP8266.
- Press the button on the ESP8266 to shoot projectiles in the direction of movement.
- Avoid obstacles and shoot enemies to eliminate them.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Make sure to update tests as appropriate.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
