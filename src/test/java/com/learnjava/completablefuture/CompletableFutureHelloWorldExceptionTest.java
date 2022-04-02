package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.learnjava.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldExceptionTest {

    @Mock
    HelloWorldService hws = mock(HelloWorldService.class);

    @InjectMocks
    CompletableFutureHelloWorldException hwcfe;

    @Test
    void helloworld_3_async_calls_handle() {
        //given
        when(hws.world()).thenCallRealMethod();
        when(hws.hello()).thenThrow(new RuntimeException("Exception occurred in hello!!"));


        //when
        String result = hwcfe.helloworld_3_async_calls_handle();

        //then
        log(result);
        assertEquals(" HI COMPLETABLEFUTURE!", result);
    }
    @Test
    void helloworld_3_async_calls_handle_2() {
        //given
        when(hws.hello()).thenThrow(new RuntimeException("Exception occurred in hello!"));
        when(hws.world()).thenThrow(new RuntimeException("Exception occurred in world!"));

        //when
        String result = hwcfe.helloworld_3_async_calls_handle();

        //then
        assertEquals(" HI COMPLETABLEFUTURE!", result);
    }
    @Test
    void helloworld_3_async_calls_handle_3() {
        //given
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenThrow(new RuntimeException("Exception occurred in world!"));

        //when
        String result = hwcfe.helloworld_3_async_calls_handle();

        //then
        assertEquals(" HI COMPLETABLEFUTURE!", result);
    }
    @Test
    void helloworld_3_async_calls_handle_4() {
        //given
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();
        //when
        String result = hwcfe.helloworld_3_async_calls_handle();

        //then
        assertEquals(" HELLO WORLD! HI COMPLETABLEFUTURE!", result);
    }
}
