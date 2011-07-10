##Usage

```xml
<dependency>
   <groupId>de.felixschulze.android.test</groupId>
   <artifactId>JUnitInstrumentationTestRunner</artifactId>
   <version>1.0</version>
</dependency>
```

##Build

    $ mvn install

##Usage

    $ adb shell am instrument -w com.example.yourproject.test/de.felixschulze.android.test.JUnitInstrumentationTestRunner
