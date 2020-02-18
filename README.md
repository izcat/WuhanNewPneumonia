# WuhanNewPneumonia


美赛数模第三次培训的题目——新型冠状病毒基础研究
我们组考虑选择的角度为新型冠状病毒的传染病仿真和动态模拟研究。

宅在家做题也是为社会做贡献:)

传染病传播的模拟代码参考了 [lispczz](github.com/lispczz/pneumonia) 所写的 Java仿真程序。

## 主要工作
 - 在其基础上修改了部分bug
 - 新增了治愈病人的处理逻辑
 - UI界面更具有交互性

## 仿真结论
 - 在传播初期，潜在的感染者数量远高于实际确诊患者
 - 戴口罩等降低传播率的防治手段能有效阻止病毒传播
 - 封城等交通管制措施能降低传播速度，但无法遏止病毒在城市内扩散


## 建模队友计算特征重要度

------------

模拟界面 ![Simulation](https://github.com/izcat/WuhanNewPneumonia/blob/master/picture/Simulation.png)
增长趋势图 ![figure](https://github.com/izcat/WuhanNewPneumonia/blob/master/picture/Figure.png)


以上所有工作在1.11号完结，目前一周过去了，当前的数据已经远高于我们的预期 O.O

粗糙的SIR模型预测 ![predict](https://github.com/izcat/WuhanNewPneumonia/blob/master/picture/2.2%E5%8F%B7%E6%8B%9F%E5%90%88%E5%90%8E.png)


希望早日战胜疫情，如上图2.22号左右到达峰值吧 :) 

