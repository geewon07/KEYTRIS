from fastapi import FastAPI, File, UploadFile, BackgroundTasks
from hdfs import InsecureClient
import subprocess
from konlpy.tag import Okt
import uvicorn
from pyspark.sql import SparkSession
from pyspark.sql.functions import udf
from pyspark.sql.types import ArrayType, StringType
from pyspark import SparkFiles
import re
from datetime import datetime
from tqdm import tqdm
import json
import csv

app = FastAPI()
hdfs_url = "http://ip-172-26-2-236:9870"
client = InsecureClient(hdfs_url, user='ubuntu')

@app.get("/delete")
async def delete():
    path = "/original-data/"
    file_list = client.list(path)
    print(file_list)
    for file_name in file_list:
        file_path = f"{path}/{file_name}"
        client.delete(file_path)

@app.post("/upload/{folder}/{sid}/{date}")
async def what(sid:int, date:str, folder:str, file: UploadFile = File(...)):
    print(sid, date,folder)
    with client.write(f"/{folder}/{sid}/{file.filename}") as writer:
        writer.write(file.file.read())
    return {"sid":sid, "date":date, "filename": file.filename}


@app.post("/upload")
async def upload(file: UploadFile = File(...)):
    # HDFS에 저장
    with client.write(f"/original-data/{file.filename}") as writer:
        writer.write(file.file.read())
    return {"filename": file.filename}

@app.get("/process")
async def processing():
    spark = SparkSession.builder \
        .appName("HDFS Data Processing") \
        .master("yarn") \
        .config("spark.driver.memory", "2g") \
        .config("spark.executor.memory", "4g") \
        .config("spark.yarn.jars", "hdfs://ip-172-26-2-236:9000/spark/jars/*.jar") \
        .getOrCreate()
    spark.sparkContext.addFile("filter_words.txt")

    with open(SparkFiles.get("filter_words.txt"), 'r', encoding='utf-8') as freq_file:
        filter_words = set(freq_file.read().splitlines())

    okt = Okt()
    today_date = datetime.now().strftime('%Y-%m-%d')
    hangul_pattern = re.compile('[^ㄱ-ㅎㅏ-ㅣ가-힣]+')

    sids = [i for i in range(100, 108)]

    for sid in sids:
        df = spark.read.json(f"hdfs://ip-172-26-2-236:9000/original-data/{today_date}_{sid}.json")
        df_processed = df_processed.withColumn("main_processed", process_text(df.main, filter_words))

        df_processed.write.csv(f"hdfs://ip-172-26-2-236:9000/data/{sid}/{today_date}.csv")



    spark.stop()


if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=5000)
