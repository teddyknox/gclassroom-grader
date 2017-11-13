# Google Classroom Java Grader

Provides a barebones command line interface for batch testing student code. Google Authentication should launch on first run.

Exposes 3 commands

List all courses associated with your account:
```java -jar ./build/libs/google-classroom-grader-all.jar courses```

List all assignments associated with a provided courseId (get that from the output of the first command):
```java -jar ./build/libs/google-classroom-grader-all.jar assignments list --course <courseId>```

Grade assignment using a java test class on the path (will add JUnit support soon):
```java -jar ./build/libs/google-classroom-grader-all.jar assignments list --course <courseId> --assignment <assignmentId> --testClass <test java class path>```
