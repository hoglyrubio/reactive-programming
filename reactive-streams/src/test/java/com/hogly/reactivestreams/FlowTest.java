package com.hogly.reactivestreams;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FlowTest {

  @Test
  public void whenSubscribeToIt_thenShouldConsumeAll() {

    // given
    SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
    EndSubscriber<String> subscriber = new EndSubscriber<>();
    publisher.subscribe(subscriber);
    List<String> items = List.of("1", "x", "2", "x", "3", "x");

    // when
    assertThat(publisher.getNumberOfSubscribers(), equalTo(1));
    items.forEach(publisher::submit);
    publisher.close();

    // then
    await().atMost(1000, TimeUnit.MILLISECONDS)
        .untilAsserted(() -> assertThat(subscriber.consumedElements, equalTo(items)));
  }

  @Test
  public void whenSubscribeAndTransformElements_thenShouldConsumeAll()
      throws InterruptedException {

    // given
    SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
    TransformProcessor<String, Integer> transformProcessor
        = new TransformProcessor<>(Integer::parseInt);
    EndSubscriber<Integer> subscriber = new EndSubscriber<>();
    List<String> items = List.of("1", "2", "3");
    List<Integer> expectedResult = List.of(1, 2, 3);

    // when
    publisher.subscribe(transformProcessor);
    transformProcessor.subscribe(subscriber);
    items.forEach(publisher::submit);
    publisher.close();

    // then
    await().atMost(1000, TimeUnit.MILLISECONDS)
        .untilAsserted(() -> assertThat(subscriber.consumedElements, equalTo(expectedResult)));
  }

}
