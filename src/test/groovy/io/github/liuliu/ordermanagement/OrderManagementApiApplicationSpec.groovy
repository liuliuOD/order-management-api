package io.github.liuliu.ordermanagement

import io.github.liuliu.ordermanagement.storage.MybatisStorageImpl
import io.github.liuliu.ordermanagement.storage.Storage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest
class OrderManagementApiApplicationSpec extends Specification {

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    Storage storage

    def "context loads"() {
        expect:
        true
    }

    def "storage bean should resolve to mybatis implementation"() {
        expect:
        storage instanceof MybatisStorageImpl
        applicationContext.containsBean("mybatisStorageImpl")
        !applicationContext.containsBean("storageImpl")
    }
}
