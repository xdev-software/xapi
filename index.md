[![Build](https://img.shields.io/github/workflow/status/xdev-software/xapi/Master%20CI)](https://github.com/xdev-software/xapi/actions?query=workflow%3A%22Master+CI%22)
[![Latest version](https://img.shields.io/maven-central/v/com.xdev-software/xapi)](https://mvnrepository.com/artifact/com.xdev-software/xapi)
[![Build Develop](https://img.shields.io/github/workflow/status/xdev-software/xapi/Develop%20CI/develop?label=build%20develop)](https://github.com/xdev-software/xapi/actions?query=workflow%3A%22Develop+CI%22+branch%3Adevelop)
[![javadoc](https://javadoc.io/badge2/com.xdev-software/xapi/javadoc.svg)](https://javadoc.io/doc/com.xdev-software/xapi) 

# XDEV Application Framework
The XDEV Application Framework is the core of every application developed with XDEV-IDE. It provides a basic architecture and infrastructure for graphic Swing interfaces and database applications and facilitates many tasks that every Java programmer usually has to take care of himself, including: 

 - JDBC and SQL programming
 - transaction management
 - data encryption
 - protection against SQL injection
 - connection pooling
 - processing of query results (result sets)
 - lazy loading
 - data binding
 - data validation. 

With version 4, the framework provides a concept for automated data record locking. The framework also offers numerous classes and methods that simplify access to databases, files, RAM, interfaces, external applications, and web services. 

The framework can be used with every Java IDE. Without the XDEV IDE, however, the wizards and RAD-tooling of the XDEV IDE (GUI Builder, VT Editor, etc.) are missing for the best RAD-development experience. 

## XDEV-IDE
XDEV(-IDE) is a visual Java development environment for fast and easy application development (RAD - Rapid Application Development). XDEV differs from other Java IDEs such as Eclipse or NetBeans, focusing on programming through a far-reaching RAD concept. The IDE's main components are a Swing GUI builder, the XDEV Application Framework, and numerous drag-and-drop tools and wizards with which the functions of the framework can be integrated.

The XDEV-IDE was license-free up to version 4 inclusive and is available for Windows, Linux and macOS. From version 5, the previously proprietary licensed additional modules are included in the IDE and the license of the entire product has been converted to a paid subscription model. The XDEV Application Framework, which represents the core of the RAD concept of XDEV and is part of every XDEV application, was released as open-source in 2008.

## Contributing

We would absolutely love to get the community involved, and we welcome any form of contributions – comments and questions on different communication channels, issues and pull request in the repositories, and anything that you build and share using our components.

### Get in touch with the team

Twitter: https://twitter.com/xdevsoftware<br/>
Mail: info@xdev-software.de

### Some ways to help:

- **Report bugs**: File issues on GitHub.
- **Send pull requests**: If you want to contribute code, check out the development instructions below.

We encourage you to read the [contribution instructions by GitHub](https://guides.github.com/activities/contributing-to-open-source/#contributing) also.

## Dependencies and Licenses
The XDEV Application Framework is released under [GNU Lesser General Public License version 3](https://www.gnu.org/licenses/lgpl-3.0.en.html) aka LGPL 3<br/>
View the [summary of all dependencies online](https://xdev-software.github.io/xapi/dependencies/)

## Releasing
If the ``develop`` is ready for release, create a pull request to the ``master``-Branch and merge the changes

When the release is finished do the following:
* Merge the auto-generated PR (with the incremented version number) back into the ``develop``
* Add the release notes to the [GitHub release](https://github.com/xdev-software/xapi/releases/latest)
