<h3 align="center">KTR - Mobile Communication - Packet Watcher App</h3>

<br />
  <a href="https://github.com/AK960/PacketWatcher_App"><strong>Explore the docs »</strong></a>
<br />
<br />
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#report">Report</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#to-dos">To-Dos</a></li>
    <li><a href="#bugs">Bugs</a></li>
  </ol>
</details>

## Report
In the following, I documented the process and status of the PacketWatcherApp-Project from this <a href="https://github.com/AK960/PacketWatcher_App.git">GitHub-Repo</a>. The goal was to design an Android Application that can send and receive TCP and UDP Packets. Additionally, I also used this Readme.md file to take personal notes on the topics that were subject of this project.

#### Directory structure  
The MainActivity.kt file lies under app/src/main/java/com/mobilkommunikation/project and calls on the package ./ui/screens which contains two files to render the screen. Within these files, the ./ui/components package is called which holds the elements that are contained inside of the scaffold, designed in the MainActivity. The package ./controllers contains the Kotlin files that hold the logical functions that implement the client and server functions. 

#### Layout Design
As said before, a Scaffold was designed which creates a topBar without further functionality and also a bottomBar that is used to switch tabs between the Client and Server View.
The Client-View contains a SegmentedControl field, that lets the user choose between the two protocols "TCP" and "UDP" to send a message to the server. The inputFields provide the interaction possibility to enter a message and the endpoint it is supposed to be send to. By interaction with the button below, the packet is transmitted and shall be shown in the outputField below. The Server-View works analogous. 

#### Logic
The interaction components provide the input options for the user to set up the desired connection. When interacting with the buttons to start a service, first the IP-Address and port number are checked regarding validity. When the check fails, an error message is shown on the screen. On success, the values are passed to the intended handlers, which perform case distinctions and pass on the values to the designated functions. 

#### Status Quo
In the following Notes section, the status quo of the project is noted down, formulating further to-dos and requirements. Also, the major bugs are listed as some notes regarding important variables and example functions.  

## Notes
### States and important Variables
| Variable Name                | Description                                                       | Description                                      |
|------------------------------|-------------------------------------------------------------------|--------------------------------------------------|
| `clientServerStateIndex`     | Client = 0, Server = 1                                            | Switching Tabs Client/ServerView                 |
| `selectedProtocolStateIndex` | TCP or UDP                                                        | Selecting Protocol in SegmentedControl           |
| `ipAddress`                  | Choosing IP Address to transmit to                                | Passing TextField input to transmission function |
| `portNumber`                 | Choosing connection endpoint                                      | Passing TextField input to transmission function |
| `tcpMessage`                 | Specifying message to transmit                                    | Passing TextField input to transmission function |
| `protocol`                   | Choosing transmission type, takes over selectedProtocolStateIndex | Passing TextField input to transmission function |

### Functions
#### Choosing Transmission Type based on State Variable
  - onProtocolSelected: (String) -> Unit
    - onProtocolSelected --> Name of the parameter, represents a function that is called later
    - (String) --> String is passed to the Lambda-Function, which contains selected protocol
      - Variable selectedProtocolStateIndex = it (it from if-statement) is passed ("TCP" or "UDP")
    - Unit --> Function does not return a value (synonym to void)
  - Function expects Lambda-Function as argument that passes some behavior/logic to the function
    - This is the term { selectedProtocolStateIndex = it }
      - it is the sole parameter of the function
    - Makes the function flexible: The logic for the selection of the protocol does not need to be defined 
    but is rather passed to the code by the calling code
  - Function calls the function SegmentedControl and passes the Lambda-Function as parameter
    - SegmentedControl expects Lambda-Function as argument for onOptionSelected
    - The onClick-function in SegmentedControl calls the lambda function on the current option
    - Thus, the value from selectedProtocolStateIndex is updated to "UDP" when interaction with the UDP button

