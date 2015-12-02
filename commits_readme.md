#How to Ccontribute
The Eclipse BPMN2 project is using Gerrit [1] to manage code contributions and reviews. The repository is hosted at eclipse.org [2], the one on github is only a mirror that is updated nightly.

Versions 1.1.x are for Luna and should be developed in the "luna" branch. The "master" branch is always targeted for the next eclipse release.
Mars (master) release, which is 1.2.0 

## How to Fix Stuff
If you need to fix stuff for Luna it should go into version 1.1.3 which has not been finalized yet. Version 1.1.3 will probably be finalized before the next maintenance build of Luna and I don't know when THAT will happen - it's up to the eclipse Project Management Committee (PMC).

The project team provides a version "finalized" if there are no more changes planned on that version; the feature and plugin artifacts are renamed from "artifact-name.x.y.z.timestamp" to "artifact-name.x.y.z.Final" for example the plugin jar:

    org.eclipse.bpmn2.modeler_1.1.3.201504171557.jar

will be renamed to

    org.eclipse.bpmn2.modeler_1.1.3.Final.jar

Additional bug fixes after a version is finalized should be made to the next service release, so the next one for Luna would be 1.1.4. I will usually finalize a version just before the next service release of a particular eclipse platform. If no new SRs happen before the next major eclipse release (Mars), I will finalize 1.1.3 for Luna.


[1] [https://wiki.eclipse.org/Gerrit](https://wiki.eclipse.org/Gerrit)

[2] [https://git.eclipse.org/c/bpmn2-modeler/org.eclipse.bpmn2-modeler.git](https://git.eclipse.org/c/bpmn2-modeler/org.eclipse.bpmn2-modeler.git)


#Gerrit


The BPMN2 Modeler Git repository has been enabled for gerrit. This means that project committers must re-clone their local repositories using the new repository URL [3]. The use of gerrit is optional at this time until everyone has had a chance to become more familiar with the new code review process. Please post questions and report problems to the mailing list or newsgroup if you are having trouble committing changes.

Other projects in the works include migrating the Eclipse project website for the BPMN2 Modeler to Git. This will complete the migration of Eclipse repositories and make CVS obsolete.


[1] http://download.eclipse.org/bpmn2-modeler/site-helios/0.1.0
[2] http://download.eclipse.org/bpmn2-modeler/site/0.1.0
[3] ssh://<your_username>@git.eclipse.org:29418/bpmn2-modeler/org.eclipse.bpmn2-modeler.git


normal repository: git clone https://git.eclipse.org/r/bpmn2-modeler/org.eclipse.bpmn2-modeler

gerrit repository: ssh://rsoika@git.eclipse.org:29418/bpmn2-modeler/org.eclipse.bpmn2-modeler.git


# Comments on commit

It is sufficient to start an issue in Bugzilla and add the ID into my commit comment like 'Bug xxxxxx'

Example:

    Bug 465939 - Call activity loop cause StackOverflowException