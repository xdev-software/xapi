## 6.0.0
* Updated some dependencies and plugins
* Ensured build compatibility with Java 8, 11 and 17 #33 
* Minor code improvements, mostly for tests

### Detached JavaFX - Migration Guide

*This guide only applies if you use XAPI outside of the XDEV IDE.*

#### Dependencies
The Java FX part of the project was detached into [another repo](https://github.com/xdev-software/xapi-fx).

If your project uses the components `XdevBrowser` or `XdevJFXPanel` you need to add a dependency to your project:
```XML
<dependency>
    <groupId>com.xdev-software</groupId>
    <artifactId>xapi-fx</artifactId>
    <version>1.0.0-java8</version>
</dependency>
```
Make sure to use the artifact matching the Java Version of your runtime environment.

#### Changed classes and methods
Moved Method from `UIUtils#runInJFXThread(Runable)` to `UIUtilsFX#runInJFXThread(Runable)`
