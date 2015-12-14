import ch.qos.logback.classic.Levelimport ch.qos.logback.classic.encoder.PatternLayoutEncoderimport ch.qos.logback.classic.filter.LevelFilterimport ch.qos.logback.classic.filter.ThresholdFilterimport ch.qos.logback.core.ConsoleAppenderimport ch.qos.logback.core.FileAppenderimport static ch.qos.logback.classic.Level.*import static ch.qos.logback.core.spi.FilterReply.DENYimport static ch.qos.logback.core.spi.FilterReply.NEUTRALfinal Level STDOUT_LOG_LEVEL = INFOfinal Level STDERR_LOG_LEVEL = ERRORfinal String pat_bare = "%msg%rEx%n"final String pat_date_level_logger = "%date{yyyy-MM-dd HH:mm:ss.SSS} %-5level %-40logger{40} %msg%rEx%n"final String appender_stdout = "appender_STDOUT"final String appender_stderr = "appender_STDERR"final String appender_trace = "debug_log_appender"appender(appender_stdout, ConsoleAppender) {  target = "System.out"  filter(ThresholdFilter) {level = STDOUT_LOG_LEVEL}  filter(LevelFilter) {level = ERROR; onMatch = DENY; onMismatch = NEUTRAL}  encoder(PatternLayoutEncoder) {pattern = pat_bare}}appender(appender_stderr, ConsoleAppender) {  target = "System.err"  filter(ThresholdFilter) {level = STDERR_LOG_LEVEL}  encoder(PatternLayoutEncoder) {pattern = pat_bare}}appender(appender_trace, FileAppender) {  file = "./extract-apks-tool.log"  append = false  encoder(PatternLayoutEncoder) {pattern = pat_date_level_logger}}root(TRACE, [  appender_stdout,  appender_stderr,  appender_trace,])