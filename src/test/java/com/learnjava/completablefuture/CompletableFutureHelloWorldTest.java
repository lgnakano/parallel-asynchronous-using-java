package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {

    HelloWorldService hws = new HelloWorldService();
    CompletableFutureHelloWorld cfhw = new CompletableFutureHelloWorld(hws);

    @Test
    void helloWorld() {

        //given

        //when
        CompletableFuture<String> completableFuture =
                cfhw.helloWorld();

        //then
        completableFuture
                .thenAccept(s -> assertEquals("HELLO WORLD", s))
                .join();
    }

    @Test
    void helloWorld_withSize() {
        // given

        //when
        CompletableFuture<String> completableFuture =
                cfhw.helloWorld_withSize();

        //then
        completableFuture
                .thenAccept(s -> assertEquals("11 - HELLO WORLD", s))
                .join();
    }

    @Test
    void helloWorld_approach1() {

        //given

        //when

        //then
        assertEquals("hello world!",
                cfhw.helloWorld_approach1());
    }

    @Test
    void helloworld_multiple_async_calls() {
        // given

        //when

        //then
        assertEquals("HELLO WORLD!",
                cfhw.helloworld_multiple_async_calls());
    }

    @Test
    void helloworld_3_async_calls() {
        // given

        //when

        //then
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!",
                cfhw.helloworld_3_async_calls());
    }

    @Test
    void helloworld_3_async_calls_log() {
        // given

        //when

        //then
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!",
                cfhw.helloworld_3_async_calls_log());
    }

    @Test
    void helloworld_3_async_calls_log_async() {
        // given

        //when

        //then
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!",
                cfhw.helloworld_3_async_calls_log_async());
    }


    @Test
    void helloworld_3_async_calls_custom_threadpool() {
        // given

        //when

        //then
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!",
                cfhw.helloworld_3_async_calls_custom_threadpool());
    }

    @Test
    void helloworld_3_async_calls_custom_threadpool_async() {
        // given

        //when

        //then
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!",
                cfhw.helloworld_3_async_calls_custom_threadpool_async());
    }

    @Test
    void helloworld_4_async_calls() {
        // given

        //when

        //then
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE! BYE!",
                cfhw.helloworld_4_async_calls());
    }

    @Test
    void helloWorld_thenCompose() {

        //given
        startTimer();
        //when
        CompletableFuture<String> completableFuture =
                cfhw.helloWorld_thenCompose();

        //then
        completableFuture
                .thenAccept(s -> assertEquals("HELLO WORLD!", s))
                .join();
        timeTaken();
        stopWatchReset();
    }

    @Test
    void  anyOf() {
        // given
        startTimer();
        // when
        String helloWorld = cfhw.anyOf();
        //then
        assertEquals("hello world", helloWorld);
        timeTaken();
        stopWatchReset();
    }

}

