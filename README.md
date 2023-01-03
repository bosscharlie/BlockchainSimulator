# BlockchainSmulator

本项目来自2022年清华大学赛博智能经济与区块链课程项目第10组，实现了一个事件驱动的区块链模拟系统。

## 使用方式

在顶层目录下通过如下命令进行仿真：

```
mvn clean install
mvn compile
java -cp target/BlockchainSimulator-0.0.1-SNAPSHOT.jar src/main/java/simulator/Main.java
http-server
```

Ref：

- [GitHub - bcasim/bcasim](https://github.com/bcasim/bcasim)

- [GitHub - dsg-titech/simblock: An open source blockchain network simulator.](https://github.com/dsg-titech/simblock)
