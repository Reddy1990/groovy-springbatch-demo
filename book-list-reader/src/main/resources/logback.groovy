appender("stdout", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
	pattern = "%d [%thread] [%level] %logger - %msg%n"
  }
}
logger("org.springframework.batch", INFO)
logger("com.cucharitas.book", INFO)
root(WARN, ["stdout"])
