## ChatGPT Wrapper

### App Summary: 
This app first takes ChatGPT and emulates a prompt-answer chat screen.  The twist added on top of this wrapper is that it allows the user to select the tone of ChatGPT's response (default, professional, casual, humorous). You can switch between tones and ChatGPT will respond, remembering the previous inquiries. Additionally, the two extras added are allowing the user to change font size (small, medium, large) as well as allowing the user to select one of three options for a given output (hint, translation, elaborate).  

### Screenshot: 

### Setup: 
 - Open local.properties in project and add line of code: OPENAI_AI_Key= "your API key"
 - Add this to your buildConfig in build.gradle (app-level):
 
    *val apiKey = BuildConfig.OPENAI_API_KEY*


### Build/Run Steps: 
 - Devices tested: Medium Phone, Pixel 8 Pro
 - minSDK: 24
 - targetSDK: 36
 - Android Studio: Narwhal 3

### Architecture Diagram:

| activity (UI/User input) | --> | view model (holds state) | --> | repository (data logic/API call) | --> | OpenAI service (external API) |

### LLM Usage: 
ChatGPT was used in this project. Mainly in the creation of functions, UI and understanding how different classes will work with each other. Prompts similar to: "write me a function that...".


