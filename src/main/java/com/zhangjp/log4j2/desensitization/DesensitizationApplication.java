package com.zhangjp.log4j2.desensitization;

import com.alibaba.fastjson.JSON;
import com.zhangjp.log4j2.desensitization.fastjson.DesensitizationJsonFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
@ServletComponentScan
public class DesensitizationApplication {
    private static final Logger logger = LoggerFactory.getLogger(DesensitizationApplication.class);

    @Resource
    private DesensitizationJsonFilter valueFilter;

    public static void main(String[] args) {
        SpringApplication.run(DesensitizationApplication.class, args);
        Test test = new Test("17699998888", "370281199911113090", "6217000088050017995", "dww123456zzz", "zhangjp@777.com");
        logger.error("{}", JSON.toJSONString(test, true));
        System.out.println("=====启动成功=====");
        logger.error("{}", JSON.toJSONString(new MyObj("fastJson", "zhangjp"), new DesensitizationJsonFilter()));

    }

    @GetMapping("/index")
    public String index() {
        Test test = new Test("17699998888", "370281199911113090", "6217000088050017995", "dww123456zzz", "zhangjp@777.com");
        logger.error("{}", JSON.toJSONString(test, valueFilter));
        logger.error("============end==============");
        return "Index";
    }


    static class MyObj {
        public MyObj(String fastJson, String name) {
            this.fastJson = fastJson;
            this.name = name;
        }

        private String fastJson;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFastJson() {
            return fastJson;
        }

        public void setFastJson(String fastJson) {
            this.fastJson = fastJson;
        }
    }

    static class Test {
        public Test(String cardtelno, String certificateno, String depositacct, String tpasswd, String email) {
            this.cardtelno = cardtelno;
            this.certificateno = certificateno;
            this.depositacct = depositacct;
            this.tpasswd = tpasswd;
            this.email = email;
        }

        private String cardtelno;
        private String certificateno;
        private String depositacct;
        private String tpasswd;
        private String email;


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCardtelno() {
            return cardtelno;
        }

        public void setCardtelno(String cardtelno) {
            this.cardtelno = cardtelno;
        }

        public String getCertificateno() {
            return certificateno;
        }

        public void setCertificateno(String certificateno) {
            this.certificateno = certificateno;
        }

        public String getDepositacct() {
            return depositacct;
        }

        public void setDepositacct(String depositacct) {
            this.depositacct = depositacct;
        }

        public String getTpasswd() {
            return tpasswd;
        }

        public void setTpasswd(String tpasswd) {
            this.tpasswd = tpasswd;
        }

    }
}
