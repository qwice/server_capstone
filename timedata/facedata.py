import cv2  # pylint: disable=no-member
import numpy as np
import requests
from datetime import datetime, time

# 사용자 인식 haar 알고리즘 연동
face_cascade = cv2.CascadeClassifier('C:\\capstone2\\capstone\\haar\\haarcascade_frontalface_default.xml')  # pylint: disable=no-member
recognizer = cv2.face.LBPHFaceRecognizer_create()  # pylint: disable=no-member
recognizer.read('C:\\capstone2\\capstone\\haar\\trainer\\trainer.yml')

# 출퇴근 시간대 설정
exit_start_time = time(7, 45)
exit_end_time = time(8, 15)
entry_start_time = time(18, 45)
entry_end_time = time(19, 15)

# ID를 닉네임으로 매핑
id_to_nickname = {
    1: "seoyoung",
    2: "sanggi",
    # 추가적인 사용자 ID와 닉네임 매핑
}

def is_within_time_range(start_time, end_time):
    now = datetime.now().time()
    return start_time <= now <= end_time

def get_member_info_from_id(member_id):
    url = f"http://localhost:4000/api/getUserByMemberId/{member_id}"
    try:
        print(f"Request URL: {url}")
        response = requests.get(url)
        print(f"Response Status Code: {response.status_code}")
        print(f"Response Content: {response.content.decode()}")

        if response.status_code == 200:
            member_info = response.json()  # Adjusted to fetch the correct JSON object
            return member_info['memberId'], member_info['memberId']
        else:
            return "Unknown User", None
    except Exception as e:
        print(f"Error retrieving member info: {e}")
        return "Unknown User", None

def log_time_to_server(member_id, current_time, log_type):
    url = "http://localhost:4000/api/logTime"
    data = {
        "memberId": member_id,
        "date": current_time.strftime('%Y-%m-%d'),
        "entryTime": current_time.strftime('%H:%M:%S') if log_type == 'entry' else None,
        "exitTime": current_time.strftime('%H:%M:%S') if log_type == 'exit' else None,
        "type": log_type
    }
    try:
        response = requests.post(url, json=data)
        print(f"Server response status code: {response.status_code}")
        print(f"Server response content: {response.content.decode()}")
        response.raise_for_status()  # Check if the request was successful
        if response.headers.get('Content-Type') == 'application/json':
            print("Log to server:", response.json())
        else:
            print("Log to server:", response.text)
    except Exception as e:
        print(f"Error sending data to server: {e}")

# 웹캠 열기
cap = cv2.VideoCapture(0)  # pylint: disable=no-member

while True:
    ret, frame = cap.read()
    if not ret:
        break

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)  # pylint: disable=no-member
    faces = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))  # pylint: disable=no-member

    for (x, y, w, h) in faces:
        id, confidence = recognizer.predict(gray[y:y+h, x:x+w])  # pylint: disable=no-member
        if confidence < 100:
            member_id = id_to_nickname.get(id, "Unknown User")  # id를 닉네임으로 변환
            print(f"Detected {member_id} with confidence: {confidence}")
            current_time = datetime.now()
            if is_within_time_range(entry_start_time, entry_end_time):
                log_time_to_server(member_id, current_time, 'entry')  # memberId로 로그 기록
            elif is_within_time_range(exit_start_time, exit_end_time):
                log_time_to_server(member_id, current_time, 'exit')  # memberId로 로그 기록
            cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)  # pylint: disable=no-member
            cv2.putText(frame, member_id, (x+5, y-5), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 2)  # pylint: disable=no-member
        else:
            cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 0, 255), 2)  # pylint: disable=no-member

    cv2.imshow("Face Recognition", frame)  # pylint: disable=no-member
    if cv2.waitKey(1) & 0xFF == ord('q'):  # pylint: disable=no-member
        break

cap.release()
cv2.destroyAllWindows()  # pylint: disable=no-member
