package com.hair.management;

import com.hair.management.generate.MysqlGenerator;
import org.junit.jupiter.api.Test;

public class MysqlGeneratorTest {

    @Test
    public void testOneGenerator(){
        MysqlGenerator mysqlGenerator = new MysqlGenerator();
        mysqlGenerator.setXml(true);
//        mysqlGenerator.setController(true);
        mysqlGenerator.generator("hair", "vip_user","vip_account_info","hair_master","user_consumer_info");
    }
}
