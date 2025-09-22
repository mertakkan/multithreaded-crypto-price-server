
## Protocol Specification

The application uses a custom binary protocol for communication between the client and server.

**Message Structure:**
`MessageType` + `CryptoType` + `SocketAddress` + `RequestCount`

**Encoding Process:**

1.  The individual parts of the message are converted into byte arrays.
2.  These byte arrays are combined using a `ByteArrayOutputStream`.
3.  The combined byte array is then transformed into a hexadecimal string representation for transmission over the TCP socket.

**Message Types:**

| Type | Description                                  |
| :--- | :------------------------------------------- |
| `1`  | Price request for a specific cryptocurrency. |
| `2`  | Request for the list of all available cryptocurrencies. |
| `0`  | Disconnect the client and exit the application. |

## API Integration

-   **Service**: CoinGecko API v3
-   **Endpoints Used**:
    -   `/coins/list`: To retrieve the list of all available cryptocurrencies.
    -   `/simple/price`: To get the current price for specific coins.
-   **Currency**: All price data is returned in Turkish Lira (TRY).
-   **Rate Limiting**: The application is designed to make optimized requests to respect the API's rate limits.

## Key Implementation Details

-   **Concurrent Handling**: Each client connection is managed in a separate thread on the server, allowing for high concurrency.
-   **Error Management**: The application includes comprehensive exception handling for network-related issues and API errors.
-   **Data Validation**: Input is validated, and the server provides a "Coin Not Found" response for invalid cryptocurrency names.
-   **Memory Optimization**: The list of available coins is limited to the first 100 results to comply with TCP payload size restrictions and optimize memory usage.
-   **Connection Management**: Resources are cleaned up automatically, and connections are gracefully disconnected upon exit or timeout.

## Learning Outcomes

This project demonstrates proficiency in the following areas:

-   Socket programming and network communication in Java.
-   Developing multithreaded applications.
-   Integrating with RESTful APIs and processing JSON data.
-   Designing and implementing custom binary protocols.
-   Applying client-server architectural patterns.
-   Effective resource management and error handling.

## Future Enhancements

-   **Multi-Currency Support**: Add the ability for clients to request prices in different fiat currencies.
-   **Authentication**: Implement user authentication and session management for secure access.
-   **Historical Data**: Add functionality to query historical price data for cryptocurrencies.
-   **GUI Client**: Develop a graphical user interface for the client application for a more user-friendly experience.
-   **Database Caching**: Implement a database caching layer to reduce API calls and improve performance for frequent requests.

## License

This project is intended for educational and portfolio demonstration purposes.
