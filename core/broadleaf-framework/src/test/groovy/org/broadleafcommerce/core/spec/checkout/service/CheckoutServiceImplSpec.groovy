/*-
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.core.spec.checkout.service

import org.broadleafcommerce.common.event.BroadleafApplicationEventPublisher
import org.broadleafcommerce.core.checkout.service.CheckoutServiceImpl
import org.broadleafcommerce.core.checkout.service.exception.CheckoutException
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutProcessContextFactory
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutSeed
import org.broadleafcommerce.core.order.domain.Order
import org.broadleafcommerce.core.order.domain.OrderImpl
import org.broadleafcommerce.core.order.service.OrderService
import org.broadleafcommerce.core.order.service.type.OrderStatus
import org.broadleafcommerce.core.workflow.BaseActivity
import org.broadleafcommerce.core.workflow.DefaultErrorHandler
import org.broadleafcommerce.core.workflow.DefaultProcessContextImpl
import org.broadleafcommerce.core.workflow.ProcessContext
import org.broadleafcommerce.core.workflow.Processor
import org.broadleafcommerce.core.workflow.SequenceProcessor
import org.broadleafcommerce.core.workflow.state.ActivityStateManagerImpl
import org.springframework.beans.factory.support.StaticListableBeanFactory
import spock.lang.Specification

/**
 * BR-001 and BR-012. Status rejection is outside the workflow. A workflow activity
 * failure is wrapped as CheckoutException and does not continue to later activities.
 */
class CheckoutServiceImplSpec extends Specification {

    def "BR-001 submitted or cancelled orders cannot enter checkout"() {
        setup:
        Order order = new OrderImpl()
        order.id = 42
        order.status = status
        OrderService orderService = Mock()
        Processor workflow = Mock()
        CheckoutServiceImpl checkoutService = new CheckoutServiceImpl()
        checkoutService.orderService = orderService
        checkoutService.checkoutWorkflow = workflow

        when:
        checkoutService.performCheckout(order)

        then:
        CheckoutException ex = thrown()
        ex.message.contains("already been submitted or cancelled")
        ex.message.contains("42")
        0 * orderService.save(_, _)
        0 * workflow.doActivities(_)

        where:
        status << [OrderStatus.SUBMITTED, OrderStatus.CANCELLED]
    }

    def "BR-001 an in-process order is not rejected before the checkout workflow"() {
        setup:
        Order order = new OrderImpl()
        order.id = 7
        order.status = OrderStatus.IN_PROCESS
        ProcessContext<CheckoutSeed> workflowContext = new DefaultProcessContextImpl<>()
        workflowContext.seedData = new CheckoutSeed(order, new HashMap<String, Object>())
        OrderService orderService = Mock()
        orderService.save(_, false) >> order
        Processor workflow = Mock()
        CheckoutServiceImpl checkoutService = new CheckoutServiceImpl()
        checkoutService.orderService = orderService
        checkoutService.checkoutWorkflow = workflow
        checkoutService.eventPublisher = Mock(BroadleafApplicationEventPublisher)

        when:
        checkoutService.performCheckout(order)

        then:
        notThrown(CheckoutException)
        1 * workflow.doActivities(_) >> workflowContext
    }

    def "BR-012 an activity exception stops later activities and performCheckout throws CheckoutException"() {
        setup:
        new ActivityStateManagerImpl().init()
        boolean laterActivityRan = false
        def failingActivity = new BaseActivity<ProcessContext<CheckoutSeed>>() {
            @Override
            ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) {
                throw new IllegalArgumentException("gate failed")
            }
        }
        def laterActivity = new BaseActivity<ProcessContext<CheckoutSeed>>() {
            @Override
            ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) {
                laterActivityRan = true
                return context
            }
        }

        ActivityStateManagerImpl stateManager = new ActivityStateManagerImpl()
        stateManager.init()
        StaticListableBeanFactory beanFactory = new StaticListableBeanFactory() {
            @Override
            <T> T getBean(Class<T> requiredType, Object... args) {
                return (T) stateManager
            }
        }
        beanFactory.addBean("blActivityStateManager", stateManager)

        SequenceProcessor processor = new SequenceProcessor()
        processor.setBeanName("blCheckoutWorkflow")
        processor.setProcessContextFactory(new CheckoutProcessContextFactory())
        processor.setDefaultErrorHandler(new DefaultErrorHandler())
        processor.setActivities([failingActivity, laterActivity])
        processor.setBeanFactory(beanFactory)

        Order order = new OrderImpl()
        order.id = 11
        order.status = OrderStatus.IN_PROCESS
        OrderService orderService = Mock()
        orderService.save(_, false) >> order
        CheckoutServiceImpl checkoutService = new CheckoutServiceImpl()
        checkoutService.orderService = orderService
        checkoutService.checkoutWorkflow = processor

        when:
        checkoutService.performCheckout(order)

        then:
        CheckoutException ex = thrown()
        ex.cause instanceof IllegalArgumentException
        ex.cause.message == "gate failed"
        !laterActivityRan
    }
}
