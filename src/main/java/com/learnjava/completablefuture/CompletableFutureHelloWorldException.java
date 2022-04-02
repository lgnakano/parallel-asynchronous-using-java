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
                    log("Exception is: " + e.getMessage());
                    return " ";
                })
                .thenCombine(world, (h, w) -> h+w) // " world'"
                .handle((res2, e2) -> {
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
}
