package com.xlx.utility;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

public class InfluxdbTest {
	public static void main(String[] args) {
		InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "admin", "admin");
		// 创建数据库
		//influxDB.createDatabase("mydb");
		
		Point point1 = Point.measurement("cpu")  
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)  
                .addField("idle", 90.0)
                .addField("system", 1.0)  
                .build();  
		
		//通过如下语句查看数据库的现有策略
		// SHOW RETENTION POLICIES ON telegraf
		influxDB.write("mydb", "autogen", point1);
		
		Query query = new Query("SELECT * FROM cpu", "mydb");  
		QueryResult result = influxDB.query(query);
		for(Result r : result.getResults())
		{
			for(Series s : r.getSeries())
			{
				s.getName();
				List<String> columns = s.getColumns();
				for(List<Object> l : s.getValues())
				{
					int count =columns.size();
					for(int i = 0; i < count; ++i)
					{
						System.out.println(columns.get(i) + ":" + l.get(i).toString());
					}
				}
			}
		}
	}
}
