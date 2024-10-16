package com.order;

public class OutOfStockException extends Exception {

    public OutOfStockException(String msg) {
        super(msg);
    }
}
