# swellrt-selenium

Simple Java/Selenium scripts to play with concurrent real-time writing in the text editor of Wave/SwellRT.

## Usage

Run main class `PadWriteMain.java` with following command line arguments:

```
<path-to-webdriver>,<path-to-webdriver>... <pad-url> <input-file>,<line-to-start>[,browser] <input-file>,<line-to-start>[,browser] ...
```

The tool will start a new thread per each input file: 

- The thread will load the pad url in a new Web browser instance 
- It will write the contents of the input file in the pad "simulating" human writing. 
- Writing will start at the provided <line-to-start>
- Optionally the browser brand of each thread can be provided with following codes: `chrome` (default) or `firefox` 

After all threads finish the writing, the tool will wait for key input to close browser windows.


## Setting up a target Wave/SwellRT editor

Before executing the tool, ensure your app automatically enables the edit mode in the editor component when it's loaded in the browser.
Also, the editor panel should have at least a blank lines for any if the threads run with the tool.


## Configuring web drivers

To install web drivers check out following links:

- http://www.seleniumhq.org/docs/03_webdriver.jsp#setting-up-webdriver-project
- https://sites.google.com/a/chromium.org/chromedriver/
- https://github.com/mozilla/geckodriver

