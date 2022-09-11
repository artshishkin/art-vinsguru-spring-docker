[![CircleCI](https://circleci.com/gh/artshishkin/art-vinsguru-spring-docker.svg?style=svg)](https://circleci.com/gh/artshishkin/art-vinsguru-spring-docker)
[![codecov](https://codecov.io/gh/artshishkin/art-vinsguru-spring-docker/branch/main/graph/badge.svg?token=U5YRYVEM7N)](https://codecov.io/gh/artshishkin/art-vinsguru-spring-docker)
![Java CI with Maven](https://github.com/artshishkin/art-vinsguru-spring-docker/workflows/Java%20CI%20with%20Maven/badge.svg)
![Spring Boot version][springver]

![Docker][docker]
[![GitHub issues](https://img.shields.io/github/issues/artshishkin/art-vinsguru-spring-docker)](https://github.com/artshishkin/art-vinsguru-spring-docker/issues)
![Project licence][licence]

# art-vinsguru-spring-docker
Tutorial - Docker From Scratch [For Spring Developers] - from Vinoth Selvaraj (Udemy)

### Documentation

1. [Docker commands](docs/docker-commands.pdf)
2. [Linux commands cheat sheet](docs/linux-cheat-sheet.pdf)

### Section 4: Docker Crash Course

#### [37. Installing Java Manually in Ubuntu Container](/Section_4_Docker_Crash_Course/37_InstallingJavaManuallyInUbuntuContainer/installing_Java_manually_instruction.md)

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

### Certificate of Completion

![Certificate of completion](Certificate/UC-DockerFromScratch-Vinsguru.jpg "Certificate of Completion")

[docker]: https://img.shields.io/static/v1?label=&message=Docker&labelColor=white&color=white&logo=docker
[licence]: https://img.shields.io/github/license/artshishkin/art-vinsguru-spring-docker.svg
[springver]: https://img.shields.io/badge/dynamic/xml?label=Spring%20Boot&query=%2F%2A%5Blocal-name%28%29%3D%27project%27%5D%2F%2A%5Blocal-name%28%29%3D%27parent%27%5D%2F%2A%5Blocal-name%28%29%3D%27version%27%5D&url=https%3A%2F%2Fraw.githubusercontent.com%2Fartshishkin%2Fart-vinsguru-spring-docker%2Fmaster%2Fpom.xml&logo=Spring&labelColor=white&color=grey
