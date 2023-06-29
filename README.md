# Clipboard Cleaner
![](./src/main/resources/img/icon-64.png) 

This is the simplest and lightweight clipboard cleaner, the clipboard will be empty every selected delay in minutes; by default runs every `30 minutes`.

## Motivation

Working in IT is common to have to store your passwords in multiple places and copied them from there; therefore some malicious applications use that, to attack you using a technique called `Clipboard Hijacking`, with the clipboard cleaner application we can prevent this kind of attack.

## Features

- [x] Enabled or disabled 
- [x] Set custom delay
- [ ] Multiplatform installation
  - [x] [Windows](./build/win/clipboard-cleaner.exe)
  - [ ] MacOSX
  - [ ] Linux
- [x] Running in background
- [ ] Clean clipboard manually
- [ ] Save personal configuration
- [ ] Show latest cleaning time

## Installation

### Windows

> The executable of the application for windows is already generated with [launch4j](https://launch4j.sourceforge.net/)

Download the `.exe` file [clipboard-cleaner.exe](./build/win/clipboard-cleaner.exe) and put it in some place.

#### Add to startup

- Generate and cut direct access of `clipboard-cleaner.exe` file
- Prest `ctrl + r` 
- Type `shell:startup`
- Paste the direct access in the opened window

### Linux

TODO

### MacOS X

TODO

## Contributing

TODO

## License

TODO

<a href="https://www.flaticon.com/free-icons/clipboard" title="clipboard icons">Clipboard icons created by sbhgraphics - Flaticon</a>