mkdir -p target/classes
scalac -deprecation -d target/classes `find src/main -name \*.scala`
