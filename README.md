### 日志脱敏

#####  写在前边：此demo主要介绍使用两种方式解决日志脱敏，当然也可以使用其他方式，比如通过脱敏工具类，或者自定义注解实现脱敏字段以及脱敏规则等等……
	
###### 如有更好的实现方式或者思路，欢迎相互交流 ，邮箱： `zjp20123258@163.com`

- 一、基于Log4j2实现日志脱敏，借助Log4j2的自定义插件特性，自定义JSON格式数据的脱敏插件，基于正则表达式实现，并支持自定义脱敏的规则；可在原有基础上扩展使其支持脱敏toString格式的数据。

- 二、 基于fastJson的ValueFliter实现字段脱敏

###### 使用方式（在log4j2.xml）配置文件中：

```java
 <MyPatternLayout pattern="${commonPattern}" header="${header}">
	 <jsonReplaces>
		 <jsonReplace keys="cardtelno,,mobileno,mobileNo" methodName="md5"/>
		 <jsonReplace keys="certificateno,certificateNo" methodName="default"/>
	 </jsonReplaces>
</MyPatternLayout>
```
1. keys: 为要脱敏的字段，以英文符号隔开，支持多个；
1. methodName: 为针对此keys的脱敏规则，支持自定义；

**自定义方式如下：**
```java
/**
 * 创建时间 2019年四月04日 星期四 10:45
 * 作者: zhangjunping
 * 描述：MD5方式脱敏器
 */
@Component
public class MD5Device implements DesensitizationStrategy {
    @Override
    public String produceCipherText(String plaintext) {
        return DigestUtils.md5DigestAsHex(plaintext.getBytes());
    }

    @PostConstruct
    @Override
    public void addDeviceToMap() {
		// 此处map的key即为，标签内methodName的属性
        DEVICE_METHOD_MAP.put("md5",this);
    }
}

```
###### 实现思路，借助Layout 最后一层，拦截日志，进行正则匹配替换脱敏字段


![log4j2类图](https://github.com/191824852/desensitization/blob/master/log4j2.jpg "log4j2类图")



###### 此demo中，还包含了，日志统一记录业务代码，以及请求源IP的例子！ 有需要的可以看一下，实现起来比较简单，利于日志排查！


### End
