# 데이터베이스 수동 생성 명령어
```
CREATE DATABASES wanted DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER wanted@localhost IDENTIFIED BY 'wanted123';
CREATE USER wanted@'%' IDENTIFIED BY 'wanted123';

GRANT ALL PRIVILEGES ON wanted.* TO 'wanted'@'localhost';
GRANT ALL PRIVILEGES ON wanted.* TO 'wanted'@'%';
```
