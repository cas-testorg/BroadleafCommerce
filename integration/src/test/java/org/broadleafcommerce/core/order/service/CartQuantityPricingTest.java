/*-
 * #%L
 * BroadleafCommerce Integration
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
package org.broadleafcommerce.core.order.service;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.order.domain.FulfillmentGroupItem;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.call.OrderItemRequestDTO;
import org.broadleafcommerce.core.order.service.exception.AddToCartException;
import org.broadleafcommerce.core.order.service.exception.RemoveFromCartException;
import org.broadleafcommerce.core.order.service.exception.UpdateCartException;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import jakarta.annotation.Resource;

/**
 * Regression coverage for GitHub issue #7: cart subtotal must recalculate after quantity updates.
 */
public class CartQuantityPricingTest extends OrderBaseTest {

    @Resource(name = "blOrderItemService")
    private OrderItemService orderItemService;

    @Test
    @Transactional
    public void updateItemQuantityRepricesOrderTotalsAndSurvivesReload()
            throws AddToCartException, UpdateCartException, RemoveFromCartException, PricingException {
        Order order = setUpCartWithActiveSku();
        OrderItem item = order.getOrderItems().get(0);
        Money unitPrice = item.getRetailPrice();
        Money originalSubTotal = order.getSubTotal();
        Money originalTotal = order.getTotal();

        assert item.getQuantity() == 1;
        assert originalSubTotal != null;
        assert originalSubTotal.equals(unitPrice);

        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
        orderItemRequestDTO.setOrderItemId(item.getId());
        orderItemRequestDTO.setQuantity(3);

        order = orderService.updateItemQuantity(order.getId(), orderItemRequestDTO, true);

        OrderItem updatedItem = orderItemService.readOrderItemById(item.getId());
        assert updatedItem != null;
        assert updatedItem.getQuantity() == 3;

        FulfillmentGroupItem fgItem = order.getFulfillmentGroups().get(0).getFulfillmentGroupItems().get(0);
        assert fgItem.getQuantity() == 3;

        Money expectedSubTotal = unitPrice.multiply(3);
        assert order.getSubTotal().equals(expectedSubTotal);
        assert updatedItem.getTotalPrice().equals(expectedSubTotal);
        assert order.getTotal() != null;
        assert !order.getTotal().equals(originalTotal);

        order = orderService.findOrderById(order.getId());
        updatedItem = orderItemService.readOrderItemById(item.getId());
        assert updatedItem.getQuantity() == 3;
        assert order.getSubTotal().equals(expectedSubTotal);
        assert order.getTotal() != null;
        assert !order.getTotal().equals(originalTotal);
    }

    @Test
    @Transactional
    public void updateItemQuantityWithoutRepricingLeavesStaleSubtotal()
            throws AddToCartException, UpdateCartException, RemoveFromCartException, PricingException {
        Order order = setUpCartWithActiveSku();
        OrderItem item = order.getOrderItems().get(0);
        Money originalSubTotal = order.getSubTotal();

        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
        orderItemRequestDTO.setOrderItemId(item.getId());
        orderItemRequestDTO.setQuantity(3);

        order = orderService.updateItemQuantity(order.getId(), orderItemRequestDTO, false);

        assert orderItemService.readOrderItemById(item.getId()).getQuantity() == 3;
        assert order.getSubTotal().equals(originalSubTotal);
    }

    @Test
    @Transactional
    public void controllerStyleUpdateQuantitySaveWithoutRepricingCanLeaveStaleSubtotal()
            throws AddToCartException, UpdateCartException, RemoveFromCartException, PricingException {
        Order order = setUpCartWithActiveSku();
        OrderItem item = order.getOrderItems().get(0);
        Money unitPrice = item.getRetailPrice();
        Money originalSubTotal = order.getSubTotal();

        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
        orderItemRequestDTO.setOrderItemId(item.getId());
        orderItemRequestDTO.setQuantity(3);

        order = orderService.updateItemQuantity(order.getId(), orderItemRequestDTO, true);
        order = orderService.save(order, false);

        assert orderItemService.readOrderItemById(item.getId()).getQuantity() == 3;

        Money expectedSubTotal = unitPrice.multiply(3);
        assert order.getSubTotal().equals(expectedSubTotal);

        order = orderService.findOrderById(order.getId());
        assert order.getSubTotal().equals(expectedSubTotal);
        assert !order.getSubTotal().equals(originalSubTotal);
    }
}
