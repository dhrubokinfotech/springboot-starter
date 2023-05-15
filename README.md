# SpringBoot Starter Project
Java Spring Boot based boilerplate starter application with Spring Security (JWT), Swagger and Logging and many more. 

## How to use/modify this project

**Step 1:**  Clone and open the project in ```Intellij Idea``` or any other Java (Spring) supported IDE. Right-click on the project's root folder then rename the project by navigating ```project_root/Refactor/Rename```

![1](https://github.com/dhrubokinfotech/springboot-starter/assets/26526539/ab06fc6a-c3f1-4683-9191-6b7a742d31a9)

**Step 2:** Open your project from your file system then rename the project folder name

![2](https://github.com/dhrubokinfotech/springboot-starter/assets/26526539/e603e8d8-92d0-42fa-b00b-d93ab6ffbe6c)

**Step 3:** Navigate to the project's package name (```project_root/src/main/java```) then rename the package

![3](https://github.com/dhrubokinfotech/springboot-starter/assets/26526539/d533e9d4-ba58-48d2-9848-8f7b30f1af56)

**Step 4:** Navigate to ```project_root/src/main/java/package_name``` then rename the ```StarterApplication.java``` and ```StarterApplicationTests.java``` class name

![4](https://github.com/dhrubokinfotech/springboot-starter/assets/26526539/d93db833-6b80-470a-91d5-be38df8124bf)

**Step 5:** Open the ```pom.xml file``` then change the value of ```groupId```, ```artifactId```, ```name``` and ```description``` field

![5](https://github.com/dhrubokinfotech/springboot-starter/assets/26526539/39263cc9-2043-4e2c-8e4f-87b27a0ed26a)

**Step 6:** Navigate to ```src/main/java/com/disl/starter/constants/SecurityConstants.java``` then Change the ```SECRET``` field's value

![6](https://github.com/dhrubokinfotech/springboot-starter/assets/26526539/ea4e3873-62fc-4fe9-b9da-9066df34831d)

**Step 7:** Navigate to ```project_root/src/main/resources``` and open ```application-development.properties```, ```application-staging.properties``` and ```application-production.properties``` file then change ```datasource``` and ```mail``` related values.

![7](https://github.com/dhrubokinfotech/springboot-starter/assets/26526539/57b7dd24-4db4-4dda-bf43-df80bec8d9c8)


**Step 8:** Navigate to ```project_root/src/main/resources``` and open ```application.properties```, then uncomment your required ```spring.profiles.active``` profile/property. You can also change the ```fcm``` related values (if needed)

![8](https://github.com/dhrubokinfotech/springboot-starter/assets/26526539/3277e7b2-469e-46d7-b51f-26ef2c9cb7f0)

**Step 9:** Finally run the project.

## Note
Spring Boot 3 required at least ```Java version 17```. So make sure you have installed the 17 or greater version.



