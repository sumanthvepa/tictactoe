# Build and Installation Notes

## Notes on Packaging the Java Console app

### Windows
To build an executable installer for the java console app run the following
command in java/console/tictactoe. This needs to be run on a Windows machine.

```
jpackage @windows.jpack
```

This assumes that you have jpackage and its dependencies have already been
installed. The generated exe is located in app\build\libs and is called
tictactoe-ver.exe.

### MacOS
On macOS invoke the packager as follows:
```
jpackage @macos.jpack
```
Once again, this assumes you have jpackage installed
via a JDK.  The generated dmg is located in app/build/libs.
It is named tictactoe-ver.dmg.

The app can be installed in the normal way on MacOS.
However, double-clicking the app icon in the menu will
not result in the program running. To do that you
will have to go open a terminal and invoke the
executable
/Applications/tictactoe.app/Contents/MacOS/tictactoe.
Very inconvenient, but it's the best I can do for the moment.
