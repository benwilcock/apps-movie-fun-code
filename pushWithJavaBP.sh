mvn clean package -DskipTests -Dmaven.test.skip=true
cf push moviefun -p target/moviefun.war -b https://github.com/cloudfoundry/java-buildpack.git
