<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]
-->

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/AK960/PacketWatcher_App">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">KTR - Mobile Communication - Packet Watcher App</h3>

  <p align="center">
     ### TODO: ADD PROJECT DESCRIPTION ###
    <br />
    <a href="https://github.com/AK960/PacketWatcher_App"><strong>Explore the docs »</strong></a>
    <br />
    <br />
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#to-dos">To-Dos</a></li>
    <li><a href="#bugs">Bugs</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
  </ol>
  
</details>

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
    - Makes the function flexible: The logic for the selection of the protocol does not need to be defined but is rather passed to the code by the calling code
  - Function calls the function SegmentedControl and passes the Lambda-Function as parameter
    - SegmentedControl expects Lambda-Function as argument for onOptionSelected
    - The onClick-function in SegmentedControl calls the lambda function on the current option
    - Thus, the value from selectedProtocolStateIndex is updated to "UDP" when interaction with the UDP button
#### 

## To-Dos
Server View:
- Change button content if server is running (state variable)

## Bugs
- Hier kommen Bugs rein

## Roadmap
- [ ] Feature 1
- [ ] Feature 2
- [ ] Feature 3
  - [ ] Nested Feature

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Theory Notes
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
<p align="right">(<a href="#readme-top">back to top</a>)</p>

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
        Text("Welcome to the Basics Codelab!")
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
<p align="right">(<a href="#readme-top">back to top</a>)</p>

### ViewModels
ViewModel is a class that exposes the state of the user interface and encapsulates the associated
logic. Its principal advantage is that it caches state and persists it through configuration changes. 
This means that your UI does not have to fetch data again when navigating between activities.


## Contact
<div>
  <p>Alexander Kleinwächter</p> 
  [@email]<p><a href="a.kleinwaechter97@gmail.com">E-Mail</a></p>
  [@linkedIn]<p><a href="https://www.linkedin.com/in/alexander-kleinw%C3%A4chter-42606428b/">LindedIn</a></p>
  [@twitter]<p><a href="https://twitter.com/@a_kleini97">Twitter</a></p>
  [@project-repo]<p><a href="https://github.com/github_username/repo_name">Project Repo</a></p>
</div>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Notizen und Vorlagen aus kopiertem Projekt ------------------------------------------------------------------------------------------------->
<!-- Table of Contents
(...)
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
(...)
-->

<!-- ABOUT THE PROJECT 
## About The Project
Here's a blank template to get started: To avoid retyping too much info. Do a search and replace with your text editor for the following: `github_username`, `repo_name`, `twitter_handle`, `linkedin_username`, `email_client`, `email`, `project_title`, `project_description`

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With
* [![Next][Next.js]][Next-url]
* [![React][React.js]][React-url]
* [![Vue][Vue.js]][Vue-url]
* [![Angular][Angular.io]][Angular-url]
* [![Svelte][Svelte.dev]][Svelte-url]
* [![Laravel][Laravel.com]][Laravel-url]
* [![Bootstrap][Bootstrap.com]][Bootstrap-url]
* [![JQuery][JQuery.com]][JQuery-url]
-->

<!-- GETTING STARTED
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites
This is an example of how to list things you need to use the software and how to install them.
* npm
  ```sh
  npm install npm@latest -g
  ```

### Installation
1. Get a free API Key at [https://example.com](https://example.com)
2. Clone the repo
   ```sh
   git clone https://github.com/github_username/repo_name.git
   ```
3. Install NPM packages
   ```sh
   npm install
   ```
4. Enter your API in `config.js`
   ```js
   const API_KEY = 'ENTER YOUR API';
   ```
5. Change git remote url to avoid accidental pushes to base project
   ```sh
   git remote set-url origin github_username/repo_name
   git remote -v # confirm the changes
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>
-->

<!-- USAGE EXAMPLES 
## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

_For more examples, please refer to the [Documentation](https://example.com)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>
-->



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_username
[product-screenshot]: images/screenshot.png
[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D
[Vue-url]: https://vuejs.org/
[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[Svelte.dev]: https://img.shields.io/badge/Svelte-4A4A55?style=for-the-badge&logo=svelte&logoColor=FF3E00
[Svelte-url]: https://svelte.dev/
[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com 
