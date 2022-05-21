### 데이터베이스 설치
```
  docker run --platform linux/amd64 -p 3306:3306 --name mysql \   
  -e MYSQL_ROOT_PASSWORD=heechul -e MYSQL_DATABASE=heechul -e MYSQL_PASSWORD=heechul \   
  -d mysql
  ```


### 실행
##### 디렉토리 이동
```
cd ./community
```
##### 빌드 및 실행
```
./gradlew build -x test &&
java -jar ./build/libs/community-0.0.1-SNAPSHOT.jar
```

### [API 문서](https://documenter.getpostman.com/view/10893095/Uyxoh3gM#41c75655-e766-4365-bac6-414e0d5e0c7b)
- 예시가 있는 요청의 경우 우측상단에있는 "아래를 가리키는 화살표"를 눌러서 API요청 결과 예시를 확인할 수 있습니다.

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
