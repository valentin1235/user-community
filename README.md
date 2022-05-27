### 데이터베이스 설치
```
docker run --platform linux/amd64 -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=heechul -e MYSQL_DATABASE=heechul -e MYSQL_PASSWORD=heechul -d mysql
```
- Docker가 설치되어있지 않다면 [링크](https://docs.docker.com/get-docker/) 에서 플랫폼에 맞는 도커를 설치합니다
- MAC M1 사용자가 아니라면 `--platform linux/amd64` 옵션을 지우고 실행합니다


### 실행
- 디렉토리 이동
```
cd ./community
```
- 빌드 및 실행
```
./gradlew build -x test &&
java -jar ./build/libs/community-0.0.1-SNAPSHOT.jar
```

### 문서
- [API 문서](https://documenter.getpostman.com/view/10893095/Uyxoh3gM#41c75655-e766-4365-bac6-414e0d5e0c7b) : 예시가 있는 요청의 경우 우측상단에있는 "아래를 가리키는 화살표"를 눌러서 API요청 결과 예시를 확인할 수 있습니다.
- [ERD](https://github.com/valentin1235/zaritalk-community/blob/main/community.png?raw=true)

### 기능
- 유저 목록 `GET /users` : 등록된 유저의 목록을 표출합니다
- 유저 생성 `POST /users/user` : 유저를 생성합니다
- 게시글 목록 `GET /postings/posting` : 게시글 목록을 표출합니다. 요청 헤더에 들어가있는 유저가 "좋아요" 누른 게시글은 liked 필드가 true로 나옵니다
- 게시글 상세 `GET /postings/{postingId}/detail` : 게시글 상세정보와 댓글을 보여줍니다
- 게시글 생성 `POST /postings/posting` : 게시글을 생성합니다
- 게시글 수정 `PUT /postings/{postingId}` : 게시글을 수정합니다
- 게시글 삭제 `DELETE /postings/{postingId}` : 게시글을 삭제합니다(soft delete)
- 좋아요 `POST /postings/{postingId}/like` : 게시글을 "좋아요"합니다. 이미 좋아요 된 게시글에 한번 더 요청하는 경우 "좋아요"를 삭제합니다
- 댓글 생성 `POST /comments/comment?postingId=` : 댓글을 생성합니다
- 댓글 수정 `PUT /comments/{commentId}` : 댓글을 수정합니다
- 댓글 삭제 `DELETE /comments/{commentId}` : 댓글을 삭제합니다

### 테스트
```
./gradlew test
```

### 하면 좋을것들
- 서비스 테스트와 데이터베이스 결합 제거
- 데이터베이스와 어플리케이션을 docker-compose로 묶어서 배포하기
- service쪽에서 발생하는 예외 `NotAuthorized`, `AccountTypeMismatch`를 어떻게 공통적으로 처리할지 고민해보기
- 지금은 목록 표출할때 데이터베이스에서 조회해온 결과목록에 반복문을 돌면서 dto를 하나하나 만들어주고 있는데, 데이터베이스에서 한번의 쿼리로 가져올수 있는 방향 고민해보기. 얘를들면 view를 만들어서 view테이블에 쿼리 등

## 이슈 로그
### < n + 1쿼리 이슈 >
##### 상황
- 게시글 목록을 불러오로고나서 게시글 하나마다 좋아요 개수를 계산하기 위해 레이지 로딩이 발생
- 게시글 목록이 많을수록 쿼리가 n만큼 늘어나서 성능이 좋지않음
##### 해결
- querydsl을 사용해서 group by를 통한 게시글당 좋아요 개수 도출

### < jpql 동적쿼리 이슈 >
##### 상황
- 동적쿼리를 jpql을 통해서 만들기에는 검색키워드 유무에 따라서 문자열을 한쳐주는등의 동작을 해야하기 때문에 유지보수성이 떨어진다고 판단
##### 해결 
- querydsl 사용 

### < querydsl 이슈 >
##### 상황
```
JPAExpressions.select( // sub-query start
    like.id.count()
            .when(0L)
            .then(0)
            .otherwise(1) 
).from(user)
    .leftJoin(like)
    .on(user.id.eq(like.user.id))
    .on(like.posting.id.eq(posting.id))
    .where(user.id.eq(userId))
    .groupBy(user.id)
    .fetchOne()
```
위와같은 서브쿼리를 dto의 필드 하나로 할당해주는데 `JPAExpressions.select`의 반환타입이 `Expression<?>`라서 필드에 할당이 안됨.
##### 해결
- 첫번째로 게시글 목록을 가져와서 게시글의 id(pk)를 담은 리스트를 만든다.
- 두번째로 게시글의 id 리스트를 조건에 넣어서 "요청한 유저의 게시글 좋아요 여부"를 쿼리한다.
- 세번째로로 첫번째에서 가져온 게시글 목록을 게시글 id를 key로 하는 맵으로 만든다.
- 마지막으로 두번째 단계에서 가져온 "좋아요 여부" 목록을 반복문을 돌면서 세번째에서 만든 맵의 key에 맞게 매핑해준다.

### <  >
