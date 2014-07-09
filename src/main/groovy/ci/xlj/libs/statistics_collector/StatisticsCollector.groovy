package ci.xlj.libs.statistics_collector

import java.util.logging.Logger

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils

import ci.xlj.libs.utils.StringUtils

class StatisticsCollector {

	private static Logger logger = Logger.getLogger("ci.xlj.libs.statistics_collector.StatisticsCollector")

	private StatisticsCollector(){
	}

	private static client
	def static serverUrl

	def static init(serverURL){
		this.serverUrl = serverURL

		client = new DefaultHttpClient()
	}

	def static post
	def static entity
	def static response
	def static responseContent

	def static sendMessage(msg){
		post = new HttpPost(serverUrl)
		post.setHeader("Content-Type", "text/xml;charset=UTF-8")

		if (msg) {
			def stringEntity = new StringEntity(msg)
			post.setEntity(stringEntity)
		}

		response = client.execute(post)
		entity = response.getEntity()
		responseContent=getHtmlFromHttpEntity()
		def retCode=response.getStatusLine().getStatusCode()
		if(retCode!=200){
			logger.info("Send OK.")
		}else{
			logger.info("Send Result: ${retCode} ${responseContent}")
		}
		
		EntityUtils.consume(entity)
	}

	private static getHtmlFromHttpEntity() {
		def htmlContent = ""

		try {
			def reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "utf-8"))

			reader.eachLine { htmlContent<<=it }
		} catch (Exception e) {
			logger.info("Error in transforming HTTP Entity into String. Details:\n"
					+ StringUtils.getStrackTrace(e))
		}

		return htmlContent.toString()
	}
}
