package me.asmai.project.service;
import static me.asmai.project.repository.EntryPoint.getDataset;
import static me.asmai.project.repository.EntryPoint.sparkSession;
import me.asmai.project.repository.EndPoint;
import me.asmai.project.repository.EntryPoint;
import me.asmai.project.model.Cars;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.StreamingQueryException;
import java.util.concurrent.TimeoutException;

public class CarsService {

    public void getAllCars(int numberRows) throws StreamingQueryException, TimeoutException {
        EndPoint.displayDatasetWithCars(EntryPoint.getDataset(),numberRows,"append");
    }

    public void getCarsByModel(double model) throws StreamingQueryException, TimeoutException {

        Dataset<Cars> cars = EntryPoint.getDataset().filter("Model == \"" + model + "\"");
        EndPoint.displayDatasetWithCars(cars,407,"append");

    }

    public void getModelOfCarsByLessHorsePower() throws StreamingQueryException, TimeoutException {

        EntryPoint.getDataset().createOrReplaceTempView("cars");
        Dataset<Row> cars = sparkSession().sql("SELECT Model,MIN(Horsepower) FROM cars GROUP BY Model");
        EndPoint.displayDatasetWithRows(cars,407, "complete");
    }

    public void getCarsSortedByModelAndHorsePower() throws StreamingQueryException, TimeoutException {

        EntryPoint.getDataset().createOrReplaceTempView("cars");
        Dataset<Row> cars = getDataset()
                .groupBy("Model","Horsepower","car","MPG","Cylinders","Weight","Acceleration","Displacement")
                .count()
                .sort("Model","Horsepower");
        EndPoint.displayDatasetWithRows(cars,407,"complete");
    }

    public void getCarsByBetweenTwoModelsOfAnOriginAndSortedByHorsePower(double model1, double model2, String origin) throws StreamingQueryException, TimeoutException {
        Dataset<Row> cars = getDataset().filter((FilterFunction<Cars>) car -> car.getOrigin().equals(origin))
                .filter("Model >= \"" + model1 + "\"")
                .filter("Model <= \"" + model2 + "\"")
                .groupBy("Horsepower")
                .count()
                .sort("Horsepower");

        EndPoint.displayDatasetWithRows(cars,407,"complete");
    }

    public void getCarsByOriginAndSortedByModel(String origin) throws StreamingQueryException, TimeoutException {
        Dataset<Row> cars = getDataset().filter((FilterFunction<Cars>) car -> car.getOrigin().equals(origin))
                .groupBy("Model")
                .count()
                .sort("Model");
        EndPoint.displayDatasetWithRows(cars,407,"complete");
    }
}
