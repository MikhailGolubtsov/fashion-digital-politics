### Build & run

To build execute:

```bash
sbt assembly
```

In order to run an application execute:

```bash
java -jar target/scala-2.12/fashion-digital-task-assembly-0.1.jar
```

### Limitations & possible improvements

- A naive algorithm - it's required that the whole parsed dataset fits
into memory. But a constant memory implementation is also possible
(assuming constant cardinality of the speakers set). This requires
a fully streaming solution.
- Lack of unit tests
- Lack of an integration test
- No logging
- No configuration file
- Return 400 if any http response code of a downloading request
is not 200 or 5xx