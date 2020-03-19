/*
 *  Copyright (c) 2017, salesforce.com, inc.
 *  All rights reserved.
 *  Licensed under the BSD 3-Clause license.
 *  For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.rxgrpc;

import com.google.protobuf.Empty;
import com.salesforce.servicelibs.NumberProto;
import com.salesforce.servicelibs.RxNumbersGrpc;
import io.grpc.testing.GrpcServerRule;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("Duplicates")
public class BackpressureIntegrationTest {
    @Rule
    public GrpcServerRule serverRule = new GrpcServerRule();

    @Rule
    public UnhandledRxJavaErrorRule errorRule = new UnhandledRxJavaErrorRule().autoVerifyNoError();

    private static final int NUMBER_OF_STREAM_ELEMENTS = 140;

    private static AtomicLong lastValueTime;
    private static AtomicLong numberOfWaits;

    private static class TestService extends RxNumbersGrpc.NumbersImplBase {
        @Override
        public Single<NumberProto.Number> requestPressure(Flowable<NumberProto.Number> request) {
            return request
                    .map(proto -> proto.getNumber(0))
                    .doOnNext(i -> System.out.println("    --> " + i))
                    .doOnNext(i -> waitIfValuesAreEqual(i, 3))
                    .last(-1)
                    .map(BackpressureIntegrationTest::protoNum);
        }

        @Override
        public Flowable<NumberProto.Number> responsePressure(Single<Empty> request) {
            return Flowable
                    .fromIterable(IntStream.range(0, NUMBER_OF_STREAM_ELEMENTS)::iterator)
                    .doOnNext(i -> System.out.println("   <-- " + i))
                    .doOnNext(i -> updateNumberOfWaits(lastValueTime, numberOfWaits))
                    .map(BackpressureIntegrationTest::protoNum);
        }

        @Override
        public Flowable<NumberProto.Number> twoWayRequestPressure(Flowable<NumberProto.Number> request) {
            return requestPressure(request).toFlowable();
        }

        @Override
        public Flowable<NumberProto.Number> twoWayResponsePressure(Flowable<NumberProto.Number> request) {
            request.subscribe();
            return responsePressure(null);
        }
    }

    @Before
    public void resetServerStats() {
        lastValueTime = new AtomicLong(0);
        numberOfWaits = new AtomicLong(0);
    }

    @Test
    public void clientToServerBackpressure() {
        serverRule.getServiceRegistry().addService(new TestService());
        RxNumbersGrpc.RxNumbersStub stub = RxNumbersGrpc.newRxStub(serverRule.getChannel());

        Flowable<NumberProto.Number> rxRequest = Flowable
                .fromIterable(IntStream.range(0, NUMBER_OF_STREAM_ELEMENTS)::iterator)
                .doOnNext(i -> System.out.println(i + " --> "))
                .doOnNext(i -> updateNumberOfWaits(lastValueTime, numberOfWaits))
                .map(BackpressureIntegrationTest::protoNum);

        TestObserver<NumberProto.Number> rxResponse = rxRequest.as(stub::requestPressure).test();

        rxResponse.awaitTerminalEvent(5, TimeUnit.SECONDS);
        rxResponse.assertComplete()
                .assertValue(v -> v.getNumber(0) == NUMBER_OF_STREAM_ELEMENTS - 1);

        assertThat(numberOfWaits.get()).isEqualTo(1);
    }

    @Test
    public void serverToClientBackpressure() {
        serverRule.getServiceRegistry().addService(new TestService());
        RxNumbersGrpc.RxNumbersStub stub = RxNumbersGrpc.newRxStub(serverRule.getChannel());

        Single<Empty> rxRequest = Single.just(Empty.getDefaultInstance());

        TestSubscriber<NumberProto.Number> rxResponse = rxRequest.as(stub::responsePressure)
                .doOnNext(n -> System.out.println(n.getNumber(0) + "  <--"))
                .doOnNext(n -> waitIfValuesAreEqual(n.getNumber(0), 3))
                .test();

        rxResponse.awaitTerminalEvent(5, TimeUnit.SECONDS);
        rxResponse.assertComplete()
                .assertValueCount(NUMBER_OF_STREAM_ELEMENTS);

        assertThat(numberOfWaits.get()).isEqualTo(1);
    }

    @Test
    public void bidiResponseBackpressure() {
        serverRule.getServiceRegistry().addService(new TestService());
        RxNumbersGrpc.RxNumbersStub stub = RxNumbersGrpc.newRxStub(serverRule.getChannel());

        TestSubscriber<NumberProto.Number> rxResponse = Flowable.<NumberProto.Number>empty()
                .compose(stub::twoWayResponsePressure)
                .doOnNext(n -> System.out.println(n.getNumber(0) + "  <--"))
                .doOnNext(n -> waitIfValuesAreEqual(n.getNumber(0), 3))
                .test();

        rxResponse.awaitTerminalEvent(5, TimeUnit.SECONDS);
        rxResponse.assertComplete()
                .assertValueCount(NUMBER_OF_STREAM_ELEMENTS);

        assertThat(numberOfWaits.get()).isEqualTo(1);
    }

    @Test
    public void bidiRequestBackpressure() {
        serverRule.getServiceRegistry().addService(new TestService());
        RxNumbersGrpc.RxNumbersStub stub = RxNumbersGrpc.newRxStub(serverRule.getChannel());

        Flowable<NumberProto.Number> rxRequest = Flowable
                .fromIterable(IntStream.range(0, NUMBER_OF_STREAM_ELEMENTS)::iterator)
                .doOnNext(i -> System.out.println(i + " --> "))
                .doOnNext(i -> updateNumberOfWaits(lastValueTime, numberOfWaits))
                .map(BackpressureIntegrationTest::protoNum);

        TestSubscriber<NumberProto.Number> rxResponse = rxRequest.compose(stub::twoWayRequestPressure).test();

        rxResponse.awaitTerminalEvent(5, TimeUnit.SECONDS);
        rxResponse.assertComplete()
                .assertValue(v -> v.getNumber(0) == NUMBER_OF_STREAM_ELEMENTS - 1);

        assertThat(numberOfWaits.get()).isEqualTo(1);
    }


    private static void updateNumberOfWaits(AtomicLong start, AtomicLong maxTime) {
        Long now = System.currentTimeMillis();
        Long startValue = start.get();
        if (startValue != 0 && now - startValue > 1000) {
            maxTime.incrementAndGet();
        }
        start.set(now);
    }

    private static void waitIfValuesAreEqual(int value, int other) {
        if (value == other) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
    }

    private static NumberProto.Number protoNum(int i) {
        Integer[] ints = new Integer[32 * 1024];
        Arrays.setAll(ints, operand -> i);

        return NumberProto.Number.newBuilder().addAllNumber(Arrays.asList(ints)).build();
    }
}
