## 6.0.2
* The code can now be compiled with [``javac``](https://en.wikipedia.org/wiki/Javac)
  * ``SqlDdlTable.Triggers#defineTriggers`` was changed from
    ```java
    protected <A extends DbmsAdaptor<A>> void defineTriggers(final A dbms)
    ```
    to
    ```java
    protected void defineTriggers(final DbmsAdaptor<?> dbms)
    ```
* Updated dependencies
* Synced repo with XDEVs standards

## 6.0.1
* Don't start AWT Threads when not required #57


## 6.0.0
* Ensured build compatibility with Java 8, 11 and 17
* Minor code improvements, mostly for tests
* Updated some maven dependencies and plugins

### Complete overview about the new XDEV (IDE) Framework in version 6
![XDEV-IDE-Framework-6 overview](https://user-images.githubusercontent.com/45384811/134640194-0b42a238-3c7e-402a-8b05-51419108dbbd.png)

### Detached JavaFX - Migration Guide

*This guide only applies if you use XAPI outside of the XDEV IDE.*

*Feel free to contact us or open an issue if you have any questions.*

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

