import time
import matplotlib.pyplot as plt
from Point import Point
import Setting
import Hospital
import random
import math
from enum import Enum

worldTime = 0
infectedNum = Setting.ORIGINAL_COUNT
confirmedNum = 0
curedNum = 0

class State(Enum):
	NORMAL = 0
	SHADOW = NORMAL+1
	SUSPECTED = SHADOW+1
	CONFIRMED = SHADOW+1
	ISOLATED = CONFIRMED+1
	CURED = ISOLATED+1


class Person:

	# SAFE_DIST = 1
	NEIGHBOR = [(0, 1), (0, -1), (1, 0), (-1, 0)]
	# NEIGHBOR.extend([(1, 1), (1, -1), (-1, 1), (-1, -1)])
	sig = 1
	targetSig = 100
	def __init__(self, city, x, y):
		self.city = city
		self.x = x
		self.y = y

		# 随机走动坐标的均值
		self.targetXU = 100* random.gauss(0, 1) + x
		self.targetYU = 100* random.gauss(0, 1) + y
		self.moveTarget = None

		self.state = State.NORMAL
		self.infectedTime = 0
		self.confirmedTime = 0
		self.hospitalTime = 0

	def isInfected(self):
		return self.state.value>=State.SHADOW.value

	def beInfected(self):
		self.state = State.SHADOW
		self.infectedTime = worldTime

	def canMove(self):
		value = Person.sig*random.gauss(0, 1) + Setting.u
		return value>0

	def action(self):
		if self.state==State.ISOLATED:
			return

		if not self.canMove():
			return

		if self.moveTarget==None:
			targetX = Person.targetSig*random.gauss(0, 1) + self.targetXU
			targetY = Person.targetSig*random.gauss(0, 1) + self.targetYU
			self.moveTarget = Point(targetX, targetY)


		dX = self.moveTarget.x - self.x
		dY = self.moveTarget.y - self.y
		length = math.sqrt(dX*dX + dY*dY)

		# 已走到
		if length<1:
			moveTarget = None
			return

		udX = 1.42*dX // length
		udY = 1.42*dY // length

		if (self.x, self.y) in peopleDict and self in peopleDict[(self.x, self.y)]:
			peopleDict[(self.x, self.y)].remove(self)
		self.x += udX
		self.y += udY

		if (self.x, self.y) not in peopleDict:
			peopleDict[(self.x, self.y)] = []
		peopleDict[(self.x, self.y)].append(self)

	def update(self):
		# print("%d, %d" % (self.x, self.y), end='')
		self.action()
		# print("===> %d, %d" % (self.x, self.y))

		if not self.isInfected():
			for dir in Person.NEIGHBOR:
				nx, ny = self.x+dir[0], self.y+dir[1]
				if (nx, ny) not in peopleDict:
					continue
				personList = peopleDict[(nx, ny)]
				for person in personList:
					if person.isInfected():
						if random.uniform(0, 1)<Setting.BROAD_RATE:
							self.beInfected()
							global infectedNum
							infectedNum += 1
							break

				if self.isInfected():
					break

		
		# print(Main.worldTime)
		# import Main???

		if self.state==State.SHADOW and worldTime-self.infectedTime>=random.randint(Setting.SHADOW_TIME-30, Setting.SHADOW_TIME+30):
			self.state = State.CONFIRMED
			self.confirmedTime = worldTime
			global confirmedNum
			confirmedNum += 1

	
		if self.state==State.CONFIRMED and worldTime-self.confirmedTime>=Setting.HOSPITAL_RECEIVE_TIME:
			bed = Hospital.pickBed()
			if bed==None:
				print("隔离区没有空床位")
			else:
				self.state = State.ISOLATED
				self.x, self.y = bed.x, bed.y
				self.bed = bed
				bed.setEmpty(False)
				self.hospitalTime = worldTime


		if self.state==State.ISOLATED and worldTime-self.hospitalTime>=Setting.CURED_TIME:
			self.state = State.CURED
			global curedNum
			curedNum += 1
			# personPool.curedNum += 1

			Hospital.returnBed(self.bed)
			self.bed.setEmpty(True)

			x = 100*random.gauss(0, 1) + city.x
			y = 100*random.gauss(0, 1) + city.y
			(x, y) = map(int, (x, y))

			self.x, self.y = x, y


city = Point(10000, 10000)
peoplePool = []
peopleDict = dict()		# 位置映射到人



for i in range(Setting.CITY_POPULATION):
	x = 1000*random.gauss(0, 1) + city.x
	y = 1000*random.gauss(0, 1) + city.y
	(x, y) = map(int, (x, y))

	person = Person(city, x, y)
	peoplePool.append(person)

	if (x, y) not in peopleDict:
		peopleDict[(x, y)] = list()
	
	peopleDict[(x, y)].append(person)



# worldTime = 0
# peoplePool = PeoplePool.peoplePool
# 

import matplotlib.pyplot as plt

def plot_pic(infected, confirmed, cured, infected_now):
	'''
	days: 天数
	infected: 累计感染人数
	confirmed: 累计确诊人数
	cured: 累计治愈人数
	infected_now: 当前感染人数(即infected - cured)
	'''
	days = range(1,len(infected)+1)
	plt.scatter(days, infected, s = 10, c='r', label = 'people infected')
	plt.scatter(days, confirmed, s = 10, c='g', label = 'people confirmed')
	plt.scatter(days, cured, s = 10, c='b', label = 'people cured')
	plt.scatter(days, infected_now, s = 10, c='y', label = 'people infected right now')

	plt.title('Data chart', fontsize = 18) 
	plt.xlabel('Days', fontsize = 12)
	plt.ylabel('Number of people', fontsize = 12)
	plt.legend()
	plt.show()

def showPeople():
	X = []
	Y = []
	for (x, y) in peopleDict:
		X.append(x)
		Y.append(y)
	plt.scatter(X, Y)
	plt.show()
	
if __name__=='__main__':
	# showPeople()
	# 初始感染者
	infectedPeople = random.sample(peoplePool, Setting.ORIGINAL_COUNT)
	for person in infectedPeople:
		person.beInfected()

	day = 0


	infectedTotal = []
	confirmedTotal = []
	curedTotal = []
	infectedNow = []

	while worldTime<600:
		# time.sleep(0.1)

		for p in peoplePool:
			p.update()

		worldTime += 1
		
		if day==worldTime//10:
			print("Day%3d: %d %d %d" % (day+1, infectedNum, confirmedNum, curedNum))
			infectedTotal.append(infectedNum)
			confirmedTotal.append(confirmedNum)
			curedTotal.append(curedNum)
			infectedNow.append(infectedNum-curedNum)

			day += 1


	plot_pic(infectedTotal, confirmedTotal, curedTotal, infectedNow)
