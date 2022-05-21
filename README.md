### 데이터베이스 설치
```
docker run --platform linux/amd64 -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=heechul -e MYSQL_DATABASE=heechul -e MYSQL_PASSWORD=heechul -d mysql
```
- Docker가 설치되어있지 않다면 [링크](https://docs.docker.com/get-docker/) 에서 플랫폼에 맞는 도커를 설치합니다
- MAC M1 사용자가 아니라면 `--platform linux/amd64` 옵션을 지우고 실행바랍니다


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
- 좋아요 `POST /postings/{postingId}/like` : 게시글을 "좋아요"합니다. 이미좋아요 된 게시글에 한번 더 요청하는 경우 "좋아요"를 삭제합니다
- 댓글 생성 `POST /comments/comment?postingId=` : 댓글을 생성합니다
- 댓글 수정 `PUT /comments/{commentId}` : 댓글을 수정합니다
- 댓글 삭제 `DELETE /comments/{commentId}` : 댓글을 삭제합니다

### 테스트
```
./gradlew test
```

### 하면 좋을것들
- 테스트가 데이터베이스랑 결합되어있어서 각 repository에 해당하는 인터페이스를 만들어서 repository를 사용하는쪽에서는 인터페이스에 의존하도록하고, 프로그램 종료되면 날아가는 메모리디비형태의 repository 구현체를 만들어서 테스트용으로 사용하기
- 데이터베이스와 어플리케이션을 docker-compose로 묶어서 배포하기
- service쪽에서 발생하는 예외 `NotAuthorized`, `AccountTypeMismatch`를 어떻게 공통적으로 처리할지 고민해보기
- 지금은 목록 표출할때 데이터베이스에서 조회해온 결과목록에 반복문을 돌면서 dto를 하나하나 만들어주고 있는데, 데이터가 많이 쌓이면 비효율적이기 때문에 디비안에 view를 만들어서 한번의 쿼리로 가져오는 방법 고민해보기
