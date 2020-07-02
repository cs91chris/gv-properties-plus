[![N|Solid](http://www.greenvulcanotechnologies.com/wp-content/uploads/2017/04/logo_gv_FLAT-300x138.png)](http://www.greenvulcanotechnologies.com)

# GreenVulcano PropertiesHandler Extras

This GreenVulcano extension adds a new set of placeholders:

1. the ``sqljson``, which acts both as ``sql`` and ``sqltable`` placeholders, depending on how many results it has to fetch and response with json format.

2. the ``jsonpath`` that takes a json string and an expression. see [https://github.com/json-path/JsonPath](https://github.com/json-path/JsonPath)


## Getting started

### Prerequisites

You need to install the GreenVulcano engine on the Apache Karaf container. Please refer to [this link](https://github.com/greenvulcano/gv-engine/blob/master/quickstart-guide.md) for further reference.

### Installation

Clone or download this repository on your computer, and then run ``mvn install`` in its root folder.

Then, run this command in the karaf shell to install the actual extension and the dependencies:

```shell
bundle:install -s -l 96 mvn:com.jayway.jsonpath/json-path/2.1.0
bundle:install -s -l 96 mvn:com.fasterxml.jackson.core/jackson-databind/2.9.10
bundle:install -s -l 96 file:<PATH_PROJECT>/target/gv-properties-plus-<VERSION>.jar
```

That's it! You can now make use of the new placeholders in your Vulcon project.
