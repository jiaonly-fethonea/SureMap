# Project Structure / 项目目录结构

```
SureMap/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml                  # 应用配置，包名 com.sure.mapnote
│       │                                        # App config, package com.sure.mapnote
│       ├── java/com/sure/mapnote/               # Android Java 层（Activity / 数据 / 存储）
│       │                                        # Android Java layer (Activity / data / storage)
│       │   ├── BaseActivity.java                # Activity 基类
│       │   ├── MainActivity.java                # 入口 / 项目列表
│       │   ├── CreateProjectActivity.java       # 新建项目
│       │   ├── MapActivity.java                 # 地图界面（WebView + 桥接）
│       │   ├── RepositoryActivity.java          # 项目仓库
│       │   ├── ImportExportActivity.java        # 导入 / 导出
│       │   ├── OfficialActivity.java            # 官方说明页
│       │   ├── Project.java                     # 数据模型（标记 / 区域）
│       │   ├── RepoStore.java                   # 本地存储
│       │   ├── Prefs.java                       # SharedPreferences
│       │   ├── Lang.java                        # 多语言文案
│       │   └── Util.java                        # 工具类
│       ├── assets/
│       │   ├── map.html                         # 地图界面核心（Leaflet + 交互逻辑）
│       │   │                                    # Core map UI (Leaflet + interaction logic)
│       │   ├── leaflet/                         # Leaflet 地图库（第三方）
│       │   │                                    # Leaflet map library (third-party)
│       │   ├── regions/                         # 行政区划 GeoJSON 数据
│       │   │                                    # Administrative division GeoJSON data
│       │   │   ├── world-countries.geojson
│       │   │   ├── china-provinces.geojson
│       │   │   ├── japan-prefectures.geojson
│       │   │   └── usa-states.geojson
│       │   └── official.txt                     # 官方说明文本
│       └── res/                                 # 图标 / 主题 / 字符串 / 颜色
│                                                # Icons / themes / strings / colors
│           ├── drawable/                        # 按钮 / 卡片背景
│           ├── mipmap-*/                        # 应用图标
│           └── values/                          # colors / strings / styles
├── build.gradle / settings.gradle               # Gradle 构建配置
│                                                # Gradle build configuration
├── gradle.properties
├── CHANGELOG.md                                 # 变更记录
├── LICENSE                                      # MIT License
└── README.md                                    # 项目说明
```
