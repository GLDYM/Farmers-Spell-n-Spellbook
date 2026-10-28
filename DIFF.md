# 1.21.1 与 1.20.1 差异

## 实际行为差异

|  | 1.21.1 | 1.20.1 |
|---|---|---|
| 厨锅经验 | 累计经验，取走结果/配方结算时才生成经验 | 每次烹饪完成立即生成经验 |
| 学派套装 | 耐久倍率为 37，附魔点为 20 | 耐久倍率为 38，附魔点为 15 |
| 食物容器 | hogskin_sausage、wine_beef_stew、wine_rice、cinderous_hotpot 食用后返还碗 | 食用后不返还碗 |
| 镰刀 | 要求 age 达到最大的 BushBlock                                | 所有 BushBlock 都视为可收割 |
| 流体 | 真实注册 | 虚拟 NoopFluid |
| 神出鬼没黑名单 | 继承 #c:bosses | 只显式列出原版四个 Boss 实体 |
| 创造模式物品栏 | 类航空学 | 普通 |

## ID 差异

- farmers_spell:butter_beer ↔ farmers_spell:butterbeer
- farmers_spell:upgrade_orb_gluttony ↔ farmers_spell:gluttony_upgrade_orb

## 相同行为不同实现

|  | 1.21.1 | 1.20.1 |
|---|---|---|
| Curios 逻辑 | event 触发 | tick 检测 |
| 模型高亮 | 使用 neoforge 特性，在模型文件定义 | 通过代码实现 |
| 方块食物掉落 | 战利品表 | 代码硬编码 |
| 智慧木橱柜 | 独立实现继承 CabinetBlock | CabinetBlock |

## 独有特性

| 1.21.1 | 1.20.1 |
|---|---|
| 平底锅法术（WIP） |  |
| EMI 兼容 |  |
| Botany Pots 兼容 |  |