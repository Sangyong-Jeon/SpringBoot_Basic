# 커뮤니티 사이트 만들기

스프링 부트(Spring Boot)로 기본동작하는 CRUD 게시판 만들기


## 기능

- 게시글 등록, 조회, 수정, 삭제
    - 회원일 때만 등록, 수정, 삭제
    - 게시글 카테고리별 검색 (정렬)
    - 게시글 제목 검색 (정렬)
    - 게시글 조회수는 하루에 1번 증가 (쿠키 기반)
    - 페이지네이션
- 댓글 등록, 조회, 수정, 삭제
    - 비회원 가능
- 대댓글 등록, 조회, 수정, 삭제
    - 비회원 가능
- 파일 업로드, 다운로드, 출력 (여러개 가능)
- 로그인 기능

## 기술스택
- Java 8
- Spring Boot 2.7.0
- Spring Data JPA
- Spring Security
- JPA
- Thymeleaf
- Validation
- Lombok
- Ajax
- jQuery
- H2 DB
- IntelliJ IDEA

## Spring Boot 선정 이유
- Django -> 파이썬 배우지 않음
- Node.js -> JS를 사용하고, 싱글 쓰레드, 논블로킹 등 빠르고 쉬운 개발 장점이 있지만, 장애 대응, 디버깅, 테스트 등이 힘듬

- JS보다는 Java에 대한 이해도가 높은 상황
- 프론트는 고려하지 않음
- 지금껏 Java, 스프링부트, JPA를 학습 및 개발하였으므로 익숙함
- 현재 공부하고 싶은 것이 많은 상황에서 새로운 프레임워크와 언어를 학습할 시간적 여유가 부족함
- 스프링부트 개발할 때, IDE의 다양한 지원을 받으면서 개발해본 경험
- 무엇보다 Java를 학습하면서 얻은 지식으로 백엔드를 구성할 때, 예전과 다른 코드와 구조방식을 보면서 재미를 느낌

## 상세 설명
### 1. DB (oracle 기준)
임시로 H2 Database 사용
<img width="940" alt="image" src="https://user-images.githubusercontent.com/80039556/190580388-9920a946-4872-476e-899a-cc71538b0524.png">

- **회원 테이블** (비회원일 때 미사용)
    - 회원 번호 **PK**
    - 아이디 **UNIQ , NOT NULL**
    - 비밀번호 **NOT NULL**
    - 이름 **NOT NULL**
    - 권한 **NOT NULL**
    - 생성일시
    - 수정일시
- **게시글 테이블**
    - 게시글 번호 **PK**
    - 회원 번호 **FK**
    - 카테고리
    - 제목
    - 내용
    - 조회수
    - 생성일시
    - 수정일시
    - 생성자
    - 수정자
- **파일 테이블**
    - 파일 번호 **PK**
    - 게시글 번호 **FK**
    - 실제 파일이름
    - 저장 파일이름
    - 생성일시
    - 수정일시
- **댓글 테이블**
    - 댓글 번호 **PK**
    - 부모 댓글 번호 **FK**
    - 게시글 번호 **FK**
    - 회원 번호 **FK** (비회원일 때 미사용)
    - 내용
    - 비밀번호 (비회원일 때 사용)
    - 생성일시
    - 수정일시
    - 생성자 (비회원일 때 "anonymousUser" 저장)
    - 수정자 (비회원일 때 "anonymousUser" 저장)
    
### 2. 서버 사이드 렌더링 (SSR)
Spring Framework에서 권장하는 View Template Engine인 `Thymeleaf`를 사용

- 백엔드 서버에서 HTML을 동적으로 렌더링

### 3. 회원가입 및 로그인 기능
`Spring Security` 프레임워크 사용

- 비밀번호는 `Bcryptpasswordencoder` 를 사용하여 암호화

- `Bcryptpasswordencoder`
  - 스프링 시큐리티(Spring Security) 프레임워크에서 제공하는 클래스
  - 비밀번호를 암호화하는데 사용할 수 있는 메소드를 가진 클래스
  - `encode()`
    - 암호화해주는 메소드
    - SHA-1, 8바이트로 결합된 해쉬이며 랜덤 생성된 솔트(salt) 지원으로 똑같은 비밀번호를 입력해도 매번 다른 인코딩된 문자열 반환
  - `matchers()`
    - 입력한 비밀번호와 인코딩된 비밀번호 일치 여부 확인하는 메소드
    - 첫번째 매개변수는 일치여부를 확인하려는 인코딩 안된 비밀번호
    - 두번째 매개변수는 인코딩 된 비밀번호
    - 반환 타입은 boolean
  
### 4. 페이지네이션

![Image](https://user-images.githubusercontent.com/80039556/204278737-6904b04c-011e-48a1-b572-278cadecd3c6.gif)

- 첫 페이지 번호 그룹이면  `<<` 버튼 표시 X
- 마지막 페이지 번호 그룹이면 `>>` 버튼 표시 X
- `>>` 버튼을 누르면 다음 페이지 번호 그룹 표시함
- `<<` 버튼을 누르면 이전 페이지 번호 그룹 표시함
  - e.g. 현재 11 버튼 그룹이라면 `<<` 누르면 이전 페이지 번호 그룹인 1 ~ 10버튼이 표시됨
- 게시글 제목 검색 시에도 페이지네이션 적용
- 게시글 제목 검색 후, 다른 페이지 번호 클릭 시에도 페이지네이션 유지

