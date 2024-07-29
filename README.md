# 토스를 모티브로 한 핀테크 백엔드 개발

간단한 계좌, 거래 기능에 관한 API를 만든 후 테스트 코드 작성, 성능테스트 등을 학습하고 <이펙티브 자바>, <도메인 주도 개발 시작하기> 등 서적에서 학습한 내용을 적용하여 코드를 작성했습니다.

## 실행방법 
```
./gradlwe build
cd build/libs
java -jar tess-0.0.1-SNAPSHOT.jar
```

## REST DOCS
build 파일 실행 후
localhost:8080/docs/index.html

## **사용기술**

Java 11 <br/>
Spring Boot 2.7.2 <br/>
MySQL 8.0 <br/>

## **DB ERD**

<img width="643" alt="tess_erd" src="https://github.com/f-lab-edu/tess/assets/65644373/f6c849ea-573a-452c-a2ca-072c57f05d45">

## **테스트 커버리지 측정**

<img width="779" alt="테스트커버리지" src="https://github.com/user-attachments/assets/7e336176-c6a4-46f5-8b6d-6dc97c095fe1">


## **캐싱 & 성능테스트**

100명, 1000명의 user가 동시접속 할 때를 가정하여 송금하기와 계좌전체조회하기 API에서 성능테스트를 진행했습니다. 계좌 전체조회하기에는 캐싱을 적용했습니다.
<br/>

초기 테스트에서 100명일 때 5%, 1000명일 때 30%였던 실패율을 예외처리 후 0%로 감소시켰습니다.

### **0) 초기 테스트 때의 실패율 그래프  -> 예외처리 후 0% 실패율로 감소 (1,2 그래프)**
<img width="872" alt="100명_성능테스트" src="https://github.com/f-lab-edu/tess/assets/65644373/ebf74332-bd01-4702-9e77-8ea3f99b4b99">

<img width="867" alt="1000명 성능테스트2" src="https://github.com/f-lab-edu/tess/assets/65644373/80bdbcc2-82b2-4e8f-97be-7fdab61f0ba9">

### **1) 100명 user (캐싱 초기 적용)** - 평균 RPS 33.9
<img width="894" alt="100명 성능" src="https://github.com/user-attachments/assets/130b4b83-9749-41f7-bf84-2d0c1ca0e7d4">

<br/>

cf) 성능 테스트 하면서 캐싱 적용 <br/>
<img width="181" alt="캐시적용ㅇ" src="https://github.com/user-attachments/assets/b6a0a16b-61fe-4160-bf54-bb29b41acc7f">
<br/>

초당 요청수(RPS)는 평균 약 33.9이고, 응답시간이 약 30ms 이하로 안정적으로 유지됩니다.
계좌 전체조회하기에 대한 캐싱이 적용 후, 테스트 API에 대한 95% 응답시간의 최대값이 87ms에서 20ms정도로 급격히 감소했습니다.


<br/>

### **2) 1000명 user** - 평균 RPS 304

<img width="917" alt="240729 성능 1000명" src="https://github.com/user-attachments/assets/5e7f37ba-3d96-4a6d-b8f9-65e4db2bc363">

<br/>

초당 요청수(RPS)는 평균적으로 약 304입니다. 평균 응답 시간은 100ms 이하로 유지됩니다.

<br/>