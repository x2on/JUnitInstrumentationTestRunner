##Usage

JUnitInstrumentationTestRunner is in central maven repository, so simply add the following dependency to your pom.xml:

```xml
<dependency>
   <groupId>de.felixschulze.android.test</groupId>
   <artifactId>JUnitInstrumentationTestRunner</artifactId>
   <version>1.0</version>
</dependency>
```


##Usage

    $ adb shell am instrument -w com.example.yourproject.test/de.felixschulze.android.test.JUnitInstrumentationTestRunner

##Build

    $ mvn install
