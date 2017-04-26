import os
import motor
from motor import MotorClient
#Tornado libraries
from tornado.ioloop import IOLoop
from tornado.escape import json_encode
from tornado.web import RequestHandler, Application, asynchronous, removeslash
from tornado.httpserver import HTTPServer
from tornado.httpclient import AsyncHTTPClient
from tornado.gen import engine, Task, coroutine
import tornado.ioloop
import tornado.web
import hashlib, uuid
#Other libraries
import json
from bson import ObjectId
import urllib2
import datetime

salt = uuid.uuid4().hex
db = MotorClient()['ppa']

class JSONEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, ObjectId):
            return str(o)
        return json.JSONEncoder.default(self, o)

class MainHandler(RequestHandler):
	def get(self):
		self.write('Hello World')

class LoginHandler(RequestHandler):
	@removeslash
	@coroutine
	def post(self):
		username = self.get_argument('username')
		password = self.get_argument('password')
		data = db.userDetails.find({'uname':username})
		finalData = None
		while (yield data.fetch_next):
			finalData = data.next_object()
		if(finalData['password']==password):
			self.write({"Response":True})
		else:
			self.write({"Response":False})

class SignUpHandler(RequestHandler):
	@coroutine
	@removeslash
	def post(self):
		username = self.get_argument('username')
		password = self.get_argument('password')
		finalData = None
		data = db.userDetails.find({'uname':username})
		while (yield data.fetch_next):
			finalData = data.next_object()
		
		if(finalData is None):
			yield db.userDetails.insert({"uname":username, "password":password})
			self.write({"Response":True})
		else:			
			self.write({'Response':False})
			

class clientInsertServerStats(RequestHandler):
	@removeslash
	@coroutine
	def post(self):
		writeData = json.loads(json.dumps(self.request.body))
		db = self.settings['db']
		print(writeData)
		jsonData = {}
		jsonData = json.loads(writeData)
		jsonData['Time'] = str(datetime.datetime.now())
		yield db.clientStats.insert(jsonData)
		print("Done Server stats")


class clientReadServerStats(RequestHandler):
	@removeslash
	@coroutine
	def post(self):
		uname = self.get_argument('uname')
		#FIND IN THE DB
		finalData = []
		data = db.clientStats.find({'uname':uname})
		while (yield data.fetch_next):
			finalData = data.next_object()
		JSONdata = {}
		JSONdata['Most Recent Data'] = (finalData)
		self.write(JSONEncoder().encode(JSONdata))

class clientAnalyseServerStats(RequestHandler):
	@removeslash
	@coroutine
	def post(self):
		uname = self.get_argument('uname')
		#FIND IN THE DB
		sys_info_arr =[]
		data = db.clientStats.find({'uname':uname})
		while (yield data.fetch_next):
			sys_info_arr.append(data.next_object())
		JSONdata = {}
		JSONdata['Stats array'] = (sys_info_arr)
		self.write(JSONEncoder().encode(JSONdata))

class clientInfoInsert(RequestHandler):
	@removeslash
	@coroutine
	def post(self):
		writeData = json.loads(json.dumps(self.request.body))
		db = self.settings['db']
		print(writeData)
		yield db.userStats.insert(json.loads(writeData))
		print("Done Client Info")

class clientInfoRead(RequestHandler):
	@removeslash
	@coroutine
	def post(self):
		uname = self.get_argument('uname')
		#FIND IN THE DB
		sys_info_arr = []
		data = db.userStats.find({'uname':uname})
		while (yield data.fetch_next):
			sys_info_arr.append(data.next_object())
		self.write(JSONEncoder().encode(sys_info_arr[0]))

class networkInfoInsert(RequestHandler):
	@removeslash
	@coroutine
	def post(self):
		writeData = json.loads(json.dumps(self.request.body))
		db = self.settings['db']
		print(writeData)
		yield db.networkInfo.insert(json.loads(writeData))
		print("Done Network Info")

class networkInfoRead(RequestHandler):
	@removeslash
	@coroutine
	def post(self):
		uname = self.get_argument('uname')
		#FIND IN THE DB
		network_arr =[]
		data = db.networkInfo.find({'uname':uname})
		print data
		while (yield data.fetch_next):
			network_arr.append(data.next_object())
			finalData = data.next_object()
			print finalData
		JSONdata = {}
		JSONdata['Network Stats array'] = (network_arr)
		print(JSONdata)
		self.write(JSONEncoder().encode(JSONdata))

application = tornado.web.Application([
	(r'/', MainHandler),
	(r'/login', LoginHandler),
	(r'/signUp',SignUpHandler),
	(r'/clientInsertServerStats',clientInsertServerStats),
	(r'/clientInfoInsert',clientInfoInsert),
	(r'/clientInfoRead',clientInfoRead),
	(r'/clientAnalyseServerStats',clientAnalyseServerStats),
	(r'/networkInfoInsert',networkInfoInsert),
	(r'/networkInfoRead',networkInfoRead),
	(r'/clientReadServerStats',clientReadServerStats)
	],db = db,debug=True)
 
def my_callback(result, error):
 	print('result %s' % repr(result))
 	IOLoop.instance().stop()

#main init
if __name__ == "__main__":
	application = HTTPServer(application)
	application.listen(6969)
	tornado.ioloop.IOLoop.instance().start()
