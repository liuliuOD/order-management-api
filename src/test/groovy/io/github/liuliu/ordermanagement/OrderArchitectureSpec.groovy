package io.github.liuliu.ordermanagement

import io.github.liuliu.ordermanagement.controller.OrderController
import io.github.liuliu.ordermanagement.controller.ProductController
import io.github.liuliu.ordermanagement.domain.dto.CreateOrderResultDto
import io.github.liuliu.ordermanagement.domain.dto.GetOrdersByUserQueryDto
import io.github.liuliu.ordermanagement.domain.dto.GetProductByIdQueryDto
import io.github.liuliu.ordermanagement.domain.dto.OrderDto
import io.github.liuliu.ordermanagement.domain.dto.PagedOrderDto
import io.github.liuliu.ordermanagement.domain.dto.PatchOrderCommandDto
import io.github.liuliu.ordermanagement.service.OrderService
import io.github.liuliu.ordermanagement.service.ProductService
import spock.lang.Specification

class OrderArchitectureSpec extends Specification {

    def "OrderService should expose DTO-only API for order operations"() {
        expect:
        OrderService.getDeclaredMethod("createOrder", io.github.liuliu.ordermanagement.domain.dto.CreateOrderCommandDto)
                .getReturnType() == CreateOrderResultDto
        OrderService.getDeclaredMethod("patchOrder", PatchOrderCommandDto)
                .getReturnType() == OrderDto
        OrderService.getDeclaredMethod("getOrdersByUserId", GetOrdersByUserQueryDto)
                .getReturnType() == PagedOrderDto
    }

    def "OrderController should not depend on OrderEntity"() {
        expect:
        !OrderController.declaredFields.any { it.type.name == 'io.github.liuliu.ordermanagement.domain.entity.OrderEntity' }
        !OrderController.declaredMethods.any { method ->
            method.returnType.name == 'io.github.liuliu.ordermanagement.domain.entity.OrderEntity' ||
                    method.parameterTypes.any { it.name == 'io.github.liuliu.ordermanagement.domain.entity.OrderEntity' }
        }
    }

    def "ProductService should expose DTO-only API for product operations"() {
        expect:
        ProductService.getDeclaredMethod("getProductById", GetProductByIdQueryDto)
                .getReturnType() == io.github.liuliu.ordermanagement.domain.dto.ProductDto
    }

    def "ProductController should not depend on Entity classes"() {
        expect:
        !ProductController.declaredFields.any { it.type.name.startsWith('io.github.liuliu.ordermanagement.domain.entity.') }
        !ProductController.declaredMethods.any { method ->
            method.returnType.name.startsWith('io.github.liuliu.ordermanagement.domain.entity.') ||
                    method.parameterTypes.any { it.name.startsWith('io.github.liuliu.ordermanagement.domain.entity.') }
        }
    }
}
