import matplotlib.pyplot as plt
import numpy as np
# generate different normal distributions
x = np.arange(-10, 11, 1)  #形成一个数组，第三个参数表示步长，#start,end,step
y = x ** 2

plt.plot(x, y)

# 第一个参数是x轴坐标
# 第二个参数是y轴坐标
# 第三个参数是要显式的内容
# alpha 设置字体的透明度
# family 设置字体
# size 设置字体的大小
# style 设置字体的风格
# wight 字体的粗细
# bbox 给字体添加框，alpha 设置框体的透明度， facecolor 设置框体的颜色
plt.text(-3, 20, "function: y = x * x", size = 15, alpha = 0.2)
plt.text(-3, 40, "function: y = x * x", size = 15,\
         family = "fantasy", color = "r", style = "italic", weight = "light",\
         bbox = dict(facecolor = "r", alpha = 0.2))

plt.show()
