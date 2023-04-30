package com.hogly.reactivestreams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class EndSubscriber<T> implements Flow.Subscriber<T> {

  private Flow.Subscription subscription;
  public final List<T> consumedElements = new ArrayList<>();

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(1);
  }

  @Override
  public void onNext(T item) {
    System.out.println("Got : " + item);
    consumedElements.add(item);
    subscription.request(1);
  }

  @Override
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  @Override
  public void onComplete() {
    System.out.println("Done");

  }
}
