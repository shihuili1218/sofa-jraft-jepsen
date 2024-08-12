
# 打开gitpod
https://gitpod.io/#/github.com/sofastack/sofa-jraft

# 准备容器
所有操作使用root，密码为123456
```shell
docker run --cap-add=NET_ADMIN --cap-add=NET_RAW --privileged -itd --hostname control --name control  shihuili1218/jraft-jepsen-yy-root &&
docker run --cap-add=NET_ADMIN --cap-add=NET_RAW --privileged -itd --hostname n1 --name n1  shihuili1218/jraft-jepsen-yy-root &&
docker run --cap-add=NET_ADMIN --cap-add=NET_RAW --privileged -itd --hostname n2 --name n2  shihuili1218/jraft-jepsen-yy-root &&
docker run --cap-add=NET_ADMIN --cap-add=NET_RAW --privileged -itd --hostname n3 --name n3  shihuili1218/jraft-jepsen-yy-root &&
docker run --cap-add=NET_ADMIN --cap-add=NET_RAW --privileged -itd --hostname n4 --name n4  shihuili1218/jraft-jepsen-yy-root &&
docker run --cap-add=NET_ADMIN --cap-add=NET_RAW --privileged -itd --hostname n5 --name n5  shihuili1218/jraft-jepsen-yy-root
```

启动ssh服务
```shell
docker exec -it control service ssh restart && docker exec -it n1 service ssh restart && docker exec -it n2 service ssh restart && docker exec -it n3 service ssh restart && docker exec -it n4 service ssh restart && docker exec -it n5 service ssh restart
```

每台服务器/etc/hosts文件添加
```
172.17.0.3 n1
172.17.0.4 n2
172.17.0.5 n3
172.17.0.6 n4
172.17.0.7 n5
```

在control上执行
```shell
ssh-keygen -t rsa -m PEM
ssh-copy-id -i /root/.ssh/id_rsa.pub root@n1
ssh-copy-id -i /root/.ssh/id_rsa.pub root@n2
ssh-copy-id -i /root/.ssh/id_rsa.pub root@n3
ssh-copy-id -i /root/.ssh/id_rsa.pub root@n4
ssh-copy-id -i /root/.ssh/id_rsa.pub root@n5
```

# 编译

注意：shihuili1218/jraft-jepsen-yy-root 镜像中的sofa-jraft、sofa-jraft-jepsen项目，是shihuili1218的仓库。

下载最新的代码，sofa-jraft，更新pom.xml中的版本号，然后执行
```shell
mvn clean install -DskipTests=true
```

打包atomic-server，并将其推到n1 ~ n2
```shell
sh control self-install

export CONTROL_ROOT=yes
sh control run jraft build
sh control run jraft deploy
```

# 测试

如果执行失败，可以到n1 ~ n5 /home/admin/atomic-server/atomic.log 找找原因

开始 jepsen 验证，一共 6 项 test case

- configuration-test: remove and add a random node

    ```sh run_test.sh --testfn configuration-test```

- bridge-test: weaving the network into happy little intersecting majority rings

  `sh run_test.sh --testfn bridge-test`

- pause-test: pausing random node with SIGSTOP/SIGCONT

  `sh run_test.sh --testfn pause-test`

- crash-test: killing random nodes and restarting them

  `sh run_test.sh --testfn crash-test`

- partition-test: Cuts the network into randomly chosen halves

  `sh run_test.sh --testfn partition-test`

- partition-majority-test: Cuts the network into randomly majority groups

  `sh run_test.sh --testfn partition-majority-test`


测试日志及结果

在 store 目录中存放测试日志，通过日志可以查看测试结果。



# shihuili1218/jraft-jepsen-yy-root 
镜像中已安装的软件
```shell
apt-get install openjdk-8-jdk openjdk-8-jre openjdk-8-jre-headless libjna-java gnuplot graphviz maven iptables git -y
update-alternatives --set iptables /usr/sbin/iptables-legacy
cd /usr/local/bin 
wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein  
chmod 755 lein
lein
```
