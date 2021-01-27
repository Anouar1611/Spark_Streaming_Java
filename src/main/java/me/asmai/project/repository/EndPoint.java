package me.asmai.project.repository;

import me.asmai.project.model.Cars;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.concurrent.TimeoutException;

public class EndPoint {

    public EndPoint(){}

    public static void displayDatasetWithCars(Dataset<Cars> dataset, int numberRows, String outputMode) throws StreamingQueryException, TimeoutException {
        dataset.writeStream()
                .format("console")
                .outputMode(outputMode)
                .option("numRows",numberRows)
                .start()
                .awaitTermination();
    }

    public static void displayDatasetWithRows(Dataset<Row> dataset, int numberRows , String outputMode) throws StreamingQueryException, TimeoutException {
        dataset.writeStream()
                .format("console")
                .outputMode(outputMode)
                .option("numRows",numberRows)
                .start()
                .awaitTermination();
    }
}


