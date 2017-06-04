import sbtsparksubmit.SparkSubmitPlugin.autoImport._

object SparkSubmit {
  lazy val settings = SparkSubmitSetting(
    SparkSubmitSetting("spark-streaming-rsvp",
      Seq("--class", "com.github.mmolimar.askss.streaming.RsvpStreaming"), Seq("5")
    ))


}