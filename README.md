# pc2-telemetry
[![Build Status](https://travis-ci.org/ralfhergert/pc2-telemetry.svg?branch=master)](https://travis-ci.org/ralfhergert/pc2-telemetry)

Telemetry analysis tool for Project Cars 2. It captures the telemetry data Project Cars 2 is sending via UDP.
The data can be analyzed or stored in a file for exchange or offline analysis sessions.

# How to use?
PC2-Telemetry is a Java Application, so make sure you have a JavaRuntimeEnvironment (JRE) in at least version 1.8 installed.
You can get [Java free from Oracle](https://java.com). Download the latest release pc2-telemetry.jar. Start the application
by double-clicking the JarFile.

Start Project Cars 2. Make sure that in Project Cars' system options. Sending UDP-packets in version 2 is active.
As soon as you are driving on a track PC2-Telemetry should start receiving and rendering the data in real-time.

# Features
Version 1 is still in development. The wanted features for version 1 are:
 * Capturing CarPhysics-packets from Project Cars 2
 * captured data can be rendered as line-graphs on screen in real-time
 * captures data can be stored in or read from files


# How to request features or report bugs?
Please have a look at the [issues of pc2-telemetry](https://github.com/ralfhergert/pc2-telemetry/issues) whether a similar
feature request or bug report is already filed. If not then please create a new issue. If you would have already a solution
consider creating a pull-request.

# How to contribute?
Contributions are very welcome. Create a fork and start modifying. If you think your change should flow back into this
repository then don't hesitate to create a pull-request.
