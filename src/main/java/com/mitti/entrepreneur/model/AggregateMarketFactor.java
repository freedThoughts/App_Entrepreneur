package com.mitti.entrepreneur.model;

public interface AggregateMarketFactor {
    double process(IMarketFactor marketFactor);
}
