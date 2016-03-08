#BPMN2 Modeler

The Eclipse BPMN2 Modeler is a graphical tool for authoring models that are compliant with the BPMN 2.0 standard. [Business Process Model and Notation](http://www.bpmn.org/) (BPMN) is a graphical representation for specifying business processes in a business process model. The objective of BPMN is to support business process management, for both technical users and business users, by providing a notation that is intuitive to business users, yet able to represent complex process semantics. BPMN is maintained by the Object Management Group (OMG)

##Extensibility

The goal of the Eclipse BPMN2 Modeler is not only to proved a flexible graphical modeling tool for BPMN 2.0 standard, but also a powerful platform to use the extensibility mechanism of BPMN 2.0. This mechanism allows extending standard BPMN elements with additional attributes. This extensions can be used by custom modeling tools as also by specific BPM and Workflow engines. The Eclipse BPMN2 Modeler provides a clear concept of extension points which makes it easy to extend the BPMN standard and customize the modeling tool. 


 
![Image Alt](http://www.eclipse.org/bpmn2-modeler/images/snapshots.gif)

##Documentation
The Eclipse BPMN2 Modeler project provides a [Wiki Page](http://wiki.eclipse.org/BPMN2-Modeler) providing several examples and tutorials. The [Eclipse BPMN Modeler User Guide](http://www.eclipse.org/bpmn2-modeler/documentation/BPMN2ModelerUserGuide-1.0.1.pdf) contains detailed information about the usage. The latest information about new releases can be found [here](http://www.eclipse.org/bpmn2-modeler/whatsnew/index.php). 

##Architecture
The Eclipse BPMN2 Modeler is built on [Eclipse Graphiti](http://www.eclipse.org/graphiti) and uses the [BPMN 2.0 EMF meta model](http://www.eclipse.org/modeling/mdt/?project=bpmn2) developed within the Eclipse [Model Development Tools](http://www.eclipse.org/modeling/mdt/) (MDT) project. This meta model is compatible with the [BPMN 2.0 specification](http://www.omg.org/spec/BPMN/2.0/PDF) proposed by the [Object Management Group](http://www.osoa.org/display/Main/Home).

The Eclipse BPMN2 Modeler is build on the [Eclipse Plug-in Architecture](http://www.eclipse.org/articles/Article-Plug-in-architecture/plugin_architecture.html) and provides several extension points to adapt the plug-in for individual projects.
On the [Wiki Deveoper Tutorials](https://wiki.eclipse.org/BPMN2-Modeler/DeveloperTutorials) you can find examples and guidliens how to extend the capabilities of Eclipse BPMN2 Modeler.


##Getting Involved
Beside the [Wiki](http://wiki.eclipse.org/BPMN2-Modeler) as a source of all knowledge, there are several ways to get involved. 
If you have a few spare cycles and are interested in helping out, pick from our [current list of open issues](https://bugs.eclipse.org/bugs/buglist.cgi?classification=SOA&list_id=1567033&product=BPMN2Modeler) and send us your contributions.
To get the sources, the [primary Git repository](https://git.eclipse.org/c/bpmn2-modeler/org.eclipse.bpmn2-modeler.git) for BPMN2 Modeler is located at eclipse. This repository is also mirrored at github where you can submit your pull requests.
