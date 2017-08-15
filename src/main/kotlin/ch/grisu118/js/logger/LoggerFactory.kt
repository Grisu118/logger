package ch.grisu118.js.logger

object LoggerFactory {

  private val defaultLevel = Level.INFO
  private val levels = mutableSetOf<Pair<Regex, Level>>()
  private val loggers = mutableMapOf<String, Logger>()

  fun logger(owner: Any): ILogger {
    val dyn = owner.asDynamic()
    return dyn.__g118Logger ?: run {
      val logger = logger(owner::class.js.name)
      dyn.__g118Logger = logger
      logger
    }
  }

  fun logger(name: String): ILogger {
    return loggers[name] ?: run {
      val logger = Logger(name, level(name))
      loggers[name] = logger
      logger
    }
  }

  fun loglevel(regex: Regex, level: Level) {
    levels.add(regex to level)
  }

  private fun level(name: String) = levels.filter { it.first.matches(name) }.maxBy { it.second }?.second ?: defaultLevel

}