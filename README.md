Configuration Manual

Prerequisites

Before running the application, ensure that the following software is installed on your system:

	1.	Java Development Kit (JDK): Version 20 or higher
	2.	Maven: For dependency management and building the application
	3.	SQLite: For database integration (no separate server setup required as SQLite is embedded)
	4.	Git: For version control
	5.	IDE/Text Editor: IntelliJ IDEA (recommended) or any text editor of your choice

Steps to Run the Application

	1. Clone the repository
      		Clone the project repository from GitHub: https://github.com/abdulnafey18/Secure-TodoApp.git
      		Navigate into the project directory: cd TodoApp
  	2. Checkout the Desired Branch
      		The project is split into two branches: Secure and insecure
      		To run the secure version of the application:
      		git checkout secure
      		To run the insecure version of the application:
      		git checkout insecure
  	3. Execute the Application by either running the MainApplication class or executing this command
      		mvn spring-boot:run
  	4. Access the Application
      		Once the application starts, you can access it in your browser at: http://localhost:8080

Configuration Details

	•	Database Configuration
    The application uses SQLite for data storage. This file is excluded from Git tracking via the .gitignore file
	•	Port Configuration
    By default, the application runs on port 8080. You can change this in the application.properties file located under src/main/resources: server.port=8080
	•	Log Files
    Application logs are stored in log-file.txt in the root directory. This file is excluded from Git tracking via the .gitignore file

Project Branches

  The application is divided into two branches:
  
	  1.	Secure Branch: Implemented mitigation to vulnerabilites such as SQL Injection, XSS prevention, Sensitive data exposure
	  2.	Insecure Branch: Demonstrates vulnerabilities such as SQL Injection, XSS prevention, Sensitive data exposure

  Use the respective branch to explore the different versions.
      
      
