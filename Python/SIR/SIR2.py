import matplotlib.pyplot as plt
import numpy as np
import math

'''
SIR模型

S susceptible		易感者
I infected 			感染者
R recovered/removed	痊愈者

ds/dt = -beta * i * s
di/dt = beta * i * s - gamma * i
dr/dt = gamma * i

'''

beta = 0.35/10**7
gamma = 0.01

S = 10**7  
I = 10     # 感染者
R = 0      # 痊愈者

dT = 1
T = 0
day = 0

# print("Day %3d: %d %d %d" % (day, S, I, R))

IList = [I]
dIList = [I]

print("totally infected | newly infected")

while T<35:
	if T>=21:
		beta = (0.18-(T-21)*0.014)/10**7

	dS_dT = -beta * I * S
	dI_dT = beta * I * S - gamma * I
	dR_dT = gamma * I

	dS = int(dS_dT * dT)
	dI = int(dI_dT * dT)
	dR = int(dR_dT * dT)
	# print("(%d %d %d)" % (dS, dI, dR))
	
	S += dS
	I += dI
	R += dR

	T += dT
	day += 1
	# print("Day %3d: %d %d %d" % (day, S, I, R))	
	print("%15d %15d %d" % (I, dI, dR))

	IList.append(I)
	dIList.append(dI)


plt.plot(range(1, len(IList)+1), IList, 'r')
plt.plot(range(1, len(dIList)+1), dIList, 'g')
# plt.show()



# logI = [math.log(x) for x in IList]

# plt.plot(range(1, len(logI)+1), logI, 'r')
# plt.show()


# wuhan 1.10 - 2.6 共29天数据
data = '''0 0
0 0
0 0
0 0
0 0
0 0
0 0
0 0
16 16
21 5
65 44
127 62
281 154
558 264
923 365
1321 398
1801 480
2420 619
3124 704
3886 762
4638 755
5306 669
6028 726
6916 890
7646 731
8353 707
8999 696
9645 558'''


Totally = [0, 0]
Newly = [0, 0]

datas = data.split('\n')
for line in datas:
	line = list(map(int, line.split(' ')))
	Totally.append(line[0])
	Newly.append(line[1])



plt.plot(range(1, len(Totally)+1), Totally, 'or', label="Totally confirmedNum")
plt.plot(range(1, len(Newly)+1), Newly, 'og', label="Newly confirmedNum")
plt.axis([0, 40, 0, 15000])
plt.legend()
plt.show()
 