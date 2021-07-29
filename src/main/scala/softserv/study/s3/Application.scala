package softserv.study.s3

import akka.NotUsed
import akka.stream.alpakka.s3.{MultipartUploadResult, ObjectMetadata}
import akka.stream.alpakka.s3.scaladsl.S3
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Application {
  private val log = LoggerFactory.getLogger(this.getClass)

  implicit val ec: ExecutionContextExecutor = system.dispatcher

  def main(args: Array[String]): Unit = {
    val s3Config = config.getConfig("application.aws.s3")
    val bucketName = s3Config.getString("bucket-name")

    val processPut = putToBucket("New", bucketName, "AnotherKey")

    processPut onComplete {
      case Success(message) => log.info(s"Put to bucket: $message")
      case Failure(ex) => log.error(ex.getMessage)
    }

    val processGet = getFromBucket(bucketName, "AnotherKey")

    processGet onComplete {
      case Success(message) => log.info(s"Get from bucket: $message")
      case Failure(ex) => log.error(ex.getMessage)
    }
  }

  def putToBucket(content: String, bucketName: String, objectKey: String): Future[MultipartUploadResult] = {
    val file: Source[ByteString, NotUsed] =
      Source.single(ByteString(content))

    val s3Sink: Sink[ByteString, Future[MultipartUploadResult]] =
      S3.multipartUpload(bucketName, objectKey)

    file.runWith(s3Sink)
  }

  def getFromBucket(bucketName: String, objectKey: String): Future[String] = {
    val s3File: Source[Option[(Source[ByteString, NotUsed], ObjectMetadata)], NotUsed] =
      S3.download(bucketName, objectKey)

    val data = s3File
      .runWith(Sink.head)
      .map(_.get._1)

    data.flatMap(
      _.runWith(Sink.head)
        .map(_.utf8String))
  }
}