## To-Dos
### Must <!-- Critical, must be delivered for project success -->
- Test Communication with external client / server
- Update Assignment Report

### Should <!-- Important but less time critial, can be implemented later --> 
- Erase label from input field after using it without loosing ability to stop server -> value as state variable in viewmodel? 
- Edit label to display input options
- Update Readme

### Could <!-- Desirable but not critical -->
- Changing App Theme & Colors

## Bugs
- Not yet known

## Theory Notes - General Programming
### Lambda Functions
Characteristics:
  - "Unit" represents an empty return value, like void
  - Lambda-Function can be passed to dictate the behavior of the higher order function
  - The value of the last expression is returned in the end
  - They do not have names and can be passed as values

Syntax: 
```sh
# overall: Gets two values and returns one
val operarion: (Type, Type) -> ReturnType = { param1, param2 -> someLogic }
# Lambda expression in general 
{ param1: Type, param2: Type -> return value }
# Examples:
val add: (Int, Int) -> Int = { a, b -> a + b } # Returns solution as int
val show: () -> Unit = { content -> Column ( ... ){ ... } }
public fun Scaffold(
  modifier: Modifier = Modifier,
  topBar: @Composable () -> Unit = {},
  (...)
  content: @Composable (paddingValues) -> Unit
) { innerPadding -> (...) } 
```

Notation:
Lambda functions can be passed to higher order functions as argument
```sh
fun functionName (
  onProtocolSelected: (String) -> Unit
) { someString -> 
    println(someString) 
}
# To call the function
onProtocolSelected { var = "someString" } 
# If a function only has one parameter it can be referenced by "it"
onProtocolSelected { var = it } 
```
If the Lambda-Function is the last argument of a function, the {} can be outside the argument block
```sh
listOf(1, 2, 3).forEach { println(it) }
# instead of
val list = listOf(1, 2, 3)
list.forEach(fun(item: int) {
  println(item)
}) 
```
The Lambda-Function can have default parameters
```sh
val greet: (String) -> Unit = { name -> println("Hello, $name!") }
greet("Max")
```
Last but not least a simple comparison to a regular function
```sh
# Normal
fun add(a: Int, b: Int): Int {
  return a + b
}
# Lambda
val add = { a: Int, b: Int -> a + b }
```

### Sockets
Sockets are connection endpoints which relay traffic between client- and server-processes. For that, they 
are identified by the combination of IP address and port number.
- Server Socket: To communicate with a server process, it must be possible to address it from the outside. 
Thus, it binds itself to a socket through which it can be accessed and listens for incoming connections. If 
it receives a connection it accepts it and blocks the process until the request comes in, reads it, processes 
it, and responds. Afterwards it waits for further requests until the clients closes the connection.
- Client Socket: The client does not need a fixed port but rather is assigned a free one. It then connects 
to the server, transmitting its own connection details through which it can be reached again. After 
transmitting the request, it waits for the servers response and listens on the specified port. Receiving 
the response, it either sends another request of closes the connection.
#### Connection Oriented (TCP)
Connection oriented protocols establish a connection before exchanging data. The workflow is as follows: 
##### Server Side
```sh
val ipAddress = "192.168.1.1"
val port = 8080
# 1. Create a ServerSocket
val serverSocket = ServerSocket(port)
# 2. Bind socket (optional)
serverSocket.bind(InetSocketAddress(ipAddress, port))
# 3. Queue connections (server calls listen() to queue incoming connections - handled automatically in Java/Kotlin
serverSocket.listen()
# 4. Accept connections
val clientSocket = serverSocket.accept()
# 5. Read and write data: as soon as a connection is established, Server can read data and send response
val input = clientSocket.getInputStream().bufferedReader().readLine()
val output = clientSocket.getOutputStream()
output.write("Hello Client\n".toByteArray())
# 6. Close Connection
clientSocket.close()
```
Note: The class ServerSocket already implements the binding of sockets to an address and a port internally. When calling
ServerSocket(), the socket is bound to the IP 0.0.0.0, which represents all available interfaces, and the specified port
by default. 
To provide an IP-address manually, call InetSocketAddress() - see above.
##### Client Side
```sh
# 1. Create a Socket
val socket = Socket()
# 2. Connect to server
socket.connect(InetSocketAddress(ipAddress, port))
# 3. Send data
val output = socket.getOutputStream()
output.write("Hello Server\n".toByteArray())
# 4. Receive data
val input = socket.getInputStream().bufferedReader().readLine()
println("Received from server: $input")
# 5. Close connection
socket.close()
```
#### Connectionless (UDP)
##### Server side


