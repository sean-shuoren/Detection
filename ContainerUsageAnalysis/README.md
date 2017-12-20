# Container Usage Analysis

Automatically detect the element fields that are not used in a container.

To start with example mock program, run
```
./cua_start.sh mock
```

To add other packages to check, put the source code or the compiled class files, modify the `sootArgs` in `cua/RunContainerAnalysis.java` file to include all the class from that package, then run
```
./cua_start.sh <pkg>
```

Information will be showed in standard output start with `[cua]`. If there is elements that have less attributed in the container, it will be showed in standard output start with `[cua-report]`.

