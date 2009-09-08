mkdir -p target/scaladoc
scaladoc -d target/scaladoc `find src/main/scala -name \*.scala`
