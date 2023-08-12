# 원티드 프리온보딩 백엔드 인턴십 - 선발 과제
<br></br>
## 지원자 정보
- 이름: 강석원
- 이메일: kangsw1988@gmail.com
- 원티드 아이디: kangsw1988@gmail.com

<br>

## 애플리케이션의 실행 방법
### 실행 조건
- docker image build & docker compose를 사용하기 때문에 docker service가 실행중이어야 한다.
### 실행 방법
1. 저장소를 로컬에 clone
```shell
git clone https://github.com/piopoi/wanted-pre-onboarding-backend.git
```
2. 프로젝트 경로로 이동
```shell
cd [프로젝트 경로]
```
3. 배포 스크립트 실행
```shell
./deploy.sh
```
- deploy.sh 요약
  - git fetch, git pull
  - gradlew clean build
  - Remove previous docker container
  - Remove previous docker image
  - docker compose up
<br>

## 데이터베이스 테이블 구조

[//]: # (![db_diagram]&#40;docs/db_diagram.png&#41;)
<img src="docs/db_diagram.png" width="300"/>
<br>

## 구현한 API의 동작을 촬영한 데모 영상 링크

<br>

## 구현 방법 및 이유에 대한 간략한 설명

<br>

## API 명세(request/response 포함)
