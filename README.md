## gencode

数据库表转实体、生成通用jdbc代码

### 使用

1. 打包

> mvn install

2. 新建别名 

linux 系统下，在~/.bashrc目录下补充如下一行，命令行执行`source ~/.bashrc`设置完成，以后可在命令行中使用gencode命令 
> alias gencode='java -jar /home/root/.m2/repository/com/uetty/gencode/1.0.2-RC/gencode-1.0.2-RC-jar-with-dependencies.jar'` 

<!-- Win10 系统下，在`C:\Users\Administrator\Documents\WindowsPowerShell`目录下新建文件Microsoft.PowerShell_profile.ps1，输入如下内容，以后可在PowerShell中使用gencode命令
> function Gencode0 {
>     java -jar C:\Users\Administrator\.m2\repository\com\uetty\gencode\1.0.2-RC\gencode-1.0.2-RC-jar-with-dependencies.jar
> }
> Set-Alias gencode Gencode0
-->
3. 命令行查看操作命令
gencode
4. 模式选择 -m 1（或者2)
```
1封装mybatis generator自动生成实体和mapper.xml,生成在~/gencode目录下
2封装jdbc语句自动生成(需生成代码引用StatementUtil类)
```
   
5. 示例
通过以下命令查看选项
> gencode

示例使用命令（均要求连接数据库）
> gencode -m 2 --tb t_require --gt 7 --jo require

> gencode -m 2 --tb t_req_attachment --gt 1 --jo attachment

> gencode -m 1 --of /gencode/ --pe com.xxx.xxx.bean --pd com.xxx.xxx.dao --ds Dao --pf tb_

6. 配置shell文件`gencode.sh`使用
```
#!/bin/bash

# jar file path
JAR_PATH=/build/gencode.jar
# 驱动，基本不可能改动
DB_DRIVER=com.mysql.cj.jdbc.Driver
# 数据库连接符
DB_URL="jdbc:mysql://localhost:3306/local_demo?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&autoReconnect=true"
# 数据库用户名
DB_USER=root
# 数据库秘密
DB_PASS=123456
# 模式
MODE=1
# 自动生成的代码文件存储路径，代码以表为单位存在各个文件夹中
OUTPUT=/gencode
# 获取第一个参数作为bean的包名
BEAN_PACKAGE=$1
# 获取第二个参数作为dao的包名
DAO_PACKAGE=$2
# 获取第三个文件dao JAVA类名后缀，一般会选择：dao或mapper，手字母最终会被大写
DAONAME_PREFIX=$3
# 表名前缀，表设计时经常都会给每张表加上前缀
TABLE_PREFIX=tb_

java -jar "$JAR_PATH" -m $MODE -d "$DB_DRIVER" -s "$DB_URL" -u "$DB_USER" -p "$DB_PASS" --of "$OUTPUT" --pe "$BEAN_PACKAGE" --pd "$DAO_PACKAGE" --ds $DAONAME_PREFIX --pf $TABLE_PREFIX
```
给文件加可执行权限：
> sudo chmod a+x gencode.sh

使用
> ./gencode.sh com.uetty.entity com.uetty.dao dao

### Support or Contact


