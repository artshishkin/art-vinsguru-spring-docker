### Installing Java Manually in Ubuntu Container

1. Create and run container
   - `docker run -it ubuntu`
2. Look container version
   - `uname -m` -> `x86_64`
3. Link for JDK 
   - visit [https://jdk.java.net/archive/](https://jdk.java.net/archive/)
   - choose Linux/x64 (because of x86_64)
   - copy link address
   - `https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz`
4. Install curl
   - `apt-get update`
   - `apt-get install curl -y`
5. Download JDK
   - `curl https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz --output java17.tar.gz`
6. Extract JDK
   - `tar -xvzf java17.tar.gz`
   - `rm java17.tar.gz` - remove unnecessary file
7. Make Java to work
   - `export PATH=$PATH:/jdk-17.0.2/bin` - add Java bin directory to PATH  

