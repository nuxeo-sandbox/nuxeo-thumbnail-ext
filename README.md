nuxeo-thumbnail-ext
===================

A plugin that makes it easier to support thumbnail generation for file types that are not supported out of the box.

# List of Features
The plugin includes a thumbnail factory that extends the default document thumbnail factory and looks at all the available converters to generate a thumbnail for a given file types.
The main drawback of the default thumbnail factory is that it only looks at the `anyToThumbnail` converter, which makes **composition** cumbersome if multiple plugins override it.

With this plugin, contributing a converter to `png` for a given mime-type is enough for the platform to automatically generate thumbnails. 

Here's an example with a command line based converter  

```xml
<extension point="converter" target="org.nuxeo.ecm.core.convert.service.ConversionServiceImpl">
    <converter class="org.nuxeo.ecm.platform.convert.plugins.CommandLineConverter" name="myconverter">
        <sourceMimeType>sourceMimeType</sourceMimeType>
        <destinationMimeType>image/png</destinationMimeType>
        <parameters>
            <parameter name="CommandLineName">theCommandLine</parameter>
        </parameters>
    </converter>
</extension>
```

# Build
Assuming maven is correctly setup on your computer:

```
git clone https://github.com/nuxeo-sandbox/nuxeo-thumbnail-ext
cd nuxeo-thumbnail-ext
mvn clean install
```

# Install
Either manually install the plugin on your instance or deploy from the marketplace

# Marketplace 
This plugin is published on the [Nuxeo marketplace](https://connect.nuxeo.com/nuxeo/site/marketplace/package/nuxeo-thumbnail-ext) 

# Support
**These features are not part of the Nuxeo Production platform.**

These solutions are provided for inspiration and we encourage customers to use them as code samples and learning resources.

This is a moving project (no API maintenance, no deprecation process, etc.) If any of these solutions are found to be useful for the Nuxeo Platform in general, they will be integrated directly into platform, not maintained here.

# License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

# About Nuxeo
Nuxeo Platform is an open source Content Services platform, written in Java. Data can be stored in both SQL & NoSQL databases.

The development of the Nuxeo Platform is mostly done by Nuxeo employees with an open development model.

The source code, documentation, roadmap, issue tracker, testing, benchmarks are all public.

Typically, Nuxeo users build different types of information management solutions for [document management](https://www.nuxeo.com/solutions/document-management/), [case management](https://www.nuxeo.com/solutions/case-management/), and [digital asset management](https://www.nuxeo.com/solutions/dam-digital-asset-management/), use cases. It uses schema-flexible metadata & content models that allows content to be repurposed to fulfill future use cases.

More information is available at [www.nuxeo.com](https://www.nuxeo.com).
