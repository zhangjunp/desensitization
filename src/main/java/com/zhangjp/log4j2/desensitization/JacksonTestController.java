package com.zhangjp.log4j2.desensitization;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建时间 2019年九月12日 星期四 15:38
 * 作者: zhangjp
 */

@RestController("jack")
public class JacksonTestController {

    @GetMapping("/test")
    public AddCardOutputVo test(AddCardOutputVo addCardOutputVo){
        addCardOutputVo.setAppSheetSerialNo("123456789");
        return addCardOutputVo;
    }


    @GetMapping("/test1")
    public String test1(@JsonSerialize CustCancellableTransactionInputVo addCardOutputVo){
        System.out.println("addCardOutputVo = " + addCardOutputVo.toString());
        return addCardOutputVo.getAppSheetSerialNo();
    }
}
