﻿# Slike-service

Pognati je potrebno podatkovno bazo (SQL) na portu 3307:
``` docker run --name mysql-slike -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=slike_db -p 3307:3306 -d mysql:latest ```
