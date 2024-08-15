<div align="center">

# Kids In Company: Backend Project

키즈인컴퍼니 서비스의 Backend Project에 대해 소개합니다.
작성자: 김현겸 (kylo-dev), 정준희(sungsil0624)

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FCLOVIDER%2Fkic-backend&count_bg=%23E7E413&title_bg=%231F36A4&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

</div>


## 목차

1. [프로젝트 소개](#프로젝트-소개)
2. [팀원](#팀원)
3. [기술 스택](#기술-스택)
4. [ERD](#ERD)
5. [서비스 아키텍처](#서비스-아키텍처)
6. [도전한 사항](#도전한-사항)
7. [최적화한 사항](#최적화한-사항)

## 💡 프로젝트 소개
>디케이테크인 내부의 공정하고 투명한 사내 어린이집 인원 배정을 위해 어린이집 모집과 추첨을 자동화한 서비스입니다.

## 🫶 팀원

|No|이름|역할|깃허브|
|------|---|---|---|
|1|김현겸|PM, BE|[kylo-dev](https://github.com/kylo-dev)|
|2|공예영|PL, FE|[yeyounging](https://github.com/yeyounging)|
|3|김성민|FE|[Collection50](https://github.com/Collection50)|
|4|서용준|FE|[mango0422](https://github.com/mango0422)|
|5|권민우|PL, BE|[MINUUUUUUUUUUUU](https://github.com/MINUUUUUUUUUUUU)|
|6|정준희|BE|[sungsil0624](https://github.com/sungsil0624)|
|7|정희찬|BE, Infra|[anselmo228](https://github.com/anselmo228)|
|8|이주애|BE, Infra|[leejuae](https://github.com/leejuae)|
|9|김관일|BE|[KIM-KWAN-IL](https://github.com/KIM-KWAN-IL)|

## 🛠️ 기술 스택

<img width="400" alt="image" src="https://github.com/user-attachments/assets/a6610aa3-2cd3-4897-b5cc-4e77fc6c278c">


## 📝 ERD

![키즈인컴퍼니 ERD](https://github.com/user-attachments/assets/56cefb42-935d-4ce6-b79b-7cd8f2e05a73)

## 🌱 서비스 아키텍처

<img width="1097" alt="image" src="https://github.com/user-attachments/assets/fc729e9e-baf9-4d7d-9a8b-235083346fef">

## 도전한 사항

## 최적화한 사항

* JIB 빌드를 통한 빌드 시간 77% 단축
* JRE 이미지 빌드를 통한 이미지 1.8배 경량화 및 보안성 강화
    * https://github.com/CLOVIDER/kic-backend/pull/120
* 관리자 대시보드 페이지 Redis 캐싱을 통한 쿼리 2회 단축 및 서버 데이터 전송과 처리 성능 2.5배 향상
    * https://github.com/CLOVIDER/kic-backend/issues/186

