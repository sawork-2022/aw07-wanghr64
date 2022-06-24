package com.micropos.delivery.rest;

import com.micropos.carts.api.*;
import com.micropos.carts.dto.*;
import com.micropos.carts.mapper.ItemMapper;
import com.micropos.carts.model.Item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.*;
import java.util.function.Consumer;

@RestController
@RequestMapping("/order")
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.micropos.carts.mapper")
public class DeliveryController implements DeliveryApi {

    private final ItemMapper itemMapper;

    class OrderChecker implements Consumer<Item[]> {
        @Override
        public void accept(Item[] items) {
            orders.add((List<ItemDto>) itemMapper.toItemsDto(Arrays.asList(items)));
        }
    }

    public DeliveryController(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Bean
    public Consumer<Item[]> checkLoan() {
        return new OrderChecker();
    }

    private List<List<ItemDto>> orders;

    @Override
    @GetMapping()
    public ResponseEntity<List<List<ItemDto>>> viewDelivery() {
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}