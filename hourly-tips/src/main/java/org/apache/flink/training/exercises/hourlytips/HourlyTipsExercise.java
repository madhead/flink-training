/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.training.exercises.hourlytips;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.training.exercises.common.datatypes.TaxiFare;
import org.apache.flink.training.exercises.common.sources.TaxiFareGenerator;

/**
 * The Hourly Tips exercise from the Flink training.
 *
 * <p>The task of the exercise is to first calculate the total tips collected by each driver, hour
 * by hour, and then from that stream, find the highest tip total in each hour.
 */
public class HourlyTipsExercise {

    private final SourceFunction<TaxiFare> source;
    private final SinkFunction<Tuple3<Long, Long, Float>> sink;

    /** Creates a job using the source and sink provided. */
    public HourlyTipsExercise(
            SourceFunction<TaxiFare> source, SinkFunction<Tuple3<Long, Long, Float>> sink) {

        this.source = source;
        this.sink = sink;
    }

    /**
     * Main method.
     *
     * @throws Exception which occurs during job execution.
     */
    public static void main(String[] args) throws Exception {

        HourlyTipsExercise job =
                new HourlyTipsExercise(new TaxiFareGenerator(), new PrintSinkFunction<>());

        job.execute();
    }

    /**
     * Create and execute the hourly tips pipeline.
     *
     * @return {JobExecutionResult}
     * @throws Exception which occurs during job execution.
     */
    public JobExecutionResult execute() throws Exception {

        // set up streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // start the data generator
        DataStream<TaxiFare> fares = env.addSource(source);

        // replace this with your solution
        if (true) {
            throw new IllegalArgumentException();
        }

        // the results should be sent to the sink that was passed in
        // (otherwise the tests won't work)
        // you can end the pipeline with something like this:

        // DataStream<Tuple3<Long, Long, Float>> hourlyMax = ...
        // hourlyMax.addSink(sink);

        // execute the pipeline and return the result
        return env.execute("Hourly Tips");
    }
}

/*
public JobExecutionResult execute() throws Exception {
    // set up streaming execution environment
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    // start the data generator
    DataStream<TaxiFare> fares = env
            .addSource(source)
            .assignTimestampsAndWatermarks(
                    WatermarkStrategy
                            .<TaxiFare>forMonotonousTimestamps()
                            .withTimestampAssigner(
                                    (fare, elementTimeStamp) -> fare.getEventTimeMillis()
                            )
            );

    fares
            .map(fare -> Tuple2.of(fare.driverId, fare.tip))
            .returns(new TypeHint<Tuple2<Long, Float>>() {})

            .keyBy(tuple -> tuple.f0)

            .window(TumblingEventTimeWindows.of(Time.hours(1)))

            .reduce(
                    new HourlyTipsReduceFunction(),
                    new WrapWithWindowInfo()
            )

            .windowAll(TumblingEventTimeWindows.of(Time.hours(1)))

            .maxBy(2)

            .addSink(sink);

    return env.execute("Hourly Tips");
}

static class HourlyTipsReduceFunction implements ReduceFunction<Tuple2<Long, Float>> {
    @Override
    public Tuple2<Long, Float> reduce(Tuple2<Long, Float> value1, Tuple2<Long, Float> value2) throws Exception {
        return Tuple2.of(value1.f0, value1.f1 + value2.f1);
    }
}

static class WrapWithWindowInfo extends ProcessWindowFunction<Tuple2<Long, Float>, Tuple3<Long, Long, Float>, Long, TimeWindow> {
    @Override
    public void process(
            Long key,
            ProcessWindowFunction<Tuple2<Long, Float>, Tuple3<Long, Long, Float>, Long, TimeWindow>.Context context,
            Iterable<Tuple2<Long, Float>> elements,
            Collector<Tuple3<Long, Long, Float>> out
    ) throws Exception {
        out.collect(
                Tuple3.of(
                        context.window().getEnd(),
                        key,
                        (float) StreamSupport.stream(elements.spliterator(), false)
                                .mapToDouble(tuple -> tuple.f1)
                                .sum()
                )
        );
    }
}

 */