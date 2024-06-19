import pandas as pd
from datetime import datetime, timedelta, time as dt_time
import requests
from prophet import Prophet
import random
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.font_manager as fm
import numpy as np
import warnings
import json

# 글꼴 설정
font_path = 'C:/Windows/Fonts/NanumGothic.ttf'
font_prop = fm.FontProperties(fname=font_path)
matplotlib.rcParams['font.family'] = font_prop.get_name()
matplotlib.use('TkAgg')
warnings.filterwarnings("ignore", category=FutureWarning)

BASE_URL = "http://localhost:4000/api"

def generate_fake_data(start_date, end_date, memberId):
    date_range = pd.date_range(start_date, end_date, freq='D')
    data = []
    for single_date in date_range:
        exit_time = (datetime.combine(single_date, datetime.min.time()) + 
                      timedelta(hours=7, minutes=45) + 
                      timedelta(minutes=random.randint(0, 30)))
        entry_time = (datetime.combine(single_date, datetime.min.time()) + 
                     timedelta(hours=18, minutes=45) + 
                     timedelta(minutes=random.randint(0, 30)))
        data.append({
            'memberId': memberId,
            'date': single_date.strftime('%Y-%m-%d'),
            'entryTime': entry_time.strftime('%H:%M:%S'),
            'exitTime': exit_time.strftime('%H:%M:%S')
        })
    return data

def insert_data(data):
    headers = {'Content-Type': 'application/json'}
    response = requests.post(f"{BASE_URL}/logTimes", json=data, headers=headers)
    if response.status_code != 200:
        print(f"Failed to insert data: {response.status_code} - {response.text}")
    else:
        print("Data inserted successfully")

if __name__ == "__main__":
    memberId = 'sanggi'  # 이 부분을 특정 memberId로 변경
    fake_data = generate_fake_data('2024-01-01', '2024-06-17', memberId)
    insert_data(fake_data)  # 생성된 데이터를 일괄적으로 삽입

def time_from_float(hours_float):
    hours = int(hours_float)
    minutes = int((hours_float - hours) * 60)
    return f"{hours:02d}:{minutes:02d}"

def get_prediction_date():
    now = datetime.now()
    if now.time() < datetime.strptime('09:00', '%H:%M').time():
        return now.date()
    elif now.time() < datetime.strptime('20:00', '%H:%M').time():
        return now.date()
    else:
        return now.date() + timedelta(days=1)
def fetch_and_predict(memberId):
    url = f"{BASE_URL}/logTimes?memberId={memberId}"
    response = requests.get(url)
    if response.status_code == 200:
        data = response.json()
        
        # JSON 데이터를 출력하여 확인
        print(json.dumps(data, indent=4))

        df = pd.DataFrame(data)
        df['date'] = pd.to_datetime(df['date'])

        # 컬럼 이름을 확인하여 사용
        print(df.columns)                                                                                                                                                                   

        # 컬럼 이름 변경
        df.rename(columns={'entryTime': 'entry_time', 'exitTime': 'exit_time'}, inplace=True)

        df['exit_time'] = pd.to_datetime(df['exit_time'], format='%H:%M:%S')
        df['entry_time'] = pd.to_datetime(df['entry_time'], format='%H:%M:%S')

        prediction_date = get_prediction_date()

        # 출근 시간 예측
        df['ds'] = df['date']
        df['y'] = df['exit_time'].dt.hour + df['exit_time'].dt.minute / 60
        model_exit = Prophet()
        model_exit.fit(df[['ds', 'y']])
        future_exit = model_exit.make_future_dataframe(periods=1)
        forecast_exit = model_exit.predict(future_exit)
        predicted_exit_time = forecast_exit.iloc[-1]['yhat']
        formatted_exit_time = time_from_float(predicted_exit_time)

        # 퇴근 시간 예측
        df['y'] = df['entry_time'].dt.hour + df['entry_time'].dt.minute / 60
        model_entry = Prophet()
        model_entry.fit(df[['ds', 'y']])
        future_entry = model_entry.make_future_dataframe(periods=1)
        forecast_entry = model_entry.predict(future_entry)
        predicted_entry_time = forecast_entry.iloc[-1]['yhat']
        formatted_entry_time = time_from_float(predicted_entry_time)

        return formatted_exit_time, formatted_entry_time
    else:
        print(f"Failed to fetch data: {response.status_code} - {response.text}")
        return None, None

def save_prediction(memberId, date, time, time_type):
    headers = {'Content-Type': 'application/json'}
    # Get existing predictions
    check_url = f"{BASE_URL}/getPredictedTimesByMemberId?memberId={memberId}"
    check_response = requests.get(check_url)

    payload = {
        'member': {'memberId': memberId},
        'date': date.strftime('%Y-%m-%d'),
        'time': time,
        'type': time_type
    }

    if check_response.status_code == 200:
        existing_data = check_response.json()
        # Check if specific prediction already exists
        prediction_exists = any(d for d in existing_data if d['date'] == date.strftime('%Y-%m-%d') and d['type'] == time_type)

        if prediction_exists:
            # Update existing prediction
            existing_id = next(d['id'] for d in existing_data if d['date'] == date.strftime('%Y-%m-%d') and d['type'] == time_type)
            update_url = f"{BASE_URL}/updatePrediction"
            payload['id'] = existing_id
            update_response = requests.post(update_url, json=payload, headers=headers)
            response_message = "updated" if update_response.status_code == 200 else f"update failed: {update_response.text}"
        else:
            # Save new prediction
            create_url = f"{BASE_URL}/savePrediction"
            create_response = requests.post(create_url, json=payload, headers=headers)
            response_message = "saved" if create_response.status_code == 200 else f"save failed: {create_response.text}"
        print(f"Prediction {response_message} successfully")
    elif check_response.status_code == 404:
        # No existing predictions, create new
        create_url = f"{BASE_URL}/savePrediction"
        create_response = requests.post(create_url, json=payload, headers=headers)
        response_message = "saved" if create_response.status_code == 200 else f"save failed: {create_response.text}"
        print(f"Prediction {response_message} successfully")
    else:
        print(f"Failed to retrieve existing data: {check_response.status_code} - {check_response.text}")

if __name__ == "__main__":
    memberId = 'sanggi'
    fake_data = generate_fake_data('2024-01-01', '2024-05-31', memberId)
    insert_data(fake_data)
    predicted_exit_time, predicted_entry_time = fetch_and_predict(memberId)
    if predicted_exit_time and predicted_entry_time:
        today = get_prediction_date()
        save_prediction(memberId, today, predicted_exit_time, 'exit_time')
        save_prediction(memberId, today, predicted_entry_time, 'entry_time')
        print(f"Member ID {memberId}'s predicted exit time: {predicted_exit_time}")
        print(f"Member ID {memberId}'s predicted entry time: {predicted_entry_time}")
    else:
        print("Failed to predict times")
