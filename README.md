# 🏠 스마트 홈 IoT 시스템 (Capstone Project)

## 📋 프로젝트 개요
이 프로젝트는 Spring Boot 기반의 스마트 홈 IoT 시스템으로, 사용자의 생활 패턴을 학습하여 자동으로 가전제품을 제어하는 시스템입니다. 얼굴 인식, 음성 처리, 예측 알고리즘을 통해 지능형 홈 자동화를 구현합니다.

## 🛠️ 기술 스택
- **백엔드**: Spring Boot 3.2.5, Java 17
- **데이터베이스**: MySQL
- **빌드 도구**: Gradle
- **AI/ML**: Python (얼굴 인식, Haar Cascade)
- **기타**: JPA, Lombok, Thymeleaf

## 📁 프로젝트 구조

```
server_capstone-main/
├── src/
│   ├── main/
│   │   ├── java/com/example/capstone/
│   │   │   ├── controller/          # REST API 컨트롤러
│   │   │   │   ├── AirController.java       # 에어컨 제어
│   │   │   │   ├── BoilerController.java    # 보일러 제어
│   │   │   │   ├── LightController.java     # 조명 제어
│   │   │   │   ├── MemberController.java    # 회원 관리
│   │   │   │   ├── PredictController.java   # 예측 및 자동 제어
│   │   │   │   ├── TemperatureController.java # 온도 관리
│   │   │   │   ├── TimedataController.java  # 시간 데이터 관리
│   │   │   │   └── WindowController.java    # 창문 제어
│   │   │   ├── dto/                 # 데이터 전송 객체
│   │   │   ├── entity/              # JPA 엔티티
│   │   │   ├── repository/          # 데이터 접근 계층
│   │   │   ├── service/             # 비즈니스 로직
│   │   │   └── CapstoneApplication.java # 메인 애플리케이션
│   │   └── resources/               # 설정 파일 및 리소스
│   ├── audio/                       # 음성 처리 모듈
│   │   ├── TextController.java      # 음성-텍스트 변환
│   │   ├── Script.java              # 스크립트 처리
│   │   └── Request.java             # 요청 처리
│   └── test/                        # 테스트 코드
├── haar/                            # 얼굴 인식 모듈
│   ├── 01_face_dataset.py           # 얼굴 데이터셋 생성
│   ├── 02_face_training.py          # 얼굴 인식 모델 훈련
│   ├── 03_face_recognition.py       # 얼굴 인식 실행
│   ├── faceDetection.py             # 얼굴 감지
│   ├── haarcascade_frontalface_default.xml # Haar Cascade 모델
│   └── dataset/                     # 얼굴 데이터셋
├── timedata/                        # 시간 데이터 관리
├── .expo/                           # React Native Expo 설정
├── build.gradle                     # Gradle 빌드 설정
└── settings.gradle                  # Gradle 프로젝트 설정
```

## ✨ 주요 기능

### 🎯 스마트 기기 제어
- **조명 제어**: 자동 점등/소등, 밝기 조절
- **에어컨 제어**: 온도 설정, 전원 관리
- **보일러 제어**: 온수 및 난방 관리
- **창문 제어**: 자동 개폐 시스템

### 🤖 지능형 자동화
- **패턴 학습**: 사용자의 생활 패턴 분석
- **예측 제어**: 출입 시간 예측을 통한 사전 제어
- **스케줄링**: 시간 기반 자동 실행 (cron 표현식 사용)
- **자동 제어 모드**: 학습된 패턴에 따른 완전 자동화

### 👤 사용자 인증 및 관리
- **얼굴 인식**: OpenCV Haar Cascade 기반 얼굴 감지 및 인식
- **회원 관리**: 사용자별 개인화된 설정 관리
- **패턴 저장**: 개인별 생활 패턴 데이터 저장

### 🗣️ 음성 처리
- **음성 명령**: 음성을 통한 기기 제어
- **텍스트 변환**: STT(Speech-to-Text) 기능

## 🚀 시작하기

### 필수 요구사항
- Java 17+
- MySQL 8.0+
- Python 3.8+ (얼굴 인식 기능)
- Gradle 7.0+

### 설치 및 실행

1. **저장소 클론**
   ```bash
   git clone <repository-url>
   cd server_capstone-main
   ```

2. **데이터베이스 설정**
   - MySQL 서버 실행
   - 데이터베이스 생성 및 연결 정보 설정

3. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

4. **얼굴 인식 모듈 설정** (선택사항)
   ```bash
   cd haar/
   python 01_face_dataset.py    # 얼굴 데이터셋 생성
   python 02_face_training.py   # 모델 훈련
   python 03_face_recognition.py # 얼굴 인식 실행
   ```

## 📡 API 엔드포인트

### 기기 제어
- `POST /api/light/control` - 조명 제어
- `POST /api/air/control` - 에어컨 제어
- `POST /api/boiler/control` - 보일러 제어
- `POST /api/window/control` - 창문 제어

### 예측 및 자동화
- `POST /api/savePrediction` - 예측 데이터 저장
- `POST /api/updatePrediction` - 예측 데이터 업데이트
- `GET /api/getPredictedTimesByMemberId` - 사용자별 예측 데이터 조회
- `POST /api/setAutoControlTemperature` - 자동 제어 온도 설정

### 사용자 관리
- `POST /api/member/register` - 회원 가입
- `POST /api/member/login` - 로그인
- `GET /api/member/profile` - 프로필 조회

## 🏗️ 아키텍처

### 레이어드 아키텍처
```
┌─────────────────┐
│   Controller    │ ← REST API 엔드포인트
├─────────────────┤
│    Service      │ ← 비즈니스 로직
├─────────────────┤
│   Repository    │ ← 데이터 접근 계층
├─────────────────┤
│    Entity       │ ← JPA 엔티티
└─────────────────┘
```

### 외부 모듈
- **얼굴 인식 모듈**: Python 기반 독립 실행
- **음성 처리 모듈**: 음성 명령 처리
- **자동화 스케줄러**: Spring 스케줄링

## 🔧 설정

### application.properties
```properties
# 데이터베이스 설정
spring.datasource.url=jdbc:mysql://localhost:3306/capstone
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA 설정
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 📈 향후 개발 계획
- [ ] 모바일 앱 연동 (React Native)
- [ ] 실시간 센서 데이터 처리
- [ ] 머신러닝 기반 예측 정확도 향상
- [ ] 클라우드 배포 및 확장성 개선
- [ ] 보안 강화 (JWT 토큰 기반 인증)

## 🤝 기여하기
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 라이선스
이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 📞 문의
프로젝트 관련 문의사항이 있으시면 이슈를 생성해주세요. 