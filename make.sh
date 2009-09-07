mkdir -p target/classes
scalac -unchecked -deprecation -d target/classes `find src/main -name \*.scala`
