import matplotlib.pyplot as plt
import numpy as np
import math

'''
SIR模型

S susceptible		易感者
I infected 			感染者
R recovered/removed	痊愈者

ds/dt = -beta * i * s / N
di/dt = beta * i * s / N - gamma * i
dr/dt = gamma * i

'''

beta = 0.305
gamma = 0.012

N = 10**7 	# 武汉市人口

I = 10 		# 感染者
S = N-I  	# 感染者
R = 0 		# 痊愈者

dT = 1
T = 0
day = 0

# print("Day %3d: %d %d %d" % (day, S, I, R))

IList = [I]
dIList = [I]
dRList = [R]

print("totally infected | newly infected  | newly recovered")

while T<48:
	# if T==27:
	# 	beta = 0.18

	# if T>=30:
	# 	beta *= 0.83

	dS_dT = -beta * I * S / N
	dI_dT = beta * I * S / N - gamma * I
	dR_dT = gamma * I

	dS = int(dS_dT * dT)
	dI = int(dI_dT * dT)
	dR = int(dR_dT * dT)
	# print("(%d %d %d)" % (dS, dI, dR))
	
	# if dI<0:
	# 	# print(day)
	# 	break

	S += dS
	I += dI
	R += dR

	T += dT
	day += 1
	# print("Day %3d: %d %d %d" % (day, S, I, R))	
	print("%15d %15d %15d" % (I, dI, dR))

	IList.append(I)
	dIList.append(dI)
	dRList.append(dR)


plt.plot(range(len(IList)), IList, 'r')
plt.plot(range(len(dIList)), dIList, 'b')

plt.plot([27, 27], [0, 50000], 'k-')
plt.annotate("Effect occured", (27,10000), xytext=(10, 10000), arrowprops=dict(arrowstyle='->'))

# plt.plot(range(1, len(dRList)+1), dRList, 'g')
# plt.show()



# logI = [math.log(x) for x in IList]

# plt.plot(range(1, len(logI)+1), logI, 'r')
# plt.show()


# wuhan 1.10 - 2.8 共31天数据
data = '''41 0
41 0
41 0
41 0
41 0
41 0
45 4
62 17
121 59
198 77
258 60
363 105
425 62
495 70
572 77
618 46
698 80
1590 892
1905 315
2261 356
2639 378
3215 576
4109 894
5142 1033
6384 1242
8351 1967
10117 1766
11618 1501
13603 1985
14982 1379
16902 1921'''


Totally = [0, 0]
Newly = [0, 0]

datas = data.split('\n')
for line in datas:
	line = list(map(int, line.split(' ')))
	Totally.append(line[0])
	Newly.append(line[1])



plt.plot(range(len(Totally)), Totally, 'or', label="Totally confirmed cases")
plt.plot(range(len(Newly)), Newly, 'ob', label="Newly confirmed cases")
plt.axis([0, 50, 0, 27000])

xxx = list(range(0, 51, 10))
xxx.append(27)
xxx.sort()

plt.xticks(xxx)
plt.xlabel('day')
plt.ylabel('nums')
plt.legend(loc='best')
plt.show()
 