PKG=$1

javac programs/$PKG/*.java

javac -cp .:cua-lib/soot-3.0.1-jar-with-dependencies.jar cua/*.java
java -cp .:cua-lib/soot-3.0.1-jar-with-dependencies.jar cua.RunContainerUsageAnalysis $PKG
