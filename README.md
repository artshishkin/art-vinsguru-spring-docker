[![CircleCI](https://circleci.com/gh/artshishkin/art-vinsguru-spring-docker.svg?style=svg)](https://circleci.com/gh/artshishkin/art-vinsguru-spring-docker)
[![codecov](https://codecov.io/gh/artshishkin/art-vinsguru-spring-docker/branch/main/graph/badge.svg?token=U5YRYVEM7N)](https://codecov.io/gh/artshishkin/art-vinsguru-spring-docker)
![Java CI with Maven](https://github.com/artshishkin/art-vinsguru-spring-docker/workflows/Java%20CI%20with%20Maven/badge.svg)
![Spring Boot version][springver]

![Docker][docker]
![Testcontainers version][testcontainersver]
![Elasticsearch Container][elasticsearch-container-ver]

[![GitHub issues](https://img.shields.io/github/issues/artshishkin/art-vinsguru-spring-docker)](https://github.com/artshishkin/art-vinsguru-spring-docker/issues)
![Project licence][licence]

# art-vinsguru-spring-docker
Tutorial - Docker From Scratch [For Spring Developers] - from Vinoth Selvaraj (Udemy)

### Documentation

1. [Docker commands](docs/docker-commands.pdf)
2. [Linux commands cheat sheet](docs/linux-cheat-sheet.pdf)

---
### Section 4: Docker Crash Course

#### [37. Installing Java Manually in Ubuntu Container](/Section_4_Docker_Crash_Course/37_InstallingJavaManuallyInUbuntuContainer/installing_Java_manually_instruction.md)

---
### Section 6: Dockerizing Spring Application

#### 76. Docker Compose Build Option

- `docker-compose build`
- **OR**
- `docker-compose up --build -d`

#### 77. Docker Compose Profile

To start only certain service we have several opportunities 
1. Use command argument
   - `docker-compose up mongo -d` - starts only mongo service
2. Use profiles
   - `docker-compose up -d` - only mongo_stack starts
   - `docker-compose --profile=app up -d` - additionally starts services with profile app

#### 78. Multi Stage Dockerfile

1. Command line:
   - _art-vinsguru-spring-docker\job-service>_ `docker build -t artarkatesoft/art-vinsguru-job-service:multistage -f ./Dockerfile-multistage ./../`

2. Docker-compose:
   - _art-vinsguru-spring-docker\docker-compose\art-vinsguru-docker>_ `docker-compose --profile=multistage-build build`

#### 79. Build Pack

- `mvn spring-boot:build-image`

---
### 8 Consolidated Logging with ELK Stack

#### 8.2 Running elastic search with docker

1. Start full stack with elastic cluster
   - `docker-compose --profile=app --profile=logging-elk up`
2. In case of error in Windows
   - `max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]`
   - use commands for Windows
   - `wsl -d docker-desktop`
   - `sysctl -w vm.max_map_count=262144`
   - or permanently add `vm.max_map_count = 262144` to __/etc/sysctl.conf__

####  8.6. View Logs in Kibana

1. Start full stack with elastic cluster
   - `docker-compose --profile=app --profile=logging-elk up -d`
2.  View `filebeat` logs
   -  Exiting: error loading config file: config file ("filebeat.yml") can only be writable by the owner but the permissions are "-rwxrwxrwx" (to fix the permissions use: 'chmod go-w /usr/share/filebeat/filebeat.yml')
   -  in Windows we can disable the permission checking by adding:
      -  `command: filebeat -e -strict.perms=false`
3.  View logs
   -  localhost: 5601
   -  Kibana -> Discover ->
   -  Create index pattern
      -  Your index pattern matches 2 sources.
      -  Index pattern name: `filebeat*`
      -  Next step
   -  Select a primary time field for use with the global time filter.
      -  `@timestamp`
      -  Create index pattern
   -  Kibana -> Discover
---
### 11 Distributed Tracing with Zipkin

#### 11.5 View trace and dependencies in Zipkin

- Find a trace

![Zipkin tracing](docs/zipkin-screenshot.jpg)

- View dependencies (_other microservices_)

![Zipkin dependencies](docs/zipkin-dependencies.jpg)

- Find logs of a request by **trace_id** in Kibana

![Kibana Logs](docs/kibana-search-by-trace-id.jpg)

#### 11.6 Persisting Zipkin data into Elasticsearch

1. Add index pattern into Kibana
   - Menu &rarr; Management &rarr; Stack Management &rarr; 
   - Index Patterns &rarr; Create index pattern &rarr; `zipkin*`
2. Discover zipkin data
   - Discover &rarr; Change index pattern &rarr; `zipkin*`
   - by `traceId` 
     - i.e. `traceId : "13eb59a6cfc10547"` (choose one)

---

### _Certificate of Completion_

![Certificate of completion](Certificate/UC-DockerFromScratch-Vinsguru.jpg "Certificate of Completion")

[docker]: https://img.shields.io/static/v1?label=&message=Docker&labelColor=white&color=white&logo=docker
[licence]: https://img.shields.io/github/license/artshishkin/art-vinsguru-spring-docker.svg
[springver]: https://img.shields.io/badge/dynamic/xml?label=Spring%20Boot&query=%2F%2A%5Blocal-name%28%29%3D%27project%27%5D%2F%2A%5Blocal-name%28%29%3D%27parent%27%5D%2F%2A%5Blocal-name%28%29%3D%27version%27%5D&url=https%3A%2F%2Fraw.githubusercontent.com%2Fartshishkin%2Fart-vinsguru-spring-docker%2Fmain%2Fpom.xml&logo=Spring&labelColor=white&color=grey
[testcontainersver]: https://img.shields.io/badge/dynamic/xml?label=Testcontainers&query=%2F%2A%5Blocal-name%28%29%3D%27project%27%5D%2F%2A%5Blocal-name%28%29%3D%27properties%27%5D%2F%2A%5Blocal-name%28%29%3D%27testcontainers.version%27%5D&url=https%3A%2F%2Fraw.githubusercontent.com%2Fartshishkin%2Fart-vinsguru-spring-docker%2Fmain%2Fpom.xml&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAjCAIAAAAMti2GAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAPCSURBVEhL7ZZNbBRlGMffj9nZabddtqW1yldDoSUSP1ICaoKlUJtw0BDTNCQcOBDjxSOnxpvGaLhw9KYSLxxAOGDCQa1iNBFogYCBdLds60KXfmw3W9idz/fDZ3bGsl1md6cYb/4Om5ln3uf/Pvt/n3nfwVJKFI6fHmXh952XNnm3DQklnbeso1fGby3n4Pq19o7zB4fao1HvUR0aS5+8fvWr5NQLmhYlBG4tIRZN84O+Xaf3vekNqEU96a9TybHJ682UxhQFY+xHEYKUEmM656f27juxs8+PPkOw9GQud/y3KwXLTKhRUiFaiZCyYFvtUe3bgcE9Gzv8aAXV0kXHOfbrL78vzIMDStmB+rCyP/u7Xjx74GBLJOJHy6yR/vjGxJf37nZomkapHwqHyXnOND96effne/b6oVXpszPpk9f+UAluUSKVtoYHdIrMsYU8/cZbx7b3QATPrKyMjP+YNQ3op1q2hgcWADp1U6z5wtAwzXx49Gbx8RYbI4yh/ucr2QPSCUbxaCSzbKfmS6QV00Jn83Rvm90UiTAJf8wfuG6kQhFz8ExG5PMypkbKPSAkRyi9pSXTHUeEECbWOYGEVsISZ+flbJZzKQmFf4/89gIXFC71KJ3q2bDUFaMCYR5mAgkuKgRDmdMZrpsCCl+19GnnQoBId4J8XE32thUTGly76xI0ARhXdgDrJZz6i+efCGhXAm1QsVTVLwU8oZAl5Fxnc7onwTTFnaBa3a1UMDz7UGRzHNToWlGP4PcNRilC2gTf39Y6tzUOacT3p2wrwguLMj3HGXcLf1bUI1jaA54pTBY1OrUzke+MwWQgVCi4tj4x1tgaSD1pAFJhASiTSwk1tXtjOsVyK4KSalsDaSDtARqUI0GQ4DLQ1kBCSftIt1vDsx7pdfK/dBXQWv8JsD0QXXDEGWwVfuxfA1LCcnTGyfkd/Z9s3mXZpsFZ4E4UHvcMc5he1D870H/uvYGnx+6R6clLy1kSgXMsaAFgj2oiyveLqCn4RLY4d4rG+6/0XDwy6EXWnOizlj6YvJYxS6qiwrbjRz1qS3MhDcPsbt/w8+jQ9kSrH62S9vgu/2g0fQsuNFrx0RQkDbkly4ED8dy7+0f7uv3oPwRIe4w9nDqVTSJF1bC7a1RJQxYslDSssbdf/2Kg30upoqY0AF9Gh6cnxgsLVImqmKxK21zYJWO4d+vlkUN1vrDqSXvc0R8PpyYWbUNt1ZRLSzpyuuKxH0YOvdrZ5o+oBUiH4ZulB+j2ZfTpmTN/3vdDjWhc9XOC0N95QCMLG07m0AAAAABJRU5ErkJggg==&labelColor=white&color=grey
[elasticsearch-container-ver]: https://img.shields.io/badge/dynamic/yaml?label=Elasticsearch&query=$.ELASTIC_VERSION&url=https%3A%2F%2Fraw.githubusercontent.com%2Fartshishkin%2Fart-vinsguru-spring-docker%2Fmain%2Fdocker-compose%2Fart-vinsguru-docker%2Fenv-versions.yaml&logoWidth=40&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFYAAAAkCAYAAAATvM09AAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAASdAAAEnQB3mYfeAAABBNJREFUaEPlmU1IFGEYx9/xc2sLtzzkFuZXRHgIO9TBCN0gr56CFKK8dOoWRR07ZFEkEV2CyMJS+oAw6GKQ7tlDEiRSmCtWElG5kOWmNr3Pu8/I7HzsvM/MzuyEPxjnfR6dZf2/z/t/P0ZROcwDi2/OsOUvfRhl2dyaYqXROozWJ2RhVxdn2c/xVoycUZc/s9gRT33nmaHxNOuenMAoSz2/Zk60ZQMfKMG7FOmxHSRRAaV8u3hu4aWCmfWBlLCTd2NsaqAZI3doAq8XHIUFQUu4KMD8XJW4ewHELYrAKbwHRF6PtavSeG0aW+4J1HtX0izxkHssGCsCzf42/zzWVlinoV9scWFCMtK1vypvvmsfJjTKvI9AOyyFlfHTQggLlG09xqJ7r2MkCa9A5ZK5AmGWVy4mSXm/MHms7CRVCL+tav9EF1VDJ1IO1LwkiSTvGAKk5ZaR71+j2KIDov4vJO4n2Rif/BR+lyXHClYzC9jyh9LKGLY8ErAVGJ9VJZ4j77zCgpvJywjknYDnjLu2k1xkpxVF6IWFYWisNPinwPN0aQEl37GxRQhr9E7t8zXg92ADRpyqdk3YnTVxkaBSyYfl3M23GJnJrGb7TT3eKO5UqEO7UHmNBt6xVnuLweYWbGUxVv/a5FURibi61E3b8BOsqSxVxBUZnMEMEd0/nYPH/Fpol0fsPg7sQX9BB0EnaAhhp2eC2e+5FreI1Nspa4T/HaioiSuE3dXYIIIgIItr1+ce8yLkNmaZ19F/MHfIO9GLFiE8NqooLF4n2zVmPva+wpYcS93BdaRXrFYFdrTzaxT9OXTCwiycsqu6AKF+Bb2ogBBW4cI2hURYu1k4dMCX5JK18+sULt30CGHPnjvPnj1+hCk6FGFTnbWsJlqGkRnKtrFYyOy8xOR17eoVEfhNJvM3r6iA+3ETDDDkZfB0CENF7WnClj2hFpYPf72P5kNYAbZDQQ+fvO6F1GTBT0cdzgg0Aq1YGWAPH1ZkRQVCJ6zMiVMxMJ4NOJFjBT+WfrPqB7cxkuNQE22xP7/8h73rOIqRNWFbGVAsQCOnYrdENmDLP+LlFaxtdJi9mJvGjBlqdfgJTKZUUQHT5EWpWmq1WpFMdGIrlzBULYiqP0KkYPJYqFqVi+tEIUTNR7Gr1ouogOXkpZ6+ICWuV+yqFYBJDLwtcPhSD169eBEVyLuOVW5dZoqF7/ppAUaCPjuAkVKIlYnjBmFq4RtrfjqAkXdRZVYFRsTrZ2z7AlQp36s6vSCkIL3zKrlzw7OoslVqBeVclILMG1c3SAsLDM++Z30fJjGSx02V2lEIge2O+goJSVg9u0eeiDWpHSDm8wOH2Z5YNWYKzEqaDb1mbOTXhDgYBx/WezHMe+LiP7RtcnC7Osb+AQgSzKe9xJg9AAAAAElFTkSuQmCC&labelColor=white&color=grey
