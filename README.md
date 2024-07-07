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

<img width="1272" alt="테스트 커버리지 70" src="https://github.com/f-lab-edu/tess/assets/65644373/c4f44906-6d7a-4f24-a7ef-535c850f2d3c">


## **캐싱 & 성능테스트**

100명, 1000명의 user가 동시접속 할 때를 가정하여 송금하기와 계좌전체조회하기 API에서 성능테스트를 진행했습니다. 계좌 전체조회하기에는 캐싱을 적용했습니다.


### **1) 100명 user (캐싱 초기 적용)** - 평균 RPS 35, 실패율 5%

<img width="872" alt="100명_성능테스트" src="https://github.com/f-lab-edu/tess/assets/65644373/ebf74332-bd01-4702-9e77-8ea3f99b4b99">

<br/>


cf) 성능 테스트 하면서 캐싱 적용 <br/>
<img width="247" alt="계좌전체조회_캐싱" src="https://github.com/f-lab-edu/tess/assets/65644373/e83464ca-3d33-470a-add8-4ad70236b4b8">

<br/>
초당 요청수(RPS)는 평균 약 35이고, 100명까지는 요청에 대한 실패율이 거의 없으며, 응답시간이 약 30ms에서 안정적으로 유지됩니다.
계좌 전체조회하기에 대한 캐싱이 적용되면서 응답시간이 감소했습니다.


<br/>

### **2) 1000명 user** - 평균 RPS 160, 실패율 30 %

<img width="867" alt="1000명 성능테스트2" src="https://github.com/f-lab-edu/tess/assets/65644373/80bdbcc2-82b2-4e8f-97be-7fdab61f0ba9">

<br/>

초당 요청수(RPS)는 평균적으로 약 160이고, 유저가 6~700명을 넘어갈 때부터 실패율이 계속해서 증가합니다. 

<br/>

<img width="865" alt="700명 이상일때 부터 급격한 성능저하" src="https://github.com/f-lab-edu/tess/assets/65644373/de80a9ea-29dd-459a-ae89-52a1b74cb856">

700명인 시점에, 100명일 때의 35RPS보다 5배 이상 증가한 192 RPS를 처리하면서, 실패율이 급격하게 상승했습니다.
