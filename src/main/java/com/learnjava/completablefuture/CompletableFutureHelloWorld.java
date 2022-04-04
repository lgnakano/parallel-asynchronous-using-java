package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {
    private final HelloWorldService hws;

    public CompletableFutureHelloWorld(HelloWorldService hws) {
        this.hws = hws;
    }

    public CompletableFuture<String> helloWorld() {
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase)
//                        .join()
        ;

    }

    public CompletableFuture<String> helloWorld_withSize() {
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase)
                .thenApply(s -> (s.length() + " - ").concat(s));
    }

    public String helloWorld_approach1() {
        startTimer();
        String hello = hws.hello();
        String world = hws.world();
        timeTaken();
        stopWatchReset();
        return hello + world;
    }

    public String helloworld_multiple_async_calls(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);

        String hw= hello
                .thenCombine(world, (h, w) -> h+w) // first, second
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }

    public String helloworld_3_async_calls(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw= hello
                .thenCombine(world, (h, w) -> h+w) // first, second
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }

    public String helloworld_3_async_calls_log(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw= hello
                .thenCombine(world, (h, w) -> {
                    log("thenCombine h/w");
                    return h+w;
                }) // first, second
                .thenCombine(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                })
                .thenApply(s -> {
                    log("thenApply toUpperCase");
                    return s.toUpperCase();
                })
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }

    public String helloworld_3_async_calls_log_async(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw= hello
                .thenCombineAsync(world, (h, w) -> {
                    log("thenCombine h/w");
                    return h+w;
                }) // first, second
                .thenCombineAsync(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                })
                .thenApplyAsync(s -> {
                    log("thenApply toUpperCase");
                    return s.toUpperCase();
                })
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }

    public String helloworld_3_async_calls_custom_threadpool(){
        startTimer();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture
                .supplyAsync(hws::hello, executorService);
        CompletableFuture<String> world = CompletableFuture
                .supplyAsync(hws::world, executorService);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture
                .supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        }, executorService);

        String hw= hello
                .thenCombine(world, (h, w) -> {
                    log("thenCombine h/w");
                    return h+w;
                }) // first, second
                .thenCombine(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                })
                .thenApply(s -> {
                    log("thenApply toUpperCase");
                    return s.toUpperCase();
                })
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }

    public String helloworld_3_async_calls_custom_threadpool_async(){
        startTimer();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture
                .supplyAsync(hws::hello, executorService);
        CompletableFuture<String> world = CompletableFuture
                .supplyAsync(hws::world, executorService);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture
                .supplyAsync(()->{
                    delay(1000);
                    return " Hi CompletableFuture!";
                }, executorService);

        String hw= hello
                .thenCombineAsync(world, (h, w) -> {
                    log("thenCombine h/w");
                    return h+w;
                }, executorService) // first, second
                .thenCombineAsync(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                }, executorService)
                .thenApplyAsync(s -> {
                    log("thenApply toUpperCase");
                    return s.toUpperCase();
                }, executorService)
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }


    public String helloworld_4_async_calls(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });
        CompletableFuture<String> byeCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Bye!";
        });

        String hw= hello
                .thenCombine(world, (h, w) -> h+w) // first, second
                .thenCombine(hiCompletableFuture, (previous, current) -> previous + current)
                .thenCombine(byeCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();
        stopWatchReset();
        return hw;
    }

    public CompletableFuture<String> helloWorld_thenCompose() {
        return CompletableFuture.supplyAsync(hws::hello)
                .thenCompose(hws::worldFuture)
                .thenApply(String::toUpperCase)
//                        .join()
                ;
    }

    public String anyOf() {
        // db
        var db = CompletableFuture.supplyAsync(() -> {
            delay(4000);
            log("response from db");
            return "hello world";
        });

        // restCall
        var restCall = CompletableFuture.supplyAsync(() -> {
            delay(2000);
            log("response from restCall");
            return "hello world";
        });

        //soapCall
        var soapCall = CompletableFuture.supplyAsync(() -> {
            delay(3000);
            log("response from soapCall");
            return "hello world";
        });

        var cfList = List.of(db, restCall, soapCall);
        CompletableFuture<Object> cfAnyOf = CompletableFuture.anyOf(cfList.toArray(new CompletableFuture[0]));

        return (String) cfAnyOf.thenApply(v -> {
            if (v instanceof String) {
                return v;
            } else
            return null;
        })
                .join();
    }

    public static void main(String[] args) {

        HelloWorldService hws = new HelloWorldService();

        CompletableFuture.supplyAsync(hws::helloWorld)
                        .thenApply(String::toUpperCase)
                                .thenAccept((result) ->
                                    log("Result is: " + result)
                                )
                                        .join();

        log("Done!");
//        delay(2000);
    }
}
