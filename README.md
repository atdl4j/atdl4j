# atdl4j

#### The Open Source Java Solution for FIXatdl

atdl4j is an open source and not-for-profit Java implementation of [FIXatdl](http://www.fixprotocol.org/FIXatdl), the FIX Protocol Algorithmic Trading Definition Language standard developed by FIX Protocol Limited (FPL).

Please see [the atdl4j Homepage](http://atdl4j.org) for an overview.

## Features

* Compatible with Java 6 and upper version
* Displays algo input screens based on the latest industry-standard FIXatdl 1.1 XML schema
* Reads and generates algorithm-specific FIX message content
* Can be integrated into a trading system, or run as a standalone testing/validation tool.
* Supports the full set of UI widgets defined in FIXatdl 1.1
* Support for message validation and widget state rules (such as show/hide and enable/disable.)
* Supports strategy filtering, customizable settings, and context-specific views (for example Cancel/Replace mode)
* Written in the Java language using standard libraries.
* Both Swing and SWT user interfaces are available, using shared base classes.
* Source code can be modified to support in-house FIXatdl schema extensions

## Contributing

If you wish to contribute code to atdl4j:

* Fork the atdl4j project on GitHub
* Commit your changes to your fork
* Raise a pull request, which will be reviewed by the atdl4j core team

If you are new to git source control and/or Github, please visit the [Github Help Page](https://help.github.com/)

## Documentation

Docs and downloads are available at the [atdl4j Wiki](https://github.com/atdl4j/atdl4j/wiki)

Running the testing apps:

Swing: ```mvn.bat exec:java -Pswing```

SWT: ```mvn.bat exec:java -Pswt```

[Examples](examples.md)

## FIX Engine Integration

Please note that atdl4j is **NOT** a FIX engine for sending and receiving orders over the wire. Rather, atdl4j draws order entry screens from FIXatdl templates and gets/sets their FIX parameter values.

If you are intending to implement a full-stack trading system with FIX order capability, you will additionally require a FIX engine such as the open-source [QuickFIX/J](http://www.quickfixj.org/).

## Key Contributors

* Scott Atwell, American Century
* [John Shields](https://github.com/johnnyshields)
* Danilo Tuler, Investtools
* Renato Gallart, Investtools

## License

atdlj is licensed under the MIT license. Refer to LICENSE file for details

FIX Protocol and FIXatdl are trademarks or service marks of FIX Protocol Limited

[Looking for a .NET implementation of FIXatdl? Check out atdl4net](http://atdl4net.org)
