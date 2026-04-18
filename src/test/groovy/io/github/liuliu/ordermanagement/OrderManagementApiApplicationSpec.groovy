package io.github.liuliu.ordermanagement

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class OrderManagementApiApplicationSpec extends Specification {

    def "context loads"() {
        expect:
        true
    }
}
