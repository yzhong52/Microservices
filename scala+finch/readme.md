# Sbt

```
$ sbt --version    
sbt version in this project: 	1.3.12
sbt script version: 1.3.12
```

# Build Docker Image

We use [sbt-native-packager](https://www.scala-sbt.org/sbt-native-packager/formats/docker.html)
for publishing docker images. 

```
sbt docker:publishLocal
```