## Theory Notes - Jetpack Compose / Kotlin Specific
### States
Intro: Compose apps transform data into UI by calling composable functions. If the data 
changes, Compose re-executes the functions with the new data, creating an updated UI 
(recomposition). However, Compose does not track common variables and monitor its changes.
The functions "State" / "mutableStateOf" / "mutableIntStateOf" are interfaces that hold
values that are monitored by compose and trigger recompositions whenever their value changes. 

Remember: If such a value is defined within a composable, it is prone to unwanted recompositions.
When some other state-value changes, all state values would be reset. To preserve a state across
compositions, the mutable state can be remembered. The remember-function is used to guard a state
value against recomposition, so that it is not reset.
```sh 
@Compose
fun Function(...) {
  val myState = remember { mutableStateOf(false) } # accessing with "myState.value"
  ( ... )
}
# or:
fun Function(...) {
  # "by" -> accessing state with only "myState"
  # "mutableStateOf" -> state is remembered when rotating the screen
  var myState by rememberSavable { mutableStateOf(false) } 
  ( ... )
}
```
However, when calling the same composable from different parts of the screen, it will create different 
UI elements with their own version of the state - internal states can be thought of as private 
variables in a class.
User interactions with various interaction components can be dealt with using states.

State hoisting: The term refers to the principle that state values can be declared in parent functions
and passed on to child functions as lambda expression. To do so, action buttons like onClick() call
functions and pass a lambda phrase as parameter. This way, composable functions are more flexible and 
can better be reused for different views.
```sh 
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by remember { mutableStateOf(true) }
    Surface(modifier) {
        if (shouldShowOnboarding) { # state is true, hence OnboardingScreen is shown
            # passing function that tells OnboardingScreen to set state to false when button is clicked 
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false }) 
        } else {
            Greetings() # When state is set to false, Greetings are shown
        }
    }
}
@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit, # Taking in state variable as lambda 
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Code Lab!")
        Button(
            modifier = Modifier
                .padding(vertical = 24.dp),
            # onClick executes the lambda function and finally sets state value to false
            # new view is composed (@Compose tracks and remembers state variable)
            onClick = onContinueClicked 
        ) {
            Text("Continue")
        }
    }
}
```

### Coroutines
Coroutines are design patterns in Kotlin, equivalent to threads in Java, for handling asynchronous tasks. By sourcing out activities to 
background threads, we can make sure, that the main thread where the UI-functions are running on are not blocked when i.e. performing a 
network operation that waits for a result and blocks the thread it is running on for the time being.
A simple syntax to start a coroutine is the following: 
```sh
# simple coroutine with simulated activity (delay in ms)
fun startTcpServer (
    portNumber: Int = getAvailablePort()
): Job {
    return GlobalScope.launch(Dispatchers.IO) {
      # creating socket and server logic
    }
```
Coroutine Builders:
```sh
import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = GlobalScope.launch(Dispatchers.IO) {
        val data = fetchDataFromNetwork()
        withContext(Dispatchers.Main) {
            updateUI(data)
        }
    }
}

suspend fun fetchDataFromNetwork(): String {
    delay(1000) // Simulate network delay
    return "Data from network"
}

fun updateUI(data: String) {
    println("Updating UI with: $data")
}
```
- launch: starts a new coroutine. It is typically used for tasks that do not return a result or where you do not need to wait for the task to
complete.
  - Purpose: launch a new coroutine
  - Return Type: Job - can be used to manage coroutine
  - Use Case: Fire and forget tasks
  - Behavior: Runs concurrently with the rest of the code; non-blocking
