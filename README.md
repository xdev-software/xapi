[![Latest version](https://img.shields.io/maven-central/v/com.xdev-software/xapi-db-{name})](https://mvnrepository.com/artifact/com.xdev-software/xapi-db-{name})
[![Build](https://img.shields.io/github/workflow/status/xdev-software/xapi-db-{name}/Check%20Build/develop)](https://github.com/xdev-software/xapi-db-{name}/actions/workflows/checkBuild.yml?query=branch%3Adevelop)
[![javadoc](https://javadoc.io/badge2/com.xdev-software/xapi-db-{name}/javadoc.svg)](https://javadoc.io/doc/com.xdev-software/xapi-db-{name}) 

# !!! Important Note for Developers !!!

This REAMDE is just a template for the SqlEngine Database Adapter. <br>
For a propper use, please change the following placeholder:
- {name} = The name of the Adapter written all in lowercase. For example: sybase, h2, mysql-5.
- {NAME} = The name of the Adapter written normaly. For example: Sybase, H2, SQLite.

Furthermore, you could delete the heading '# Important Note' if there is nothing important, to tell the user. Otherwise, please consider a well written and understandable Note, why things are like they are.
For example: 
- If the adapter is only for read from the DB, but not write into it.
- If the adapters jdbc is not existend in maven, and therefore, users have to manually load the jdbc-driver from a source of their choice.


# SqlEngine Database Adapter {NAME}

The XDEV Application Framework provides an abstraction over database dialects as part of its SqlEngine. This module is the Database Adapter for {NAME} which includes the {NAME}-specific implementation for database access.

# Important Note


## XDEV-IDE
XDEV(-IDE) is a visual Java development environment for fast and easy application development (RAD - Rapid Application Development). XDEV differs from other Java IDEs such as Eclipse or NetBeans, focusing on programming through a far-reaching RAD concept. The IDE's main components are a Swing GUI builder, the XDEV Application Framework, and numerous drag-and-drop tools and wizards with which the functions of the framework can be integrated.

The XDEV-IDE was license-free up to version 4 inclusive and is available for Windows, Linux and macOS. From version 5, the previously proprietary licensed additional modules are included in the IDE and the license of the entire product has been converted to a paid subscription model. The XDEV Application Framework, which represents the core of the RAD concept of XDEV and is part of every XDEV application, was released as open-source in 2008.

## Contributing

We would absolutely love to get the community involved, and we welcome any form of contributions – comments and questions on different communication channels, issues and pull request in the repositories, and anything that you build and share using our components.

### Get in touch with the team

Twitter: https://twitter.com/xdevsoftware 
<br/>
Mail: opensource@xdev-software.de

### Some ways to help:

- **Report bugs**: File issues on GitHub.
- **Send pull requests**: If you want to contribute code, check out the development instructions below.

We encourage you to read the [contribution instructions by GitHub](https://guides.github.com/activities/contributing-to-open-source/#contributing) also.

## Dependencies and Licenses
The XDEV Application Framework is released under [GNU Lesser General Public License version 3](https://www.gnu.org/licenses/lgpl-3.0.en.html) aka LGPL 3<br/>
View the [summary of all dependencies online](https://xdev-software.github.io/xapi-db-{name}/dependencies/)

## Support
If you need support as soon as possible and you can not wait for any pull request, feel free to use our support 
https://xdev.software/en/services/support

## Releasing [![Build](https://img.shields.io/github/workflow/status/xdev-software/xapi-db-{name}/Release?label=Release)](https://github.com/xdev-software/xapi-db-{name}/actions/workflows/release.yml)
Consider doing a [test-deployment](https://github.com/xdev-software/xapi-db-{name}/actions/workflows/test-deploy.yml?query=branch%3Adevelop) before actually releasing.

If the ``develop`` is ready for release, create a pull request to the ``master``-Branch and merge the changes.

When the release is finished do the following:
* Merge the auto-generated PR (with the incremented version number) back into the ``develop``
* Add the release notes to the [GitHub release](https://github.com/xdev-software/xapi-db-{name}/releases/latest)