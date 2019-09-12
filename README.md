## gencode

数据库表转实体、生成通用jdbc代码

### 使用

1. 打包
> mvn install
2. 新建别名
linux 系统下，在~/.bashrc目录下补充如下一行，命令行执行`source ~/.bashrc`设置完成，以后可在命令行中使用gencode命令
> alias gencode='java -jar /home/root/.m2/repository/com/uetty/gencode/1.0.2-RC/gencode-1.0.2-RC-jar-with-dependencies.jar'`
Win10 系统下，在`C:\Users\Administrator\Documents\WindowsPowerShell`目录下新建文件Microsoft.PowerShell_profile.ps1，输入如下内容，以后可在PowerShell中使用gencode命令
> function Gencode0 {
>     java -jar C:\Users\Administrator\.m2\repository\com\uetty\gencode\1.0.2-RC\gencode-1.0.2-RC-jar-with-dependencies.jar
> }
> Set-Alias gencode Gencode0
3. 命令行查看操作命令
gencode
4. 模式选择 -m 1（或者2)   1封装mybatis generator自动生成实体和mapper.xml,生成在~/gencode目录下   2封装jdbc语句自动生成(需生成代码引用StatementUtil类)
5. 示例
> gencode -m 2 --tb t_require --gt 7 --jo require
> gencode -m 2 --tb t_req_attachment --gt 1 --jo attachment
> gencode -m 1 --pe com.xxx.plugin.yyy.mo



### Support or Contact


