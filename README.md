# ThesisProject
The main focus of the prototype is to connect the components that are created and distributed on applications developed by using Appcelerator Alloy with components created and used within Xamarin applications on an environment level. Also an interesting aspect is how we can make a component on one of the platforms and distribute it to the other.

In order to run the code JDK (Java Development Kit) and an IDE (Integrated Development Environment) needs 
to be installed on the computer. JDK can be downloaded from http://www.oracle.com/technetwork/java/javase/downloads/index.html. 
	
In terms on an IDE we recommend the use of Eclipse or NetBeans. The latest version of NetBeans can be downloaded from http://www.oracle.com/technetwork/java/javase/downloads/jdk-netbeans-jsp-142931.html. Since Eclipse has different distributions we recommend the usage of Eclipse Luna since this is the environment we used for development. The IDE can be downloaded from http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/marsr. 
	
In order to see the different results of the interpretation of different components the file path inside of the main execution file "Execution.java" located in package "com.java.main" needs to be changed. 

It is recommended to use Windows operating system to run the project since the output directory for the solution is located in the user Desktop. If the output directory needs to be changed update variable "file" in function "prepareStructure()" in "AlloyInterpreter.java" located in package "com.java.interpreter".
