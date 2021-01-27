package me.asmai.project.repository;

import me.asmai.project.model.Cars;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class EntryPoint {

    public EntryPoint() { }

    public static SparkSession sparkSession(){
        return SparkSession
                .builder()
                .appName(" Application with Spark Streaming and Java")
                .master("local[*]")
                .getOrCreate();
    }

    private static StructType customSchema(){
        return new StructType(new StructField[] {
                new StructField("Car", DataTypes.StringType, true, Metadata.empty()),
                new StructField("MPG", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("Cylinders", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("Displacement", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("Horsepower", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("Weight", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("Acceleration", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("Model", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("Origin", DataTypes.StringType, true, Metadata.empty()),
        });
    }

    public static Dataset<Cars> getDataset(){
        Encoder<Cars> carsEncoder = Encoders.bean(Cars.class);
        return sparkSession()
                .readStream()
                .option("header","true")
                .option("treatEmptyValuesAsNulls", "true")
                .schema(customSchema())
                .option("delimiter",";")
                .csv("src/main/resources/data/cars*.csv")
                .as(carsEncoder);
    }

}
