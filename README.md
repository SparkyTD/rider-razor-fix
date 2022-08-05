# IMPORTANT: This tool is highly experimental, and it might break your Rider installation if you don't follow the installation instructions. Only tested with Rider 2022.2, and with smaller projects.
___
# RiderRazorFix
This is a simple bytecode modification to the JetBrains Rider IDE that enables the EditAndContinue feature for `.razor` files **Blazor Server** applications. This means that whenever you edit a Razor file, Rider will display the "Sources are modified" prompt, allowing you to "Apply changes" and hot-reload the project. Note that this only works for Blazor Server applications. **Blazor WASM is not supported by this tool.** This tool is a partial fix for [RIDER-69647](https://youtrack.jetbrains.com/issue/RIDER-69647).
___
## Why can't Rider hot reload .razor files by default?
Actually it can, it just doesn't want to. Everything needed to hot-reload a Blazor Server project is already fully implemented in Rider, with the exception of one small detail. Without going too deep into the inner workings of EnC in Rider, there are a set of java classes that contain the logic that decides whether or not the hot reload notification should be displayed after a file was modified.

In the case of `.razor` files, this class is `com.jetbrains.rider.ideaInterop.fileTypes.razor.debugger.RazorEncWritingAccessProvider`, more specifically the `isSupportedFile` method. By default, this method checks if the modified file's language equals "Razor", and only then does it return true. That's where the problem is. For some reason, the method that gets an open file's language will always return the string "Blazor", which doesn't match, and the `isSupportedFile` method returns false. Whether the `isSupportedFile` method is checking the wrong value, or the file language is incorrect is something that only JetBrains developers can tell.

## How does this tool work
This project is basically a [Java Agent](https://stackify.com/what-are-java-agents-and-how-to-profile-with-them/#:~:text=Java%20agents%20are%20a%20special,ve%20existed%20since%20Java%205.) that modifies the mentioned class before it gets loaded into the java runtime. While loading the class, it replaces the string "Razor" with "Blazor" in the `isSupportedFile` method to ensure that the "Apply Changes" button appears after modifying a .razor file.

## Installation
1. Download a prebuilt binary (e.g. `%localappdata%\JetBrains\rider-razor-fix.jar`), or optionally clone this repo and build it yourself. Make sure to use the `shadowJar` gradle task to generate the .jar file
2. Open Rider, go to Help > Edit Custom VM Options
3. Add this at the end of the file: `-javaagent:C:\\Users\\Sparky\\AppData\\Local\\JetBrains\\rider-razor-fix.jar`. Make sure you type the exact full path without quotes, and replace all `\` characters with `\\`. If there are spaces in the file's full path, move it to a different location so there are no spaces.
4. Restart Rider

If you accidentally misspell something in the VM options file, or delete the downloaded .jar file, Rider won't be able to start anymore. In this case read [this](https://www.jetbrains.com/help/rider/Tuning_the_IDE.html) or [this](https://www.jetbrains.com/help/rider/Directories_Used_by_the_IDE_to_Store_Settings_Caches_Plugins_and_Logs.html#config-directory) article to locate your .vmoptions file, and manually fix or remove the `javaagent` flag.
