## Robo4J-lego-tank-example
Robo4J Lego Mindstorm Simple Tank Demo example

Example using Robo4J to send messages between the Http (REST API), Mindstorm Brick Buttons and Lego Mindstorm Tank platform

The internal sever is accessible on the port 8025 and supports POST requests (no authentication required).

example : <ROBO4J_IP>:8025

GET request:

http:<IP_ADDRESS>:8025?type=tank&command=stop

Request command types: stop, move, back, left, right


GET response: done

## Building from Source
The Robo4j framework uses [Gradle][] to build.
It's required to create fatJar file and run.

## Requirements
* [Java JDK 8][]
* [Robo4j.io][] :: version: alpha-0.3

## Staying in Touch
Follow [@robo4j][] or authors: [@miragemiko][] , [@hirt][]
on Twitter. In-depth articles can be found at [Robo4j.io][] or [miragemiko blog][]

## License
The Robo4j.io Framework is released under version 3.0 of the [General Public License][].

[Robo4j.io]: http://www.robo4j.io
[miragemiko blog]: http://www.miroslavkopecky.com
[General Public License]: http://www.gnu.org/licenses/gpl-3.0-standalone.html0
[@robo4j]: https://twitter.com/robo4j
[@miragemiko]: https://twitter.com/miragemiko
[@hirt]: https://twitter.com/hirt
[Gradle]: http://gradle.org
[Java JDK 8]: http://www.oracle.com/technetwork/java/javase/downloads
[Git]: http://help.github.com/set-up-git-redirect
[Robo4j documentation]: http://www.robo4j.io/p/documentation.html
