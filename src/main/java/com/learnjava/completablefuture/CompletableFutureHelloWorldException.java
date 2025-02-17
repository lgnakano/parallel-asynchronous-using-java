package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldException {
    private final HelloWorldService hws;

    public CompletableFutureHelloWorldException(HelloWorldService hws) {
        this.hws = hws;
    }
    public String helloworld_3_async_calls_handle(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                .handle((res, e) -> {
                    if (null != e) {
                        log("Exception is: " + e.getMessage());
                        return "";
                    } else {
                        return res;
                    }
                })
                .thenCombine(world, (h, w) -> h+w) // " world'"
                .handle((res2, e2) -> {
                    if (null != e2) {
                        log("Exception after world is: " + e2);
                        return "";
                    } else {
                        return res2;
                    }
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                // " world! Hi CompletableFuture!
                .thenApply(String::toUpperCase)
                //" WORLD! HI COMPLETABLEFUTURE!"
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }
    public String helloworld_3_async_calls_exceptionally(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                .exceptionally((e) -> {
                    log("Exception is: " + e.getMessage());
                    return "";
                })
                .thenCombine(world, (h, w) -> h+w) // " world'"
                .exceptionally((e2) -> {
                    log("Exception after world is: " + e2);
                    return "";
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                // " world! Hi CompletableFuture!
                .thenApply(String::toUpperCase)
                //" WORLD! HI COMPLETABLEFUTURE!"
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }
    public String helloworld_3_async_calls_whenComplete(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                .whenComplete((res, e) -> {
                    log("res after hello is: " + res);
                    if (null != e) {
                        log("Exception in whenComplete is: " + e.getMessage());
                    }
                })
                .exceptionally((e) -> {
                    log("Exception in exceptionally is: " + e.getMessage());
                    return "";
                })
                .thenCombine(world, (h, w) -> h+w) // " world'"
                .whenComplete((res, e) -> {
                    log("res after world is: " + res);
                    if (null != e) {
                        log("Exception in exceptionally after world is: " + e);
                    }
                })
                .exceptionally((e2) -> {
                    log("Exception in exceptionally after world is: " + e2);
                    return "";
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                // " world! Hi CompletableFuture!
                .thenApply(String::toUpperCase)
                //" WORLD! HI COMPLETABLEFUTURE!"
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }
}
