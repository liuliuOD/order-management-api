package io.github.liuliu.ordermanagement

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.liuliu.model.CreateOrderRequest
import io.github.liuliu.model.PatchOrderRequest
import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity
import io.github.liuliu.ordermanagement.domain.entity.UserEntity
import io.github.liuliu.ordermanagement.domain.enumtype.CalculationType
import io.github.liuliu.ordermanagement.mapper.OrderMapper
import io.github.liuliu.ordermanagement.mapper.ProductMapper
import io.github.liuliu.ordermanagement.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification
import spock.lang.Subject

import java.math.BigDecimal
import java.util.UUID

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderManagementIntegrationSpec extends Specification {

    @Autowired
    WebApplicationContext context

    @Subject
    MockMvc mockMvc
    ObjectMapper objectMapper

    def setup() {
        objectMapper = new ObjectMapper()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }


    @Autowired
    ProductMapper productMapper

    @Autowired
    UserMapper userMapper

    @Autowired
    OrderMapper orderMapper

    def "Task 3-1: Get Product by Id"() {
        given: "A product category and a product in the database"
        def categoryId = UUID.randomUUID()
        def category = ProductCategoryEntity.builder()
                .categoryId(categoryId)
                .categoryName("Electronics")
                .taxRate(new BigDecimal("0.1000"))
                .calculationType(CalculationType.DEFAULT)
                .build()
        productMapper.insertCategory(category)

        def productId = UUID.randomUUID()
        def product = ProductEntity.builder()
                .id(productId)
                .productCategoryId(categoryId)
                .productName("Laptop")
                .unitPrice(new BigDecimal("1000.0000"))
                .build()
        productMapper.insertProduct(product)

        expect: "Calling GET /api/v1/product/{id} returns the product with tax rate"
        mockMvc.perform(get("/api/v1/product/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id').value(productId.toString()))
                .andExpect(jsonPath('$.productName').value("Laptop"))
                .andExpect(jsonPath('$.unitPrice').value(1000.0))
                .andExpect(jsonPath('$.taxRate').value(0.1))

        and: "Calling with non-existent id returns 404"
        mockMvc.perform(get("/api/v1/product/{productId}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
    }

    def "Task 3-2: Order CRUD Operations"() {
        given: "A user, a product category, and a product"
        def userId = UUID.randomUUID()
        userMapper.insert(UserEntity.builder().id(userId).username("testuser").email("test@example.com").build())

        def categoryId = UUID.randomUUID()
        productMapper.insertCategory(ProductCategoryEntity.builder()
                .categoryId(categoryId)
                .categoryName("Electronics")
                .taxRate(new BigDecimal("0.1000"))
                .calculationType(CalculationType.DEFAULT)
                .build())

        def productId = UUID.randomUUID()
        productMapper.insertProduct(ProductEntity.builder()
                .id(productId)
                .productCategoryId(categoryId)
                .productName("Laptop")
                .unitPrice(new BigDecimal("1000.0000"))
                .build())

        when: "Creating an order"
        def createRequest = new CreateOrderRequest(userId: userId, productId: productId, orderAmount: 2)
        def createResult = mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()

        def orderId = UUID.fromString(objectMapper.readTree(createResult.response.contentAsString).get("id").asText())

        then: "Order is created with correct total cost"
        // 1000 * 2 * (1 + 0.1) = 2200
        def order = orderMapper.findById(orderId).get()
        order.totalCost == new BigDecimal("2200.0000")

        when: "Patching the order amount"
        def patchRequest = new PatchOrderRequest(orderAmount: 3)
        mockMvc.perform(patch("/api/v1/order/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.orderAmount').value(3))
                .andExpect(jsonPath('$.totalCost').value(3300.0)) // 1000 * 3 * 1.1 = 3300

        then: "Order amount and total cost are updated"
        orderMapper.findById(orderId).get().orderAmount == 3
        orderMapper.findById(orderId).get().totalCost == new BigDecimal("3300.0000")

        when: "Deleting the order"
        mockMvc.perform(delete("/api/v1/order/{orderId}", orderId))
                .andExpect(status().isNoContent())

        then: "Order is soft-deleted"
        !orderMapper.findById(orderId).isPresent()
    }

    def "Unsupported Content-Type should return 415 instead of 500"() {
        when: "Calling create order endpoint with unsupported media type"
        def result = mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.TEXT_PLAIN)
                .content("not-json"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath('$.code').value("ERR_UNSUPPORTED_MEDIA_TYPE"))
                .andExpect(jsonPath('$.message').value("Content-Type is not supported"))
                .andExpect(header().exists("X-Request-Id"))
                .andReturn()

        then:
        result.response.status == 415
    }

    def "Task 3-3: User Deletion and Query"() {
        given: "A user and their orders"
        def userId = UUID.randomUUID()
        userMapper.insert(UserEntity.builder().id(userId).username("testuser").email("test@example.com").build())

        def categoryId = UUID.randomUUID()
        productMapper.insertCategory(ProductCategoryEntity.builder()
                .categoryId(categoryId)
                .categoryName("Electronics")
                .taxRate(new BigDecimal("0.1000"))
                .calculationType(CalculationType.DEFAULT)
                .build())

        def productId = UUID.randomUUID()
        productMapper.insertProduct(ProductEntity.builder()
                .id(productId)
                .productCategoryId(categoryId)
                .productName("Laptop")
                .unitPrice(new BigDecimal("1000.0000"))
                .build())

        // Create 2 orders
        mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateOrderRequest(userId: userId, productId: productId, orderAmount: 1))))
        mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateOrderRequest(userId: userId, productId: productId, orderAmount: 2))))

        expect: "Retrieving orders for user returns paged list"
        mockMvc.perform(get("/api/v1/order/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.data.length()').value(2))
                .andExpect(jsonPath('$.pagination.totalItems').value(2))

        when: "Deleting the user"
        mockMvc.perform(delete("/api/v1/user/{userId}", userId))
                .andExpect(status().isNoContent())

        then: "User and their orders are soft-deleted"
        !userMapper.findById(userId).isPresent()
        mockMvc.perform(get("/api/v1/order/{userId}", userId))
                .andExpect(status().isNotFound()) // User not found
    }
}
