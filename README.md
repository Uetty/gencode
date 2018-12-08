## gencode

数据库表转实体、生成通用jdbc代码

### 使用

```markdown
1. 打包
mvn install
2. 新建别名
alias gencode='java -jar /home/vince/.m2/repository/com/uetty/gencode/1.0.2-RC/gencode-1.0.2-RC-jar-with-dependencies.jar'
3. 命令行查看操作命令
gencode
4. 模式选择 -m 1（或者2)   1封装mybatis generator自动生成实体和mapper.xml,生成在~/gencode目录下   2封装jdbc语句自动生成(需生成代码引用StatementUtil类)
5. 示例
gencode -m 2 --tb t_require --gt 7 --jo require
gencode -m 2 --tb t_req_attachment --gt 1 --jo attachment
gencode -m 1 --pe com.xxx.plugin.yyy.mo

```
### Support or Contact


