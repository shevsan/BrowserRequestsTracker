# App Description
Android app that tracks user requests in the Google Chorme and if it contains 'google.com' saves to the local db.
Stack: Kotlin, Compose, Coroutines, MVVM, Clean Arch, Single Activity, Dependecy Injection (Hilt), Room, Accessibility Service.

# How to run an app? 
After the installation of the apk, you have to enable Accessibility Service with settings screen that will appear. After this step, you can go googling something and all of your requests will be saved in the app.

# Functionallity 
App shows a list of your google requests. On the tap on item the link will be copied to clipboard. With swipe to the left you can delete one item. If there are more than one request, the clear all button will be displayed.

# Package Structure

```
# App Module
.
├── presentation                  # Store MainActivity / Compose Screens / Theme / VM, etc.
│
├── data                          # Network / Database / DataSource / Repository Impl
│
├── domain                        # Repository Interface / UseCase / Models / Mappers
│
├── di                            # AppModule / DataModule
│
└── common                        # Constants
```
```
# Presentation package
.
├── components                    # Components used around all applications, like some items, top bars
│
├── theme                         # App Colors / App Theme / App Shapes / App Typography
│
└── ui                            # Screens / View Model / Screen Components / Screen Constants
```
# Naming Conventions
```
Class - ExampleExample
```
```
Function - exampleExample
```
```
Composable - ExampleExample
```
```
Composable Preview - ExampleScreenPreview
```
# Compose File Structure
```ExampleScreen.kt```

name for the composable func should be ```ExampleScreen```. 

If a composable is going to contain a lot of code - you should divide it into smaller composable functions ```sections```.

Sections should be called following this pattern: ```*screenName*Screen*SectionName*Section```, example: ```ExampleScreenScrollableSection```

```EexmpleScreen``` contains ```ExampleScreenContent``` wich handles only ui. All ViewModel invocations should happen inside ```ExampleScreen```

For example:
```
@Composable
fun ExampleScreen(modifier: Modifier) {
   val viewModel: ExampleViewModel = hiltViewModel()
   ExampleScreenContent(modifier, viewModel.state.value)
}

@Composable
fun ExampleScreenContent(modifier: Modifier, state: ExampleState) {
    Column(modifier) {
        ExampleScreenTopBarSection(modifier = Modifier)
        ExampleScreenScrollableSection(modifier = Modifier)
    }
}

@Composable
fun ExampleScreenTopBarSection(modifier: Modifier) {
    Row(modifier) {
        //code
    }
}

@Composable
fun ExampleScreenScrollableSection(modifier: Modifier) {
    Column(modifier) {
        //code
    }
}

@Preview
@Composable
fun ExampleScreenPreview() {
    ExampleScreenContent(modifier = Modifier, state = ExampleState.DefaultValue)
}
```
