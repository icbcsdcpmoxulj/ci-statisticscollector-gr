package ci.xlj.libs.statistics_collector

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.apache.log4j.Logger

import ci.xlj.libs.utils.StringUtils

class StatisticsCollector {

	private Logger logger = Logger.getLogger(StatisticsCollector)

	private StatisticsCollector(){
	}

	private DefaultHttpClient client
	def serverUrl

	def StatisticsCollector getInstance(serverURL){
		this.serverUrl = StringUtils.transformToUrl(serverURL)
		logger.info("Server URL is '$serverUrl'.")

		client = new DefaultHttpClient()
	}

	def post
	def entity
	def response
	
	def sendMessage(msg){
		post = new HttpPost(serverUrl)
		post.setHeader("Content-Type", "text/xml;charset=UTF-8")

		if (msg) {
			def stringEntity = new StringEntity(msg)
			post.setEntity(stringEntity)
		}

		response = client.execute(post)
		entity = response.getEntity()

		EntityUtils.consume(entity)
	}
}