- withContext: switches the context of the current coroutine. It is used for tasks where you need to get a result or when you need to switch
to a different dispatcher temporarily
  - Purpose: Change the coroutine context
  - Return Type: Result of the block of code
  - Use Case: Executing a block of code in a different context and getting the result
  - Behavior: Suspend the coroutine until the block completes; does not block the thread
- Jobs: manage / control the lifecycle of coroutines through certain methods (e.g. starting, stopping, querying the status). A job 
is returned when a coroutine is started (e.g. by launch). This job can be used to cancel the coroutine later or to check its status.

Coroutine Scopes: 
```sh
# GlobalScope
fun fetchData() {
    GlobalScope.launch {
        // Long-running operation
        val data = fetchDataFromNetwork()
        println("Data fetched: $data")
    }
}
suspend fun fetchDataFromNetwork(): String {
    // Simulate network call
    delay(2000)
    return "Network Data"
}
# CoroutineScope
## simple
fun handleStartClientInteraction() {
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    coroutineScope.launch {
        when (protocolSelected) {
            "TCP" -> startTcpClient(ipAddress, portNumber, tcpMessage)
            "UDP" -> startUdpClient(ipAddress, portNumber, tcpMessage)
        }
    }
}

suspend fun startTcpClient() {
    return withContext(Dispatchers.IO) {
        println("Hello from TCP-Client")
    }
}

## with Viewmodel
class MyViewModel : ViewModel() {
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun fetchData() {
        viewModelScope.launch {
            val data = fetchDataFromNetwork()
            println("Data fetched: $data")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    suspend fun fetchDataFromNetwork(): String {
        return withContext(Dispatchers.IO) {
            // Simulate network call
            delay(2000)
            "Network Data"
        }
    }
}
```
- GlobalScope: is a global scope that launches top-level coroutines that are not bound to any specific lifecycle / view and run independently. 
They are long-running and live until the application is terminated execution is completed.
  - Unbounded Lifespan: continue to run until application terminates
  - Not tied to lifecycle: ideal for long-running operations that need to continue regardless of the current scope or lifecycle
  - Resource Management: can lead to potential memory leaks if not managed carefully
- CoroutineScope: provides a structured way to manage coroutines, ensuring that they respect the lifecycle of the scope they are launched in.
It is tied to a lifecycle, such as an Activity or ViewModel and ensures coroutines are cancelled when the lifecycle is destroyed.
  - Lifecycle-aware: automatically cancelled / destroyed when scope is destroyed
  - Structured concurrency: ensures coroutines are properly managed and exceptions are propagated
  - Resource management: helps prevent memory leaks by cancelling coroutines when they are no longer needed
- runBlocking: While coroutineScope must be called from within another coroutine, runBlocking can launch a coroutine from outside an existing
coroutine. The coroutine launched by runBlocking will block the current thread.

### ViewModels
ViewModel is a class that exposes the state of the user interface and encapsulates the associated
logic. Its principal advantage is that it caches state and persists it through configuration changes. 
This means that your UI does not have to fetch data again when navigating between activities.

## Useful Links and Material
[IBM Socket Programming Infos][ibm socket-programming infos]
[IBM Socket Programming Book][ibm socket-programming book]

[ibm socket-programming infos]:https://www.ibm.com/docs/de/i/7.5?topic=communications-socket-programming
[ibm socket-programming book]:https://www.ibm.com/docs/de/ssw_ibm_i_75/pdf/rzab6pdf.pdf
